package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserLogin;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserRegisterForm;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.NutzerRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Nutzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	private final NutzerRepository nutzerRepository;

	public AuthController(NutzerRepository nutzerRepository) {
		this.nutzerRepository = nutzerRepository;

	}

	@PostMapping("/singin")
	public ResponseEntity<String> authenticateUser(@ModelAttribute UserLogin loginDto){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
	}


	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@ModelAttribute UserRegisterForm registerDto){
		System.out.println("registering user");
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Nutzer user=new Nutzer();
		String encodedpassword=passwordEncoder.encode(registerDto.getPassword());
		user.setPassword(encodedpassword);
		user.setUsername(registerDto.getUsername());
		user.setFirstname(registerDto.getFirstname());
		user.setLastname(registerDto.getLastname());
		user.setEmail(registerDto.getEmail());
		nutzerRepository.save(user);

		return new ResponseEntity<>("User created successfully!.", HttpStatus.OK);

	}

}
