package it.uniroma3.siw_libro.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recensioni")
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrecensione")
	private int idRecensione;
	
	@Column(name = "intestazione", length = 80, nullable = false)
	private String titolo;
	
	@Column(name = "valutazione", nullable = false)
	private int voto;
	
	@Column(name = "commento", nullable = false)
	private String testo;
	
	@Column(name = "data")
	private LocalDate data;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nome_cliente", nullable = false, referencedColumnName = "username")
	private Utente utente;
	
	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "nome_libro", nullable = false, referencedColumnName = "titolo")
	private Libro libro;

	public int getIdRecensione() {
		return idRecensione;
	}

	public void setIdRecensione(int idRecensione) {
		this.idRecensione = idRecensione;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(idRecensione, titolo, voto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return idRecensione == other.idRecensione && Objects.equals(titolo, other.titolo) && voto == other.voto;
	}

	@Override
	public String toString() {
		return "Recensione [titolo=" + titolo + ", voto=" + voto + ", testo=" + testo + "]";
	}
	
	

}
