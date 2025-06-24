package it.uniroma3.siw_libro.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import it.uniroma3.siw_libro.DTO.CategoriaDTO;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.CategoriaService;
import it.uniroma3.siw_libro.util.FileUtils;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private AuthenticatedUserService authenticatedUserService;
	
	@GetMapping("/visualizzaCategorie")
	public String visualizzaCategorie(Model model) {
		
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		model.addAttribute("categorie", categorie);
		
		return "/admin/categoria/areaCategorie";
	}
	
	@GetMapping("/creaCategoria")
	public String creaCategorie(Model model) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		model.addAttribute("categoriaDTO", categoriaDTO);
		return "/admin/categoria/creaCategoria";
	}
	
	@PostMapping("/creaCategoria")
	public String aggiungiCategoria(Model model, @Valid @ModelAttribute CategoriaDTO categoriaDTO, BindingResult result, RedirectAttributes redirectAttributes)
			throws IOException {
		
		Utente admin = authenticatedUserService.getUtenteAutentication();
		
		Optional<Categoria> categoria = categoriaService.trovaCategoriaPerNome(categoriaDTO.getNome());
		if (categoria.isPresent()) {
			result.addError(new FieldError("categoriaDTO", "nomeCategoria", "La categoria già esiste"));
		}

		if (categoriaDTO.getImage().isEmpty()) {
			result.addError(new FieldError("categoriaDTO", "image", "Il file è obbligatorio"));
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			System.out.println("C'è qualche problema qui");
			return "/admin/categoria/creaCategoria";
		}

		try {
			List<Libro> libri = new ArrayList<>();
			String imageName = FileUtils.salvaImmagine(categoriaDTO.getImage(), "src/main/resources/static/images/categorie/");
			Categoria nuovaCategoria = categoriaService.creaCategoria(categoriaDTO, imageName, libri);
			categoriaService.salvaCategoria(nuovaCategoria);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria creata con successo");
			logger.info("Categoria: " + nuovaCategoria.getNomeCategoria() + " creata da admin: " + admin.getUsername());
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un problema durante la creazione della categoria");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di creazione della categoria" +e.getMessage());
			return "/admin/aggiungiCategoria";
		}

		return "redirect:/admin/categoria/visualizzaCategorie";
	}
	
	@GetMapping("/modificaCategoria")
	public String modificaCategoria(Model model, @RequestParam Integer id) {		
		try {
			Categoria categoria = categoriaService.trovaCategoriaPerId(id);
			model.addAttribute("categoria", categoria);
			CategoriaDTO categoriaDTO = categoriaService.creaCategoriaDTO(categoria);
			model.addAttribute("categoriaDTO", categoriaDTO);
		} catch (Exception e) {
			return "redirect:/admin/categoria/visualizzaCategorie";
		}
		return "/admin/categoria/modificaCategoria";
	}
	
	@PostMapping("/modificaCategoria")
	public String updateCategoria(Model model, @RequestParam Integer id,
			@Valid @ModelAttribute CategoriaDTO categoriaDTO, BindingResult result, RedirectAttributes redirectAttributes) {
		
		Utente admin = authenticatedUserService.getUtenteAutentication();
		
		Categoria categoria = categoriaService.trovaCategoriaPerId(id);
		model.addAttribute("categoria", categoria);

		if (categoriaService.checkImage(categoriaDTO)) {
			result.addError(new FieldError("CategoriaDTO", "image", "il file è obbligatorio"));
		}
		
		try {
			String uploadDir = "src/main/resources/static/images/categorie/";
			String nuovaImmagine = null;
			
			if (categoriaDTO.getImage() != null && !categoriaDTO.getImage().isEmpty()) {
				if (categoria.getImage() != null && !categoria.getImage().isEmpty()) {
					FileUtils.eliminaImmagine(categoria.getImage(), uploadDir);
				}
				
				nuovaImmagine = FileUtils.salvaImmagine(categoriaDTO.getImage(), uploadDir);
			}
			
			categoria = categoriaService.aggiornaCategoriaEsistente(categoria, categoriaDTO, nuovaImmagine);
			categoriaService.salvaCategoria(categoria);
			redirectAttributes.addFlashAttribute("successMessage", "La categoria è stata modificata con successo");
			logger.info("Categoria: " + categoria.getNomeCategoria() + " modificata da admin: " + admin.getUsername());
		} catch (Exception e) {
			model.addAttribute("categoria", categoria);
			redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un problema durante la modifica della categoria");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di modifica della categoria" +e.getMessage());
			result.addError(new FieldError("CategoriaDTO", "image", "il file è obbligatorio"));
			return "redirect:/admin/categoria/edit";
		}

		if (result.hasErrors()) {
		    System.out.println("Model attributes: " + model.asMap());
		    return "/admin/categoria/modificaCategoria";
		}


		return "redirect:/admin/categoria/visualizzaCategorie";
	}
	
	@GetMapping("/delete")
	public String deleteCategoria(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		Categoria categoriaEliminata = categoriaService.trovaCategoriaPerId(id);
		try {
			categoriaService.eliminaCategoriaConRisorse(id);
			logger.info("Categoria: " + categoriaEliminata.getNomeCategoria() + " eliminata da admin: " + admin.getUsername());
			redirectAttributes.addFlashAttribute("successMessage", "Categoria eliminata con successo.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione della categoria.");
			logger.error("Ciao: " + admin.getUsername() + " è stato rilevato il seguente errore in fase di eliminazione della categoria" +e.getMessage());
		}
		return "redirect:/admin/categoria/visualizzaCategorie";
	}

}
