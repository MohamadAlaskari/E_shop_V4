package shp.server.net;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.common.interfaces.E_ShopInterface;

/**
 * Klasse zur Verarbeitung der Kommunikation zwischen EINEM Client und dem
 * Server. Die Kommunikation folgt dabei dem "Protokoll" der Anwendung. Das
 * ClientRequestProcessor-Objekt führt folgende Schritte aus: 0. Begrüßungszeile
 * an den Client senden Danach in einer Schleife: 1. Empfang einer Zeile vom
 * Client (z.B. Aktionsauswahl, hier eingeschränkt); wenn der Client die
 * Abbruchaktion sendet ('q'), wird die Schleife verlassen 2. abhängig von
 * ausgewählter Aktion Empfang weiterer Zeilen (Parameter für ausgewählte
 * Aktion) 3. Senden der Antwort an den Client; die Antwort besteht je nach
 * Aktion aus einer oder mehr Zeilen
 * 
 * @author teschke, eirund
 */
class ClientRequestProcessor implements Runnable {

	// Bibliotheksverwaltungsobjekt, das die eigentliche Arbeit machen soll
	private E_ShopInterface shop;

	// Datenstrukturen für die Kommunikation
	private Socket clientSocket;
	private BufferedReader in;
	private PrintStream out;

	/**
	 * @param clientSocket
	 * @param shop
	 */
	public ClientRequestProcessor(Socket clientSocket, E_ShopInterface shop) {

		this.shop = shop;
		this.clientSocket = clientSocket;

		// I/O-Streams initialisieren und ClientRequestProcessor-Objekt als Thread
		// starten:
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (IOException e2) {
			}
			System.err.println("Ausnahme bei Bereitstellung des Streams: " + e);
			return;
		}

		System.out.println("Verbunden mit " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
	}

	/**
	 * Methode zur Abwicklung der Kommunikation mit dem Client gemäß dem vorgebenen
	 * Kommunikationsprotokoll.
	 */
	public void run() {

		String input = "";

		// Begrüßungsnachricht an den Client senden
		out.println("Server an Client: Bin bereit für Deine Anfragen!");

		// Hauptschleife zur wiederholten Abwicklung der Kommunikation
		do {
			
			// Beginn der Benutzerinteraktion:
			// Aktion vom Client einlesen [dann ggf. weitere Daten einlesen ...]
			try {
				input = in.readLine();
				
			} catch (Exception e) {
				System.out.println("--->Fehler beim Lesen vom Client (Aktion): ");
				System.out.println(e.getMessage());
				continue;
			}

			// Eingabe bearbeiten:
			if (input == null) {
				
				// input wird von readLine() auf null gesetzt, wenn Client Verbindung abbricht
				// Einfach behandeln wie ein "quit"
				input = "q";
			} else if (input.equals("a")) {
				// Aktion "Bücher _a_usgeben" gewählt
				ausgeben();
			} else if (input.equals("k")) {
				kundenEinloggen();
			}
//		 else if (input.equals("e")) {
//				// Aktion "Buch _e_infügen" gewählt
//				einfuegen();
//			} else if (input.equals("f")) {
//				// Aktion "Bücher _f_inden" (suchen) gewählt
//				suchen();
//			} else if (input.equals("s")) {
//				// Aktion "_s_peichern" gewählt
//				speichern();
//			}
			// ---
			// weitere Server-Dienste ...
			// ---

		} while (!(input.equals("q")));

		// Verbindung wurde vom Client abgebrochen:
		disconnect();
	}

	private void ausgeben() {
		// Die Arbeit soll wieder das Bibliotheksverwaltungsobjekt machen:
		List<Artikel> artikel = null;
		artikel = shop.gibAlleArtikeln();

		sendeArtikelListAnClient(artikel);
	}

	private void speichern() {
		// Parameter sind in diesem Fall nicht einzulesen

		// die Arbeit macht wie immer Bibliotheksverwaltungsobjekt:
		try {
			shop.schreibeArtikel();
			shop.schreibeMitarbeiter();
			shop.schreibeVerlauf();
			shop.schreibeKunde();
			out.println("Erfolg");
		} catch (IOException e) {
			System.out.println("--->Fehler beim Sichern: ");
			System.out.println(e.getMessage());
			out.println("Fehler");
		}
	}

	public void sucheArtikelNachName() throws ArtikelExistiertNichtException {
		String input = null;
		// lese die notwendigen Parameter, einzeln pro Zeile
		// hier ist nur der Titel der gesuchten Bücher erforderlich:
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String name = input;
		List<Artikel> artikeln = null;
		Artikel artikel = null;
		if (name.equals("")) {
			artikeln = shop.gibAlleArtikeln();
			sendeArtikelListAnClient(artikeln);
		} else {
			artikel = shop.sucheArtikelNachName(name);
			sendeArtikelAnClient(artikel);
		}

	}

	private void sendeArtikelListAnClient(List<Artikel> artikeln) {
		// Anzahl der gefundenen Bücher senden
		out.println(artikeln.size());
		for (Artikel artikel : artikeln) {
			sendeArtikelAnClient(artikel);
		}
	}

	private void sendeArtikelAnClient(Artikel artikel) {
		out.println(artikel.getArtikelId());
		out.println(artikel.getName());
		out.println(artikel.getBeschreibung());
		out.println(artikel.getBestand());
		out.println(artikel.getPreis());
		out.println(artikel.isVerfuegbar());
		out.println(artikel.getIstPackung());
		if (artikel.getIstPackung()) {
			Massengutartikel artikel_1 = (Massengutartikel) artikel;
			out.println(artikel_1.getPackungsGroesse());
		}
	}

	public Kunde kundenEinloggen() {
		Kunde kunde = null;
		String input = "";
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Nutzername K): ");
			System.out.println(e.getMessage());
		}
		String nutzername = input;
		
		
		try {
			input = in.readLine();
			
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Password K): ");
			System.out.println(e.getMessage());
		}
		String password = input;
		try {
			
			kunde = shop.kundenEinloggen(nutzername, password);
			out.println("Erfolg");
			sendeKundeAnClient(kunde);
			
		} catch (NutzernameOderPasswortFalschException e) {
			System.out.println(e.getMessage());
		}
		return kunde;

	}

	private void sendeKundeAnClient(Kunde kunde) {
		
		out.println(kunde.getKndNr());
		out.println(kunde.getName());
		out.println(kunde.getVorname());
		out.println(kunde.getNutzerName());
		out.println(kunde.getPasswort());
		out.println(kunde.getAdresse().getStrasse());
		out.println(kunde.getAdresse().gethNr());
		out.println(kunde.getAdresse().getOrt());
		out.println(kunde.getAdresse().getPlz());
		out.println(kunde.getAdresse().getLand());

	}

	private void fuegeArtikelEin() {

	}

	private Mitarbeiter getMitarbeiter() {
		String input = "";
		int maId;
		String name;
		String vorname;
		String nutzerName;
		String passwort;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter id): ");
			System.out.println(e.getMessage());
		}
		maId = Integer.parseInt(input);
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		name = input;
		return null;
	}

	private void disconnect() {
		try {
			out.println("Tschuess!");
			clientSocket.close();

			System.out.println("Verbindung zu " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
					+ " durch Client abgebrochen");
		} catch (Exception e) {
			System.out.println("--->Fehler beim Beenden der Verbindung: ");
			System.out.println(e.getMessage());
			out.println("Fehler");
		}
	}
}
