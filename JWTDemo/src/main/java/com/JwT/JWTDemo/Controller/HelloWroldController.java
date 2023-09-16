package com.JwT.JWTDemo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWroldController {
	
	
	@RequestMapping("/hello")
	public String firstPage() {
		return "Hello World";
	}

}
