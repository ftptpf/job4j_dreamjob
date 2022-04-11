CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    city_id INT REFERENCES city (id),
    create_date DATE
);

CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    create_date DATE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);