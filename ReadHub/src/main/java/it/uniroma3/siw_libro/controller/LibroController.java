package it.uniroma3.siw_libro.controller;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.uniroma3.siw_libro.DTO.LibroDTO;
import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.AutoreService;
import it.uniroma3.siw_libro.service.CategoriaService;
import it.uniroma3.siw_libro.service.LibroService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/libro")
public class LibroController {
	
	private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

	@Autowired
	private LibroService libroService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	@GetMapping("/visualizzaLibri")
	public String visualizzaLibri(Model model) {
		List<Libro> libri = libroService.trovaTuttiILibri();
		model.addAttribute("libri", libri);
		return "/admin/libro/areaLibro";
	}

	@GetMapping("/creaLibro")
	public String creaLibro(Model model) {
		LibroDTO libroDTO = new LibroDTO();
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		List<Autore> autori = autoreService.trovaTuttiGliAutori();
		model.addAttribute("libroDTO", libroDTO);
		model.addAttribute("categorie", categorie);
		model.addAttribute("autori", autori);
		return "/admin/libro/creaLibro";
	}

	@PostMapping("/creaLibro")
	public String aggiungiLibro(Model model, @Valid @ModelAttribute LibroDTO libroDTO, BindingResult result, RedirectAttributes redirectAttributes) {
		
		Utente admin = authenticatedUserService.getUtenteAutentication();
		
		if (libroService.checkLibro(libroDTO)) {
			result.addError(new FieldError("libroDTO", "titolo", "Il libro già esiste"));
		}
		
		if(libroDTO.getPubblicazione() == 0) {
			result.addError(new FieldError("libroDTO", "pubblicazione", "Pubblicazione: parametro richiesto"));
		}
		
		if (libroDTO.getImmagini() == null || libroDTO.getImmagini().stream().allMatch(MultipartFile::isEmpty)) {
		    result.addError(new FieldError("libroDTO", "immagini", "Almeno un'immagine è obbligatoria"));
		}



		// Se ci sono errori di validazione, torna alla view
		if (result.hasErrors()) {
			List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
			List<Autore> autori = autoreService.trovaTuttiGliAutori();
			model.addAttribute("categorie", categorie);
			model.addAttribute("autori", autori);
			return "/admin/libro/creaLibro";
		}

		try {
			// Creazione nuovo libro
			Libro nuovoLibro = new Libro();
			nuovoLibro = libroService.creaLibroDaDTO(nuovoLibro, libroDTO, null);
			libroService.salvaLibro(nuovoLibro);
			redirectAttributes.addFlashAttribute("successMessage", "Libro creato con successo.");
			logger.info("Libro: " + nuovoLibro.getTitolo() + " creato da admin: " + admin.getUsername());
		} catch (Exception e) {
			System.err.println("Errore durante la creazione del libro: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un errore durante la creazione del libro.");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di creazione del libro" +e.getMessage());
			return "/admin/libro/creaLibro";
		}

		return "redirect:/admin/libro/visualizzaLibri";
	}

	@GetMapping("/modificaLibro")
	public String editLibro(Model model, @RequestParam Integer id) {

		Libro libro = libroService.trovaLibroPerId(id);
		model.addAttribute("libro", libro);
		LibroDTO libroDTO = libroService.creaLibroDTO(libro);
		model.addAttribute("libroDTO", libroDTO);
		
		System.out.println("Introduzione trovata: " + libro.getIntroduzione());
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		List<Autore> autori = autoreService.trovaTuttiGliAutori();
		model.addAttribute("categorie", categorie);
		model.addAttribute("autori", autori);

		return "/admin/libro/modificaLibro";
	}

	@PostMapping("/modificaLibro")
	public String modificaLibro(@RequestParam Integer id, Model model, @Valid @ModelAttribute LibroDTO libroDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		// Verifica esistenza del libro da modificare
		Libro libroEsistente = libroService.trovaLibroPerId(id);
		if (libroEsistente == null) {
			model.addAttribute("erroreGenerico", "Libro non trovato");
			return "/admin/libro/modificaLibro";
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println("Errore: " + error.toString()));
			model.addAttribute("libroDTO", libroDTO);
			return "/admin/libro/modificaLibro";
		}

		try {
			libroService.creaLibroDaDTO(libroEsistente, libroDTO, libroEsistente.getImmagini());
			libroService.salvaLibro(libroEsistente);
			redirectAttributes.addFlashAttribute("successMessage", "Libro Modificato con Successo.");
			logger.info("Libro: " + libroEsistente.getTitolo() + " modificato da admin: " + admin.getUsername());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un errore durante la modifica del Libro.");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di modifica del libro" +e.getMessage());
			return "/admin/libro/modificaLibro";
		}

		return "redirect:/admin/libro/visualizzaLibri";
	}

	@GetMapping("/delete")
	public String deleteLibro(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		Libro libroEliminato = libroService.trovaLibroPerId(id);
		try {
			libroService.eliminaLibroConRisorse(id);
			logger.info("Libro: " + libroEliminato.getTitolo() + " eliminato da admin: " + admin.getUsername());
			redirectAttributes.addFlashAttribute("successMessage", "Libro eliminato con successo.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del Libro");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di eliminazione del libro" +e.getMessage());
		}
		return "redirect:/admin/libro/visualizzaLibri";
	}

}
