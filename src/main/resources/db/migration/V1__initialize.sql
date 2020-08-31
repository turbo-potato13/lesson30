create table users (
  id                    bigserial,
  username              varchar(30) not null,
  password              varchar(80) not null,
  email                 varchar(50) unique,
  primary key (id)
);

create table roles (
  id                    serial,
  name                  varchar(50) not null,
  primary key (id)
);

CREATE TABLE users_roles (
  user_id               bigint not null,
  role_id               int not null,
  primary key (user_id, role_id),
  foreign key (user_id) references users (id),
  foreign key (role_id) references roles (id)
);

insert into roles (name)
values
('ROLE_USER'), ('ROLE_ADMIN'), ('DELETE_USERS_PERMISSION');

insert into users (username, password, email)
values
('Bob', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com'),
('John Johnson', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com');

insert into users_roles (user_id, role_id) values (1, 1), (1, 3);

create table books (id bigserial primary key, title varchar(255), description varchar(5000), genre varchar(255), price numeric(8, 2), publish_year int);
insert into books (title, description, genre, price, publish_year) values
('Harry Potter: Philosopher''s Stone', 'Description 1', 'FANTASY', 300.0, 2000),
('Harry Potter: Chamber of Secrets', 'Description 2', 'DETECTIVE', 400.0, 2001),
('Harry Potter: Prisoner of Azkaban', 'Description 3', 'FANTASY', 500.0, 2002),
('Harry Potter: Goblet of Fire', 'Description 4', 'DETECTIVE', 700.0, 2007),
('Harry Potter: Order of the Phoenix', 'Description 5', 'FANTASY', 440.0, 2004),
('Harry Potter: Half-Blood Price', 'Description 6', 'DETECTIVE', 650.0, 2007),
('Harry Potter: Deathly Hallows', 'Description 6', 'DETECTIVE', 650.0, 2007),
('Lockwood & Co.', 'Description 7', 'FANTASY', 200.0, 2006),
('Neverwhere', 'Description 7', 'FANTASY', 200.0, 2000),
('Mistborn', 'Description 7', 'FANTASY', 200.0, 2000),
('Ambers Chronicles', 'Description 17', 'FANTASY', 200.0, 1989),
('Lord Of The Ring: The Fellowship of the Ring', 'Description 8', 'FANTASY', 1200.0, 2006),
('Lord Of The Ring: The Two Towers', 'Description 9', 'FANTASY', 900.0, 2004),
('Lord Of The Ring: The Return of the King', 'Description 10', 'FANTASY', 600.0, 2001),
('Hobbit', 'Description 11', 'FICTION', 500.0, 2001);

drop table if exists orders cascade;
create table orders (id bigserial, user_id bigint not null, price numeric(8, 2) not null, status varchar(255), primary key(id), constraint fk_user_id foreign key (user_id) references users (id));

drop table if exists orders_items cascade;
create table orders_items (id bigserial, order_id bigint not null, book_id bigint not null, quantity int, price numeric(8, 2), primary key(id), constraint fk_book_id foreign key (book_id) references books (id), constraint fk_order_id foreign key (order_id) references orders (id));

