package com.myProject.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myProject.web.dao.BaseDao;
import com.myProject.web.dao.Circle;
import com.myProject.web.dao.DaoManager;
import com.myProject.web.dao.HibernateDaoImpl;
import com.myProject.web.dao.Offer;
import com.myProject.web.service.OfferService;

@Controller
public class offerController {
	
	@Autowired
	OfferService offerService;
	@Autowired
	BaseDao baseDao;
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
	public String createOffer(Model model, @Valid Offer offer, BindingResult result){
		// logical name what view 
	   	
		if (result.hasErrors()){
			System.out.println("form does not validated");
			List<ObjectError>errors = result.getAllErrors();
			for(ObjectError error : errors){
				System.out.println(error);
			}
		}else {
			System.out.println("form validated");
		}
		
	 System.out.println( "offer is " + offer.getName()	);
		return "home";
	}
	
	
	@RequestMapping("/createOffer")
	public String createOff(){
		// logical name what view 

	    HibernateDaoImpl jd = DaoManager.getHibernateTemplate();
	    Circle circle = new Circle(8,"right circle");	
	    jd.getSessionFactory().openSession().save(circle);
		System.out.println(" i am calling dj");
		int  count= jd.getCircleCount();
		System.out.println("count is" +count);
		
	/*		
		offerService.saveCircle(circle);
	    */
		return "createOffer";
	}



	public BaseDao getBaseDao() {
		return baseDao;
	}



	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
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
