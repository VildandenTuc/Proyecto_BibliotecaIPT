-- V2__add_unique_constraint_dni.sql

ALTER TABLE usuarios
ADD CONSTRAINT unique_dni UNIQUE (dni);
