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
BEGIN
    INSERT INTO ADMIN.USERS (USER_ROLE, NAME, SURNAME, PATRONYMIC, PASSWORD, BIRTHDAY, PHONE_NUMBER, EMAIL)
        values (user_role_a, name_a, surname_a, patronymic_a, password_a, birthday_a, phone_number_a, email_a);

    commit;
end REGISTRATION_NEW_USER;

CREATE OR REPLACE FUNCTION GET_ALL_USERS
RETURN SYS_REFCURSOR
IS
    v_result SYS_REFCURSOR;
BEGIN
    OPEN v_result FOR
        select USER_ID, USER_ROLE, NAME, SURNAME, PATRONYMIC, PASSWORD, BIRTHDAY from USERS;

    RETURN v_result;
end;

CREATE OR REPLACE PROCEDURE DEBUG_GET_ALL_USERS
AS
BEGIN

    DECLARE user_id INT;
        user_role nvarchar2(32);
        user_name nvarchar2(64);
        user_surname nvarchar2(64);
        user_patronymic nvarchar2(64);
        user_password nvarchar2(128);
        user_birthday date;
        user_cursor SYS_REFCURSOR;
    BEGIN

    user_cursor := GET_ALL_USERS();

    LOOP
        FETCH user_cursor INTO user_id, user_role, user_name, user_surname, user_patronymic, user_password, user_birthday;

        EXIT WHEN user_cursor%NOTFOUND;

        INSERT INTO ADMIN.DEBUG_LOG (TITLE, DESCRIPTION)
            values ('USER_INFO', 'ID: ' || TO_CHAR(user_id) || ', NAME: ' || TO_CHAR(user_name) || ', ROLE: ' || TO_CHAR(user_role));
    END LOOP;

    CLOSE user_cursor;
    END;

    commit;
END;



DROP PROCEDURE REGISTRATION_NEW_USER;
DROP PROCEDURE DEBUG_GET_ALL_USERS;
DROP FUNCTION GET_ALL_USERS;
