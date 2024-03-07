INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT true, 'violence','red','gun', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'violence');

INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT false, 'alcohol and drug misuse','','', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'alcohol and drug misuse');

INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT  true, 'sparse street lightning','blue','sun', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'sparse street lighting');

INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT  true, 'vandalism','purple','spray can', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'vandalism');

INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT  true, 'pollution','green','trash can', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'pollution');

INSERT INTO category (allow_discrete,"name","color","icon","aggregation_radius_meters", "minimum_score")
SELECT  false, 'large crowds','','', 100, 1.0
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'large crowds');