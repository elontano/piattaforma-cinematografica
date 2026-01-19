# 🎬 Piattaforma Cinematografica - Gestione Remota 🎬 

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![gRPC](https://img.shields.io/badge/gRPC-Client%2FServer-blue)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX-green)
![Build](https://img.shields.io/badge/Build-Maven-red)

Un'applicazione software distribuita per la gestione di una collezione personale di film, sviluppata con architettura **Client-Server** basata su **gRPC**.

Il progetto permette agli utenti di organizzare la propria collezione virtuale di film , offrendo funzionalità di catalogazione, ricerca avanzata e persistenza dei dati.

## Funzionalità Principali

* **Autenticazione Utente**: Registrazione e Login per accedere alla propria collezione privata.
* **Gestione Film (CRUD)**:
    * Aggiunta di nuovi titoli con dettagli (regista, anno, genere, rating, stato visione).
    * Modifica delle informazioni esistenti.
    * Rimozione dei film dalla collezione.
* **Ricerca e Filtri**: Ricerca per titolo/regista e filtri per genere o stato di visione.
* **Ordinamento**: Visualizzazione ordinata per Titolo, Anno o Valutazione.
* **Interfaccia Grafica**: GUI intuitiva realizzata in **JavaFX**.

Il sistema segue un'architettura a strati (**Layered Architecture**) per garantire modularità e manutenibilità.

### 🔌 Modulo Protocol (`protocol`)
Definisce i contratti di comunicazione tra Client e Server.

### 🖥️ Modulo Server (`server`)
Gestisce la logica di business e la persistenza dei dati.

### 📱 Modulo Client (`client`)
Interfaccia utente interattiva sviluppata in JavaFX.

## 🛠️ Tecnologie Utilizzate

* **Linguaggio**: Java
* **Framework RPC**: gRPC 
* **UI Framework**: JavaFX
* **Database**: MySQL
* **Build Tool**: Maven
* **Testing**: JUnit 5, Mockito

## Guida all'Installazione

### Prerequisiti
* Java JDK 17 o superiore.
* Maven installato.
* MySQL Server in esecuzione.

### 1. Configurazione Database
Crea un database vuoto chiamato `piattaforma_cinematografica`. Le tabelle verranno create automaticamente all'avvio del server.
Assicurati che i parametri in `server/src/main/resources/config.properties` corrispondano alla tua configurazione locale:
```properties
server.name=127.0.0.1
port.number=3306
db.name=piattaforma_cinematografica
db.user=root
db.password=LA_TUA_PASSWORD
