package it.uniroma3.siw_libro.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw_libro.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Integer>{
	Optional<Autore> findByCognome(String cognome);
}
