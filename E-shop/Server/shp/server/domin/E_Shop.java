package shp.server.domin;

import java.io.IOException;
import java.text.ParseException;
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
import shp.common.entities.Verlauf.AKTIONSTYP;
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

public class E_Shop implements E_ShopInterface {

	private ArtikelVerwaltung artikelVW;
	private WarenkorbVerwaltung warenKorbVW;
	private BestellungVerwaltung bestellVW;
	private KundeVerwaltung kundeVW;
	private MitarbeiterVerwaltung mitarbeiterVW;
	private RechnungsVerwaltung rechnungVW;
	private VerlaufsVerwaltung verlaufVW;
	private String datei = "";

	public E_Shop(String datei) throws IOException, ArtikelExistiertBereitsException, ArtikelExistiertNichtException,
			MitarbeiterUsernameIstBenutztException, ParseException, BestandPasstNichtMitPackungsGroesseException {

		this.datei = datei;
		artikelVW = new ArtikelVerwaltung();
		artikelVW.liesDaten(datei + "_Artikel.txt");
		warenKorbVW = new WarenkorbVerwaltung();
		bestellVW = new BestellungVerwaltung();
		kundeVW = new KundeVerwaltung();
		kundeVW.liesDaten(datei + "_Kunde.txt");
		mitarbeiterVW = new MitarbeiterVerwaltung();
		mitarbeiterVW.liesDaten(datei + "_Mitarbeiter.txt");
		rechnungVW = new RechnungsVerwaltung();
		verlaufVW = new VerlaufsVerwaltung();
		verlaufVW.liesDaten(datei + "_Verlauf.txt", artikelVW, kundeVW, mitarbeiterVW);
	}

	// Artikel Methoden
	@Override
	public Vector<Artikel> gibAlleArtikeln() {
		return artikelVW.getArtikelListe();
	}

	@Override
	public Artikel sucheArtikelNachName(String name) throws ArtikelExistiertNichtException {
		return artikelVW.sucheArtikel(name);
	}

//	public Artikel fuegeArtikelEin(String name, String beschreibung, int bestand, double preis, boolean istPackung)
//			throws AnzahlIsNichtDefiniertException, ArtikelExistiertBereitsException {
//
//		Artikel artikel = new Artikel(name, beschreibung, bestand, preis, istPackung);
//		artikelVW.fugeArtikelEin(artikel);
//		return artikel;
//	}

	@Override
	public Artikel fuegeArtikelEin(Mitarbeiter mitarbeiter, String name, String beschreibung, int bestand, double preis,
			boolean istPackung) throws AnzahlIsNichtDefiniertException, ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {

		Artikel artikel = new Artikel(name, beschreibung, bestand, preis, istPackung);
		artikelVW.fugeArtikelEin(artikel);
		verlaufVW.addVerlauf(AKTIONSTYP.Neue, mitarbeiter, name, bestand);

		return artikel;
	}

	@Override
	public Artikel fuegeMassenArtikelEin(Mitarbeiter mitarbeiter, String name, String beschreibung, int bestand,
			double preis, boolean istPackung, int packungsGroesse)
			throws AnzahlIsNichtDefiniertException, ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {

		Massengutartikel artikel = new Massengutartikel(name, beschreibung, bestand, preis, istPackung,
				packungsGroesse);
		verlaufVW.addVerlauf(AKTIONSTYP.Neue, mitarbeiter, name, bestand);
		artikelVW.fugeArtikelEin(artikel);

		return artikel;
	}

	@Override
	public Artikel loescheArtikel(Mitarbeiter mitarbeiter, String name) throws ArtikelExistiertNichtException {
		Artikel artikel = artikelVW.sucheArtikel(name);
		int menge = artikel.getBestand();
		artikelVW.artikelloeschen(artikel);
		verlaufVW.addVerlauf(AKTIONSTYP.LOESCHEN, mitarbeiter, name, menge);

		return artikel;

	}

	@Override
	public Artikel erhoeheArtikelBestand(Mitarbeiter mitarbeiter, String name, int anzahl)
			throws ArtikelExistiertNichtException, BestandPasstNichtMitPackungsGroesseException {

		Artikel artikel = artikelVW.bestandErhoehen(name, anzahl);

		verlaufVW.addVerlauf(AKTIONSTYP.ERHOEHEN, mitarbeiter, name, anzahl);
		return artikel;
	}

	@Override
	public Artikel senkenArtikelBestand(Mitarbeiter mitarbeiter, String name, int anzahl)
			throws ArtikelExistiertNichtException, BestandPasstNichtMitPackungsGroesseException,
			SenkenUnterNullNichtMoeglichException {

		Artikel artikel = artikelVW.bestandSenken(name, anzahl);
		verlaufVW.addVerlauf(AKTIONSTYP.SENKEN, mitarbeiter, name, anzahl);
		return artikel;
	}

	@Override
	public void gibArtikelnlisteAus(List<Artikel> artikelListe) {
		if (artikelListe.isEmpty()) {
			System.out.println("Liste ist leer.");
		} else {
			for (Artikel artikel : artikelListe) {
				System.out.println(artikel);
			}
		}
	}

	@Override
	public boolean checkMassengutatikel(Artikel artikel) throws ArtikelExistiertNichtException {
		if (ArtikelVerwaltung.checkMassengutartikel(artikel)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Massengutartikel artikelZuMassengeutartikel(Artikel artikel) throws ArtikelExistiertNichtException {
		return ArtikelVerwaltung.artikelZuMassengutartikel(artikel);
	}

	@Override
	public void schreibeArtikel() throws IOException {
		artikelVW.schreibeDaten(datei + "_Artikel.txt");
	}

	// Kunde Methoden
	@Override
	public Kunde sucheKunde(String nutzerName) {
		return kundeVW.sucheKunde(nutzerName);
	}

	@Override
	public Kunde kundenRegistrieren(String name, String vorname, String nutzerNr, String passwort, String strasse,
			String hNr, String plz, String ort, String land) throws KundeUsernameIstbenutztException {
		Adresse adresse = new Adresse(strasse, hNr, plz, ort, land);
		Kunde kunde = new Kunde(name, vorname, nutzerNr, passwort, adresse);
		kundeVW.kundeRegistieren(kunde);
		return kunde;

	}

	@Override
	public Kunde kundenEinloggen(String nutzerName, String passwort) throws NutzernameOderPasswortFalschException {
		return kundeVW.kundeEinloggen(nutzerName, passwort);

	}

	@Override
	public List<Bestellung> GibAlleMeineBestellungen(Kunde kunde) {
		return kundeVW.getMeineBestellungen(kunde);

	}

	@Override
	public void loggeKundeAus(Kunde kunde) {
		kundeVW.kundeAusloggen(kunde);
	}

	@Override
	public List<Kunde> gibAlleKunden() {
		return kundeVW.getList_Kunde();
	}

	@Override
	public void schreibeKunde() throws IOException {
		kundeVW.schreibeDaten(datei + "_Kunde.txt");
	}

	// Mitarbeiter Methoden
	@Override
	public Mitarbeiter sucheMitarbeiter(String nutzerName) {
		return mitarbeiterVW.sucheMitarbeiter(nutzerName);
	}

	@Override
	public List<String> mitarbeiterMenue() {
		return mitarbeiterVW.mitarbeiterMenue();
	}

	@Override
	public void mitarbeiterEinfuegen(String name, String vorName, String nutzerName, String passwort)
			throws MitarbeiterUsernameIstBenutztException {

		Mitarbeiter mitarbeiter = new Mitarbeiter(name, vorName, nutzerName, passwort);
		mitarbeiterVW.fuegeMitarbeiterEin(mitarbeiter);

	}

	@Override
	public Mitarbeiter mitarbeiterEinloggen(String nutzerName, String passwort)
			throws NutzernameOderPasswortFalschException {
		return mitarbeiterVW.mitarbeiterEinloggen(nutzerName, passwort);

	}

	@Override
	public void regestiereNeueMitarbeiter(String name, String vorName, String nutzerName, String passwort)
			throws MitarbeiterUsernameIstBenutztException {

		mitarbeiterVW.neueMitarbeiterRegistieren(name, vorName, nutzerName, passwort);

	}

	@Override
	public void loggeMitarbeiterAus(Mitarbeiter mitarbeiter) {
		mitarbeiterVW.mitarbeiterAusloggen(mitarbeiter);
	}

	@Override
	public List<Mitarbeiter> gibAlleMitarbeiter() {
		return mitarbeiterVW.getList_Mitarbeiter();
	}

	@Override
	public void schreibeMitarbeiter() throws IOException {
		mitarbeiterVW.schreibeDaten(datei + "_Mitarbeiter.txt");
	}

	// Rechnung Methoden
	@Override
	public Rechnung erstelleRechnung(Bestellung bestl) {

		return rechnungVW.erstelleRechnung(bestl);
	}

	@Override
	public List<Rechnung> GinAlleRechnungen() {
		return rechnungVW.getRechnungenList();
	}

	// Warenkorb
	@Override
	public void fuegeArtikelInkorbEin(Kunde kunde, Artikel art, int anzahl)
			throws NichtGenugArtikelVorhandenException, BestandPasstNichtMitPackungsGroesseException,
			ArtikelExistiertNichtException, AnzahlIsNichtDefiniertException {
		warenKorbVW.fuegeArtikelInKorbEin(kunde, art, anzahl);
	}

	@Override
	public void entferneArtikelVomWarenkorb(Kunde kunde, Artikel art, int anzahl)
			throws AnzahlIsNichtDefiniertException, BestandPasstNichtMitPackungsGroesseException,
			ArtikelExistiertNichtException {
		warenKorbVW.entferneArtikelKorbListe(kunde, art, anzahl);
	}

	@Override
	public void loescheArtikelVomWarenkorb(Kunde kunde, Artikel art) {
		warenKorbVW.loescheArtikeVomKorb(kunde, art);
	}

	@Override
	public void leereWarenkorb(Kunde kunde) {
		warenKorbVW.leereWarenKorb(kunde);
	}

	@Override
	public Warenkorb getKundenWarenkorb(Kunde kunde) {
		return warenKorbVW.getWarenkorb(kunde);
	}

	// Bestellung
	@Override
	public Bestellung bestellen(Kunde kunde)
			throws WarenkorbLeerException, NichtGenugArtikelVorhandenException, SenkenUnterNullNichtMoeglichException {
		Bestellung best = bestellVW.bestellen(kunde);

		for (Artikel artikel : best.getBestellteArtikeln().keySet()) {
			verlaufVW.addVerlauf(AKTIONSTYP.BESTELLEN, kunde, artikel.getName(),
					best.getBestellteArtikeln().get(artikel));
		}
		return best;
	}

	@Override
	public List<Bestellung> getBestellungList() {
		return bestellVW.getBestellungList();
	}

	// Verlauf
	@Override
	public List<Verlauf> gibVerlauflistaus() throws VerlaufLeerException {
		return verlaufVW.getVerlauflListe();
	}

	//
	@Override
	public void schreibeVerlauf() throws IOException {
		verlaufVW.schreibeDaten(datei + "_Verlauf.txt");
	}

	@Override
	public List<Verlauf> zeigeVerlaufArtikelDreissigTage(String name) throws ArtikelExistiertNichtException {
		Artikel artikel = artikelVW.sucheArtikel(name);
		return verlaufVW.getLetzeDreissigTageVerlauf(artikel);
	}

	public void disconnect() throws IOException {
		// Hier gibt's nichts zu tun, da Anforderungen eines Verbindungsabbruchs
		// durch einen Client bereits vom ClientRequestProcessor verarbeitet werden.
	}

}
