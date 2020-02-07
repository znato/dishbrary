-- RIGHTS
INSERT INTO dishbrary_right (id, name) VALUES (1, 'READ_USER');
INSERT INTO dishbrary_right (id, name) VALUES (2, 'WRITE_USER');

INSERT INTO dishbrary_right (id, name) VALUES (3, 'READ_RECIPE');
INSERT INTO dishbrary_right (id, name) VALUES (4, 'WRITE_RECIPE');

INSERT INTO dishbrary_right (id, name) VALUES (5, 'READ_CATEGORY');
INSERT INTO dishbrary_right (id, name) VALUES (6, 'WRITE_CATEGORY');

INSERT INTO dishbrary_right (id, name) VALUES (7, 'READ_INGREDIENT');
INSERT INTO dishbrary_right (id, name) VALUES (8, 'WRITE_INGREDIENT');

INSERT INTO dishbrary_right (id, name) VALUES (9, 'READ_CUISINE');
INSERT INTO dishbrary_right (id, name) VALUES (10, 'WRITE_CUISINE');

INSERT INTO dishbrary_right (id, name) VALUES (11, 'READ_COMMENT');
INSERT INTO dishbrary_right (id, name) VALUES (12, 'WRITE_COMMENT');

-- ROLES

INSERT INTO dishbrary_role (id, name) VALUES (1, 'ADMIN');

INSERT INTO dishbrary_role (id, name) VALUES (2, 'SIMPLE_USER');

--ROLE_RIGHTS (relation table)

--admin
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 1);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 2);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 3);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 4);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 5);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 6);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 7);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 8);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 9);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 10);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 11);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (1, 12);

--user
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 3);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 4);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 5);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 6);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 7);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 8);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 9);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 10);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 11);
INSERT INTO dishbrary_role_rights (roles_id, rights_id) VALUES (2, 12);