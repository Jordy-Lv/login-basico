# Pendiente por implementar

Ya está listo: seguridad (HTTP Basic, sin endpoints públicos), usuarios/roles (ADMIN y SUPERVISOR)
con seed inicial, y el CRUD completo de Camiones y Conductores (solo ADMIN puede crear/editar/eliminar,
cualquier usuario autenticado puede consultar).

Falta lo siguiente:

## 1. Módulo de asociación Conductor ↔ Camión (lo más importante)

Requisito original: "el supervisor solo podrá asociar conductores a camiones" (y el ADMIN también
debería poder hacerlo).

Sugerencia de diseño (a definir por quien lo implemente):

- Decidir la relación: ¿un conductor asignado a un solo camión a la vez (relación simple con un campo
  `conductor_id` en `camiones`), o histórico de asociaciones (tabla intermedia `asociaciones` con
  fecha de asignación)? Para el alcance de esta actividad probablemente basta con la opción simple.
- Si se opta por tabla intermedia, crear entidad `Asociacion` (`camion`, `conductor`, `fechaAsociacion`)
  y su repositorio.
- Endpoint sugerido: `POST /api/camiones/{camionId}/conductores/{conductorId}` (o
  `POST /api/asociaciones` con body `{camionId, conductorId}`).
- Seguridad: permitir tanto `ADMIN` como `SUPERVISOR` (`@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")`),
  a diferencia de los endpoints de creación de camiones/conductores que son solo ADMIN.
- Validar que el camión y el conductor existan (reusar `ResourceNotFoundException`).
- Definir si un conductor puede estar asociado a más de un camión a la vez, y manejar ese caso
  (regla de negocio a confirmar).
- Endpoint para desasociar / consultar la asociación actual de un camión (opcional pero recomendable).

## 2. Pruebas

- No hay tests todavía. Agregar al menos:
  - Tests de integración de seguridad (verificar que sin credenciales todo responde 401, que
    SUPERVISOR recibe 403 al intentar crear un camión, etc.).
  - Tests del módulo de asociación una vez implementado.

## 3. Detalles menores

- Revisar si se necesitan más campos en `Conductor` (actualmente: nombre, documento, licencia).
- Evaluar agregar paginación en los `GET` de listado si se esperan muchos registros.
- Documentar (Swagger/OpenAPI) si se quiere facilitar las pruebas manuales — opcional, no pedido
  explícitamente en el enunciado.

## Notas de arranque

- Requiere PostgreSQL corriendo y la base `login_basico` creada (ver README).
- Usuarios de prueba creados automáticamente al primer arranque: `admin/admin123` (ADMIN) y
  `supervisor/supervisor123` (SUPERVISOR).
