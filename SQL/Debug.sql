insert into ADMIN.USERS (user_role, name, surname, patronymic, password, birthday, PHONE_NUMBER, EMAIL)
values ('ADMIN', 'Dima', 'Okylich', 'Iur`evich', 'pravoda01', '16.04.2004', '+80297080318', 'dima123.zample@gmail.com');

CALL ADMIN.REGISTRATION_NEW_USER('ADMIN', 'ADMIN',
                                  'ADMIN_SUR', 'ADMIN_PAT',
                                 'ADMIN', '16.04.2004',
                                    '+375297080318', 'dima1234.zample@gmail.com');

DELETE FROM ADMIN.USERS WHERE USERS.USER_ID = 62;

commit;

DELETE FROM ADMIN.DEBUG_LOG;

commit;

select * from ADMIN.USERS;
select * from ADMIN.APPOINTMENTS;

DELETE FROM APPOINTMENTS;

commit;

insert into DISEASES(NAME, DESCRIPTION) values ('Test', 'AAAAAAA');

CALL ADMIN.DEBUG_GET_ALL_USERS();
CALL ADMIN.GET_CURRENT_USER_DEBUG();

SELECT * FROM DEBUG_LOG;

SELECT * FROM all_objects WHERE object_name = 'DBMS_CRYPTO' AND object_type = 'PACKAGE';
