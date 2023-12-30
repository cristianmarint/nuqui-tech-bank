CREATE TABLE IF NOT EXISTS humans
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255),
    lastname       VARCHAR(255),
    identification VARCHAR(255) UNIQUE
);