CREATE TABLE IF EXISTS users (
                       id serial primary key,
                       name varchar(50) not null,
                       email varchar(255) not null
);

CREATE TABLE IF EXISTS items (
                       id serial primary key,
                       name varchar(50) not null,
                       description varchar(255) not null,
                       is_available boolean not null ,
                       owner_id int references users (id) not null
);

CREATE TABLE IF EXISTS bookings (
                          id serial primary key,
                          start_date timestamp without time zone not null,
                          end_date timestamp without time zone not null,
                          item_id int references items (id) not null,
                          booker_id int references bookings (id) not null,
                          status varchar(10) not null
);

CREATE TABLE IF EXISTS comments (
                          id serial primary key,
                          text varchar(255) not null,
                          item_id int references items (id) not null,
                          author_id int references users (id) not null,
                          created timestamp without time zone not null
);