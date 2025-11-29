CREATE TABLE users (
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    nickname  VARCHAR(50),
    avatar    VARCHAR(255),
    role      SMALLINT     NOT NULL DEFAULT 0
);