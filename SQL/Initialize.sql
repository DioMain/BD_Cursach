CREATE TABLESPACE MEDKIT_TS
    datafile 'C:\Users\1234\Desktop\oradata\ORCL\OTHERFILES\MEDKIT_TS.dbf'
    SIZE 32M
    AUTOEXTEND ON
    NEXT 32M
    MAXSIZE 512M;

CREATE TEMPORARY TABLESPACE MEDKIT_TEMP_TS
    tempfile 'C:\Users\1234\Desktop\oradata\ORCL\OTHERFILES\MEDKIT_TEMP_TS.dbf'
    SIZE 16M
    AUTOEXTEND ON
    NEXT 16M
    MAXSIZE 256M;

commit;

DROP TABLESPACE MEDKIT_TS including contents and datafiles;
DROP TABLESPACE MEDKIT_TEMP_TS including contents and datafiles;

commit;
