DROP TABLE IF EXISTS comments;

DROP TABLE IF EXISTS bookings;

DROP TABLE IF EXISTS items;

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
                       id serial primary key,
                       name varchar(50) not null,
                       email varchar(255) not null
);

CREATE TABLE IF NOT EXISTS requests (
                                        id serial primary key,
                                        description varchar(255) not null,
                                        requester_id int references users (id),
                                        created timestamp without time zone not null
);

CREATE TABLE IF NOT EXISTS items (
                       id serial primary key,
                       name varchar(50) not null,
                       description varchar(255) not null,
                       is_available boolean not null ,
                       owner_id int references users (id) not null,
                       request_id int references requests (id)
);

CREATE TABLE IF NOT EXISTS bookings (
                          id serial primary key,
                          start_date timestamp without time zone not null,
                          end_date timestamp without time zone not null,
                          item_id int references items (id) not null,
                          booker_id int references users (id) not null,
                          status varchar(10) not null
);

CREATE TABLE IF NOT EXISTS comments (
                          id serial primary key,
                          text varchar(255) not null,
                          item_id int references items (id) not null,
                          author_id int references users (id) not null,
                          created timestamp without time zone not null
);
