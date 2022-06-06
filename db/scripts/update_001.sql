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
    id   serial primary key,
    name varchar(2000)
);

drop table j_user cascade;
drop table j_role cascade;
delete
from j_user;
delete
from j_role;
delete
from j_role_j_user;

-- toone
--
-- create table j_user
-- (
--     id      serial primary key,
--     name    varchar(2000),
--     role_id int not null references j_role (id)
-- );