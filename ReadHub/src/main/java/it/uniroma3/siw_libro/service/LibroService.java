package it.uniroma3.siw_libro.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_libro.DTO.LibroDTO;
import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.repository.LibroRepository;
import it.uniroma3.siw_libro.util.FileUtils;

@Service
public class LibroService {
	
	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private AutoreService autoreService;
	

	public List<Libro> trovaTuttiILibri() {
		return StreamSupport.stream(libroRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Optional<Libro> trovaLibroPerTitolo(String titolo) {
		return libroRepository.findByTitolo(titolo);
	}
	
	public void salvaLibro(Libro libro) {
		libroRepository.save(libro);
	}

	public void eliminaLibroConRisorse(Integer id) throws IOException {
	    Libro libro = libroRepository.findById(id).orElse(null);
	    if (libro == null) {
	        throw new IllegalArgumentException("Libro non trovato con id " + id);
	    }

	    // 1. Elimina le immagini associate al libro
	    String uploadDir = "uploads/";
	    if (libro.getImmagini() != null && !libro.getImmagini().isEmpty()) {
	        for (String nomeImmagine : libro.getImmagini()) {
	            FileUtils.eliminaImmagine(nomeImmagine, uploadDir);
	        }
	    }

	    // 2. Rimuove le associazioni con gli autori (bidirezionale, se gestito)
	    if (libro.getAutori() != null && !libro.getAutori().isEmpty()) {
	        for (Autore autore : libro.getAutori()) {
	            autore.getLibri().remove(libro); // solo se la relazione è bidirezionale
	        }
	        libro.getAutori().clear(); // rimuove tutti gli autori dal libro
	    }

	    // 3. Rimuove recensioni associate (opzionale, dipende da CascadeType)
	    if (libro.getRecensioniPerLibro() != null) {
	        libro.getRecensioniPerLibro().clear();
	    }

	    // 4. Elimina il libro
	    libroRepository.delete(libro);
	}

	public Libro trovaLibroPerId(Integer id) {
		// TODO Auto-generated method stub
		return libroRepository.findById(id).orElse(null);
	}

	public LibroDTO creaLibroDTO(Libro libro) {
		
		Set<Integer> idAutori = libro.getAutori()
                .stream()
                .map(Autore::getIdAutore) 
                .collect(Collectors.toSet());
		
		LibroDTO libroDTO = new LibroDTO();
		libroDTO.setTitolo(libro.getTitolo());
		libroDTO.setPubblicazione(libro.getPubblicazione());
		libroDTO.setAutori(idAutori);
		libroDTO.setCategoria(libro.getCategoria());
		libroDTO.setPubblicazione(libro.getPubblicazione());
		libroDTO.setRecensioni(libro.getRecensioniPerLibro());
		libroDTO.setIntroduzione(libro.getIntroduzione());

		return libroDTO;
	}
	
	public boolean checkLibro (LibroDTO libroDTO) {
		return trovaLibroPerTitolo(libroDTO.getTitolo()).isPresent();
	}
	
	public Libro creaLibroDaDTO(Libro nuovoLibro, LibroDTO libroDTO, List<String> immaginiPrecedenti) throws IOException {
		nuovoLibro.setTitolo(libroDTO.getTitolo());
		Categoria categoria = categoriaService.trovaCategoriaPerId(libroDTO.getCategoria().getIdCategoria());
		nuovoLibro.setCategoria(categoria);
		nuovoLibro.setIntroduzione(libroDTO.getIntroduzione());
		
		List<String> nomiFile = new ArrayList<>();
	    // Cartella target all'interno della directory delle risorse
	    Path uploadDir = Paths.get("src/main/resources/static/images/libri");
	    Files.createDirectories(uploadDir);

	    for (MultipartFile file : libroDTO.getImmagini()) {
	        if (!file.isEmpty()) {
	            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            Path targetPath = uploadDir.resolve(filename);
	            Files.write(targetPath, file.getBytes());
	            nomiFile.add(filename);
	        }
	    }
	    if (nomiFile.isEmpty() && immaginiPrecedenti != null) {
	        nomiFile = immaginiPrecedenti;
	    }
	    nuovoLibro.setImmagini(nomiFile);

	    // Associazione autori
	    Set<Autore> autori = autoreService.findAllById(libroDTO.getAutori());
	    nuovoLibro.setAutori(autori);

	    // Imposta data pubblicazione (solo modifica)
	    if (libroDTO.getPubblicazione() != 0) {
	        nuovoLibro.setPubblicazione(libroDTO.getPubblicazione());
	    }

	    // In caso di nuovo libro inizializza la lista recensioni
	    if (nuovoLibro.getRecensioniPerLibro() == null) {
	        nuovoLibro.setRecensioniPerLibro(new ArrayList<>());
	    }
	    
	    return nuovoLibro;
	}
	
	public Libro popolaCampiLibroDaDTO(Libro libro, LibroDTO libroDTO, List<String> immaginiPrecedenti) throws IOException {
	    libro.setTitolo(libroDTO.getTitolo());
	    Categoria categoria = categoriaService.trovaCategoriaPerId(libroDTO.getCategoria().getIdCategoria());
	    libro.setCategoria(categoria);

	    // Gestione immagini: salva nuove immagini, oppure mantiene quelle precedenti se nessuna nuova è caricata
	    List<String> nomiFile = new ArrayList<>();
	    for (MultipartFile file : libroDTO.getImmagini()) {
	        if (!file.isEmpty()) {
	            String nomeFile = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            Path path = Paths.get("uploads/" + nomeFile);
	            Files.createDirectories(path.getParent());
	            Files.write(path, file.getBytes());
	            nomiFile.add(nomeFile);
	        }
	    }
	    if (nomiFile.isEmpty() && immaginiPrecedenti != null) {
	        nomiFile = immaginiPrecedenti;
	    }
	    libro.setImmagini(nomiFile);

	    // Associazione autori
	    Set<Autore> autori = autoreService.findAllById(libroDTO.getAutori());
	    libro.setAutori(autori);

	    // Imposta data pubblicazione (solo modifica)
	    if (libroDTO.getPubblicazione() != 0) {
	        libro.setPubblicazione(libroDTO.getPubblicazione());
	    }

	    // In caso di nuovo libro inizializza la lista recensioni
	    if (libro.getRecensioniPerLibro() == null) {
	        libro.setRecensioniPerLibro(new ArrayList<>());
	    }
	    
	    return libro;
	}






}
