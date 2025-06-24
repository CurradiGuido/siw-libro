package it.uniroma3.siw_libro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.UtenteService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	AuthenticatedUserService authenticatedUserService;
	
	@GetMapping({"", "/"})
	public String homePage(@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (logout != null) {
            model.addAttribute("logout", true);
        }
		return "homePage";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/oauth/complete")
	public String completePage(Model model, @AuthenticationPrincipal OAuth2User user) {
	    Utente utente = authenticatedUserService.getUtenteAutentication();
	    
	    if (utente == null) {
	        utente = new Utente();
	        if(user != null && user.getAttribute("email") != null) {
	            utente.setEmail(user.getAttribute("email"));
	        }
	    }
	    
	    model.addAttribute("utente", utente);
	    return "complete-profile";
	}
	
	@PostMapping("/oauth/complete")
	public String completaProfilo(@ModelAttribute("utente") Utente utenteForm, Model model) {
	    Utente utenteOpt = utenteService.trovaUtentePerMail(utenteForm.getEmail());

	    if (utenteOpt != null) {
	        Utente u = utenteService.completaOauth(utenteOpt, utenteForm);
	        utenteService.salvaUtente(u);
	        model.addAttribute("utente", u);
	        
	        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	                u.getUsername(), 
	                null, 
	                List.of(new SimpleGrantedAuthority(u.getRuolo().toString()))
	            );
	            SecurityContextHolder.getContext().setAuthentication(auth);
	            
	            } else {
	    }
	    
	    return "redirect:/utente/user/home";
	}



	@PostMapping("/oauth/register")
	public String registerOAuthUser(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
		utenteService.completeRegistration(email, username, password);
		logger.info("L'utente: " + username + " si è registrato mediante Oauth");
		return "redirect:/login?success";
	}

}
