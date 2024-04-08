INSERT INTO category (allow_discrete,"name","spam_detection_frequency_in_minutes","color","icon","aggregation_radius_meters", "minimum_score", "heatmap_spread_radius", "spam_alert_threshold")
SELECT true, 'CityGuard Demo',1,'red','gun', 100, 2.0, 5, 5
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE "name" = 'CityGuard Demo');