/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/11/23 16:26:32                          */
/*==============================================================*/


alter table BAM_BUSINESSSCENARIO
   drop constraint FK_BAM_BUSI_REFERENCE_BAM_FOLD;

alter table BAM_QUERYVIEW
   drop constraint FK_BAM_QUER_REFERENCE_BAM_FOLD;

alter table BAM_RULE
   drop constraint FK_RULE_REFERENCE_RULETEMP;

drop index "Index_19";

alter table BAM_QUERYVIEW
   drop primary key cascade;

drop table "tmp_BAM_QUERYVIEW" cascade constraints;

rename BAM_QUERYVIEW to "tmp_BAM_QUERYVIEW";

alter table BAM_RULE 
   drop column RULETEMPLATEID;

create sequence S_BAM_ENTITYMODEL;

alter table BAM_ALERTMESSAGE_ARCH add ARCHIVETIME DATE;

comment on column BAM_ALERTMESSAGE_ARCH.ARCHIVETIME is
'�鵵ʱ��';

/*==============================================================*/
/* Table: BAM_ENTITYMODEL                                       */
/*==============================================================*/
create table BAM_ENTITYMODEL  (
   ID                   NUMBER                          not null,
   NAME                 VARCHAR(100),
   CODE                 VARCHAR(100),
   DESCRIPTION          VARCHAR(1000),
   MTCODE               VARCHAR(255),
   constraint PK_BAM_ENTITYMODEL primary key (ID)
);

comment on table BAM_ENTITYMODEL is
'����ʵ��ģ���б�';

comment on column BAM_ENTITYMODEL.NAME is
'����';

comment on column BAM_ENTITYMODEL.CODE is
'����';

comment on column BAM_ENTITYMODEL.DESCRIPTION is
'����';

comment on column BAM_ENTITYMODEL.MTCODE is
'Ԫ����ģ�����';

/*==============================================================*/
/* Index: IDX_ENTITYMODEL_1                                     */
/*==============================================================*/
create unique index IDX_ENTITYMODEL_1 on BAM_ENTITYMODEL (
   CODE ASC
);

alter table BAM_MESSAGESUBSCRIBER_ARCH add ARCHIVETIME DATE;

comment on column BAM_MESSAGESUBSCRIBER_ARCH.ARCHIVETIME is
'�鵵ʱ��';

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
'��ѯ��ͼ';

comment on column BAM_QUERYVIEW.DATASOURCENAME is
'����Դ����';

comment on column BAM_QUERYVIEW.METADATA is
'Ԫ����';

insert into BAM_QUERYVIEW (ID, NAME, CODE, DATASOURCENAME, SQL, FOLDERID, DESCRIPTION, METADATA)
select ID, NAME, CODE, DATASOURCENAME, SQL, FOLDERID, DESCRIPTION, METADATA
from "tmp_BAM_QUERYVIEW";

drop table "tmp_BAM_QUERYVIEW" cascade constraints;

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

alter table BAM_QUERYVIEW
   add constraint FK_BAM_QUER_REFERENCE_BAM_FOLD foreign key (FOLDERID)
      references BAM_FOLDER (ID);

alter table BAM_ENTITYMODEL add DATASOURCE VARCHAR2(100);

comment on column BAM_ENTITYMODEL.DATASOURCE is
'����Դ';

alter table BAM_ENTITYMODEL add DWDS VARCHAR2(200);

comment on column BAM_ENTITYMODEL.DWDS is
'ά������Դ';

alter table BAM_RELATIONALDATASOURCE add STATUS NUMBER(2) default 1;

comment on column BAM_RELATIONALDATASOURCE.STATUS is
'״̬��0  ͣ�ã�1  ���ã�Ĭ��Ϊ����';
