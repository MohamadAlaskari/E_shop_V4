package shp.common.entities;



/**
 * 
 * Die Klasse RechnungsVerwaltung verwaltet die Erstellung und Verwaltung von
 * Rechnungen.
 */
public class Rechnung {

	private double gesamtPreis;
	private int rechnungNr;
	private Bestellung bestellung;
	private String formattedDatumZeit;

	/**
	 * Konstruktor f�r die Rechnung.
	 * 
	 * @param bestellung Die Bestellung, f�r die die Rechnung erstellt wird.
	 */
	public Rechnung(Bestellung bestellung, String formattedDatumZeit) {
		this.formattedDatumZeit = formattedDatumZeit;
		this.bestellung = bestellung;

	}

	/**
	 * Gibt die Bestellung zur�ck, f�r die die Rechnung erstellt wurde.
	 * 
	 * @return Die Bestellung.
	 */
	public Bestellung getBestellung() {
		return bestellung;
	}

	/**
	 * Setzt die Bestellung, f�r die die Rechnung erstellt wird.
	 * 
	 * @param bestellung Die Bestellung.
	 */
	public void setBestellung(Bestellung bestellung) {
		this.bestellung = bestellung;
	}

	/**
	 * Gibt den Gesamtpreis der Rechnung zur�ck.
	 * 
	 * @return Der Gesamtpreis der Rechnung.
	 */
	public double getGesamtPreis() {
		return gesamtPreis;
	}

	/**
	 * Setzt den Gesamtpreis der Rechnung.
	 * 
	 * @param gesamtPreis Der Gesamtpreis der Rechnung.
	 */
	public void setGesamtPreis(double gesamtPreis) {
		this.gesamtPreis = gesamtPreis;
	}

	/**
	 * Gibt die Rechnungsnummer zur�ck.
	 * 
	 * @return Die Rechnungsnummer.
	 */
	public int getRechnungNr() {
		return rechnungNr;
	}

	/**
	 * Setzt die Rechnungsnummer.
	 * 
	 * @param rechnungNr Die Rechnungsnummer.
	 */
	public void setRechnungNr(int rechnungNr) {
		this.rechnungNr = rechnungNr;
	}

	/**
	 * Gibt das formatierte Datum und die Uhrzeit der Rechnung zur�ck.
	 * 
	 * @return Das formatierte Datum und die Uhrzeit der Rechnung.
	 */
	public String getFormattedDatumZeit() {
		return formattedDatumZeit;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rechnung: am " + formattedDatumZeit);
		builder.append(" RechnungNr: " + rechnungNr);
		builder.append("\n\t");
		builder.append(bestellung.getKunde().getVorname());
		builder.append(" ");
		builder.append(bestellung.getKunde().getName());
		builder.append(bestellung.getKunde().getAdresse());
		builder.append("\n\n\tBestellungsNr.: " + bestellung.getBestellungsNr());
		builder.append("\n");
		int i = 1;
		for (Artikel best : bestellung.getBestellteArtikeln().keySet()) {
			int anzahlArtikel = bestellung.getBestellteArtikeln().get(best);
			Artikel bestellungArtikel = best;
			
			builder.append(
					"\t\t" + i + ". artikel: " + bestellungArtikel.getName() + ", menge: " + anzahlArtikel + "\n");
			i++;
		}
		builder.append("\n\t Summe: " + bestellung.getGesamtprise());
		builder.append("€ ink. 19% mwst");

		return builder.toString();
	}

}
