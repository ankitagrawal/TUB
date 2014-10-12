package com.myProject.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myProject.web.dao.Offer;
import com.myProject.web.service.OfferService;

@Controller
public class offerController {
	
	@Autowired
	OfferService offerService;
	@RequestMapping("/")
	public String showHome(HttpSession session){
		// logical name what view 
		
	     List<String> offers = offerService.getOffers();
		session.setAttribute("offers", offers);
		return "home";
	}

	
	
	@RequestMapping(value="/test" , method=RequestMethod.GET)
	public String showTest(Model model, @RequestParam("id") String id){
		// logical name what view 
		
	   System.out.println(" id is" + id);
	   model.addAttribute("id", id);
		return "home";
	}

	
	@RequestMapping(value="/docreate")
	public String createOffer(Model model, Offer offer){
		// logical name what view 
	   	
	 System.out.println( "offer is " + offer.getName()	);
		return "home";
	}
	
	
	@RequestMapping("/createOffer")
	public String createOff(){
		// logical name what view 
	 System.out.println("hi i m here");
		return "createOffer";
	}
	
	
	/*@RequestMapping("/mv")
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
		
		model.addAttribute("offers","offers");
		return "home";
	}*/
}
