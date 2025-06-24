package it.uniroma3.siw_libro.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw_libro.DTO.AutoreDTO;
import it.uniroma3.siw_libro.model.Autore;
import it.uniroma3.siw_libro.model.Libro;
import it.uniroma3.siw_libro.repository.AutoreRepository;
import it.uniroma3.siw_libro.util.FileUtils;
import jakarta.validation.Valid;
@Service
public class AutoreService {
	
	@Autowired
	private AutoreRepository autoreRepository;
	
	public List<Autore> trovaTuttiGliAutori(){
		return StreamSupport.stream(autoreRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Optional<Autore> trovaAutorePerCognome(String cognome) {
		// TODO Auto-generated method stub
		return autoreRepository.findByCognome(cognome);
	}

	public Autore creaAutore(@Valid AutoreDTO autoreDTO, String imageName, Set<Libro> libri) {
		
		
		Autore newAutore = new Autore();
		newAutore.setNome(autoreDTO.getNome());
		newAutore.setCognome(autoreDTO.getCognome());
		newAutore.setDataDiNascita(autoreDTO.getDataDiNascita());
		newAutore.setDataDiMorte(autoreDTO.getDataDiMorte());
		newAutore.setNazionalità(autoreDTO.getNazionalità());
		newAutore.setImage(imageName);
		newAutore.setLibri(libri);
		newAutore.setInformazioniAutore(autoreDTO.getInformazioniAutore());
		
		return newAutore;
	}

	public void salvaAutore(Autore nuovoAutore) {
		// TODO Auto-generated method stub
		autoreRepository.save(nuovoAutore);
		
	}

	public Autore trovaAutorePerId(Integer id) {
		// TODO Auto-generated method stub
		return autoreRepository.findById(id).orElse(null);
	}

	public Autore aggiornaAutoreEsistente(Autore autore, @Valid AutoreDTO autoreDTO, String nuovaImmagine) {
		// TODO Auto-generated method stub
		autore.setNome(autoreDTO.getNome());
		autore.setCognome(autoreDTO.getCognome());
		autore.setDataDiNascita(autoreDTO.getDataDiNascita());
		autore.setDataDiMorte(autoreDTO.getDataDiMorte());
		autore.setNazionalità(autoreDTO.getNazionalità());
		autore.setInformazioniAutore(autoreDTO.getInformazioniAutore());
		
		if(nuovaImmagine != null) {
			autore.setImage(nuovaImmagine);			
		}
		autore.setLibri(autoreDTO.getLibri());
		
		return autore;
	}

	public boolean checkImage(@Valid AutoreDTO autoreDTO) {
		// TODO Auto-generated method stub
		return autoreDTO.getImage().isEmpty();
	}

	public void eliminaAutoreConRisorse(Integer id) throws IOException {
		// TODO Auto-generated method stub
		Autore autore = trovaAutorePerId(id);
		if(autore == null) {
			throw new IllegalArgumentException("Autore non trovato con id " + id);
		}
		
		// Elimina l'immagine se esiste
				String uploadDir = "src/main/resources/static/images/autori/";
				if (autore.getImage() != null && !autore.getImage().isEmpty()) {
					FileUtils.eliminaImmagine(autore.getImage(), uploadDir);
				}

				// Scollega i prodotti (se presenti)
				if (autore.getLibri() != null && !autore.getLibri().isEmpty()) {
					for (Libro libro : autore.getLibri()) {
						libro.setAutori(null); // oppure set a una "CategoriaGenerica"
					}
				}

				// Ora elimina la categoria
				autoreRepository.delete(autore);
		
	}

	public AutoreDTO creaAutoreDTO(Autore autore) {
		// TODO Auto-generated method stub
		AutoreDTO autoreDTO = new AutoreDTO();
		autoreDTO.setNome(autore.getNome());
		autoreDTO.setCognome(autore.getCognome());
		autoreDTO.setDataDiNascita(autore.getDataDiNascita());
		autoreDTO.setDataDiMorte(autore.getDataDiMorte());
		autoreDTO.setNazionalità(autore.getNazionalità());
		autoreDTO.setLibri(autore.getLibri());
		return autoreDTO;
	}
	
	public Set<Autore> findAllById(Set<Integer> ids) {
	    Iterable<Autore> autoriIter = autoreRepository.findAllById(ids);
	    Set<Autore> autori = StreamSupport
	            .stream(autoriIter.spliterator(), false)
	            .collect(Collectors.toSet());

	    if (autori.size() != ids.size()) {
	        throw new RuntimeException("Uno o più autori non esistono");
	    }

	    return autori;
	}
	
	

}
