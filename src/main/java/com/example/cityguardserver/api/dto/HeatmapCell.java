package com.example.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeatmapCell {
    private float latitude;
    private float longitude;
    private float sizeLat;
    private float sizeLon;
    private float value;
}
