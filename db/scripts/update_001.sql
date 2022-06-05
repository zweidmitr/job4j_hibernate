create table if not exists candidates
(
    id   serial primary key,
    name varchar(255)
);

create table j_role
(
    id   serial primary key,
    name varchar(2000)
);

create table j_user
(
    id      serial primary key,
    name    varchar(2000),
    role_id int not null references j_role (id)
);