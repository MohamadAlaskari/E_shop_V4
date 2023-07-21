package shp.server.persistence;

import java.io.IOException;
import java.text.ParseException;

import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Verlauf;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.server.domin.ArtikelVerwaltung;
import shp.server.domin.KundeVerwaltung;
import shp.server.domin.MitarbeiterVerwaltung;


public interface PersistenceManager {

	public void openForReading(String datenquelle) throws IOException;
	
	public void openForWriting(String datenquelle) throws IOException;
	
	public boolean close();

	
	public Artikel ladeArtikel() throws IOException;
	public boolean speichereArtikel(Artikel artikel) throws IOException;
	
	public Kunde ladeKunde() throws IOException;
	public boolean speichereKunde(Kunde kunde) throws IOException;
	
	public Mitarbeiter ladeMitarbeiter() throws IOException;
	public boolean speichereMitarbeiter(Mitarbeiter mitarbeiter) throws IOException;
	
	public Verlauf ladeVerlauf(ArtikelVerwaltung art, KundeVerwaltung kd, MitarbeiterVerwaltung mt) throws IOException, ArtikelExistiertNichtException, ParseException;
	public boolean speichereVerlauf(Verlauf verlauf) throws IOException;
	

	
}
