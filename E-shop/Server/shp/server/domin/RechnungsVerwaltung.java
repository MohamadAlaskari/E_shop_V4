package shp.server.domin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import shp.common.entities.Bestellung;
import shp.common.entities.Rechnung;

/**
 * 
 * Die Klasse RechnungsVerwaltung verwaltet die Erstellung und Verwaltung von
 * Rechnungen.
 */
public class RechnungsVerwaltung {
	private Rechnung rechnung;
	private List<Rechnung> RechnungenList = new Vector<>();
	private LocalDateTime aktuelleDatumZeit = LocalDateTime.now();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
	private String formattedDatumZeit;

	/**
	 * Erstellt eine Rechnung f�r eine Bestellung.
	 * 
	 * @param bestellung Die Bestellung, f�r die die Rechnung erstellt wird.
	 * @return Die erstellte Rechnung.
	 */
	public Rechnung erstelleRechnung(Bestellung bestellung) {

		updateTime();
		this.formattedDatumZeit = aktuelleDatumZeit.format(formatter);

		rechnung = new Rechnung(bestellung, formattedDatumZeit);
		generateRechnungsNr();
		RechnungenList.add(rechnung);
		bestellung.setRechnung(rechnung);
		return rechnung;
	}

	/*
	 * Akualisiert die aktuelleDatumZeit Variable
	 */
	public void updateTime() {
		aktuelleDatumZeit = LocalDateTime.now();
	}

	/**
	 * 
	 * Generiert eine eindeutige Rechnungsnummer f�r die neue Rechnung.
	 */
	private void generateRechnungsNr() {
		if (RechnungenList.isEmpty()) {
			rechnung.setRechnungNr(303);
		} else {
			int lastRechnungNr = RechnungenList.get(RechnungenList.size() - 1).getRechnungNr();
			rechnung.setRechnungNr(lastRechnungNr + 34);
		}
	}

	/**
	 * Gibt die Liste aller vorhandenen Rechnungen zur�ck.
	 * 
	 * @return Die Liste der Rechnungen.
	 */
	public List<Rechnung> getRechnungenList() {
		return RechnungenList;
	}


}
