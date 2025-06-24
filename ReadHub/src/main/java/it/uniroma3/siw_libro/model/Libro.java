package it.uniroma3.siw_libro.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "libri")
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlibro")
	private int idLibro;
	
	@Column(name = "titolo", length = 50, nullable = false, unique = true)
	private String titolo;
	
	@Column(name = "annoDiPubblicazione", nullable = false)
	private int pubblicazione;
	
	@Column(length = 10000)
	private String introduzione;
	
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Recensione> recensioniPerLibro = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria", nullable = false, referencedColumnName = "nome")
	private Categoria categoria;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Libro_Autore", joinColumns = @JoinColumn(name = "idlibro"), inverseJoinColumns = @JoinColumn(name = "idautore"))
	private Set<Autore> autori = new HashSet<>();
	
	@ElementCollection
	@CollectionTable(name = "immagini_libro", joinColumns = @JoinColumn(name = "idlibro"))
	@Column(name = "nome_immagine")
	private List<String> immagini;

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

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

	public List<Recensione> getRecensioniPerLibro() {
		return recensioniPerLibro;
	}

	public void setRecensioniPerLibro(List<Recensione> recensioniPerLibro) {
		this.recensioniPerLibro = recensioniPerLibro;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Set<Autore> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}

	public List<String> getImmagini() {
		return immagini;
	}

	public void setImmagini(List<String> immagini) {
		this.immagini = immagini;
	}	

	public String getIntroduzione() {
		return introduzione;
	}

	public void setIntroduzione(String introduzione) {
		this.introduzione = introduzione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pubblicazione, titolo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return pubblicazione == other.pubblicazione && Objects.equals(titolo, other.titolo);
	}

	@Override
	public String toString() {
		return "Libro [titolo=" + titolo + ", pubblicazione=" + pubblicazione + "]";
	}
	
	

}
