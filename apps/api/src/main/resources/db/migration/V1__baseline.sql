-- Baseline migration so Flyway owns the schema from the first deploy.
-- Tenants, users, memberships and row-level security arrive in S0-5 (V2__...).
CREATE TABLE app_meta (
    key        text PRIMARY KEY,
    value      text NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT now()
);

INSERT INTO app_meta (key, value) VALUES ('schema_baseline', 'S0-1');
