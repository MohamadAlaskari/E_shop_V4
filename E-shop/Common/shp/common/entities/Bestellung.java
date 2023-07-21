package shp.common.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class Bestellung {
	/**
	 * Die Klasse "Bestellung" repr�sentiert eine Kundenbestellung. Sie enth�lt
	 * Informationen wie Bestellungsnummer, Kundendaten, bestellte Artikel und den
	 * Gesamtpreis etc.
	 */

	
	private String formattedDatumZeit;
	private int bestellungsNr;
	private Kunde kunde;
	private HashMap<Artikel, Integer> bestellteArtikeln;
	private double Gesamtprise = 0;
	private Rechnung rechnung;

	/**
	 * Konstruktor der Klasse Bestellung. Erzeugt eine neue Instanz einer Bestellung
	 * mit den angegebenen Parametern.
	 * 
	 * @param kunde             der Kunde, der die Bestellung aufgibt
	 * @param bestellteArtikeln die Liste der bestellten Artikel und ihrer
	 *                          jeweiligen Anzahl
	 * @param gesamtPreis       der Gesamtpreis der Bestellung
	 */

	public Bestellung(Kunde kunde, HashMap<Artikel, Integer> bestellteArtikeln, double gesamtprise, String formattedDatumZeit) {
		this.formattedDatumZeit = formattedDatumZeit;
		this.bestellteArtikeln = bestellteArtikeln;
		this.kunde = kunde;
		this.Gesamtprise = gesamtprise;
	}

	/**
	 * Gibt die Liste der bestellten Artikel zur�ck.
	 * 
	 * @return die Liste der bestellten Artikel
	 */
	public HashMap<Artikel, Integer> getBestellteArtikeln() {
		return bestellteArtikeln;
	}

	/**
	 * Setzt die Liste der bestellten Artikel.
	 * 
	 * @param bestellteArtikeln die Liste der bestellten Artikel
	 */
	public void setBestellteArtikeln(HashMap<Artikel, Integer> bestellteArtikeln) {
		this.bestellteArtikeln = bestellteArtikeln;
	}

	/**
	 * Gibt den Kunden der Bestellung zur�ck.
	 * 
	 * @return der Kunde der Bestellung
	 */
	public Kunde getKunde() {
		return kunde;
	}

	/**
	 * Setzt den Kunden der Bestellung.
	 * 
	 * @param kunde der Kunde der Bestellung
	 */
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	/**
	 * Gibt das formatierte Datum und die Uhrzeit der Bestellung zur�ck.
	 * 
	 * @return das formatierte Datum und die Uhrzeit der Bestellung
	 */
	public String getFormattedDatumZeit() {
		return formattedDatumZeit;
	}

	/**
	 * Gibt den Gesamtpreis der Bestellung zur�ck.
	 * 
	 * @return der Gesamtpreis der Bestellung
	 */
	public double getGesamtprise() {
		return Gesamtprise;
	}

	/**
	 * Setzt den Gesamtpreis der Bestellung.
	 * 
	 * @param gesamtPreis der Gesamtpreis der Bestellung
	 */
	public void setGesamtprise(double gesamtprise) {
		Gesamtprise = gesamtprise;
	}

	/**
	 * Gibt die Bestellungsnummer zur�ck.
	 * 
	 * @return die Bestellungsnummer
	 */
	public int getBestellungsNr() {
		return bestellungsNr;
	}

	/**
	 * Setzt die Bestellungsnummer.
	 * 
	 * @param bestellungsNr die Bestellungsnummer
	 */
	public void setBestellungsNr(int bestellungsNr) {
		this.bestellungsNr = bestellungsNr;
	}

	/**
	 * Gibt die Rechnung der Bestellung zur�ck.
	 * 
	 * @return die Rechnung der Bestellung
	 */
	public Rechnung getRechnung() {
		return rechnung;
	}

	/**
	 * Setzt die Rechnung der Bestellung.
	 * 
	 * @param rechnung die Rechnung der Bestellung
	 */

	public void setRechnung(Rechnung rechnung) {
		this.rechnung = rechnung;
	}
	
	/**
	 * Gibt die Bestellungsiformationen dar.
	 * 
	 * @return Die Bestellungsiformationen des Kunde
	 */

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bestellung:  am \n\t");
		builder.append(formattedDatumZeit);
		builder.append(", kundeNr.= ");
		builder.append(kunde.getKndNr());
		builder.append("\nBstellteArtikeln: \n");

		int i = 1;
		for (Artikel best : bestellteArtikeln.keySet()) {

			int anzahlArtikel = bestellteArtikeln.get(best);
			Artikel bestellungArtikel = best;
			builder.append("\t" + i + ". artikel: " + bestellungArtikel.getName() + ", menge: " + anzahlArtikel + "\n");
			i++;
		}

		return builder.toString();
	}

}
