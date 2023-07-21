package shp.client.ui.swing.panels;

import javax.swing.*;

import shp.common.entities.Artikel;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// Wichtig: Das SearchBooksPanel _ist ein_ Panel und damit auch eine Component;
// es kann daher in das Layout eines anderen Containers 
// (in unserer Anwendung des Frames) eingefügt werden.
public class SearchArtikelsPanel extends JPanel {

	// Über dieses Interface übermittelt das SearchBooksPanel
	// Suchergebnisse an einen Empfänger.
	// In unserem Fall ist der Empfänger die BibGuiMitKomponenten,
	// die dieses Interface implementiert und auf ein neues
	// Suchergebnis reagiert, indem sie die Bücherliste aktualisiert.
	public interface SearchResultListener {
		void onSearchResult(List<Artikel> artikel);

	}

	private E_ShopInterface shop;
	private JTextField searchTextField;
	private JButton searchButton = null;
	private SearchResultListener searchResultListener;

	public SearchArtikelsPanel(E_ShopInterface shop, SearchResultListener searchResultListener) {
		this.shop = shop;
		this.searchResultListener = searchResultListener;

		setupUI();

		setupEvents();
	}

	private void setupUI() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0; // Zeile 0

		JLabel searchLabel = new JLabel("Suchbegriff:");
		c.gridx = 0; // Spalte 0
		c.weightx = 0.2; // 20% der gesamten Breite
		c.anchor = GridBagConstraints.EAST;
		gridBagLayout.setConstraints(searchLabel, c);
		this.add(searchLabel);

		searchTextField = new JTextField();
		searchTextField.setToolTipText("Hier Suchbegriff eingeben.");
		c.gridx = 1; // Spalte 1
		c.weightx = 0.6; // 60% der gesamten Breite
		gridBagLayout.setConstraints(searchTextField, c);
		this.add(searchTextField);

		searchButton = new JButton("Suchen");
		c.gridx = 2; // Spalte 2
		c.weightx = 0.2; // 20% der gesamten Breite
		gridBagLayout.setConstraints(searchButton, c);
		this.add(searchButton);

		// Rahmen definieren
		setBorder(BorderFactory.createTitledBorder("Suchen"));
	}

	private void setupEvents() {
		searchButton.addActionListener(new SuchListener());
	}

	// Lokale Klasse für Reaktion auf Such-Klick
	class SuchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(searchButton)) {
				String suchbegriff = searchTextField.getText();
				List<Artikel> suchErgebnis = new ArrayList<Artikel>();
				if (suchbegriff.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Gib Artikelname ein!", "Info!",
							JOptionPane.INFORMATION_MESSAGE);
					suchErgebnis = shop.gibAlleArtikeln();
				} else {

					try {
						suchErgebnis.add(shop.sucheArtikelNachName(suchbegriff));
					} catch (ArtikelExistiertNichtException e) {

						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						suchErgebnis = shop.gibAlleArtikeln();
					}

				}
				searchTextField.setText("");
				searchResultListener.onSearchResult(suchErgebnis);
			}
		}
	}
}
