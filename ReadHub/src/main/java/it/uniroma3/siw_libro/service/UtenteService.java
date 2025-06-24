package it.uniroma3.siw_libro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_libro.DTO.UtenteDTO;
import it.uniroma3.siw_libro.ENUM.Ruolo;
import it.uniroma3.siw_libro.model.Recensione;
import it.uniroma3.siw_libro.model.Utente;
import it.uniroma3.siw_libro.repository.UtenteRepository;
import jakarta.validation.Valid;

@Service
public class UtenteService{
	
	
	@Autowired
	private UtenteRepository utenteRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	AuthenticatedUserService authenticatedUserService;

	
	public Utente creaUtente (String nome, String cognome, String username, String passwrod, String email, Ruolo ruolo, String image, List<Recensione> recensioni) {
		
		Utente utente = new Utente();
		utente.setNome(nome);
		utente.setCognome(cognome);
		utente.setUsername(username);
		utente.setPassword(passwrod);
		utente.setEmail(email);
		utente.setRuolo(ruolo);
		utente.setImage(image);
		utente.setRecensioni(recensioni);
		return utente;
	}

	public boolean checkPasswordAndConfirm(String password, String confirmPassword) {
		// TODO Auto-generated method stub
		return password.equals(confirmPassword);
	} 

	public boolean checkEmail(String email) {
		// TODO Auto-generated method stub
		return utenteRepo.findByEmail(email) != null;
	}

	public boolean checkUsername(String username) {
		// TODO Auto-generated method stub
		return utenteRepo.findByUsername(username) != null;
	}

	public Utente creaUtente(@Valid UtenteDTO utenteDTO, String imageName) {
		// TODO Auto-generated method stub
		
		Utente newUtente = new Utente();
		newUtente.setNome(utenteDTO.getNome());
		newUtente.setCognome(utenteDTO.getCognome());
		newUtente.setEmail(utenteDTO.getEmail());
		newUtente.setUsername(utenteDTO.getUsername());
		newUtente.setPassword(bCryptPasswordEncoder.encode(utenteDTO.getPassword()));
		newUtente.setRuolo(Ruolo.USER);
		newUtente.setImage(imageName);
		
		return newUtente;
	}

	public Utente salvaUtente(Utente nuovoUtente) {
		// TODO Auto-generated method stub
		return utenteRepo.save(nuovoUtente);
	}

	public Utente trovaUtentePerMail(String email) {
		// TODO Auto-generated method stub
		return utenteRepo.findByEmail(email);
	}

	public Utente completaOauth(Utente utenteOpt, Utente utenteForm) {
		// TODO Auto-generated method stub
		Utente utente = utenteOpt;
		utente.setNome(utenteForm.getNome());
		utente.setCognome(utenteForm.getCognome());
		utente.setUsername(utenteForm.getUsername());
		utente.setRuolo(Ruolo.USER);		utente.setPassword(bCryptPasswordEncoder.encode("oauth"));
		return utente;
	}

	public void completeRegistration(String email, String username, String password) {
        Utente u = utenteRepo.findByEmail(email);
        u.setUsername(username);
        u.setPassword(new BCryptPasswordEncoder().encode(password));
        utenteRepo.save(u);
    }

	public Utente trovaUtentePerUsername(String username) {
		// TODO Auto-generated method stub
		return utenteRepo.findByUsername(username);
	}

	public void modificaUtente(Utente utenteEsistente, Utente utenteModificato, String nuovaPassword) {
		// TODO Auto-generated method stub
		utenteEsistente.setNome(utenteModificato.getNome());
		utenteEsistente.setCognome(utenteModificato.getCognome());
		utenteEsistente.setEmail(utenteModificato.getEmail());
		
		if(nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
			String passwordCriptata = bCryptPasswordEncoder.encode(nuovaPassword);
			utenteEsistente.setPassword(passwordCriptata);
		}
		
	}


}
