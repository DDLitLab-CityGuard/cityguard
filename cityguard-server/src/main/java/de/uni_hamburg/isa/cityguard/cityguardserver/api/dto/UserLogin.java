package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;


import lombok.Data;

@Data
public class UserLogin {
	private String email;
	private String password;
}
