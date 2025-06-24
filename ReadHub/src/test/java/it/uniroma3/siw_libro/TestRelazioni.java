package it.uniroma3.siw_libro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.repository.AutoreRepository;
import it.uniroma3.siw_libro.repository.CategoriaRepository;
import it.uniroma3.siw_libro.repository.LibroRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Rollback(false)
class TestRelazioni {
	
	@Autowired
	private AutoreRepository autoreRepo;
	
	@Autowired
	private LibroRepository libroRepo;
	
	@Autowired
	private CategoriaRepository categoriaRepo;

	@Test
	void test() {
		//
	}
	
	@Test
	void Autori_LibriMapping() {
		
	    Categoria categoria = new Categoria();
	    categoria.setNomeCategoria("CategoriaDiProva");
	    categoria.setDescrizione("ProvaDescrizione");
	    categoria.setImage("ProvaImmagine");
	    categoriaRepo.save(categoria);

	    
	    Libro libro = new Libro();
	    libro.setTitolo("LibroDiProva");
	    libro.setPubblicazione(1990);
	    libro.setCategoria(categoria);

	    
	    Autore autore1 = new Autore();
	    autore1.setNome("NomeProva1");
	    autore1.setCognome("CognomeProva1");
	    autore1.setDataDiNascita(LocalDate.of(1990, 5, 14));
	    autore1.setImage("ImmagineProva1");
	    autore1.setNazionalità("OriginiProva1");

	    
	    Autore autore2 = new Autore();
	    autore2.setNome("NomeProva2");
	    autore2.setCognome("CognomeProva2");
	    autore2.setDataDiNascita(LocalDate.of(1993, 11, 13));
	    autore2.setImage("ImmagineProva2");
	    autore2.setNazionalità("OriginiProva2");

	    
	    autoreRepo.save(autore1);
	    autoreRepo.save(autore2);

	    
	    libro.getAutori().add(autore1);
	    libro.getAutori().add(autore2);
	    autore1.getLibri().add(libro);
	    autore2.getLibri().add(libro);

	    libroRepo.save(libro); 
	    categoria.getLibri().add(libro);
	    categoriaRepo.save(categoria);

	    
	    Optional<Libro> libroSalvato = libroRepo.findByTitolo("LibroDiProva");
	    assertTrue(libroSalvato.isPresent(), "Libro non trovato nel database");

	    Libro libroVerificato = libroSalvato.get();
	    assertEquals(2, libroVerificato.getAutori().size(), "Il libro deve avere 2 autori");
	}
	
	@Test
	void testLibriConImmagini() {
	    Categoria categoria = new Categoria();
	    categoria.setNomeCategoria("CategoriaDiProva");
	    categoria.setDescrizione("ProvaDescrizione");
	    categoria.setImage("ProvaImmagine");
	    categoriaRepo.save(categoria);

	    Autore autore1 = new Autore();
	    autore1.setNome("NomeProva1");
	    autore1.setCognome("CognomeProva1");
	    autore1.setDataDiNascita(LocalDate.of(1990, 5, 14));
	    autore1.setImage("ImmagineProva1");
	    autore1.setNazionalità("OriginiProva1");
	    autoreRepo.save(autore1); // <--- SALVARE PRIMA

	    Libro libro = new Libro();
	    libro.setTitolo("LibroDiProva");
	    libro.setPubblicazione(1990);
	    libro.setCategoria(categoria);
	    libro.setImmagini(List.of("copertina.jpg", "retro.jpg"));
	    libro.getAutori().add(autore1);

	    libroRepo.save(libro); // <--- POI SALVARE IL LIBRO

	    Optional<Libro> optionalLibro = libroRepo.findById(libro.getIdLibro());
	    assertThat(optionalLibro).isPresent();

	    Libro salvato = optionalLibro.get();
	    assertThat(salvato.getImmagini()).containsExactly("copertina.jpg", "retro.jpg");
	}



}