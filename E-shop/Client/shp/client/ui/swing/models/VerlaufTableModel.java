package shp.client.ui.swing.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import shp.common.entities.Verlauf;

public class VerlaufTableModel extends AbstractTableModel {

    private List<Verlauf> verlaeufe;
    private String[] spaltenNamen = {"Datum", "Nutzer", "Aktion", "Artikel", "aenderung"};

    public VerlaufTableModel(List<Verlauf> verlaeufe) {
        super();
        this.verlaeufe = new Vector<>(verlaeufe);
    }

    public void setVerlaeufe(List<Verlauf> verlaeufe) {
        this.verlaeufe = new Vector<>(verlaeufe);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return verlaeufe.size();
    }

    @Override
    public int getColumnCount() {
        return spaltenNamen.length;
    }

    @Override
    public String getColumnName(int col) {
        return spaltenNamen[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Verlauf verlauf = verlaeufe.get(row);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        switch (col) {
            case 0:
                return format.format(verlauf.getDate());
            case 1:
                return verlauf.getNutzer().getVorname() + " " + verlauf.getNutzer().getName();
            case 2:
                return verlauf.getAktion().getValue();
            case 3:
                return verlauf.getArtikelName();
            case 4:
                return verlauf.getAenderungsMenge();
            default:
                return null;
        }
    }
}
