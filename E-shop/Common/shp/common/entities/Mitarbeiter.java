package shp.common.entities;

/**
 * Die Klasse "Mitarbeiter" repr�sentiert einen Mitarbeiter und erbt von der
 * Klasse "Nutzer". Sie enth�lt Informationen �ber den Mitarbeiter wie
 * MitarbeiterID und Rolle.
 */

public class Mitarbeiter extends Nutzer {
	private int maId;
	private static final char rolle = 'm';

	/**
	 * Konstruktor1 f�r die Klasse Mitarbeiter.
	 * 
	 * Der Konstruktor initialisiert einen Mitarbeiter mit den angegebenen Werten
	 * f�r Name, Vorname, Nutzername, Passwort und Adresse. Dabei wird der
	 * Konstruktor der Basisklasse "Nutzer" aufgerufen.
	 * 
	 * @param name     Der Name des Mitarbeiters.
	 * @param vorname  Der Vorname des Mitarbeiters.
	 * @param nutzerNr Die Nutzernummer des Mitarbeiters.
	 * @param passwort Das Passwort des Mitarbeiters.
	 * @param adresse  Die Adresse des Mitarbeiters.
	 */

	public Mitarbeiter(String name, String vorname, String nutzerNr, String passwort, Adresse adresse) {
		super(name, vorname, nutzerNr, passwort, adresse);
	}

	/**
	 * Konstruktor 2 f�r die Klasse Mitarbeiter ohne Adresse.
	 * 
	 * Der Konstruktor initialisiert einen Mitarbeiter mit den angegebenen Werten
	 * f�r Name, Vorname, Nutzername und Passwort. Dabei wird der Konstruktor der
	 * Basisklasse "Nutzer" aufgerufen.
	 * 
	 * @param name     Der Name des Mitarbeiters.
	 * @param vorname  Der Vorname des Mitarbeiters.
	 * @param nutzerNr Die Nutzernummer des Mitarbeiters.
	 * @param passwort Das Passwort des Mitarbeiters.
	 */

	public Mitarbeiter(String name, String vorname, String nutzerNr, String passwort) {
		super(name, vorname, nutzerNr, passwort);
	}

	/**
	 * Konstruktor 2 f�r die Klasse Mitarbeiter ohne Adresse.
	 * 
	 * Der Konstruktor initialisiert einen Mitarbeiter mit den angegebenen Werten
	 * f�r Name, Vorname, Nutzername und Passwort. Dabei wird der Konstruktor der
	 * Basisklasse "Nutzer" aufgerufen.
	 * 
	 * @param name     Der Name des Mitarbeiters.
	 * @param vorname  Der Vorname des Mitarbeiters.
	 * @param nutzerNr Die Nutzernummer des Mitarbeiters.
	 * @param passwort Das Passwort des Mitarbeiters.
	 */

	public Mitarbeiter(int maId,String name, String vorname, String nutzerNr, String passwort) {
		super(name, vorname, nutzerNr, passwort);
		this.maId=maId;
	}
	/**
	 * Gibt die Mitarbeiter-ID zur�ck.
	 * 
	 * @return Die Mitarbeiter-ID.
	 */
	public int getMaId() {
		return maId;
	}

	/**
	 * Setzt die Mitarbeiter-ID.
	 * 
	 * @param maId Die Mitarbeiter-ID.
	 */
	public void setMaId(int maId) {
		this.maId = maId;
	}

	/**
	 * Gibt die Rolle des Mitarbeiters zur�ck.
	 * 
	 * @return Die Rolle des Mitarbeiters.
	 */
	public static char getRolle() {
		return rolle;
	}

	/**
	 * Gibt die Mitarbeiterformationen dar.
	 * 
	 * @return Die Mitarbeiterformationen des Mitarbeiters
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mitarbeiter ");
		builder.append(super.toString());
		builder.append(" maId:");
		builder.append(maId);
		builder.append("\n");
		return builder.toString();
	}

}
