package de.uni_hamburg.isa.cityguard.cityguardserver.database.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CgUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true)
	private String email;
	private String password;
	private String firstname;
	private String lastname;


}
