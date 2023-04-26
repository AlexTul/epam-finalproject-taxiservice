-- FILL TABLES WITH DEV DATA --
insert into roles (id, role)
values (0, 'CLIENT'), (1, 'ADMINISTRATOR');

insert into users (id, first_name, last_name, email, password, role_id)
values (0, 'Admin', 'Super', 'admin@gmail.com', '9a497b0374e8e798f44291ad4a2fe4aad20f11bb', 1);