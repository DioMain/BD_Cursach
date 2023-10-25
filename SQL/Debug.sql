create tablespace TEST_SPACE
    datafile 'C:\Users\1234\Desktop\oradata\ORCL\MEDKIT_PDB\TEST_SPACE.dbf'
    SIZE 7M
    AUTOEXTEND ON
    NEXT 5M
    MAXSIZE 20M;

drop tablespace TEST_SPACE including contents and datafiles;

alter user ADMIN quota 4M on TEST_SPACE;

create table Test (
    Id int primary key generated always as identity(start with 1 increment by 1),
    Name nvarchar2(64) not null,
    constraint foreign key

) tablespace TEST_SPACE;

insert into Test values (0, 'Name');

select * from Test;

drop table Test;