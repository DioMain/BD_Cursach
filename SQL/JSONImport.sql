CREATE TABLE JSON_DOCUMENT (
    id int generated always as identity (start with 1 increment by 1),
    data CLOB,

    constraint json_documents_pk primary key (id),
    constraint json_documents_json_chk check (data is json)
) tablespace MEDKIT_TS;

DELETE FROM JSON_DOCUMENT;

DROP TABLE JSON_DOCUMENT PURGE;

SELECT * FROM JSON_DOCUMENT;

CREATE DIRECTORY JSON_DIR AS 'C:\Users\1234\Desktop\JSON';

DROP DIRECTORY JSON_DIR;

INSERT INTO JSON_DOCUMENT (data) VALUES (bfilename('JSON_DIR', 'IMPORT.json'));

COMMIT;

INSERT INTO USERS (USER_ROLE, NAME, SURNAME, PATRONYMIC, PASSWORD, BIRTHDAY, PHONE_NUMBER, EMAIL)
SELECT jt.USER_ROLE, jt.NAME, jt.SURNAME, jt.PATRONYMIC, jt.PASSWORD, TO_DATE(jt.BIRTHDAY, 'YYYY-MM-DD'),
       jt.PHONE_NUMBER, jt.EMAIL
FROM JSON_DOCUMENT j,
     JSON_TABLE(
         j.data,
         '$[*]'
         COLUMNS (
             USER_ROLE VARCHAR2(32) PATH '$.USER_ROLE',
             NAME VARCHAR2(64) PATH '$.NAME',
             SURNAME VARCHAR2(64) PATH '$.SURNAME',
             PATRONYMIC VARCHAR2(64) PATH '$.PATRONYMIC',
             PASSWORD VARCHAR2(128) PATH '$.PASSWORD',
             BIRTHDAY VARCHAR2(10) PATH '$.BIRTHDAY',
             PHONE_NUMBER VARCHAR2(32) PATH '$.PHONE_NUMBER',
             EMAIL VARCHAR2(64) PATH '$.EMAIL'
         )
     ) jt;

COMMIT;

SELECT * FROM USERS;