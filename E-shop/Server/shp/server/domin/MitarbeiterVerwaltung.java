package shp.server.domin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import shp.common.entities.Mitarbeiter;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.server.persistence.FilePersistenceManager;
import shp.server.persistence.PersistenceManager;

/*
*Diese Klasse verwaltet Mitarbeiterobjekte und bietet Funktionen zur
*Mitarbeiterverwaltung.
*/
public class MitarbeiterVerwaltung {

	public List<Mitarbeiter> list_Mitarbeiter = new Vector<Mitarbeiter>(); // list mit alle regestrierte Mitarbeiter
	private PersistenceManager pm = new FilePersistenceManager();
	private List<String> mitarbeiterMenue= new ArrayList<>();

	public List<String> mitarbeiterMenue(){
		mitarbeiterMenue.add("Artikel anzeigen");
		mitarbeiterMenue.add("Artikel loeschen");
		mitarbeiterMenue.add("Artikel einfuegen");
		mitarbeiterMenue.add("Artikel suchen");
		mitarbeiterMenue.add("Mitarbeiter regestrieren");
		mitarbeiterMenue.add(" Mitarbeiterliste anzeigen");
		mitarbeiterMenue.add("Artikelbestand erhoehen");
		mitarbeiterMenue.add("Artikelbestand senken");
		mitarbeiterMenue.add("Zeige Verlauf");
		mitarbeiterMenue.add("30Tage Verlauf");
		return this.mitarbeiterMenue;
	}

	/*
	 * Gibt die Liste der Mitarbeiter zur�ck.*
	 * 
	 * @return Die Liste der Mitarbeiter.
	 **/
	public List<Mitarbeiter> getList_Mitarbeiter() {
		return list_Mitarbeiter;
	}

	public Mitarbeiter liesDaten(String datei)
			throws IOException, ArtikelExistiertBereitsException, MitarbeiterUsernameIstBenutztException {
		pm.openForReading(datei);
		Mitarbeiter einMitarbeiter;
		einMitarbeiter = pm.ladeMitarbeiter();
		while (einMitarbeiter != null) {
			list_Mitarbeiter.add(einMitarbeiter);
			einMitarbeiter = pm.ladeMitarbeiter();
		}
		pm.close();
		return einMitarbeiter;
	}

	public void schreibeDaten(String datei) throws IOException {
		pm.openForWriting(datei);
		for (Mitarbeiter mitarbeiter : list_Mitarbeiter) {
			pm.speichereMitarbeiter(mitarbeiter);
		}
		pm.close();
	}

	/**
	 * F�gt einen neuen Mitarbeiter zur Liste hinzu.*
	 * 
	 * @param mitarbeiter Der hinzuzuf�gende Mitarbeiter.
	 * @throws MitarbeiterUsernameIstBenutztException Wenn die Mitarbeiter bereits
	 *                                                verwendet wird.
	 */
	public void fuegeMitarbeiterEin(Mitarbeiter mitarbeiter) throws MitarbeiterUsernameIstBenutztException {
		Iterator<Mitarbeiter> iter = list_Mitarbeiter.iterator();

		while (iter.hasNext()) {
			Mitarbeiter ma = iter.next();
			if (ma.getNutzerName().equals(mitarbeiter.getNutzerName()) || ma.getMaId() == mitarbeiter.getMaId()) {
				throw new MitarbeiterUsernameIstBenutztException(mitarbeiter, "");
			}
		}

		generateMitarbeiterId(mitarbeiter);

		list_Mitarbeiter.add(mitarbeiter);
	}

	/**
	 * 
	 * Generiert eine Mitarbeiter-ID f�r den �bergebenen Mitarbeiter.*
	 * 
	 * @param mitarbeiter Der Mitarbeiter, f�r den eine ID generiert werden soll.
	 */

	private void generateMitarbeiterId(Mitarbeiter mitarbeiter) {
		if (list_Mitarbeiter.isEmpty()) {
			mitarbeiter.setMaId(1);
		} else {
			int lastMitrbeiterID = list_Mitarbeiter.get(list_Mitarbeiter.size() - 1).getMaId();
			mitarbeiter.setMaId(lastMitrbeiterID + 1);
		}
	}

	/**
	 * 
	 * Meldet einen Mitarbeiter mit den angegebenen Anmeldeinformationen an.*
	 * 
	 * @param nutzerName Der Nutzername des Mitarbeiters.
	 * @param passwort   Das Passwort des Mitarbeiters.
	 * @return Der angemeldete Mitarbeiter.
	 * @throws NutzernameOderPasswortFalschException Wenn der Nutzername oder das
	 *                                               Passwort falsch sind.
	 */

	public Mitarbeiter mitarbeiterEinloggen(String nutzerName, String passwort)
			throws NutzernameOderPasswortFalschException {
		// TODO: Implementiere die Methode "einloggen" f�r den Nutzer.
		Iterator<Mitarbeiter> iter = list_Mitarbeiter.iterator();

		Mitarbeiter mitarbeiter = null;

		boolean treffer = false;
		while (iter.hasNext()) {
			Mitarbeiter ma = iter.next();
			if (ma.getNutzerName().equals(nutzerName) && ma.getPasswort().equals(passwort)) {
				mitarbeiter = ma;
				treffer = true;
				break;
			}
		}
		if (!treffer || list_Mitarbeiter.isEmpty()) {
			throw new NutzernameOderPasswortFalschException();
		}

		return mitarbeiter;

	}

	public Mitarbeiter sucheMitarbeiter(String nutzerName) {
		Mitarbeiter suchMitarbeiter = null;
		boolean mitarbeiterGefunden = false;

		if (list_Mitarbeiter.isEmpty()) {
			// Exception to do
			return null;
		} else {
			Iterator<Mitarbeiter> iter = list_Mitarbeiter.iterator();
			while (iter.hasNext()) {
				Mitarbeiter m = iter.next();
				if (m.getNutzerName().equals(nutzerName)) {
					suchMitarbeiter = m;
					mitarbeiterGefunden = true;
					break;
				}
			}
		}
		if (!mitarbeiterGefunden) {
			// Exception to do
			return null;
		}

		return suchMitarbeiter;
	}

	public void mitarbeiterAusloggen(Mitarbeiter mitarbeiter) {

		mitarbeiter = null;

	}

	/**
	 * 
	 * Registriert einen neuen Mitarbeiter mit den angegebenen Informationen.*
	 * 
	 * @param name       Der Name des Mitarbeiters.
	 * @param vorName    Der Vorname des Mitarbeiters.
	 * @param nutzerName Der Nutzername des Mitarbeiters.
	 * @param passwort   Das Passwort des Mitarbeiters.
	 * @throws MitarbeiterUsernameIstBenutztException Wenn die Mitarbeiter-ID
	 *                                                bereits verwendet wird.
	 */

	public void neueMitarbeiterRegistieren(String name, String vorName, String nutzerName, String passwort)
			throws MitarbeiterUsernameIstBenutztException {
		Mitarbeiter mitarbeiter = new Mitarbeiter(name, vorName, nutzerName, passwort);
		fuegeMitarbeiterEin(mitarbeiter);
	}
}
