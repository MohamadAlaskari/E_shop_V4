package shp.common.exceptions;

import shp.common.entities.Artikel;


	

public class SenkenUnterNullNichtMoeglichException extends Exception {

	private Artikel artikel;

	public SenkenUnterNullNichtMoeglichException(Artikel artikel , int anzahl) {
		// TODO Auto-generated constructor stub

		super("Artikel: '" + artikel.getName() + "'" + " kann nicht um '" + anzahl +"' gesunken werden, da der Bestand '"+artikel.getBestand()+"' beträgt");
		this.artikel = artikel;
	}

	public Artikel getArtikel() {
		return artikel;
	}

}
