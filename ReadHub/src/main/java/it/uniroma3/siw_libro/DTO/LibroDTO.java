package it.uniroma3.siw_libro.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw_libro.model.Categoria;
import it.uniroma3.siw_libro.model.Recensione;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LibroDTO {
	
	@NotEmpty(message = "Titolo: parametro richiesto")
	private String titolo;
	
	@NotNull(message = "Pubblicazione: parametro richiesto")
	private int pubblicazione;
	
	@NotEmpty(message = "Introduzione: parametro richiesto")
	private String introduzione;
	
	private List<Recensione> recensioni = new ArrayList<>();
	private Categoria categoria;
	private Set<Integer> autori = new HashSet<>();
	private List<MultipartFile> immagini = new ArrayList<>();
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public int getPubblicazione() {
		return pubblicazione;
	}
	public void setPubblicazione(int pubblicazione) {
		this.pubblicazione = pubblicazione;
	}
	public List<Recensione> getRecensioni() {
		return recensioni;
	}
	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Set<Integer> getAutori() {
		return autori;
	}
	public void setAutori(Set<Integer> autori) {
		this.autori = autori;
	}
	public List<MultipartFile> getImmagini() {
		return immagini;
	}
	public void setImmagini(List<MultipartFile> immagini) {
		this.immagini = immagini;
	}
	public String getIntroduzione() {
		return introduzione;
	}
	public void setIntroduzione(String introduzione) {
		this.introduzione = introduzione;
	}
	
	
	
	

}
