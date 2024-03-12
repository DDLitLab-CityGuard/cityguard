INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT true, 'violence',1,'red','gun', 100, 2.0, 5
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'violence');

INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT false, 'alcohol and drug misuse',30,'','', 100, 1.0, 3
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'alcohol and drug misuse');

INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT  true, 'sparse street lightning',30,'blue','sun', 100, 1.0, 2
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'sparse street lighting');

INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT  true, 'vandalism',30,'purple','spray can', 100, 1.0, 7
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'vandalism');

INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT  true, 'pollution',30,'green','trash can', 100, 1.0, 8
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'pollution');

INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius")
SELECT  false, 'large crowds',30,'','', 100, 1.0, 5