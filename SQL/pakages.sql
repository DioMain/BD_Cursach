CREATE OR REPLACE PACKAGE USER_PACK AS
PROCEDURE REGISTRATION_NEW_USER(
    user_role_a in varchar2,
    name_a in varchar2,
    surname_a in varchar2,
    patronymic_a in varchar2,
    password_a in varchar2,
    birthday_a in date,
    phone_number_a in nvarchar2,
    email_a in nvarchar2
);
FUNCTION GET_ALL_USERS RETURN SYS_REFCURSOR;
FUNCTION GET_CURRENT_USER (
    a_email in NVARCHAR2,
    a_password in NVARCHAR2
)
RETURN SYS_REFCURSOR;
PROCEDURE UPDATE_CURRENT_USER (
    user_id_a in int,
    name_a in varchar2,
    surname_a in varchar2,
    patronymic_a in varchar2,
    birthday_a in date,
    phone_number_a in nvarchar2
);
PROCEDURE DELETE_CURRENT_USER (
    user_id_a in int
);
FUNCTION GET_USERS_BY_ROLE (
    role_a NVARCHAR2
)
RETURN SYS_REFCURSOR;
FUNCTION GET_USER (
    id_a int
)
RETURN SYS_REFCURSOR;
FUNCTION GET_USER_BY_EMAIL (
    email_a nvarchar2
)
RETURN SYS_REFCURSOR;
FUNCTION GET_USERS_BY_NAME_AND_ROLE (
    name_a nvarchar2,
    surname_a nvarchar2,
    patronymic_a nvarchar2,
    role_a nvarchar2
)
RETURN SYS_REFCURSOR;
FUNCTION GET_FIRST_USERS (
    count_a int
)
RETURN SYS_REFCURSOR;
FUNCTION GET_USERS_BY_NAME (
    name_a nvarchar2,
    surname_a nvarchar2,
    patronymic_a nvarchar2
)
RETURN SYS_REFCURSOR;
END USER_PACK;

CREATE OR REPLACE PACKAGE BODY USER_PACK AS
PROCEDURE REGISTRATION_NEW_USER(
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
    em_count number;
BEGIN
    v_encrypted_raw := dbms_crypto.ENCRYPT(
            utl_i18n.string_to_raw(password_a, 'AL32UTF8'),
            DBMS_CRYPTO.ENCRYPT_RC4,
            utl_i18n.string_to_raw('MEDKIT')
          );

    SELECT COUNT(*) INTO em_count FROM USERS WHERE EMAIL like email_a;

    IF (em_count > 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THIS EMAIL EXISTS!');
    END IF;

    IF (birthday_a >= sysdate) THEN
        RAISE_APPLICATION_ERROR(-20000, 'NOT VALID DATE!');
    end if;

    INSERT INTO ADMIN.USERS (USER_ROLE, NAME, SURNAME, PATRONYMIC, PASSWORD, BIRTHDAY, PHONE_NUMBER, EMAIL)
        values (user_role_a, name_a, surname_a, patronymic_a, v_encrypted_raw, birthday_a, phone_number_a, email_a);

    commit;
EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end REGISTRATION_NEW_USER;

FUNCTION GET_ALL_USERS
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

FUNCTION GET_CURRENT_USER (
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

PROCEDURE UPDATE_CURRENT_USER (
    user_id_a in int,
    name_a in varchar2,
    surname_a in varchar2,
    patronymic_a in varchar2,
    birthday_a in date,
    phone_number_a in nvarchar2
)
AS
BEGIN
    IF (birthday_a > SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20001, 'INVALID DATE');
    end IF;

    UPDATE ADMIN.USERS
        SET
            ADMIN.USERS.NAME = name_a,
            ADMIN.USERS.SURNAME = surname_a,
            ADMIN.USERS.PATRONYMIC = patronymic_a,
            ADMIN.USERS.BIRTHDAY = birthday_a,
            ADMIN.USERS.PHONE_NUMBER = phone_number_a
        WHERE
            ADMIN.USERS.USER_ID = user_id_a;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end;

PROCEDURE DELETE_CURRENT_USER (
    user_id_a in int
)
AS
BEGIN
    DELETE FROM ADMIN.USERS WHERE ADMIN.USERS.USER_ID = user_id_a;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end;

FUNCTION GET_USERS_BY_ROLE (
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

FUNCTION GET_USER (
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

FUNCTION GET_USER_BY_EMAIL (
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

FUNCTION GET_USERS_BY_NAME_AND_ROLE (
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

FUNCTION GET_FIRST_USERS (
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

FUNCTION GET_USERS_BY_NAME (
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
END USER_PACK;

DROP PACKAGE USER_PACK;

CREATE OR REPLACE PACKAGE APPOINTMENT_PACK AS
PROCEDURE INSERT_NEW_APPOINTMENT (
    patient_id_a int,
    doctor_id_a int,
    date_a date
);
PROCEDURE UPDATE_APPOINTMENT (
    appointment_id_a int,
    new_state int
);
PROCEDURE DELETE_APPOINTMENT (
    appointment_id_a int
);
FUNCTION GET_ALL_APPOINTMENTS RETURN SYS_REFCURSOR;
FUNCTION GET_APPOINTMENT (
    id_a int
)
RETURN SYS_REFCURSOR;
FUNCTION GET_APPOINTMENTS_BY_USERID (
    user_id_a int
)
    RETURN SYS_REFCURSOR;
END APPOINTMENT_PACK;

CREATE OR REPLACE PACKAGE BODY APPOINTMENT_PACK AS
PROCEDURE INSERT_NEW_APPOINTMENT (
    patient_id_a int,
    doctor_id_a int,
    date_a date
)
AS
BEGIN
    IF (date_a < SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20000, 'INVALID DATE');
    end IF;

   INSERT INTO ADMIN.APPOINTMENTS
               (ADMIN.APPOINTMENTS.DOCTOR_ID, ADMIN.APPOINTMENTS.PATIENT_ID, ADMIN.APPOINTMENTS.APPOINTMENT_DATE)
               VALUES (doctor_id_a, patient_id_a, date_a);
   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE UPDATE_APPOINTMENT (
    appointment_id_a int,
    new_state int
)
AS
BEGIN

   UPDATE ADMIN.APPOINTMENTS
                SET ADMIN.APPOINTMENTS.APPOINTMENT_STATE = new_state
                WHERE ADMIN.APPOINTMENTS.APPOINTMENT_ID = appointment_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_APPOINTMENT (
    appointment_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.APPOINTMENTS WHERE ADMIN.APPOINTMENTS.APPOINTMENT_ID = appointment_id_a;

   commit;
EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION GET_ALL_APPOINTMENTS
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.APPOINTMENTS;

    RETURN v_cursor;
END;

FUNCTION GET_APPOINTMENT (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.APPOINTMENTS WHERE APPOINTMENT_ID = id_a;

    RETURN v_cursor;
END;

FUNCTION GET_APPOINTMENTS_BY_USERID (
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
END APPOINTMENT_PACK;

DROP PACKAGE APPOINTMENT_PACK;

CREATE OR REPLACE PACKAGE MEDICINE_PACK AS
PROCEDURE INSERT_NEW_MEDICINE (
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
);
PROCEDURE UPDATE_MEDICINE (
    medicine_id_a int,
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
);
PROCEDURE DELETE_MEDICINE (
    medicine_id_a int
);
FUNCTION GET_ALL_MEDICINES RETURN SYS_REFCURSOR;
FUNCTION GET_MEDICINE (
    id_a int
)
    RETURN SYS_REFCURSOR;
END MEDICINE_PACK;

CREATE OR REPLACE PACKAGE BODY MEDICINE_PACK AS
PROCEDURE INSERT_NEW_MEDICINE (
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
)
AS
BEGIN
    IF (start_date_a > SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20000, 'INVALID DATE');
    end IF;

   INSERT INTO ADMIN.MEDICINES (ADMIN.MEDICINES.NAME, ADMIN.MEDICINES.DESCRIPTION, ADMIN.MEDICINES.MANUFACTURER, ADMIN.MEDICINES.PRICE, ADMIN.MEDICINES.START_DATE)
                VALUES (name_a, description_a, manufacturer_a, price_a, start_date_a);

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE UPDATE_MEDICINE (
    medicine_id_a int,
    name_a nvarchar2,
    description_a nvarchar2,
    manufacturer_a nvarchar2,
    price_a number,
    start_date_a date
)
AS
BEGIN
    IF (start_date_a > SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20000, 'INVALID DATE');
    end IF;

   UPDATE ADMIN.MEDICINES
        SET ADMIN.MEDICINES.NAME = name_a, ADMIN.MEDICINES.DESCRIPTION = description_a,
            ADMIN.MEDICINES.MANUFACTURER = manufacturer_a, ADMIN.MEDICINES.PRICE = price_a,
            ADMIN.MEDICINES.START_DATE = start_date_a
        WHERE ADMIN.MEDICINES.MEDICINE_ID = medicine_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_MEDICINE (
    medicine_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.MEDICINES WHERE ADMIN.MEDICINES.MEDICINE_ID = medicine_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION GET_ALL_MEDICINES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.MEDICINES;

    RETURN v_cursor;
END;

FUNCTION GET_MEDICINE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.MEDICINES WHERE MEDICINE_ID = id_a;

    RETURN v_cursor;
END;
END MEDICINE_PACK;

DROP PACKAGE MEDICINE_PACK;

CREATE OR REPLACE PACKAGE SYMPTOM_PACK AS
PROCEDURE INSERT_NEW_SYMPTOM (
    name_a nvarchar2,
    description_a nvarchar2
);
PROCEDURE UPDATE_SYMPTOM (
    symptom_id_a int,
    name_a nvarchar2,
    description_a nvarchar2
);
PROCEDURE DELETE_SYMPTOM (
    symptom_id_a int
);
FUNCTION GET_ALL_SYMPTOMS RETURN SYS_REFCURSOR;
FUNCTION GET_SYMPTOM (
    id_a int
)
    RETURN SYS_REFCURSOR;
FUNCTION GET_SYMPTOMS_BY_DISEASE (
    disease_id_a int
)
    RETURN SYS_REFCURSOR;
END SYMPTOM_PACK;

CREATE OR REPLACE PACKAGE BODY SYMPTOM_PACK AS
PROCEDURE INSERT_NEW_SYMPTOM (
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   INSERT INTO ADMIN.SYMPTOMS (ADMIN.SYMPTOMS.NAME, ADMIN.SYMPTOMS.DESCRIPTION)
                VALUES (name_a, description_a);

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE UPDATE_SYMPTOM (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_SYMPTOM (
    symptom_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.SYMPTOMS WHERE ADMIN.SYMPTOMS.SYMPTOM_ID = symptom_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION GET_ALL_SYMPTOMS
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.SYMPTOMS;

    RETURN v_cursor;
END;

FUNCTION GET_SYMPTOM (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.SYMPTOMS WHERE ADMIN.SYMPTOMS.SYMPTOM_ID = id_a;

    RETURN v_cursor;
END;

FUNCTION GET_SYMPTOMS_BY_DISEASE (
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
END SYMPTOM_PACK;

DROP PACKAGE SYMPTOM_PACK;

CREATE OR REPLACE PACKAGE DIAGNOSE_PACK AS
    PROCEDURE INSERT_NEW_DIAGNOSE (
        doctor_id_a int,
        patient_id_a int,
        disease_id_a int,

        open_date_a date,
        close_date_a date,
        note_a nvarchar2,
        description_a nvarchar2,
        diagnose_state_a int
    );
    FUNCTION INSERT_NEW_DIAGNOSE_RET_ID (
        doctor_id_a int,
        patient_id_a int,
        disease_id_a int,

        open_date_a date,
        close_date_a date,
        note_a nvarchar2,
        description_a nvarchar2,
        diagnose_state_a int
    )
        RETURN INT;
    PROCEDURE UPDATE_DIAGNOSE (
        diagnose_id_a int,
        doctor_id_a int,
        patient_id_a int,
        disease_id_a int,

        open_date_a date,
        close_date_a date,
        note_a nvarchar2,
        description_a nvarchar2,
        diagnose_state_a int
    );
    PROCEDURE DELETE_DIAGNOSE (
        diagnose_id_a int
    );
    FUNCTION GET_ALL_DIAGNOSES RETURN SYS_REFCURSOR;
    FUNCTION GET_DIAGNOSE (
        id_a int
    )
        RETURN SYS_REFCURSOR;
    FUNCTION GET_DIAGNOSE_BY_USER (
        user_a int
    )
        RETURN SYS_REFCURSOR;
END DIAGNOSE_PACK;

CREATE OR REPLACE PACKAGE BODY DIAGNOSE_PACK AS
PROCEDURE INSERT_NEW_DIAGNOSE (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION INSERT_NEW_DIAGNOSE_RET_ID (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RETURN 0;
    END;
END;

PROCEDURE UPDATE_DIAGNOSE (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_DIAGNOSE (
    diagnose_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.DIAGNOSES WHERE ADMIN.DIAGNOSES.DIAGNOSE_ID = diagnose_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION GET_ALL_DIAGNOSES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSES;

    RETURN v_cursor;
END;

FUNCTION GET_DIAGNOSE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSES WHERE ADMIN.DIAGNOSES.DIAGNOSE_ID = id_a;

    RETURN v_cursor;
END;

FUNCTION GET_DIAGNOSE_BY_USER (
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
END DIAGNOSE_PACK;

DROP PACKAGE DIAGNOSE_PACK;

CREATE OR REPLACE PACKAGE DISEASE_PACK AS
PROCEDURE INSERT_NEW_DISEASE (
    name_a nvarchar2,
    description_a nvarchar2
);
FUNCTION INSERT_NEW_DISEASE_RET_ID (
    name_a nvarchar2,
    description_a nvarchar2
)
    RETURN INT;
PROCEDURE UPDATE_DISEASE (
    disease_id_a int,
    name_a nvarchar2,
    description_a nvarchar2
);
PROCEDURE DELETE_DISEASE (
    disease_id_a int
);
FUNCTION GET_ALL_DISEASES RETURN SYS_REFCURSOR;
FUNCTION GET_DISEASE (
    id_a int
)
    RETURN SYS_REFCURSOR;
END DISEASE_PACK;

CREATE OR REPLACE PACKAGE BODY DISEASE_PACK AS
PROCEDURE INSERT_NEW_DISEASE (
    name_a nvarchar2,
    description_a nvarchar2
)
AS
BEGIN
   INSERT INTO ADMIN.DISEASES (ADMIN.DISEASES.DISEASE_ID, ADMIN.DISEASES.NAME, ADMIN.DISEASES.DESCRIPTION)
                VALUES (ADMIN.DISEASE_ID_SEQUENCE.nextval, name_a, description_a);

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION INSERT_NEW_DISEASE_RET_ID (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RETURN 0;
    END;
END;

PROCEDURE UPDATE_DISEASE (
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

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_DISEASE (
    disease_id_a int
)
AS
BEGIN
   DELETE FROM ADMIN.DISEASES WHERE ADMIN.DISEASES.DISEASE_ID = disease_id_a;

   commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

FUNCTION GET_ALL_DISEASES
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASES;

    RETURN v_cursor;
END;

FUNCTION GET_DISEASE (
    id_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASES WHERE ADMIN.DISEASES.DISEASE_ID = id_a;

    RETURN v_cursor;
END;
END DISEASE_PACK;

DROP PACKAGE DISEASE_PACK;

CREATE OR REPLACE PACKAGE MTM_PACK AS
PROCEDURE DIAGNOSES_TO_MEDICINES (
    diagnose int,
    medicine int
);
PROCEDURE DIAGNOSES_TO_SYMPTOMS (
    diagnose int,
    symptom int
);
PROCEDURE DISEASES_TO_SYMPTOMS (
    disease int,
    symptom int
);
FUNCTION GET_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
)
    RETURN SYS_REFCURSOR;
FUNCTION GET_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR;
FUNCTION GET_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR;
PROCEDURE DELETE_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
);
PROCEDURE DELETE_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
);
PROCEDURE DELETE_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
);
END MTM_PACK;

CREATE OR REPLACE PACKAGE BODY MTM_PACK AS
PROCEDURE DIAGNOSES_TO_MEDICINES (
    diagnose int,
    medicine int
)
AS
BEGIN
    INSERT INTO DIAGNOSESTOMEDICINES (DIAGNOSE_ID, MEDICINE_ID)
                VALUES (diagnose, medicine);

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end;

PROCEDURE DIAGNOSES_TO_SYMPTOMS (
    diagnose int,
    symptom int
)
AS
BEGIN
    INSERT INTO DIAGNOSESTOSYMPTOMS (DIAGNOSE_ID, SYMPTOM_ID)
                VALUES (diagnose, symptom);

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end;

PROCEDURE DISEASES_TO_SYMPTOMS (
    disease int,
    symptom int
)
AS
BEGIN
    INSERT INTO DISEASESTOSYMPTOMS (DISEASE_ID, SYMPTOM_ID)
                VALUES (disease, symptom);

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
end;

FUNCTION GET_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DISEASESTOSYMPTOMS WHERE DISEASE_ID = disease_a;

    RETURN v_cursor;
END;

FUNCTION GET_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSESTOSYMPTOMS WHERE DIAGNOSE_ID = diagnose_a;

    RETURN v_cursor;
END;

FUNCTION GET_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM ADMIN.DIAGNOSESTOMEDICINES WHERE DIAGNOSE_ID = diagnose_a;

    RETURN v_cursor;
END;

PROCEDURE DELETE_DIAGNOSESTOMEDICINES_BY_DIAGNOSE (
    diagnose_a int
)
AS
BEGIN
    DELETE FROM DIAGNOSESTOMEDICINES WHERE DIAGNOSE_ID = diagnose_a;

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE (
    diagnose_a int
)
AS
BEGIN
    DELETE FROM DIAGNOSESTOSYMPTOMS WHERE DIAGNOSE_ID = diagnose_a;

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;

PROCEDURE DELETE_DISEASESTOSYMPTOMS_BY_DISEASE (
    disease_a int
)
AS
BEGIN
    DELETE FROM DISEASESTOSYMPTOMS WHERE DISEASE_ID = disease_a;

    commit;

EXCEPTION
    WHEN OTHERS THEN BEGIN
        ROLLBACK;

        RAISE_APPLICATION_ERROR(-20000, SQLERRM);
    END;
END;
END MTM_PACK;

DROP PACKAGE MTM_PACK;