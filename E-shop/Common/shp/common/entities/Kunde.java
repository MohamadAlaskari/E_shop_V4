package shp.common.entities;

import java.util.List;
import java.util.Vector;

/**
 * Die Klasse "Kunde" repr�sentiert einen Kunden und erbt von der Klasse
 * "Nutzer". Sie enth�lt Informationen �ber den Kunden wie Kundennummer, Rolle
 * und den Kunden-Warenkorb.
 */

public class Kunde extends Nutzer {
	private int kndNr;
	private static final char rolle = 'k';
	private Warenkorb kundeWarenkorb;
	private List<Bestellung> meineBestellungen = new Vector<>();
	private Bestellung aktuelleBestellung;

	/**
	 * Constructor
	 * 
	 * Der Konstruktor der Klasse initialisiert einen Kunden mit den angegebenen
	 * Werten f�r Name, Vorname, Nutzername, Passwort und Adresse. Dabei wird der
	 * Konstruktor der Basisklasse "Nutzer" aufgerufen und ein neuer
	 * Kunden-Warenkorb f�r den Kunden erstellt.
	 * 
	 * @param kndNr    Die Kundennummer
	 * @param name     Der Name des Kunden
	 * @param vorname  Der Vorname des Kunden
	 * @param nutzerNr Die Nutzernummer des Kunden
	 * @param passwort Das Passwort des Kunden
	 * @param adresse  Die Adresse des Kunden
	 */

	public Kunde(String name, String vorname, String nutzerNr, String passwort, Adresse adresse) {
		super(name, vorname, nutzerNr, passwort, adresse);
		this.kundeWarenkorb = new Warenkorb();
	}

	/**
	 * @param kndNr
	 * @param name
	 * @param vorname
	 * @param nutzerNr
	 * @param passwort
	 * @param adresse
	 * @param kundeWarenkorb
	 * @param meineBestellungen
	 * @param aktuelleBestellung
	 */
	public Kunde(int kndNr, String name, String vorname, String nutzerNr, String passwort, Adresse adresse) {
		super(name, vorname, nutzerNr, passwort, adresse);
		this.kndNr = kndNr;
		this.kundeWarenkorb = new Warenkorb();
	}

	/**
	 * Gibt die Kundennummer zur�ck
	 * 
	 * @return die Kundennummer
	 */
	public int getKndNr() {
		return kndNr;
	}

	/**
	 * Setzt die Kundennummer
	 * 
	 * @param Kundennummer
	 */
	public void setKndNr(int kndNr) {
		this.kndNr = kndNr;
	}

	/**
	 * Gibt die Rolle des Kunden zur�ck
	 * 
	 * @return die rolle
	 */
	public static char getRolle() {
		return rolle;
	}

	/**
	 * Gibt den Warenkorb des Kunden zur�ck
	 * 
	 * @return der kundeWarenkorb
	 */
	public Warenkorb getKundeWarenkorb() {
		return kundeWarenkorb;
	}

	/**
	 * Setzt den Warenkorb des Kunden.
	 * 
	 * @param artikel Der Artikel, der zum Warenkorb hinzugef�gt wird
	 * @param anzahl  Die Anzahl des Artikels
	 */
	public void setKundeWarenkorb(Artikel artikel, int anzahl) {
		this.kundeWarenkorb.setKorbArtikelListe(artikel, anzahl);
		;
	}

	/**
	 * Gibt die Liste der Bestellungen des Kunden zur�ck.
	 * 
	 * @return Die Liste der Bestellungen des Kunden
	 */
	public List<Bestellung> getMeineBestellungen() {
		return meineBestellungen;
	}

	/**
	 * F�gt dem Kunden eine Bestellung hinzu.
	 * 
	 * @param best Die hinzuzuf�gende Bestellung
	 */
	public void setMeineBestellungen(Bestellung best) {
		this.meineBestellungen.add(best);
	}

	/**
	 * Gibt die aktuelle Bestellung des Kunden zur�ck.
	 * 
	 * @return Die aktuelle Bestellung des Kunden
	 */
	public Bestellung getAktuelleBestellung() {
		return aktuelleBestellung;
	}

	/**
	 * Setzt die aktuelle Bestellung des Kunden.
	 * 
	 * @param aktuelleBestellung Die aktuelle Bestellung des Kunden
	 */
	public void setAktuelleBestellung(Bestellung aktuelleBestellung) {
		this.aktuelleBestellung = aktuelleBestellung;
	}

	/**
	 * Gibt die Kundeninformationen dar.
	 * 
	 * @return Die Kundeninformationen des Kunden
	 */

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("________________________\n");
		builder.append("Kunde: ");
		builder.append("\n\tkndNr: " + kndNr);
		builder.append("\n");
		builder.append(super.toString());
		builder.append("\n");

		return builder.toString();
	}

}
