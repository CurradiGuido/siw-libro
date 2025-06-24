**ReadHub** (di seguito denominata “Community”) è un’organizzazione operante nel settore dell’informazione digitale, il cui core service consiste nell’erogazione gratuita di contenuti editoriali consultabili in modalità remota. L’azienda intende fornire una piattaforma strutturata per la fruizione, l’analisi e l’arricchimento di un archivio digitale composto da libri, autori e recensioni utente. A tal fine è stato progettato e realizzato **ReadHub** (di seguito denominato “Sistema”), un sistema informativo distribuito ad architettura Client – Server, concepito per supportare in modo efficiente e scalabile le operazioni di accesso, gestione e persistenza dei dati editoriali.

Il Sistema ReadHub è stato concepito per soddisfare esigenze differenti in base alla tipologia di utente autenticato, prevedendo 3 livelli di accesso, ciascuno associato a specifiche autorizzazioni:

- Utenti **Occasionali** (Non Autenticati):

Possono accedere in sola lettura all’intero corpus informativo del Sistema. Sono in grado di visualizzare le informazioni bibliografiche relative ai libri, le schede degli autori e i contenuti delle recensioni. Non è concessa alcuna operazione di modifica o inserimento dati;

- Utenti **Registrati**:

Oltre alla funzionalità di consultazione, hanno la facoltà di inserire una singola recensione per ciascun libro presente nel sistema. Ogni recensione è costituita da un titolo, un voto (un intero compreso tra 1 e 5) e un testo descrittivo. La logica applicativa vincola ciascun utente registrato ad una sola recensione per libro, garantendo così l’unicità del contributo;

- **Amministratori**:

Dispongono di un accesso completo al back- end informativo del Sistema. Sono autorizzati a creare, modificare e cancellare i dati relativi ai libri e agli autori. Inoltre, hanno la possibilità di eliminare recensioni eventualmente ritenute inappropriate o non conformi con le linee guida editoriali, pur non essendo abilitati alla loro modifica. L’amministratore agisce dunque come supervisore e gestore della qualità dei contenuti.

Il Sistema consente la gestione persistente e relazionale delle seguenti entità:

- **Libro**: Titolo, Anno di pubblicazione, una o più immagini rappresentative, autore o autori associati;
- **Autore**: Nome e Cognome, Data di nascita, eventuale data di morte, nazionalità e foto;
- **Recensione**: titolo, voto (intero tra 1 e 5), testo descrittivo;
- **Categoria**: Nome, Descrizione, Immagine;
- **Utente**: Nome, Cognome, Email, Username, Password, Immagine;

L’uso del sistema in discussione è descritto principalmente dal seguente caso d’uso:

| Codice | Titolo Caso d’Uso | Attore | Tipo Operazione |
| --- | --- | --- | --- |
| **UC1** | Consultazione dettagli libro | Utente Occasionale | Lettura |
| **UC2** | Consultazione dettagli autore | Utente Occasionale | Lettura |
| **UC3** | Inserimento Recensione Libro | Utente Registrato | Inserimento |
| **UC4** | Visualizzazione delle proprie recensioni | Utente Registrato | Lettura |
| **UC5** | Inserimento nuovo libro | Amministratore | Inserimento |
| **UC6** | Modifica dati autore | Amministratore | Aggiornamento |

- **Caso d’Uso UC1**: Consultazione Recensioni libro – Attore primario: Utente Occasionale:

1. Un Utente occasionale “accede” al sito web per cercare informazioni su un libro;
2. L’Utente seleziona l’attività “Novità Editoriali”;
3. Il Sistema mostra l’elenco dei libri disponibili, con titolo e autore;
4. L’Utente seleziona “Scopri cosa ne pensano i membri della community” dall’elenco;
5. Il Sistema mostra i dettagli sulle recensioni del libro selezionato, inclusi: nome dell’utente, titolo della recensione, voto e testo;

- **Caso d’Uso UC2**: Consultazione dettagli autore – Attore primario: Utente Occasionale:

1. Un Utente occasionale accede al sito web per cercare informazioni su un autore;
2. L’Utente seleziona l’attività “Novità Editoriali”;
3. Il Sistema mostra l’elenco dei libri disponibili, con titolo e autore;
4. L’Utente seleziona un autore dall’elenco;
5. Il Sistema mostra i dettagli dell’autore selezionato, inclusi: nome, cognome, data e luogo di nascita, eventuale data di morte, nazionalità, immagine, una sintesi della vita dell’autore ed un elenco dei libri ad esso associato;

- **_Caso d’Uso UC3_**_: Inserimento Recensione Libro – Attore primario: Utente Registrato_

1. Un Utente registrato vuole recensire un libro che ha letto;
2. L’Utente inserisce il suo username e la sua password. Il Sistema verifica la correttezza dei dati immessi, e autentica l’Utente. Il Sistema mostra nome e cognome dell’utente;
3. L’utente seleziona l’opzione “Novità Editoriali”;
4. Il Sistema mostra i libri presenti nel catalogo;
5. L’utente sceglie il libro da recensire;
6. Il sistema richiede di inserire: titolo recensione, voto (da 1 a 5) e testo descrittivo;
7. L’utente Inserisce le informazioni richieste e conferma;
8. Il Sistema salva la recensione, la associa all’utente e al libro selezionato, e la rende visibile pubblicamente;

_Estensioni:_

- 2a: Credenziali dell’Amministratore non valide. Il Sistema termina l’esecuzione del caso d’uso;
- 7a: L’utente annulla l’operazione di inserimento. Il Sistema non registra nessuna informazione sulla recensione;
- **Caso d’Uso UC4**: Visualizzazione delle proprie recensioni – Attore primario: Utente Registrato;

1. Un Utente registrato vuole visualizzare le recensioni che ha inserito;
2. L’Utente inserisce il suo username e la sua password. Il Sistema verifica la correttezza dei dati immessi, e autentica l’Utente.
3. Il Sistema mostra nome e cognome dell’Utente, e il menu con le azioni disponibili;
4. L’Utente seleziona l’attività “La tua Libreria”;
5. Il Sistema mostra, oltre le informazioni personali dell’utente, un elenco delle recensioni inserite da quest’ultimo, ciascuna con: titolo recensione, voto, testo, e titolo del libro a cui è associata;

- **_Caso d’Uso UC5_**_: Inserimento nuovo libro – Attore primario: Amministratore_

1. Un Amministratore accede al sistema per inserire un nuovo libro;
2. L’Amministratore inserisce le sue credenziali. Il Sistema verifica la correttezza dei dati immessi, e autentica l’Amministratore. Il Sistema mostra nome e cognome dell’amministratore.
3. Il Sistema mostra il menu di amministrazione;
4. L’Amministratore seleziona l’attività “Gestione Libri” e successivamente “Nuovo Libro”;
5. Il Sistema richiede di inserire: titolo del libro, anno di pubblicazione, introduzione, categoria, autori e un’immagine di copertina.
6. L’Amministratore inserisce le informazioni richieste e seleziona uno o più autori.
7. L’amministratore conferma l’inserimento del nuovo libro;
8. Il Sistema salva il nuovo libro nel database e lo rende disponibile alla consultazione pubblica;

_Estensioni_:

- 2a: Credenziali dell’Amministratore non valide. Il Sistema termina l’esecuzione del caso d’uso;
- 7a: L’Amministratore annullazione l’operazione di inserimento. Il Sistema non registra nessuna informazione sul libro;
- **Caso d’Uso UC6**: Modifica dati autore – Attore primario: Amministratore

1. Un Amministratore accede al sistema per modificare i dati di un autore;
2. L’Amministratore inserisce le sue credenziali. Il Sistema verifica la correttezza dei dati immessi, e autentica l’Amministratore;
3. Il Sistema mostra il menu di amministrazione;
4. L’Amministratore seleziona l’attività “Autori”;
5. Il Sistema mostra l’elenco degli autori registrati;
6. L’Amministratore seleziona “Modifica”;
7. Il Sistema mostra i dettagli dell’autore e consente la modifica dei campi: nome, cognome, data di nascita, data di morte, nazionalità, informazioni autore e fotografia;
8. L’Amministratore modifica le informazioni desiderate e conferma;
9. Il Sistema aggiorna i dati dell’autore e li rende immediatamente disponibili al pubblico;

_Estensioni_:

- 2a: Credenziali dell’Amministratore non valide. Il Sistema termina l’esecuzione del caso d’uso;
- 8a: L’Amministratore annullazione l’operazione di aggiornamento. Il Sistema non registra la modifica di nessuna informazione in merito all’autore;

Strumenti utilizzati per la modellazione:

| **Draw.io** | Applicazione gratuita e open-source per la creazione di diagrammi UML. |
| --- | --- |

**Modello di Dominio**

![Descrizione immagine](img/Modello%20di%20Dominio.png)




**PROGETTAZIONE LATO DB**: (PostgreSQL)

DIAGRAMMA ENTITA’ – RELAZIONE:

- Per ogni libro sono di interesse il titolo, l’anno di pubblicazione, una o più immagini, l’autore o gli autori (ipotizziamo che ogni libro possa avere anche più di un autore);
- Per gli autori sono di interesse il nome, il cognome, la data di nascita e l’eventuale data di morte, la nazionalità e una fotografia;
- Ai libri possono essere associate recensioni scritte da utenti registrati. Una recensione ha un titolo, un voto (un intero compreso tra 1 e 5) e un testo.

Scelte progettuali rilevanti:

- L’entità Utente racchiude i concetti di Utente Registrato, Utente Occasionale e Amministratore, differenziati da una role based, ovvero un approccio basato sui ruoli come meccanismo di controllo degli accessi e spartizione dei privilegi;

![Descrizione immagine](img/Diagramma%20ER.png)


**Tabella Riepilogativa**

| Tabella Figlia | Colonna FK | Tabella Padre | Colonna FK | ON DELETE | Perché | ON UPDATE |
| --- | --- | --- | --- | --- | --- | --- |
| Libro | idCategoria | Categoria | idCategoria | CASCADE | Se elimino una categoria, vengono eliminati anche i libri associati | CASCADE |
| Libro_Autore | IdLibro | Libro | idLibro | CASCADE | Se elimino un libro, vengono eliminati anche i suoi collegamenti con gli autori | CASCADE |
| Libro_Autore | idAutore | Autore | idAutore | RESTRICT | Evito l’eliminazione dell’autore se è ancora associato a libri | CASCADE |
| Recensione | IdLibro | Libro | idLibro | CASCADE | Se elimino un libro, spariscono le recensioni relative | CASCADE |
| Recensione | idUtente | Utente | idUtente | CASCADE | Se elimino l’utente, spariscono le sue recensioni | CASCADE |

**Tecnologie Utilizzate**
- Springboot: Framework base per l'avvio e la configurazione dell'applicazione Spring;
- Spring Web: Gestisce le richieste HTTP;
- Spring Data JPA: Facilita l'interazione con il database tramite repository e query automatiche;
- PostgreSQL: Sistema di gestione di database relazionali utilizzato per la persistenza dei dati;
- HTML: Utilizzato per strutturare le pagine web dell'applicazione;
- CSS: Responsabile dello stile e della presentazione visiva dell'interfaccia utente;
- Thymeleaf: Motore di template per integrare dati dinamici nelle pagine HTML lato server;
- Spring Security: Fornisce l'autenticazione e autorizzazione per proteggere l'applicazione;
- Oauth2: Permette l'autenticazione tramite provider esterni come Google;
- Jasypt: Cifra credenziali sensibili nei file di configurazione per una maggiore sicurrezza;
- Spring Email: Gestisce l'invio di email dall'applicazione;
- LogBack: Sistema di logging per tracciare eventi e accessi all'applicazione;
- Validation: Valida i dati inseriti dagli utenti nei form per garantirne correttezza e integrità
- Data Transfer Object (DTO): Oggetti utilizzati per trasferire dati tra frontend e backend in modo sicuro e strutturato;
- Spring DevTools: Offre funzionalità utili in fase di sviluppo come il reload automatico e live refresh;
