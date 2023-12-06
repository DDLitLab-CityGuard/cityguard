# Anleitung um den Client from Scratch zu starten (ohne die anderen Container)

## Erstmaliges Starten des Containers

1. **Vorbereitung**: Stelle sicher, dass Docker auf deinem System installiert ist. Falls noch nicht geschehen, kannst du Docker von der offiziellen [Docker-Website](https://www.docker.com/get-started) herunterladen und installieren.

2. **Klonen des Projekts**: Wenn das Projekt in einem Git-Repository liegt, klone es zuerst auf deinen lokalen Rechner. Wenn du bereits eine Kopie des Projekts hast, kannst du diesen Schritt überspringen.

    ```
    git clone [URL-DEINES-PROJEKTS]
    cd [PROJEKTVERZEICHNIS]
    ```

3. **Starten des Containers**: Führe den folgenden Befehl im Wurzelverzeichnis deines Projekts aus, um den Docker-Container zu starten. Ersetze `mein_container_name` mit einem Namen deiner Wahl für den Container.

    ```
    docker run -d --name mein_container_name -v ${PWD}:/usr/share/nginx/html -p 80:80 nginx
    ```

    Dieser Befehl startet einen Nginx-Container im Hintergrund (`-d` für "detached") und bindet das aktuelle Verzeichnis (`${PWD}`) an den Nginx-Server im Container. Der Container ist über den Port 80 auf deinem Host-System erreichbar.

## Container Neustarten

Nachdem der Container einmal erstellt und gestartet wurde, kannst du ihn mit den folgenden Docker-Befehlen stoppen, starten oder neustarten:

- **Container stoppen**: 

    ```
    docker stop mein_container_name
    ```

- **Container starten**: 

    ```
    docker start mein_container_name
    ```

- **Container neustarten**: 

    ```
    docker restart mein_container_name
    ```

## Zusätzliche Informationen

- Um den Status des Containers zu überprüfen, verwende:

    ```
    docker ps -a
    ```

- Um Logs des Containers anzusehen, verwende:

    ```
    docker logs mein_container_name
    ```

Bei Fragen oder Problemen mit Docker beziehe dich auf die offizielle [Docker-Dokumentation](https://docs.docker.com/).

