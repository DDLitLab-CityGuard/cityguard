INSERT INTO category (allow_discrete,"name")
SELECT true, 'marker'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'marker');

INSERT INTO category (allow_discrete,"name")
SELECT false, 'heatmap'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'heatmap');
