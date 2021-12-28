CREATE TABLE users
(
    id VARCHAR(63) NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_bot BOOLEAN NOT NULL,
    adds_count INTEGER NOT NULL,
    edits_count INTEGER NOT NULL,
    CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE TABLE wikis
(
    id VARCHAR(63) NOT NULL,
    name VARCHAR(255) NOT NULL,
    edits_count INTEGER NOT NULL,
    CONSTRAINT pk_wikis_id PRIMARY KEY (id)
);

CREATE TABLE changes
(
    id VARCHAR(63) NOT NULL,
    "timestamp" TIMESTAMP NOT NULL,
    title VARCHAR(255) NOT NULL,
    comment TEXT NOT NULL,
    type VARCHAR(63) NOT NULL,
    user_id VARCHAR(63) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    wiki_id VARCHAR(63) NOT NULL REFERENCES wikis (id) ON UPDATE CASCADE,
    CONSTRAINT pk_changes_id PRIMARY KEY (id)
);

CREATE TABLE user_changes_intervals
(
    id VARCHAR(63) NOT NULL,
    start_timestamp TIMESTAMP NOT NULL,
    duration_in_minutes INTEGER NOT NULL,
    adds_count INTEGER NOT NULL,
    edits_count INTEGER NOT NULL,
    user_id VARCHAR(63) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    CONSTRAINT pk_user_changes_interval_id PRIMARY KEY (id)
);

CREATE TABLE user_wikis
(
    id VARCHAR(63) NOT NULL,
    changes_count INTEGER NOT NULL,
    wiki_name VARCHAR(255) NOT NULL, --TODO: remove and use join
    user_id VARCHAR(63) NOT NULL REFERENCES users (id) ON UPDATE CASCADE,
    wiki_id VARCHAR(63) NOT NULL REFERENCES wikis (id) ON UPDATE CASCADE,
    CONSTRAINT pk_user_wiki_id PRIMARY KEY (id)
);

