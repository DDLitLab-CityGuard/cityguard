package com.example.cityguardserver.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.PatternSyntaxException;

import static java.lang.Float.parseFloat;


@Getter
@Setter
@Slf4j
public class ReportForm {

    private float latitude;
    private float longitude;

    @JsonProperty("date")
    private LocalDate reportedDate; //ISO 8601

    @JsonProperty("time")
    private LocalTime reportedTime; //ISO 8601

    @JsonProperty("desc")
    @Size(max = 255)
    private String description;

    @JsonProperty("currentDatetime")
    private Boolean useCurrentDateTime = false;

    @JsonProperty("currentLocation")
    private Boolean useCurrentLocation = false;

    @JsonProperty("category")
    @NotNull
    private String category;
    @JsonProperty("location")
    private void splitCoordinates(@NotNull String coordinates) throws NullPointerException, NumberFormatException, PatternSyntaxException{
        try {
            String[] latLongArray = coordinates.split(",");
            this.latitude = parseFloat(latLongArray[0]);
            this.longitude = parseFloat(latLongArray[1]);
        }
        catch (NullPointerException | NumberFormatException | PatternSyntaxException e){
            throw new NumberFormatException("Die eingegeben Koordinaten sind nicht im richtigen Format");
        }
    }
}