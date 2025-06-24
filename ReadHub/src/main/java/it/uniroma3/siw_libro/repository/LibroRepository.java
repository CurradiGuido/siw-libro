package it.uniroma3.siw_libro.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_libro.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Integer>{
	
	Optional<Libro> findByTitolo(String titolo);
}
