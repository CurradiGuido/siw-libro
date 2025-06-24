package it.uniroma3.siw_libro.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import it.uniroma3.siw_libro.DTO.RecensioneDTO;
import it.uniroma3.siw_libro.DTO.UtenteDTO;
import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Recensione;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.repository.RecensioneRepository;
import it.uniroma3.siw_libro.service.AuthenticatedUserService;
import it.uniroma3.siw_libro.service.AutoreService;
import it.uniroma3.siw_libro.service.CategoriaService;
import it.uniroma3.siw_libro.service.LibroService;
import it.uniroma3.siw_libro.service.RecensioneService;
import it.uniroma3.siw_libro.service.UtenteService;
import it.uniroma3.siw_libro.util.FileUtils;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/utente")
public class UtenteController {

	private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);

	@Autowired
	AuthenticatedUserService authenticatedUserService;

	@Autowired
	private LibroService libroService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RecensioneRepository recensioneRepository;

	@Autowired
	private RecensioneService recensioneService;

	@Autowired
	private AutoreService autoreService;

	@GetMapping("/register")
	public String registerPage(Model model) {
		UtenteDTO utenteDTO = new UtenteDTO();
		model.addAttribute("utenteDTO", utenteDTO);
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(Model model, @Valid @ModelAttribute UtenteDTO utenteDTO, BindingResult result,
			RedirectAttributes redirectAttributes) throws IOException {

		if (!utenteService.checkPasswordAndConfirm(utenteDTO.getPassword(), utenteDTO.getConfirmPassword())) {
			result.addError(new FieldError("utenteDTO", "confirmPassword",
					"Reinserisci le password e assicurati che siano uguali"));
		}

		if (utenteService.checkEmail(utenteDTO.getEmail())) {
			result.addError(new FieldError("utenteDTO", "email", "Email già in utilizzo"));
		}

		if (utenteService.checkUsername(utenteDTO.getUsername())) {
			result.addError(new FieldError("utenteDTO", "username", "Username già in utilizzo"));
		}

		if (result.hasErrors()) {
			return "register";
		}

		try {
			String imageName = FileUtils.salvaImmagine(utenteDTO.getImage(),
					"src/main/resources/static/images/utenti/");
			Utente nuovoUtente = utenteService.creaUtente(utenteDTO, imageName);
			utenteService.salvaUtente(nuovoUtente);
			redirectAttributes.addFlashAttribute("successMessage", "✅ Account creato con successo!\r\n"
					+ "			Effettua il login per iniziare la tua avventura su ComicVerse.");
			// Successo: resetta il DTO e mostra il messaggio di successo
			model.addAttribute("utenteDTO", new UtenteDTO());
			logger.info("E' stata rilevata una nuova registrazione: Username = " + nuovoUtente.getUsername());
			return "redirect:/utente/register";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Si è verificato qualche problema durante la registrazione");
			logger.error("E' stato rilevato un problema durante la registrazione del nuovo utente: Username = "
					+ utenteDTO.getUsername());
			return "redirect:/utente/register";
		}

	}

	@GetMapping("/user/home")
	@PreAuthorize("hasRole('USER')")
	public String userHome(Model model) {
		Utente utente = authenticatedUserService.getUtenteAutentication();
		model.addAttribute("utenteDTO", utente);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("UTENTE LOGGATO: " + auth.getName());
		System.out.println("RUOLI: " + auth.getAuthorities());

		return "homePage";

	}

	@GetMapping("/admin/home")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Utente admin = authenticatedUserService.getUtenteAutentication();
		model.addAttribute("adminDTO", admin);
		return "/admin/adminHome";

	}

	@GetMapping("/ultimeUscite")
	public String visionaUltimeUscite(Model model) {

		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		List<Libro> libri = libroService.trovaTuttiILibri();
		model.addAttribute("categorie", categorie);
		model.addAttribute("libri", libri);

		return "/utente/ultimeUscite";
	}

	@GetMapping("/forum")
	public String accediAlForum(Model model) {
		return "/utente/forum";
	}

	@GetMapping("/libreria")
	public String accediAlProfilo(Model model) {
		Utente utente = utenteService
				.trovaUtentePerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("utente", utente);
		List<Recensione> recensioni = recensioneService.trovaRecensioniDaUtente(utente);
		model.addAttribute("recensioni", recensioni);
		return "/utente/libreria";
	}

	@GetMapping("/eventi")
	public String mostraEventi() {
		return "/utente/eventi";
	}

	@GetMapping("/contatti")
	public String contatti(Model model) {
		Utente utente = utenteService
				.trovaUtentePerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("utente", utente);
		return "/utente/contatti";
	}

	@GetMapping("/dettagliAutore")
	public String dettagliAutore(Model model, @RequestParam Integer id) {
		Autore autore = autoreService.trovaAutorePerId(id);
		model.addAttribute("autore", autore);
		return "/utente/dettagliAutore";
	}

	@GetMapping("/valutaRecensioni")
	public String visualizzaRecensioni(Model model, @RequestParam Integer id) {
		Libro libro = libroService.trovaLibroPerId(id);
		List<Recensione> recensioni = libro.getRecensioniPerLibro();
		model.addAttribute("libro", libro);
		model.addAttribute("recensioni", recensioni);

		return "/utente/visualizzaRecensioni";
	}

	@GetMapping("/recensione")
	public String lasciaUnaRecensione(Model model, @RequestParam Integer id) {
		Libro libro = libroService.trovaLibroPerId(id);
		model.addAttribute("libro", libro);
		RecensioneDTO recensioneDTO = new RecensioneDTO();
		recensioneDTO.setLibro(libro);
		model.addAttribute("recensioneDTO", recensioneDTO);
		return "/utente/recensione";
	}

	@PostMapping("/recensione")
	public String aggiungiRecensione(Model model, @RequestParam("libroId") Integer libroId,
			@Valid @ModelAttribute("recensioneDTO") RecensioneDTO recensioneDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {

		Utente utente = utenteService
				.trovaUtentePerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		Libro libro = libroService.trovaLibroPerId(libroId);

		if (recensioneRepository.existsByUtenteAndLibro(utente, libro)) {
			result.rejectValue("titolo", "recensione.exists", "Hai già recensito questo libro");
			System.out.println("Hai già lasciato una recensione per questo libro");
		}

		if (result.hasErrors()) {
			model.addAttribute("libro", libro); // per mostrare info libro/form
			return "/utente/recensione";
		}
		try {
			Recensione recensione = recensioneService.creaRecensioneDaDTO(recensioneDTO, libro, utente);
			recensioneRepository.save(recensione);
			redirectAttributes.addFlashAttribute("successMessage", "Recensione creata con successo.");
			logger.info("L'utente: " + utente.getUsername() + " ha appena lasciato una recensione in merito al libro: "
					+ libro.getTitolo());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"C'è stato qualche problema durante la creazione della recensione. Riprova più tardi");
			logger.error(
					"E' stato rilevato un problema durante l'inserimento di una nuova recensione da parte dell'utente: "
							+ utente.getUsername());
		}

		return "redirect:/utente/ultimeUscite";
	}

	@PostMapping("/aggiornaDatiUtente")
	public String aggiornaDatiUtente(@ModelAttribute("utente") Utente utenteModificato,
			@RequestParam("password") String nuovaPassword, Principal principal,
			RedirectAttributes redirectAttributes) {
		Utente utenteEsistente = utenteService.trovaUtentePerUsername(principal.getName());
		try {
			utenteService.modificaUtente(utenteEsistente, utenteModificato, nuovaPassword);
			utenteService.salvaUtente(utenteEsistente);
			redirectAttributes.addFlashAttribute("successMessage", "Dati modificati con successo.");
			logger.info("L'utente: " + utenteEsistente.getUsername() + " ha appena aggiornato i suoi dati personali");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Si è verificato un problema durante la modifica dei tuoi dati personali");
			logger.error("E' stato rilevato un problema durante l'aggiornamento dei dati personali dell'utente: "
					+ utenteEsistente.getUsername());
		}
		return "redirect:/utente/libreria";
	}

}
