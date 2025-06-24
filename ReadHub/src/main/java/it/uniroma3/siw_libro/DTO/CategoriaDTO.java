package it.uniroma3.siw_libro.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_libro.model.Libro;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoriaDTO {
	
	@NotEmpty(message = "Nome: parametro richiesto")
	private String nome;
	
	@NotEmpty(message = "Descrizione: parametro richiesto")
	@Size(max = 255, message = "Descrizione: la lunghezza non può superare i 255 caratteri")
	private String descrizione;
	
	@NotNull(message = "Immagine: parametro richiesto")
	private MultipartFile image;
	
	private List<Libro> libri = new ArrayList<>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public List<Libro> getLibri() {
		return libri;
	}

	public void setLibri(List<Libro> libri) {
		this.libri = libri;
	}
	
	
}
