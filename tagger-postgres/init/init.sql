CREATE TYPE user_role AS ENUM ('ADMIN', 'GROUP', 'TEST');

CREATE TYPE lesson_time AS ENUM (
    'FIRST_LESSON',
    'SECOND_LESSON',
    'THIRD_LESSON',
    'FOURTH_LESSON',
    'FIFTH_LESSON',
    'SIXTH_LESSON',
    'SEVENTH_LESSON',
    'EIGHTH_LESSON'
    );

create table auditoriums
(
    id varchar(255) not null
        primary key
);

create table events
(
    id          serial
        primary key,
    description varchar(255),
    name        varchar(255) not null,
    time_start  timestamp(6) not null
);

create table groups
(
    id       serial
        primary key,
    course   integer      not null,
    group_id varchar(255) not null,
    subgroup integer      not null
);

create table lectors
(
    id  serial
        primary key,
    fio varchar(255) not null
);

create table studies
(
    id   serial
        primary key,
    name varchar(255) not null,
    url  varchar(255)
);

create table lessons
(
    id            serial
        primary key,
    day_of_week   varchar(255) not null,
    lesson_time   lesson_time not null,
    week          integer      not null,
    auditorium_id varchar(255)
        constraint fkmlbr15dx36uahyaw7w7ppjtaj
            references auditoriums,
    group_id      integer
        constraint fktdolsaotaqlwxbxwaxt00kimk
            references groups,
    lector_id     integer
        constraint fkg5smn6w1mq7q3sg2r5rp0hh4v
            references lectors,
    study_id      integer
        constraint fkswi637oy9j8k68gh555rh5b9n
            references studies
);

create table users
(
    id       bigint not null
        primary key,
    zachetka bigint,
    group_id integer
        constraint fkemfuglprp85bh5xwhfm898ysc
            references groups
);

create table user_roles
(
    user_id bigint not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references users,
    role    user_role
);

INSERT INTO users (id, group_id) VALUES (289070067, null);
INSERT INTO user_roles (user_id, role) VALUES (289070067, 'ADMIN');
INSERT INTO users (id, group_id) VALUES (328597719, null);
INSERT INTO user_roles (user_id, role) VALUES (328597719, 'ADMIN');
