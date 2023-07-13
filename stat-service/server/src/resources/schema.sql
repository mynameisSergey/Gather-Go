DROP TABLE IF EXISTS stats cascade;
CREATE TABLE IF NOT EXISTS stats
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app       varchar(250),
    uri       varchar(250),
    ip        varchar(20),
    timestamp TIMESTAMP WITHOUT TIME ZONE
)