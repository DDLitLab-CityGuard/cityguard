CREATE TABLE report
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    longitude   FLOAT,
    latitude    FLOAT,
    description VARCHAR(255),
    category_id BIGINT,
    date_time   TIMESTAMP WITHOUT TIME ZONE,
    spam        BOOLEAN,
    CONSTRAINT pk_report PRIMARY KEY (id)
);


ALTER TABLE report
    ADD CONSTRAINT FK_REPORT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

