create tablespace TEST_SPACE
    datafile 'C:\Users\1234\Desktop\oradata\ORCL\MEDKIT_PDB\TEST_SPACE.dbf'
    SIZE 7M
    AUTOEXTEND ON
    NEXT 5M
    MAXSIZE 20M;

drop tablespace TEST_SPACE including contents and datafiles;

grant SYSDBA to ADMIN;

create table Test (
    Id int generated always as identity(start with 1 increment by 1),
    primary key (Id),
    Name nvarchar2(64) not null

) tablespace TEST_SPACE;

select * from Test;

drop table Test;

insert into DISEASES(NAME, DESCRIPTION) values ('Test', 'AAAAAAA');

select * from USERS_S;