INSERT INTO category (allow_discrete,"name","color","icon")
SELECT true, 'violence','red','gun'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'violence');


INSERT INTO category (allow_discrete,"name","color","icon")
SELECT false, 'alcohol and drug misuse','',''
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'alcohol and drug misuse');

INSERT INTO category (allow_discrete,"name","color","icon")
SELECT  true, 'sparse street lightning','blue','sun'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'sparse street lighting');

INSERT INTO category (allow_discrete,"name","color","icon")
SELECT  true, 'vandalism','purple','spray can'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'vandalism');

INSERT INTO category (allow_discrete,"name","color","icon")
SELECT  true, 'pollution','green','trash can'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'pollution');

INSERT INTO category (allow_discrete,"name","color","icon")
SELECT  false, 'large crowds','',''
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'large crowds');