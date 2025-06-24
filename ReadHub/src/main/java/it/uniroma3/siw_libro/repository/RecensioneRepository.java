package it.uniroma3.siw_libro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Recensione;
import it.uniroma3.siw_libro.model.Utente;

public interface RecensioneRepository extends CrudRepository<Recensione, Integer>{
	
	boolean existsByUtenteAndLibro(Utente utente, Libro libro);
	List<Recensione> findByUtente(Utente utente);

}
