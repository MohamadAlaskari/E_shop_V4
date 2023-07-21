package shp.client.ui.swing.panels;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import shp.client.ui.swing.panels.Login_Panel.LoginSuccessListener;
import shp.client.ui.swing.panels.Login_Panel.PanelChangeListener;
import shp.common.exceptions.KundeUsernameIstbenutztException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Cursor;

public class Registrieren_Panel extends JPanel {
	private JPanel panel_registrierenContainer = new JPanel();
	private JPanel panel_title = new JPanel();
	private JLabel lbl_RegistrierenTitle = new JLabel("Registrieren");
	private JPanel panel_form = new JPanel();
	private JPanel panel_btns = new JPanel();
	private JButton btn_registrieren = new JButton("Registrieren");
	private JButton btn_login = new JButton("Login");
	private JTextField textField_Name = new JTextField();
	private JTextField textField_Vorname = new JTextField();
	private JTextField textField_Strasse = new JTextField();
	private JTextField textField_HausNr = new JTextField();
	private JTextField textField_Plz = new JTextField();
	private JTextField textField_Ort = new JTextField();
	private JRadioButton rdbtn_Deutschland = new JRadioButton("Deutschland");
	private JRadioButton rdbtn_Oestereich = new JRadioButton("Östereich");
	private JTextField textField_UserName = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel lbl_Name = new JLabel("Name");
	private JLabel lbl_Vorname = new JLabel("Vorname");
	private JLabel lbl_Strasse = new JLabel("Strasse");
	private JLabel lbl_Plz = new JLabel("Plz.");
	private JLabel lbl_Username = new JLabel("Username");
	private JLabel lbl_Password = new JLabel("Password");
	private JLabel lbl_HausNr = new JLabel("HausNr.");
	private JLabel lbl_Ort = new JLabel("Ort");
	private ButtonGroup buttonGroup = new ButtonGroup();
	 private LoginSuccessListener loginSuccessListener;
	 private PanelChangeListener panelChangeListener;
	 private Login_Panel loginPanel;

	


	// eshop
	private E_ShopInterface shop;

	/**
	 * Create the panel.
	 */
	public Registrieren_Panel(E_ShopInterface shop, LoginSuccessListener loginSuccessListener, PanelChangeListener panelChangeListener) {
	    this.loginSuccessListener = loginSuccessListener;
	    this.panelChangeListener = panelChangeListener;
		this.shop = shop;

		initGUI();
	}

	private void initGUI() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 87, 0 };
		gridBagLayout.rowHeights = new int[] { 123, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_panel_registrierenContainer = new GridBagConstraints();
		gbc_panel_registrierenContainer.fill = GridBagConstraints.BOTH;
		gbc_panel_registrierenContainer.gridx = 0;
		gbc_panel_registrierenContainer.gridy = 0;
		panel_registrierenContainer.setBackground(Color.DARK_GRAY);
		add(panel_registrierenContainer, gbc_panel_registrierenContainer);
		GridBagLayout gbl_panel_registrierenContainer = new GridBagLayout();
		gbl_panel_registrierenContainer.columnWidths = new int[] { 276, 0 };
		gbl_panel_registrierenContainer.rowHeights = new int[] { 43, 0, 0, 0 };
		gbl_panel_registrierenContainer.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_registrierenContainer.rowWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		panel_registrierenContainer.setLayout(gbl_panel_registrierenContainer);

		GridBagConstraints gbc_panel_title = new GridBagConstraints();
		gbc_panel_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_title.insets = new Insets(0, 0, 5, 0);
		gbc_panel_title.gridx = 0;
		gbc_panel_title.gridy = 0;
		panel_title.setBackground(Color.DARK_GRAY);
		panel_registrierenContainer.add(panel_title, gbc_panel_title);
		lbl_RegistrierenTitle.setForeground(Color.ORANGE);
		lbl_RegistrierenTitle.setFont(new Font("Tahoma", Font.BOLD, 24));

		panel_title.add(lbl_RegistrierenTitle);

		GridBagConstraints gbc_panel_form = new GridBagConstraints();
		gbc_panel_form.anchor = GridBagConstraints.SOUTH;
		gbc_panel_form.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_form.insets = new Insets(0, 0, 5, 0);
		gbc_panel_form.gridx = 0;
		gbc_panel_form.gridy = 1;
		panel_form.setBackground(Color.DARK_GRAY);
		panel_registrierenContainer.add(panel_form, gbc_panel_form);
		GridBagLayout gbl_panel_form = new GridBagLayout();
		gbl_panel_form.columnWidths = new int[] { 22, 48, 0, 64, 0, 17, 0 };
		gbl_panel_form.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_form.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_form.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_form.setLayout(gbl_panel_form);

		GridBagConstraints gbc_lbl_Name = new GridBagConstraints();
		gbc_lbl_Name.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Name.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lbl_Name.gridx = 1;
		gbc_lbl_Name.gridy = 0;
		lbl_Name.setForeground(Color.WHITE);
		panel_form.add(lbl_Name, gbc_lbl_Name);

		GridBagConstraints gbc_textField_Name = new GridBagConstraints();
		gbc_textField_Name.anchor = GridBagConstraints.SOUTH;
		gbc_textField_Name.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Name.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Name.gridx = 2;
		gbc_textField_Name.gridy = 0;
		panel_form.add(textField_Name, gbc_textField_Name);

		GridBagConstraints gbc_lbl_Vorname = new GridBagConstraints();
		gbc_lbl_Vorname.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Vorname.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lbl_Vorname.gridx = 3;
		gbc_lbl_Vorname.gridy = 0;
		lbl_Vorname.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl_Vorname.setForeground(Color.WHITE);
		panel_form.add(lbl_Vorname, gbc_lbl_Vorname);

		GridBagConstraints gbc_textField_Vorname = new GridBagConstraints();
		gbc_textField_Vorname.anchor = GridBagConstraints.SOUTH;
		gbc_textField_Vorname.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Vorname.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Vorname.gridx = 4;
		gbc_textField_Vorname.gridy = 0;
		panel_form.add(textField_Vorname, gbc_textField_Vorname);

		GridBagConstraints gbc_lbl_Strasse = new GridBagConstraints();
		gbc_lbl_Strasse.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Strasse.anchor = GridBagConstraints.EAST;
		gbc_lbl_Strasse.gridx = 1;
		gbc_lbl_Strasse.gridy = 1;
		lbl_Strasse.setForeground(Color.WHITE);
		panel_form.add(lbl_Strasse, gbc_lbl_Strasse);

		GridBagConstraints gbc_textField_Strasse = new GridBagConstraints();
		gbc_textField_Strasse.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Strasse.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Strasse.gridx = 2;
		gbc_textField_Strasse.gridy = 1;
		panel_form.add(textField_Strasse, gbc_textField_Strasse);

		GridBagConstraints gbc_lbl_HausNr = new GridBagConstraints();
		gbc_lbl_HausNr.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_HausNr.anchor = GridBagConstraints.EAST;
		gbc_lbl_HausNr.gridx = 3;
		gbc_lbl_HausNr.gridy = 1;
		lbl_HausNr.setForeground(Color.WHITE);
		panel_form.add(lbl_HausNr, gbc_lbl_HausNr);

		GridBagConstraints gbc_textField_HausNr = new GridBagConstraints();
		gbc_textField_HausNr.insets = new Insets(0, 0, 5, 5);
		gbc_textField_HausNr.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_HausNr.gridx = 4;
		gbc_textField_HausNr.gridy = 1;
		panel_form.add(textField_HausNr, gbc_textField_HausNr);

		GridBagConstraints gbc_lbl_Plz = new GridBagConstraints();
		gbc_lbl_Plz.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Plz.anchor = GridBagConstraints.EAST;
		gbc_lbl_Plz.gridx = 1;
		gbc_lbl_Plz.gridy = 2;
		lbl_Plz.setForeground(Color.WHITE);
		panel_form.add(lbl_Plz, gbc_lbl_Plz);

		GridBagConstraints gbc_textField_Plz = new GridBagConstraints();
		gbc_textField_Plz.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Plz.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Plz.gridx = 2;
		gbc_textField_Plz.gridy = 2;
		panel_form.add(textField_Plz, gbc_textField_Plz);

		GridBagConstraints gbc_lbl_Ort = new GridBagConstraints();
		gbc_lbl_Ort.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Ort.anchor = GridBagConstraints.EAST;
		gbc_lbl_Ort.gridx = 3;
		gbc_lbl_Ort.gridy = 2;
		lbl_Ort.setForeground(Color.WHITE);
		panel_form.add(lbl_Ort, gbc_lbl_Ort);

		GridBagConstraints gbc_textField_Ort = new GridBagConstraints();
		gbc_textField_Ort.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Ort.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Ort.gridx = 4;
		gbc_textField_Ort.gridy = 2;
		panel_form.add(textField_Ort, gbc_textField_Ort);

		GridBagConstraints gbc_rdbtn_Deutschland = new GridBagConstraints();
		gbc_rdbtn_Deutschland.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtn_Deutschland.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtn_Deutschland.gridx = 2;
		gbc_rdbtn_Deutschland.gridy = 3;
		buttonGroup.add(rdbtn_Deutschland);
		rdbtn_Deutschland.setForeground(Color.WHITE);
		rdbtn_Deutschland.setBackground(Color.DARK_GRAY);
		panel_form.add(rdbtn_Deutschland, gbc_rdbtn_Deutschland);

		GridBagConstraints gbc_rdbtn_Oestereich = new GridBagConstraints();
		gbc_rdbtn_Oestereich.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtn_Oestereich.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtn_Oestereich.gridx = 4;
		gbc_rdbtn_Oestereich.gridy = 3;
		buttonGroup.add(rdbtn_Oestereich);
		rdbtn_Oestereich.setForeground(Color.WHITE);
		rdbtn_Oestereich.setBackground(Color.DARK_GRAY);
		panel_form.add(rdbtn_Oestereich, gbc_rdbtn_Oestereich);

		GridBagConstraints gbc_lbl_Username = new GridBagConstraints();
		gbc_lbl_Username.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Username.anchor = GridBagConstraints.EAST;
		gbc_lbl_Username.gridx = 1;
		gbc_lbl_Username.gridy = 4;
		lbl_Username.setForeground(Color.WHITE);
		panel_form.add(lbl_Username, gbc_lbl_Username);

		GridBagConstraints gbc_textField_UserName = new GridBagConstraints();
		gbc_textField_UserName.gridwidth = 3;
		gbc_textField_UserName.insets = new Insets(0, 0, 5, 5);
		gbc_textField_UserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_UserName.gridx = 2;
		gbc_textField_UserName.gridy = 4;
		panel_form.add(textField_UserName, gbc_textField_UserName);

		GridBagConstraints gbc_lbl_Password = new GridBagConstraints();
		gbc_lbl_Password.insets = new Insets(0, 0, 0, 5);
		gbc_lbl_Password.anchor = GridBagConstraints.EAST;
		gbc_lbl_Password.gridx = 1;
		gbc_lbl_Password.gridy = 5;
		lbl_Password.setForeground(Color.WHITE);
		panel_form.add(lbl_Password, gbc_lbl_Password);

		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 3;
		gbc_passwordField.insets = new Insets(0, 0, 0, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 5;
		this.panel_form.add(this.passwordField, gbc_passwordField);

		GridBagConstraints gbc_panel_btns = new GridBagConstraints();
		gbc_panel_btns.fill = GridBagConstraints.BOTH;
		gbc_panel_btns.gridx = 0;
		gbc_panel_btns.gridy = 2;
		panel_btns.setBackground(Color.DARK_GRAY);
		panel_registrierenContainer.add(panel_btns, gbc_panel_btns);
		this.btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btn_login_actionPerformed(e);
			}
		});
		this.btn_login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		

		panel_btns.add(btn_login);
		this.btn_registrieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btn_registrieren_actionPerformed(e);
			}
		});
		this.btn_registrieren.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginPanel = new Login_Panel(shop, loginSuccessListener, panelChangeListener);
		panel_btns.add(btn_registrieren);
	}

	

	

	protected void do_btn_login_actionPerformed(ActionEvent e) {
	    panelChangeListener.onPanelChange(loginPanel);
	}
	
	protected void do_btn_registrieren_actionPerformed(ActionEvent e) {
		String name = textField_Name.getText().trim();
		String vorName = textField_Vorname.getText().trim();
		String strasse = textField_Strasse.getText().trim();
		String hausNr = textField_HausNr.getText().trim();
		String plz = textField_Plz.getText().trim();
		String ort = textField_Ort.getText().trim();
		String land = "";

		if (rdbtn_Deutschland.isSelected()) {
			land = "Deutschlad";
		} else if (rdbtn_Oestereich.isSelected()) {
			land = "Österreich";
		}
		String userName = textField_UserName.getText().trim();
		char[] passwordArr = passwordField.getPassword();
		String password = new String(passwordArr).trim();

		if (name.isEmpty() || vorName.isEmpty() || strasse.isEmpty() || hausNr.isEmpty() || plz.isEmpty()
				|| ort.isEmpty() || land.isEmpty() || userName.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle erforderlichen Felder aus");
		} else {

			try {
				shop.kundenRegistrieren(name, vorName, userName, password, strasse, hausNr, plz, ort, land);

				shop.schreibeKunde();

				JOptionPane.showMessageDialog(null, "Du hast dich erfolgreich registriert!", "Info Message",
						JOptionPane.INFORMATION_MESSAGE);

				 panelChangeListener.onPanelChange(loginPanel);
			} catch (KundeUsernameIstbenutztException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
