package shp.common.entities;

/**
 * Eine abstrakte Klasse, die einen Nutzer repr�sentiert.
 */

public abstract class Nutzer {
	private String name;
	private String vorname;
	private String nutzerName;
	private String passwort;
	private Adresse adresse;

	/**
	 * Konstruktor f�r den Nutzer mit Adresse.
	 * 
	 * @param name     Der Name des Nutzers.
	 * @param vorname  Der Vorname des Nutzers.
	 * @param nutzerNr Die Nutzernummer des Nutzers.
	 * @param passwort Das Passwort des Nutzers.
	 * @param adresse  Die Adresse des Nutzers.
	 */
	public Nutzer(String name, String vorname, String nutzerNr, String passwort, Adresse adresse) {
		this.name = name;
		this.vorname = vorname;
		this.nutzerName = nutzerNr;
		this.passwort = passwort;
		this.adresse = adresse;
	}
	/**
	 * Konstruktor f�r den Nutzer ohne Adresse.
	 * 
	 * @param name     Der Name des Nutzers.
	 * @param vorname  Der Vorname des Nutzers.
	 * @param nutzerNr Die Nutzernummer des Nutzers.
	 * @param passwort Das Passwort des Nutzers.
	 */
	

	public Nutzer(String name, String vorname, String nutzerNr, String passwort) {
		this.name = name;
		this.vorname = vorname;
		this.nutzerName = nutzerNr;
		this.passwort = passwort;

	}

	/**
	 * Gibt den Namen des Nutzers zur�ck.
	 * 
	 * @return Der Name des Nutzers.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen des Nutzers.
	 * 
	 * @param name Der Name des Nutzers.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt den Vornamen des Nutzers zur�ck.
	 * 
	 * @return Der Vorname des Nutzers.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Setzt den Vornamen des Nutzers.
	 * 
	 * @param vorname Der Vorname des Nutzers.
	 */
	public void setVorname(String vorname) {
		vorname = vorname;
	}

	/**
	 * Gibt die Nutzernummer des Nutzers zur�ck.
	 * 
	 * @return Die Nutzernummer des Nutzers.
	 */
	public String getNutzerName() {
		return nutzerName;
	}

	/**
	 * Setzt die Nutzernummer des Nutzers.
	 * 
	 * @param nutzerNr Die Nutzernummer des Nutzers.
	 */
	public void setNutzerName(String nutzerNr) {
		nutzerNr = nutzerNr;
	}

	/**
	 * Gibt das Passwort des Nutzers zur�ck.
	 * 
	 * @return Das Passwort des Nutzers.
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * Setzt das Passwort des Nutzers.
	 * 
	 * @param passwort Das Passwort des Nutzers.
	 */
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	/**
	 * Gibt die Adresse des Nutzers zur�ck.
	 * 
	 * @return Die Adresse des Nutzers.
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	/**
	 * Gibt eine formatierte Zeichenkette mit allen Informationen des Nutzers
	 * zur�ck.
	 * 
	 * @return Eine formatierte Zeichenkette mit allen Informationen des Nutzers.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\tName: ");
		builder.append(name);
		builder.append(", Vorname: ");
		builder.append(vorname);
		builder.append(", NutzerName: ");
		builder.append(nutzerName);
		builder.append("");
		return builder.toString();
	}

}
