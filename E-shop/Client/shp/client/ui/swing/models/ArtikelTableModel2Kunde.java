package shp.client.ui.swing.models;

import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;

import shp.common.entities.Artikel;
import shp.common.entities.Massengutartikel;

import java.util.List;
import java.util.Vector;

public class ArtikelTableModel2Kunde extends AbstractTableModel  {

	private List<Artikel> artikeln;
	private String[] spaltenNamen = { "ID", "Name", "Beschreibung", "Preis","Verfügbarkeit", "MassengutArtikel",
			"Packungsgroeße" };

	public ArtikelTableModel2Kunde(List<Artikel> aktuelleArtikeln) {
		super();
		
		artikeln = new Vector<Artikel>();
		artikeln.addAll(aktuelleArtikeln);
		
	}

	public void setArtikeln(List<Artikel> aktuelleArtikeln) {
		artikeln.clear();
		artikeln.addAll(aktuelleArtikeln);
		// Änderungen an den Daten des Modells mitzuteilen,
		// damit die JTable diese Änderungen reflektieren kann.
		fireTableDataChanged();
	}

	/*
	 * Ab hier überschriebene Methoden mit Informationen, die eine JTable von jedem
	 * TableModel erwartet: - Anzahl der Zeilen - Anzahl der Spalten - Benennung der
	 * Spalten - Wert einer Zelle in Zeile / Spalte
	 */

	@Override
	public int getRowCount() {
		return artikeln.size();
	}

	@Override
	public int getColumnCount() {
		return spaltenNamen.length;
	}

	@Override
	public String getColumnName(int col) {
		return spaltenNamen[col];
	}
	
	public Artikel getSelecetedArtikel(int row) {
		
		Artikel artikel = artikeln.get(row);
		return artikel;
	}

	
	
	@Override
	public Object getValueAt(int row, int col) {
		Artikel gewaehltesArtikel = artikeln.get(row);

		if (gewaehltesArtikel.getIstPackung()) {
			Massengutartikel gewaehltesArtikel_1 = (Massengutartikel) gewaehltesArtikel;
			switch (col) {
			case 0:
				return gewaehltesArtikel_1.getArtikelId();
			case 1:
				return gewaehltesArtikel_1.getName();
			case 2:
				return gewaehltesArtikel_1.getBeschreibung();
			case 3:
				return gewaehltesArtikel_1.getPreis();
			case 4:
				return gewaehltesArtikel_1.isVerfuegbar();
			case 5:
				return gewaehltesArtikel_1.getIstPackung();
			case 6:
				return gewaehltesArtikel_1.getPackungsGroesse();
			default:
				return null;
			}
		} else {

			switch (col) {
			case 0:
				return gewaehltesArtikel.getArtikelId();
			case 1:
				return gewaehltesArtikel.getName();
			case 2:
				return gewaehltesArtikel.getBeschreibung();
			case 3:
				return gewaehltesArtikel.getPreis();
			case 4:
				return gewaehltesArtikel.isVerfuegbar();
			case 5:
				return gewaehltesArtikel.getIstPackung();
			
			default:
				return null;
			}
		}

	}

}
