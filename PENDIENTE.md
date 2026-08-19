# Pendiente por implementar

Ya está listo: seguridad (HTTP Basic, sin endpoints públicos), usuarios/roles (ADMIN y SUPERVISOR)
con seed inicial, el CRUD completo de Camiones y Conductores (solo ADMIN puede crear/editar/eliminar,
cualquier usuario autenticado puede consultar), y el módulo de asociación Conductor ↔ Camión.

## 1. Módulo de asociación Conductor ↔ Camión — HECHO

Implementado con la opción simple: campo `conductor_id` en `camiones` (relación `@ManyToOne` a
`Conductor`), es decir, un conductor solo puede estar asociado a un camión a la vez. Si se asocia
un conductor que ya tenía otro camión asignado, se libera automáticamente de ese otro camión
(`CamionService.asociarConductor`).

- `POST /api/camiones/{camionId}/conductores/{conductorId}` — asocia (`hasAnyRole('ADMIN','SUPERVISOR')`).
- `DELETE /api/camiones/{camionId}/conductores` — desasocia (mismos roles).
- `GET`/`POST`/`PUT` de camión devuelven `conductorId`/`conductorNombre` (null si no tiene).
- Valida existencia de camión y conductor reusando `ResourceNotFoundException`.

No se implementó histórico de asociaciones (tabla `asociaciones` con fecha) — si se necesita
trazabilidad de cambios de asignación en el futuro, habría que migrar a esa alternativa.

## 2. Pruebas — HECHO

Tests de integración con MockMvc + H2 en memoria (no requieren PostgreSQL corriendo). Dependencia
`h2` agregada solo en scope `test`; `src/test/resources/application.properties` sobreescribe el
datasource para los tests.

- `SecurityIntegrationTest`: sin credenciales → 401, credenciales inválidas → 401, SUPERVISOR → 403
  al crear camión/conductor, ADMIN → 201 al crear ambos, usuario autenticado puede listar.
- `AsociacionConductorCamionIntegrationTest`: asociar (ADMIN/SUPERVISOR), 404 si camión o conductor
  no existen, reasociar a otro camión libera al primero, desasociar deja `conductorId` en null,
  sin credenciales → 401.

Ejecutar con `mvn test`.

## 3. Detalles menores

- Revisar si se necesitan más campos en `Conductor` (actualmente: nombre, documento, licencia).
- Evaluar agregar paginación en los `GET` de listado si se esperan muchos registros.
- Documentar (Swagger/OpenAPI) si se quiere facilitar las pruebas manuales — opcional, no pedido
  explícitamente en el enunciado.

## Notas de arranque

- Requiere PostgreSQL corriendo y la base `login_basico` creada (ver README).
- Usuarios de prueba creados automáticamente al primer arranque: `admin/admin123` (ADMIN) y
  `supervisor/supervisor123` (SUPERVISOR).
