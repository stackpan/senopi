CREATE TABLE users
(
    id       UUID        NOT NULL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password TEXT        NOT NULL,
    fullname VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX unique_users_username ON users (username);

CREATE TABLE authentications
(
    token VARCHAR(1024) NOT NULL PRIMARY KEY
);

CREATE TABLE notes
(
    id         UUID         NOT NULL PRIMARY KEY,
    title      varchar(100) NOT NULL,
    body       TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    owner      UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE tags
(
    id       UUID        NOT NULL PRIMARY KEY,
    body     VARCHAR(24) NOT NULL,
    notes_id UUID        NOT NULL REFERENCES notes (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX unique_tags_body_notes_id ON tags (body, notes_id);