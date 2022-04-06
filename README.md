# fist-project

## My fist projectø

### Anwendung
Die Anwendung ist eine Webaplication, welche auf einen Localen Server startet wird in der man sich
Einloggen und Registrieren kann, um dann Kommentare zu schreiben. 

### Technologien
Mit Java SpringBoot habe ich das Backend geschrieben, welches mit Restcalls mit dem Forntend kommuniziert.
Das Frontend habe ich mit HTML und Javascript geschrieben.

### Ausführung
Um die Anwendung zu benutzen, muss Maven installiert werden und dann gestartet werdern.
```bash 
mvm clean install
mvn compile	
``` 
Start Docker:
```
Docker run -p 27017:27017 --rm --name somemongo -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=passwort -d mongo
```

Um die Tests auszuführen 
```bash
mvn test
```

