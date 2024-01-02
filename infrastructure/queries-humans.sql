select * from humans;

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (7, 'Eliezer', 'Thompson', 750, 'celestine96@hotmail.com', 'username', 'password7')
ON CONFLICT (id) DO NOTHING;

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (8, 'Carlos', 'Saldarriaga', 850, 'carlos@hotmail.com', 'carlos', 'password8')
ON CONFLICT (id) DO NOTHING;

INSERT INTO humans (id, name, lastname, identification, email, username, password)
VALUES (69, 'nuqui', 'tech', 69, 'contact@nuqui.tech', 'nuqui tech fee', 'password69')
ON CONFLICT (id) DO NOTHING;