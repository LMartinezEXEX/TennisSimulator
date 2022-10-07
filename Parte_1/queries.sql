-- Lenguaje SQLite

-- Todos los productos del rubro 'libreria', creados hoy.
SELECT p.*
FROM producto as p 
INNER JOIN rubro as r ON p.id_rubro=r.id_rubro 
WHERE rubro='libreria' and fecha_creacion=CURRENT_DATE;

-- Monto total vendido por cliente.
SELECT c.nombre as 'Nombre', IFNULL(SUM(v.precio_unitario * v.cantidad), 0) as 'Venta total'
FROM cliente as c
LEFT JOIN venta as v ON c.id_cliente=v.id_cliente
GROUP BY c.id_cliente;

-- Cantidad de ventas por producto
SELECT p.nombre as 'Nombre', COUNT(v.id_venta) as 'Cantidad de ventas'
FROM producto as p
LEFT JOIN venta as v ON p.codigo=v.codigo_producto
GROUP BY p.codigo;

-- Cantidad de productos comprados por cliente en el mes actual.
SELECT c.nombre as 'Nombre', SUM(IFNULL(cantidad, 0)) as 'Cantidad de productos comprados'
FROM cliente as c
LEFT JOIN venta as v ON c.id_cliente=v.id_cliente
WHERE strftime('%m', v.fecha)=strftime('%m', CURRENT_DATE)
GROUP By c.id_cliente;

-- Ventas que tienen al menos un producto del rubro 'bazar'.
SELECT v.*
FROM venta as v
INNER JOIN (
  SELECT *
  FROM producto as p
  INNER JOIN rubro as r ON p.id_rubro=r.id_rubro
  WHERE r.rubro='bazar'
) as prod_bazar ON v.codigo_producto=prod_bazar.codigo
WHERE v.cantidad>0;

-- Rubros que no tienen ventas en los ultimos dos meses
SELECT r.*
FROM rubro as r
WHERE r.id_rubro not in (
  SELECT p.id_rubro
  FROM producto as p
  INNER JOIN venta as v ON p.codigo=v.codigo_producto
  WHERE strftime('%m', CURRENT_DATE)-2 < strftime('%m', v.fecha) 
  GROUP BY p.codigo
);
