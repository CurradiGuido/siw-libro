package it.uniroma3.siw_libro.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_libro.DTO.CategoriaDTO;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.repository.CategoriaRepository;
import it.uniroma3.siw_libro.util.FileUtils;
import jakarta.validation.Valid;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	public List<Categoria> trovaTutteLeCategorie() {
		return StreamSupport.stream(categoriaRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Categoria creaCategoria(CategoriaDTO categoriaDTO, String filename, List<Libro> libri) {

		Categoria newCategoria = new Categoria();
		newCategoria.setNomeCategoria(categoriaDTO.getNome());
		newCategoria.setDescrizione(categoriaDTO.getDescrizione());
		newCategoria.setImage(filename);
		newCategoria.setLibri(libri);

		return newCategoria;

	}

	public Categoria salvaCategoria(Categoria nuovaCategoria) {
		// TODO Auto-generated method stub
		return categoriaRepository.save(nuovaCategoria);
	}

	public Optional<Categoria> trovaCategoriaPerNome(String nome) {
		// TODO Auto-generated method stub
		return categoriaRepository.findByNomeCategoria(nome);
	}

	public Categoria trovaCategoriaPerId(Integer id) {
		// TODO Auto-generated method stub
		return categoriaRepository.findById(id).orElse(null);
	}

	public CategoriaDTO creaCategoriaDTO(Categoria categoria) {
		// TODO Auto-generated method stub
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setNome(categoria.getNomeCategoria());
		categoriaDTO.setDescrizione(categoria.getDescrizione());
		categoriaDTO.setLibri(categoria.getLibri());

		return categoriaDTO;
	}

	public void cancellaCategoria(Integer id) {
		// TODO Auto-generated method stub
		categoriaRepository.deleteById(id);
	}

	public boolean checkImage(@Valid CategoriaDTO categoriaDTO) {
		// TODO Auto-generated method stub
		return categoriaDTO.getImage().isEmpty();
	}

	public Categoria aggiornaCategoriaEsistente(Categoria categoria, CategoriaDTO categoriaDTO, String nuovaImmagine) {
		categoria.setNomeCategoria(categoriaDTO.getNome());
		categoria.setDescrizione(categoriaDTO.getDescrizione());
		if (nuovaImmagine != null)
			categoria.setImage(nuovaImmagine);
		return categoria;
	}

	public void eliminaCategoriaConRisorse(Integer id) throws IOException {
		Categoria categoria = trovaCategoriaPerId(id);
		if (categoria == null) {
			throw new IllegalArgumentException("Categoria non trovata con id " + id);
		}

		// Elimina l'immagine se esiste
		String uploadDir = "src/main/resources/static/images/categorie/";
		if (categoria.getImage() != null && !categoria.getImage().isEmpty()) {
			FileUtils.eliminaImmagine(categoria.getImage(), uploadDir);
		}

		// Scollega i prodotti (se presenti)
		if (categoria.getLibri() != null && !categoria.getLibri().isEmpty()) {
			for (Libro libro : categoria.getLibri()) {
				libro.setCategoria(null); // oppure set a una "CategoriaGenerica"
			}
		}

		// Ora elimina la categoria
		categoriaRepository.delete(categoria);
	}

}
