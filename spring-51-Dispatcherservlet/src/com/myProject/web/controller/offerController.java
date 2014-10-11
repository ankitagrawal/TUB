package com.myProject.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class offerController {
	
	@RequestMapping("/")
	public String showHome(){
		// logical name what view 
		return "home";
	}

}
