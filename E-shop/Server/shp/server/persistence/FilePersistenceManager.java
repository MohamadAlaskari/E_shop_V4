package shp.server.persistence;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import shp.common.entities.Adresse;
import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Massengutartikel;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Nutzer;
import shp.common.entities.Verlauf;
import shp.common.entities.Warenkorb;
import shp.common.entities.Verlauf.AKTIONSTYP;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.server.domin.ArtikelVerwaltung;
import shp.server.domin.KundeVerwaltung;
import shp.server.domin.MitarbeiterVerwaltung;

public class FilePersistenceManager implements PersistenceManager {

	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	public boolean close() {
		if (writer != null)
			writer.close();

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public Artikel ladeArtikel() throws IOException {
		String artikelId_Check = liesZeile();
		Artikel artikel;
		if (artikelId_Check == null) {
			// keine Daten vorhanden
			return null;
		}
		int artikelId = Integer.parseInt(artikelId_Check);

		String name = liesZeile();
		String beschreibung = liesZeile();
		int bestand = Integer.parseInt(liesZeile());
		double preis = Double.parseDouble(liesZeile());

		String verfuegbarCode = liesZeile();
		// Codierung des Ausleihstatus in boolean umwandeln
		boolean verfuegbar = verfuegbarCode.equals("t") ? true : false;
		String istPackungCode = liesZeile();
		boolean istPackung = istPackungCode.equals("t") ? true : false;
		if (istPackung) {
			int packungsGroesse = Integer.parseInt(liesZeile());
			artikel = new Massengutartikel(artikelId, name, beschreibung, bestand, preis, verfuegbar, istPackung,
					packungsGroesse);
		} else {
			artikel = new Artikel(artikelId, name, beschreibung, bestand, preis, verfuegbar, istPackung);

		}
		return artikel;

	}

	public boolean speichereArtikel(Artikel artikel) throws IOException {
		schreibeZeile(artikel.getArtikelId() + "");
		schreibeZeile(artikel.getName());
		schreibeZeile(artikel.getBeschreibung());
		schreibeZeile(artikel.getBestand() + "");
		schreibeZeile(artikel.getPreis() + "");
		if (artikel.isVerfuegbar())
			schreibeZeile("t");
		else
			schreibeZeile("f");
		schreibeZeile((artikel.getIstPackung() ? "t" : "f") + "");
		if (artikel.getIstPackung()) {
			Massengutartikel artikel_1 = (Massengutartikel) artikel;
			schreibeZeile(artikel_1.getPackungsGroesse() + "");
		}

		return true;
	}

	// kunde
	public Kunde ladeKunde() throws IOException {
		String kndNr_Check = liesZeile();
		if (kndNr_Check == null) {
			// keine Daten vorhanden
			return null;
		}

		int kndNr = Integer.parseInt(kndNr_Check);
		String name = liesZeile();
		String vorName = liesZeile();
		String nutzerNr = liesZeile();
		String password = liesZeile();

		// Adresse
		String strassenName = liesZeile();
		String hausNr = liesZeile();
		String plz = liesZeile();
		String ort = liesZeile();
		String land = liesZeile();
		Adresse adresse = new Adresse(strassenName, hausNr, plz, ort, land);
		Kunde kunde = new Kunde(kndNr, name, vorName, nutzerNr, password, adresse);

		return kunde;
	}

	public boolean speichereKunde(Kunde kunde) throws IOException {
		schreibeZeile(kunde.getKndNr() + "");
		schreibeZeile(kunde.getName());
		schreibeZeile(kunde.getVorname());
		schreibeZeile(kunde.getNutzerName());
		schreibeZeile(kunde.getPasswort());
		schreibeZeile(kunde.getAdresse().getStrasse());
		schreibeZeile(kunde.getAdresse().gethNr());
		schreibeZeile(kunde.getAdresse().getPlz());
		schreibeZeile(kunde.getAdresse().getOrt());
		schreibeZeile(kunde.getAdresse().getLand());

		return true;
	}

	// kunde
	public Mitarbeiter ladeMitarbeiter() throws IOException {
		String maNr_Check = liesZeile();
		if (maNr_Check == null) {
			// keine Daten vorhanden
			return null;
		}
		int maNr = Integer.parseInt(maNr_Check);
		String name = liesZeile();
		String vorName = liesZeile();
		String nutzerNr = liesZeile();
		String password = liesZeile();
		Mitarbeiter ma = new Mitarbeiter(maNr, name, vorName, nutzerNr, password);
		return ma;
	}

	public boolean speichereMitarbeiter(Mitarbeiter mitarbeiter) throws IOException {
		schreibeZeile(mitarbeiter.getMaId() + "");
		schreibeZeile(mitarbeiter.getName());
		schreibeZeile(mitarbeiter.getVorname());
		schreibeZeile(mitarbeiter.getNutzerName());
		schreibeZeile(mitarbeiter.getPasswort());
		return true;
	}

	// Verlauf
	public Verlauf ladeVerlauf(ArtikelVerwaltung art, KundeVerwaltung kd, MitarbeiterVerwaltung mt)
			throws IOException, ArtikelExistiertNichtException, ParseException {
		String aktionS = liesZeile();
		if (aktionS == null) {
			return null;
		} else {
			AKTIONSTYP aktion = AKTIONSTYP.valueOf(aktionS);
			String nutzerName = liesZeile();
			String artikelName = liesZeile();
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Date date = format.parse(liesZeile());
			int aenderungsMenge = Integer.parseInt(liesZeile());

			Nutzer nutzer = null;
//			if (kd.sucheKunde(nutzerName) != null) {
//				nutzer = kd.sucheKunde(nutzerName);
//			}
//
//			else if (mt.sucheMitarbeiter(nutzerName) != null) {
//				nutzer = mt.sucheMitarbeiter(nutzerName);
//			}
			//!
			nutzer= (kd.sucheKunde(nutzerName) != null ? kd.sucheKunde(nutzerName): mt.sucheMitarbeiter(nutzerName));
			Verlauf verlauf = new Verlauf(aktion, nutzer, artikelName, date, aenderungsMenge);
			return verlauf;
		}
	}

	public boolean speichereVerlauf(Verlauf verlauf) throws IOException {
		schreibeZeile(verlauf.getAktion().name());
		schreibeZeile(verlauf.getNutzer().getNutzerName());
		schreibeZeile(verlauf.getArtikelName());
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		schreibeZeile(format.format(verlauf.getDate()));
		schreibeZeile(verlauf.getAenderungsMenge() + "");

		return true;
	}

	private String liesZeile() throws IOException {
		if (reader != null)
			return reader.readLine();
		else
			return "";
	}

	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}

}
