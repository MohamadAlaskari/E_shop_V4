package shp.client.ui.swing.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import shp.common.entities.Artikel;
import shp.common.entities.Warenkorb;

public class WarenkorbModel extends AbstractTableModel {

	private HashMap<Artikel, Integer> warenkorbMap;
	private String[] spaltenNamen = { "Artikel Name", "Menge" };
	private Warenkorb warenkorb;
	private double gesamtpreis;

	public WarenkorbModel(HashMap<Artikel, Integer> aktuelleArtikeln) {
		super();

		warenkorbMap = new HashMap<Artikel, Integer>();
		warenkorbMap.putAll(aktuelleArtikeln);

	}

	public void setWarenkorb(HashMap<Artikel, Integer> aktuelleWarenkorbMap, Warenkorb warenkorb) {
		
		this.warenkorb = warenkorb;

		if (warenkorbMap == null) {

			warenkorbMap = new HashMap<Artikel, Integer>();
		} else {
			warenkorbMap.clear();
		}
		warenkorbMap.putAll(aktuelleWarenkorbMap);
		fireTableDataChanged();

	}

	@Override
	public int getRowCount() {

		return warenkorbMap.size()+1;
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
		List<Artikel> keys = new ArrayList<Artikel>(warenkorbMap.keySet());
		Artikel artikel = keys.get(row);
		return artikel;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Wenn es die letzte Zeile ist, zeigen Sie den Gesamtpreis an
		if (warenkorb == null) {
			gesamtpreis= 0.0;
		}
		else {
			gesamtpreis =warenkorb.getGesamtPrise();
		}
		if (rowIndex == warenkorbMap.size()) {
			if (columnIndex == 0) {
				return "Gesamtpreis";
			} else if (columnIndex == 1) {
				
				return gesamtpreis; // Verwenden Sie die getGesamtPreis() Methode
			} else {
				
				return null;
			}
		}

		List<Artikel> keys = new ArrayList<Artikel>(warenkorbMap.keySet());
		Artikel artikel = keys.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return artikel.getName();
		case 1:
			return warenkorbMap.get(artikel);
		default:
			return null;
		}
	}

//	@Override
//	public Object getValueAt(int rowIndex, int columnIndex) {
//
//		List<Artikel> keys = new ArrayList<Artikel>(warenkorbMap.keySet());
//		Artikel artikel = keys.get(rowIndex);
//
//		switch (columnIndex) {
//		case 0:
//			return artikel.getName();
//		case 1:
//			return warenkorbMap.get(artikel);
//		default:
//			return null;
//		}
//
//	}
}
