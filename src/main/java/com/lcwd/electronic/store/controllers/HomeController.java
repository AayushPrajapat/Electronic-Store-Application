package com.lcwd.electronic.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeController {

	@GetMapping("/home")
	public String home() {
		return"Welcome to Electronic Store Application...";
	}
}
