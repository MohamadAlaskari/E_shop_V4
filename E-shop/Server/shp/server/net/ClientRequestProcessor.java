package shp.server.net;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.exceptions.AnzahlIsNichtDefiniertException;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
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
				ausgeben();
			} else if (input.equals("k")) {
				kundenEinloggen();
			} else if (input.equals("d")) {
				loeschen();
			} else if (input.equals("h")) {
				erhoheArtikelBestand();
			} else if (input.equals("w")) {
				senkeArtikelBestand();
			} else if (input.equals("j")) {
				checkMassengutatikel();
			} else if (input.equals("n")) {
				checkMassengutatikel();
			} else if (input.equals("e")) {
				fuegeArtikelEin();
			} else if (input.equals("g")) {
				gibArtikelnlisteAus();
			} else if (input.equals("x")) {
				schreibeArtikel();
			}
			/////////////////////Mitarbeiter/////////////////////////
			else if (input.equals("maeinloggen")) {
                mitarbeiterEinlogen();
            } else if (input.equals("masuchen")) {
                mitarbeiterSuchen();
            } else if (input.equals("maregestrieren")) {
                mitarbeiterRegistrieren();
            } else if (input.equals("malogout")) {
                mitarbeiterLogout();
            } else if (input.equals("mashreiben")) {
                mitarbeiterSchreiben();
            } else if (input.equals("gibAlleMitarbeiter")) {
                sendeMitArbeiter_List_AnClient();
            }
			/////////////////////Ende Mitarbeiter/////////////////////////

			else if (input.equals("s")) {
				// Aktion "_s_peichern" gewählt
				speichern();
			}
			// ---
			// weitere Server-Dienste ...
			// ---

		} while (!(input.equals("q")));

		// Verbindung wurde vom Client abgebrochen:
		disconnect();
	}

/////////////////////////Mitarbeiter ///////////////////////////
	private Mitarbeiter mitarbeiterEinlogen() {
		Mitarbeiter mitarbeiter = null;
		String input = "";
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Nutzername M): ");
			System.out.println(e.getMessage());
		}
		String nutzername = input;

		try {
			input = in.readLine();

		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Password M): ");
			System.out.println(e.getMessage());
		}
		String password = input;
		try {
			mitarbeiter = shop.mitarbeiterEinloggen(nutzername, password);
			out.println("Erfolg");
			sendeMitarbeiterAnClient(mitarbeiter);

		} catch (NutzernameOderPasswortFalschException e) {
			System.out.println(e.getMessage());
			out.println("Fehler");

		}
		return mitarbeiter;
	}

	private Mitarbeiter mitarbeiterSuchen() {
		Mitarbeiter mitarbeiter = null;
		String input = "";
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Nutzername mitarbeiterSuchen): ");
			System.out.println(e.getMessage());
		}
		String nutzername = input;

		try {
			mitarbeiter = shop.sucheMitarbeiter(nutzername);
			out.println("Erfolg");
			sendeMitarbeiterAnClient(mitarbeiter);

		} catch (Exception e) {
			System.out.println(e.getMessage());
        // wir haben noch keine exception
			System.out.println("mitarbeiter mit '" + nutzername + "' nicht gefunden");
			out.println("Fehler");

		}
		return mitarbeiter;
	}

	private void mitarbeiterRegistrieren() {
		Mitarbeiter mitarbeiter = lies_mitarbeiter();

		try {
			shop.regestiereNeueMitarbeiter(mitarbeiter.getName(), mitarbeiter.getVorname(), mitarbeiter.getNutzerName(),
					mitarbeiter.getPasswort());
			out.println("Erfolg");
		} catch (MitarbeiterUsernameIstBenutztException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
			out.println("Fehler");
		}

	}

	private void sendeMitarbeiterAnClient(Mitarbeiter mitarbeiter) {
		out.println(mitarbeiter.getMaId());
		out.println(mitarbeiter.getName());
		out.println(mitarbeiter.getVorname());
		out.println(mitarbeiter.getNutzerName());
		out.println(mitarbeiter.getPasswort());
	}

	private void mitarbeiterLogout() {
		Mitarbeiter mitarbeiter = lies_mitarbeiter();

		try {
			shop.loggeMitarbeiterAus(mitarbeiter);
			out.println("Erfolg");

		} catch (Exception e) {
			out.println("Fehler");
		}

	}

	private Mitarbeiter lies_mitarbeiter() {
		Mitarbeiter mitarbeiter;
		String input = "";

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Name mitarbeiterRegistrieren): ");
			System.out.println(e.getMessage());
		}
		String name = input;

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (vorName mitarbeiterRegistrieren): ");
			System.out.println(e.getMessage());
		}
		String vorName = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (nutzerName mitarbeiterRegistrieren): ");
			System.out.println(e.getMessage());
		}
		String nutzerName = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (password mitarbeiterRegistrieren): ");
			System.out.println(e.getMessage());
		}
		String password = input;

		mitarbeiter = new Mitarbeiter(name, vorName, nutzerName, password);
		return mitarbeiter;
	}

	private void mitarbeiterSchreiben() {
		try {
			shop.schreibeMitarbeiter();
		} catch (IOException e) {
			e.getMessage();

		}
	}

	private void sendeMitArbeiter_List_AnClient() {
		try {
			List<Mitarbeiter> mitarbeiter_List = shop.gibAlleMitarbeiter();
			out.println("Erfolg");
			out.println(mitarbeiter_List.size());
			for (Mitarbeiter mitarbeiter : mitarbeiter_List) {
				sendeMitarbeiterAnClient(mitarbeiter);
			}
		} catch (Exception e) {
			out.println("Fehler");
		}

	}
//////////////////////////Ende Mitarbeiter/////////////////////////////

	private void ausgeben() {
		// Die Arbeit soll wieder das Bibliotheksverwaltungsobjekt machen:
		List<Artikel> artikel = null;
		artikel = shop.gibAlleArtikeln();

		sendeArtikelListAnClient(artikel);
	}

	private void sendeArtikelListAnClient(List<Artikel> artikeln) {
		// Anzahl der gefundenen Bücher senden
		out.println(artikeln.size());
		for (Artikel artikel : artikeln) {
			sendeArtikelAnClient(artikel);
		}
	}

	public void gibArtikelnlisteAus() {
		Vector<Artikel> liste = new Vector<Artikel>();
		String input = "?";
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ListeSize): ");
			System.out.println(e.getMessage());
		}

		int anzahl = Integer.parseInt(input);
		for (int i = 0; i < anzahl; i++) {
			Artikel artikel = getArtikelVonClient();
			liste.add(artikel);
		}
		shop.gibArtikelnlisteAus(liste);
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

	public void fuegeArtikelEin() {
		Mitarbeiter mitarbeiter = getMitarbeiterVonClient();
		Artikel artikel = getArtikelVonClient();

		try {
			artikel = shop.fuegeArtikelEin(mitarbeiter, artikel.getName(), artikel.getBeschreibung(),
					artikel.getBestand(), artikel.getPreis(), artikel.getIstPackung());
			sendeArtikelAnClient(artikel);
			out.println("Erfolg");
		} catch (AnzahlIsNichtDefiniertException | ArtikelExistiertBereitsException
				| BestandPasstNichtMitPackungsGroesseException | ArtikelExistiertNichtException e) {

			e.getMessage();
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

	private void loeschen() {
		String input = null;
		// lese die notwendigen Parameter, einzeln pro Zeile
		// zuerst die Nummer des einzufügenden Buchs:
		Mitarbeiter mitarbeiter = getMitarbeiterVonClient();
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String artikelName = input;

		try {
			shop.loescheArtikel(mitarbeiter, artikelName);
		} catch (ArtikelExistiertNichtException e) {
			e.getMessage();
			out.println("Fehler");
		}
		// Rückmeldung an den Client: Aktion erfolgreich!
		out.println("Erfolg");
	}

	private void erhoheArtikelBestand() {
		String input = null;
		// lese die notwendigen Parameter, einzeln pro Zeile
		// zuerst die Nummer des einzufügenden Buchs:
		Mitarbeiter mitarbeiter = getMitarbeiterVonClient();
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String artikelName = input;

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (anzahl): ");
			System.out.println(e.getMessage());
		}
		int anzahl = Integer.parseInt(input);

		try {
			shop.erhoeheArtikelBestand(mitarbeiter, artikelName, anzahl);
		} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException e) {
			// TODO Auto-generated catch block
			e.getMessage();
			out.println("Fehler");
		}

		// Rückmeldung an den Client: Aktion erfolgreich!
		out.println("Erfolg");

	}

	private void senkeArtikelBestand() {
		String input = null;
		// lese die notwendigen Parameter, einzeln pro Zeile
		// zuerst die Nummer des einzufügenden Buchs:
		Mitarbeiter mitarbeiter = getMitarbeiterVonClient();
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String artikelName = input;

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (anzahl): ");
			System.out.println(e.getMessage());
		}
		int anzahl = Integer.parseInt(input);

		try {
			shop.senkenArtikelBestand(mitarbeiter, artikelName, anzahl);
		} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException
				| SenkenUnterNullNichtMoeglichException e) {

			e.getMessage();
			out.println("Fehler");
		}

		// Rückmeldung an den Client: Aktion erfolgreich!
		out.println("Erfolg");

	}

	public void checkMassengutatikel() {
		boolean status = false;
		Artikel artikel = getArtikelVonClient();

		try {
			status = shop.checkMassengutatikel(artikel);
		} catch (ArtikelExistiertNichtException e) {

			e.getMessage();
		}
		out.println(status);
	}

	public void artikelZuMassengeutartikel() {
		Artikel artikel = getArtikelVonClient();

		try {
			Massengutartikel Artikel_1 = shop.artikelZuMassengeutartikel(artikel);
			sendeArtikelAnClient(Artikel_1);
		} catch (ArtikelExistiertNichtException e) {

			e.getMessage();
			out.println("Fehler");
		}
		out.println("Erfolg");
	}

	public void schreibeArtikel() {
		try {
			shop.schreibeArtikel();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	// kunde

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

	private Mitarbeiter getMitarbeiterVonClient() {
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

	private Artikel getArtikelVonClient() {
		String input = "";
		Artikel artikel = null;
		int artikelId;
		String name;
		String beschreibung;
		int bestand;
		double preis;
		boolean verfuegbar;
		boolean istPackung = false;
		int packungsGroesse;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter id): ");
			System.out.println(e.getMessage());
		}
		artikelId = Integer.parseInt(input);
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		name = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		beschreibung = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		bestand = Integer.parseInt(input);
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		preis = Double.parseDouble(input);
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		verfuegbar = Boolean.parseBoolean(input);
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
			System.out.println(e.getMessage());
		}
		istPackung = Boolean.parseBoolean(input);

		if (istPackung) {

			try {
				input = in.readLine();
			} catch (Exception e) {
				System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name): ");
				System.out.println(e.getMessage());
			}
			packungsGroesse = Integer.parseInt(input);
			artikel = new Massengutartikel(artikelId, name, beschreibung, bestand, preis, verfuegbar, istPackung,
					packungsGroesse);
		} else {
			artikel = new Artikel(artikelId, name, beschreibung, bestand, preis, verfuegbar, istPackung);
		}
		return artikel;
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
