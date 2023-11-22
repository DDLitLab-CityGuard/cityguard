package com.example.cityguardserver.database.dto;

import jakarta.persistence.*;
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
    @ManyToOne
    private Category category;
    private LocalDateTime dateTime;



    //TODO welche sind required??

}


