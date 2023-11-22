package com.example.cityguardserver.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

import static java.lang.Float.parseFloat;


@Getter
@Setter
@ToString
public class ReportForm {

    private float latitude;
    private float longitude;

    private float measured_latitude;
    private float measured_longitude;

    @JsonProperty("date")
    private LocalDate reportedDate; //ISO 8601

    @JsonProperty("time")
    private LocalTime reportedTime; //ISO 8601

    @JsonProperty("desc")
    @Size(max = 255)
    private String description;

   
    private Boolean useCurrentDateTime = false;

    private Boolean useCurrentLocation = false;

    @JsonProperty("category")
    @NotNull
    @ManyToOne
    private Long categoryId;

    @JsonProperty("location")
    private void splitCoordinates(String coordinates) throws NullPointerException, NumberFormatException, PatternSyntaxException{
        try {
            String[] latLongArray = coordinates.split(",");
            this.latitude = parseFloat(latLongArray[0]);
            this.longitude = parseFloat(latLongArray[1]);
        }
        catch (NullPointerException | NumberFormatException | PatternSyntaxException e){
            throw new NumberFormatException("Die eingegeben Koordinaten sind nicht im richtigen Format");
        }
    }

    @JsonProperty("location_hidden")
    private void splitCoordinatesHidden(String coordinates) throws NullPointerException, NumberFormatException, PatternSyntaxException{
        try {
            String[] latLongArray = coordinates.split(",");
            this.measured_latitude = parseFloat(latLongArray[0].strip());
            this.measured_longitude = parseFloat(latLongArray[1].strip());
        }
        catch (NullPointerException | NumberFormatException | PatternSyntaxException e){
            throw new NumberFormatException("Die eingegeben Koordinaten sind nicht im richtigen Format");
        }
    }

    @JsonProperty("currentDateTime")
    public void setUseCurrentDateTime(Boolean useCurrentDateTime) {
        this.useCurrentDateTime = Objects.requireNonNullElse(useCurrentDateTime, true);
    }

    @JsonProperty("currentLocation")
    public void setUseCurrentLocation(Boolean useCurrentLocation) {
        this.useCurrentLocation = Objects.requireNonNullElse(useCurrentLocation, true);
    }
}