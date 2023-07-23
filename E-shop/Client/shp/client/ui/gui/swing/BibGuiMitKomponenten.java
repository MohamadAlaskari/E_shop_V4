package shp.client.ui.gui.swing;

import javax.swing.*;

import shp.client.net.EshopsFassade;
import shp.client.ui.swing.models.ArtikelTableModel;
import shp.client.ui.swing.models.ArtikelTableModel2Kunde;
import shp.client.ui.swing.models.VerlaufTableModel;
import shp.client.ui.swing.models.WarenkorbModel;
import shp.client.ui.swing.panels.AddArtikelPanel;
import shp.client.ui.swing.panels.ArtikelnTablePanel;
import shp.client.ui.swing.panels.KundenMenuePanel;
import shp.client.ui.swing.panels.Login_Panel;
import shp.client.ui.swing.panels.LogoutPanel;
import shp.client.ui.swing.panels.MitarbeiterMenuePanel;
import shp.client.ui.swing.panels.SearchArtikelsPanel;
import shp.client.ui.swing.panels.KundenMenuePanel.OnWarenkorpListener;
import shp.common.entities.Artikel;
import shp.common.entities.Kunde;
import shp.common.entities.Mitarbeiter;
import shp.common.entities.Nutzer;
import shp.common.entities.Verlauf;
import shp.common.exceptions.ArtikelExistiertBereitsException;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.BestandPasstNichtMitPackungsGroesseException;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.exceptions.VerlaufLeerException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BibGuiMitKomponenten extends JFrame implements SearchArtikelsPanel.SearchResultListener,
		MitarbeiterMenuePanel.TableDataListener, Login_Panel.LoginSuccessListener, Login_Panel.PanelChangeListener,
		LogoutPanel.PanelChangeBeiLogout, KundenMenuePanel.OnWarenkorpListener {

	private E_ShopInterface shop;
	private E_ShopInterface fas;

	private SearchArtikelsPanel searchPanel;
	private AddArtikelPanel addArtikelPanel;
	private KundenMenuePanel kundenMenuePanel;
	private LogoutPanel logoutPanel;
	private ArtikelnTablePanel artikelnTablePanel;
	private JPanel loginPanel;
	private MitarbeiterMenuePanel mitarbeiterMenuePanel;
	private WarenkorbModel warenkorbModel;

	private Nutzer loggedNutzer;
	private List<Artikel> artikeln;
	private ArtikelTableModel2Kunde artikelTableModel2Kunde;
	public ArtikelTableModel artikelTableModel;
	JScrollPane scrollPane;
	private HashMap<Artikel, Integer> warenkorb;
	private List<Verlauf> verlaufListe;
	private VerlaufTableModel verTableModel;
	public static final int DEFAULT_PORT = 6789;

	public static void main(String[] args) {
		int portArg = 0;
		String hostArg = null;
		InetAddress ia = null;

		// ---
		// Hier werden die main-Parameter geprüft:
		// ---

		// Host- und Port-Argument einlesen, wenn angegeben
		if (args.length > 2) {
			System.out.println("Aufruf: java <Klassenname> [<hostname> [<port>]]");
			System.exit(0);
		}
		switch (args.length) {
		case 0:
			try {
				ia = InetAddress.getLocalHost();
			} catch (Exception e) {
				System.out.println("XXX InetAdress-Fehler: " + e);
				System.exit(0);
			}
			hostArg = ia.getHostName(); // host ist lokale Maschine
			portArg = DEFAULT_PORT;
			break;
		case 1:
			portArg = DEFAULT_PORT;
			hostArg = args[0];
			break;
		case 2:
			hostArg = args[0];
			try {
				portArg = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("Aufruf: java BibClientGUI [<hostname> [<port>]]");
				System.exit(0);
			}
		}

		// Swing-UI auf dem GUI-Thread initialisieren
		// (host und port müssen für Verwendung in inner class final sein)
		final String host = hostArg;
		final int port = portArg;
		// Start der Anwendung (per anonymer Klasse)
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BibGuiMitKomponenten gui = new BibGuiMitKomponenten("EShop", host, port);
				} catch (ArtikelExistiertBereitsException | ArtikelExistiertNichtException
						| MitarbeiterUsernameIstBenutztException | ParseException
						| BestandPasstNichtMitPackungsGroesseException e) {
					e.printStackTrace();
				}
			}
		});

//		// Start der Anwendung (per Lambda-Expression)
//		SwingUtilities.invokeLater(() -> { BibGuiAusVL gui = new BibGuiAusVL("Bibliothek"); });
	}

	public BibGuiMitKomponenten(String titel, String host, int port)
			throws ArtikelExistiertBereitsException, ArtikelExistiertNichtException,
			MitarbeiterUsernameIstBenutztException, ParseException, BestandPasstNichtMitPackungsGroesseException {
		super(titel);

		try {
			shop = new EshopsFassade(host, port, loggedNutzer);
//			fas = new EshopsFassade(host, port, loggedNutzer);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		runProg();

	}

	private void runProg() {

		getContentPane().removeAll();
		loginPanel = new Login_Panel(this.shop, this, this);
		getContentPane().add(loginPanel);
		setSize(640, 480);
		setVisible(true);

	}

	private void initialize(Nutzer loggednutzer) {
		this.loggedNutzer = loggednutzer;

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowCloser());

		// layout
		getContentPane().setLayout(new BorderLayout());

		// Menü initialisieren
		setupMenu();

		// North
		searchPanel = new SearchArtikelsPanel(shop, this);
		getContentPane().add(searchPanel, BorderLayout.NORTH);

		// Center
		artikeln = shop.gibAlleArtikeln();

		// south
		logoutPanel = new LogoutPanel(shop, this.loggedNutzer, this);
		getContentPane().add(logoutPanel, BorderLayout.SOUTH);

		if (shop.sucheMitarbeiter(loggednutzer.getNutzerName()) != null) {
			// West
			addArtikelPanel = new AddArtikelPanel(shop, this.loggedNutzer, this);
			getContentPane().add(addArtikelPanel, BorderLayout.WEST);

			artikelnTablePanel = new ArtikelnTablePanel(this.shop, (Mitarbeiter) loggednutzer);

			// center
			scrollPane = new JScrollPane(artikelnTablePanel);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			scrollPane.setBorder(BorderFactory.createTitledBorder("Artikeln"));
			// East
			mitarbeiterMenuePanel = new MitarbeiterMenuePanel(this, shop, loggedNutzer);
			getContentPane().add(mitarbeiterMenuePanel, BorderLayout.EAST);

		} else if (shop.sucheKunde(loggednutzer.getNutzerName()) != null) {
			Kunde k = shop.sucheKunde(loggednutzer.getNutzerName());
			warenkorbModel = new WarenkorbModel(k.getKundeWarenkorb().getKorbArtikelListe());
			kundenMenuePanel = new KundenMenuePanel(shop, (Kunde) loggednutzer, this);
			getContentPane().add(kundenMenuePanel, BorderLayout.WEST);
			// center

			// TODO the next row have to remove, because the ArtikelTableModel2Kunde is
			// already intialisiert in constructor von artikelnTablePanel -->
			// --> artikelTableModel2Kunde = new ArtikelTableModel2Kunde(artikeln);

			artikelTableModel2Kunde = new ArtikelTableModel2Kunde(artikeln);
			artikelnTablePanel = new ArtikelnTablePanel(this.shop, (Kunde) loggednutzer);
			scrollPane = new JScrollPane(artikelnTablePanel);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			scrollPane.setBorder(BorderFactory.createTitledBorder("Artikeln"));

		}

		this.setSize(640, 480);
		this.setVisible(true);
	}

	private void setupMenu() {
		// Menüleiste anlegen ...
		JMenuBar mBar = new JMenuBar();

		JMenu fileMenu = new FileMenu();
		mBar.add(fileMenu);

		JMenu helpMenu = new HelpMenu();
		mBar.add(helpMenu);

		// ... und beim Fenster anmelden
		this.setJMenuBar(mBar);
	}

	/*
	 * (non-Javadoc)
	 *
	 * Mitgliedsklasse für File-Menü
	 *
	 */
	class FileMenu extends JMenu implements ActionListener {

		public FileMenu() {
			super("File");

			JMenuItem saveItem = new JMenuItem("Save");
			saveItem.addActionListener(this);
			add(saveItem);

			addSeparator();

			JMenuItem quitItem = new JMenuItem("Quit");
			quitItem.addActionListener(this);
			add(quitItem);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "Save":
				try {
					shop.schreibeArtikel();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				break;
			case "Quit":
				// Nur "this" ginge nicht, weil "this" auf das FileMenu-Objekt zeigt.
				// "BibGuiAusVL.this" zeigt auf das dieses (innere) FileMenu-Objekt
				// umgebende Objekt der Klasse BibGuiAusVL.
				BibGuiMitKomponenten.this.setVisible(false);
				BibGuiMitKomponenten.this.dispose();
				System.exit(0);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * Mitgliedsklasse für Help-Menü
	 *
	 */
	class HelpMenu extends JMenu implements ActionListener {

		public HelpMenu() {
			super("Help");

			// Nur zu Testzwecken: Menü mit Untermenü
			JMenu m = new JMenu("About");
			JMenuItem mi = new JMenuItem("Programmers");
			mi.addActionListener(this);
			m.add(mi);
			mi = new JMenuItem("Stuff");
			mi.addActionListener(this);
			m.add(mi);
			this.add(m);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Klick auf Menü '" + e.getActionCommand() + "'.");
		}
	}

	@Override
	public void onPanelChangeBeiLogout() {
		runProg();
	}

	@Override
	public void onLoginSuccess(Nutzer loggednutzer) {
		this.loggedNutzer = loggednutzer;

		getContentPane().removeAll();
		initialize(loggednutzer);
	/*	if (shop.sucheKunde(loggednutzer.getNutzerName()) != null) {
			
		} else if (shop.sucheMitarbeiter(loggednutzer.getNutzerName()) != null) {
			initialize(loggednutzer);
		}*/
	}

	@Override
	public void onPanelChange(JPanel newPanel) {

		getContentPane().remove(loginPanel); // Wir entfernen das alte Panel
		loginPanel = newPanel; // Wir speichern das neue Panel
		getContentPane().add(loginPanel); // Wir fügen das neue Panel hinzu

		validate(); // Wir aktualisieren das GUI
		repaint();
	}

	@Override
	public void onSearchResult(java.util.List<Artikel> artikeln) {
		artikelTableModel = new ArtikelTableModel(artikeln);
		artikelnTablePanel.setModel(artikelTableModel);
		artikelnTablePanel.updateArtikelnList(artikeln);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Artikeln"));

	}

	@Override
	public void updateTable() {
		artikeln = shop.gibAlleArtikeln();
		artikelTableModel = new ArtikelTableModel(artikeln);
		artikelnTablePanel.setModel(artikelTableModel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Artikeln"));
	}

	@Override
	public void updateToVerlauf(List<Verlauf> verlaufListe) {

		verTableModel = new VerlaufTableModel(verlaufListe);
		artikelnTablePanel.setModel(verTableModel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Verlauf"));
	}

	@Override
	public void updateWarenKorb() {
		Kunde k = (Kunde) loggedNutzer;

		warenkorb = k.getKundeWarenkorb().getKorbArtikelListe();
		warenkorbModel.setWarenkorb(warenkorb, k.getKundeWarenkorb());
		artikelnTablePanel.setModel(warenkorbModel);

		scrollPane.setBorder(BorderFactory.createTitledBorder("WarenKorb"));

	}

	@Override
	public Artikel onSelctedRow() {

		ArtikelTableModel artikeltabelModel = new ArtikelTableModel(artikeln);
		// -1 bedeuted dass keines row selected
		int selectedRow = artikelnTablePanel.selectedrowIndex();
		Artikel arikel = null;

		if (selectedRow != -1 && artikelnTablePanel.getSelectedRowCount() == 1) {
			arikel = artikeltabelModel.getSelecetedArtikel(selectedRow);
		}
		return arikel;

	}

	// für kunden
	@Override
	public void updateToArtikel(List<Artikel> artikeln) {
		artikelTableModel2Kunde = new ArtikelTableModel2Kunde(artikeln);
		artikelnTablePanel.setModel(artikelTableModel2Kunde);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Artikeln"));
	}

	@Override
	public Artikel onSelectedRow_Kunde() {

		// -1 bedeuted dass keines row selected
		int selectedRow = artikelnTablePanel.selectedrowIndex();
		Artikel arikel = null;

		if (selectedRow != -1 && artikelnTablePanel.getSelectedRowCount() == 1) {
			arikel = artikelTableModel2Kunde.getSelecetedArtikel(selectedRow);
		}
		return arikel;
	}

	@Override
	public Artikel onSelectedRow_Warenkorb() {

		// -1 bedeuted dass keines row selected
		int selectedRow = artikelnTablePanel.selectedrowIndex();
		Artikel arikel = null;

		if (selectedRow != -1 && artikelnTablePanel.getSelectedRowCount() == 1
				&& selectedRow != warenkorbModel.getRowCount() - 1) {
			arikel = warenkorbModel.getSelecetedArtikel(selectedRow);
		}
		return arikel;
	}

}
