-- Inserts para TipoVia
INSERT INTO TipoVia (tipo, codigo, abreviatura, descripcion) VALUES 
('Calle', 'CL', 'C.', 'Una calle común'),
('Avenida', 'AV', 'Av.', 'Una avenida principal'),
('Boulevard', 'BL', 'Blvd.', 'Un boulevard amplio');

-- Inserts para Via
INSERT INTO Via (nombre, longitud, ancho, capacidad, estado, congestion, tipo_id) VALUES 
('Calle Falsa 123', 1.2, 5.0, 100, 'Abierta', 'BAJO', 1),
('Avenida Siempre Viva', 2.5, 10.0, 200, 'En mantenimiento', 'MEDIO', 2),
('Boulevard de los Sueños Rotos', 3.0, 15.0, 300, 'Cerrada', 'ALTO', 3);