package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;

		import lombok.Getter;
		import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String password;



}
