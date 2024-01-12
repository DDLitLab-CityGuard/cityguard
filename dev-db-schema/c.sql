INSERT INTO category (allow_discrete,"name","marker_type")
SELECT true, 'violence','redMarker'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'violence');


INSERT INTO category (allow_discrete,"name","marker_type")
SELECT false, 'alcohol and drug misuse',''
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'alcohol and drug misuse');

INSERT INTO category (allow_discrete,"name","marker_type")
SELECT  true, 'sparse street lightning','blueMarker'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'sparse street lighting');

INSERT INTO category (allow_discrete,"name","marker_type")
SELECT  true, 'vandalism','purpleMarker'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'vandalism');

INSERT INTO category (allow_discrete,"name","marker_type")
SELECT  true, 'pollution','greenMarker'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'pollution');

INSERT INTO category (allow_discrete,"name","marker_type")
SELECT  false, 'large crowds',''
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'large crowds');