package shp.client.ui.swing.panels;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;

import shp.client.ui.swing.models.VerlaufTableModel;
import shp.common.entities.Artikel;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Nutzer;
import shp.common.entities.Verlauf;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
import shp.common.exceptions.VerlaufLeerException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Cursor;



public class MitarbeiterMenuePanel extends JPanel {
	private JButton btn_zeigeArtikeln;
	private JButton btnLoeschen;
	private JButton btnBestand_Senken;
	private JButton btnArtikel_Erhoehen;
	private JButton btnZeigeverlauf;
	private JButton btn_30erVerlauf;
	private TableDataListener tableDataListener;
	private E_ShopInterface shop;
	private Mitarbeiter mitarbeiter;
	private JSeparator separator;
	private JButton btn_MA_regestrieren;

	public interface TableDataListener {
		public Artikel onSelctedRow();

		public void updateTable();

		public void updateToVerlauf(List<Verlauf> verlaufListe);

	}

	/**
	 * Create the panel.
	 */
	public MitarbeiterMenuePanel(TableDataListener tableDataListener, E_ShopInterface shop, Nutzer loggedNutzer) {
		this.tableDataListener = tableDataListener;
		this.shop = shop;
		this.mitarbeiter = (Mitarbeiter) loggedNutzer;
		initGUI();
	}

	private void initGUI() {
		setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Menue",
				TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 107, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 21, 30, 0, 21, 21, 21, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		{
			this.btnLoeschen = new JButton("Artikel Löschen");
			this.btnLoeschen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.btnLoeschen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btnLoeschen_actionPerformed(e);
				}
			});
			{
				this.btn_zeigeArtikeln = new JButton("Zeige Artikeln");
				this.btn_zeigeArtikeln.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				this.btn_zeigeArtikeln.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btn_zeigeArtikeln_actionPerformed(e);
					}
				});
				GridBagConstraints gbc_btn_zeigeArtikeln = new GridBagConstraints();
				gbc_btn_zeigeArtikeln.insets = new Insets(0, 0, 5, 0);
				gbc_btn_zeigeArtikeln.fill = GridBagConstraints.BOTH;
				gbc_btn_zeigeArtikeln.gridx = 0;
				gbc_btn_zeigeArtikeln.gridy = 0;
				add(this.btn_zeigeArtikeln, gbc_btn_zeigeArtikeln);
			}
			{
				this.btnZeigeverlauf = new JButton("Zeige Verlauf");
				this.btnZeigeverlauf.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				this.btnZeigeverlauf.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btnZeigeverlauf_actionPerformed(e);
					}
				});
				GridBagConstraints gbc_btnZeigeverlauf = new GridBagConstraints();
				gbc_btnZeigeverlauf.insets = new Insets(0, 0, 5, 0);
				gbc_btnZeigeverlauf.fill = GridBagConstraints.BOTH;
				gbc_btnZeigeverlauf.gridx = 0;
				gbc_btnZeigeverlauf.gridy = 1;
				add(this.btnZeigeverlauf, gbc_btnZeigeverlauf);
			}
			{
				this.separator = new JSeparator();
				GridBagConstraints gbc_separator = new GridBagConstraints();
				gbc_separator.fill = GridBagConstraints.HORIZONTAL;
				gbc_separator.insets = new Insets(0, 0, 5, 0);
				gbc_separator.gridx = 0;
				gbc_separator.gridy = 2;
				add(this.separator, gbc_separator);
			}
			{
				this.btn_30erVerlauf = new JButton("30er Verlauf");
				this.btn_30erVerlauf.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				this.btn_30erVerlauf.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btn_30erVerlauf_actionPerformed(e);
					}
				});
				GridBagConstraints gbc_btn_30erVerlauf = new GridBagConstraints();
				gbc_btn_30erVerlauf.insets = new Insets(0, 0, 5, 0);
				gbc_btn_30erVerlauf.fill = GridBagConstraints.HORIZONTAL;
				gbc_btn_30erVerlauf.gridx = 0;
				gbc_btn_30erVerlauf.gridy = 3;
				add(this.btn_30erVerlauf, gbc_btn_30erVerlauf);
			}
			GridBagConstraints gbc_btnLoeschen = new GridBagConstraints();
			gbc_btnLoeschen.fill = GridBagConstraints.BOTH;
			gbc_btnLoeschen.insets = new Insets(0, 0, 5, 0);
			gbc_btnLoeschen.gridx = 0;
			gbc_btnLoeschen.gridy = 4;
			add(this.btnLoeschen, gbc_btnLoeschen);
		}
		{
			this.btnBestand_Senken = new JButton("Bestand Senken");
			this.btnBestand_Senken.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.btnBestand_Senken.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btnBestand_Senken_actionPerformed(e);
				}
			});
			GridBagConstraints gbc_btnBestand_Senken = new GridBagConstraints();
			gbc_btnBestand_Senken.fill = GridBagConstraints.BOTH;
			gbc_btnBestand_Senken.insets = new Insets(0, 0, 5, 0);
			gbc_btnBestand_Senken.gridx = 0;
			gbc_btnBestand_Senken.gridy = 5;
			add(this.btnBestand_Senken, gbc_btnBestand_Senken);
		}
		{
			this.btnArtikel_Erhoehen = new JButton("Artikel Erhoehen");
			this.btnArtikel_Erhoehen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.btnArtikel_Erhoehen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btnArtikel_Erhoehen_actionPerformed(e);
				}
			});
			GridBagConstraints gbc_btnArtikel_Erhoehen = new GridBagConstraints();
			gbc_btnArtikel_Erhoehen.insets = new Insets(0, 0, 5, 0);
			gbc_btnArtikel_Erhoehen.fill = GridBagConstraints.BOTH;
			gbc_btnArtikel_Erhoehen.gridx = 0;
			gbc_btnArtikel_Erhoehen.gridy = 6;
			add(this.btnArtikel_Erhoehen, gbc_btnArtikel_Erhoehen);
		}
		{
			this.btn_MA_regestrieren = new JButton("MA. regestrieren");
			this.btn_MA_regestrieren.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_MA_regestrieren_actionPerformed(e);
				}
			});
			this.btn_MA_regestrieren.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			GridBagConstraints gbc_btn_MA_regestrieren = new GridBagConstraints();
			gbc_btn_MA_regestrieren.anchor = GridBagConstraints.SOUTH;
			gbc_btn_MA_regestrieren.fill = GridBagConstraints.HORIZONTAL;
			gbc_btn_MA_regestrieren.gridx = 0;
			gbc_btn_MA_regestrieren.gridy = 8;
			add(this.btn_MA_regestrieren, gbc_btn_MA_regestrieren);
		}
	}

	// button loeschen
	protected void do_btnLoeschen_actionPerformed(ActionEvent e) {
		if (tableDataListener.onSelctedRow() == null) {

			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {

				shop.loescheArtikel(mitarbeiter, tableDataListener.onSelctedRow().getName());
				shop.schreibeArtikel();
				shop.schreibeVerlauf();
				tableDataListener.updateTable();
			} catch (ArtikelExistiertNichtException | IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// button senken
	protected void do_btnBestand_Senken_actionPerformed(ActionEvent e) {
		if (tableDataListener.onSelctedRow() == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {
				int menge = 0;
				String result = JOptionPane.showInputDialog(null, " Bitte zu senkende Menge eingeben:", "Eingabe >",
						JOptionPane.PLAIN_MESSAGE);
				menge = Integer.parseInt(result);
				shop.senkenArtikelBestand(mitarbeiter, tableDataListener.onSelctedRow().getName(), menge);
				shop.schreibeArtikel();
				shop.schreibeVerlauf();
				tableDataListener.updateTable();
			} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException | IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Die Menge muss eine ganze Zahl sein.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (SenkenUnterNullNichtMoeglichException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			}
		}
	}

	// button erhoehen
	protected void do_btnArtikel_Erhoehen_actionPerformed(ActionEvent e) {
		if (tableDataListener.onSelctedRow() == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {
				int menge = 0;
				String result = JOptionPane.showInputDialog(null, " Bitte zu erhöhte Menge eingeben:", "Eingabe >",
						JOptionPane.PLAIN_MESSAGE);
				menge = Integer.parseInt(result);
				shop.erhoeheArtikelBestand(mitarbeiter, tableDataListener.onSelctedRow().getName(), menge);
				shop.schreibeArtikel();
				shop.schreibeVerlauf();
				tableDataListener.updateTable();
			} catch (ArtikelExistiertNichtException | BestandPasstNichtMitPackungsGroesseException | IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Die Menge muss eine ganze Zahl sein.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	protected void do_btnZeigeverlauf_actionPerformed(ActionEvent e) {

		try {
			List<Verlauf> verlaufListe = shop.gibVerlauflistaus();
			tableDataListener.updateToVerlauf(verlaufListe);
		} catch (VerlaufLeerException e1) {
			System.out.println(e1.getMessage());
		}
		showBtns_verlaufVerwalten();

	}

	protected void do_btn_30erVerlauf_actionPerformed(ActionEvent e) {
		if (tableDataListener.onSelctedRow() == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			showBtns_verlaufVerwalten();
			try {
				String artikelName = tableDataListener.onSelctedRow().getName();
				List<Verlauf> verlaufListe = shop.zeigeVerlaufArtikelDreissigTage(artikelName);
				tableDataListener.updateToVerlauf(verlaufListe);
			} catch (ArtikelExistiertNichtException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	protected void do_btn_zeigeArtikeln_actionPerformed(ActionEvent e) {
		List<Artikel> artikeln = shop.gibAlleArtikeln();
		tableDataListener.updateTable();
		showBtns_ArtikelVerwalten();
	}

	private void showBtns_ArtikelVerwalten() {
		btnLoeschen.setVisible(true);
		btnArtikel_Erhoehen.setVisible(true);
		btnBestand_Senken.setVisible(true);
		btn_30erVerlauf.setVisible(true);

	}

	private void showBtns_verlaufVerwalten() {
		btnLoeschen.setVisible(false);
		btnArtikel_Erhoehen.setVisible(false);
		btnBestand_Senken.setVisible(false);
		btn_30erVerlauf.setVisible(false);

	}

	protected void do_btn_MA_regestrieren_actionPerformed(ActionEvent e) {
		MARegestrierenDialog regDialog = new MARegestrierenDialog(shop);

		// Setze das Dialogfenster als modal, um zu verhindern, dass Benutzer auf das
		// Hauptfenster zugreifen können, während der Dialog geöffnet ist
		regDialog.setModal(true);

		regDialog.setVisible(true);
	}
}
