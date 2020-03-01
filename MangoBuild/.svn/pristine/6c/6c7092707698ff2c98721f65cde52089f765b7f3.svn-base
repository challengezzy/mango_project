/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/4/27 11:12:22                           */
/*==============================================================*/


alter table BAM_ALERT
   drop constraint FK_BAM_ALER_REFERENCE_BAM_BUSI;

alter table BAM_BUSINESSSCENARIO
   drop constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD;

alter table BAM_DASHBOARD
   drop constraint FK_BAM_DASH_REFERENCE_BAM_FOLD;

alter table BAM_DASHBOARDOBJECT
   drop constraint FK_BAM_DO_REFERENCE_BAM_FOLD;

alter table BAM_FOLDER
   drop constraint FK_BAM_FOLD_REFERENCE_BAM_FOLD;

alter table BAM_MESSAGESUBSCRIBER
   drop constraint FK_BAM_MESS_REFERENCE_BAM_ALER;

alter table BAM_RULE
   drop constraint FK_BAM_RULE_REFERENCE_BAM_BUSI;

alter table BAM_RULE
   drop constraint FK_BAM_RULE_REFERENCE_BAM_ALER;

alter table BAM_SUBSCIBER
   drop constraint FK_BAM_SUBS_REFERENCE_BAM_ALER;

drop view V_BAM_FOLDER_BUSINESSACTIVITY;

drop view V_BAM_FOLDER_DASHBOARD;

drop view V_BAM_FOLDER_DO;

drop table BAM_ACCOUNTSETTINGS cascade constraints;

drop index "Index_6";

drop table BAM_ALERT cascade constraints;

drop table BAM_ALERTMESSAGE cascade constraints;

drop table BAM_ALERTMESSAGE_ARCH cascade constraints;

drop index "Index_3";

drop table BAM_BUSINESSSCENARIO cascade constraints;

drop index "Index_2";

drop table BAM_BUSINESSVIEW cascade constraints;

drop index "Index_9";

drop table BAM_DASHBOARD cascade constraints;

drop index "Index_11";

drop table BAM_DASHBOARDOBJECT cascade constraints;

drop index "Index_5";

drop table BAM_EVENTDATASOURCE cascade constraints;

drop index "Index_10";

drop table BAM_FOLDER cascade constraints;

drop index "Index_8";

drop table BAM_MESSAGESUBSCRIBER cascade constraints;

drop table BAM_MESSAGESUBSCRIBER_ARCH cascade constraints;

drop table BAM_RELATIONALDATASOURCE cascade constraints;

drop index "Index_4";

drop table BAM_RULE cascade constraints;

drop index "Index_7";

drop table BAM_SUBSCIBER cascade constraints;

drop index "Index_1";

drop table BAM_SYSTEMSETTINGS cascade constraints;

drop sequence S_BAM_ACCOUNTSETTINGS;

drop sequence S_BAM_ALERT;

drop sequence S_BAM_ALERTMESSAGE;

drop sequence S_BAM_BUSINESSSCENARIO;

drop sequence S_BAM_BUSINESSVIEW;

drop sequence S_BAM_DASHBOARD;

drop sequence S_BAM_DASHBOARDOBJECT;

drop sequence S_BAM_EVENTDATASOURCE;

drop sequence S_BAM_FOLDER;

drop sequence S_BAM_MESSAGESUBSCRIBER;

drop sequence S_BAM_RELATIONALDATASOURCE;

drop sequence S_BAM_RULE;

drop sequence S_BAM_SUBSCIBER;

drop sequence S_BAM_SYSTEMSETTINGS;

create sequence S_BAM_ACCOUNTSETTINGS;

create sequence S_BAM_ALERT;

create sequence S_BAM_ALERTMESSAGE;

create sequence S_BAM_BUSINESSSCENARIO;

create sequence S_BAM_BUSINESSVIEW;

create sequence S_BAM_DASHBOARD;

create sequence S_BAM_DASHBOARDOBJECT;

create sequence S_BAM_EVENTDATASOURCE;

create sequence S_BAM_FOLDER;

create sequence S_BAM_MESSAGESUBSCRIBER;

create sequence S_BAM_RELATIONALDATASOURCE;

create sequence S_BAM_RULE;

create sequence S_BAM_SUBSCIBER;

create sequence S_BAM_SYSTEMSETTINGS;

/*==============================================================*/
/* Table: BAM_ACCOUNTSETTINGS                                   */
/*==============================================================*/
create table BAM_ACCOUNTSETTINGS  (
   ID                   NUMBER(18)                      not null,
   USERNAME             VARCHAR2(50)                    not null,
   MTCODE               VARCHAR2(255)                   not null,
   constraint PK_BAM_ACCOUNTSETTINGS primary key (ID)
);

comment on table BAM_ACCOUNTSETTINGS is
'用户配置，XML格式存放';

comment on column BAM_ACCOUNTSETTINGS.USERNAME is
'用户名';

comment on column BAM_ACCOUNTSETTINGS.MTCODE is
'关联的元数据模板编码';

/*==============================================================*/
/* Table: BAM_ALERT                                             */
/*==============================================================*/
create table BAM_ALERT  (
   ID                   NUMBER(18)                      not null,
   BUSINESSSCENARIOID   NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   SEVERITY             NUMBER(11)                      not null,
   SUBJECT              VARCHAR2(1000),
   BODY                 CLOB,
   STATUS               NUMBER(11)                      not null,
   constraint PK_BAM_ALERT primary key (ID)
);

comment on table BAM_ALERT is
'告警类型';

comment on column BAM_ALERT.BUSINESSSCENARIOID is
'关联的业务场景ID';

comment on column BAM_ALERT.NAME is
'名称';

comment on column BAM_ALERT.CODE is
'编码';

comment on column BAM_ALERT.DESCRIPTION is
'描述';

comment on column BAM_ALERT.SEVERITY is
'重要程度，数据字典
0 高
1 一般
2 低';

comment on column BAM_ALERT.SUBJECT is
'主题';

comment on column BAM_ALERT.BODY is
'主体内容';

comment on column BAM_ALERT.STATUS is
'状态，数据字典
0 普通状态 Lowered
1 激活状态 Raised
2 确认状态 Acknowledged';

/*==============================================================*/
/* Index: "Index_6"                                             */
/*==============================================================*/
create unique index "Index_6" on BAM_ALERT (
   NAME ASC
);

/*==============================================================*/
/* Table: BAM_ALERTMESSAGE                                      */
/*==============================================================*/
create table BAM_ALERTMESSAGE  (
   ID                   NUMBER(18)                      not null,
   ALERTID              NUMBER(18)                      not null,
   SUBJECT              VARCHAR2(1000),
   SEVERITY             NUMBER(11)                      not null,
   BODY                 CLOB,
   ACTIVATETIME         DATE                            not null,
   STATUS               NUMBER(11)                      not null,
   constraint PK_BAM_ALERTMESSAGE primary key (ID)
);

comment on table BAM_ALERTMESSAGE is
'告警消息';

comment on column BAM_ALERTMESSAGE.ALERTID is
'关联的告警类型ID，弱关联';

comment on column BAM_ALERTMESSAGE.SUBJECT is
'主题';

comment on column BAM_ALERTMESSAGE.SEVERITY is
'重要程度，数据字典，同ALERT';

comment on column BAM_ALERTMESSAGE.BODY is
'主体内容';

comment on column BAM_ALERTMESSAGE.ACTIVATETIME is
'触发时间';

comment on column BAM_ALERTMESSAGE.STATUS is
'状态，数据字典
0 激活 Raised
1 忽略 Ignore';

/*==============================================================*/
/* Table: BAM_ALERTMESSAGE_ARCH                                 */
/*==============================================================*/
create table BAM_ALERTMESSAGE_ARCH  (
   ID                   NUMBER(18),
   ALERTID              NUMBER(18),
   SUBJECT              VARCHAR2(1000),
   SEVERITY             NUMBER(11),
   BODY                 CLOB,
   ACTIVATETIME         DATE,
   STATUS               NUMBER(11)
);

comment on table BAM_ALERTMESSAGE_ARCH is
'告警实例归档表';

comment on column BAM_ALERTMESSAGE_ARCH.ALERTID is
'关联的告警类型ID，弱关联';

comment on column BAM_ALERTMESSAGE_ARCH.SUBJECT is
'主题';

comment on column BAM_ALERTMESSAGE_ARCH.SEVERITY is
'重要程度，数据字典，同ALERT';

comment on column BAM_ALERTMESSAGE_ARCH.BODY is
'主体内容';

comment on column BAM_ALERTMESSAGE_ARCH.ACTIVATETIME is
'触发时间';

comment on column BAM_ALERTMESSAGE_ARCH.STATUS is
'状态，数据字典
0 激活 Raised
1 忽略 Ignore';

/*==============================================================*/
/* Table: BAM_BUSINESSSCENARIO                                  */
/*==============================================================*/
create table BAM_BUSINESSSCENARIO  (
   ID                   NUMBER(18)                      not null,
   FOLDERID             NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   DATASOURCETYPE       NUMBER(11),
   DATASOURCECODE       VARCHAR2(100),
   constraint PK_BAM_BUSINESSSCENARIO primary key (ID)
);

comment on table BAM_BUSINESSSCENARIO is
'业务场景';

comment on column BAM_BUSINESSSCENARIO.FOLDERID is
'所属目录';

comment on column BAM_BUSINESSSCENARIO.NAME is
'名称';

comment on column BAM_BUSINESSSCENARIO.CODE is
'编码';

comment on column BAM_BUSINESSSCENARIO.DESCRIPTION is
'描述';

comment on column BAM_BUSINESSSCENARIO.DATASOURCETYPE is
'场景的数据源类型，数据字典
0 业务视图
1 立方体';

comment on column BAM_BUSINESSSCENARIO.DATASOURCECODE is
'场景的数据源编码';

/*==============================================================*/
/* Index: "Index_3"                                             */
/*==============================================================*/
create unique index "Index_3" on BAM_BUSINESSSCENARIO (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_BUSINESSVIEW                                      */
/*==============================================================*/
create table BAM_BUSINESSVIEW  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   ISPERSISTVIEWDATA    NUMBER(11),
   STREAMNAME           VARCHAR2(255),
   STREAMWINDOWNAME     VARCHAR2(255),
   STREAMMODULENAME     VARCHAR2(255),
   constraint PK_BAM_BUSINESSVIEW primary key (ID)
);

comment on table BAM_BUSINESSVIEW is
'业务视图';

comment on column BAM_BUSINESSVIEW.NAME is
'名称';

comment on column BAM_BUSINESSVIEW.CODE is
'编码';

comment on column BAM_BUSINESSVIEW.DESCRIPTION is
'描述';

comment on column BAM_BUSINESSVIEW.ISPERSISTVIEWDATA is
'是否持久化视图数据
数据字典 是/否';

comment on column BAM_BUSINESSVIEW.STREAMNAME is
'对应的事件流名称';

comment on column BAM_BUSINESSVIEW.STREAMWINDOWNAME is
'对应的事件流窗口名称';

comment on column BAM_BUSINESSVIEW.STREAMMODULENAME is
'关联的流模块';

/*==============================================================*/
/* Index: "Index_2"                                             */
/*==============================================================*/
create unique index "Index_2" on BAM_BUSINESSVIEW (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_DASHBOARD                                         */
/*==============================================================*/
create table BAM_DASHBOARD  (
   ID                   NUMBER(18)                      not null,
   FOLDERID             NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   REFRESHINTERVAL      NUMBER(5),
   LAYOUT_MTCODE        VARCHAR2(255),
   constraint PK_BAM_DASHBOARD primary key (ID)
);

comment on table BAM_DASHBOARD is
'仪表盘';

comment on column BAM_DASHBOARD.NAME is
'名称';

comment on column BAM_DASHBOARD.CODE is
'编码';

comment on column BAM_DASHBOARD.DESCRIPTION is
'描述';

comment on column BAM_DASHBOARD.REFRESHINTERVAL is
'刷新间隔，单位秒';

comment on column BAM_DASHBOARD.LAYOUT_MTCODE is
'布局的元数据模板编码';

/*==============================================================*/
/* Index: "Index_9"                                             */
/*==============================================================*/
create unique index "Index_9" on BAM_DASHBOARD (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_DASHBOARDOBJECT                                   */
/*==============================================================*/
create table BAM_DASHBOARDOBJECT  (
   ID                   NUMBER(18)                      not null,
   FOLDERID             NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   TYPE                 NUMBER(11)                      not null,
   MTCODE               VARCHAR2(255),
   constraint PK_BAM_DASHBOARDOBJECT primary key (ID)
);

comment on table BAM_DASHBOARDOBJECT is
'仪表盘对象';

comment on column BAM_DASHBOARDOBJECT.FOLDERID is
'关联目录ID';

comment on column BAM_DASHBOARDOBJECT.NAME is
'名称';

comment on column BAM_DASHBOARDOBJECT.CODE is
'编码';

comment on column BAM_DASHBOARDOBJECT.DESCRIPTION is
'描述';

comment on column BAM_DASHBOARDOBJECT.TYPE is
'类型，数据字典
0 饼图 PieChart
1 混合图 CombinationChart
2 分布图 DistributionChart
3 透视混合图 PivotCombinationChart
4 地理图 GeographyChart
5 指示器 Indicator
6 列表 Table';

comment on column BAM_DASHBOARDOBJECT.MTCODE is
'关联的元数据模板编码';

/*==============================================================*/
/* Index: "Index_11"                                            */
/*==============================================================*/
create unique index "Index_11" on BAM_DASHBOARDOBJECT (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_EVENTDATASOURCE                                   */
/*==============================================================*/
create table BAM_EVENTDATASOURCE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   ADAPTORCLASS         VARCHAR2(255),
   CONFIGURATION        CLOB,
   STATUS               NUMBER(11),
   constraint PK_BAM_EVENTDATASOURCE primary key (ID)
);

comment on table BAM_EVENTDATASOURCE is
'事件源';

comment on column BAM_EVENTDATASOURCE.NAME is
'名称';

comment on column BAM_EVENTDATASOURCE.ADAPTORCLASS is
'适配器实现类';

comment on column BAM_EVENTDATASOURCE.STATUS is
'状态，数据字典
Opened - The begin state; The adapter is not generating or accepting events in this state 

Started - When the adapter is active, generating and accepting events 

Paused - When operation of the adapter is suspended 

Destroyed ';

/*==============================================================*/
/* Index: "Index_5"                                             */
/*==============================================================*/
create unique index "Index_5" on BAM_EVENTDATASOURCE (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_FOLDER                                            */
/*==============================================================*/
create table BAM_FOLDER  (
   ID                   NUMBER(18)                      not null,
   PARENTID             NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   TYPE                 NUMBER(11)                      not null,
   constraint PK_BAM_FOLDER primary key (ID)
);

comment on table BAM_FOLDER is
'对象目录';

comment on column BAM_FOLDER.PARENTID is
'父目录ID';

comment on column BAM_FOLDER.NAME is
'名称';

comment on column BAM_FOLDER.CODE is
'编码';

comment on column BAM_FOLDER.DESCRIPTION is
'描述';

comment on column BAM_FOLDER.TYPE is
'类型，数据字典
0 业务活动  BusinessActivity
1 仪表盘 Dashboard
2 仪表盘对象 DashboardObject';

/*==============================================================*/
/* Index: "Index_10"                                            */
/*==============================================================*/
create unique index "Index_10" on BAM_FOLDER (
   CODE ASC,
   TYPE ASC
);

/*==============================================================*/
/* Table: BAM_MESSAGESUBSCRIBER                                 */
/*==============================================================*/
create table BAM_MESSAGESUBSCRIBER  (
   ID                   NUMBER(18)                      not null,
   ALERTMESSAGEID       NUMBER(18)                      not null,
   USERNAME             VARCHAR2(50)                    not null,
   constraint PK_BAM_MESSAGESUBSCRIBER primary key (ID)
);

comment on table BAM_MESSAGESUBSCRIBER is
'告警消息的实际订阅者';

comment on column BAM_MESSAGESUBSCRIBER.ALERTMESSAGEID is
'关联的告警消息ID';

comment on column BAM_MESSAGESUBSCRIBER.USERNAME is
'关联的用户名';

/*==============================================================*/
/* Index: "Index_8"                                             */
/*==============================================================*/
create unique index "Index_8" on BAM_MESSAGESUBSCRIBER (
   ALERTMESSAGEID ASC,
   USERNAME ASC
);

/*==============================================================*/
/* Table: BAM_MESSAGESUBSCRIBER_ARCH                            */
/*==============================================================*/
create table BAM_MESSAGESUBSCRIBER_ARCH  (
   ID                   NUMBER(18),
   ALERTMESSAGEID       NUMBER(18),
   USERNAME             VARCHAR2(50)
);

comment on table BAM_MESSAGESUBSCRIBER_ARCH is
'告警消息的实际订阅者归档表';

comment on column BAM_MESSAGESUBSCRIBER_ARCH.ALERTMESSAGEID is
'关联的告警消息ID';

comment on column BAM_MESSAGESUBSCRIBER_ARCH.USERNAME is
'关联的用户名';

/*==============================================================*/
/* Table: BAM_RELATIONALDATASOURCE                              */
/*==============================================================*/
create table BAM_RELATIONALDATASOURCE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100)                   not null,
   TYPE                 NUMBER(11),
   CONFIGURATION        CLOB,
   constraint PK_BAM_RELATIONALDATASOURCE primary key (ID)
);

comment on table BAM_RELATIONALDATASOURCE is
'关系型数据源';

comment on column BAM_RELATIONALDATASOURCE.NAME is
'名称';

comment on column BAM_RELATIONALDATASOURCE.TYPE is
'类型，数据字典
0 datasource
1 datasourcefactory
2 drivermanager';

comment on column BAM_RELATIONALDATASOURCE.CONFIGURATION is
'XML配置信息';

/*==============================================================*/
/* Table: BAM_RULE                                              */
/*==============================================================*/
create table BAM_RULE  (
   ID                   NUMBER(18)                      not null,
   BUSINESSSCENARIOID   NUMBER(18),
   NAME                 VARCHAR2(100)                   not null,
   CODE                 VARCHAR2(100)                   not null,
   DESCRIPTION          VARCHAR2(1000),
   CONDITION            VARCHAR2(1000),
   HOLDSFOR             NUMBER(4),
   ACTIONALERTID        NUMBER(18),
   ACTIONTYPE           NUMBER(11),
   STREAMMODULENAME     VARCHAR2(255),
   constraint PK_BAM_RULE primary key (ID)
);

comment on table BAM_RULE is
'业务规则';

comment on column BAM_RULE.BUSINESSSCENARIOID is
'业务场景ID';

comment on column BAM_RULE.NAME is
'名称';

comment on column BAM_RULE.DESCRIPTION is
'描述';

comment on column BAM_RULE.CONDITION is
'规则条件';

comment on column BAM_RULE.HOLDSFOR is
'当条件符合并持续多少时间以后才执行动作
单位秒';

comment on column BAM_RULE.ACTIONALERTID is
'关联的告警ID';

comment on column BAM_RULE.ACTIONTYPE is
'动作类型，数据字典
0 每次均发送 Send Everytime
1 只发送一次 Send Once
2 重置上次告警 Reset';

comment on column BAM_RULE.STREAMMODULENAME is
'关联的流模块名称';

/*==============================================================*/
/* Index: "Index_4"                                             */
/*==============================================================*/
create unique index "Index_4" on BAM_RULE (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_SUBSCIBER                                         */
/*==============================================================*/
create table BAM_SUBSCIBER  (
   ID                   NUMBER(18)                      not null,
   OBJECTNAME           VARCHAR2(50)                    not null,
   TYPE                 NUMBER(11)                      not null,
   SUBSCRIPTION         NUMBER(11)                      not null,
   DELIVERYTYPE         NUMBER(11)                      not null,
   ALERTID              NUMBER(18)                      not null,
   constraint PK_BAM_SUBSCIBER primary key (ID)
);

comment on table BAM_SUBSCIBER is
'订阅者';

comment on column BAM_SUBSCIBER.OBJECTNAME is
'订阅者名称';

comment on column BAM_SUBSCIBER.TYPE is
'类型，数据字典
0 用户
1 角色
2 工位';

comment on column BAM_SUBSCIBER.SUBSCRIPTION is
'订阅方式，数据字典
0 必须 Mandatory
1 可选 Optional';

comment on column BAM_SUBSCIBER.DELIVERYTYPE is
'传送方式，数据字典
0 普通
1 邮件
2 全部';

comment on column BAM_SUBSCIBER.ALERTID is
'关联的告警类型ID';

/*==============================================================*/
/* Index: "Index_7"                                             */
/*==============================================================*/
create unique index "Index_7" on BAM_SUBSCIBER (
   OBJECTNAME ASC,
   TYPE ASC,
   ALERTID ASC
);

/*==============================================================*/
/* Table: BAM_SYSTEMSETTINGS                                    */
/*==============================================================*/
create table BAM_SYSTEMSETTINGS  (
   ID                   NUMBER(11)                      not null,
   KEY                  VARCHAR2(255)                   not null,
   KEYCN                VARCHAR2(255),
   VALUE                VARCHAR2(4000),
   constraint PK_BAM_SYSTEMSETTINGS primary key (ID)
);

comment on table BAM_SYSTEMSETTINGS is
'系统参数';

comment on column BAM_SYSTEMSETTINGS.KEY is
'参数键值编码';

comment on column BAM_SYSTEMSETTINGS.KEYCN is
'中文参数名';

/*==============================================================*/
/* Index: "Index_1"                                             */
/*==============================================================*/
create unique index "Index_1" on BAM_SYSTEMSETTINGS (
   KEY ASC
);

/*==============================================================*/
/* View: V_BAM_FOLDER_BUSINESSACTIVITY                          */
/*==============================================================*/
create or replace view V_BAM_FOLDER_BUSINESSACTIVITY as
select
   *
from
   BAM_FOLDER
where
   type=0
with read only;

 comment on table V_BAM_FOLDER_BUSINESSACTIVITY is
'业务活动目录';

/*==============================================================*/
/* View: V_BAM_FOLDER_DASHBOARD                                 */
/*==============================================================*/
create or replace view V_BAM_FOLDER_DASHBOARD as
select
   *
from
   BAM_FOLDER
where
   type=1
with read only;

 comment on table V_BAM_FOLDER_DASHBOARD is
'仪表盘目录';

/*==============================================================*/
/* View: V_BAM_FOLDER_DO                                        */
/*==============================================================*/
create or replace view V_BAM_FOLDER_DO as
select
   *
from
   BAM_FOLDER
where
   type=2
with read only;

 comment on table V_BAM_FOLDER_DO is
'仪表盘对象目录';

alter table BAM_ALERT
   add constraint FK_BAM_ALER_REFERENCE_BAM_BUSI foreign key (BUSINESSSCENARIOID)
      references BAM_BUSINESSSCENARIO (ID);

alter table BAM_BUSINESSSCENARIO
   add constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_DASHBOARD
   add constraint FK_BAM_DASH_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_DASHBOARDOBJECT
   add constraint FK_BAM_DO_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_FOLDER
   add constraint FK_BAM_FOLD_REFERENCE_BAM_FOLD foreign key (PARENTID)
      references BAM_FOLDER (ID);

alter table BAM_MESSAGESUBSCRIBER
   add constraint FK_BAM_MESS_REFERENCE_BAM_ALER foreign key (ALERTMESSAGEID)
      references BAM_ALERTMESSAGE (ID);

alter table BAM_RULE
   add constraint FK_BAM_RULE_REFERENCE_BAM_BUSI foreign key (BUSINESSSCENARIOID)
      references BAM_BUSINESSSCENARIO (ID);

alter table BAM_RULE
   add constraint FK_BAM_RULE_REFERENCE_BAM_ALER foreign key (ACTIONALERTID)
      references BAM_ALERT (ID);

alter table BAM_SUBSCIBER
   add constraint FK_BAM_SUBS_REFERENCE_BAM_ALER foreign key (ALERTID)
      references BAM_ALERT (ID);

/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/6/15 15:01:09                           */
/*==============================================================*/


create sequence S_BAM_JARFILE;

create sequence S_BAM_RULETEMPLATE;

create sequence S_BAM_RULETEMPLATEPARAMETER;

create sequence S_BAM_TASK;

create sequence S_BAM_TASKWATCHER;

--alter table BAM_ALERTMESSAGE_ARCH add ARCHIVETIME DATE;

--comment on column BAM_ALERTMESSAGE_ARCH.ARCHIVETIME is
--'归档时间';

alter table BAM_BUSINESSVIEW add TYPE NUMBER(11) default 0;

comment on column BAM_BUSINESSVIEW.TYPE is
'类型';

alter table BAM_BUSINESSVIEW add METADATA CLOB;

comment on column BAM_BUSINESSVIEW.METADATA is
'设计元数据';

alter table BAM_DASHBOARD add SEQ NUMBER(5) default 1;

comment on column BAM_DASHBOARD.SEQ is
'顺序';

alter table BAM_FOLDER add SEQ NUMBER(5) default 1;

comment on column BAM_FOLDER.SEQ is
'顺序';

/*==============================================================*/
/* Table: BAM_JARFILE                                           */
/*==============================================================*/
create table BAM_JARFILE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(255),
   CODE                 VARCHAR2(255),
   PATH                 VARCHAR2(1000),
   DESCRIPTION          VARCHAR2(500),
   constraint PK_BAM_JARFILE primary key (ID)
);

comment on table BAM_JARFILE is
'jar包管理';

comment on column BAM_JARFILE.PATH is
'文件路径';

comment on column BAM_JARFILE.DESCRIPTION is
'描述';

/*==============================================================*/
/* Index: "Index_12"                                            */
/*==============================================================*/
create unique index "Index_12" on BAM_JARFILE (
   CODE ASC
);

--alter table BAM_MESSAGESUBSCRIBER_ARCH add ARCHIVETIME DATE;

--comment on column BAM_MESSAGESUBSCRIBER_ARCH.ARCHIVETIME is
--'归档时间';

alter table BAM_RULE add RULETEMPLATEID NUMBER(18);

/*==============================================================*/
/* Table: BAM_RULETEMPLATE                                      */
/*==============================================================*/
create table BAM_RULETEMPLATE  (
   ID                   NUMBER(18)                      not null,
   BUSINESSSCENARIOID   NUMBER(18),
   NAME                 VARCHAR2(100),
   CODE                 VARCHAR2(100),
   DESCRIPTION          VARCHAR2(1000),
   CONDITION            VARCHAR2(1000),
   HOLDSFOR             NUMBER(4),
   RESETCONDITION       VARCHAR2(1000),
   RESETHOLDSFOR        NUMBER(4),
   SEVERITY             NUMBER(11),
   SUBJECT              VARCHAR2(1000),
   BODY                 CLOB,
   SHOWCONDITION        NUMBER(1)                      default 1,
   constraint PK_BAM_RULETEMPLATE primary key (ID)
);

comment on table BAM_RULETEMPLATE is
'规则模板';

comment on column BAM_RULETEMPLATE.NAME is
'名称';

comment on column BAM_RULETEMPLATE.CODE is
'编码';

comment on column BAM_RULETEMPLATE.DESCRIPTION is
'描述';

comment on column BAM_RULETEMPLATE.CONDITION is
'触发条件';

comment on column BAM_RULETEMPLATE.HOLDSFOR is
'持续时间';

comment on column BAM_RULETEMPLATE.RESETCONDITION is
'重置条件';

comment on column BAM_RULETEMPLATE.RESETHOLDSFOR is
'重置持续时间';

comment on column BAM_RULETEMPLATE.SEVERITY is
'重要程度';

comment on column BAM_RULETEMPLATE.SUBJECT is
'主题';

comment on column BAM_RULETEMPLATE.BODY is
'内容';

comment on column BAM_RULETEMPLATE.SHOWCONDITION is
'是否显示条件';

/*==============================================================*/
/* Index: "Index_14"                                            */
/*==============================================================*/
create unique index "Index_14" on BAM_RULETEMPLATE (
   CODE ASC
);

/*==============================================================*/
/* Table: BAM_RULETEMPLATEPARAMETER                             */
/*==============================================================*/
create table BAM_RULETEMPLATEPARAMETER  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100),
   TYPE                 VARCHAR2(20)                   default 'STRING',
   DESCRIPTION          VARCHAR2(1000),
   CAPTION              VARCHAR2(500),
   RULETEMPLATEID       NUMBER(18),
   constraint PK_BAM_RULETEMPLATEPARAMETER primary key (ID)
);

comment on table BAM_RULETEMPLATEPARAMETER is
'规则模板参数';

comment on column BAM_RULETEMPLATEPARAMETER.NAME is
'参数名';

comment on column BAM_RULETEMPLATEPARAMETER.TYPE is
'参数类型，默认STRING，也可以是NUMBER';

comment on column BAM_RULETEMPLATEPARAMETER.DESCRIPTION is
'描述';

comment on column BAM_RULETEMPLATEPARAMETER.CAPTION is
'输入提示';

/*==============================================================*/
/* Index: "Index_15"                                            */
/*==============================================================*/
create unique index "Index_15" on BAM_RULETEMPLATEPARAMETER (
   NAME ASC
);

/*==============================================================*/
/* Table: BAM_TASK                                              */
/*==============================================================*/
create table BAM_TASK  (
   ID                   NUMBER(18)                      not null,
   SUBJECT              VARCHAR2(255),
   MEMO                 CLOB,
   OWNER                VARCHAR2(255),
   SERVERITY            NUMBER(11),
   STATE                NUMBER(11),
   DEADLINE             DATE,
   CREATEDATE           DATE,
   LASTUPDATED          DATE,
   ORIGINSOURCE         VARCHAR2(255),
   ORIGINCONTENT        CLOB,
   ALERTMESSAGEID       NUMBER(18),
   constraint PK_BAM_TASK primary key (ID)
);

comment on table BAM_TASK is
'任务';

comment on column BAM_TASK.SUBJECT is
'标题';

comment on column BAM_TASK.MEMO is
'备注，不断追加';

comment on column BAM_TASK.OWNER is
'当前处理人，用户名';

comment on column BAM_TASK.SERVERITY is
'重要程度，数据字典，0 low，1 normal，2 high';

comment on column BAM_TASK.STATE is
'状态，完成/未完成';

comment on column BAM_TASK.DEADLINE is
'最后期限';

comment on column BAM_TASK.CREATEDATE is
'创建时间';

comment on column BAM_TASK.LASTUPDATED is
'最后更新时间';

comment on column BAM_TASK.ORIGINSOURCE is
'最初来源，可能是告警的标题';

comment on column BAM_TASK.ORIGINCONTENT is
'最初来源的内容，可能是告警的内容';

/*==============================================================*/
/* Index: "Index_13"                                            */
/*==============================================================*/
create index "Index_13" on BAM_TASK (
   ALERTMESSAGEID ASC
);

/*==============================================================*/
/* Table: BAM_TASKWATCHER                                       */
/*==============================================================*/
create table BAM_TASKWATCHER  (
   ID                   NUMBER(18)                      not null,
   TASKID               NUMBER(18)                      not null,
   USERNAME             VARCHAR2(255),
   constraint PK_BAM_TASKWATCHER primary key (ID)
);

comment on table BAM_TASKWATCHER is
'任务观察者，不能修改任务信息';

/*==============================================================*/
/* Table: BAM_TASKWATCHER_ARCH                                  */
/*==============================================================*/
create table BAM_TASKWATCHER_ARCH  (
   ID                   NUMBER(18),
   TASKID               NUMBER(18),
   USERNAME             VARCHAR2(255),
   ARCHIVETIME          DATE
);

comment on table BAM_TASKWATCHER_ARCH is
'任务观察者归档表';

comment on column BAM_TASKWATCHER_ARCH.ARCHIVETIME is
'归档时间';

/*==============================================================*/
/* Table: BAM_TASK_ARCH                                         */
/*==============================================================*/
create table BAM_TASK_ARCH  (
   ID                   NUMBER(18),
   SUBJECT              VARCHAR2(255),
   MEMO                 CLOB,
   OWNER                VARCHAR2(255),
   SERVERITY            NUMBER(11),
   STATE                NUMBER(11),
   DEADLINE             DATE,
   CREATEDATE           DATE,
   LASTUPDATED          DATE,
   ORIGINSOURCE         VARCHAR2(255),
   ORIGINCONTENT        CLOB,
   ALERTMESSAGEID       NUMBER(18)
);

comment on table BAM_TASK_ARCH is
'任务归档表';

comment on column BAM_TASK_ARCH.SUBJECT is
'标题';

comment on column BAM_TASK_ARCH.MEMO is
'备注，不断追加';

comment on column BAM_TASK_ARCH.OWNER is
'当前处理人，用户名';

comment on column BAM_TASK_ARCH.SERVERITY is
'重要程度，数据字典，0 low，1 normal，2 high';

comment on column BAM_TASK_ARCH.STATE is
'状态，完成/未完成';

comment on column BAM_TASK_ARCH.DEADLINE is
'最后期限';

comment on column BAM_TASK_ARCH.CREATEDATE is
'创建时间';

comment on column BAM_TASK_ARCH.LASTUPDATED is
'最后更新时间';

comment on column BAM_TASK_ARCH.ORIGINSOURCE is
'最初来源，可能是告警的标题';

comment on column BAM_TASK_ARCH.ORIGINCONTENT is
'最初来源的内容，可能是告警的内容';

alter table BAM_RULE
   add constraint FK_RULE_REFERENCE_RULETEMP foreign key (RULETEMPLATEID)
      references BAM_RULETEMPLATE (ID);

alter table BAM_RULETEMPLATE
   add constraint FK_RULETEMP_REFERENCE_BAM_BUSI foreign key (BUSINESSSCENARIOID)
      references BAM_BUSINESSSCENARIO (ID);

alter table BAM_RULETEMPLATEPARAMETER
   add constraint FK_PARA_REFERENCE_RULETEMP foreign key (RULETEMPLATEID)
      references BAM_RULETEMPLATE (ID);

alter table BAM_TASKWATCHER
   add constraint FK_BAM_TASK_REFERENCE_BAM_TASK foreign key (TASKID)
      references BAM_TASK (ID);

/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/8/24 14:13:00                           */
/*==============================================================*/


alter table BAM_BUSINESSSCENARIO
   drop constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD;

alter table BAM_RULE
   drop constraint FK_RULE_REFERENCE_RULETEMP;

--alter table BAM_ALERTMESSAGE_ARCH 
  -- drop column ARCHIVETIME;

--alter table BAM_MESSAGESUBSCRIBER_ARCH 
  -- drop column ARCHIVETIME;

alter table BAM_RULE 
   drop column RULETEMPLATEID;

create sequence S_BAM_ACCESSFILTER;

create sequence S_BAM_ANALYZEVIEW;

create sequence S_BAM_BUSINESSRULE;

create sequence S_BAM_QUERYVIEW;

create sequence S_BAM_ROLE_DASHBOARD;

create sequence S_BAM_R_ACCESSFILTER_DO;

create sequence S_BAM_TASKTYPE;

create sequence S_BAM_USER_DASHBOARD;

create sequence S_BAM_WORKPOSITION_DASHBOARD;

create sequence S_BAM_WORKSPACE;

/*==============================================================*/
/* User: SMARTX_BAM                                             */
/*==============================================================*/
create user SMARTX_BAM identified by '';

/*==============================================================*/
/* Table: BAM_ACCESSFILTER                                      */
/*==============================================================*/
create table BAM_ACCESSFILTER  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100),
   CODE                 VARCHAR2(100),
   DESCRIPTION          VARCHAR2(1000),
   CONDITION            VARCHAR2(1000),
   constraint PK_BAM_ACCESSFILTER primary key (ID)
);

comment on table BAM_ACCESSFILTER is
'仪表盘对象访问控制器';

comment on column BAM_ACCESSFILTER.DESCRIPTION is
'描述';

/*==============================================================*/
/* Index: "Index_50"                                            */
/*==============================================================*/
create unique index "Index_50" on BAM_ACCESSFILTER (
   CODE ASC
);

/*==============================================================*/
/* Index: "Index_20"                                            */
/*==============================================================*/
create index "Index_20" on BAM_ALERTMESSAGE (
   ALERTID ASC
);

/*==============================================================*/
/* Index: "Index_21"                                            */
/*==============================================================*/
create index "Index_21" on BAM_ALERTMESSAGE (
   STATUS ASC
);

/*==============================================================*/
/* Index: "Index_22"                                            */
/*==============================================================*/
create index "Index_22" on BAM_ALERTMESSAGE (
   ACTIVATETIME ASC
);

/*==============================================================*/
/* Table: BAM_ANALYZEVIEW                                       */
/*==============================================================*/
create table BAM_ANALYZEVIEW  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100),
   CODE                 VARCHAR2(100),
   METADATA             CLOB,
   FOLDERID             NUMBER(18),
   constraint PK_BAM_ANALYZEVIEW primary key (ID)
);

/*==============================================================*/
/* Index: "Index_27"                                            */
/*==============================================================*/
create unique index "Index_27" on BAM_ANALYZEVIEW (
   CODE ASC
);

/*==============================================================*/
/* Index: "Index_30"                                            */
/*==============================================================*/
create index "Index_30" on BAM_ANALYZEVIEW (
   FOLDERID ASC
);

/*==============================================================*/
/* Table: BAM_BUSINESSRULE                                      */
/*==============================================================*/
create table BAM_BUSINESSRULE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(255),
   CODE                 VARCHAR2(255),
   CREATETIME           DATE,
   DESCRIPTION          VARCHAR2(1000),
   LASTUPDATETIME       DATE,
   PARAMETERINFO        CLOB,
   RULETEMPATEID        NUMBER(18),
   constraint PK_BAM_BUSINESSRULE primary key (ID)
);

comment on table BAM_BUSINESSRULE is
'业务规则';

comment on column BAM_BUSINESSRULE.NAME is
'名称';

comment on column BAM_BUSINESSRULE.CODE is
'编码';

comment on column BAM_BUSINESSRULE.CREATETIME is
'创建时间';

comment on column BAM_BUSINESSRULE.DESCRIPTION is
'描述';

comment on column BAM_BUSINESSRULE.LASTUPDATETIME is
'上次更新时间';

comment on column BAM_BUSINESSRULE.PARAMETERINFO is
'参数值';

comment on column BAM_BUSINESSRULE.RULETEMPATEID is
'关联的规则模板ID';

/*==============================================================*/
/* Index: "Index_16"                                            */
/*==============================================================*/
create unique index "Index_16" on BAM_BUSINESSRULE (
   CODE ASC
);

/*==============================================================*/
/* Index: "Index_32"                                            */
/*==============================================================*/
create index "Index_32" on BAM_BUSINESSSCENARIO (
   FOLDERID ASC
);

alter table BAM_BUSINESSVIEW add FOLDERID NUMBER(18);

/*==============================================================*/
/* Index: "Index_17"                                            */
/*==============================================================*/
create unique index "Index_17" on BAM_BUSINESSVIEW (
   STREAMNAME ASC
);

/*==============================================================*/
/* Index: "Index_18"                                            */
/*==============================================================*/
create unique index "Index_18" on BAM_BUSINESSVIEW (
   STREAMWINDOWNAME ASC
);

/*==============================================================*/
/* Index: "Index_37"                                            */
/*==============================================================*/
create index "Index_37" on BAM_BUSINESSVIEW (
   FOLDERID ASC
);

/*==============================================================*/
/* Index: "Index_38"                                            */
/*==============================================================*/
create index "Index_38" on BAM_BUSINESSVIEW (
   TYPE ASC
);

/*==============================================================*/
/* Index: "Index_34"                                            */
/*==============================================================*/
create index "Index_34" on BAM_DASHBOARD (
   FOLDERID ASC
);

/*==============================================================*/
/* Index: "Index_35"                                            */
/*==============================================================*/
create index "Index_35" on BAM_DASHBOARD (
   SEQ ASC
);

/*==============================================================*/
/* Index: "Index_33"                                            */
/*==============================================================*/
create index "Index_33" on BAM_DASHBOARDOBJECT (
   FOLDERID ASC
);

/*==============================================================*/
/* Index: "Index_39"                                            */
/*==============================================================*/
create index "Index_39" on BAM_DASHBOARDOBJECT (
   TYPE ASC
);

/*==============================================================*/
/* Index: "Index_29"                                            */
/*==============================================================*/
create index "Index_29" on BAM_FOLDER (
   PARENTID ASC
);

/*==============================================================*/
/* Index: "Index_36"                                            */
/*==============================================================*/
create index "Index_36" on BAM_FOLDER (
   TYPE ASC
);

/*==============================================================*/
/* Index: "Index_23"                                            */
/*==============================================================*/
create index "Index_23" on BAM_MESSAGESUBSCRIBER (
   ALERTMESSAGEID ASC
);

/*==============================================================*/
/* Index: "Index_24"                                            */
/*==============================================================*/
create index "Index_24" on BAM_MESSAGESUBSCRIBER (
   USERNAME ASC
);

/*==============================================================*/
/* Table: BAM_QUERYVIEW                                         */
/*==============================================================*/
create table BAM_QUERYVIEW  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100),
   CODE                 VARCHAR2(100),
   DATASOURCENAME       VARCHAR2(100),
   SQL                  CLOB,
   FOLDERID             NUMBER(18),
   DESCRIPTION          VARCHAR2(1000),
   METADATA             CLOB,
   constraint PK_BAM_QUERYVIEW primary key (ID)
);

comment on table BAM_QUERYVIEW is
'查询视图';

comment on column BAM_QUERYVIEW.DATASOURCENAME is
'数据源名称';

comment on column BAM_QUERYVIEW.METADATA is
'元数据';

/*==============================================================*/
/* Index: "Index_19"                                            */
/*==============================================================*/
create unique index "Index_19" on BAM_QUERYVIEW (
   CODE ASC
);

/*==============================================================*/
/* Index: "Index_31"                                            */
/*==============================================================*/
create index "Index_31" on BAM_QUERYVIEW (
   ID ASC
);

/*==============================================================*/
/* Table: BAM_ROLE_DASHBOARD                                    */
/*==============================================================*/
create table BAM_ROLE_DASHBOARD  (
   ID                   NUMBER(18)                      not null,
   ROLEID               NUMBER(18),
   DASHBOARDID          NUMBER(18),
   constraint PK_BAM_ROLE_DASHBOARD primary key (ID)
);

comment on table BAM_ROLE_DASHBOARD is
'角色仪表盘权限';

/*==============================================================*/
/* Index: "Index_48"                                            */
/*==============================================================*/
create index "Index_48" on BAM_ROLE_DASHBOARD (
   DASHBOARDID ASC
);

/*==============================================================*/
/* Index: "Index_49"                                            */
/*==============================================================*/
create index "Index_49" on BAM_ROLE_DASHBOARD (
   ROLEID ASC
);

comment on table BAM_RULE is
'告警规则';

alter table BAM_RULE add BUSINESSRULEID NUMBER(18);

comment on column BAM_RULE.BUSINESSRULEID is
'关联的业务规则ID';

/*==============================================================*/
/* Index: "Index_25"                                            */
/*==============================================================*/
create index "Index_25" on BAM_RULE (
   ACTIONTYPE ASC
);

/*==============================================================*/
/* Index: "Index_26"                                            */
/*==============================================================*/
create index "Index_26" on BAM_RULE (
   ACTIONALERTID ASC
);

/*==============================================================*/
/* Table: BAM_R_ACCESSFILTER_DO                                 */
/*==============================================================*/
create table BAM_R_ACCESSFILTER_DO  (
   ID                   NUMBER(18)                      not null,
   ACCESSFILTERID       NUMBER(18),
   DASHBOARDOBJECTID    NUMBER(18),
   constraint PK_BAM_R_ACCESSFILTER_DO primary key (ID)
);

comment on table BAM_R_ACCESSFILTER_DO is
'访问控制器与仪表盘对象关联表';

/*==============================================================*/
/* Index: "Index_51"                                            */
/*==============================================================*/
create index "Index_51" on BAM_R_ACCESSFILTER_DO (
   ACCESSFILTERID ASC
);

/*==============================================================*/
/* Index: "Index_52"                                            */
/*==============================================================*/
create index "Index_52" on BAM_R_ACCESSFILTER_DO (
   DASHBOARDOBJECTID ASC
);

alter table BAM_TASK add TYPEID NUMBER(18);

/*==============================================================*/
/* Index: "Index_40"                                            */
/*==============================================================*/
create index "Index_40" on BAM_TASK (
   TYPEID ASC
);

/*==============================================================*/
/* Index: "Index_41"                                            */
/*==============================================================*/
create index "Index_41" on BAM_TASK (
   OWNER ASC
);

/*==============================================================*/
/* Table: BAM_TASKTYPE                                          */
/*==============================================================*/
create table BAM_TASKTYPE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(255),
   CODE                 VARCHAR2(100),
   DESCRIPTION          VARCHAR2(1000),
   constraint PK_BAM_TASKTYPE primary key (ID)
);

comment on table BAM_TASKTYPE is
'任务类别';

comment on column BAM_TASKTYPE.NAME is
'名称';

comment on column BAM_TASKTYPE.CODE is
'编码';

comment on column BAM_TASKTYPE.DESCRIPTION is
'描述';

/*==============================================================*/
/* Index: "Index_28"                                            */
/*==============================================================*/
create unique index "Index_28" on BAM_TASKTYPE (
   CODE ASC
);

alter table BAM_TASK_ARCH add ARCHIVETIME DATE;

comment on column BAM_TASK_ARCH.ARCHIVETIME is
'归档时间';

comment on column BAM_TASK_ARCH.ALERTMESSAGEID is
'告警消息ID';

/*==============================================================*/
/* Table: BAM_USER_DASHBOARD                                    */
/*==============================================================*/
create table BAM_USER_DASHBOARD  (
   ID                   NUMBER(18)                      not null,
   USERID               NUMBER(18),
   DASHBOARDID          NUMBER(18),
   constraint PK_BAM_USER_DASHBOARD primary key (ID)
);

comment on table BAM_USER_DASHBOARD is
'用户仪表盘权限';

/*==============================================================*/
/* Index: "Index_46"                                            */
/*==============================================================*/
create index "Index_46" on BAM_USER_DASHBOARD (
   DASHBOARDID ASC
);

/*==============================================================*/
/* Index: "Index_47"                                            */
/*==============================================================*/
create index "Index_47" on BAM_USER_DASHBOARD (
   USERID ASC
);

/*==============================================================*/
/* Table: BAM_WORKPOSITION_DASHBOARD                            */
/*==============================================================*/
create table BAM_WORKPOSITION_DASHBOARD  (
   ID                   NUMBER(18)                      not null,
   WORKPOSITIONID       NUMBER(18),
   DASHBOARDID          NUMBER(18),
   constraint PK_BAM_WORKPOSITION_DASHBOARD primary key (ID)
);

comment on table BAM_WORKPOSITION_DASHBOARD is
'工位仪表盘权限';

/*==============================================================*/
/* Index: "Index_44"                                            */
/*==============================================================*/
create index "Index_44" on BAM_WORKPOSITION_DASHBOARD (
   WORKPOSITIONID ASC
);

/*==============================================================*/
/* Index: "Index_45"                                            */
/*==============================================================*/
create index "Index_45" on BAM_WORKPOSITION_DASHBOARD (
   DASHBOARDID ASC
);

/*==============================================================*/
/* Table: BAM_WORKSPACE                                         */
/*==============================================================*/
create table BAM_WORKSPACE  (
   ID                   NUMBER(18)                      not null,
   NAME                 VARCHAR2(100),
   CODE                 VARCHAR2(100),
   METADATA             CLOB,
   OWNER                VARCHAR2(255),
   CREATETIME           DATE,
   LASTUPDATETIME       DATE,
   constraint PK_BAM_WORKSPACE primary key (ID)
);

comment on table BAM_WORKSPACE is
'集成设计工程空间';

comment on column BAM_WORKSPACE.NAME is
'名称';

comment on column BAM_WORKSPACE.CODE is
'编码';

comment on column BAM_WORKSPACE.METADATA is
'元数据';

comment on column BAM_WORKSPACE.OWNER is
'建立者';

comment on column BAM_WORKSPACE.CREATETIME is
'创建时间';

comment on column BAM_WORKSPACE.LASTUPDATETIME is
'上次修改时间';

/*==============================================================*/
/* Index: "Index_42"                                            */
/*==============================================================*/
create unique index "Index_42" on BAM_WORKSPACE (
   CODE ASC
);

/*==============================================================*/
/* Index: "Index_43"                                            */
/*==============================================================*/
create index "Index_43" on BAM_WORKSPACE (
   OWNER ASC
);

alter table BAM_ANALYZEVIEW
   add constraint FK_BAM_ANAL_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_BUSINESSRULE
   add constraint FK_BAM_BUSI_REFERENCE_BAM_RULE foreign key (RULETEMPATEID)
      references BAM_RULETEMPLATE (ID);

alter table BAM_BUSINESSSCENARIO
   add constraint FK_BAM_BUSI_REF_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_BUSINESSVIEW
   add constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_QUERYVIEW
   add constraint FK_BAM_QUER_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_ROLE_DASHBOARD
   add constraint FK_BAM_ROLE_REFERENCE_BAM_DASH foreign key (DASHBOARDID)
      references BAM_DASHBOARD (ID);

alter table BAM_RULE
   add constraint FK_BAM_RULE__BAM_BUSIRULE foreign key (BUSINESSRULEID)
      references BAM_BUSINESSRULE (ID);

alter table BAM_R_ACCESSFILTER_DO
   add constraint FK_BAM_R_AC_REFERENCE_BAM_ACCE foreign key (ACCESSFILTERID)
      references BAM_ACCESSFILTER (ID);

alter table BAM_R_ACCESSFILTER_DO
   add constraint FK_BAM_R_AC_REFERENCE_BAM_DASH foreign key (DASHBOARDOBJECTID)
      references BAM_DASHBOARDOBJECT (ID);

alter table BAM_TASK
   add constraint FK_TASK_REFERENCE_TASKTYPE foreign key (TYPEID)
      references BAM_TASKTYPE (ID);

alter table BAM_USER_DASHBOARD
   add constraint FK_BAM_USER_REFERENCE_BAM_DASH foreign key (DASHBOARDID)
      references BAM_DASHBOARD (ID);

alter table BAM_WORKPOSITION_DASHBOARD
   add constraint FK_BAM_WORK_REFERENCE_BAM_DASH foreign key (DASHBOARDID)
      references BAM_DASHBOARD (ID);

