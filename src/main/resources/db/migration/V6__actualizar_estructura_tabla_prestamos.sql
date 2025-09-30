ALTER TABLE prestamos
    ADD COLUMN fecha_devolucion_esperada DATE NOT NULL,
    ADD COLUMN fecha_devolucion_real DATE;

ALTER TABLE prestamos
    DROP COLUMN fecha_devolucion;
