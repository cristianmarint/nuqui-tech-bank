CREATE TABLE IF NOT EXISTS humans
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255),
    lastname       VARCHAR(255),
    identification VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);