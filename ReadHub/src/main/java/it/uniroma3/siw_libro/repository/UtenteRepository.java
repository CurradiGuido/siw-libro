package it.uniroma3.siw_libro.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_libro.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Integer> {
	
	Utente findByEmail(String email);

	Utente findByUsername(String username);
}
