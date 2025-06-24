package it.uniroma3.siw_libro.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.uniroma3.siw_libro.DTO.AutoreDTO;
import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.AutoreService;
import it.uniroma3.siw_libro.util.FileUtils;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/autore")
public class AutoreController {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoreController.class);
	
	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private AuthenticatedUserService authenticatedUserService;
	
	@GetMapping("/visualizzaAutori")
	public String visualizzaAreaAutori(Model model) {
		List<Autore> autori = autoreService.trovaTuttiGliAutori();
		model.addAttribute("autori", autori);
		return "/admin/autore/areaAutori";		
	}
	
	@GetMapping("/creaAutore")
	public String creaAutore(Model model) {
		AutoreDTO autoreDTO = new AutoreDTO();
		model.addAttribute("autoreDTO", autoreDTO);
		return "/admin/autore/creaAutore";
	}
	
	@PostMapping("/creaAutore")
	public String aggiungiCategoria(Model model, @Valid @ModelAttribute AutoreDTO autoreDTO, BindingResult result, RedirectAttributes redirectAttributes)
			throws IOException {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		
		Optional<Autore> autore = autoreService.trovaAutorePerCognome(autoreDTO.getCognome());
		if (autore.isPresent()) {
			result.addError(new FieldError("autoreDTO", "nomeAutore", "L'autore già esiste"));
		}

		if (autoreDTO.getImage().isEmpty()) {
			result.addError(new FieldError("autoreDTO", "image", "Il file è obbligatorio"));
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			System.out.println("C'è qualche problema qui");
			return "/admin/autore/creaAutore";
		}

		try {
			Set<Libro> libri = new HashSet<>();
			String imageName = FileUtils.salvaImmagine(autoreDTO.getImage(), "src/main/resources/static/images/autori/");
			Autore nuovoAutore = autoreService.creaAutore(autoreDTO, imageName, libri);
			autoreService.salvaAutore(nuovoAutore);
			redirectAttributes.addFlashAttribute("successMessage", "Autore creato con successo");
			logger.info("Autore: " + nuovoAutore.getNome() + " " + nuovoAutore.getCognome() + " creato da admin: " + admin.getUsername());
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un problema durante la creazione dell'autore");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di creazione dell'autore" +e.getMessage());
			return "/admin/autore/creaAutore";
		}

		return "redirect:/admin/autore/visualizzaAutori";
	}
	
	@GetMapping("/editAutore")
	public String modificaAutore(Model model, @RequestParam Integer id) {
		try {
			Autore autore = autoreService.trovaAutorePerId(id);
			model.addAttribute("autore", autore);
			AutoreDTO autoreDTO = autoreService.creaAutoreDTO(autore);
			model.addAttribute("autoreDTO", autoreDTO);
		}catch(Exception e) {
			return "redirect:/admin/autore/visualizzaAutori";
		}
		
		return "/admin/autore/modificaAutore";
	}
	
	@PostMapping("/editAutore")
	public String updateAutore(Model model, @RequestParam Integer id,
			@Valid @ModelAttribute AutoreDTO autoreDTO, BindingResult result, RedirectAttributes redirectAttributes) {

		Utente admin = authenticatedUserService.getUtenteAutentication();
		Autore autore = autoreService.trovaAutorePerId(id);
		model.addAttribute("autore", autore);

		if (autoreService.checkImage(autoreDTO)) {
			result.addError(new FieldError("AutoreDTO", "image", "il file è obbligatorio"));
		}
		
		try {
			String uploadDir = "src/main/resources/static/images/autore/";
			String nuovaImmagine = null;
			
			if (autoreDTO.getImage() != null && !autoreDTO.getImage().isEmpty()) {
				if (autore.getImage() != null && !autore.getImage().isEmpty()) {
					FileUtils.eliminaImmagine(autore.getImage(), uploadDir);
				}
				
				nuovaImmagine = FileUtils.salvaImmagine(autoreDTO.getImage(), uploadDir);
			}
			if (result.hasErrors()) {
				return "/admin/autore/modificaAutore";
			}
			
			autore = autoreService.aggiornaAutoreEsistente(autore, autoreDTO, nuovaImmagine);
			autoreService.salvaAutore(autore);
			redirectAttributes.addFlashAttribute("successMessage", "Autore modificato con successo");
			logger.info("Autore: " + autore.getNome() + " " + autore.getCognome() + " modificato da admin: " + admin.getUsername());
		} catch (Exception e) {
			model.addAttribute("autore", autore);
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un problema durante la modifica dell'autore");
			result.addError(new FieldError("AutoreDTO", "image", "il file è obbligatorio"));
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di modifica dell'autore" +e.getMessage());
			return "redirect:/admin/autore/editAutore";
		}



		return "redirect:/admin/autore/visualizzaAutori";
	}
	
	@GetMapping("/deleteAutore")
	public String deleteAutore(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		Autore autoreEliminato = autoreService.trovaAutorePerId(id);
		try {
			autoreService.eliminaAutoreConRisorse(id);
			redirectAttributes.addFlashAttribute("successMessage", "Autore eliminato con successo.");
			logger.info("Autore: " + autoreEliminato.getNome() + " " + autoreEliminato.getCognome() + " eliminato da admin: " + admin.getUsername());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione dell'autore.");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di eliminazione dell'autore" +e.getMessage());
		}
		return "redirect:/admin/autore/visualizzaAutori";
	}
	
	

}
