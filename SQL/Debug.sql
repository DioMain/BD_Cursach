insert into ADMIN.USERS (user_role, name, surname, patronymic, password, birthday)
values ('DOCTOR', 'TEST', 'TEST', 'TEST', 'PASSWORD', '16.04.2004');

CALL ADMIN.REGISTRATION_NEW_USER('PATIENT', 'DIMAS',
                                  'OKULICH', 'IUR`EVICH',
                                 'PASS', '16.04.2003');

select * from ADMIN.USERS;

insert into DISEASES(NAME, DESCRIPTION) values ('Test', 'AAAAAAA');

CALL ADMIN.DEBUG_GET_ALL_USERS();

SELECT * FROM DEBUG_LOG;