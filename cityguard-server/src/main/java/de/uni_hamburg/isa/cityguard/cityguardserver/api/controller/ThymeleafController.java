package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;

import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserLogin;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.UserRegisterForm;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.AuthenticationRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.UserRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.AuthenticationToken;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Controller
@AllArgsConstructor
public class ThymeleafController {

	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;
	private final Random random = new Random();

	@GetMapping("/welcome")
	public String hello() {
		return "welcome";
	}

	@GetMapping("/index")
	public String index(HttpSession session) {
		if (session.getAttribute("token") != null) {
			System.out.println("Token: " + session.getAttribute("token"));
		}
		return "index";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@PostMapping("/login")
	public String authenticateUser(@ModelAttribute UserLogin loginDto, HttpSession session) {
		String email = loginDto.getEmail();
		System.out.println("email: " + email);
		String password = loginDto.getPassword();
		Optional<CgUser> user = userRepository.findByEmail(email);
		for (CgUser cgUser : userRepository.findAll()) {
			System.out.println(cgUser.getEmail() + " " + cgUser.getPassword());
		}

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


}