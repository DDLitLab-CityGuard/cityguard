package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
	@GetMapping("/welcome")
	public String hello(Model model) {

		return "welcome";
	}

@GetMapping("/index")
	public String index(HttpSession session) {
		if (session.getAttribute("token") != null) {
			System.out.println("Token: " + session.getAttribute("token"));
		}
		return "index";
	}




}
