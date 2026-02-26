# 🎬 Gestione Remota Piattaforma Cinematografica 🎬 

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![gRPC](https://img.shields.io/badge/gRPC-Client%2FServer-blue)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX-green)
![Build](https://img.shields.io/badge/Build-Maven-red)

Un'applicazione software distribuita per la gestione di una collezione personale di film, sviluppata con architettura **Client-Server** basata su **gRPC**.

Il progetto permette agli utenti di organizzare la propria collezione virtuale di film, offrendo funzionalità di catalogazione, ricerca avanzata e persistenza dei dati.

Progetto realizzato per l'esame di Ing. del Software.

## Funzionalità Principali

* **Autenticazione Utente**: Registrazione e Login per accedere alla propria collezione privata.
* **Gestione Film (CRUD)**:
    * Aggiunta di nuovi titoli con dettagli (regista, anno, genere, rating, stato visione).
    * Modifica delle informazioni esistenti.
    * Rimozione dei film dalla collezione.
* **Ricerca e Filtri**: Ricerca per titolo/regista e filtri per genere o stato di visione.
* **Ordinamento**: Visualizzazione ordinata per Titolo, Anno o Valutazione.
* **Interfaccia Grafica**: GUI intuitiva realizzata in **JavaFX**.

Il sistema segue un'architettura a strati (**Layered Architecture: Client, Protocol, Server**) per garantire modularità e manutenibilità.

## 🛠️ Tecnologie Utilizzate

* **Linguaggio**: Java
* **Framework RPC**: gRPC 
* **UI Framework**: JavaFX
* **Database**: MySQL
* **Build Tool**: Maven
* **Testing**: JUnit 5, Mockito

