/*create role ADMIN_RL;

grant create session, create table, alter any table, drop any table, select any table to ADMIN_RL;
grant create procedure, alter any procedure, drop any procedure to ADMIN_RL;
grant create view, alter any analytic view, alter any materialized view,
    drop any analytic view, drop any materialized view to ADMIN_RL;
grant create tablespace, drop tablespace to ADMIN_RL;*/

CREATE ROLE DOCTOR_RL;

GRANT SELECT ANY TABLE, EXECUTE ANY PROCEDURE, CREATE SESSION TO DOCTOR_RL;

GRANT INSERT, UPDATE, DELETE ON ADMIN.Diagnoses TO DOCTOR_RL;
GRANT INSERT, UPDATE, DELETE ON ADMIN.DiagnosesToMedicines TO DOCTOR_RL;
GRANT INSERT, UPDATE, DELETE ON ADMIN.DiagnosesToSymptoms TO DOCTOR_RL;
GRANT INSERT, UPDATE, DELETE ON ADMIN.DiseasesToSymptoms TO DOCTOR_RL;

GRANT UPDATE, DELETE ON ADMIN.Appointments TO DOCTOR_RL;


CREATE ROLE PATIENT_RL;

GRANT SELECT ANY TABLE, EXECUTE ANY PROCEDURE, CREATE SESSION TO PATIENT_RL;

GRANT INSERT, UPDATE, DELETE ON ADMIN.Appointments TO PATIENT_RL;

CREATE ROLE LOGREG_RL;

GRANT EXECUTE ANY PROCEDURE, CREATE SESSION TO LOGREG_RL;

GRANT SELECT, INSERT ON ADMIN.USERS TO LOGREG_RL;

-- DELETE

DROP ROLE ADMIN_RL;
DROP ROLE DOCTOR_RL;
DROP ROLE PATIENT_RL;
DROP ROLE LOGREG_RL;

