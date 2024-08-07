
CREATE TABLE cg_user
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    firstname   VARCHAR(255),
    lastname    VARCHAR(255),
    email       VARCHAR(255),
    password    VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT unique_username UNIQUE (email)
);


CREATE TABLE authentication_token
(
    id          BIGINT,
    cg_user_id     BIGINT,
    CONSTRAINT pk_authentication_token PRIMARY KEY (id)
);

ALTER TABLE authentication_token
    ADD CONSTRAINT FK_AUTHENTICATION_TOKEN_ON_USER FOREIGN KEY (cg_user_id) REFERENCES cg_user (id);