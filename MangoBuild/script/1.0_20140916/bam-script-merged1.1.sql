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
'�û����ã�XML��ʽ���';

comment on column BAM_ACCOUNTSETTINGS.USERNAME is
'�û���';

comment on column BAM_ACCOUNTSETTINGS.MTCODE is
'������Ԫ����ģ�����';

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
'�澯����';

comment on column BAM_ALERT.BUSINESSSCENARIOID is
'������ҵ�񳡾�ID';

comment on column BAM_ALERT.NAME is
'����';

comment on column BAM_ALERT.CODE is
'����';

comment on column BAM_ALERT.DESCRIPTION is
'����';

comment on column BAM_ALERT.SEVERITY is
'��Ҫ�̶ȣ������ֵ�
0 ��
1 һ��
2 ��';

comment on column BAM_ALERT.SUBJECT is
'����';

comment on column BAM_ALERT.BODY is
'��������';

comment on column BAM_ALERT.STATUS is
'״̬�������ֵ�
0 ��ͨ״̬ Lowered
1 ����״̬ Raised
2 ȷ��״̬ Acknowledged';

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
'�澯��Ϣ';

comment on column BAM_ALERTMESSAGE.ALERTID is
'�����ĸ澯����ID��������';

comment on column BAM_ALERTMESSAGE.SUBJECT is
'����';

comment on column BAM_ALERTMESSAGE.SEVERITY is
'��Ҫ�̶ȣ������ֵ䣬ͬALERT';

comment on column BAM_ALERTMESSAGE.BODY is
'��������';

comment on column BAM_ALERTMESSAGE.ACTIVATETIME is
'����ʱ��';

comment on column BAM_ALERTMESSAGE.STATUS is
'״̬�������ֵ�
0 ���� Raised
1 ���� Ignore';

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
'�澯ʵ���鵵��';

comment on column BAM_ALERTMESSAGE_ARCH.ALERTID is
'�����ĸ澯����ID��������';

comment on column BAM_ALERTMESSAGE_ARCH.SUBJECT is
'����';

comment on column BAM_ALERTMESSAGE_ARCH.SEVERITY is
'��Ҫ�̶ȣ������ֵ䣬ͬALERT';

comment on column BAM_ALERTMESSAGE_ARCH.BODY is
'��������';

comment on column BAM_ALERTMESSAGE_ARCH.ACTIVATETIME is
'����ʱ��';

comment on column BAM_ALERTMESSAGE_ARCH.STATUS is
'״̬�������ֵ�
0 ���� Raised
1 ���� Ignore';

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
'ҵ�񳡾�';

comment on column BAM_BUSINESSSCENARIO.FOLDERID is
'����Ŀ¼';

comment on column BAM_BUSINESSSCENARIO.NAME is
'����';

comment on column BAM_BUSINESSSCENARIO.CODE is
'����';

comment on column BAM_BUSINESSSCENARIO.DESCRIPTION is
'����';

comment on column BAM_BUSINESSSCENARIO.DATASOURCETYPE is
'����������Դ���ͣ������ֵ�
0 ҵ����ͼ
1 ������';

comment on column BAM_BUSINESSSCENARIO.DATASOURCECODE is
'����������Դ����';

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
'ҵ����ͼ';

comment on column BAM_BUSINESSVIEW.NAME is
'����';

comment on column BAM_BUSINESSVIEW.CODE is
'����';

comment on column BAM_BUSINESSVIEW.DESCRIPTION is
'����';

comment on column BAM_BUSINESSVIEW.ISPERSISTVIEWDATA is
'�Ƿ�־û���ͼ����
�����ֵ� ��/��';

comment on column BAM_BUSINESSVIEW.STREAMNAME is
'��Ӧ���¼�������';

comment on column BAM_BUSINESSVIEW.STREAMWINDOWNAME is
'��Ӧ���¼�����������';

comment on column BAM_BUSINESSVIEW.STREAMMODULENAME is
'��������ģ��';

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
'�Ǳ���';

comment on column BAM_DASHBOARD.NAME is
'����';

comment on column BAM_DASHBOARD.CODE is
'����';

comment on column BAM_DASHBOARD.DESCRIPTION is
'����';

comment on column BAM_DASHBOARD.REFRESHINTERVAL is
'ˢ�¼������λ��';

comment on column BAM_DASHBOARD.LAYOUT_MTCODE is
'���ֵ�Ԫ����ģ�����';

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
'�Ǳ��̶���';

comment on column BAM_DASHBOARDOBJECT.FOLDERID is
'����Ŀ¼ID';

comment on column BAM_DASHBOARDOBJECT.NAME is
'����';

comment on column BAM_DASHBOARDOBJECT.CODE is
'����';

comment on column BAM_DASHBOARDOBJECT.DESCRIPTION is
'����';

comment on column BAM_DASHBOARDOBJECT.TYPE is
'���ͣ������ֵ�
0 ��ͼ PieChart
1 ���ͼ CombinationChart
2 �ֲ�ͼ DistributionChart
3 ͸�ӻ��ͼ PivotCombinationChart
4 ����ͼ GeographyChart
5 ָʾ�� Indicator
6 �б� Table';

comment on column BAM_DASHBOARDOBJECT.MTCODE is
'������Ԫ����ģ�����';

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
'�¼�Դ';

comment on column BAM_EVENTDATASOURCE.NAME is
'����';

comment on column BAM_EVENTDATASOURCE.ADAPTORCLASS is
'������ʵ����';

comment on column BAM_EVENTDATASOURCE.STATUS is
'״̬�������ֵ�
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
'����Ŀ¼';

comment on column BAM_FOLDER.PARENTID is
'��Ŀ¼ID';

comment on column BAM_FOLDER.NAME is
'����';

comment on column BAM_FOLDER.CODE is
'����';

comment on column BAM_FOLDER.DESCRIPTION is
'����';

comment on column BAM_FOLDER.TYPE is
'���ͣ������ֵ�
0 ҵ��  BusinessActivity
1 �Ǳ��� Dashboard
2 �Ǳ��̶��� DashboardObject';

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
'�澯��Ϣ��ʵ�ʶ�����';

comment on column BAM_MESSAGESUBSCRIBER.ALERTMESSAGEID is
'�����ĸ澯��ϢID';

comment on column BAM_MESSAGESUBSCRIBER.USERNAME is
'�������û���';

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
'�澯��Ϣ��ʵ�ʶ����߹鵵��';

comment on column BAM_MESSAGESUBSCRIBER_ARCH.ALERTMESSAGEID is
'�����ĸ澯��ϢID';

comment on column BAM_MESSAGESUBSCRIBER_ARCH.USERNAME is
'�������û���';

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
'��ϵ������Դ';

comment on column BAM_RELATIONALDATASOURCE.NAME is
'����';

comment on column BAM_RELATIONALDATASOURCE.TYPE is
'���ͣ������ֵ�
0 datasource
1 datasourcefactory
2 drivermanager';

comment on column BAM_RELATIONALDATASOURCE.CONFIGURATION is
'XML������Ϣ';

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
'ҵ�����';

comment on column BAM_RULE.BUSINESSSCENARIOID is
'ҵ�񳡾�ID';

comment on column BAM_RULE.NAME is
'����';

comment on column BAM_RULE.DESCRIPTION is
'����';

comment on column BAM_RULE.CONDITION is
'��������';

comment on column BAM_RULE.HOLDSFOR is
'���������ϲ���������ʱ���Ժ��ִ�ж���
��λ��';

comment on column BAM_RULE.ACTIONALERTID is
'�����ĸ澯ID';

comment on column BAM_RULE.ACTIONTYPE is
'�������ͣ������ֵ�
0 ÿ�ξ����� Send Everytime
1 ֻ����һ�� Send Once
2 �����ϴθ澯 Reset';

comment on column BAM_RULE.STREAMMODULENAME is
'��������ģ������';

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
'������';

comment on column BAM_SUBSCIBER.OBJECTNAME is
'����������';

comment on column BAM_SUBSCIBER.TYPE is
'���ͣ������ֵ�
0 �û�
1 ��ɫ
2 ��λ';

comment on column BAM_SUBSCIBER.SUBSCRIPTION is
'���ķ�ʽ�������ֵ�
0 ���� Mandatory
1 ��ѡ Optional';

comment on column BAM_SUBSCIBER.DELIVERYTYPE is
'���ͷ�ʽ�������ֵ�
0 ��ͨ
1 �ʼ�
2 ȫ��';

comment on column BAM_SUBSCIBER.ALERTID is
'�����ĸ澯����ID';

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
'ϵͳ����';

comment on column BAM_SYSTEMSETTINGS.KEY is
'������ֵ����';

comment on column BAM_SYSTEMSETTINGS.KEYCN is
'���Ĳ�����';

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
'ҵ��Ŀ¼';

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
'�Ǳ���Ŀ¼';

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
'�Ǳ��̶���Ŀ¼';

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

alter table BAM_ALERTMESSAGE_ARCH add ARCHIVETIME DATE;

comment on column BAM_ALERTMESSAGE_ARCH.ARCHIVETIME is
'�鵵ʱ��';

alter table BAM_BUSINESSVIEW add TYPE NUMBER(11) default 0;

comment on column BAM_BUSINESSVIEW.TYPE is
'����';

alter table BAM_BUSINESSVIEW add METADATA CLOB;

comment on column BAM_BUSINESSVIEW.METADATA is
'���Ԫ����';

alter table BAM_DASHBOARD add SEQ NUMBER(5) default 1;

comment on column BAM_DASHBOARD.SEQ is
'˳��';

alter table BAM_FOLDER add SEQ NUMBER(5) default 1;

comment on column BAM_FOLDER.SEQ is
'˳��';

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
'jar������';

comment on column BAM_JARFILE.PATH is
'�ļ�·��';

comment on column BAM_JARFILE.DESCRIPTION is
'����';

/*==============================================================*/
/* Index: "Index_12"                                            */
/*==============================================================*/
create unique index "Index_12" on BAM_JARFILE (
   CODE ASC
);

alter table BAM_MESSAGESUBSCRIBER_ARCH add ARCHIVETIME DATE;

comment on column BAM_MESSAGESUBSCRIBER_ARCH.ARCHIVETIME is
'�鵵ʱ��';

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
'����ģ��';

comment on column BAM_RULETEMPLATE.NAME is
'����';

comment on column BAM_RULETEMPLATE.CODE is
'����';

comment on column BAM_RULETEMPLATE.DESCRIPTION is
'����';

comment on column BAM_RULETEMPLATE.CONDITION is
'��������';

comment on column BAM_RULETEMPLATE.HOLDSFOR is
'����ʱ��';

comment on column BAM_RULETEMPLATE.RESETCONDITION is
'��������';

comment on column BAM_RULETEMPLATE.RESETHOLDSFOR is
'���ó���ʱ��';

comment on column BAM_RULETEMPLATE.SEVERITY is
'��Ҫ�̶�';

comment on column BAM_RULETEMPLATE.SUBJECT is
'����';

comment on column BAM_RULETEMPLATE.BODY is
'����';

comment on column BAM_RULETEMPLATE.SHOWCONDITION is
'�Ƿ���ʾ����';

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
'����ģ�����';

comment on column BAM_RULETEMPLATEPARAMETER.NAME is
'������';

comment on column BAM_RULETEMPLATEPARAMETER.TYPE is
'�������ͣ�Ĭ��STRING��Ҳ������NUMBER';

comment on column BAM_RULETEMPLATEPARAMETER.DESCRIPTION is
'����';

comment on column BAM_RULETEMPLATEPARAMETER.CAPTION is
'������ʾ';

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
'����';

comment on column BAM_TASK.SUBJECT is
'����';

comment on column BAM_TASK.MEMO is
'��ע������׷��';

comment on column BAM_TASK.OWNER is
'��ǰ�����ˣ��û���';

comment on column BAM_TASK.SERVERITY is
'��Ҫ�̶ȣ������ֵ䣬0 low��1 normal��2 high';

comment on column BAM_TASK.STATE is
'״̬�����/δ���';

comment on column BAM_TASK.DEADLINE is
'�������';

comment on column BAM_TASK.CREATEDATE is
'����ʱ��';

comment on column BAM_TASK.LASTUPDATED is
'������ʱ��';

comment on column BAM_TASK.ORIGINSOURCE is
'�����Դ�������Ǹ澯�ı���';

comment on column BAM_TASK.ORIGINCONTENT is
'�����Դ�����ݣ������Ǹ澯������';

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
'����۲��ߣ������޸�������Ϣ';

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
'����۲��߹鵵��';

comment on column BAM_TASKWATCHER_ARCH.ARCHIVETIME is
'�鵵ʱ��';

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
'����鵵��';

comment on column BAM_TASK_ARCH.SUBJECT is
'����';

comment on column BAM_TASK_ARCH.MEMO is
'��ע������׷��';

comment on column BAM_TASK_ARCH.OWNER is
'��ǰ�����ˣ��û���';

comment on column BAM_TASK_ARCH.SERVERITY is
'��Ҫ�̶ȣ������ֵ䣬0 low��1 normal��2 high';

comment on column BAM_TASK_ARCH.STATE is
'״̬�����/δ���';

comment on column BAM_TASK_ARCH.DEADLINE is
'�������';

comment on column BAM_TASK_ARCH.CREATEDATE is
'����ʱ��';

comment on column BAM_TASK_ARCH.LASTUPDATED is
'������ʱ��';

comment on column BAM_TASK_ARCH.ORIGINSOURCE is
'�����Դ�������Ǹ澯�ı���';

comment on column BAM_TASK_ARCH.ORIGINCONTENT is
'�����Դ�����ݣ������Ǹ澯������';

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

