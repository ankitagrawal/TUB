package com.myProject.web.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class offerController {
	
	@RequestMapping("/")
	public String showHome(HttpSession session){
		// logical name what view 
		session.setAttribute("name", "ankit");
		return "home";
	}

	
	@RequestMapping("/mv")
	public ModelAndView showHome(){
		// logical name what view 
		// model exist in request scope not session
		ModelAndView mv = new ModelAndView("home");
		Map<String,Object> model = mv.getModel();
		
		model.put("river", "ganga");
		return mv;
	}
	
	
	
	@RequestMapping("/mvm")
	public String showHome(Model model){
		// logical name what view 
		
		model.addAttribute("name","nisha");
		return "home";
	}
}
