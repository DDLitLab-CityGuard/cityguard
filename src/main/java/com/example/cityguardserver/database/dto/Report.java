package com.example.cityguardserver.database.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@ToString
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float longitude;
    private Float latitude;
    private String description;
    private String category;
    private LocalDateTime dateTime;
    private Boolean heatmap;

    //TODO welche sind required??

}


