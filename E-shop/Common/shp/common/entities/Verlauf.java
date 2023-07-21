package shp.common.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Die Klasse Verlauf repr�sentiert einen Verlauf einer Aktion in Bezug auf
 * einen bestimmten Artikel und Nutzer.
 */
public class Verlauf {


	private Date date = new Date();
	DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	String formatteddate;
	private Nutzer nutzer;
	private String artikelName;
	private AKTIONSTYP aktion;
	public enum AKTIONSTYP{
		Neue("Mitarbeiter: (Neue Artikel hinzugef�gt)"), 
		ERHOEHEN("Mitarbeiter: (Artikelbestand erh�ht)"),
		BESTELLEN("Kunde: (Kundenbestllung)"),
		SENKEN("Mitarbeiter: (Bestand gesenkt) "),
		LOESCHEN("Mitarbeiter: (Artikel gel�scht)");
		
		private final String value;
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		private AKTIONSTYP(String value){
	         this.value = value;
	        
	     }
		 
	}
	private int aenderungsMenge;
	
	




	/**
	 * 
	 * 
	 * Erstellt einen neuen Verlauf mit der angegebenen Aktion, dem betroffenen
	 * Nutzer, dem betroffenen Artikel und dem formatierten Datum und Uhrzeit.*
	 * 
	 * @param nutzer             der betroffene Nutzer
	 * @param artikel            der betroffene Artikel
	 * @param formattedDatumZeit das formatierte Datum und die formatierte Uhrzeit
	 */
	public Verlauf(AKTIONSTYP aktion, Nutzer nutzer, String artikelName, Date date ,int aenderungsMenge ) {
		this.aktion=aktion;
		this.date = date;
		this.nutzer = nutzer;
		this.artikelName = artikelName;
		this.aenderungsMenge=aenderungsMenge;
		 this.formatteddate = format.format(date);
	}
	
	

	/**
	 * 
	 * Gibt den betroffenen Nutzer zur�ck.*
	 * 
	 * @return der betroffene Nutzer
	 */

	public Nutzer getNutzer() {
		return nutzer;
	}

	/**
	 * 
	 * Setzt den betroffenen Nutzer.*
	 * 
	 * @param nutzer der betroffene Nutzer
	 */
	public void setNutzer(Nutzer nutzer) {
		this.nutzer = nutzer;
	}

	

	/**
	 * 
	 * Gibt den betroffenen Artikel zur�ck.*
	 * 
	 * @return der betroffene Artikel
	 */
	public String getArtikelName() {
		return artikelName;
	}



	/**
	 * @return the aktion
	 */
	public AKTIONSTYP getAktion() {
		return aktion;
	}

	
	

	public int getAenderungsMenge() {
		return aenderungsMenge;
	}



	public void setAenderungsMenge(int aenderungsMenge) {
		this.aenderungsMenge = aenderungsMenge;
	}
	
	

	public Date getDate() {
		return date;
	}



	/**
	 * 
	 * Gibt Alle gespeicherte �nderungen im Verlauf in einen Zeichenkette zur�ck.*
	 * 
	 * @return gespeicherte �nderungen im Verlauf.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("\n�nderung:  ");
		builder.append(aktion.value);
		builder.append(" Name: '");
		builder.append(nutzer.getName());
		builder.append(" ");
		builder.append(nutzer.getVorname());
		builder.append("' hat der Bestand folgende Artikel:  ");
		builder.append("'"+ artikelName+"'");
		builder.append(" ,am ");
		builder.append(formatteddate);
		builder.append(", ge�ndert. ");
		builder.append("Der Bestand des Artikels hat sich um  ");
		builder.append(aenderungsMenge);
		builder.append(" St�ck ge�ndert");

		return builder.toString();
	}

}
