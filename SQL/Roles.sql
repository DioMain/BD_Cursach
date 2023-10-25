create role ADMIN_RL;

grant create session, create table, alter any table, drop any table, select any table to ADMIN_RL;
grant create procedure, alter any procedure, drop any procedure to ADMIN_RL;
grant create view, alter any analytic view, alter any materialized view,
    drop any analytic view, drop any materialized view to ADMIN_RL;


grant SYSDBA to ADMIN;

DROP ROLE ADMIN_RL;