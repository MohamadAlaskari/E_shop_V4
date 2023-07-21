package shp.server.domin;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import shp.common.entities.Artikel;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.exceptions.AnzahlIsNichtDefiniertException;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
import shp.server.persistence.FilePersistenceManager;
import shp.server.persistence.PersistenceManager;

/**
 * to Diese Klasse verwaltet Artikel und bietet Funktionen zur
 * Artikelverwaltung.
 */
public class ArtikelVerwaltung {

	private Vector<Artikel> artikelListe = new Vector<>(); // list mit alle eingef�gte Artikeln

	private PersistenceManager pm = new FilePersistenceManager();

	public void liesDaten(String datei) throws IOException, ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {
		pm.openForReading(datei);
		Artikel einArtikel;
		einArtikel = pm.ladeArtikel();
		while (einArtikel != null) {
			fugeArtikelEin(einArtikel);
			einArtikel = pm.ladeArtikel();
		}
		pm.close();
	}

	public void schreibeDaten(String datei) throws IOException {
		pm.openForWriting(datei);
		for (Artikel artikel : artikelListe) {
			pm.speichereArtikel(artikel);
		}
		pm.close();
	}

	/**
	 * 
	 * F�gt einen Artikel mit der angegebenen Anzahl hinzu.*
	 * 
	 * @param artikel Der hinzuzuf�gende Artikel.
	 * @param anzahl  Die Anzahl des Artikels.
	 * @throws AnzahlIsNichtDefiniertException Wenn die Anzahl nicht definiert ist.
	 */

	/**
	 * 
	 * F�gt einen Artikel ohne Angabe der Anzahl hinzu. Artikel kriegt bei einf�gen
	 * Bestand* und man braucht nicht immer anzahl einzugeben bei der eingabe
	 * addiert es sich mit dem Bestand
	 * 
	 * @param artikel Der hinzuzuf�gende Artikel.
	 * @throws ArtikelExistiertNichtException
	 * @throws AnzahlIsNichtDefiniertException Wenn die Anzahl nicht definiert ist.
	 */
	public void fugeArtikelEin(Artikel artikel) throws ArtikelExistiertBereitsException,
			BestandPasstNichtMitPackungsGroesseException, ArtikelExistiertNichtException {
		Iterator<Artikel> iter = artikelListe.iterator();

		while (iter.hasNext()) {
			Artikel art = iter.next();
			if (art.getName().equals(artikel.getName())) {
				throw new ArtikelExistiertBereitsException(artikel, ". Sie k�nnen sonst die Artikel Bestand �ndern ");
			}
		}
		if (checkMassengutartikel(artikel)) {

			if (CheckModulo(artikelZuMassengutartikel(artikel).getBestand(),
					artikelZuMassengutartikel(artikel).getPackungsGroesse())) {
				genertaeArtiekelNr(artikelZuMassengutartikel(artikel));
				artikelListe.add(artikelZuMassengutartikel(artikel));
				updateVerfuegbarkeit(artikelZuMassengutartikel(artikel));
			} else {
				throw new BestandPasstNichtMitPackungsGroesseException(artikelZuMassengutartikel(artikel), "");
			}

		} else {
			genertaeArtiekelNr(artikel);
			artikelListe.add(artikel);
			updateVerfuegbarkeit(artikel);
		}

	}

	public static boolean CheckModulo(int bestandt, int packungsgroesse) {
		if (bestandt % packungsgroesse == 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * Generiert eine eindeutige Artikel-ID f�r den �bergebenen Artikel.*
	 * 
	 * @param artikel Der Artikel, f�r den eine ID generiert werden soll.
	 */

	private void genertaeArtiekelNr(Artikel artikel) {
		if (artikelListe.isEmpty()) {
			artikel.setArtikelId(1322);
		} else {
			int lastRechnungNr = artikelListe.get(artikelListe.size() - 1).getArtikelId();
			artikel.setArtikelId(lastRechnungNr + 2);
		}
	}

	/**
	 * 
	 * Erh�ht den Bestand des Artikels mit dem angegebenen Namen um die angegebene
	 * Anzahl.*
	 * 
	 * @param name   Der Name des Artikels.
	 * @param anzahl Die Anzahl, um die der Bestand erh�ht werden soll.
	 * @throws ArtikelExistiertNichtException
	 * @throws BestandPasstNichtMitPackungsGroesseException
	 */

	public Artikel bestandErhoehen(String name, int anzahl)
			throws ArtikelExistiertNichtException, BestandPasstNichtMitPackungsGroesseException {
		Artikel artikel = sucheArtikel(name);
		if (checkMassengutartikel(artikel)) {

			if (CheckModulo(anzahl, artikelZuMassengutartikel(artikel).getPackungsGroesse())) {
				artikel.setBestand(artikel.getBestand() + anzahl);
				updateVerfuegbarkeit(artikel);
			} else {
				throw new BestandPasstNichtMitPackungsGroesseException(anzahl, artikelZuMassengutartikel(artikel), "");
			}
		} else {
			artikel.setBestand(artikel.getBestand() + anzahl);
			updateVerfuegbarkeit(artikel);
		}

		return artikel;

	}

	/**
	 * 
	 * Verringert den Bestand des Artikels mit dem angegebenen Namen um die
	 * angegebene Anzahl.*
	 * 
	 * @param name   Der Name des Artikels.
	 * @param anzahl Die Anzahl, um die der Bestand verringert werden soll.
	 * @throws ArtikelExistiertNichtException
	 * @throws BestandPasstNichtMitPackungsGroesseException
	 * @throws SenkenUnterNullNichtMoeglichException
	 */
	public Artikel bestandSenken(String name, int anzahl) throws ArtikelExistiertNichtException,
			BestandPasstNichtMitPackungsGroesseException, SenkenUnterNullNichtMoeglichException {
		Artikel artikel = sucheArtikel(name);
		int neueMenge = artikel.getBestand() - anzahl;
		if (neueMenge < 0) {
			throw new SenkenUnterNullNichtMoeglichException(artikel, anzahl);
		} else {
			if (checkMassengutartikel(artikel)) {
				if (CheckModulo(anzahl, artikelZuMassengutartikel(artikel).getPackungsGroesse())) {
					artikel.setBestand(artikel.getBestand() - anzahl);
					updateVerfuegbarkeit(artikel);
				} else {
					throw new BestandPasstNichtMitPackungsGroesseException(anzahl, artikelZuMassengutartikel(artikel),
							"");
				}
			} else {
				artikel.setBestand(artikel.getBestand() - anzahl);
				updateVerfuegbarkeit(artikel);
			}
		}

		return artikel;

	}

	/**
	 * 
	 * Verringert den Bestand des angegebenen Artikels um die angegebene Anzahl.*
	 * 
	 * @param artikel Der Artikel, dessen Bestand verringert werden soll.
	 * @param anzahl  Die Anzahl, um die der Bestand verringert werden soll.
	 * @throws SenkenUnterNullNichtMoeglichException
	 */

	public void bestandSenken(Artikel artikel, int anzahl) throws SenkenUnterNullNichtMoeglichException {
		int neueMenge = artikel.getBestand() - anzahl;
		if (neueMenge < 0) {
			throw new SenkenUnterNullNichtMoeglichException(artikel, anzahl);
		}

		else {
			artikel.setBestand(neueMenge);
			updateVerfuegbarkeit(artikel);
		}

	}

	/**
	 * 
	 * L�scht den angegebenen Artikel.*
	 * 
	 * @param artikel Der zu l�schende Artikel.
	 * @throws ArtikelExistiertNichtException Wenn der Artikel nicht existiert.
	 */

	public void artikelloeschen(Artikel artikel) throws ArtikelExistiertNichtException {
		if (this.artikelListe.contains(artikel)) {
			artikelListe.remove(artikel);
		} else {
			throw new ArtikelExistiertNichtException(artikel, "");
		}
	}

	/**
	 * 
	 * Sucht nach einem Artikel mit dem angegebenen Namen.*
	 * 
	 * @param name Der Name des zu suchenden Artikels.
	 * @return Der gefundene Artikel oder null, wenn kein Artikel mit dem
	 *         angegebenen Namen gefunden wurde.
	 * @throws ArtikelExistiertNichtException
	 */

	public Artikel sucheArtikel(String name) throws ArtikelExistiertNichtException {
		Artikel suchArtikel = null;
		boolean artikelGefunden = false;

		if (artikelListe.isEmpty()) {
			throw new ArtikelExistiertNichtException(" Artikel Liste ist Leer");
		} else {
			Iterator<Artikel> iter = artikelListe.iterator();
			while (iter.hasNext()) {
				Artikel a = iter.next();
				if (a.getName().equals(name)) {

					suchArtikel = a;
					artikelGefunden = true;
					break;
				}
			}
		}

		if (!artikelGefunden) {
			throw new ArtikelExistiertNichtException(name);
		}

		return suchArtikel;
	}

	public static boolean checkMassengutartikel(Artikel artikel) {

		return artikel.getIstPackung();

	}

	public static Massengutartikel artikelZuMassengutartikel(Artikel artikel) {

		Massengutartikel artikel_1 = (Massengutartikel) artikel;
		return artikel_1;

	}

	/**
	 * 
	 * Aktualisiert die Verf�gbarkeit des angegebenen Artikels.*
	 * 
	 * @param artikel Der Artikel, dessen Verf�gbarkeit aktualisiert werden soll.
	 * @return true, wenn der Artikel verf�gbar ist, andernfalls false.
	 */
	public boolean updateVerfuegbarkeit(Artikel artikel) {
		if (artikel.getBestand() == 0) {
			artikel.setVerfuegbar(false);
			return false;
		} else {
			artikel.setVerfuegbar(true);
			return true;
		}
	}

	/*
	 * 
	 * @return Die Liste von unsere Artikeln.
	 **/
	public Vector<Artikel> getArtikelListe() {
		return new Vector<>(artikelListe);
	}
}
