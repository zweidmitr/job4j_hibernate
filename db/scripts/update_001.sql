create table if not exists candidates
(
    id   serial primary key,
    name varchar(255)
);
create table persons
(
    id   serial primary key,
    name varchar(2000)
);
create table addresses
(
    id     serial primary key,
    number varchar(255),
    street varchar(2000)
);
create table j_role
(
    id   serial primary key,
    name varchar(2000)
);
create table j_user
(
    id   serial primary key,
    name varchar(2000)
);
create table car
(
    id   serial primary key,
    name varchar(255)
);
create table c_model
(
    id   serial primary key,
    name varchar(255)
);

drop table car cascade;
drop table c_model cascade;
-- drop table car_c_model cascade;
drop table j_user cascade;
drop table j_role cascade;
drop table addresses;
drop table persons cascade;
drop table addresses cascade;
-- drop table persons_addresses cascade ;

-- toone
--
-- create table j_user
-- (
--     id      serial primary key,
--     name    varchar(2000),
--     role_id int not null references j_role (id)
-- );