CREATE TABLE users
(
    id VARCHAR(25) NOT NULL,
    name VARCHAR(25) NOT NULL,
    is_bot BOOLEAN NOT NULL,
    CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE TABLE wikis
(
    id VARCHAR(25) NOT NULL,
    name VARCHAR(25) NOT NULL,
    CONSTRAINT pk_wikis_id PRIMARY KEY (id)
);

CREATE TABLE edits
(
    id VARCHAR(25) NOT NULL,
    "timestamp" TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    comment TEXT NOT NULL,
    editor_id VARCHAR(25) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    wiki_id VARCHAR(25) NOT NULL REFERENCES wikis (id) ON UPDATE CASCADE,
    CONSTRAINT pk_edits_id PRIMARY KEY (id)
);