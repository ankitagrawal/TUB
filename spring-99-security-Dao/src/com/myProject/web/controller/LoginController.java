package com.myProject.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myProject.web.dao.DaoManager;
import com.myProject.web.dao.Offer;
import com.myProject.web.dao.User;
import com.myProject.web.service.UserService;

@Controller
public class LoginController {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@RequestMapping("/login")
	public String showLogin(){
		return "login";
	}
	
	
	@RequestMapping("/newAccount")
	public String showNewAccount(Model model){
		model.addAttribute("user", new User());
		return "newAccount";
	}

	
	
	@RequestMapping(value="/createaccount", method=RequestMethod.POST)
	public String createAccount( @Valid User user, BindingResult result){
		// logical name what view 
	   	
		if (result.hasErrors()){
			System.out.println("form does not validated");
			List<ObjectError>errors = result.getAllErrors();
			for(ObjectError error : errors){
				System.out.println(error);
			}
			return "newAccount";
		}else {
			System.out.println("form validated");
		}
		user.setAuthority("user");
		user.setEnabled(true);
		int users = DaoManager.getHibernateTemplate().getUserCount();
		System.out.println("  number of user" + users);
		try{
			userService.create(user);
		}catch(DuplicateKeyException ex){
			result.rejectValue("username", "DuplicateKey.user.username", "The user name already exist");
			return "newAccount";
		}
		
	 System.out.println( "user is " + user.getUsername()	);
		return "accountcreated";
	}
	

}
