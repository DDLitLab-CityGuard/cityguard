package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserLogin;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserRegisterForm;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.AuthenticationRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.UserRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.AuthenticationToken;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;

import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;
	private final Random random;

	public AuthController(UserRepository nutzerRepository, AuthenticationRepository authenticationRepository) {
		this.userRepository = nutzerRepository;
		this.authenticationRepository = authenticationRepository;
		this.random = new Random();
	}

	@PostMapping("/login")
	public String authenticateUser(@ModelAttribute UserLogin loginDto, HttpSession session) {
		String email = loginDto.getUsernameOrEmail();
		String password = loginDto.getPassword();
		Optional<CgUser> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			if (BCrypt.checkpw(password, user.get().getPassword())) {
				//create token
				AuthenticationToken token = new AuthenticationToken();
				token.setId(random.nextLong());
				LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
				token.setExpirationDate(expirationDate);
				token.setCgUser(user.get());
				authenticationRepository.save(token);
				session.setAttribute("token", token.getId());
				return "redirect:/index";
			}
		}
		return "/login";
	}

	@PostMapping("/welcome")
	public ResponseEntity<String> registerUser(@ModelAttribute UserRegisterForm registerDto){
		System.out.println("registering user");
		CgUser cgUser =new CgUser();
		String encodedPassword = BCrypt.hashpw(registerDto.getPassword(), BCrypt.gensalt(6));
		cgUser.setPassword(encodedPassword);
		cgUser.setUsername(registerDto.getUsername());
		cgUser.setFirstname(registerDto.getFirstname());
		cgUser.setLastname(registerDto.getLastname());
		cgUser.setEmail(registerDto.getEmail());
		userRepository.save(cgUser);

		return new ResponseEntity<>("User created successfully!.", HttpStatus.OK);

	}




	@GetMapping("/login")
	public String login() {

		return "login";
	}


	@GetMapping("/welcome")
	public String hello() {

		return "welcome";
	}


}
