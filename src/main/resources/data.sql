drop table if exists roles;
drop table if exists usuarios;
drop table if exists distritos;
drop table if exists locales;
drop table if exists fotos_locales;
drop table if exists tipos_evento;
drop table if exists local_tipo_evento;
drop table if exists mobiliario;
drop table if exists fotos_mobiliario;
drop table if exists metodos_pago;
drop table if exists reservas;
drop table if exists reserva_mobiliario;
drop table if exists pagos;

-- Roles
CREATE TABLE roles (
                       id_rol INT AUTO_INCREMENT PRIMARY KEY,
                       nombre_rol VARCHAR(50) NOT NULL UNIQUE,
                       descripcion TEXT
);

-- Usuarios
CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          dni VARCHAR(8) NOT NULL UNIQUE,
                          celular VARCHAR(20) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          contrasena VARCHAR(255) NOT NULL,
                          id_rol INT NOT NULL,
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Distritos
CREATE TABLE distritos (
                           id_distrito INT AUTO_INCREMENT PRIMARY KEY,
                           nombre_distrito VARCHAR(100) NOT NULL UNIQUE
);

-- Locales
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

-- Fotos local
CREATE TABLE fotos_locales (
                               id_foto INT AUTO_INCREMENT PRIMARY KEY,
                               id_local INT NOT NULL,
                               url_foto VARCHAR(255) NOT NULL,
                               descripcion VARCHAR(255),
                               FOREIGN KEY (id_local) REFERENCES locales(id_local) ON DELETE CASCADE
);

-- Tipos de evento
CREATE TABLE tipos_evento (
                              id_tipo_evento INT AUTO_INCREMENT PRIMARY KEY,
                              nombre_tipo VARCHAR(100) NOT NULL UNIQUE,
                              descripcion TEXT
);

-- Local-evento
CREATE TABLE local_tipo_evento (
                                   id_local INT NOT NULL,
                                   id_tipo_evento INT NOT NULL,
                                   PRIMARY KEY (id_local, id_tipo_evento),
                                   FOREIGN KEY (id_local) REFERENCES locales(id_local) ON DELETE CASCADE,
                                   FOREIGN KEY (id_tipo_evento) REFERENCES tipos_evento(id_tipo_evento) ON DELETE CASCADE
);

-- Mobiliario
CREATE TABLE mobiliario (
                            id_mobiliario INT AUTO_INCREMENT PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL,
                            descripcion TEXT,
                            stock_total INT NOT NULL,
                            precio_unitario DECIMAL(10,2) NOT NULL
);

-- Fotos mobiliario
CREATE TABLE fotos_mobiliario (
                                  id_foto INT AUTO_INCREMENT PRIMARY KEY,
                                  id_mobiliario INT NOT NULL,
                                  url_foto VARCHAR(255) NOT NULL,
                                  descripcion VARCHAR(255),
                                  FOREIGN KEY (id_mobiliario) REFERENCES mobiliario(id_mobiliario) ON DELETE CASCADE
);

-- Métodos de pago
CREATE TABLE metodos_pago (
                              id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
                              nombre_metodo VARCHAR(50) NOT NULL UNIQUE,
                              descripcion TEXT
);

-- Reservas
CREATE TABLE reservas (
                          id_reserva INT AUTO_INCREMENT PRIMARY KEY,
                          id_usuario INT NOT NULL,
                          id_local INT NOT NULL,
                          id_tipo_evento INT NOT NULL,
                          fecha DATE NOT NULL,
                          hora_inicio TIME NOT NULL,
                          hora_fin TIME NOT NULL,
                          cantidad_personas INT NOT NULL,
                          costo_local DECIMAL(10,2) NOT NULL,
                          costo_mobiliario DECIMAL(10,2) DEFAULT 0.00,
                          costo_total DECIMAL(12,2) NOT NULL,
                          estado ENUM('Pendiente','Confirmada','Cancelada') DEFAULT 'Pendiente',
                          fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                          FOREIGN KEY (id_local) REFERENCES locales(id_local),
                          FOREIGN KEY (id_tipo_evento) REFERENCES tipos_evento(id_tipo_evento),
                          CHECK (cantidad_personas > 0)
);

-- Detalle de mobiliario reservado
CREATE TABLE reserva_mobiliario (
                                    id_reserva INT NOT NULL,
                                    id_mobiliario INT NOT NULL,
                                    cantidad INT NOT NULL,
                                    precio_unitario DECIMAL(10,2) NOT NULL,
                                    subtotal DECIMAL(10,2) NOT NULL,
                                    PRIMARY KEY (id_reserva, id_mobiliario),
                                    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
                                    FOREIGN KEY (id_mobiliario) REFERENCES mobiliario(id_mobiliario)
);

-- Pagos
CREATE TABLE pagos (
                       id_pago INT AUTO_INCREMENT PRIMARY KEY,
                       id_reserva INT NOT NULL,
                       id_metodo_pago INT NOT NULL,
                       monto DECIMAL(12,2) NOT NULL,
                       estado ENUM('Pendiente','Pagado','Fallido') DEFAULT 'Pendiente',
                       codigo_confirmacion VARCHAR(50),
                       fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       comprobante_url VARCHAR(255),
                       FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
                       FOREIGN KEY (id_metodo_pago) REFERENCES metodos_pago(id_metodo_pago)
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

# -- Insertar usuarios (contraseñas encriptadas con BCrypt para "123456")
# INSERT INTO usuarios (nombre, apellido, email, celular, contrasena, id_rol) VALUES
#                                                                                 ('Admin', 'Sistema', 'admin@alquileventos.com', '999888777', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 1),
#                                                                                 ('Juan', 'Pérez', 'juan.perez@email.com', '987654321', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2),
#                                                                                 ('María', 'García', 'maria.garcia@email.com', '987654322', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2),
#                                                                                 ('Carlos', 'López', 'carlos.lopez@email.com', '987654323', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE.sJEpKoHjdEDUO2', 2);

-- Insertar locales
INSERT INTO locales (nombre_local, direccion, id_distrito, aforo_maximo, precio_hora, descripcion, estado) VALUES
                                                                                                               ('Salón Elegance', 'Av. Larco 1234, Miraflores', 1, 150, 250.00, 'Elegante salón con vista al mar, ideal para bodas y eventos especiales', 'DISPONIBLE'),
                                                                                                               ('Centro de Convenciones Lima', 'Av. Javier Prado 2456, San Isidro', 2, 300, 400.00, 'Moderno centro de convenciones equipado con tecnología de punta', 'DISPONIBLE'),
                                                                                                               ('Salón Barranco Club', 'Av. Grau 789, Barranco', 3, 80, 180.00, 'Acogedor salón en el corazón de Barranco, perfecto para eventos íntimos', 'DISPONIBLE'),
                                                                                                               ('Hacienda Los Jardines', 'Av. Benavides 3456, Surco', 4, 200, 320.00, 'Hermosa hacienda con amplios jardines y piscina', 'DISPONIBLE'),
                                                                                                               ('Salón Crystal', 'Av. La Molina 567, La Molina', 5, 120, 220.00, 'Salón moderno con decoración minimalista y excelente iluminación', 'DISPONIBLE');

-- Insertar relaciones local-tipo evento
INSERT INTO local_tipo_evento (id_local, id_tipo_evento) VALUES
                                                             (1, 1), (1, 4), (1, 7),
                                                             (2, 3), (2, 8),
                                                             (3, 2), (3, 5), (3, 6),
                                                             (4, 1), (4, 2), (4, 4), (4, 7),
                                                             (5, 1), (5, 2), (5, 3), (5, 8);

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

-- Insertar métodos de pago
INSERT INTO metodos_pago (nombre_metodo, descripcion) VALUES
                                                          ('Tarjeta', 'Pago con tarjeta de crédito o débito'),
                                                          ('Yape', 'Pago mediante aplicación Yape'),
                                                          ('Plin', 'Pago mediante aplicación Plin');

# -- Insertar reservas de ejemplo
# INSERT INTO reservas (id_usuario, id_local, id_tipo_evento, fecha, hora_inicio, hora_fin, cantidad_personas, costo_local, costo_mobiliario, costo_total, estado) VALUES
#                                                                                                                                                                      (2, 1, 1, '2024-12-15', '18:00:00', '23:00:00', 120, 1250.00, 580.00, 1830.00, 'Confirmada'),
#                                                                                                                                                                      (3, 3, 2, '2024-11-20', '15:00:00', '20:00:00', 60, 900.00, 560.00, 1460.00, 'Pendiente'),
#                                                                                                                                                                      (4, 2, 3, '2024-11-25', '09:00:00', '17:00:00', 200, 3200.00, 200.00, 3400.00, 'Confirmada');
#
# -- Insertar mobiliario para reservas (con subtotales)
# INSERT INTO reserva_mobiliario (id_reserva, id_mobiliario, cantidad, precio_unitario, subtotal) VALUES
#                                                                                                     (1, 1, 15, 25.00, 375.00),
#                                                                                                     (1, 3, 120, 8.00, 960.00),
#                                                                                                     (1, 5, 15, 12.00, 180.00),
#                                                                                                     (1, 7, 15, 35.00, 525.00),
#                                                                                                     (2, 2, 10, 20.00, 200.00),
#                                                                                                     (2, 4, 60, 8.00, 480.00),
#                                                                                                     (3, 8, 2, 80.00, 160.00),
#                                                                                                     (3, 9, 1, 120.00, 120.00);
#
# -- Insertar pagos
# INSERT INTO pagos (id_reserva, id_metodo_pago, monto, estado, comprobante_url) VALUES
#                                                                                    (1, 1, 1830.00, 'Pagado', '/comprobantes/pago-001.pdf'),
#                                                                                    (2, 2, 1460.00, 'Pendiente', NULL),
#                                                                                    (3, 3, 3400.00, 'Pagado', '/comprobantes/pago-003.pdf');
