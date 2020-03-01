--add by zhangzy 2012/1/4 begin  报表模板与报表实例

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

comment on table PUB_REPORT_TEMPLATE is 'JASPER报表模板';
comment on column PUB_REPORT_TEMPLATE.ID is 'ID';
comment on column PUB_REPORT_TEMPLATE.CODE is '报表编码';
comment on column PUB_REPORT_TEMPLATE.NAME is '报表名称';
comment on column PUB_REPORT_TEMPLATE.MTCODE is '对应元数据编码';
comment on column PUB_REPORT_TEMPLATE.DATASOURCE is '数据源';
comment on column PUB_REPORT_TEMPLATE.JASPERFILEPATH is 'JASPER文件路径';
comment on column PUB_REPORT_TEMPLATE.JASPERXML is 'JASPERXML定义';
comment on column PUB_REPORT_TEMPLATE.JASPERCONTENT is '.jasper文件内容，二进制形式表示';
comment on column PUB_REPORT_TEMPLATE.REPORTDESC is '报表内容描述';
comment on column PUB_REPORT_TEMPLATE.EDITDATE is '更新日期';
comment on column PUB_REPORT_TEMPLATE.PARAM1 is '报表参数1';
comment on column PUB_REPORT_TEMPLATE.PARAM2 is '报表参数2';
comment on column PUB_REPORT_TEMPLATE.PARAM3 is '报表参数3';
comment on column PUB_REPORT_TEMPLATE.PARAM4 is '报表参数4';
comment on column PUB_REPORT_TEMPLATE.PARAM5 is '报表参数5';
comment on column PUB_REPORT_TEMPLATE.PARAM6 is '报表参数6';
comment on column PUB_REPORT_TEMPLATE.PARAM7 is '报表参数7';
comment on column PUB_REPORT_TEMPLATE.PARAM8 is '报表参数8';
comment on column PUB_REPORT_TEMPLATE.PARAM9 is '报表参数9';

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

comment on table PUB_REPORT_INSTANCE is 'JASPER报表实例';
comment on column PUB_REPORT_INSTANCE.ID is 'ID';
comment on column PUB_REPORT_INSTANCE.CODE is '报表编码';
comment on column PUB_REPORT_INSTANCE.NAME is '报表名称';
comment on column PUB_REPORT_INSTANCE.TEMPLATCONTENT is '报表模板元数据';
comment on column PUB_REPORT_INSTANCE.CREATEDATE_STR is '报表生成日期';
comment on column PUB_REPORT_INSTANCE.TYPE is '报表类型(html,pdf,excel)';
comment on column PUB_REPORT_INSTANCE.REPORTPATH is '报表文件路径';
comment on column PUB_REPORT_INSTANCE.REPORTNAME is '报表文件名称';
comment on column PUB_REPORT_INSTANCE.CREATETIME is '生成时间';
comment on column PUB_REPORT_INSTANCE.PARAM1 is '报表参数1';
comment on column PUB_REPORT_INSTANCE.PARAM2 is '报表参数2';
comment on column PUB_REPORT_INSTANCE.PARAM3 is '报表参数3';
comment on column PUB_REPORT_INSTANCE.PARAM4 is '报表参数4';
comment on column PUB_REPORT_INSTANCE.PARAM5 is '报表参数5';
comment on column PUB_REPORT_INSTANCE.PARAM6 is '报表参数6';
comment on column PUB_REPORT_INSTANCE.PARAM7 is '报表参数7';
comment on column PUB_REPORT_INSTANCE.PARAM8 is '报表参数8';
comment on column PUB_REPORT_INSTANCE.PARAM9 is '报表参数9';

create index "IDX_REPORT_INSTANCE1" on PUB_REPORT_INSTANCE (   CODE ASC);
create index "IDX_REPORT_INSTANCE2" on PUB_REPORT_INSTANCE (   CREATEDATE_STR ASC);
create index "IDX_REPORT_INSTANCE3" on PUB_REPORT_INSTANCE (   REPORTNAME ASC);
create index "IDX_REPORT_INSTANCE4" on PUB_REPORT_INSTANCE (   PARAM1 ASC);
create index "IDX_REPORT_INSTANCE5" on PUB_REPORT_INSTANCE (   PARAM2 ASC);
create index "IDX_REPORT_INSTANCE6" on PUB_REPORT_INSTANCE (   PARAM3 ASC);


COMMENT ON COLUMN PUB_TEMPLET_1.CARDCUSTPANEL IS '应用模块,软件发布用';
--add by zhangzy 2012/1/4 end

--add by zhangzy 2012/1/12 begin
ALTER TABLE PUB_MESSAGES ADD CREATOR VARCHAR2(255);
COMMENT ON COLUMN PUB_MESSAGES.CREATOR  IS '创建人';

-- Create table 文件内容存储
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
 
comment on table PUB_FILECONTENT is '文件内容存储';

comment on column PUB_FILECONTENT.ID  is 'ID';
comment on column PUB_FILECONTENT.TABLENAME  is '对应表名';
comment on column PUB_FILECONTENT.KEYCOLUMN  is '对应表关键字的列';
comment on column PUB_FILECONTENT.KEYVALUE  is '对应表主键值';
comment on column PUB_FILECONTENT.FILENAME  is '文件名';
comment on column PUB_FILECONTENT.CONTENT  is '文件内容';
comment on column PUB_FILECONTENT.DOWNLOADTIMES  is '下载次数';
-- Create/Recreate indexes 
create index IDX1_FILECONTENT on PUB_FILECONTENT (TABLENAME);
create index IDX2_FILECONTENT on PUB_FILECONTENT (KEYVALUE);
create index IDX3_FILECONTENT on PUB_FILECONTENT (FILENAME);

CREATE OR REPLACE VIEW V_PUB_FILECONTENT AS
SELECT --附件视图，去除文件内容字段，减少查询数据大小
     ID,TABLENAME,KEYCOLUMN,KEYVALUE,FILENAME,DOWNLOADTIMES
  FROM PUB_FILECONTENT;
/

CREATE OR REPLACE VIEW V_MESSAGES_SHOW AS
SELECT --公告查看，只显示在当前时间段内能看到的公告
    M.ID,M.TITLE,M.STARTTIME,M.ENDTIME,M.DEGREE,M.CONTENT,M.CREATETIME,M.CREATOR,M.TYPE,M.VERSION
 FROM PUB_MESSAGES M WHERE M.STARTTIME < SYSDATE AND M.ENDTIME > SYSDATE;
/

-- add by zhangzy 2012/1/12 end