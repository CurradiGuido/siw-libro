package it.uniroma3.siw_libro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw_libro.model.Recensione;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.RecensioneService;

@Controller
@RequestMapping("/admin/recensione")
public class RecensioneController {
	
	private static final Logger logger = LoggerFactory.getLogger(RecensioneController.class);
	
	@Autowired
	private RecensioneService recensioneService;
	
	@Autowired
	private AuthenticatedUserService authenticatedUserService;
	
	@GetMapping("/visualizzaRecensioni")
	public String visualizzaRecensioni(Model model) {
		List<Recensione> recensioni = recensioneService.trovaTutteLeRecensioni();
		model.addAttribute("recensioni", recensioni);
		return "/admin/recensione/visualizzaRecensioni";
	}
	
	@PostMapping("/deleteRecensione")
	public String cancellaRecensione(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		Recensione recensioneEliminata = recensioneService.trovaRecensionePerId(id);
		try {
			recensioneService.eliminaRecensioneConRisorse(id);
			redirectAttributes.addFlashAttribute("successMessage", "Recensione eliminata con successo.");
			logger.info("La Recensione dell'utente: " + recensioneEliminata.getUtente().getUsername() + " in merito al libro" + recensioneEliminata.getLibro().getTitolo() + " è stata eliminata da admin: " + admin.getUsername());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione della recensione.");
		}
		return "redirect:/admin/recensione/visualizzaRecensioni";
		
	}

}
