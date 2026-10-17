CREATE TABLE users
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    email          TEXT UNIQUE,
    email_verified BOOLEAN     NOT NULL DEFAULT FALSE,
    password_hash  TEXT        NOT NULL,
    display_name   TEXT,
    timezone       TEXT        NOT NULL DEFAULT 'UTC',
    apple_sub      TEXT UNIQUE,
    google_sub     TEXT UNIQUE,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);