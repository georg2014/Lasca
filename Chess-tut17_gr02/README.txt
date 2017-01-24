README f�r Chess(Stub)

Dieses Projekt dient als Einstieg in die Implementierungsaufgabe (HA2 SWTPP WS 2015/2016). 

Die Aufgabe soll als Servlet auf einem Apache Tomcat Web-Server abgeliefert werden. In eclipse ist dies 
ein "dynamic web project". 

Im Folgenden wird ein �berblick �ber die schon vorgegebenen Ressourcen 
gegeben. Danach folgen noch ein paar Erkl�rungen zur Funktionsweise des Servlets. Au�erdem sollten 
die Kommentare in den bestehenden Dateien beachtet werden. Hier sind auch noch einige Hinweise zu finden.

1) Projekt-�berblick
--------------------

---| Chess
   Der �bergeordnete Projekt-Ordner. Hier befinden sich die Eclipse-Konfiguration und die Projekt-Dateien, 
   au�erdem nat�rlich alle im Folgenden beschriebenen Ordner.
   
   ---| src
      Hier befinden sich alle reinen Java-Source-Code-Dateien, aufgeteilt in packets. In eclipse sind die 
      packets als Ganzes zu sehen (getrennt mit Punkten). Im Dateisystem sind das jeweils Unter-Ordner.
   
      ---| de.tuberlin.pes.swtpp.chess.control
         Packet f�r die Controller-Klassen des Klassenmodells.
         
      ---| de.tuberlin.pes.swtpp.chess.model
         Packet f�r die Entity-Klassen des Klassenmodells, d.h. das eigentliche Datenmodell. 
         
      ---| de.tuberlin.pes.swtpp.chess.test
         Hier befinden sich die jUnit Testcases und Testsuiten, mit denen die anderen Klassen getestet werden.
         
      ---| de.tuberlin.pes.swtpp.chess.web
         Hier befindet sich die Klasse ChessServlet, welche die zentrale Komponente in die Web-Anwendung auf Server-Seite ist. 
         Zu dem Servlet sp�ter mehr.
         
   ---| build
      Hier landen die kompilierten Klassen
      
   ---| WebContent
      In diesem Ordner werden die Web-Ressourcen des Projekts gespeichert, die der Apache Tomcat Server dann wie 
      ein Web-Server zur Verf�gung stellt. Bilder, html-Seiten, css-Dateien sind also �ber eine URL erreichbar (je nach ProjektName
      z.B. http://localhost:8080/ConferencePortal/...) mit dem Pfad innerhalb dieses Ordners erreichbar. Wenn die Ressource
      innerhalb einer Page auf dem Tomcat verlinkt wird kann auch mit einem relativen Pfad gearbeitet werden. Z.B. <a href="meinbild.jpg">Bild</>
  
2) Implementierungsaufgabe

Wir haben die Web-Funktionalit�t in dem gegebenen Projekt schon erledigt. Die Interaktion mit den Requests ist also f�r alle Use-Cases bereits erledigt. 
Ihr m�sst also nur in der Servlet-Klasse den ToDos und den Beispielen folgen und dort die geforderten Klassen und Funktionen einf�gen. Die GUI (HTML/Javascript) 
f�r den Client-Browser) ist auch bereits erledigt. Wichtig f�r die Kommunikation mit dem Servlet ist allerdings das Format der Daten. Die Beispiele sollten genug 
Information enthalten.   
Zur Orientierung und Erkl�rung der bereits erledigten Anteile dienen die beiden folgenden Kapitel.   
  
2) Das Servlet

Ein Servlet ist ein "Beh�ltnis" f�r eine dynamische Web Anwendung, die auf einem Application Server wie dem Apache Tomcat 
ausgef�hrt werden kann. Kern davon ist das Servlet (in unserem Fall ChessServlet), diese Klasse implementiert die Klasse HttpServlet 
und ist im deployment descriptor (Konfiguration des Projekts) registriert. Hierdurch wird dem Apache 
Web Server signalisiert, dass er das Servlet f�r HTTP-requests bereit stellen soll. Alle Anfragen (HTTP requests) des
Benutzers an die URL des Servlets (http://localhost:8080/Chess/ChessServlet) werden an die Servlet-Klasse weitergeleitet.
Die Anfragen im Beispiel werden an die doGet()-Methode von ConferenceServlet weitergeleitet.

Wichtig: 
Die Servlet-Klasse wird einmal vom Server erstellt. Egal welcher Benutzer/welche Session/welcher Browser etc. - man landet immer in dieser
Servlet-Klasse. Die Attribute sind also immer sichtbar. Sie eignen sich nur f�r globale Variablen und die Daten, die nicht vom aktuellen Zustand der
Session eines Benutzers abh�ngen.

2.1) Der Request und die Session

In dem Objekt "HttpServletRequest request" stecken alle Informationen, die der Application Server aus dem Request herausgesucht hat oder
die er der aktuellen Benutzersession zugeordnet hat. Genauer:

- mit request.getParameter("ParameterName):String kriegt man die Parameter aus dem Request, d.h. die Daten die der Benutzer gerade abgeschickt hat.
- mit request.getSession() kriegt man die Session f�r den Benutzer, in der die dem WebServer bekannten Session-Daten gespeichert sind. Hier
  �berleben also die Daten verschiedene Aufrufe von doGet() f�r den gleichen Benutzer. Die Daten, die man z.B. f�r die n�chste JSP-Seite oder "f�r nach dem
  n�chsten Klick des Benutzers" speichern m�chte, gehen hier rein. Beispiel:
  request.getSession().setAttribute("currentUser", meinaktuellerBenutzer) speichert den aktuell durch Login/Passwort ermittelten Benutzer
  User meinaktuellerBenutzer = (User) request.getSession().getAttribute("currentUser") gibt ihn wieder zur�ck.
- mit request.getSession.getAttribute kann man also auch Daten zu den JSP-Seiten schicken, z.B. auch Listen ob Objekten etc.

3) Die Client-Seite

Die Client-Seite besteht in diesem Projekt aus HTML-Dateien, deren dynamische Inhalte mit Javascript erstellt werden. Die Javascript-Funktionen senden Requests 
an das Servlet, welches wiederum mit den geforderten Daten antwortet soll. Kern der Schach-Anwendung sind die Bibliotheken von chessboardjs.com, welches uns das
Schachbrett liefert.