package it.uniroma3.siw_libro.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_libro.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {
	Optional<Categoria> findByNomeCategoria(String nomeCategoria);
}
