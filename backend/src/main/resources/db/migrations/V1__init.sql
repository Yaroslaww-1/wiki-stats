CREATE TABLE users
(
    id VARCHAR(63) NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_bot BOOLEAN NOT NULL,
    CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE TABLE wikis
(
    id VARCHAR(63) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_wikis_id PRIMARY KEY (id)
);

CREATE TABLE edits
(
    id VARCHAR(63) NOT NULL,
    "timestamp" TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    comment TEXT NOT NULL,
    type VARCHAR(63) NOT NULL,
    editor_id VARCHAR(63) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    wiki_id VARCHAR(63) NOT NULL REFERENCES wikis (id) ON UPDATE CASCADE,
    CONSTRAINT pk_edits_id PRIMARY KEY (id)
);

CREATE TABLE user_edit_stats
(
    id VARCHAR(63) NOT NULL,
    start_timestamp TIMESTAMP NOT NULL,
    duration_in_minutes INTEGER NOT NULL,
    add_count INTEGER NOT NULL,
    edit_count INTEGER NOT NULL,
    user_id VARCHAR(63) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    CONSTRAINT pk_user_edit_stats_id PRIMARY KEY (id)
);