package shp.common.entities;

/**
 * Die Klasse "Artikel" repr�sentiert einen Artikel und enth�lt Informationen
 * wie Artikel-ID, Name, Beschreibung, Bestand, Preis und Verf�gbarkeit.
 */
public class Artikel {
	private int artikelId;
	private String name;
	private String beschreibung;
	private int bestand;
	private double preis;
	private boolean verfuegbar;
	private boolean istPackung = false;

	

	/**
	 * Konstruktor1 f�r die Klasse Artikel. Erzeugt einen Artikel mit den
	 * angegebenen Werten f�r Name, Beschreibung, Bestand und Preis. Der Artikel
	 * wird standardm��ig als verf�gbar markiert.
	 * 
	 * @param name         der Name des Artikels
	 * @param beschreibung die Beschreibung des Artikels
	 * @param bestand      der Bestand des Artikels
	 * @param preis        der Preis des Artikels
	 */
	public Artikel(String name, String beschreibung, int bestand, double preis, boolean istPackung) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.bestand = bestand;
		this.preis = preis;
		this.verfuegbar = true;
		this.istPackung = istPackung;
	}

	/**
	 * Konstruktor2 f�r die Klasse Artikel. Erzeugt einen Artikel mit den
	 * angegebenen Werten f�r Name, Beschreibung, Bestand, Preis und Verf�gbarkeit.
	 * 
	 * @param name         der Name des Artikels
	 * @param beschreibung die Beschreibung des Artikels
	 * @param bestand      der Bestand des Artikels
	 * @param preis        der Preis des Artikels
	 * @param verfuegbar   die Verf�gbarkeit des Artikels
	 */
	
	public Artikel(int id, String name, String beschreibung, int bestand, double preis, boolean verfuegbar, boolean istPackung) {
		this.artikelId= id;
		this.name = name;
		this.beschreibung = beschreibung;
		this.bestand = bestand;
		this.preis = preis;
		this.verfuegbar = verfuegbar;
		this.istPackung = istPackung;
	}

	/**
	 * Gibt die Artikel-ID zur�ck.
	 * 
	 * @return die Artikel-ID
	 */
	public int getArtikelId() {
		return artikelId;
	}

	/**
	 * Gibt den Namen des Artikels zur�ck.
	 * 
	 * @return der Name des Artikels
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Beschreibung des Artikels zur�ck.
	 * 
	 * @return die Beschreibung des Artikels
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * Gibt den Bestand des Artikels zur�ck.
	 * 
	 * @return der Bestand des Artikels
	 */
	public int getBestand() {
		return bestand;
	}

	/**
	 * Gibt die Verf�gbarkeit des Artikels zur�ck.
	 * 
	 * @return true, wenn der Artikel verf�gbar ist, andernfalls false
	 */
	public boolean isVerfuegbar() {
		return verfuegbar;
	}

	/**
	 * Setzt den Bestand des Artikels.
	 * 
	 * @param bestand der neue Bestand des Artikels
	 */
	public void setBestand(int bestand) {
		this.bestand = bestand;
	}

	/**
	 * Setzt die Verf�gbarkeit des Artikels.
	 * 
	 * @param verfuegbar die neue Verf�gbarkeit des Artikels
	 */
	public void setVerfuegbar(boolean verfuegbar) {
		this.verfuegbar = verfuegbar;
	}

	/**
	 * Setzt die Artikel-ID.
	 * 
	 * @param artikelId die neue Artikel-ID
	 */
	public void setArtikelId(int artikelId) {
		this.artikelId = artikelId;
	}

	/**
	 * Setzt den Namen des Artikels.
	 * 
	 * @param name der neue Name des Artikels
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Setzt die Beschreibung des Artikels.
	 * 
	 * @param beschreibung die neue Beschreibung des Artikels
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * Setzt den Preis des Artikels.
	 * 
	 * @param preis der neue Preis des Artikels
	 */
	public void setPreis(double preis) {
		this.preis = preis;
	}

	/**
	 * Gibt den Preis des Artikels zur�ck.
	 * 
	 * @return der Preis des Artikels
	 */
	public double getPreis() {
		return preis;
	}

	
	
	
	public boolean getIstPackung() {
		return istPackung;
	}

	public void setIstPackung(boolean istPackung) {
		this.istPackung = istPackung;
	}
	
	
	/**
	 * �berpr�ft, ob der Artikel mit einem anderen Artikelobjekt identisch ist.
	 * 
	 * @param andereArtikel das andere Artikelobjekt, mit dem verglichen wird
	 * @return true, wenn die Artikel identisch sind, andernfalls false
	 */
	public boolean equals(Object andereArtikel) {
		if (andereArtikel instanceof Artikel) {
			return ((this.artikelId == ((Artikel) andereArtikel).artikelId)
					&& (this.beschreibung.equals(((Artikel) andereArtikel).beschreibung)
							&& (this.name.equals(((Artikel) andereArtikel).name))
							&& (this.bestand == ((Artikel) andereArtikel).bestand)
							&& (this.preis == ((Artikel) andereArtikel).preis)));
		} else {
			return false;
		}

	}

	/**
	 * Gibt die Artikelformationen dar.
	 * 
	 * @return Die Artikelformationen des Artikel
	 */

	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(ArtikelId = " + artikelId);
		builder.append(", Name = " + name);
		builder.append(", Beschreibung = " + beschreibung);
		builder.append(", Bestand = " + bestand);
		builder.append(", Preis = " + preis);
		builder.append(", Verfügbarkeit = " + verfuegbar);
		builder.append(", Massengutartikel = " + istPackung );
		builder.append((istPackung ? "" : ")"));
		builder.append("\n");

		return builder.toString();

	}

}
