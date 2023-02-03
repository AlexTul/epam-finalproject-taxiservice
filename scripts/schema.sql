-- CREATE TABLES --

drop database if exists orders;

create table if not exists users
(
    id         bigserial primary key,
    first_name text,
    last_name  text,
    email      text not null unique,
    password   text not null,
    role       text not null
);

create table if not exists cars
(
    car_id         bigserial primary key,
    car_name       text   not null,
    car_passengers bigint not null check ( car_passengers > 0 ),
    car_category   text   not null,
    car_status     text   not null
);

create table if not exists addresses
(
    id           bigserial primary key,
    start_end    text not null unique,
    start_end_uk text not null unique
);

create table if not exists routes
(
    id          bigserial primary key,
    address_id  bigint  not null references addresses (id),
    distance    bigint  not null check ( distance > 0 ),
    route_price decimal not null check ( route_price > 0.0 ),
    travel_time bigint  not null check ( travel_time > 0 )
);

create table if not exists orders
(
    id               bigserial primary key,
    date             timestamp not null,
    customer_id      bigint    not null references users (id),
    order_passengers bigint    not null check ( order_passengers > 0 ),
    route_id         bigint    not null references routes (id),
    cost             decimal   not null check ( cost > 0.0 ),
    started_at       timestamp not null,
    finished_at      timestamp not null,

    check ( date <= orders.started_at and started_at <= finished_at )
);

create table order_car
(
    o_id bigserial not null references orders (id),
    c_id bigserial not null references cars (car_id)
);