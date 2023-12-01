package com.example.cityguardserver.database.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 *  Entity for the Category Table in the Database.
 *  It is a Spring Data JPA entity, so it will map the class to the Category Table.
 *  That means that the fields of this class will be columns of the Category Table.
 */
@Setter
@Getter
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private  String name;
	private Boolean allowDiscrete;

}