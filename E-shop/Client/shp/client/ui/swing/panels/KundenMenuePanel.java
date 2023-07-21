package shp.client.ui.swing.panels;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import shp.client.ui.swing.panels.MitarbeiterMenuePanel.TableDataListener;
import shp.common.entities.Artikel;
import shp.common.entities.Bestellung;
import shp.common.entities.Kunde;
import shp.common.entities.Rechnung;
import shp.common.exceptions.AnzahlIsNichtDefiniertException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.NichtGenugArtikelVorhandenException;
import shp.common.exceptions.SenkenUnterNullNichtMoeglichException;
import shp.common.exceptions.WarenkorbLeerException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.FlowLayout;

public class KundenMenuePanel extends JPanel {
	private JButton btn_ArtikelAnzeigen;
	private JButton btn_Warenkorb;
	private E_ShopInterface shop;
	private Kunde kunde;
	private Bestellung aktuelleBestellung;
	private OnWarenkorpListener onWarenkorpListener;
	private JButton btnFuegeArtikel;
	private boolean istWarenKorb = false;
	private JButton btnKaufen;
	private JButton btnArtikelSenken;
	private JButton btnArtikelErhoehen;
	private JButton btnEntfernen;
	private JPanel panelMinusPlus;
	private JButton btnLeeren;

	public interface OnWarenkorpListener {

		void updateToArtikel(List<Artikel> artikeln);

		Artikel onSelectedRow_Kunde();

		void updateWarenKorb();

		Artikel onSelectedRow_Warenkorb();
	}

	/**
	 * Create the panel.
	 */
	public KundenMenuePanel(E_ShopInterface shop, Kunde kunde, OnWarenkorpListener onWarenkorpListener) {
		this.shop = shop;
		this.onWarenkorpListener = onWarenkorpListener;
		this.kunde = kunde;
		this.onWarenkorpListener = onWarenkorpListener;
		initGUI();
	}

	private void initGUI() {
		setBorder(new TitledBorder(null, "Kunde Menue", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		{
			this.btn_ArtikelAnzeigen = new JButton("Artikeln anzeigen");
			this.btn_ArtikelAnzeigen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_ArtikelAnzeigen_actionPerformed(e);
				}
			});
			{
				this.btnFuegeArtikel = new JButton("Zum WK. hinzuf\u00FCgen");
				this.btnFuegeArtikel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btnAddArtikel_actionPerformed(e);
					}
				});
				add(this.btnFuegeArtikel);
			}
			add(this.btn_ArtikelAnzeigen);
		}
		{
			this.btn_Warenkorb = new JButton("Warenkorb");
			this.btn_Warenkorb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_Warenkorb_actionPerformed(e);
				}
			});
			add(this.btn_Warenkorb);
		}
		{
			btnKaufen = new JButton("Kaufen");
			btnKaufen.setVisible(false);
			btnKaufen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_Kaufen_actionPerformed(e);
				}
			});
			add(btnKaufen);
		}
		{
			{
				panelMinusPlus = new JPanel();
				panelMinusPlus.setAlignmentX(0.0f);
				add(panelMinusPlus);
				panelMinusPlus.setLayout(new BoxLayout(panelMinusPlus, BoxLayout.X_AXIS));
				btnArtikelErhoehen = new JButton("+");
				panelMinusPlus.add(btnArtikelErhoehen);
				btnArtikelErhoehen.setVisible(false);
				btnArtikelErhoehen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btn_ArtikelErhoehen_actionPerformed(e);
					}
				});
				btnArtikelErhoehen.setFont(new Font("Tahoma", Font.PLAIN, 14));
				{
					btnArtikelSenken = new JButton("-");
					panelMinusPlus.add(btnArtikelSenken);
					btnArtikelSenken.setVisible(false);
					btnArtikelSenken.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							do_btn_ArtikelSenken_actionPerformed(e);
						}
					});
					btnArtikelSenken.setFont(new Font("Tahoma", Font.PLAIN, 14));
				}
			}
		}
		{
			btnEntfernen = new JButton("Entfernen");
			btnEntfernen.setVisible(false);
			btnEntfernen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_btnEntfernen_actionPerformed(e);
				}
			});
			add(btnEntfernen);
		}
		{
			btnLeeren = new JButton("Leeren");
			btnLeeren.setVisible(false);
			btnLeeren.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_btnLeeren_actionPerformed(e);
				}
			});
			add(btnLeeren);
		}
	}

	protected void do_btn_btnLeeren_actionPerformed(ActionEvent e) {
		shop.leereWarenkorb(kunde);
		onWarenkorpListener.updateWarenKorb();
	}

	protected void do_btn_btnEntfernen_actionPerformed(ActionEvent e) {
		Artikel artikel = onWarenkorpListener.onSelectedRow_Warenkorb();
		if (artikel == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			shop.loescheArtikelVomWarenkorb(kunde, artikel);
			onWarenkorpListener.updateWarenKorb();
		}
	}

	protected void do_btn_ArtikelSenken_actionPerformed(ActionEvent e) {
		Artikel artikel = onWarenkorpListener.onSelectedRow_Warenkorb();
		if (artikel == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {

			try {
				shop.fuegeArtikelInkorbEin(kunde, artikel, -1);
				onWarenkorpListener.updateWarenKorb();
			} catch (NichtGenugArtikelVorhandenException | BestandPasstNichtMitPackungsGroesseException
					| ArtikelExistiertNichtException | AnzahlIsNichtDefiniertException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error >", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	protected void do_btn_ArtikelErhoehen_actionPerformed(ActionEvent e) {
		Artikel artikel = onWarenkorpListener.onSelectedRow_Warenkorb();
		if (artikel == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {
				shop.fuegeArtikelInkorbEin(kunde, artikel, 1);
				onWarenkorpListener.updateWarenKorb();
			} catch (NichtGenugArtikelVorhandenException | BestandPasstNichtMitPackungsGroesseException
					| ArtikelExistiertNichtException | AnzahlIsNichtDefiniertException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error >", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	protected void do_btn_Kaufen_actionPerformed(ActionEvent e) {
		try {
			aktuelleBestellung = shop.bestellen(kunde);
			Rechnung rechnung = shop.erstelleRechnung(aktuelleBestellung);
			shop.schreibeVerlauf();
			datenSischern();
			JOptionPane.showMessageDialog(null, rechnung, "info",
					JOptionPane.INFORMATION_MESSAGE);
			shop.leereWarenkorb(kunde);
			onWarenkorpListener.updateWarenKorb();
		} catch (WarenkorbLeerException | NichtGenugArtikelVorhandenException | SenkenUnterNullNichtMoeglichException
				| IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error >", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void do_btn_Warenkorb_actionPerformed(ActionEvent e) {
		changeWarenkorbBtnsVisible(true);
		onWarenkorpListener.updateWarenKorb();
	}

	protected void do_btn_ArtikelAnzeigen_actionPerformed(ActionEvent e) {
		changeWarenkorbBtnsVisible(false);
		List<Artikel> artikeln = shop.gibAlleArtikeln();
		onWarenkorpListener.updateToArtikel(artikeln);
	}

	protected void do_btnAddArtikel_actionPerformed(ActionEvent e) {

		int menge = 0;
		Artikel artikel = onWarenkorpListener.onSelectedRow_Kunde();

		if (artikel == null) {
			JOptionPane.showMessageDialog(null, "Bitte nur einen Artikel auswählen", "info",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			changeWarenkorbBtnsVisible(false);
			try {
				String result = JOptionPane.showInputDialog(null, " Bitte zu hinzufuegen Menge eingeben:", "Eingabe >",
						JOptionPane.PLAIN_MESSAGE);
				menge = Integer.parseInt(result);
				shop.fuegeArtikelInkorbEin(kunde, artikel, menge);
				onWarenkorpListener.updateWarenKorb();
				artikelHinzufuegenBestaetigt();
			} catch (NichtGenugArtikelVorhandenException | BestandPasstNichtMitPackungsGroesseException
					| ArtikelExistiertNichtException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Die Menge muss eine ganze Zahl sein.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (AnzahlIsNichtDefiniertException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void artikelHinzufuegenBestaetigt() {
		JOptionPane optionPane = new JOptionPane("Artikel hinzugef�gt.", JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = optionPane.createDialog("Best�tigung");

		Timer timer = new Timer(650, e -> dialog.dispose());
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	public void changeWarenkorbBtnsVisible(boolean a) {
		btnFuegeArtikel.setVisible(!a);
		btnLeeren.setVisible(a);
		btnKaufen.setVisible(a);
		btnArtikelErhoehen.setVisible(a);
		btnArtikelSenken.setVisible(a);
		btnEntfernen.setVisible(a);

	}

	private void textFeldeLeeren() {

	}

	/**
	 * @throws IOException
	 */
	private void datenSischern() throws IOException {
		
		shop.schreibeArtikel();
		shop.schreibeVerlauf();

	}
}
