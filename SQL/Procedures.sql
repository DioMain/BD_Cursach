--USERS

CREATE OR REPLACE PROCEDURE REGISTRATION_NEW_USER(
    user_role_a in varchar2,
    name_a in varchar2,
    surname_a in varchar2,
    patronymic_a in varchar2,
    password_a in varchar2,
    birthday_a in date,
    phone_number_a in nvarchar2,
    email_a in nvarchar2
)
AS
    v_encrypted_raw raw(128);
BEGIN
    v_encrypted_raw := dbms_crypto.ENCRYPT(
            utl_i18n.string_to_raw(password_a, 'AL32UTF8'),
            DBMS_CRYPTO.ENCRYPT_RC4,
            utl_i18n.string_to_raw('MEDKIT')
          );

    INSERT INTO ADMIN.USERS (USER_ROLE, NAME, SURNAME, PATRONYMIC, PASSWORD, BIRTHDAY, PHONE_NUMBER, EMAIL)
        values (user_role_a, name_a, surname_a, patronymic_a, v_encrypted_raw, birthday_a, phone_number_a, email_a);

    commit;
end REGISTRATION_NEW_USER;

CREATE OR REPLACE FUNCTION GET_ALL_USERS
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM USERS;

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_CURRENT_USER (
    a_email in NVARCHAR2,
    a_password in NVARCHAR2
)
RETURN SYS_REFCURSOR
IS
    v_encrypted raw(128);
    v_result SYS_REFCURSOR;
BEGIN
    v_encrypted := dbms_crypto.ENCRYPT(
            utl_i18n.string_to_raw( a_password, 'AL32UTF8'),
            DBMS_CRYPTO.ENCRYPT_RC4,
            utl_i18n.string_to_raw( 'MEDKIT')
          );

    OPEN v_result FOR
        select USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               PASSWORD,
               BIRTHDAY,
               PHONE_NUMBER,
               EMAIL from USERS where PASSWORD like v_encrypted and EMAIL like a_email;

    RETURN v_result;
end;

CREATE OR REPLACE PROCEDURE UPDATE_CURRENT_USER (
    user_id_a in int,
    name_a in varchar2,
    surname_a in varchar2,
    patronymic_a in varchar2,
    birthday_a in date,
    phone_number_a in nvarchar2
)
AS
BEGIN
    UPDATE ADMIN.USERS
        SET
            ADMIN.USERS.NAME = name_a,
            ADMIN.USERS.SURNAME = surname_a,
            ADMIN.USERS.PATRONYMIC = patronymic_a,
            ADMIN.USERS.BIRTHDAY = birthday_a,
            ADMIN.USERS.PHONE_NUMBER = phone_number_a
        WHERE
            ADMIN.USERS.USER_ID = user_id_a;
end;

CREATE OR REPLACE PROCEDURE DELETE_CURRENT_USER (
    user_id_a in int
)
AS
BEGIN
    DELETE FROM ADMIN.USERS WHERE ADMIN.USERS.USER_ID = user_id_a;
end;

CREATE OR REPLACE FUNCTION GET_USERS_BY_ROLE (
    role_a NVARCHAR2
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM USERS WHERE USER_ROLE like role_a;

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_USER (
    id_a int
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM USERS WHERE USER_ID = id_a;

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_USERS_BY_EMAIL (
    email_a nvarchar2
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM ADMIN.USERS WHERE EMAIL like '%' || email_a || '%';

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_USERS_BY_NAME_AND_ROLE (
    name_a nvarchar2,
    surname_a nvarchar2,
    patronymic_a nvarchar2,
    role_a nvarchar2
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM ADMIN.USERS
                     WHERE NAME like '%' || name_a || '%'
                        and SURNAME like '%' || surname_a || '%'
                        and PATRONYMIC like '%' || patronymic_a || '%'
                        and USER_ROLE like role_a;

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_USERS_BY_NAME (
    name_a nvarchar2,
    surname_a nvarchar2,
    patronymic_a nvarchar2
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM ADMIN.USERS
                     WHERE NAME like '%' || name_a || '%'
                        and SURNAME like '%' || surname_a || '%'
                        and PATRONYMIC like '%' || patronymic_a || '%';

    RETURN v_result;
end;

CREATE OR REPLACE FUNCTION GET_FIRST_USERS (
    count_a int
)
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        SELECT USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL FROM USERS
               FETCH FIRST count_a ROWS ONLY;

    RETURN v_result;
end;

DROP PROCEDURE REGISTRATION_NEW_USER;
DROP FUNCTION GET_ALL_USERS;
DROP FUNCTION GET_CURRENT_USER;
DROP PROCEDURE DELETE_CURRENT_USER;
DROP PROCEDURE UPDATE_CURRENT_USER;
DROP FUNCTION GET_USERS_BY_ROLE;
DROP FUNCTION GET_USER;
DROP FUNCTION GET_USERS_BY_EMAIL;
DROP FUNCTION GET_USERS_BY_NAME;
DROP FUNCTION GET_USERS_BY_NAME_AND_ROLE;
DROP FUNCTION GET_FIRST_USERS;

--Appointments

CREATE OR REPLACE PROCEDURE INSERT_NEW_APPOINTMENT (
    patient_id_a int,
    doctor_id_a int,
    date_a date
)
AS
BEGIN
   INSERT INTO ADMIN.APPOINTMENTS
               (ADMIN.APPOINTMENTS.DOCTOR_ID, ADMIN.APPOINTMENTS.PATIENT_ID, ADMIN.APPOINTMENTS.APPOINTMENT_DATE)
               VALUES (doctor_id_a, patient_id_a, date_a);

   commit;
END;

CREATE OR REPLACE PROCEDURE UPDATE_APPOINTMENT (
    appointment_id_a int,
    new_state int
)
AS
BEGIN
   UPDATE ADMIN.APPOINTMENTS
                SET ADMIN.APPOINTMENTS.APPOINTMENT_STATE = new_state
                WHERE ADMIN.APPOINTMENTS.APPOINTMENT_ID = appointment_id_a;

   commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_APPOINTMENT (
    appointment_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.APPOINTMENTS WHERE ADMIN.APPOINTMENTS.APPOINTMENT_ID = appointment_id_a;

   commit;
END;

CREATE OR REPLACE FUNCTION GET_ALL_APPOINTMENTS
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.APPOINTMENTS;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_APPOINTMENT (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.APPOINTMENTS WHERE APPOINTMENT_ID = id_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_APPOINTMENTS_BY_USERID (
    user_id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.APPOINTMENTS
                        WHERE ADMIN.APPOINTMENTS.PATIENT_ID = user_id_a or ADMIN.APPOINTMENTS.DOCTOR_ID = user_id_a;

    RETURN v_cursor;
END;

DROP PROCEDURE INSERT_NEW_APPOINTMENT;
DROP PROCEDURE UPDATE_APPOINTMENT;
DROP PROCEDURE DELETE_APPOINTMENT;
DROP FUNCTION GET_ALL_APPOINTMENTS;
DROP FUNCTION GET_APPOINTMENTS_BY_USERID;
DROP FUNCTION GET_APPOINTMENT;

--Medicines

CREATE OR REPLACE PROCEDURE INSERT_NEW_MEDICINE (
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
)
AS
BEGIN
   INSERT INTO ADMIN.MEDICINES (ADMIN.MEDICINES.NAME, ADMIN.MEDICINES.DESCRIPTION, ADMIN.MEDICINES.MANUFACTURER, ADMIN.MEDICINES.PRICE, ADMIN.MEDICINES.START_DATE)
                VALUES (name_a, description_a, manufacturer_a, price_a, start_date_a);

   commit;
END;

CREATE OR REPLACE PROCEDURE UPDATE_MEDICINE (
    medicine_id_a int,
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
)
AS
BEGIN
   UPDATE ADMIN.MEDICINES
        SET ADMIN.MEDICINES.NAME = name_a, ADMIN.MEDICINES.DESCRIPTION = description_a,
            ADMIN.MEDICINES.MANUFACTURER = manufacturer_a, ADMIN.MEDICINES.PRICE = price_a,
            ADMIN.MEDICINES.START_DATE = start_date_a
        WHERE ADMIN.MEDICINES.MEDICINE_ID = medicine_id_a;

   commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_MEDICINE (
    medicine_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.MEDICINES WHERE ADMIN.MEDICINES.MEDICINE_ID = medicine_id_a;

   commit;
END;

CREATE OR REPLACE FUNCTION GET_ALL_MEDICINES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.MEDICINES;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_MEDICINE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.MEDICINES WHERE MEDICINE_ID = id_a;

    RETURN v_cursor;
END;

DROP PROCEDURE INSERT_NEW_MEDICINE;
DROP PROCEDURE UPDATE_MEDICINE;
DROP PROCEDURE DELETE_MEDICINE;
DROP FUNCTION GET_ALL_MEDICINES;
DROP FUNCTION GET_MEDICINE;

--SYMPTOMS

CREATE OR REPLACE PROCEDURE INSERT_NEW_SYMPTOM (
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   INSERT INTO ADMIN.SYMPTOMS (ADMIN.SYMPTOMS.NAME, ADMIN.SYMPTOMS.DESCRIPTION)
                VALUES (name_a, description_a);

   commit;
END;

CREATE OR REPLACE PROCEDURE UPDATE_SYMPTOM (
    symptom_id_a int,
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   UPDATE ADMIN.SYMPTOMS
        SET ADMIN.SYMPTOMS.NAME = name_a, ADMIN.SYMPTOMS.DESCRIPTION = description_a
        WHERE ADMIN.SYMPTOMS.SYMPTOM_ID = symptom_id_a;

   commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_SYMPTOM (
    symptom_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.SYMPTOMS WHERE ADMIN.SYMPTOMS.SYMPTOM_ID = symptom_id_a;

   commit;
END;

CREATE OR REPLACE FUNCTION GET_ALL_SYMPTOMS
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.SYMPTOMS;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_SYMPTOM (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.SYMPTOMS WHERE ADMIN.SYMPTOMS.SYMPTOM_ID = id_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_SYMPTOMS_BY_DISEASE (
    disease_id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.SYMPTOMS SYMP
                                WHERE (SELECT COUNT(*) FROM DISEASESTOSYMPTOMS WHERE DISEASESTOSYMPTOMS.DISEASE_ID = disease_id_a AND DISEASESTOSYMPTOMS.SYMPTOM_ID = SYMP.SYMPTOM_ID) != 0;

    RETURN v_cursor;
END;


DROP PROCEDURE INSERT_NEW_SYMPTOM;
DROP PROCEDURE UPDATE_SYMPTOM;
DROP PROCEDURE DELETE_SYMPTOM;
DROP FUNCTION GET_ALL_SYMPTOMS;
DROP FUNCTION GET_SYMPTOM;
DROP FUNCTION GET_SYMPTOMS_BY_DISEASE;

--DIAGNOSES

CREATE OR REPLACE PROCEDURE INSERT_NEW_DIAGNOSE (
    doctor_id_a int,
    patient_id_a int,
    disease_id_a int,

    open_date_a date,
    close_date_a date,
    note_a nvarchar2,
    description_a nvarchar2,
    diagnose_state_a int
)
AS
BEGIN
   INSERT INTO ADMIN.DIAGNOSES (DIAGNOSE_ID, DOCTOR_ID, PATIENT_ID, DISEASE_ID, OPEN_DATE, CLOSE_DATE, NOTE, DESCRIPTION, DIAGNOSE_STATE)
                VALUES (ADMIN.DIAGNOSE_ID_SEQUENCE.nextval, doctor_id_a, patient_id_a, disease_id_a, open_date_a, close_date_a, note_a, description_a, diagnose_state_a);

   commit;
END;

CREATE OR REPLACE FUNCTION INSERT_NEW_DIAGNOSE_RET_ID (
    doctor_id_a int,
    patient_id_a int,
    disease_id_a int,

    open_date_a date,
    close_date_a date,
    note_a nvarchar2,
    description_a nvarchar2,
    diagnose_state_a int
)
    RETURN INT
AS
    id_v int;
BEGIN
    id_v := ADMIN.DIAGNOSE_ID_SEQUENCE.nextval;

    INSERT INTO ADMIN.DIAGNOSES (DIAGNOSE_ID, DOCTOR_ID, PATIENT_ID, DISEASE_ID, OPEN_DATE, CLOSE_DATE, NOTE, DESCRIPTION, DIAGNOSE_STATE)
                VALUES (id_v, doctor_id_a, patient_id_a, disease_id_a, open_date_a, close_date_a, note_a, description_a, diagnose_state_a);

    commit;

    RETURN id_v;
END;

CREATE OR REPLACE PROCEDURE UPDATE_DIAGNOSE (
    diagnose_id_a int,
    doctor_id_a int,
    patient_id_a int,
    disease_id_a int,

    open_date_a date,
    close_date_a date,
    note_a nvarchar2,
    description_a nvarchar2,
    diagnose_state_a int
)
AS
BEGIN
   UPDATE ADMIN.DIAGNOSES
       SET ADMIN.DIAGNOSES.DOCTOR_ID = doctor_id_a, ADMIN.DIAGNOSES.PATIENT_ID = patient_id_a, ADMIN.DIAGNOSES.DISEASE_ID = disease_id_a,
           ADMIN.DIAGNOSES.OPEN_DATE = open_date_a, ADMIN.DIAGNOSES.CLOSE_DATE = close_date_a, ADMIN.DIAGNOSES.NOTE = note_a,
           ADMIN.DIAGNOSES.DESCRIPTION = description_a, ADMIN.DIAGNOSES.DIAGNOSE_STATE = diagnose_state_a
       WHERE ADMIN.DIAGNOSES.DIAGNOSE_ID = diagnose_id_a;

   commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_DIAGNOSE (
    diagnose_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.DIAGNOSES WHERE ADMIN.DIAGNOSES.DIAGNOSE_ID = diagnose_id_a;

   commit;
END;

CREATE OR REPLACE FUNCTION GET_ALL_DIAGNOSES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSES;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DIAGNOSE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSES WHERE ADMIN.DIAGNOSES.DIAGNOSE_ID = id_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DIAGNOSE_BY_USER (
    user_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSES
                               WHERE ADMIN.DIAGNOSES.DOCTOR_ID = user_a
                                     OR ADMIN.DIAGNOSES.PATIENT_ID = user_a;

    RETURN v_cursor;
END;

DROP PROCEDURE INSERT_NEW_DIAGNOSE;
DROP FUNCTION INSERT_NEW_DIAGNOSE_RET_ID;
DROP PROCEDURE UPDATE_DIAGNOSE;
DROP PROCEDURE DELETE_DIAGNOSE;
DROP FUNCTION GET_ALL_DIAGNOSES;
DROP FUNCTION GET_DIAGNOSE;
DROP FUNCTION GET_DIAGNOSE_BY_USER;

--DISEASE

CREATE OR REPLACE PROCEDURE INSERT_NEW_DISEASE (
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   INSERT INTO ADMIN.DISEASES (ADMIN.DISEASES.DISEASE_ID, ADMIN.DISEASES.NAME, ADMIN.DISEASES.DESCRIPTION)
                VALUES (ADMIN.DISEASE_ID_SEQUENCE.nextval, name_a, description_a);

   commit;
END;

CREATE OR REPLACE FUNCTION INSERT_NEW_DISEASE_RET_ID (
    name_a nvarchar2,
    description_a nvarchar2
)
    RETURN INT
AS
    id_v int;
BEGIN
    id_v := ADMIN.DISEASE_ID_SEQUENCE.nextval;

   INSERT INTO ADMIN.DISEASES (ADMIN.DISEASES.DISEASE_ID, ADMIN.DISEASES.NAME, ADMIN.DISEASES.DESCRIPTION)
                VALUES (id_v, name_a, description_a);

   commit;

   RETURN id_v;
END;

CREATE OR REPLACE PROCEDURE UPDATE_DISEASE (
    disease_id_a int,
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   UPDATE ADMIN.DISEASES
        SET ADMIN.DISEASES.NAME = name_a, ADMIN.DISEASES.DESCRIPTION = description_a
        WHERE ADMIN.DISEASES.DISEASE_ID = disease_id_a;

   commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_DISEASE (
    disease_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.DISEASES WHERE ADMIN.DISEASES.DISEASE_ID = disease_id_a;

   commit;
END;

CREATE OR REPLACE FUNCTION GET_ALL_DISEASES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASES;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DISEASE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASES WHERE ADMIN.DISEASES.DISEASE_ID = id_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DISEASE_BY_SYMPTOMS (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASES WHERE ADMIN.DISEASES.DISEASE_ID = id_a;

    RETURN v_cursor;
END;

DROP PROCEDURE INSERT_NEW_DISEASE;
DROP FUNCTION INSERT_NEW_DISEASE_RET_ID;
DROP PROCEDURE UPDATE_DISEASE;
DROP PROCEDURE DELETE_DISEASE;
DROP FUNCTION GET_ALL_DISEASES;
DROP FUNCTION GET_DISEASE;

-- DEBUG

CREATE OR REPLACE PROCEDURE GET_CURRENT_USER_DEBUG
AS
    user_id INT;
    user_role nvarchar2(32);
    user_name nvarchar2(64);
    user_surname nvarchar2(64);
    user_patronymic nvarchar2(64);
    user_password nvarchar2(128);
    user_birthday date;
    user_phonenumber nvarchar2(32);
    user_email nvarchar2(64);
    user_cursor SYS_REFCURSOR;
BEGIN
    user_cursor := GET_CURRENT_USER('dima1234.zample@gmail.com', 'ADMIN');

    LOOP
        FETCH user_cursor INTO user_id, user_role, user_name, user_surname, user_patronymic, user_password, user_birthday, user_phonenumber, user_email;

        EXIT WHEN user_cursor%NOTFOUND;

        INSERT INTO ADMIN.DEBUG_LOG (TITLE, DESCRIPTION)
            values ('USER_INFO', 'ID: ' || user_id || ', NAME: ' || user_name || ', ROLE: ' || user_role || ', PASSWORD: ' || user_password || ', PHONE NUMBER: ' || user_phonenumber);
    END LOOP;

    CLOSE user_cursor;
END;

CREATE OR REPLACE PROCEDURE DEBUG_GET_ALL_USERS
AS
     user_id INT;
        user_role nvarchar2(32);
        user_name nvarchar2(64);
        user_surname nvarchar2(64);
        user_patronymic nvarchar2(64);
        user_password nvarchar2(128);
        user_birthday date;
        user_phonenumber nvarchar2(32);
        user_email nvarchar2(64);
        user_cursor SYS_REFCURSOR;
BEGIN
    user_cursor := GET_ALL_USERS();

    LOOP
        FETCH user_cursor INTO user_id, user_role, user_name, user_surname, user_patronymic, user_password, user_birthday, user_phonenumber, user_email;

        EXIT WHEN user_cursor%NOTFOUND;

        INSERT INTO ADMIN.DEBUG_LOG (TITLE, DESCRIPTION)
            values ('USER_INFO', 'ID: ' || user_id || ', NAME: ' || user_name || ', ROLE: ' || user_role || ', PASSWORD: ' || user_password || ', PHONE NUMBER: ' || user_phonenumber);
    END LOOP;

    CLOSE user_cursor;
END;

DROP PROCEDURE DEBUG_GET_ALL_USERS;
DROP PROCEDURE GET_CURRENT_USER_DEBUG;

-- MTM

CREATE OR REPLACE PROCEDURE DIAGNOSES_TO_MEDICINES (
    diagnose int,
    medicine int
)
AS
BEGIN
    INSERT INTO DIAGNOSESTOMEDICINES (DIAGNOSE_ID, MEDICINE_ID)
                VALUES (diagnose, medicine);

    commit;
end;

CREATE OR REPLACE PROCEDURE DIAGNOSES_TO_SYMPTOMS (
    diagnose int,
    symptom int
)
AS
BEGIN
    INSERT INTO DIAGNOSESTOSYMPTOMS (DIAGNOSE_ID, SYMPTOM_ID)
                VALUES (diagnose, symptom);

    commit;
end;

CREATE OR REPLACE PROCEDURE DISEASES_TO_SYMPTOMS (
    disease int,
    symptom int
)
AS
BEGIN
    INSERT INTO DISEASESTOSYMPTOMS (DISEASE_ID, SYMPTOM_ID)
                VALUES (disease, symptom);

    commit;
end;

CREATE OR REPLACE FUNCTION GET_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASESTOSYMPTOMS WHERE DISEASE_ID = disease_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSESTOSYMPTOMS WHERE DIAGNOSE_ID = diagnose_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION GET_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSESTOMEDICINES WHERE DIAGNOSE_ID = diagnose_a;

    RETURN v_cursor;
END;

CREATE OR REPLACE PROCEDURE DELETE_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
)
AS
BEGIN
    DELETE FROM DIAGNOSESTOMEDICINES WHERE DIAGNOSE_ID = diagnose_a;

    commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
)
AS
BEGIN
    DELETE FROM DIAGNOSESTOSYMPTOMS WHERE DIAGNOSE_ID = diagnose_a;

    commit;
END;

CREATE OR REPLACE PROCEDURE DELETE_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
)
AS
BEGIN
    DELETE FROM DISEASESTOSYMPTOMS WHERE DISEASE_ID = disease_a;

    commit;
END;

DROP PROCEDURE DIAGNOSES_TO_MEDICINES;
DROP PROCEDURE DIAGNOSES_TO_SYMPTOMS;
DROP PROCEDURE DISEASES_TO_SYMPTOMS;
DROP FUNCTION GET_DIAGNOSESTOMEDICINES_BY_DIAGNOSE;
DROP FUNCTION GET_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE;
DROP FUNCTION GET_DISEASESTOSYMPTOMS_BY_DISEASE;
DROP PROCEDURE DELETE_DIAGNOSESTOMEDICINES_BY_DIAGNOSE;
DROP PROCEDURE DELETE_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE;
DROP PROCEDURE DELETE_DISEASESTOSYMPTOMS_BY_DISEASE;
