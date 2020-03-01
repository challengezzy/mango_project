--add by zhangzy 2012/1/4 begin  ����ģ���뱨��ʵ��

create sequence S_PUB_REPORT_TEMPLATE;
create sequence S_PUB_REPORT_INSTANCE;

/*==============================================================*/
/* Table: PUB_REPORT_TEMPLATE                                    */
/*==============================================================*/
create table PUB_REPORT_TEMPLATE  (
   ID                   NUMBER(18)                      not null,
   CODE                 VARCHAR2(255),
   NAME                 VARCHAR2(255),
   MTCODE               VARCHAR2(255),
   DATASOURCE           VARCHAR2(255),
   JASPERFILEPATH       VARCHAR2(255),
   JASPERXML            CLOB,
   JASPERCONTENT        BLOB,
   REPORTDESC           VARCHAR2(1000),
   EDITDATE             DATE,
   PARAM1               VARCHAR2(500),
   PARAM2               VARCHAR2(500),
   PARAM3               VARCHAR2(500),
   PARAM4               VARCHAR2(500),
   PARAM5               VARCHAR2(500),
   PARAM6               VARCHAR2(500),
   PARAM7               VARCHAR2(500),
   PARAM8               VARCHAR2(500),
   PARAM9               VARCHAR2(500),
   constraint PK_PUB_REPORT_TEMPLAT primary key (ID)
);

comment on table PUB_REPORT_TEMPLATE is 'JASPER����ģ��';
comment on column PUB_REPORT_TEMPLATE.ID is 'ID';
comment on column PUB_REPORT_TEMPLATE.CODE is '�������';
comment on column PUB_REPORT_TEMPLATE.NAME is '��������';
comment on column PUB_REPORT_TEMPLATE.MTCODE is '��ӦԪ���ݱ���';
comment on column PUB_REPORT_TEMPLATE.DATASOURCE is '����Դ';
comment on column PUB_REPORT_TEMPLATE.JASPERFILEPATH is 'JASPER�ļ�·��';
comment on column PUB_REPORT_TEMPLATE.JASPERXML is 'JASPERXML����';
comment on column PUB_REPORT_TEMPLATE.JASPERCONTENT is '.jasper�ļ����ݣ���������ʽ��ʾ';
comment on column PUB_REPORT_TEMPLATE.REPORTDESC is '������������';
comment on column PUB_REPORT_TEMPLATE.EDITDATE is '��������';
comment on column PUB_REPORT_TEMPLATE.PARAM1 is '�������1';
comment on column PUB_REPORT_TEMPLATE.PARAM2 is '�������2';
comment on column PUB_REPORT_TEMPLATE.PARAM3 is '�������3';
comment on column PUB_REPORT_TEMPLATE.PARAM4 is '�������4';
comment on column PUB_REPORT_TEMPLATE.PARAM5 is '�������5';
comment on column PUB_REPORT_TEMPLATE.PARAM6 is '�������6';
comment on column PUB_REPORT_TEMPLATE.PARAM7 is '�������7';
comment on column PUB_REPORT_TEMPLATE.PARAM8 is '�������8';
comment on column PUB_REPORT_TEMPLATE.PARAM9 is '�������9';

create unique index "IDX_REPORT_TEMPLATE1" on PUB_REPORT_TEMPLATE (  CODE ASC );
create index "IDX_REPORT_TEMPLATE12" on PUB_REPORT_TEMPLATE ( MTCODE ASC );


/*==============================================================*/
/* Table: PUB_REPORT_INSTANCE                                   */
/*==============================================================*/
create table PUB_REPORT_INSTANCE  (
   ID                   NUMBER(18)                      not null,
   CODE                 VARCHAR2(255),
   NAME                 VARCHAR2(255),
   TEMPLATCONTENT       CLOB,
   CREATEDATE_STR       VARCHAR2(255),
   TYPE                 VARCHAR2(255),
   REPORTPATH           VARCHAR2(255),
   REPORTNAME           VARCHAR2(255),
   CREATETIME           DATE,
   PARAM1               VARCHAR2(500),
   PARAM2               VARCHAR2(500),
   PARAM3               VARCHAR2(500),
   PARAM4               VARCHAR2(500),
   PARAM5               VARCHAR2(500),
   PARAM6               VARCHAR2(500),
   PARAM7               VARCHAR2(500),
   PARAM8               VARCHAR2(500),
   PARAM9               VARCHAR2(500),
   constraint PK_PUB_REPORT_INSTANCE primary key (ID)
);

comment on table PUB_REPORT_INSTANCE is 'JASPER����ʵ��';
comment on column PUB_REPORT_INSTANCE.ID is 'ID';
comment on column PUB_REPORT_INSTANCE.CODE is '�������';
comment on column PUB_REPORT_INSTANCE.NAME is '��������';
comment on column PUB_REPORT_INSTANCE.TEMPLATCONTENT is '����ģ��Ԫ����';
comment on column PUB_REPORT_INSTANCE.CREATEDATE_STR is '������������';
comment on column PUB_REPORT_INSTANCE.TYPE is '��������(html,pdf,excel)';
comment on column PUB_REPORT_INSTANCE.REPORTPATH is '�����ļ�·��';
comment on column PUB_REPORT_INSTANCE.REPORTNAME is '�����ļ�����';
comment on column PUB_REPORT_INSTANCE.CREATETIME is '����ʱ��';
comment on column PUB_REPORT_INSTANCE.PARAM1 is '�������1';
comment on column PUB_REPORT_INSTANCE.PARAM2 is '�������2';
comment on column PUB_REPORT_INSTANCE.PARAM3 is '�������3';
comment on column PUB_REPORT_INSTANCE.PARAM4 is '�������4';
comment on column PUB_REPORT_INSTANCE.PARAM5 is '�������5';
comment on column PUB_REPORT_INSTANCE.PARAM6 is '�������6';
comment on column PUB_REPORT_INSTANCE.PARAM7 is '�������7';
comment on column PUB_REPORT_INSTANCE.PARAM8 is '�������8';
comment on column PUB_REPORT_INSTANCE.PARAM9 is '�������9';

create index "IDX_REPORT_INSTANCE1" on PUB_REPORT_INSTANCE (   CODE ASC);
create index "IDX_REPORT_INSTANCE2" on PUB_REPORT_INSTANCE (   CREATEDATE_STR ASC);
create index "IDX_REPORT_INSTANCE3" on PUB_REPORT_INSTANCE (   REPORTNAME ASC);
create index "IDX_REPORT_INSTANCE4" on PUB_REPORT_INSTANCE (   PARAM1 ASC);
create index "IDX_REPORT_INSTANCE5" on PUB_REPORT_INSTANCE (   PARAM2 ASC);
create index "IDX_REPORT_INSTANCE6" on PUB_REPORT_INSTANCE (   PARAM3 ASC);


COMMENT ON COLUMN PUB_TEMPLET_1.CARDCUSTPANEL IS 'Ӧ��ģ��,���������';
--add by zhangzy 2012/1/4 end

--add by zhangzy 2012/1/12 begin
ALTER TABLE PUB_MESSAGES ADD CREATOR VARCHAR2(255);
COMMENT ON COLUMN PUB_MESSAGES.CREATOR  IS '������';

-- Create table �ļ����ݴ洢
create sequence S_PUB_FILECONTENT;
create table PUB_FILECONTENT
(
  ID        NUMBER(18) NOT NULL,
  TABLENAME VARCHAR2(255) NOT NULL,
  KEYCOLUMN VARCHAR2(255),
  KEYVALUE  VARCHAR2(255) NOT NULL,
  FILENAME  VARCHAR2(255) ,
  CONTENT   BLOB,
  DOWNLOADTIMES NUMBER(8) default 0,
  constraint PK_PUB_FILECONTENT primary key (ID)
);
 
comment on table PUB_FILECONTENT is '�ļ����ݴ洢';

comment on column PUB_FILECONTENT.ID  is 'ID';
comment on column PUB_FILECONTENT.TABLENAME  is '��Ӧ����';
comment on column PUB_FILECONTENT.KEYCOLUMN  is '��Ӧ��ؼ��ֵ���';
comment on column PUB_FILECONTENT.KEYVALUE  is '��Ӧ������ֵ';
comment on column PUB_FILECONTENT.FILENAME  is '�ļ���';
comment on column PUB_FILECONTENT.CONTENT  is '�ļ�����';
comment on column PUB_FILECONTENT.DOWNLOADTIMES  is '���ش���';
-- Create/Recreate indexes 
create index IDX1_FILECONTENT on PUB_FILECONTENT (TABLENAME);
create index IDX2_FILECONTENT on PUB_FILECONTENT (KEYVALUE);
create index IDX3_FILECONTENT on PUB_FILECONTENT (FILENAME);

CREATE OR REPLACE VIEW V_PUB_FILECONTENT AS
SELECT --������ͼ��ȥ���ļ������ֶΣ����ٲ�ѯ���ݴ�С
     ID,TABLENAME,KEYCOLUMN,KEYVALUE,FILENAME,DOWNLOADTIMES
  FROM PUB_FILECONTENT;
/

CREATE OR REPLACE VIEW V_MESSAGES_SHOW AS
SELECT --����鿴��ֻ��ʾ�ڵ�ǰʱ������ܿ����Ĺ���
    M.ID,M.TITLE,M.STARTTIME,M.ENDTIME,M.DEGREE,M.CONTENT,M.CREATETIME,M.CREATOR,M.TYPE,M.VERSION
 FROM PUB_MESSAGES M WHERE M.STARTTIME < SYSDATE AND M.ENDTIME > SYSDATE;
/

-- add by zhangzy 2012/1/12 end