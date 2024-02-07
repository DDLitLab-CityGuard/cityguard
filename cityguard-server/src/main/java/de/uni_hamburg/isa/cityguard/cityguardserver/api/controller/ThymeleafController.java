package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
	@GetMapping("/welcome")
	public String hello(Model model) {
		model.addAttribute("message", "Hello, Thymeleaf!");
		return "welcome";
	}

@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("message", "Hello, Thymeleaf!");
		return "index";
	}




}
