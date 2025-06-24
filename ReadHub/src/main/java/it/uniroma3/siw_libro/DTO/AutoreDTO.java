package it.uniroma3.siw_libro.DTO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_libro.model.Libro;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AutoreDTO {
	
	@NotEmpty(message = "Nome: parametro richiesto")
	private String nome;
	
	@NotEmpty(message = "Cognome: parametro richiesto")
	private String cognome;
	
	@NotNull(message = "Data di Nascita: parametro richiesto")
	private LocalDate dataDiNascita;
	
	private LocalDate dataDiMorte;
	
	@NotEmpty(message = "Nazionalità: parametro richiesto")
	private String nazionalità;
	
	@NotNull(message = "Immagine: parametro richiesto")
	private MultipartFile image;
	
	@NotEmpty(message = "Informazioni: Parametro richiesto")
	private String informazioniAutore;
	
	private Set<Libro> libri = new HashSet<>();
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}
	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}
	public LocalDate getDataDiMorte() {
		return dataDiMorte;
	}
	public void setDataDiMorte(LocalDate dataDiMorte) {
		this.dataDiMorte = dataDiMorte;
	}
	public String getNazionalità() {
		return nazionalità;
	}
	public void setNazionalità(String nazionalità) {
		this.nazionalità = nazionalità;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public Set<Libro> getLibri() {
		return libri;
	}
	public void setLibri(Set<Libro> libri) {
		this.libri = libri;
	}
	public String getInformazioniAutore() {
		return informazioniAutore;
	}
	public void setInformazioniAutore(String informazioniAutore) {
		this.informazioniAutore = informazioniAutore;
	}
	
	
	

}
