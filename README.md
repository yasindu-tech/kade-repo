# Kadé

Billing, order tracking and a service board for Sri Lankan small businesses: **Bill. Share. Get paid.**

This is a monorepo with two apps and a local dev stack:

```
apps/
  api/        Spring Boot 4 API (Java 21, Gradle, PostgreSQL, Flyway)
  web/        Next.js 16 app (TypeScript, App Router, Tailwind CSS)
compose.yaml  PostgreSQL 16 + Gotenberg (HTML-to-PDF) for local development
.github/      CI workflow and pull-request template
```

## Prerequisites

| Tool           | Version            | Check                    |
| -------------- | ------------------ | ------------------------ |
| Java (JDK)     | 21                 | `java -version`          |
| Node.js        | 24 LTS (22+ works) | `node -v`                |
| Docker Desktop | any recent         | `docker compose version` |

Gradle is not needed; use the wrapper (`./gradlew`).

## Quick start

```bash
# 1. Start PostgreSQL and Gotenberg
cp .env.example .env          # optional: only if you need to change ports or passwords
docker compose up -d

# 2. Run the API on http://localhost:8080
cd apps/api
./gradlew bootRun

# 3. Run the web app on http://localhost:3000 (new terminal)
cd apps/web
npm install
npm run dev
```

Check the API: `curl localhost:8080/api/v1/meta` and `curl localhost:8080/actuator/health`.

Flyway migrates the database when the API starts. Migrations live in `apps/api/src/main/resources/db/migration`; never edit one that has been merged, add a new `V<n>__description.sql` instead.

To stop the stack: `docker compose down` (add `-v` to wipe the local database).

## Everyday commands

| Where      | Command                   | What it does                                                                                                    |
| ---------- | ------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `apps/api` | `./gradlew build`         | Compile, then run unit and integration tests (Testcontainers starts its own PostgreSQL; Docker must be running) |
| `apps/api` | `./gradlew spotlessApply` | Format Java code (google-java-format)                                                                           |
| `apps/api` | `./gradlew bootRun`       | Run the API against the Compose database                                                                        |
| `apps/web` | `npm run dev`             | Dev server with hot reload                                                                                      |
| `apps/web` | `npm run lint`            | ESLint                                                                                                          |
| `apps/web` | `npm run format`          | Format with Prettier                                                                                            |
| `apps/web` | `npm run typecheck`       | TypeScript check                                                                                                |
| `apps/web` | `npm run build`           | Production build                                                                                                |

## Continuous integration

`.github/workflows/ci.yml` runs on every pull request and on pushes to `main` and `develop`:

- **API**: format check, build, unit and Testcontainers integration tests
- **Web**: lint, format check, type check, production build
- **Dev stack**: validates `compose.yaml`

To make a red build block merging, turn on branch protection for `develop` and `main` in GitHub (Settings → Branches → Add rule): require a pull request, and require the status checks **API (Spring Boot)**, **Web (Next.js)** and **Dev stack config**.

## Working agreement

- Branch from `develop` as `feature/<story-id>-short-name`, e.g. `feature/S0-5-schema-v1`, and open the pull request into `develop`.
- `main` holds released code; `develop` is merged into `main` for each release.
- One story per pull request; the PR template lists the Definition of Done.
- Integration tests run against real PostgreSQL, never an in-memory database, so tenant isolation and SQL behave exactly as in production.
- Times are stored in UTC and shown in Asia/Colombo time; money is stored as integer cents (LKR).
