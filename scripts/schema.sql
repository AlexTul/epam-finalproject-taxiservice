-- CREATE TABLES --

drop database if exists orders;

create table if not exists roles
(
    id   bigserial primary key,
    role text not null
);

create table if not exists users
(
    id         bigserial primary key,
    first_name text,
    last_name  text,
    email      text      not null unique,
    password   text      not null,
    role_id    bigserial not null references roles (id)
);

create table if not exists cars
(
    car_id         bigserial primary key,
    car_name       text   not null,
    car_passengers bigint not null check ( car_passengers > 0 ),
    car_category   text   not null,
    car_status     text   not null
);

create table if not exists orders
(
    id               bigserial primary key,
    date             timestamp not null,
    customer_id      bigint    not null references users (id) on delete cascade,
    order_passengers bigint    not null check ( order_passengers > 0 ),
    start_travel     text      not null,
    end_travel       text      not null,
    travel_distance  decimal   not null check ( travel_distance > 0.0 ),
    travel_duration  decimal   not null check ( travel_duration > 0.0 ),
    cost             decimal   not null check ( cost > 0.0 ),
    started_at       timestamp not null,
    finished_at      timestamp not null,

    check ( date <= orders.started_at and started_at <= finished_at )
);

create table order_car
(
    o_id bigserial not null references orders (id) on delete cascade,
    c_id bigserial not null references cars (car_id) on delete cascade
);