package shp.common.entities;

import java.util.HashMap;

/**
 * Die Klasse "Warenkorb" repr�sentiert den Warenkorb eines Kunden. Sie enth�lt
 * eine Liste von Artikeln mit ihrer jeweiligen Anzahl und den Gesamtpreis.
 */
public class Warenkorb {

	private HashMap<Artikel, Integer> korbArtikelListe;
	private double gesamtPrise = 0.0;

	/**
	 * Konstruktor der Klasse Warenkorb. Erzeugt eine neue Instanz des Warenkorbs
	 * mit einer leeren Artikel-Liste.
	 */
	public Warenkorb() {
		this.korbArtikelListe = new HashMap<>();
	}

	/**
	 * Gibt die Liste der Artikel im Warenkorb zur�ck.
	 * 
	 * @return die Artikel-Liste des Warenkorbs
	 */
	public HashMap<Artikel, Integer> getKorbArtikelListe() {
		return korbArtikelListe;
	}

	/**
	 * Setzt die Liste der Artikel im Warenkorb.
	 * 
	 * @param art    der Artikel, der hinzugef�gt werden soll
	 * @param anzahl die Anzahl des Artikels, die hinzugef�gt werden soll
	 */
	public void setKorbArtikelListe(Artikel art, int anzahl) {
		this.korbArtikelListe.put(art, anzahl);
	}

	/**
	 * Gibt den Gesamtpreis des Warenkorbs zur�ck.
	 * 
	 * @return der Gesamtpreis des Warenkorbs
	 */
	public double getGesamtPrise() {
		return gesamtPrise;
	}

	/**
	 * Setzt den Gesamtpreis des Warenkorbs.
	 * 
	 * @param gesamtPreis der neue Gesamtpreis des Warenkorbs
	 */
	public void setGesamtPrise(double gesamtPrise) {
		this.gesamtPrise = gesamtPrise;
	}

	/**
	 * Gibt die Warenkorbformationen dar.
	 * 
	 * @return Die Warenkorbformationen des Artikel
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Warenkorb: \n-------------- \n");
		int i = 1;
		for (Artikel artikel : korbArtikelListe.keySet()) {

			int anzahlArtikelWK = korbArtikelListe.get(artikel);
			Artikel warenkorpartikel = artikel;
			builder.append(i + ". artikel: " + warenkorpartikel.getName() + ", menge: " + anzahlArtikelWK + "\n");
			i++;
		}

		builder.append("Die Gesamtsumme beträgt: " + gesamtPrise + " € \n");
		return builder.toString();
	}

}
