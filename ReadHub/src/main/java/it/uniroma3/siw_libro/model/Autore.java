package it.uniroma3.siw_libro.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "autori")
public class Autore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idautore")
	private int idAutore;
	
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@Column(name = "cognome", length = 50, nullable = false, unique = true)
	private String cognome;
	
	@Column(name = "nascita", nullable = false)
	private LocalDate dataDiNascita;
	
	@Column(name = "scomparsa")
	private LocalDate dataDiMorte;
	
	@Column(name = "origini", nullable = false)
	private String nazionalità;
	
	@Column(name = "fotografia")
	private String image;
	
	@Column(length = 10000)
	private String informazioniAutore;
	
	@ManyToMany(mappedBy = "autori")
	private Set<Libro> libri = new HashSet<>(); 
	
	public int getIdAutore() {
		return idAutore;
	}

	public void setIdAutore(int idAutore) {
		this.idAutore = idAutore;
	}

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
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

	@Override
	public int hashCode() {
		return Objects.hash(cognome, dataDiMorte, dataDiNascita, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(dataDiMorte, other.dataDiMorte)
				&& Objects.equals(dataDiNascita, other.dataDiNascita) && Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "Autore [nome=" + nome + ", cognome=" + cognome + ", dataDiNascita=" + dataDiNascita + ", dataDiMorte="
				+ dataDiMorte + ", nazionalità=" + nazionalità + "]";
	}
	
	

}
