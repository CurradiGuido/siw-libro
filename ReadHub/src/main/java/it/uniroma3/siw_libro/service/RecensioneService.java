package it.uniroma3.siw_libro.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw_libro.DTO.RecensioneDTO;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.model.Recensione;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.repository.RecensioneRepository;
import jakarta.validation.Valid;

@Service
public class RecensioneService {
	
	@Autowired
	private RecensioneRepository recensioneRepository;
	
	public Recensione creaRecensione(String titolo, int voto, String commento, Utente utente, Libro libro) {
		Recensione recensione = new Recensione();
		recensione.setTitolo(titolo);
		recensione.setVoto(voto);
		recensione.setTesto(commento);
		recensione.setUtente(utente);
		recensione.setLibro(libro);
		return recensione;
	}
	
	@Transactional(readOnly = true)
	public List<Recensione> trovaRecensioniDaUtente(Utente utente) {
		return recensioneRepository.findByUtente(utente);
	}
	
	public List<Recensione> trovaTutteLeRecensioni(){
		return (List<Recensione>) recensioneRepository.findAll();
	}

	public void eliminaRecensioneConRisorse(Integer id) {
		if(recensioneRepository.existsById(id)) {
			recensioneRepository.deleteById(id);
		}else {
			throw new IllegalArgumentException("Recensione con ID " + id + " non trovata.");
		}
		
	}

	public Recensione creaRecensioneDaDTO(@Valid RecensioneDTO recensioneDTO, Libro libro,
			Utente utente) {
		// TODO Auto-generated method stub
		Recensione recensione = new Recensione();
		recensione.setVoto(recensioneDTO.getVoto());
		recensione.setTitolo(recensioneDTO.getTitolo());
		recensione.setTesto(recensioneDTO.getTesto());
		recensione.setLibro(libro);
		recensione.setData(LocalDate.now());
		recensione.setUtente(utente);
		
		return recensione;
		
	}

	public Recensione trovaRecensionePerId(Integer id) {
		// TODO Auto-generated method stub
		return recensioneRepository.findById(id).orElse(null);
	}

}
