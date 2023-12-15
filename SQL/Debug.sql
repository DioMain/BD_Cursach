insert into ADMIN.USERS (user_role, name, surname, patronymic, password, birthday, PHONE_NUMBER, EMAIL)
values ('ADMIN', 'Dima', 'Okylich', 'Iur`evich', 'pravoda01', '16.04.2004', '+80297080318', 'dima123.zample@gmail.com');

CALL ADMIN.USER_PACK.REGISTRATION_NEW_USER('ADMIN', 'ADMIN',
                                  'ADMIN_SUR', 'ADMIN_PAT',
                                 'ADMIN', '16.04.2004',
                                    '+375297080318', 'dima123.zample@gmail.com');

CALL ADMIN.USER_PACK.REGISTRATION_NEW_USER('DOCTOR', 'DOCTOR',
                                  'DOCTOR_SUR', 'DOCTOR_PAT',
                                 'DOC', '12.05.2003',
                                    '+375297080317', 'doc@gmail.com');

CALL ADMIN.USER_PACK.REGISTRATION_NEW_USER('PATIENT', 'PATIENT',
                                  'PATIENT_SUR', 'PATIENT_PAT',
                                 'PAT', '17.02.2007',
                                    '+375297080319', 'pat@gmail.com');

CALL ADMIN.SYMPTOM_PACK.INSERT_NEW_SYMPTOM('asds', 'asdas');

DELETE FROM ADMIN.APPOINTMENTS;
DELETE FROM ADMIN.DIAGNOSES;
DELETE FROM ADMIN.USERS;
DELETE FROM ADMIN.DISEASES;
DELETE FROM ADMIN.MEDICINES;
DELETE FROM ADMIN.SYMPTOMS;

commit;

select * from ADMIN.USERS;
select * from ADMIN.APPOINTMENTS;
select * from ADMIN.DISEASES;
select * from ADMIN.SYMPTOMS;
select * from ADMIN.DIAGNOSES;
select * from ADMIN.MEDICINES;

begin
    ADMIN.DISEASE_PACK.DELETE_DISEASE(11);
end;

SELECT * FROM ADMIN.SYMPTOMS SYMP
         WHERE (SELECT COUNT(*) FROM DISEASESTOSYMPTOMS WHERE DISEASESTOSYMPTOMS.DISEASE_ID = 1 AND DISEASESTOSYMPTOMS.SYMPTOM_ID = SYMP.SYMPTOM_ID) != 0;

SELECT COUNT(*) FROM ADMIN.USERS;

commit;

SELECT * FROM USERS;

SELECT * FROM all_objects WHERE object_name = 'DBMS_CRYPTO' AND object_type = 'PACKAGE';

select * from ADMIN.USERS WHERE USER_ROLE = 'DOCTOR';
select * from ADMIN.USERS WHERE EMAIL like 'someemail.121@email.com';
SELECT * FROM ADMIN.USERS WHERE SURNAME LIKE 'Окулич' AND NAME LIKE 'Дмитрий' AND PATRONYMIC LIKE 'Юрьевич';

CREATE INDEX FULLNAME_INDEX ON USERS (NAME, SURNAME, PATRONYMIC);

DROP INDEX FULLNAME_INDEX;
