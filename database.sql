CREATE TABLE notes
(
    id         UUID         NOT NULL PRIMARY KEY,
    title      varchar(100) NOT NULL,
    body       TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE tags
(
    id       UUID        NOT NULL PRIMARY KEY,
    body     VARCHAR(24) NOT NULL,
    notes_id UUID        NOT NULL REFERENCES notes (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX unique_tags_body_notes_id ON tags (body, notes_id);
