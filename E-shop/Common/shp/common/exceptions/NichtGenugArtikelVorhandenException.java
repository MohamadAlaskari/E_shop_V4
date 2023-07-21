package shp.common.exceptions;

import shp.common.entities.Artikel;

public class NichtGenugArtikelVorhandenException extends Exception {

	public NichtGenugArtikelVorhandenException(Artikel art) {
		super("Die gew�hlte Menge von den " + art.getName()
				+ " ist leider nicht vorhanden oder die Artikel ist Ausverkauft. Sie k�nnen max. " + art.getBestand()
				+ " St�ck w�hlen");

	}
	
	public NichtGenugArtikelVorhandenException(int anzahl, Artikel art) {
		super("Die gew�hlte Menge von den " + art.getName()
				+ " ist leider nicht vorhanden oder die Artikel ist Ausverkauft. Sie k�nnen max. " + (art.getBestand()-anzahl)
				+ " St�ck w�hlen");

	}

}
