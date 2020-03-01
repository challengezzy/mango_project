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
'归档时间';

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

alter table BAM_MESSAGESUBSCRIBER_ARCH add ARCHIVETIME DATE;

comment on column BAM_MESSAGESUBSCRIBER_ARCH.ARCHIVETIME is
'归档时间';

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

