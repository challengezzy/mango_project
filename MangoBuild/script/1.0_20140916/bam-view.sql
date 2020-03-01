--BAM库视图脚本
create or replace view v_bam_businessview as
select b.id,
       b.name,
       b.code,
       b.description,
       b.ispersistviewdata,
       b.streamwindowname,
       b.streamname,
       b.streammodulename,
       p.providername,
       p.source,
       p.status,
       p.creator 
from 
       pub_cep_streammodule p,
       bam_businessview b 
where b.streammodulename = p.name 
/
create or replace view v_bam_dashboard as
select bd.id,
       bd.folderid,
       bd.name,
       bd.code,
       bd.description,
       bd.refreshinterval,
       bd.layout_mtcode,
       pmt.content,
       pmt.type mttype,
       bd.seq
from bam_dashboard bd,
     pub_metadata_templet pmt
where bd.layout_mtcode = pmt.code(+);
/
CREATE OR REPLACE VIEW V_BAM_DASHBOARD_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '1' TYPE,
       '仪表盘目录' TYPECN,
       BF.DESCRIPTION,
       '' MTCODE,
       1 REFRESHINTERVAL,
       1 SEQ
FROM BAM_FOLDER BF WHERE BF.TYPE=1
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'DASHBOARD' TYPE,
     '仪表盘' TYPECN,
     BB.DESCRIPTION,
     BB.LAYOUT_MTCODE MTCODE,
     BB.REFRESHINTERVAL,
     BB.SEQ
     FROM Bam_Dashboard BB;
/
create or replace view v_bam_dashboardobject as
select bd.id,
       bd.folderid,
       bd.name,
       bd.code,
       bd.description,
       bd.type,
       bd.mtcode,
       pmt.content
from bam_dashboardobject bd,
     pub_metadata_templet pmt
where bd.mtcode = pmt.code(+)
/
CREATE OR REPLACE VIEW V_BAM_DASHBOARDOBJECT_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '2' TYPE,
       '仪表盘对象目录' TYPECN,
       BF.DESCRIPTION,
       '' mtcode
FROM BAM_FOLDER BF WHERE BF.TYPE=2
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'DASHBOARDOBJECT' TYPE,
     '仪表盘对象' TYPECN,
     BB.DESCRIPTION,
     BB.Mtcode mtcode
     FROM Bam_Dashboardobject BB;
/
create or replace view v_bam_folder_dbtree_parent as
select replace(ID,'F','') as ID,NAME,code,replace(parentid,'F','') as parentid from V_BAM_DASHBOARD_TREE where type='1';
/
create or replace view v_bam_folder_dotree_parent as
select replace(ID,'F','') as ID,NAME,code,replace(parentid,'F','') as parentid from V_BAM_DASHBOARDOBJECT_TREE where type='2';
/

create or replace view v_bam_alertmessage_newest as
select t.alertid, max(t.activatetime) as activatetime from bam_alertmessage t group by alertid;
/

create or replace view v_bam_alertmessage_affirm as
select m.ID,m.ALERTID,m.SUBJECT,m.SEVERITY,m.BODY,m.ACTIVATETIME,m.STATUS
  from bam_alertmessage m, bam_alert a, bam_rule r
 where m.alertid = a.id
   and a.id = r.actionalertid
   and r.actiontype = 0
   and a.status = 1
   and m.activatetime in (select v.activatetime from v_bam_alertmessage_newest v)
   and m.alertid in (select v.alertid from v_bam_alertmessage_newest v);
/
CREATE OR REPLACE VIEW v_bam_accountsettings AS
SELECT BA.ID, BA.USERNAME, BA.MTCODE, MT.CONTENT
  FROM BAM_ACCOUNTSETTINGS BA, PUB_METADATA_TEMPLET MT
 WHERE BA.MTCODE = MT.CODE(+);
/
create or replace view v_bam_alertmessage as
select "ID","ALERTID","SUBJECT","SEVERITY","MSG","ACTIVATETIME","STATUS","USERNAME","AFFIRM" from (
select v.ID,v.ALERTID,v.SUBJECT,v.SEVERITY,to_char(v.BODY) msg,v.ACTIVATETIME,v.STATUS,mb.username,
       '0' as affirm
  from v_bam_alertmessage_affirm v,bam_messagesubscriber mb where v.ID = mb.alertmessageid
union
select t.ID,t.ALERTID,t.SUBJECT,t.SEVERITY,to_char(t.BODY) msg,t.ACTIVATETIME,t.STATUS,bm.username,
       '1' as affirm
  from bam_alertmessage t,bam_messagesubscriber bm
 where t.id = bm.alertmessageid and t.id not in(select vb.id from v_bam_alertmessage_affirm vb)) order by ACTIVATETIME desc;
/
CREATE OR REPLACE VIEW V_BAM_ALERTMESSAGE_DASHBD AS
SELECT SUBJECT,SEVERITY,to_char(ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') ACTIVATETIME,USERNAME,AFFIRM,RN
  FROM (SELECT V.SUBJECT,
               DECODE(V.SEVERITY, 0, '高', 1, '一般', 2, '底') AS SEVERITY,
               V.ACTIVATETIME,USERNAME,
               DECODE(V.AFFIRM, '0', '是', '1', '否') AS AFFIRM,
               ROWNUM AS RN
          FROM V_BAM_ALERTMESSAGE V)
 WHERE 1 = 1;
/
CREATE OR REPLACE VIEW v_bam_businessscenario_tree AS
SELECT 'F'||BF.ID AS ID ,
       BF.ID PID,
       DECODE(BF.PARENTID,NULL,NULL,'F'||BF.PARENTID) AS PARENTID,
       NAME,
       CODE,
       '0' TYPE,
       '业务场景目录' TYPECN,
       BF.DESCRIPTION
FROM BAM_FOLDER BF WHERE BF.TYPE=0
UNION
SELECT
     'S'||BB.ID AS ID,
     BB.ID PID,
     'F'||BB.FOLDERID AS PARENTID,
     NAME,
     CODE,
     '1' TYPE,
     '业务场景对像' TYPECN,
     BB.DESCRIPTION
     FROM BAM_BUSINESSSCENARIO BB;
/
create or replace view v_bam_folder_businessactivity as
select
   "ID","PARENTID","NAME","CODE","DESCRIPTION","TYPE"
from
   BAM_FOLDER
where
   type=0
with read only;
/
create or replace view v_bam_folder_dashboard as
select
   "ID","PARENTID","NAME","CODE","DESCRIPTION","TYPE"
from
   BAM_FOLDER
where
   type=1
with read only;
/
create or replace view v_bam_folder_parent as
select replace(ID,'F','') as ID,replace(PARENTID,'F','') as PARENTID,CODE as CODE,NAME as NAME from V_BAM_BUSINESSSCENARIO_TREE where 1=1 and TYPE='0';
/
CREATE OR REPLACE VIEW v_bam_rule AS
SELECT BR."ID",BR."BUSINESSSCENARIOID",BR."NAME",BR."CODE",BR."DESCRIPTION",BR."CONDITION",BR."HOLDSFOR",BR."ACTIONALERTID",BR."ACTIONTYPE",BR."STREAMMODULENAME",PCS.STATUS FROM BAM_RULE BR,PUB_CEP_STREAMMODULE PCS WHERE LOWER('RULE')||'_'||BR.CODE = PCS.NAME;
/
CREATE OR REPLACE VIEW v_bam_subsciber AS
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_ROLE PR, PUB_USER PU, PUB_USER_ROLE PUR, BAM_SUBSCIBER BS
 WHERE PR.ID = PUR.ROLEID
   AND PU.ID = PUR.USERID
   AND BS.OBJECTNAME = PR.NAME
   AND BS.TYPE = 1
UNION
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_WORKPOSITION      PW,
       PUB_USER              PU,
       PUB_USER_WORKPOSITION PUW,
       BAM_SUBSCIBER         BS
 WHERE PW.ID = PUW.WORKPOSITIONID
   AND PU.ID = PUW.USERID
   AND BS.OBJECTNAME = PW.NAME
   AND BS.TYPE = 2
UNION
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_USER PU, BAM_SUBSCIBER BS
 WHERE PU.NAME = BS.OBJECTNAME
   AND BS.TYPE = 0;
/
create or replace view v_bam_alertmessage_dashbd as
select v.ID,
       v.ALERTID,
       v.SUBJECT,
       DECODE(v.SEVERITY, 0, '高', 1, '一般', 2, '低') AS SEVERITY,
       to_char(v.BODY) msg,
       v.ACTIVATETIME as createtime,
       to_char(v.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') as ACTIVATETIME,
       v.STATUS,
       mb.username,
       '是' as affirm
  from v_bam_alertmessage_affirm v, bam_messagesubscriber mb
 where v.ID = mb.alertmessageid
union
select t.ID,
       t.ALERTID,
       t.SUBJECT,
       DECODE(t.SEVERITY, 0, '高', 1, '一般', 2, '低') AS SEVERITY,
       to_char(t.BODY) msg,
       t.ACTIVATETIME as createtime,
       to_char(t.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') as ACTIVATETIME,
       t.STATUS,
       bm.username,
       '否' as affirm
  from bam_alertmessage t, bam_messagesubscriber bm
 where t.id = bm.alertmessageid
   and t.id not in (select vb.id from v_bam_alertmessage_affirm vb);
/
CREATE OR REPLACE VIEW V_BAM_DASHBOARD_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '1' TYPE,
       '仪表盘目录' TYPECN,
       BF.DESCRIPTION,
       '' MTCODE,
       1 REFRESHINTERVAL,
       BF.Seq SEQ
FROM BAM_FOLDER BF WHERE BF.TYPE=1
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'DASHBOARD' TYPE,
     '仪表盘' TYPECN,
     BB.DESCRIPTION,
     BB.LAYOUT_MTCODE MTCODE,
     BB.REFRESHINTERVAL,
     BB.SEQ
     FROM Bam_Dashboard BB;
/
create or replace view v_bam_alertmessage_dashbd as
select v.ID,
       v.ALERTID,
       v.SUBJECT,
       DECODE(v.SEVERITY, 0, '高', 1, '一般', 2, '底') AS SEVERITY,
       to_char(v.BODY) msg,
       v.ACTIVATETIME as createtime,
       to_char(v.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') as ACTIVATETIME,
       v.STATUS,
       mb.username,
       '是' as affirm
  from v_bam_alertmessage_affirm v, bam_messagesubscriber mb
 where v.ID = mb.alertmessageid
union
select t.ID,
       t.ALERTID,
       t.SUBJECT,
       DECODE(t.SEVERITY, 0, '高', 1, '一般', 2, '低') AS SEVERITY,
       to_char(t.BODY) msg,
       t.ACTIVATETIME as createtime,
       to_char(t.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') as ACTIVATETIME,
       t.STATUS,
       bm.username,
       '否' as affirm
  from bam_alertmessage t, bam_messagesubscriber bm
 where t.id = bm.alertmessageid
   and t.id not in (select vb.id from v_bam_alertmessage_affirm vb);
/

CREATE OR REPLACE VIEW V_BAM_TASK AS
SELECT
 --create by zhangzy 2011/6/15 最新任务列表信息
 ID,
 SUBJECT,
 MEMO,
 OWNER USERNAME,
 DECODE(T.SERVERITY, 0, '高', 1, '一般', 2, '低') AS SERVERITY,
 DECODE(T.STATE,0,'未完成',1,'已完成') as STATE,
 DEADLINE,
 CREATEDATE,
 LASTUPDATED,
 ORIGINSOURCE,
 ORIGINCONTENT,
 ALERTMESSAGEID
FROM BAM_TASK T WHERE T.STATE = 0;
/
create or replace view v_bam_ruletemplateparaset as
select ID,NAME,TYPE,DESCRIPTION,CAPTION,RULETEMPLATEID,'' as VALUE from BAM_RULETEMPLATEPARAMETER;
/
create or replace view v_bam_ruletemplate_c as
select "ID","BUSINESSSCENARIOID","NAME","CODE","DESCRIPTION","CONDITION","HOLDSFOR","RESETCONDITION","RESETHOLDSFOR","SEVERITY","SUBJECT","BODY","SHOWCONDITION" from bam_ruletemplate;
/
create or replace view v_bam_rule_businessrule as
select t.ID,
       t.BUSINESSSCENARIOID,
       t.NAME,
       t.CODE,
       t.DESCRIPTION,
       t.CONDITION,
       t.HOLDSFOR,
       t.ACTIONALERTID,
       t.ACTIONTYPE,
       t.STREAMMODULENAME,
       t.BUSINESSRULEID,
       br.RULETEMPATEID,
       PCS.STATUS
  from BAM_RULE T, BAM_BUSINESSRULE BR,PUB_CEP_STREAMMODULE PCS
 where T.BUSINESSRULEID = BR.ID and LOWER('RULE')||'_'||T.CODE = PCS.NAME order by id desc;
/

create or replace procedure P_BAM_ARCHIVE_ALERTMSG(alertmessage_id In number) is
begin

  Insert Into BAM_MESSAGESUBSCRIBER_ARCH Select t.* From BAM_MESSAGESUBSCRIBER t Where t.alertmessageid=alertmessage_id;
  Delete From BAM_MESSAGESUBSCRIBER Where alertmessageid=alertmessage_id;
  Insert Into Bam_Alertmessage_Arch  Select t.* From BAM_ALERTMESSAGE t Where t.id=alertmessage_id;
  Delete From BAM_ALERTMESSAGE  Where id=alertmessage_id;
  Commit;
  
end P_BAM_ARCHIVE_ALERTMSG;
/

create or replace procedure P_BAM_ARCHIVEALL_ALERTMSG is
cur Sys_Refcursor;
v_alertmsg_id Number;
begin
  Open cur For select BM.ALERTMESSAGEID as ID from BAM_MESSAGESUBSCRIBER BM, BAM_ALERTMESSAGE BA where bm.alertmessageid = ba.id and ba.activatetime <= sysdate-5;
  While True
  Loop
      Fetch cur Into v_alertmsg_id;
      Exit When cur%Notfound;
      P_BAM_ARCHIVE_ALERTMSG(v_alertmsg_id);
  End Loop;
  Close cur;
end P_BAM_ARCHIVEALL_ALERTMSG;
/

create or replace procedure P_BAM_ARCHIVE_ALERTMSG(alertmessage_id In number) is
begin

  Insert Into BAM_MESSAGESUBSCRIBER_ARCH Select t.*,sysdate From BAM_MESSAGESUBSCRIBER t Where t.alertmessageid=alertmessage_id;
  Delete From BAM_MESSAGESUBSCRIBER Where alertmessageid=alertmessage_id;
  Insert Into Bam_Alertmessage_Arch  Select t.*,sysdate From BAM_ALERTMESSAGE t Where t.id=alertmessage_id;
  Delete From BAM_ALERTMESSAGE  Where id=alertmessage_id;
  Commit;

end P_BAM_ARCHIVE_ALERTMSG;
/

CREATE OR REPLACE VIEW V_BAM_SUBSCIBER AS
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_ROLE PR, PUB_USER PU, PUB_USER_ROLE PUR, BAM_SUBSCIBER BS
 WHERE PR.ID = PUR.ROLEID
   AND PU.ID = PUR.USERID
   AND BS.OBJECTNAME = PR.NAME
   AND BS.TYPE = 1
UNION
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_WORKPOSITION      PW,
       PUB_USER              PU,
       PUB_USER_WORKPOSITION PUW,
       BAM_SUBSCIBER         BS
 WHERE PW.ID = PUW.WORKPOSITIONID
   AND PU.ID = PUW.USERID
   AND BS.OBJECTNAME = PW.NAME
   AND BS.TYPE = 2
UNION
SELECT BS.OBJECTNAME, BS.DELIVERYTYPE, BS.ALERTID, PU.EMAIL
  FROM PUB_USER PU, BAM_SUBSCIBER BS
 WHERE PU.LOGINNAME = BS.OBJECTNAME
   AND BS.TYPE = 0;
/
create or replace view v_alertmessage_affirm_id as
select bm.alertid, max(bm.id) as id
  from bam_alertmessage bm, bam_alert ba, bam_rule br
 where bm.alertid = ba.id
   and ba.id = br.actionalertid
   and br.actiontype = 0
 group by bm.alertid;
 
create or replace view v_alertmessage_affirm as
select m.ID,m.ALERTID,m.SUBJECT,m.SEVERITY,to_char(m.BODY) msg,m.ACTIVATETIME,m.STATUS
  from bam_alertmessage m, v_alertmessage_affirm_id va
 where m.id=va.id order by id desc;
 
create or replace view v_alertmessage_unaffirm_id as
select distinct id from (
  select rank() over(partition by m.alertid order by m.id desc) rk,
         m.id,ba.id as alertid,br.actiontype
  from bam_alertmessage m, bam_alert ba, bam_rule br
 where m.alertid = ba.id
   and ba.id = br.actionalertid
 ) t where t.rk >=2 or actiontype=1;
 
create or replace view v_alertmessage_unaffirm as
select m.ID,m.ALERTID,m.SUBJECT,m.SEVERITY,to_char(m.BODY) msg,m.ACTIVATETIME,m.STATUS
  from bam_alertmessage m, v_alertmessage_unaffirm_id va
 where m.id=va.id order by id desc;

create or replace view v_bam_alertmessage as
select "ID","ALERTID","SUBJECT","SEVERITY","MSG","ACTIVATETIME","STATUS","USERNAME","AFFIRM" from (
select v.ID,v.ALERTID,v.SUBJECT,v.SEVERITY,v.msg,v.ACTIVATETIME,v.STATUS,mb.username,
       '0' as affirm
  from v_alertmessage_affirm v,bam_messagesubscriber mb where v.ID = mb.alertmessageid
union
select t.ID,t.ALERTID,t.SUBJECT,t.SEVERITY,t.msg,t.ACTIVATETIME,t.STATUS,bm.username,
       '1' as affirm
  from v_alertmessage_unaffirm t,bam_messagesubscriber bm
 where t.id = bm.alertmessageid ) order by ID desc;

create or replace view v_bam_alertmessage_dashbd as
select v.ID,
       v.ALERTID,
       v.SUBJECT,
       DECODE(v.SEVERITY, 0, '高', 1, '一般', 2, '底') AS SEVERITY,
       v.msg,
       v.ACTIVATETIME as createtime,
       to_char(v.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') as ACTIVATETIME,
       v.STATUS,
       v.username,
       DECODE(v.affirm, 0, '是', 1, '否') as affirm
  from v_bam_alertmessage v;
/
---------------------------------------------------------------------
--------------zhangzy 2011/7/14 1.2版本整理添加 begin--------------------------
create or replace view v_bam_taskemail as
select --查找任务需要发送的邮件
      w.taskid,w.username,u.email
 from bam_taskwatcher w,pub_user u
where u.name = w.username
union
select t.id taskid,t.owner username,u.email
 from bam_task t,pub_user u
where u.name = t.owner;
/
--------------zhangzy 2011/7/14 1.2版本整理添加 end --------------------------

--------------zhangzy 2011/7/18 1.2版本整理添加 begin--------------------------
create or replace view v_bam_businessview as
select b.id,
       b.name,
       b.code,
       b.description,
       b.ispersistviewdata,
       b.streamwindowname,
       b.streamname,
       b.streammodulename,
       p.providername,
       p.source,
       p.status,
       p.creator,
       b.metadata,
       b.type
from
       pub_cep_streammodule p,
       bam_businessview b
where b.streammodulename = p.name;
--------------zhangzy 2011/7/18 1.2版本整理添加 end --------------------------

--file for BAM-161 add by caohenghui --start
CREATE OR REPLACE VIEW V_BAM_BUSINESSVIEW_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '3' TYPE,
       '业务视图目录' TYPECN,
       BF.DESCRIPTION
FROM BAM_FOLDER BF WHERE BF.TYPE=3
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'BUSINESSVIEW' TYPE,
     '业务视图' TYPECN,
     BB.DESCRIPTION
     FROM BAM_BUSINESSVIEW BB;
/
create or replace view v_bam_businessview as
select b.id,
       b.name,
       b.code,
       b.description,
       b.ispersistviewdata,
       b.streamwindowname,
       b.streamname,
       b.streammodulename,
       p.providername,
       p.source,
       p.status,
       p.creator,
       b.metadata,
       b.type,
       b.folderid
from
       pub_cep_streammodule p,
       bam_businessview b
where b.streammodulename = p.name;
/

create or replace procedure P_BAM_ARCHIVEALL_ALERTMSG is
cur Sys_Refcursor;
v_alertmsg_id Number;
begin
  Open cur For select BA.ID as ID from BAM_ALERTMESSAGE BA where ba.activatetime <= sysdate-5;
  While True
  Loop
      Fetch cur Into v_alertmsg_id;
      Exit When cur%Notfound;
      P_BAM_ARCHIVE_ALERTMSG(v_alertmsg_id);
  End Loop;
  Close cur;
end P_BAM_ARCHIVEALL_ALERTMSG;
/
--file for BAM-161 add by caohenghui --end

--告警消息查询在消息数据量过大时速度非常慢，从而导致连接池溢出,修改原有查询方式
CREATE OR REPLACE VIEW V_BAM_ALERTMESSAGE_DASHBD AS
SELECT --CREATE BY ZHANGZY 2011/8/1
       M.ID,
       M.ALERTID,
       M.SUBJECT,
       DECODE(M.SEVERITY, 0, '高', 1, '一般', 2, '底') SEVERITY,
       M.BODY MSG,
       TO_CHAR(M.ACTIVATETIME,'yyyy-MM-dd hh24:mi:ss') ACTIVATETIME,
       DECODE(M.STATUS,0,'激活',1,'忽略') STATUS,
       CASE WHEN A.STATUS=1 AND EXISTS (SELECT R.ACTIONALERTID FROM BAM_RULE R WHERE R.ACTIONALERTID=A.ID AND R.ACTIONTYPE=0)
         THEN '是' ELSE '否' END AS AFFIRM,--是否需要确认 1-是，0-否
       MS.USERNAME
  FROM BAM_ALERTMESSAGE M, BAM_MESSAGESUBSCRIBER MS,BAM_ALERT A,BAM_RULE R
 WHERE M.ID = MS.ALERTMESSAGEID AND A.ID=M.ALERTID AND R.ACTIONALERTID=M.ALERTID;
/

CREATE OR REPLACE VIEW V_BAM_MESSAGEMNG AS
SELECT --CREATE BY ZHANGZY 2011/8/1
       M.ID,
       M.ALERTID,
       M.SUBJECT,
       M.SEVERITY,
       M.BODY MSG,
       M.ACTIVATETIME,
       M.STATUS,
       CASE WHEN A.STATUS=1 AND EXISTS (SELECT R.ACTIONALERTID FROM BAM_RULE R WHERE R.ACTIONALERTID=A.ID AND R.ACTIONTYPE=0)
       THEN '1' ELSE '0' END AS AFFIRM,--是否需要确认 1-是，0-否
       MS.USERNAME
  FROM BAM_ALERTMESSAGE M, BAM_MESSAGESUBSCRIBER MS,BAM_ALERT A
 WHERE M.ID = MS.ALERTMESSAGEID AND A.ID=M.ALERTID ORDER BY M.ID DESC;
/

CREATE OR REPLACE VIEW V_BAM_BUSINESSVIEW_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '3' TYPE,
       '业务视图目录' TYPECN,
       BF.DESCRIPTION,
       '' STREAMMODULENAME,
       '' STREAMWINDOWNAME,
       '' PROVIDERNAME,
       -1 STATUS
FROM BAM_FOLDER BF WHERE BF.TYPE=3
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'BUSINESSVIEW' TYPE,
     '业务视图' TYPECN,
     BB.DESCRIPTION,
     BB.STREAMMODULENAME,
     BB.STREAMWINDOWNAME,
     BB.PROVIDERNAME,
     BB.STATUS
     FROM V_BAM_BUSINESSVIEW BB;
/

------------------add by zhangzy 20110810 begin ----------
CREATE OR REPLACE VIEW V_BAM_TASKMNG AS
SELECT --create by zhangzy 20110809 任务管理视图
       T.ID,T.SUBJECT,T.MEMO,T.OWNER,T.SERVERITY,T.STATE,
       T.DEADLINE,T.CREATEDATE,T.LASTUPDATED,
       T.TYPEID,
       T.ORIGINSOURCE,T.ORIGINCONTENT,T.ALERTMESSAGEID,W.USERNAME WATCHERNAME
FROM BAM_TASK T,BAM_TASKWATCHER W
 WHERE T.ID=W.TASKID(+);
/
------------------add by zhangzy 20110810 end ----------

------------------add by zhangzy 20110822 begin ----------
CREATE OR REPLACE VIEW V_BAM_ANALYZEVIEW_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '5' TYPE,
       '分析视图目录' TYPECN,
       BF.DESCRIPTION METADATA
FROM BAM_FOLDER BF WHERE BF.TYPE=5
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'ANALYZEVIEW' TYPE,
     '分析视图' TYPECN,
     to_char(BB.METADATA) METADATA
     FROM BAM_ANALYZEVIEW BB;
/
CREATE OR REPLACE VIEW V_BAM_ANALYZEVIEW AS
SELECT ID,NAME,CODE,METADATA,FOLDERID FROM BAM_ANALYZEVIEW;
/
create or replace view V_BAM_QUERYVIEW_REF as
select code as id,to_char(sql)||';'||datasourcename code,name from BAM_QUERYVIEW;
/

CREATE OR REPLACE VIEW V_BAM_DASHBOARD_AUTHTREE AS
SELECT --create by zhangzy 2011/8/22仪表盘权限管理视图
       'F'||BF.ID AS ID ,
       bf.id PID,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       NAME,
       CODE,
       '仪表盘目录' TYPECN,
       1 SEQ,
       'Y' ISFOLDER
FROM BAM_FOLDER BF WHERE BF.TYPE=1
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id PID,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     NAME,
     CODE,
     '仪表盘' TYPECN,
     BB.SEQ,
     'N' ISFOLDER
     FROM Bam_Dashboard BB;
/

CREATE OR REPLACE VIEW V_BAM_DASHBOARDTREE_USER AS
SELECT 
  --create by zhangzy 2011/8/22 仪表盘目录视图，根据USERID进行过滤
  D.PID,UD.USERID FROM BAM_USER_DASHBOARD UD,V_BAM_DASHBOARD_AUTHTREE D WHERE D.PID=UD.DASHBOARDID
UNION
SELECT D.PID,UR.USERID FROM BAM_ROLE_DASHBOARD RD,PUB_USER_ROLE UR,V_BAM_DASHBOARD_AUTHTREE D
 WHERE D.PID=RD.DASHBOARDID AND RD.ROLEID=UR.ROLEID
UNION
SELECT D.PID,UW.USERID FROM BAM_WORKPOSITION_DASHBOARD WD,PUB_USER_WORKPOSITION UW,V_BAM_DASHBOARD_AUTHTREE D
 WHERE D.PID=WD.DASHBOARDID AND WD.WORKPOSITIONID=UW.WORKPOSITIONID;
/
------------------add by zhangzy 20110822 END ----------

create or replace procedure p_archive_task(v_taskid In number) is
begin
  /**
    author:zhangzy
    date:2011/6/15
    description: 归档一个任务相关信息
  */
  insert into bam_taskwatcher_arch(id,taskid,username,archivetime)
     select t.id,t.taskid,t.username,sysdate archivetime
     from bam_taskwatcher t where t.taskid = v_taskid;

  delete from bam_taskwatcher tw where tw.taskid = v_taskid;

  insert into bam_task_arch(id,subject,memo,owner,serverity,state,deadline,createdate,lastupdated
             ,originsource,origincontent,alertmessageid,archivetime)
    select id,subject,memo,owner,serverity,state,deadline,createdate,lastupdated
             ,originsource,origincontent,alertmessageid,sysdate archivetime
    from bam_task t where t.id = v_taskid;
  delete from bam_task t where t.id = v_taskid;

  commit;
end p_archive_task;
/

create or replace procedure p_archiveall_task is
  cur      Sys_Refcursor;
  v_taskid Number;
begin
  /**
    author:zhangzy
    date:2011/6/15
    description: 归档所有任务相关信息
  */
  Open cur For
    Select id From bam_task Where state = 1;
  While True Loop
    Fetch cur
      Into v_taskid;
    Exit When cur%Notfound;
    p_archive_task(v_taskid);

  End Loop;
  Close cur;
end p_archiveall_task;
/
--------------------------20110914 add by zhangzy------------------------------
CREATE OR REPLACE VIEW V_BAM_RULETEMPLATE_C AS
SELECT  BRP.ID,
        BRP.BUSINESSSCENARIOID,
        BRP.NAME,
        BRP.CODE,
        BRP.DESCRIPTION,
        BRP.CONDITION,
        BRP.HOLDSFOR,
        BRP.RESETCONDITION,
        BRP.RESETHOLDSFOR,
        BRP.SEVERITY,
        BRP.SUBJECT,
        BRP.BODY,
        BRP.SHOWCONDITION,
        BB.Name SCENARIONAME
FROM BAM_RULETEMPLATE BRP,BAM_BUSINESSSCENARIO BB WHERE BRP.BUSINESSSCENARIOID=BB.ID;
/
CREATE OR REPLACE VIEW V_BAM_QUERYVIEW AS
SELECT ID,NAME,CODE,DATASOURCENAME,SQL,FOLDERID,DESCRIPTION FROM BAM_QUERYVIEW;
/
CREATE OR REPLACE VIEW V_BAM_QUERYVIEW_TREE AS
SELECT 'F'||BF.ID AS ID ,
       bf.id pid,
       decode(BF.PARENTID,null,null,'F'||BF.PARENTID) AS PARENTID,
       name,
       code,
       '4' TYPE,
       '查询视图目录' TYPECN,
       BF.DESCRIPTION
FROM BAM_FOLDER BF WHERE BF.TYPE=4
UNION
SELECT
     'D'||BB.ID AS ID,
     bb.id pid,
     decode(BB.FOLDERID,null,null,'F'||BB.FOLDERID) AS PARENTID,
     name,
     code,
     'QUERYVIEW' TYPE,
     '查询视图' TYPECN,
     BB.DESCRIPTION
     FROM BAM_QUERYVIEW BB;
/

CREATE OR REPLACE PROCEDURE PROC_DROPIFEXISTS(V_OBJNAME IN VARCHAR2) IS
OBJTYPE VARCHAR2(50);
OBJCOUNT NUMBER(8);
BEGIN
    ----判断当前用户下是否有对应的表
    SELECT COUNT(OBJECT_TYPE) INTO OBJCOUNT
    FROM USER_OBJECTS
    WHERE OBJECT_NAME = UPPER(V_OBJNAME);



    IF OBJCOUNT > 0 THEN
        SELECT OBJECT_TYPE INTO OBJTYPE
        FROM USER_OBJECTS
        WHERE OBJECT_NAME = UPPER(V_OBJNAME);

        ----如果存在则使用动态SQL DROP掉该表
        EXECUTE IMMEDIATE 'DROP '|| OBJTYPE || ' ' || V_OBJNAME;
    END IF;
END PROC_DROPIFEXISTS;
/
create or replace view v_bam_entitymodel as
select be.id, be.name, be.code, be.description, be.mtcode, p.content,p.type mttype
  from bam_entitymodel be, pub_metadata_templet p
 where be.mtcode = p.code(+);
 /
 
 -- Create sequence 
create sequence S_BAM_ENTITYMODEL_DETAILNAME
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
/
create or replace view v_bam_entitymodel as
select be.id, be.name, be.code, be.datasource,be.dwds dwds, be.description, be.mtcode, p.content,p.type mttype
  from bam_entitymodel be, pub_metadata_templet p
 where be.mtcode = p.code(+);
/