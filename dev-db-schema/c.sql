INSERT INTO category (allow_discrete,"name")
SELECT true, 'marker1'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'marker1');

INSERT INTO category (allow_discrete,"name")
SELECT true, 'marker2'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'marker2');

INSERT INTO category (allow_discrete,"name")
SELECT true, 'marker3'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'marker3');

INSERT INTO category (allow_discrete,"name")
SELECT false, 'heatmap'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'heatmap');
