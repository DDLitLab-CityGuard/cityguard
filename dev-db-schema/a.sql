CREATE TABLE category
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name           VARCHAR(255),
    allow_discrete BOOLEAN,
    color   VARCHAR(255),
    icon    VARCHAR(255),
    aggregation_radius_meters BIGINT,
    minimum_score DOUBLE PRECISION,
    spam_detection_frequency_in_minutes BIGINT,
    heatmap_spread_radius BIGINT,
    CONSTRAINT pk_category PRIMARY KEY (id)
);