-- FILL TABLES WITH DEV DATA --

insert into users (id, first_name, last_name, email, password, role)
values (0, 'Admin', 'Super', 'admin@gmail.com', '9a497b0374e8e798f44291ad4a2fe4aad20f11bb', 'ADMINISTRATOR');

insert into cars (car_id, car_name, car_passengers, car_category, car_status)
values (0, 'Audi AX 0000 KX', 4, 'PASSENGER', 'AVAILABLE');
insert into cars (car_id, car_name, car_passengers, car_category, car_status)
values (1, 'Mercedes AX 1111 KX', 2, 'CARGO', 'AVAILABLE');
insert into cars (car_id, car_name, car_passengers, car_category, car_status)
values (2, 'Volkswagen AX 2222 KX', 4, 'PASSENGER', 'AVAILABLE');
insert into cars (car_id, car_name, car_passengers, car_category, car_status)
values (3, 'Volvo AX 3333 KX', 4, 'PASSENGER', 'INACTIVE');
insert into cars (car_id, car_name, car_passengers, car_category, car_status)
values (4, 'Gas AX 4444 KX', 2, 'CARGO', 'AVAILABLE');

insert into addresses (id, start_end, start_end_uk)
values (0, 'Pivnichnyy lane, 1-10 - Depovska street, 1 - 30', 'Північний провулок, 1-10 - Деповська вулиця, 1 - 30');
insert into  addresses (id, start_end, start_end_uk)
values (1, 'Poltavskyy Shlyah, 1-10 - Depovska street, 1 - 30', 'Полтавський шлях, 1-10 - Деповська вулиця, 1 - 30');
insert into addresses (id, start_end, start_end_uk)
values (2, 'Poltavskyy Shlyah, 24-12 - Depovska street, 71 - 26', 'Полтавський шлях, 24-12 - Деповська вулиця, 71 - 26');
insert into  addresses (id, start_end, start_end_uk)
values (3, 'Sadova str, 1-10 - Depovska street, 1 - 30', 'Садова вулиця, 1-10 - Деповська вулиця, 1 - 30');



insert into routes (id, address_id, distance, route_price, travel_time)
values (0, 0, 2, 40, 600);
insert into routes (id, address_id, distance, route_price, travel_time)
values (1, 1, 5, 45, 900);
insert into routes (id, address_id, distance, route_price, travel_time)
values (2, 2, 5, 45, 900);
insert into routes (id, address_id, distance, route_price, travel_time)
values (3, 3, 6, 50, 1200);