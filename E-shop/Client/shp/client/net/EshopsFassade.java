package shp.client.net;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import shp.common.entities.Adresse;
import shp.common.entities.Artikel;
import shp.common.entities.Bestellung;
import shp.common.entities.Kunde;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Nutzer;
import shp.common.entities.Rechnung;
import shp.common.entities.Verlauf;
import shp.common.entities.Warenkorb;
import shp.common.exceptions.AnzahlIsNichtDefiniertException;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.KundeUsernameIstbenutztException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.NichtGenugArtikelVorhandenException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
import shp.common.exceptions.VerlaufLeerException;
import shp.common.exceptions.WarenkorbLeerException;
import shp.common.interfaces.E_ShopInterface;

/**
 * Klasse mit Fassade der Bibliothek auf Client-Seite. Die Klasse stellt die von
 * der GUI erwarteten Methoden zur Verfügung und realisiert (transparent für die
 * GUI) die Kommunikation mit dem Server. Anmerkung: Auf dem Server wird dann
 * die eigentliche, von der lokalen Bibliotheksversion bekannte Funktionalität
 * implementiert (z.B. Bücher einfügen und suchen)
 * 
 * 
 */
public class EshopsFassade implements E_ShopInterface {

	// Datenstrukturen für die Kommunikation
	private Socket socket = null;
	private BufferedReader sin; // server-input stream
	private PrintStream sout; // server-output stream
	private Nutzer loggedNutzer;

	public Nutzer getLoggedNutzer() {
		return loggedNutzer;
	}

	/**
	 * Konstruktor, der die Verbindung zum Server aufbaut (Socket) und dieser
	 * Grundlage Eingabe- und Ausgabestreams für die Kommunikation mit dem Server
	 * erzeugt.
	 * 
	 * @param host Rechner, auf dem der Server läuft
	 * @param port Port, auf dem der Server auf Verbindungsanfragen warten
	 * @throws IOException
	 */
	public EshopsFassade(String host, int port, Nutzer loggedNutzer) throws IOException {

		this.loggedNutzer = loggedNutzer;
		try {
			// Socket-Objekt fuer die Kommunikation mit Host/Port erstellen
			socket = new Socket(host, port);

			// Stream-Objekt fuer Text-I/O ueber Socket erzeugen
			InputStream is = socket.getInputStream();
			sin = new BufferedReader(new InputStreamReader(is));
			sout = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Fehler beim Socket-Stream öffnen: " + e);
			// Wenn im "try"-Block Fehler auftreten, dann Socket schließen:
			if (socket != null)
				socket.close();
			System.err.println("Socket geschlossen");
			System.exit(0);
		}

		// Verbindung erfolgreich hergestellt: IP-Adresse und Port ausgeben
		System.err.println("Verbunden: " + socket.getInetAddress() + ":" + socket.getPort());

		// Begrüßungsmeldung vom Server lesen
		String message = sin.readLine();
		System.out.println(message);
	}

	/**
	 * Methode, die eine Liste aller im Bestand befindlichen Bücher zurückgibt.
	 * 
	 * @return Liste aller Bücher im Bestand der Bibliothek
	 */

	// Artikel
	@Override
	public Vector<Artikel> gibAlleArtikeln() {

		Vector<Artikel> liste = new Vector<Artikel>();
		sout.println("a");
		String antwort = "?";

		try {
			antwort = sin.readLine();
			int anzahl = Integer.parseInt(antwort);

			for (int i = 0; i < anzahl; i++) {
				// Buch vom Server lesen ...
				Artikel artikel = liesArtikelVonServer();
				// ... und in Liste eintragen
				liste.add(artikel);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return liste;
	}

	/**
	 * Methode zum Suchen von Büchern anhand des Titels. Es wird eine Liste von
	 * Büchern zurückgegeben, die alle Bücher mit exakt übereinstimmendem Titel
	 * enthält.
	 * 
	 * @param titel Titel des gesuchten Buchs
	 * @return Liste der gefundenen Bücher (evtl. leer)
	 */

	@Override
	public Artikel sucheArtikelNachName(String name) throws ArtikelExistiertNichtException {
		Artikel artikel = null;
		sout.println("f");
		sout.println(name);

		try {
			artikel = liesArtikelVonServer();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return artikel;
	}

	/*
	 * public List<Buch> sucheNachTitel(String titel) { List<Buch> liste = new
	 * ArrayList<Buch>();
	 * 
	 * // Kennzeichen für gewählte Aktion senden sout.println("f"); // Parameter für
	 * Aktion senden sout.println(titel);
	 * 
	 * // Antwort vom Server lesen und im info-Feld darstellen: String antwort =
	 * "?"; try { // Anzahl gefundener Bücher einlesen antwort = sin.readLine(); int
	 * anzahl = Integer.parseInt(antwort); for (int i=0; i<anzahl; i++) { // Buch
	 * vom Server lesen ... Buch buch = liesBuchVonServer(); // ... und in Liste
	 * eintragen liste.add(buch); } } catch (Exception e) {
	 * System.err.println(e.getMessage()); return null; } return liste; }
	 */

	private Artikel liesArtikelVonServer() throws IOException {
		String antwort;
		Artikel artikel = null;

		antwort = sin.readLine();
		int id = Integer.parseInt(antwort);

		String artikelName = sin.readLine();

		String beschreibung = sin.readLine();

		antwort = sin.readLine();
		int bestand = Integer.parseInt(antwort);

		antwort = sin.readLine();
		double preis = Double.parseDouble(antwort);

		antwort = sin.readLine();
		boolean verfuegbarkeit = Boolean.parseBoolean(antwort);

		antwort = sin.readLine();
		boolean istPackung = Boolean.parseBoolean(antwort);

		if (istPackung) {
			antwort = sin.readLine();
			int packungsGroesse = Integer.parseInt(antwort);
			artikel = new Massengutartikel(id, artikelName, beschreibung, bestand, preis, verfuegbarkeit, istPackung,
					packungsGroesse);
		} else {
			artikel = new Artikel(id, artikelName, beschreibung, bestand, preis, verfuegbarkeit, istPackung);
		}

		return artikel;
	}

	private Mitarbeiter liesMitarbeiterVonServer() throws IOException {
		String antwort;
		Mitarbeiter mitarbeiter = null;

		antwort = sin.readLine();
		int id = Integer.parseInt(antwort);
		String Name = sin.readLine();
		String vorname = sin.readLine();
		String nutzerName = sin.readLine();
		String password = sin.readLine();

		mitarbeiter = new Mitarbeiter(id, Name, vorname, nutzerName, password);
		return mitarbeiter;

	}

	/**
	 * Methode zum Einfügen eines neuen Buchs in den Bestand. Wenn das Buch bereits
	 * im Bestand ist, wird der Bestand nicht geändert.
	 * 
	 * @param titel  Titel des Buchs
	 * @param nummer Nummer des Buchs
	 * @returns Buch-Objekt, das im Erfolgsfall eingefügt wurde
	 * @throws BuchExistiertBereitsException wenn das Buch bereits existiert
	 */
	@Override
	public Artikel fuegeArtikelEin(Mitarbeiter mitarbeiter, String name, String beschreibung, int bestand, double preis,
			boolean istPackung) throws AnzahlIsNichtDefiniertException, ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		sout.println("e");

		sout.println(mitarbeiter.getMaId());
		sout.println(mitarbeiter.getName());
		sout.println(mitarbeiter.getNutzerName());
		sout.println(mitarbeiter.getPasswort());
		sout.println(mitarbeiter.getVorname());

		sout.println(name);
		sout.println(beschreibung);
		sout.println(bestand);
		sout.println(preis);
		sout.println(istPackung);

		String antwort = "Fehler";
		try {
			antwort = sin.readLine();
			if (antwort.equals("Erfolg")) {
				// Eingefügtes Buch vom Server lesen ...
				Artikel artikel = liesArtikelVonServer();

				// ... und zurückgeben
				return artikel;
			} else {
				// Fehler: Exception (re-)konstruieren
				String message = sin.readLine();
				throw new ArtikelExistiertBereitsException(message);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}

	}

	/*
	 * public Buch fuegeBuchEin(String titel, int nummer) throws
	 * BuchExistiertBereitsException { // Kennzeichen für gewählte Aktion senden
	 * sout.println("e"); // Parameter für Aktion senden sout.println(nummer);
	 * sout.println(titel);
	 * 
	 * // Antwort vom Server lesen: String antwort = "Fehler"; try { antwort =
	 * sin.readLine(); if (antwort.equals("Erfolg")) { // Eingefügtes Buch vom
	 * Server lesen ... Buch buch = liesArtikelVonServer(); // ... und zurückgeben
	 * return buch; } else { // Fehler: Exception (re-)konstruieren String message =
	 * sin.readLine(); throw new BuchExistiertBereitsException(message); } } catch
	 * (IOException e) { System.err.println(e.getMessage()); return null; } }
	 */

	// Kunde

	@Override
	public Kunde kundenEinloggen(String nutzerName, String passwort) {
		sout.println("k");
		sout.println(nutzerName);
		sout.println(passwort);

		String antwort = "Fehler";
		try {
			antwort = sin.readLine();
			if (antwort.equals("Erfolg")) {
				Kunde kunde = liesKundevonServer();
				loggedNutzer = kunde;
				return kunde;
			} else {
				throw new NutzernameOderPasswortFalschException();
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	public Kunde liesKundevonServer() throws IOException {

		String antwort;
		Kunde kunde = null;

		antwort = sin.readLine();
		// System.out.println("fadafa "+antwort);
		int id = Integer.parseInt(antwort);
		String name = sin.readLine();
		String vorname = sin.readLine();
		String nutzerName = sin.readLine();
		String password = sin.readLine();

		String strasse = sin.readLine();
		String hNr = sin.readLine();
		String ort = sin.readLine();
		String plz = sin.readLine();
		String land = sin.readLine();

		Adresse adresse = new Adresse(strasse, hNr, plz, ort, land);

		kunde = new Kunde(id, name, vorname, nutzerName, password, adresse);
		return kunde;

	}

	/**
	 * Methode zum Löschen eines Buchs aus dem Bestand. Es wird nur das erste
	 * Vorkommen des Buchs gelöscht.
	 * 
	 * @param titel  Titel des Buchs
	 * @param nummer Nummer des Buchs
	 */

	public void loescheBuch(String titel, int nummer) {
		// Kennzeichen für gewählte Aktion senden
		sout.println("d");
		// Parameter für Aktion senden
		sout.println(nummer);
		sout.println(titel);

		// Antwort vom Server lesen:
		String antwort;
		try {
			antwort = sin.readLine();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		System.out.println(antwort);
	}

	/**
	 * Methode zum Speichern des Buchbestands in einer Datei.
	 * 
	 * @throws IOException
	 */
	public void schreibeBuecher() throws IOException {
		// Kennzeichen für gewählte Aktion senden
		sout.println("s");
		// (Parameter sind hier nicht zu senden)

		// Antwort vom Server lesen:
		String antwort = "Fehler";
		try {
			antwort = sin.readLine();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		System.out.println(antwort);
	}

	@Override
	public void disconnect() throws IOException {
		// Kennzeichen für gewählte Aktion senden
		sout.println("q");
		// (Parameter sind hier nicht zu senden)

		// Antwort vom Server lesen:
		String antwort = "Fehler";
		try {
			antwort = sin.readLine();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		System.out.println(antwort);
	}

	@Override
	public Artikel erhoeheArtikelBestand(Mitarbeiter mitarbeiter, String name, int anzahl)
			throws ArtikelExistiertNichtException, BestandPasstNichtMitPackungsGroesseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Artikel senkenArtikelBestand(Mitarbeiter mitarbeiter, String name, int anzahl)
			throws ArtikelExistiertNichtException, BestandPasstNichtMitPackungsGroesseException,
			SenkenUnterNullNichtMoeglichException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gibArtikelnlisteAus(List<Artikel> artikelListe) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkMassengutatikel(Artikel artikel) throws ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Massengutartikel artikelZuMassengeutartikel(Artikel artikel) throws ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void schreibeArtikel() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Kunde sucheKunde(String nutzerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kundenRegistrieren(String name, String vorname, String nutzerNr, String passwort, String strasse,
			String hNr, String plz, String ort, String land) throws KundeUsernameIstbenutztException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Bestellung> GibAlleMeineBestellungen(Kunde kunde) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loggeKundeAus(Kunde kunde) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Kunde> gibAlleKunden() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void schreibeKunde() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Mitarbeiter sucheMitarbeiter(String nutzerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> mitarbeiterMenue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mitarbeiterEinfuegen(String name, String vorName, String nutzerName, String passwort)
			throws MitarbeiterUsernameIstBenutztException {
		// TODO Auto-generated method stub

	}

	@Override
	public Mitarbeiter mitarbeiterEinloggen(String nutzerName, String passwort)
			throws NutzernameOderPasswortFalschException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void regestiereNeueMitarbeiter(String name, String vorName, String nutzerName, String passwort)
			throws MitarbeiterUsernameIstBenutztException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loggeMitarbeiterAus(Mitarbeiter mitarbeiter) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Mitarbeiter> gibAlleMitarbeiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void schreibeMitarbeiter() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Rechnung erstelleRechnung(Bestellung bestl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Rechnung> GinAlleRechnungen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fuegeArtikelInkorbEin(Kunde kunde, Artikel art, int anzahl)
			throws NichtGenugArtikelVorhandenException, BestandPasstNichtMitPackungsGroesseException,
			ArtikelExistiertNichtException, AnzahlIsNichtDefiniertException {
		// TODO Auto-generated method stub

	}

	@Override
	public void entferneArtikelVomWarenkorb(Kunde kunde, Artikel art, int anzahl)
			throws AnzahlIsNichtDefiniertException, BestandPasstNichtMitPackungsGroesseException,
			ArtikelExistiertNichtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loescheArtikelVomWarenkorb(Kunde kunde, Artikel art) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leereWarenkorb(Kunde kunde) {
		// TODO Auto-generated method stub

	}

	@Override
	public Warenkorb getKundenWarenkorb(Kunde kunde) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bestellung bestellen(Kunde kunde)
			throws WarenkorbLeerException, NichtGenugArtikelVorhandenException, SenkenUnterNullNichtMoeglichException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bestellung> getBestellungList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Verlauf> gibVerlauflistaus() throws VerlaufLeerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void schreibeVerlauf() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Verlauf> zeigeVerlaufArtikelDreissigTage(String name) throws ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Artikel fuegeMassenArtikelEin(Mitarbeiter mitarbeiter, String name, String beschreibung, int bestand,
			double preis, boolean istPackung, int packungsGroesse)
			throws AnzahlIsNichtDefiniertException, ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Artikel loescheArtikel(Mitarbeiter mitarbeiter, String name) throws ArtikelExistiertNichtException {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO: Weitere Funktionen der Bibliotheksverwaltung, z.B. ausleihen,
	// zurückgeben etc.
	// ...
}
