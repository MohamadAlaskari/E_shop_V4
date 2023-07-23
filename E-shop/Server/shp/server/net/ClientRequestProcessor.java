package shp.server.net;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import shp.common.entities.Adresse;
import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Verlauf;
import shp.common.exceptions.AnzahlIsNichtDefiniertException;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.KundeUsernameIstbenutztException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
import shp.common.exceptions.VerlaufLeerException;
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
				input = "q";
			} else if (input.equals("sucheartikel")) {
				sucheArtikelNachName();
			} else if (input.equals("a")) {
				getArtikeln();
			} else if (input.equals("d")) {
				loescheArtikel();
			} else if (input.equals("erhoehen")) {
				erhoheArtikelBestand();
			} else if (input.equals("senken")) {
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
			///////////////////// Mitarbeiter/////////////////////////
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
			///////////////////// Ende Mitarbeiter/////////////////////////
			// ---------------------------kunde----------------------------//
			else if (input.equals("regK")) {
				kundenRegistrieren();
			} else if (input.equals("suchenKunde")) {
				sucheKunde();
			} else if (input.equals("einlogenk")) {
				kundenEinloggen();
			}
//--------------------------Endekunde-----------------------------//
			///////////////////// Verlaus/////////////////////////
			else if (input.equals("gibVerlaufListe")) {
				gibVerlaufListe();
			}

			else if (input.equals("s")) {
				// Aktion "_s_peichern" gewählt
				speichern();
			}

		} while (!(input.equals("q")));

		// Verbindung wurde vom Client abgebrochen:
		disconnect();
	}

	private void gibVerlaufListe() {
		List<Verlauf> verlausf_List = null;
		try {
			out.println("Erfolg");
			verlausf_List = shop.gibVerlauflistaus();
			out.println(verlausf_List.size());
			for (Verlauf verlauf : verlausf_List) {
				sendeVerlaufanClient(verlauf);
			}
		} catch (VerlaufLeerException e) {
			out.println("Fehler");
		}

	}

	private void sendeVerlaufanClient(Verlauf verlauf) {
		out.println(verlauf.getAktion());
		
	}

///////////////////////// Ende Verlauf   ///////////////////////////

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
			if (mitarbeiter != null) {

				sendeMitarbeiterAnClient(mitarbeiter);
			} else {
				out.println("");
			}

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
		if (mitarbeiter != null) {
			out.println(mitarbeiter.getMaId());
			out.println(mitarbeiter.getName());
			out.println(mitarbeiter.getVorname());
			out.println(mitarbeiter.getNutzerName());
			out.println(mitarbeiter.getPasswort());
		}
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

	private void getArtikeln() {
		// Die Arbeit soll wieder das Bibliotheksverwaltungsobjekt machen:
		List<Artikel> artikeln = null;
		artikeln = shop.gibAlleArtikeln();

		sendeArtikelListAnClient(artikeln);
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
		System.out.println(mitarbeiter);
		Artikel artikel = getArtikelVonClient();

		try {

			artikel = shop.fuegeArtikelEin(mitarbeiter, artikel.getName(), artikel.getBeschreibung(),
					artikel.getBestand(), artikel.getPreis(), artikel.getIstPackung());
			out.println("Erfolg");
			sendeArtikelAnClient(artikel);
		} catch (AnzahlIsNichtDefiniertException | ArtikelExistiertBereitsException
				| BestandPasstNichtMitPackungsGroesseException | ArtikelExistiertNichtException e) {

			e.getMessage();
			out.println("Fehler");
		}

	}

	public void sucheArtikelNachName() {
		String input = null;

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String name = input;

		try {
			out.println("Erfolg");
			Artikel artikel = shop.sucheArtikelNachName(name);
			sendeArtikelAnClient(artikel);
		} catch (ArtikelExistiertNichtException e) {
			e.getMessage();
			out.println("Fehler");
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

	private void loescheArtikel() {
		String input = null;

		Mitarbeiter mitarbeiter = getMitarbeiterVonClient();
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (ArtikelName): ");
			System.out.println(e.getMessage());
		}
		String artikelName = input;

		try {

			out.println("Erfolg");
			Artikel artikel = shop.loescheArtikel(mitarbeiter, artikelName);
			sendeArtikelAnClient(artikel);
		} catch (ArtikelExistiertNichtException e) {
			e.getMessage();
			out.println("Fehler");
		}
		// Rückmeldung an den Client: Aktion erfolgreich!
	}

	private void erhoheArtikelBestand() {
		String input = null;

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
			out.println("Erfolg");
			Artikel artikel = shop.erhoeheArtikelBestand(mitarbeiter, artikelName, anzahl);
			sendeArtikelAnClient(artikel);
		} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException e) {
			// TODO Auto-generated catch block
			e.getMessage();
			out.println("Fehler");
		}
	}

	private void senkeArtikelBestand() {
		String input = null;

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
			out.println("Erfolg");
			Artikel artikel = shop.senkenArtikelBestand(mitarbeiter, artikelName, anzahl);
			sendeArtikelAnClient(artikel);
		} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException
				| SenkenUnterNullNichtMoeglichException e) {
			e.getMessage();
			out.println("Fehler");
			e.printStackTrace();
		}

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

	// ---------------------------------- kunde----------------------------//
	public void sucheKunde() {
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
			kunde = shop.sucheKunde(nutzername);
			out.println("Erfolg");
			sendeKundeAnClient(kunde);
		} catch (Exception e) {
			out.println("Fehler");
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
			out.println("Fehler");
			System.out.println(e.getMessage());
		}
		return kunde;

	}

	public Kunde kundenRegistrieren() {
		Kunde kunde = null;
		String input = "";

		String name;
		String vorname;
		String userName;
		String password;
		String strasse;
		String hNr;
		String plz;
		String ort;
		String land;

		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde name): ");
			System.out.println(e.getMessage());
		}
		name = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde vorname): ");
			System.out.println(e.getMessage());
		}
		vorname = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde userName): ");
			System.out.println(e.getMessage());
		}
		userName = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde password): ");
			System.out.println(e.getMessage());
		}
		password = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde strasse): ");
			System.out.println(e.getMessage());
		}
		strasse = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde hNr): ");
			System.out.println(e.getMessage());
		}
		hNr = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde plz): ");
			System.out.println(e.getMessage());
		}
		plz = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde ort): ");
			System.out.println(e.getMessage());
		}
		ort = input;
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (kunde land): ");
			System.out.println(e.getMessage());
		}
		land = input;
		try {
			kunde = shop.kundenRegistrieren(name, vorname, userName, password, strasse, hNr, plz, ort, land);
			out.println("Erfolg");
			sendeKundeAnClient(new Kunde(name, vorname, userName, password, new Adresse(strasse, hNr, plz, ort, land)));
			schreibeKunde();
		} catch (KundeUsernameIstbenutztException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kunde;
	}

	public void schreibeKunde() {
		try {
			shop.schreibeKunde();
		} catch (IOException e) {
			e.getMessage();
		}
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

	//////////////////////////// Ende kund ///////////////////////

	private Mitarbeiter getMitarbeiterVonClient() {
		Mitarbeiter mitarbeiter;
		String input = "";
		try {
			input = in.readLine();
		} catch (Exception e) {
			System.out.println("--->Fehler beim Lesen vom Client (Name mitarbeiterRegistrieren): ");
			System.out.println(e.getMessage());
		}
		String idstring = input;
		int id = Integer.parseInt(idstring);
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

		mitarbeiter = new Mitarbeiter(id, name, vorName, nutzerName, password);
		return mitarbeiter;
	}

	private Artikel getArtikelVonClient() {
		String input = "";
		Artikel artikel = null;

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
		istPackung = Boolean.parseBoolean(input);

		if (istPackung) {

			try {
				input = in.readLine();
			} catch (Exception e) {
				System.out.println("--->Fehler beim Lesen vom Client (Mitarbeiter name):");
				System.out.println(e.getMessage());
			}
			packungsGroesse = Integer.parseInt(input);
			artikel = new Massengutartikel(name, beschreibung, bestand, preis, istPackung, packungsGroesse);
		} else {
			artikel = new Artikel(name, beschreibung, bestand, preis, istPackung);

		}
		System.out.println(artikel);
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
