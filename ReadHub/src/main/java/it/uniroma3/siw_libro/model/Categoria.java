package it.uniroma3.siw_libro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorie")
public class Categoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcategoria")
	private int idCategoria;
	
	@Column(name = "nome", length = 100, nullable = false, unique = true)
	private String nomeCategoria;
	
	@Column(name = "descrizione", length = 255, nullable = false)
	private String descrizione;
	
	@Column(name = "fotoIllustrativa")
	private String image;
	
	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Libro> libri = new ArrayList<>();

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Libro> getLibri() {
		return libri;
	}

	public void setLibri(List<Libro> libri) {
		this.libri = libri;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione, idCategoria, image, nomeCategoria);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return Objects.equals(descrizione, other.descrizione) && idCategoria == other.idCategoria
				&& Objects.equals(image, other.image) && Objects.equals(nomeCategoria, other.nomeCategoria);
	}

	@Override
	public String toString() {
		return "Categoria [nomeCategoria=" + nomeCategoria + ", descrizione=" + descrizione + "]";
	}
	
	

}
