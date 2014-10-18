package com.myProject.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myProject.web.dao.User;

@Controller
public class LoginController {
	@RequestMapping("/login")
	public String showLogin(){
		return "login";
	}
	
	
	@RequestMapping("/newAccount")
	public String showNewAccount(Model model){
		model.addAttribute("user", new User());
		return "newAccount";
	}

	
	@RequestMapping("/createaccount")
	public String  createAccount(){
		return "accountcreated";
	}

}
