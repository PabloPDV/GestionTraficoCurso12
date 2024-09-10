-- Inserts para TipoVia
INSERT INTO TipoVia (tipo, codigo, abreviatura, descripcion) VALUES 
('Calle', 'CL', 'C.', 'Una calle común'),
('Avenida', 'AV', 'Av.', 'Una avenida principal'),
('Boulevard', 'BL', 'Blvd.', 'Un boulevard amplio'),
('Carretera', 'CR', 'Cr.', 'Una carretera interurbana'),
('Autopista', 'AU', 'Aut.', 'Una autopista de alta velocidad'),
('Camino', 'CM', 'Cm.', 'Un camino rural'),
('Pasaje', 'PS', 'Ps.', 'Un pasaje peatonal'),
('Ruta', 'RT', 'Rt.', 'Una ruta nacional');

-- Inserts adicionales para Via
INSERT INTO Via (nombre, longitud, ancho, capacidad, estado, congestion, tipo_id) VALUES 
('Carretera Nacional 1', 50.0, 20.0, 5000, 'Abierta', 'BAJO', 4),
('Autopista del Sol', 100.0, 25.0, 10000, 'Abierta', 'MEDIO', 5),
('Camino de Tierra', 5.0, 3.0, 50, 'Abierta', 'BAJO', 6),
('Pasaje Secreto', 0.5, 2.0, 20, 'Cerrada', 'ALTO', 7),
('Ruta 66', 200.0, 30.0, 15000, 'Abierta', 'MEDIO', 8),
('Calle de la Amargura', 1.0, 4.0, 80, 'Abierta', 'BAJO', 1),
('Avenida de la Paz', 3.0, 12.0, 250, 'En mantenimiento', 'MEDIO', 2),
('Boulevard del Río', 2.5, 14.0, 220, 'Cerrada', 'ALTO', 3),
('Carretera del Norte', 60.0, 18.0, 6000, 'Abierta', 'BAJO', 4),
('Autopista del Este', 120.0, 28.0, 12000, 'Abierta', 'MEDIO', 5),
('Camino Viejo', 4.0, 3.5, 45, 'Abierta', 'BAJO', 6),
('Pasaje de los Olivos', 0.7, 2.5, 25, 'Cerrada', 'ALTO', 7),
('Ruta 40', 180.0, 32.0, 14000, 'Abierta', 'MEDIO', 8);