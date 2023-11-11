create table Users (
    user_id int generated always as identity (start with 1 increment by 1),
    primary key (user_id),

    user_role nvarchar2(32) not null,
    name nvarchar2(64) not null,
    surname nvarchar2(64) not null,
    patronymic nvarchar2(64) not null,
    password nvarchar2(128) not null,
    birthday date not null,
    phone_number nvarchar2(32) not null,
    email nvarchar2(64) not null
) tablespace MEDKIT_TS;

create table Appointments (
    appointment_id int generated always as identity (start with 1 increment by 1),
    primary key (appointment_id),

    doctor_id int not null,
    foreign key (doctor_id) references Users(user_id),

    patient_id int not null,
    foreign key (patient_id) references Users(user_id),

    appointment_date date not null,
    appointment_state int default(0)
) tablespace MEDKIT_TS;

create table Diseases (
    disease_id int generated always as identity (start with 1 increment by 1),
    primary key (disease_id),

    name nvarchar2(64) not null,
    description nvarchar2(512)
) tablespace MEDKIT_TS;

create table Diagnoses (
    diagnose_id int generated always as identity (start with 1 increment by 1),
    primary key (diagnose_id),

    doctor_id int not null,
    foreign key (doctor_id) references Users(user_id),

    patient_id int not null,
    foreign key (patient_id) references Users(user_id),

    disease_id int,
    foreign key (disease_id) references Diseases(disease_id),

    open_date date not null,
    close_date date,
    note nvarchar2(128),
    description nvarchar2(512),
    diagnose_state int default(0)
) tablespace MEDKIT_TS;

create table Symptoms (
    symptom_id int generated always as identity (start with 1 increment by 1),
    primary key (symptom_id),

    name nvarchar2(64) not null,
    description nvarchar2(256)
) tablespace MEDKIT_TS;

create table Medicines (
    medicine_id int generated always as identity (start with 1 increment by 1),
    primary key (medicine_id),

    name nvarchar2(64) not null,
    description nvarchar2(512),
    manufacturer nvarchar2(64) not null,
    price number(12, 4) default(0),
    start_date date not null
) tablespace MEDKIT_TS;

create table DiagnosesToMedicines (
    id int generated always as identity (start with 1 increment by 1),
    primary key (id),

    diagnose_id int not null,
    foreign key (diagnose_id) references Diagnoses(diagnose_id),

    medicine_id int not null,
    foreign key (medicine_id) references Medicines(medicine_id)
) tablespace MEDKIT_TS;

create table DiagnosesToSymptoms (
    id int generated always as identity (start with 1 increment by 1),
    primary key (id),

    diagnose_id int not null,
    foreign key (diagnose_id) references Diagnoses(diagnose_id),

    symptom_id int not null,
    foreign key (symptom_id) references Symptoms(symptom_id)
) tablespace MEDKIT_TS;

create table DiseasesToSymptoms (
    id int generated always as identity (start with 1 increment by 1),
    primary key (id),

    disease_id int not null,
    foreign key (disease_id) references Diseases(disease_id),

    symptom_id int not null,
    foreign key (symptom_id) references Symptoms(symptom_id)
) tablespace MEDKIT_TS;

-- ДЛЯ ОТЛАДКИ (БУДЕТ УДАЛЕНА НА РЕЛИЗЕ)
create table DEBUG_LOG (
    id int generated always as identity (start with 1 increment by 1),
    primary key (id),

    title nvarchar2(64) not null,
    description nvarchar2(512) default('DESCRIPTION NOT DEFINED!')
) tablespace MEDKIT_TS;
-------

commit;

DROP TABLE DiseasesToSymptoms;
DROP TABLE DiagnosesToMedicines;
DROP TABLE DiagnosesToSymptoms;
DROP TABLE Medicines;
DROP TABLE Symptoms;
DROP TABLE Diagnoses;
DROP TABLE Diseases;
DROP TABLE Appointments;
DROP TABLE Users;

DROP TABLE DEBUG_LOG;

commit;
