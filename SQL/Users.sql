-- ОБЩЕЕ

create profile COMMON_PROFILE limit
    PASSWORD_LIFE_TIME 180
    SESSIONS_PER_USER 100
    FAILED_LOGIN_ATTEMPTS 50
    PASSWORD_LOCK_TIME 1
    PASSWORD_REUSE_TIME 10
    PASSWORD_GRACE_TIME DEFAULT
    CONNECT_TIME 600
    IDLE_TIME 120;

DROP PROFILE COMMON_PROFILE;

-- ДЛЯ АДМИНА

ALTER USER ADMIN DEFAULT TABLESPACE MEDKIT_TS;
ALTER USER ADMIN TEMPORARY TABLESPACE MEDKIT_TEMP_TS;

ALTER USER ADMIN QUOTA 5M on MEDKIT_TS;

-- ДЛЯ ВРАЧЕЙ

CREATE USER DOCTOR IDENTIFIED BY "DOC"
    default tablespace MEDKIT_TS
    temporary tablespace MEDKIT_TEMP_TS
    profile COMMON_PROFILE
    quota 32M on MEDKIT_TS;

GRANT DOCTOR_RL TO DOCTOR;

-- ДЛЯ ПАЦИЕНТА

CREATE USER PATIENT IDENTIFIED BY "PAT"
    default tablespace MEDKIT_TS
    temporary tablespace MEDKIT_TEMP_TS
    profile COMMON_PROFILE
    quota 16M on MEDKIT_TS;

GRANT PATIENT_RL TO PATIENT;

-- ДЛЯ ЛОГГИРОВАНИЯ И РЕГЕСТРАЦИИ

CREATE USER LOGREG IDENTIFIED BY "LR"
    default tablespace MEDKIT_TS
    temporary tablespace MEDKIT_TEMP_TS
    profile COMMON_PROFILE
    quota 1M on MEDKIT_TS;

GRANT LOGREG_RL TO LOGREG;

-- DELETE

DROP USER DOCTOR CASCADE;
DROP USER PATIENT CASCADE;
DROP USER LOGREG CASCADE;
