/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/5/5 13:36:10                            */
/*==============================================================*/


alter table SPDD_AREAEFFECTSTATS
   drop constraint FK_SPDD_ARE_REFERENCE_SPDD_CHA;

alter table SPDD_AREAEFFECTSTATS
   drop constraint FK_SPDD_ARE_REFERENCE_SPDD_TIM;

alter table SPDD_BUSINESSSTATS
   drop constraint FK_SPDD_BUS_REFERENCE_SPDD_CHA;

alter table SPDD_BUSINESSSTATS
   drop constraint FK_SPDD_BUS_REFERENCE_SPDD_TIM;

alter table SPDD_BUSINESSSTATS
   drop constraint FK_SPDD_BUS_REFERENCE_SPDD_PRO;

alter table SPDD_CHANNEL
   drop constraint FK_SPDD_CHA_REFERENCE_SPDD_CHA;

alter table SPDD_CHANNELCONTRACTINFO
   drop constraint FK_SPDD_CON_REFERENCE_SPDD_CHA;

alter table SPDD_CUSTRECEPTIONSTATS
   drop constraint FK_SPDD_CUS_REFERENCE_SPDD_CHA;

alter table SPDD_CUSTRECEPTIONSTATS
   drop constraint FK_SPDD_CUS_REFERENCE_SPDD_TIM;

alter table SPDD_CUSTSATISFACTIONSTATS
   drop constraint FK_SPDD_SAT_REFERENCE_SPDD_CHA;

alter table SPDD_CUSTSATISFACTIONSTATS
   drop constraint FK_SPDD_SAT_REFERENCE_SPDD_TIM;

alter table SPDD_EMPLOYEEINFO
   drop constraint FK_SPDD_EMP_REFERENCE_SPDD_CHA;

alter table SPDD_HUMANEFFECTSTATS
   drop constraint FK_SPDD_HUM_REFERENCE_SPDD_CHA;

alter table SPDD_HUMANEFFECTSTATS
   drop constraint FK_SPDD_HUM_REFERENCE_SPDD_TIM;

alter table SPDD_LOSTCUSTOMERSTATS
   drop constraint FK_SPDD_LOS_REFERENCE_SPDD_CHA;

alter table SPDD_LOSTCUSTOMERSTATS
   drop constraint FK_SPDD_LOS_REFERENCE_SPDD_TIM;

alter table SPDD_NEWCUSTOMERSTATS
   drop constraint FK_SPDD_NEW_REFERENCE_SPDD_CHA;

alter table SPDD_NEWCUSTOMERSTATS
   drop constraint FK_SPDD_NEW_REFERENCE_SPDD_TIM;

alter table SPDD_QUEUEUPTIMESTATS
   drop constraint FK_SPDD_QUE_REFERENCE_SPDD_CHA;

alter table SPDD_QUEUEUPTIMESTATS
   drop constraint FK_SPDD_QUE_REFERENCE_SPDD_TIM;

alter table SPDD_REVENUESTATS
   drop constraint FK_SPDD_REV_REFERENCE_SPDD_CHA;

alter table SPDD_REVENUESTATS
   drop constraint FK_SPDD_REV_REFERENCE_SPDD_TIM;

alter table SPDD_TOPBUSINESSSTATS
   drop constraint FK_SPDD_TOP_REFERENCE_SPDD_TIM;

alter table SPDD_TOPBUSINESSSTATS
   drop constraint FK_SPDD_TOP_REFERENCE_SPDD_CHA;

drop table SPDD_AREAEFFECTSTATS cascade constraints;

drop table SPDD_BUSINESSSTATS cascade constraints;

drop table SPDD_CHANNEL cascade constraints;

drop table SPDD_CHANNELCONTRACTINFO cascade constraints;

drop table SPDD_CUSTRECEPTIONSTATS cascade constraints;

drop table SPDD_CUSTSATISFACTIONSTATS cascade constraints;

drop table SPDD_EMPLOYEEINFO cascade constraints;

drop table SPDD_HUMANEFFECTSTATS cascade constraints;

drop table SPDD_LOSTCUSTOMERSTATS cascade constraints;

drop table SPDD_NEWCUSTOMERSTATS cascade constraints;

drop table SPDD_PRODUCTDIMENSION cascade constraints;

drop table SPDD_QUEUEUPTIMESTATS cascade constraints;

drop table SPDD_REVENUESTATS cascade constraints;

drop table SPDD_TIMEDIMENSION cascade constraints;

drop table SPDD_TOPBUSINESSSTATS cascade constraints;

drop sequence S_SPDD_AREAEFFECTSTATS;

drop sequence S_SPDD_BUSINESSSTATS;

drop sequence S_SPDD_CHANNEL;

drop sequence S_SPDD_CHANNELCONTRACTINFO;

drop sequence S_SPDD_CUSTRECEPTIONSTATS;

drop sequence S_SPDD_CUSTSATISFACTIONSTATS;

drop sequence S_SPDD_EMPLOYEEINFO;

drop sequence S_SPDD_HUMANEFFECTSTATS;

drop sequence S_SPDD_LOSTCUSTOMERSTATS;

drop sequence S_SPDD_NEWCUSTOMERSTATS;

drop sequence S_SPDD_PRODUCTDIMENSION;

drop sequence S_SPDD_QUEUEUPTIMESTATS;

drop sequence S_SPDD_REVENUESTATS;

drop sequence S_SPDD_TIMEDIMENSION;

drop sequence S_SPDD_TOPBUSINESSSTATS;

create sequence S_SPDD_AREAEFFECTSTATS;

create sequence S_SPDD_BUSINESSSTATS;

create sequence S_SPDD_CHANNEL;

create sequence S_SPDD_CHANNELCONTRACTINFO;

create sequence S_SPDD_CUSTRECEPTIONSTATS;

create sequence S_SPDD_CUSTSATISFACTIONSTATS;

create sequence S_SPDD_EMPLOYEEINFO;

create sequence S_SPDD_HUMANEFFECTSTATS;

create sequence S_SPDD_LOSTCUSTOMERSTATS;

create sequence S_SPDD_NEWCUSTOMERSTATS;

create sequence S_SPDD_PRODUCTDIMENSION;

create sequence S_SPDD_QUEUEUPTIMESTATS;

create sequence S_SPDD_REVENUESTATS;

create sequence S_SPDD_TIMEDIMENSION;

create sequence S_SPDD_TOPBUSINESSSTATS;

/*==============================================================*/
/* Table: SPDD_AREAEFFECTSTATS                                  */
/*==============================================================*/
create table SPDD_AREAEFFECTSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_AREAEFFECTSTATS primary key (ID)
);

comment on table SPDD_AREAEFFECTSTATS is
'坪效';

comment on column SPDD_AREAEFFECTSTATS.CHANNELID is
'渠道';

comment on column SPDD_AREAEFFECTSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_AREAEFFECTSTATS.STATSTIME is
'统计时间';

comment on column SPDD_AREAEFFECTSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_BUSINESSSTATS                                    */
/*==============================================================*/
create table SPDD_BUSINESSSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   PRODUCTDEIMENSIONID  NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_BUSINESSSTATS primary key (ID)
);

comment on table SPDD_BUSINESSSTATS is
'BOSS业务受理量';

comment on column SPDD_BUSINESSSTATS.CHANNELID is
'渠道';

comment on column SPDD_BUSINESSSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_BUSINESSSTATS.PRODUCTDEIMENSIONID is
'业务维度';

comment on column SPDD_BUSINESSSTATS.STATSTIME is
'统计时间';

comment on column SPDD_BUSINESSSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_CHANNEL                                          */
/*==============================================================*/
create table SPDD_CHANNEL  (
   ID                   NUMBER(18)                      not null,
   CATAGORY             NUMBER(11),
   CODE                 VARCHAR2(100),
   PARENTID             NUMBER(18),
   NAME                 VARCHAR2(255),
   TYPE                 NUMBER(11),
   CHANNELLEVEL         NUMBER(11),
   STARLEVEL            NUMBER(11),
   OPERATIONMODE        NUMBER(11),
   BUILDINGTYPE         NUMBER(11),
   LOCATION             VARCHAR2(255),
   BIZREGIONTYPE        NUMBER(11),
   BIZTYPE              NUMBER(11),
   CONTACT              VARCHAR2(100),
   CONTACTPHONE         VARCHAR2(30),
   XPOS                 NUMBER(10,5),
   YPOS                 NUMBER(10,5),
   USAGEPROPERTY        NUMBER(11),
   STARTTIME            DATE,
   ENDTIME              DATE,
   REGIONID             NUMBER(18),
   BUILDINGAREA         NUMBER(5,2),
   HOUSECERTIFICATE     VARCHAR2(30),
   LANDCERTIFCATE       VARCHAR2(30),
   AVGRENT              NUMBER(5),
   SECURITYCOUNT        NUMBER(5),
   CLEANWORKERCOUNT     NUMBER(5),
   HASQUEUEMACHINE      NUMBER(11),
   HASPOS               NUMBER(11),
   HASVIPROOM           NUMBER(11),
   HASSELFSERVICE       NUMBER(11),
   BILLPRINTERCOUNT     NUMBER(11),
   HASADVSELFSERVICE    NUMBER(11),
   G3AREA               NUMBER(7,2),
   SCREENCOUNT          NUMBER(5),
   NEWBIZEXPPLATFORMCOUNT NUMBER(5),
   NEWPHONEEXPPLATFORMCOUNT NUMBER(5),
   ECUSTSERVICECENTERCOUNT NUMBER(5),
   FRONTAREA            NUMBER(5),
   RECEPTIONISTCOUNT    NUMBER(5),
   PHONERECEPTIONISTCOUNT NUMBER(5),
   TERMINALCOUNT        NUMBER(5),
   USEAREA              NUMBER(7,2),
   DOORHEIGHT           NUMBER(7,2),
   DOORWIDTH            NUMBER(7,2),
   constraint PK_SPDD_CHANNEL primary key (ID)
);

comment on table SPDD_CHANNEL is
'自有渠道';

comment on column SPDD_CHANNEL.CATAGORY is
'分类，自有/社会/竞争对手';

comment on column SPDD_CHANNEL.CODE is
'编码';

comment on column SPDD_CHANNEL.PARENTID is
'上级渠道';

comment on column SPDD_CHANNEL.NAME is
'名称';

comment on column SPDD_CHANNEL.TYPE is
'类型';

comment on column SPDD_CHANNEL.CHANNELLEVEL is
'级别';

comment on column SPDD_CHANNEL.STARLEVEL is
'星级';

comment on column SPDD_CHANNEL.OPERATIONMODE is
'运营方式';

comment on column SPDD_CHANNEL.BUILDINGTYPE is
'物业来源类型';

comment on column SPDD_CHANNEL.LOCATION is
'地理位置';

comment on column SPDD_CHANNEL.BIZREGIONTYPE is
'商圈类型';

comment on column SPDD_CHANNEL.BIZTYPE is
'办理业务类型';

comment on column SPDD_CHANNEL.CONTACT is
'联系人';

comment on column SPDD_CHANNEL.CONTACTPHONE is
'联系电话';

comment on column SPDD_CHANNEL.XPOS is
'经度';

comment on column SPDD_CHANNEL.YPOS is
'纬度';

comment on column SPDD_CHANNEL.USAGEPROPERTY is
'用房性质';

comment on column SPDD_CHANNEL.STARTTIME is
'营业开始时间';

comment on column SPDD_CHANNEL.ENDTIME is
'营业结束时间';

comment on column SPDD_CHANNEL.REGIONID is
'所属区域';

comment on column SPDD_CHANNEL.BUILDINGAREA is
'建筑面积';

comment on column SPDD_CHANNEL.HOUSECERTIFICATE is
'房产证号';

comment on column SPDD_CHANNEL.LANDCERTIFCATE is
'土地证号';

comment on column SPDD_CHANNEL.AVGRENT is
'年平均租金';

comment on column SPDD_CHANNEL.SECURITYCOUNT is
'保安数量';

comment on column SPDD_CHANNEL.CLEANWORKERCOUNT is
'保洁数量';

comment on column SPDD_CHANNEL.HASQUEUEMACHINE is
'有无排队叫号机';

comment on column SPDD_CHANNEL.HASPOS is
'有无POS机';

comment on column SPDD_CHANNEL.HASVIPROOM is
'有无VIP室';

comment on column SPDD_CHANNEL.HASSELFSERVICE is
'有无24小时自助营业厅';

comment on column SPDD_CHANNEL.BILLPRINTERCOUNT is
'帐详单打印机数量';

comment on column SPDD_CHANNEL.HASADVSELFSERVICE is
'综合性自助终端（含缴费)';

comment on column SPDD_CHANNEL.G3AREA is
'G3体验区面积';

comment on column SPDD_CHANNEL.SCREENCOUNT is
'电视屏数量';

comment on column SPDD_CHANNEL.NEWBIZEXPPLATFORMCOUNT is
'新业务体验营销平台接入数量';

comment on column SPDD_CHANNEL.NEWPHONEEXPPLATFORMCOUNT is
'新机体验平台数量';

comment on column SPDD_CHANNEL.ECUSTSERVICECENTERCOUNT is
'网上营业厅接入数量';

comment on column SPDD_CHANNEL.FRONTAREA is
'店面面积';

comment on column SPDD_CHANNEL.RECEPTIONISTCOUNT is
'营业员数目';

comment on column SPDD_CHANNEL.PHONERECEPTIONISTCOUNT is
'座席数目';

comment on column SPDD_CHANNEL.TERMINALCOUNT is
'终端数目';

comment on column SPDD_CHANNEL.USEAREA is
'使用面积';

comment on column SPDD_CHANNEL.DOORHEIGHT is
'门头高度';

comment on column SPDD_CHANNEL.DOORWIDTH is
'门头宽度';

/*==============================================================*/
/* Table: SPDD_CHANNELCONTRACTINFO                              */
/*==============================================================*/
create table SPDD_CHANNELCONTRACTINFO  (
   ID                   NUMBER(18)                      not null,
   TYPE                 NUMBER(11),
   CHANNELLEVEL         NUMBER(11),
   NAME                 VARCHAR2(500),
   PARTYANAME           VARCHAR2(255),
   PARTYBNAME           VARCHAR2(255),
   SERIALNUMBER         VARCHAR2(100),
   PARTYADELEGATE       VARCHAR2(30),
   PARTYBDELEGATE       VARCHAR2(30),
   CONTRACTTIME         DATE,
   EFFECTIVETIME        DATE,
   DEADTIME             DATE,
   CHANNELID            NUMBER(18),
   constraint PK_SPDD_CHANNELCONTRACTINFO primary key (ID)
);

comment on table SPDD_CHANNELCONTRACTINFO is
'自有渠道合同信息';

comment on column SPDD_CHANNELCONTRACTINFO.TYPE is
'类型';

comment on column SPDD_CHANNELCONTRACTINFO.CHANNELLEVEL is
'级别';

comment on column SPDD_CHANNELCONTRACTINFO.NAME is
'名称';

comment on column SPDD_CHANNELCONTRACTINFO.PARTYANAME is
'甲方名称';

comment on column SPDD_CHANNELCONTRACTINFO.PARTYBNAME is
'乙方名称';

comment on column SPDD_CHANNELCONTRACTINFO.SERIALNUMBER is
'序列号';

comment on column SPDD_CHANNELCONTRACTINFO.PARTYADELEGATE is
'甲方代表';

comment on column SPDD_CHANNELCONTRACTINFO.PARTYBDELEGATE is
'乙方代表';

comment on column SPDD_CHANNELCONTRACTINFO.CONTRACTTIME is
'签约时间';

comment on column SPDD_CHANNELCONTRACTINFO.EFFECTIVETIME is
'生效时间';

comment on column SPDD_CHANNELCONTRACTINFO.DEADTIME is
'失效时间';

comment on column SPDD_CHANNELCONTRACTINFO.CHANNELID is
'所属渠道';

/*==============================================================*/
/* Table: SPDD_CUSTRECEPTIONSTATS                               */
/*==============================================================*/
create table SPDD_CUSTRECEPTIONSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_CUSTRECEPTIONSTATS primary key (ID)
);

comment on table SPDD_CUSTRECEPTIONSTATS is
'接待客户数';

comment on column SPDD_CUSTRECEPTIONSTATS.CHANNELID is
'渠道';

comment on column SPDD_CUSTRECEPTIONSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_CUSTRECEPTIONSTATS.STATSTIME is
'统计时间';

comment on column SPDD_CUSTRECEPTIONSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_CUSTSATISFACTIONSTATS                            */
/*==============================================================*/
create table SPDD_CUSTSATISFACTIONSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_CUSTSATISFACTIONSTATS primary key (ID)
);

comment on table SPDD_CUSTSATISFACTIONSTATS is
'客户满意度';

comment on column SPDD_CUSTSATISFACTIONSTATS.CHANNELID is
'渠道';

comment on column SPDD_CUSTSATISFACTIONSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_CUSTSATISFACTIONSTATS.STATSTIME is
'统计时间';

comment on column SPDD_CUSTSATISFACTIONSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_EMPLOYEEINFO                                     */
/*==============================================================*/
create table SPDD_EMPLOYEEINFO  (
   ID                   NUMBER(18)                      not null,
   TYPE                 NUMBER(11),
   POSITION             NUMBER(11),
   CHANNELID            NUMBER(18),
   CODE                 VARCHAR2(50),
   NAME                 VARCHAR2(30),
   constraint PK_SPDD_EMPLOYEEINFO primary key (ID)
);

comment on table SPDD_EMPLOYEEINFO is
'营业厅雇员信息';

comment on column SPDD_EMPLOYEEINFO.TYPE is
'类型';

comment on column SPDD_EMPLOYEEINFO.POSITION is
'职位';

comment on column SPDD_EMPLOYEEINFO.CHANNELID is
'所属渠道';

comment on column SPDD_EMPLOYEEINFO.CODE is
'工号';

comment on column SPDD_EMPLOYEEINFO.NAME is
'姓名';

/*==============================================================*/
/* Table: SPDD_HUMANEFFECTSTATS                                 */
/*==============================================================*/
create table SPDD_HUMANEFFECTSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_HUMANEFFECTSTATS primary key (ID)
);

comment on table SPDD_HUMANEFFECTSTATS is
'人效';

comment on column SPDD_HUMANEFFECTSTATS.CHANNELID is
'渠道';

comment on column SPDD_HUMANEFFECTSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_HUMANEFFECTSTATS.STATSTIME is
'统计时间';

comment on column SPDD_HUMANEFFECTSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_LOSTCUSTOMERSTATS                                */
/*==============================================================*/
create table SPDD_LOSTCUSTOMERSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_LOSTCUSTOMERSTATS primary key (ID)
);

comment on table SPDD_LOSTCUSTOMERSTATS is
'离网客户数';

comment on column SPDD_LOSTCUSTOMERSTATS.CHANNELID is
'渠道';

comment on column SPDD_LOSTCUSTOMERSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_LOSTCUSTOMERSTATS.STATSTIME is
'统计时间';

comment on column SPDD_LOSTCUSTOMERSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_NEWCUSTOMERSTATS                                 */
/*==============================================================*/
create table SPDD_NEWCUSTOMERSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_NEWCUSTOMERSTATS primary key (ID)
);

comment on table SPDD_NEWCUSTOMERSTATS is
'新增客户数';

comment on column SPDD_NEWCUSTOMERSTATS.CHANNELID is
'渠道';

comment on column SPDD_NEWCUSTOMERSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_NEWCUSTOMERSTATS.STATSTIME is
'统计时间';

comment on column SPDD_NEWCUSTOMERSTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_PRODUCTDIMENSION                                 */
/*==============================================================*/
create table SPDD_PRODUCTDIMENSION  (
   ID                   NUMBER(18)                      not null,
   TYPE                 VARCHAR2(50),
   SUBTYPE              VARCHAR2(50),
   NAME                 VARCHAR2(255),
   constraint PK_SPDD_PRODUCTDIMENSION primary key (ID)
);

comment on table SPDD_PRODUCTDIMENSION is
'业务维度';

comment on column SPDD_PRODUCTDIMENSION.TYPE is
'产品大类';

comment on column SPDD_PRODUCTDIMENSION.SUBTYPE is
'产品小类';

comment on column SPDD_PRODUCTDIMENSION.NAME is
'产品名称';

/*==============================================================*/
/* Table: SPDD_QUEUEUPTIMESTATS                                 */
/*==============================================================*/
create table SPDD_QUEUEUPTIMESTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_QUEUEUPTIMESTATS primary key (ID)
);

comment on table SPDD_QUEUEUPTIMESTATS is
'排队等候时间';

comment on column SPDD_QUEUEUPTIMESTATS.CHANNELID is
'渠道';

comment on column SPDD_QUEUEUPTIMESTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_QUEUEUPTIMESTATS.STATSTIME is
'统计时间';

comment on column SPDD_QUEUEUPTIMESTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_REVENUESTATS                                     */
/*==============================================================*/
create table SPDD_REVENUESTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_REVENUESTATS primary key (ID)
);

comment on table SPDD_REVENUESTATS is
'营收额';

comment on column SPDD_REVENUESTATS.CHANNELID is
'渠道';

comment on column SPDD_REVENUESTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_REVENUESTATS.STATSTIME is
'统计时间';

comment on column SPDD_REVENUESTATS.COUNT is
'数量';

/*==============================================================*/
/* Table: SPDD_TIMEDIMENSION                                    */
/*==============================================================*/
create table SPDD_TIMEDIMENSION  (
   ID                   NUMBER(18)                      not null,
   TYPE                 NUMBER(11),
   YEAR                 VARCHAR2(4),
   SEASON               VARCHAR2(2),
   MONTH                VARCHAR2(2),
   DAY                  VARCHAR2(2),
   constraint PK_SPDD_TIMEDIMENSION primary key (ID)
);

comment on table SPDD_TIMEDIMENSION is
'日维度';

comment on column SPDD_TIMEDIMENSION.TYPE is
'类型，年/季度/月/日';

comment on column SPDD_TIMEDIMENSION.YEAR is
'年度';

comment on column SPDD_TIMEDIMENSION.SEASON is
'季节';

comment on column SPDD_TIMEDIMENSION.MONTH is
'月';

/*==============================================================*/
/* Table: SPDD_TOPBUSINESSSTATS                                 */
/*==============================================================*/
create table SPDD_TOPBUSINESSSTATS  (
   ID                   NUMBER(18)                      not null,
   CHANNELID            NUMBER(18),
   TIMEDIMENSIONID      NUMBER(18),
   STATSTIME            DATE,
   COUNT                NUMBER(8),
   constraint PK_SPDD_TOPBUSINESSSTATS primary key (ID)
);

comment on table SPDD_TOPBUSINESSSTATS is
'峰值业务受理量';

comment on column SPDD_TOPBUSINESSSTATS.CHANNELID is
'渠道';

comment on column SPDD_TOPBUSINESSSTATS.TIMEDIMENSIONID is
'时间维度';

comment on column SPDD_TOPBUSINESSSTATS.STATSTIME is
'统计时间';

comment on column SPDD_TOPBUSINESSSTATS.COUNT is
'数量';

alter table SPDD_AREAEFFECTSTATS
   add constraint FK_SPDD_ARE_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_AREAEFFECTSTATS
   add constraint FK_SPDD_ARE_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_BUSINESSSTATS
   add constraint FK_SPDD_BUS_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_BUSINESSSTATS
   add constraint FK_SPDD_BUS_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_BUSINESSSTATS
   add constraint FK_SPDD_BUS_REFERENCE_SPDD_PRO foreign key (PRODUCTDEIMENSIONID)
      references SPDD_PRODUCTDIMENSION (ID);

alter table SPDD_CHANNEL
   add constraint FK_SPDD_CHA_REFERENCE_SPDD_CHA foreign key (PARENTID)
      references SPDD_CHANNEL (ID);

alter table SPDD_CHANNELCONTRACTINFO
   add constraint FK_SPDD_CON_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_CUSTRECEPTIONSTATS
   add constraint FK_SPDD_CUS_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_CUSTRECEPTIONSTATS
   add constraint FK_SPDD_CUS_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_CUSTSATISFACTIONSTATS
   add constraint FK_SPDD_SAT_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_CUSTSATISFACTIONSTATS
   add constraint FK_SPDD_SAT_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_EMPLOYEEINFO
   add constraint FK_SPDD_EMP_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_HUMANEFFECTSTATS
   add constraint FK_SPDD_HUM_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_HUMANEFFECTSTATS
   add constraint FK_SPDD_HUM_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_LOSTCUSTOMERSTATS
   add constraint FK_SPDD_LOS_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_LOSTCUSTOMERSTATS
   add constraint FK_SPDD_LOS_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_NEWCUSTOMERSTATS
   add constraint FK_SPDD_NEW_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_NEWCUSTOMERSTATS
   add constraint FK_SPDD_NEW_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_QUEUEUPTIMESTATS
   add constraint FK_SPDD_QUE_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_QUEUEUPTIMESTATS
   add constraint FK_SPDD_QUE_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_REVENUESTATS
   add constraint FK_SPDD_REV_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

alter table SPDD_REVENUESTATS
   add constraint FK_SPDD_REV_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_TOPBUSINESSSTATS
   add constraint FK_SPDD_TOP_REFERENCE_SPDD_TIM foreign key (TIMEDIMENSIONID)
      references SPDD_TIMEDIMENSION (ID);

alter table SPDD_TOPBUSINESSSTATS
   add constraint FK_SPDD_TOP_REFERENCE_SPDD_CHA foreign key (CHANNELID)
      references SPDD_CHANNEL (ID);

