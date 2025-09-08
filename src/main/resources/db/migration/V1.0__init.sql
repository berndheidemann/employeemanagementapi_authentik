create table employee
(
    id         serial primary key,
    last_name  varchar(50) not null,
    first_name varchar(50) not null,
    street     varchar(50) not null,
    zip        varchar(5)  not null,
    city       varchar(50) not null,
    phone      varchar(30) not null
);

create table qualification
(
    id          serial primary key,
    designation varchar(50) not null unique
);

create table employee_qualification
(
    e_id integer not null,
    q_id integer not null,
    constraint fk_employee foreign key (e_id) references employee (id),
    constraint fk_qualification foreign key (q_id) references qualification (id)
);