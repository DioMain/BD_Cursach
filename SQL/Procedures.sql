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
    v_encrypted_raw := dbms_crypto.hash(
            utl_i18n.string_to_raw( CONCAT(password_a, 'MEDKIT'), 'AL32UTF8'),
            dbms_crypto.HASH_SH256
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
        select USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC,
               'HIDDEN' AS PASSWORD,
               BIRTHDAY,
               SUBSTR(PHONE_NUMBER, 0, 7) || '*******' AS PHONE_NUMBER,
               EMAIL from USERS;

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
    v_encrypted := dbms_crypto.hash(
        utl_i18n.string_to_raw( CONCAT(a_password, 'MEDKIT'), 'AL32UTF8'),
        dbms_crypto.HASH_SH256
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

DROP PROCEDURE REGISTRATION_NEW_USER;
DROP FUNCTION GET_ALL_USERS;
DROP FUNCTION GET_CURRENT_USER;
DROP PROCEDURE DELETE_CURRENT_USER;
DROP PROCEDURE UPDATE_CURRENT_USER;

--

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
    user_cursor := GET_CURRENT_USER('dima123.zample@gmail.com', 'ADMINN');

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
