package shp.common.exceptions;
import shp.common.entities.Massengutartikel;

public class BestandPasstNichtMitPackungsGroesseException extends Exception {

	private Massengutartikel artikel;

	public BestandPasstNichtMitPackungsGroesseException(Massengutartikel artikel, String zusatzMsg) {
		super("Der Bestand '" + artikel.getBestand() + "' kann nicht in gleichmäßegie Verpackunge mit anzahl '"
				+ artikel.getPackungsGroesse() + "' verteilt werden" + zusatzMsg);
		this.artikel = artikel;
	}
	public BestandPasstNichtMitPackungsGroesseException(int anzahl, Massengutartikel artikel, String zusatzMsg) {
		super("Die Menge '" + anzahl+ "' kann nicht in gleichmäßegie Verpackunge mit anzahl '"
				+ artikel.getPackungsGroesse() + "' verteilt werden" + zusatzMsg);
		this.artikel = artikel;
	}

	public Massengutartikel getArtikel() {
		return artikel;
	}

}
