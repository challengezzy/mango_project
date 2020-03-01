-------------------------------------------------------------------------------------
-- 梳理Sequence的脚本
-- 处理过程：
--     取Sequence的对应表的ID字段的最大值加1，作为新的Sequence的定义起始值。
--     判断Sequence的当前值，决定是否需要重建。
-- 异常过程：
--     不存在ID字段，或者ID字段不是NUMBER类型，自动忽略输出提示。
-------------------------------------------------------------------------------------
SET SERVEROUTPUT ON SIZE 1000000;
DECLARE
 CURSOR cseq IS SELECT SEQUENCE_NAME FROM USER_SEQUENCES WHERE SEQUENCE_NAME LIKE 'S_%' ;
 SEQNAME USER_SEQUENCES.SEQUENCE_NAME%TYPE ;
 TBLNAME USER_SEQUENCES.SEQUENCE_NAME%TYPE ;
 CURID NUMBER(18);
 HASID NUMBER(8);
BEGIN
 OPEN cseq ;
 FETCH cseq INTO SEQNAME;

 WHILE (cseq%FOUND) LOOP
   TBLNAME:=SUBSTR(SEQNAME,3);
   DBMS_OUTPUT.put_line('Table: ' || TBLNAME);
   EXECUTE IMMEDIATE 'SELECT COUNT(1) FROM COL WHERE TNAME='''|| TBLNAME 
                      ||''' AND CNAME=''ID'' AND COLTYPE=''NUMBER'' ' INTO HASID ;
   IF HASID=1 THEN
      EXECUTE IMMEDIATE 'SELECT NVL(MAX(ID),0)+1  FROM ' || TBLNAME  INTO CURID ;
      DBMS_OUTPUT.put_line('CREATE SEQUENCE ' ||SEQNAME );
      EXECUTE IMMEDIATE 'DROP SEQUENCE ' ||SEQNAME;
      EXECUTE IMMEDIATE 'CREATE SEQUENCE ' ||SEQNAME || '   START WITH ' || CURID 
	                    || '  MAXVALUE 999999999999999999  MINVALUE 1  CYCLE  NOCACHE  NOORDER';      
   ELSE
      DBMS_OUTPUT.put_line('Table ' || TBLNAME || ' has not  ''ID''.  ');
   END IF;
  FETCH cseq INTO SEQNAME ;
 END LOOP ;
 CLOSE cseq;
END ;
/
SET SERVEROUTPUT OFF;
