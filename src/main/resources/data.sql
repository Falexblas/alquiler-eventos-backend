CREATE DATABASE IF NOT EXISTS alquiler_eventos;
USE alquiler_eventos;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT
);


CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);


CREATE TABLE distritos (
    id_distrito INT AUTO_INCREMENT PRIMARY KEY,
    nombre_distrito VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE locales (
    id_local INT AUTO_INCREMENT PRIMARY KEY,
    nombre_local VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    id_distrito INT NOT NULL,
    aforo_maximo INT NOT NULL,
    precio_hora DECIMAL(10,2) NOT NULL,
    descripcion TEXT,
    estado ENUM('DISPONIBLE','NO_DISPONIBLE') DEFAULT 'DISPONIBLE',
    FOREIGN KEY (id_distrito) REFERENCES distritos(id_distrito)
);

CREATE TABLE fotos_locales (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_local INT NOT NULL,
    url_foto VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_local) REFERENCES locales(id_local) ON DELETE CASCADE
);

CREATE TABLE tipos_evento (
    id_tipo_evento INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(100) NOT NULL UNIQUE, -- Ej: Boda, Conferencia, Cumpleaños
    descripcion TEXT
);

CREATE TABLE local_tipo_evento (
    id_local INT NOT NULL,
    id_tipo_evento INT NOT NULL,
    PRIMARY KEY (id_local, id_tipo_evento),
    FOREIGN KEY (id_local) REFERENCES locales(id_local) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo_evento) REFERENCES tipos_evento(id_tipo_evento) ON DELETE CASCADE
);

CREATE TABLE mobiliario (
    id_mobiliario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    stock_total INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL
);


CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_local INT NOT NULL,
    id_tipo_evento INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    cantidad_personas INT NOT NULL,
    costo_total DECIMAL(12,2) NOT NULL,
    estado ENUM('Pendiente','Confirmada','Cancelada') DEFAULT 'Pendiente',
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_local) REFERENCES locales(id_local),
    FOREIGN KEY (id_tipo_evento) REFERENCES tipos_evento(id_tipo_evento),
    CHECK (cantidad_personas > 0)
);

CREATE TABLE reserva_mobiliario (
    id_reserva INT NOT NULL,
    id_mobiliario INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_reserva, id_mobiliario),
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
    FOREIGN KEY (id_mobiliario) REFERENCES mobiliario(id_mobiliario)
);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
	metodo_pago ENUM('Tarjeta','Yape','Plin'),
    monto DECIMAL(12,2) NOT NULL,
    estado ENUM('Pendiente','Pagado','Fallido') DEFAULT 'Pendiente',
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comprobante_url VARCHAR(255),
	FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE
);

-- Insertar roles
INSERT INTO roles (nombre_rol, descripcion) VALUES 
('ADMIN', 'Administrador del sistema'),
('CLIENTE', 'Cliente que puede hacer reservas');

-- Insertar distritos
INSERT INTO distritos (nombre_distrito) VALUES 
('Miraflores'),
('San Isidro'),
('Barranco'),
('Surco'),
('La Molina'),
('Jesús María'),
('Magdalena'),
('San Borja');

-- Insertar tipos de evento
INSERT INTO tipos_evento (nombre_tipo, descripcion) VALUES 
('Boda', 'Ceremonias matrimoniales y recepciones'),
('Cumpleaños', 'Celebraciones de cumpleaños y fiestas familiares'),
('Conferencia', 'Eventos corporativos y conferencias de negocios'),
('Graduación', 'Ceremonias de graduación y celebraciones académicas'),
('Baby Shower', 'Celebraciones previas al nacimiento'),
('Quinceañero', 'Celebraciones de quinceañeras'),
('Aniversario', 'Celebraciones de aniversarios'),
('Evento Corporativo', 'Reuniones y eventos de empresas');

-- Insertar usuarios (contraseñas encriptadas con BCrypt para "123456")
INSERT INTO usuarios (nombre, apellido, email, telefono, contrasena, id_rol) VALUES 
('Admin', 'Sistema', 'admin@alquileventos.com', '999888777', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 1),
('Juan', 'Pérez', 'juan.perez@email.com', '987654321', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2),
('María', 'García', 'maria.garcia@email.com', '987654322', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2),
('Carlos', 'López', 'carlos.lopez@email.com', '987654323', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2);

-- Insertar locales
INSERT INTO locales (nombre_local, direccion, id_distrito, aforo_maximo, precio_hora, descripcion, estado) VALUES 
('Salón Elegance', 'Av. Larco 1234, Miraflores', 1, 150, 250.00, 'Elegante salón con vista al mar, ideal para bodas y eventos especiales', 'DISPONIBLE'),
('Centro de Convenciones Lima', 'Av. Javier Prado 2456, San Isidro', 2, 300, 400.00, 'Moderno centro de convenciones equipado con tecnología de punta', 'DISPONIBLE'),
('Salón Barranco Club', 'Av. Grau 789, Barranco', 3, 80, 180.00, 'Acogedor salón en el corazón de Barranco, perfecto para eventos íntimos', 'DISPONIBLE'),
('Hacienda Los Jardines', 'Av. Benavides 3456, Surco', 4, 200, 320.00, 'Hermosa hacienda con amplios jardines y piscina', 'DISPONIBLE'),
('Salón Crystal', 'Av. La Molina 567, La Molina', 5, 120, 220.00, 'Salón moderno con decoración minimalista y excelente iluminación', 'DISPONIBLE');

-- Insertar relaciones local-tipo evento
INSERT INTO local_tipo_evento (id_local, id_tipo_evento) VALUES 
(1, 1), (1, 4), (1, 7), -- Salón Elegance: Bodas, Graduaciones, Aniversarios
(2, 3), (2, 8), -- Centro de Convenciones: Conferencias, Eventos Corporativos
(3, 2), (3, 5), (3, 6), -- Salón Barranco: Cumpleaños, Baby Shower, Quinceañeros
(4, 1), (4, 2), (4, 4), (4, 7), -- Hacienda Los Jardines: Bodas, Cumpleaños, Graduaciones, Aniversarios
(5, 1), (5, 2), (5, 3), (5, 8); -- Salón Crystal: Bodas, Cumpleaños, Conferencias, Eventos Corporativos

-- Insertar fotos de locales
INSERT INTO fotos_locales (id_local, url_foto, descripcion) VALUES 
(1, '/images/salon-elegance-1.jpg', 'Vista principal del salón'),
(1, '/images/salon-elegance-2.jpg', 'Área de recepción'),
(2, '/images/centro-convenciones-1.jpg', 'Auditorio principal'),
(2, '/images/centro-convenciones-2.jpg', 'Salas de reuniones'),
(3, '/images/barranco-club-1.jpg', 'Salón principal'),
(4, '/images/hacienda-jardines-1.jpg', 'Jardines exteriores'),
(4, '/images/hacienda-jardines-2.jpg', 'Área de piscina'),
(5, '/images/salon-crystal-1.jpg', 'Salón con iluminación nocturna');

-- Insertar mobiliario
INSERT INTO mobiliario (nombre, descripcion, stock_total, precio_unitario) VALUES 
('Mesa Redonda 8 personas', 'Mesa redonda elegante para 8 comensales', 20, 25.00),
('Mesa Rectangular 6 personas', 'Mesa rectangular para 6 comensales', 15, 20.00),
('Silla Chiavari Dorada', 'Silla elegante color dorado', 200, 8.00),
('Silla Chiavari Plateada', 'Silla elegante color plateado', 150, 8.00),
('Mantel Blanco', 'Mantel blanco de alta calidad', 50, 12.00),
('Mantel Ivory', 'Mantel color marfil', 40, 12.00),
('Centro de Mesa Floral', 'Arreglo floral para centro de mesa', 30, 35.00),
('Proyector HD', 'Proyector de alta definición para presentaciones', 5, 80.00),
('Sistema de Sonido', 'Equipo de sonido profesional', 8, 120.00),
('Tarima Pequeña', 'Tarima para eventos de 2x2 metros', 6, 60.00);

-- Insertar algunas reservas de ejemplo
INSERT INTO reservas (id_usuario, id_local, id_tipo_evento, fecha, hora_inicio, hora_fin, cantidad_personas, costo_total, estado) VALUES 
(2, 1, 1, '2024-12-15', '18:00:00', '23:00:00', 120, 1250.00, 'Confirmada'),
(3, 3, 2, '2024-11-20', '15:00:00', '20:00:00', 60, 900.00, 'Pendiente'),
(4, 2, 3, '2024-11-25', '09:00:00', '17:00:00', 200, 3200.00, 'Confirmada');

-- Insertar mobiliario para reservas
INSERT INTO reserva_mobiliario (id_reserva, id_mobiliario, cantidad, precio_unitario) VALUES 
(1, 1, 15, 25.00), -- 15 mesas redondas para la boda
(1, 3, 120, 8.00), -- 120 sillas doradas
(1, 5, 15, 12.00), -- 15 manteles blancos
(1, 7, 15, 35.00), -- 15 centros de mesa florales
(2, 2, 10, 20.00), -- 10 mesas rectangulares para cumpleaños
(2, 4, 60, 8.00), -- 60 sillas plateadas
(3, 8, 2, 80.00), -- 2 proyectores para conferencia
(3, 9, 1, 120.00); -- 1 sistema de sonido

-- Insertar pagos
INSERT INTO pagos (id_reserva, metodo_pago, monto, estado, comprobante_url) VALUES 
(1, 'Tarjeta', 1250.00, 'Pagado', '/comprobantes/pago-001.pdf'),
(2, 'Yape', 900.00, 'Pendiente', NULL),
(3, 'Plin', 3200.00, 'Pagado', '/comprobantes/pago-003.pdf');

select*from locales