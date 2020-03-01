/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/8/24 14:13:00                           */
/*==============================================================*/


alter table BAM_BUSINESSSCENARIO
   drop constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD;

alter table BAM_RULE
   drop constraint FK_RULE_REFERENCE_RULETEMP;

alter table BAM_ALERTMESSAGE_ARCH 
   drop column ARCHIVETIME;

alter table BAM_MESSAGESUBSCRIBER_ARCH 
   drop column ARCHIVETIME;

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

