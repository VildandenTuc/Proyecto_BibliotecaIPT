-- V1__init_library_schema.sql

CREATE TABLE usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  dni VARCHAR(20) NOT NULL,
  tipo_usuario ENUM('ALUMNO','DOCENTE') NOT NULL,
  email VARCHAR(100),
  telefono VARCHAR(20),
  INDEX idx_apellido (apellido),
  INDEX idx_dni (dni)
);

CREATE TABLE categorias (
  id_categoria INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  INDEX idx_nombre (nombre)
);

CREATE TABLE libros (
  id_libro INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  autores VARCHAR(250) NOT NULL,
  anio_publicacion INT,
  editorial VARCHAR(150),
  cantidad_ejemplares INT NOT NULL,
  id_categoria INT,
  INDEX idx_titulo (titulo),
  INDEX idx_autores (autores),
  FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

CREATE TABLE prestamos (
  id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  id_libro INT NOT NULL,
  fecha_prestamo DATE NOT NULL,
  fecha_devolucion DATE,
  devuelto BOOLEAN NOT NULL DEFAULT FALSE,
  falta BOOLEAN NOT NULL DEFAULT FALSE,
  observacion VARCHAR(250),
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
  FOREIGN KEY (id_libro) REFERENCES libros(id_libro)
);
