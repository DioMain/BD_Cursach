insert into ADMIN.USERS (user_role, name, surname, patronymic, password, birthday, PHONE_NUMBER, EMAIL)
values ('ADMIN', 'Dima', 'Okylich', 'Iur`evich', 'pravoda01', '16.04.2004', '+80297080318', 'dima123.zample@gmail.com');

CALL ADMIN.REGISTRATION_NEW_USER('PATIENT', 'TEst',
                                  'aa', 'IUR`asf',
                                 'PASS', '16.04.2111',
                                    'test', 'test');

DELETE FROM ADMIN.USERS WHERE USER_ID = 4;

commit;

select * from ADMIN.USERS;

insert into DISEASES(NAME, DESCRIPTION) values ('Test', 'AAAAAAA');

CALL ADMIN.DEBUG_GET_ALL_USERS();

SELECT * FROM DEBUG_LOG;