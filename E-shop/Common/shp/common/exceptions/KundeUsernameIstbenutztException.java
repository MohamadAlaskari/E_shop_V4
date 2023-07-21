package shp.common.exceptions;

import shp.common.entities.Kunde;


public class KundeUsernameIstbenutztException extends Exception{


	// TODO Auto-generated constructor stub
	Kunde kunde;
	public KundeUsernameIstbenutztException(Kunde kunde, String zusatzMsg) {


		super("Kunde: Nutzername " + kunde.getNutzerName()+ " existiert bereits " + zusatzMsg);
		this.kunde = kunde;
	}

	public Kunde getKunde() {
		return kunde;
	}
}


