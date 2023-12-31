DROP TABLE IF EXISTS humans;

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

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (7, 'Eliezer', 'Thompson', 750, 'celestine96@hotmail.com', 'username', 'password7')
ON CONFLICT (id) DO NOTHING;

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (8, 'Carlos', 'Saldarriaga', 850, 'carlos@hotmail.com', 'carlos', 'password8')
ON CONFLICT (id) DO NOTHING;

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (69, 'Nuqui', 'Tech', 69, 'contact@nuqui.tech', 'nuqui tech fee', 'password69')
ON CONFLICT (id) DO NOTHING;