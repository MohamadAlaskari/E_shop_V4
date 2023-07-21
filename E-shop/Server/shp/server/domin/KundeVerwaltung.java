package shp.server.domin;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import shp.common.entities.Artikel;
import shp.common.entities.Bestellung;
import shp.common.entities.Kunde;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.KundeUsernameIstbenutztException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.server.persistence.FilePersistenceManager;
import shp.server.persistence.PersistenceManager;

/**
 * 
 * Diese Klasse verwaltet Kundendaten und bietet Funktionen zur
 * Kundenverwaltung.
 */
public class KundeVerwaltung {
	private List<Kunde> list_Kunde = new Vector<Kunde>(); // list mit alle Kunden 
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * 
	 * Registriert einen neuen Kunden.*
	 * 
	 * @param kunde Der zu registrierende Kunde.
	 * @throws KundeUsernameIstbenutztException Wenn die Kunden-ID bereits verwendet
	 *                                          wird.
	 */
	public void kundeRegistieren(Kunde kunde) throws KundeUsernameIstbenutztException {
		Iterator<Kunde> iter = list_Kunde.iterator();

		while (iter.hasNext()) {
			Kunde ku = iter.next();
			if (ku.getNutzerName().equals(kunde.getNutzerName()) || ku.getKndNr() == kunde.getKndNr()) {
				throw new KundeUsernameIstbenutztException(kunde, "");
			}
		}
		generateKundenId(kunde);
		list_Kunde.add(kunde);
	}

	/**
	 * 
	 * Generiert eine eindeutige Kunden-ID f�r den �bergebenen Kunden.*
	 * 
	 * @param kunde Der Kunde, f�r den eine ID generiert werden soll.
	 */
	private void generateKundenId(Kunde kunde) {
		if (list_Kunde.isEmpty()) {
			kunde.setKndNr(100);
		} else {
			int lastKundenNr = list_Kunde.get(list_Kunde.size() - 1).getKndNr();
			kunde.setKndNr(lastKundenNr + 2);
		}
	}

	/**
	 * 
	 * Meldet einen Kunden mit den angegebenen Anmeldeinformationen an.*
	 * 
	 * @param nutzerName Der Nutzername des Kunden.
	 * @param passwort   Das Passwort des Kunden.
	 * @return Der angemeldete Kunde.
	 * @throws NutzernameOderPasswortFalschException Wenn der Nutzername oder das
	 *                                               Passwort falsch sind.
	 */
	public Kunde kundeEinloggen(String nutzerName, String passwort) throws NutzernameOderPasswortFalschException {
		// TODO: Implementiere die Methode "einloggen" f�r den Nutzer.
		ListIterator<Kunde> iter = list_Kunde.listIterator();

		Kunde kunde = null;
		boolean treffer = false;
		while (iter.hasNext()) {
			Kunde ku = iter.next();
			
			if (ku.getNutzerName().equals(nutzerName) && ku.getPasswort().equals(passwort)) {
				kunde = ku;
				treffer = true;
				break;
			}
		}
		if (!treffer || list_Kunde.isEmpty()) {
			throw new NutzernameOderPasswortFalschException();
		}

		return kunde;

	}

	public Kunde sucheKunde(String nutzerName) {
		Kunde suchKunde = null;
		boolean kundeGefunden = false;

		if (list_Kunde.isEmpty()) {
			// Exception to do
			return null;
		} else {
			Iterator<Kunde> iter = list_Kunde.iterator();
			while (iter.hasNext()) {
				Kunde k = iter.next();
				if (k.getNutzerName().equals(nutzerName)) {
					suchKunde = k;
					kundeGefunden = true;
					break;
				}
			}
		}
		if (!kundeGefunden) {
			// Exception to do
			return null;
		}

		return suchKunde;
	}

	public void kundeAusloggen(Kunde kunde) {
		kunde = null;
	}

	/**
	 * 
	 * Gibt die Liste der Kunden zur�ck.*
	 * 
	 * @return Die Liste der Kunden.
	 */
	public List<Kunde> getList_Kunde() {
		return list_Kunde;
	}

	/**
	 * 
	 * Gibt die Bestellungen des angegebenen Kunden zur�ck.*
	 * 
	 * @param kunde Der Kunde, dessen Bestellungen abgerufen werden sollen.
	 * @return Eine Liste der Bestellungen des Kunden.
	 */
	public List<Bestellung> getMeineBestellungen(Kunde kunde) {
		return kunde.getMeineBestellungen();
	}

	//
	public Kunde liesDaten(String datei) throws IOException {
		pm.openForReading(datei);
		Kunde einKunde;
		einKunde = pm.ladeKunde();
		
		while (einKunde != null) {
			list_Kunde.add(einKunde);
			einKunde = pm.ladeKunde();
			
		}
		pm.close();
		return einKunde;
	}

	public void schreibeDaten(String datei) throws IOException {
		pm.openForWriting(datei);
		for (Kunde kunde : list_Kunde) {
			
			pm.speichereKunde(kunde);
		}
		pm.close();
	}

}
