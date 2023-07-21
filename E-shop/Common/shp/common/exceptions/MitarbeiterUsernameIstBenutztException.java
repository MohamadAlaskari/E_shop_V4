package shp.common.exceptions;


import shp.common.entities.Mitarbeiter;

public class MitarbeiterUsernameIstBenutztException extends Exception {
	Mitarbeiter mitarbeiter;
	public MitarbeiterUsernameIstBenutztException(Mitarbeiter mitarbeiter, String zusatzMsg) {
	

				super(" Mitarbeiter Nutzername " + mitarbeiter.getNutzerName()+ " existiert bereits" + zusatzMsg);
				this.mitarbeiter = mitarbeiter;
				
	}
	
	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}

}
