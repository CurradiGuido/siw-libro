package it.uniroma3.siw_libro.DTO;

import java.time.LocalDate;

import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Utente;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RecensioneDTO {
	
	@NotEmpty(message = "titolo: parametro richiesto")
	private String titolo;
	
	@NotNull(message = "voto: parametro richiesto")
	private int voto;
	
	@NotEmpty(message = "testo: parametro richiesto")
	private String testo;
	
	
	private LocalDate data;
	private Utente utente;
	private Libro libro;
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public int getVoto() {
		return voto;
	}
	public void setVoto(int voto) {
		this.voto = voto;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	

}
