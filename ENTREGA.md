# ENTREGA — Hackathon UTEC 2026-2

## Resumen del reto
Backend Spring Boot del motor de reglas narrativo de Bandersnatch. Un cliente crea nodos (StoryNode), inicia partidas (Playthrough), registra decisiones (Decision) con clasificacion automatica y recibe notificaciones por correo.

## Stack
Java 21 - Spring Boot 3.3.5 - Maven - PostgreSQL 16 - Spring Security + JWT - Spring Mail - Spring Data JPA.

## Estrellas cubiertas
- Estrella 1 Seguridad: JWT con roles, BCrypt, refresh tokens, register/login/refresh/logout, /users/me, /users admin, ownership.
- Estrella 2 Nodos: CRUD /api/v1/nodes, validaciones (nodeKey unico 409, BRANCH requiere opciones 400, delete con partidas 409).
- Estrella 3 Partidas: POST/GET /api/v1/playthroughs, /{id} con ownership 403 y admin.
- Estrella 4 Decisiones: clasificacion por tabla del PDF, chosenOptionCode en BRANCH, timeout -5, ENDING cierra, ENDING_CORRUPT cierra y -10, historial, Idempotency-Key.
- Estrella 5 Asincronia: AFTER_COMMIT + @Async + REQUIRES_NEW, RealityLog, correo SMTP, log [BRANCH-DEC].

## Flujo asincrono
1. Cliente llama POST /api/v1/playthroughs/{id}/decisions con Idempotency-Key.
2. DecisionService.decide() en @Transactional: guarda Decision, actualiza Playthrough.
3. Al commit se publica DecisionEvent.
4. RealityLogListener (@Async + REQUIRES_NEW): inserta RealityLog, envia correo, loguea [BRANCH-DEC].
5. Respuesta HTTP vuelve antes; el cliente no espera al correo.

## Pendientes
- Envio real de correo no probado con SMTP real.
- Idempotencia no cachea la respuesta original.
- Sin paginacion en listas.
- Frontend JavaFX fuera de scope.

## Equipo
- Luis Huapaya - 202220394
- Matias Caceres - 202510370
