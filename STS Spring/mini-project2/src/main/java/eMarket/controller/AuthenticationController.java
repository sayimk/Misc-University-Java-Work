/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import static eMarket.EMarketApp.ROLE_MANAGER;
import static eMarket.EMarketApp.ROLE_CUSTOMER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eMarket.repository.UserRepository;

@Controller
public class AuthenticationController {
		
	@Autowired UserRepository userRepo;
	
	@RequestMapping(value="/user-login", method=RequestMethod.GET)
	public String loginForm() {
		return "security/login-form";
	}

	@RequestMapping(value="/error-login", method=RequestMethod.GET)
	public String invalidLogin(Model model) {
		model.addAttribute("error", true);
		return "security/login-form";
	}
	
	@RequestMapping(value="/success-login", method=RequestMethod.GET)
	public String successLogin() { //HttpServletRequest httpServletRequest, Authentication authentication
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
        eMarket.domain.User user = userRepo.findByLogin(authUser.getUsername());
        String view;
        switch (user.getRole().getId()) {
        	case ROLE_MANAGER: 
        		view = "redirect:/product/"; 
        		break;
        	case ROLE_CUSTOMER: 
        		view = "redirect:/order/"; 
        		break;
        	default: 
        		view = "security/success-login"; 
        		break;
        }
  
		return view;
	}

	@RequestMapping(value="/user-logout", method=RequestMethod.GET)
	public String logout(Model model) {
		model.addAttribute("logout", true);
		return "security/login-form";
	}
	
	@RequestMapping(value="/access-denied", method=RequestMethod.GET)
	public String error() {
		return "security/error-message";
	}
}