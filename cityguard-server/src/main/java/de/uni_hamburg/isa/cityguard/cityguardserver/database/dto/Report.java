package de.uni_hamburg.isa.cityguard.cityguardserver.database.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *  Entity for the Report Table in the Database.
 *  It is a Spring Data JPA entity, so it will map the class to the Report Table.
 *  That means that the fields of this class will be columns of the Report Table.
 */
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
	@ManyToOne
	private CgUser user;
	private boolean spam;



	//TODO welche sind required??

}
