---
name: routex-ddd-style
description: Use when refactoring or implementing Java Spring services in the Routex style: DDD level-3-ish, ports and adapters, JPA isolated in infrastructure, strict package naming, and consistent mapper/repository conventions.
---

# Routex DDD Style

Apply this skill when working on a Java Spring Boot service that should follow the same structure and naming conventions as the Routex management service.

## Goals

- Keep business logic centered on domain concepts, not controller or JPA classes.
- Keep dependency direction one-way: `interfaces -> application -> domain`, with `infrastructure` implementing ports.
- Isolate Spring Data JPA and persistence entities under `infrastructure.persistence.jpa.*`.
- Keep naming and package layout consistent so Codex can refactor safely across bounded contexts.

## Required Architecture

Use this package direction:

- `interfaces.controller`
  HTTP adapters only.
- `interfaces.models`
  Request/response DTOs only.
- `application.services`
  Use-case orchestration only.
- `application.dto.<context>`
  Commands, queries, and result DTOs for application layer.
- `domain.<context>.model`
  Domain models, aggregates, value-like records.
- `domain.<context>.port`
  Repository/service ports used by application.
- `domain.<context>.readmodel`
  Read models returned by query ports.
- `infrastructure.persistence.adapter.<context>`
  Adapter implementations for ports and mapping glue.
- `infrastructure.persistence.jpa.<context>.entity`
  JPA entities only.
- `infrastructure.persistence.jpa.<context>.repository`
  Spring Data JPA repositories only.
- `infrastructure.kafka.adapter`
  Messaging adapters implementing domain/application ports.

Never let `domain` depend on `interfaces`, controller DTOs, Spring MVC, or Kafka classes.

## Layer Rules

### Interfaces

- Controllers receive `BaseRequest<...>` and return `BaseResponse<...>` if the repo already uses that pattern.
- Controllers map HTTP DTOs to application commands/queries.
- Controllers do not hold business logic.

### Application

- Service interfaces and implementations use application command/query/result classes, not HTTP DTOs.
- Application services orchestrate:
  - load domain data through ports
  - call domain logic
  - save through ports
  - publish events through ports
- Application services may throw business exceptions, but should not know HTTP response shapes.

### Domain

- Domain contains aggregate-like models and business invariants.
- Domain ports define what application needs from persistence or external systems.
- Keep domain enums in domain if they represent business state.
- Do not place JPA repositories or JPA entities in domain.

### Infrastructure

- JPA entity classes live only under `infrastructure.persistence.jpa.<context>.entity`.
- Spring Data repository interfaces live only under `infrastructure.persistence.jpa.<context>.repository`.
- Adapter classes implement domain ports and map domain models to JPA entities.
- Kafka publishers should be adapters behind a port, not called directly from application service.

## Naming Conventions

### JPA classes

- Entity: `<Name>JpaEntity`
- Spring Data repository: `<Name>JpaRepository`
- Projection: `<Name>Projection`

Examples:

- `RouteJpaEntity`
- `RouteAssignmentJpaRepository`
- `LocationCodeProjection`

### Domain classes

- Aggregate-like domain object: meaningful business name such as `RouteAggregate`, `RoleAggregate`, `VehicleProfile`
- Query read model: `<Name>View` or `<Name>Item`
- Port: `<Name>Port` or `<Name>RepositoryPort`

### Adapter fields

If a field holds a Spring Data JPA repository, name it with `JpaRepository` suffix in the variable too:

- `routeJpaRepository`
- `vehicleJpaRepository`
- `locationJpaRepository`
- `roleJpaRepository`
- `authorityJpaRepository`
- `userRoleJpaRepository`

Do not use mixed naming like `rolesRepository`, `authoritiesRepository`, `vehicleRepository` when the type is a JPA repository.

### Mapper classes

- Prefer one mapper per aggregate or concern.
- Avoid a single large mapper for an entire bounded context once the context grows.
- Good names:
  - `RoutePersistenceMapper`
  - `PermissionPersistenceMapper`
  - `RolePersistenceMapper`
  - `UserRolePersistenceMapper`
  - `UserAccountPersistenceMapper`

## Refactor Rules

When Codex refactors an existing repo to this style:

1. Move JPA entities out of `domain` into `infrastructure.persistence.jpa.<context>.entity`.
2. Move Spring Data repositories out of `domain` into `infrastructure.persistence.jpa.<context>.repository`.
3. Update application services to depend on domain ports, not JPA repositories.
4. Add adapter classes in `infrastructure.persistence.adapter.<context>` to implement those ports.
5. Replace HTTP DTO usage inside application layer with application command/query/result DTOs.
6. Remove facade layers if they are just HTTP mapping disguised as application logic.
7. Split large persistence mappers when they cover multiple aggregates.
8. Delete legacy classes only after verifying they have no references.

## Safety Rules

- Before deleting a legacy class, verify with search that it has no usages.
- Do not move business enums out of domain unless they are purely persistence concerns.
- Keep imports explicit. If using `ArrayList`, `List`, `Set`, import them instead of using fully-qualified names inline.
- Use `apply_patch` for file content edits. If a move operation is flaky in patch tooling, move the file with shell and then patch package/import declarations.
- Compile with Java 21 after structural refactors.

## Preferred Verification

After significant refactors, run:

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn -q -DskipTests compile
```

If compile fails, fix package/import/class-name mismatches before changing business logic.

## Decision Heuristics

- If a class is used only for HTTP transport, it belongs in `interfaces.models`.
- If a class exists only to talk to the database, it belongs in `infrastructure.persistence.jpa.*`.
- If a class expresses business state or business transitions, it belongs in `domain`.
- If a class only translates between domain and persistence/transport, it is a mapper or adapter, not domain.

## Output Expectations

When applying this style, Codex should:

- preserve existing business behavior unless explicitly changing it
- prefer small, safe architectural steps
- keep package naming consistent across all bounded contexts
- mention compile verification result
- call out any remaining non-DDD areas explicitly instead of silently leaving them inconsistent
