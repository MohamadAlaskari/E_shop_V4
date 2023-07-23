package shp.client.ui.swing.panels;

import javax.swing.JPanel;


import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

import shp.client.ui.swing.panels.Registrieren_Panel;
import shp.common.entities.Nutzer;
import shp.common.exceptions.ArtikelExistiertNichtException;
import shp.common.exceptions.NutzernameOderPasswortFalschException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPasswordField;


public class Login_Panel extends JPanel {
	private JPanel panel_LoginContainer = new JPanel();
	private JPanel panel_title = new JPanel();
	private JLabel lbl_mainLogintitle = new JLabel("Login");
	private JPanel panel_btns = new JPanel();

	private JButton btn_login = new JButton("Login");
	private JPanel panel_form = new JPanel();
	private JRadioButton rdbtn_Kunde = new JRadioButton("Kunde");
	private JRadioButton rdbtnMitarbeiter = new JRadioButton("Mitarbeiter");
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPasswordField passwordField = new JPasswordField();
	private JTextField textField_userName = new JTextField();
	private LoginSuccessListener loginSuccessListener;
	private PanelChangeListener panelChangeListener;
	private JPanel registrieren_Panel ;
	//

	private E_ShopInterface shop;
	private Nutzer loggedNutzer;
	private boolean loginStatus= false;
	private final JButton btn_Registrieren = new JButton("Registrieren");
	
	public interface LoginSuccessListener {
	    public void onLoginSuccess(Nutzer nutzer);
	}
	
	public interface PanelChangeListener {
	    void onPanelChange(JPanel newPanel);
	}
	/**
	 * Create the panel.
	 */
	
	
	public Login_Panel(E_ShopInterface shop, LoginSuccessListener loginSuccessListener, PanelChangeListener panelChangeListener) {
		this.shop = shop;
		 this.loginSuccessListener = loginSuccessListener;
		 this.panelChangeListener = panelChangeListener;

		initGUI();

	}

	private void initGUI() {
		setBackground(Color.DARK_GRAY);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_panel_LoginContainer = new GridBagConstraints();
		gbc_panel_LoginContainer.fill = GridBagConstraints.BOTH;
		panel_LoginContainer.setBackground(Color.DARK_GRAY);
		add(panel_LoginContainer, gbc_panel_LoginContainer);
		GridBagLayout gbl_panel_LoginContainer = new GridBagLayout();
		gbl_panel_LoginContainer.columnWeights = new double[] { 0.0 };
		gbl_panel_LoginContainer.rowWeights = new double[] { 1.0, 0.0, 1.0 };
		gbl_panel_LoginContainer.rowHeights = new int[] { 0, 0, 0 };

		panel_LoginContainer.setLayout(gbl_panel_LoginContainer);
		panel_title.setBackground(Color.DARK_GRAY);

		GridBagConstraints gbc_panel_title = new GridBagConstraints();
		gbc_panel_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_title.anchor = GridBagConstraints.NORTH;
		gbc_panel_title.insets = new Insets(0, 0, 5, 0);
		panel_LoginContainer.add(panel_title, gbc_panel_title);
		lbl_mainLogintitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		lbl_mainLogintitle.setForeground(Color.ORANGE);

		panel_title.add(lbl_mainLogintitle);

		GridBagConstraints gbc_panel_form = new GridBagConstraints();
		gbc_panel_form.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_form.anchor = GridBagConstraints.SOUTH;
		gbc_panel_form.insets = new Insets(0, 0, 5, 0);
		gbc_panel_form.gridy = 1;
		panel_form.setBackground(Color.DARK_GRAY);
		panel_LoginContainer.add(panel_form, gbc_panel_form);
		GridBagLayout gbl_panel_form = new GridBagLayout();
		gbl_panel_form.columnWeights = new double[] { 1.0, 1.0 };
		gbl_panel_form.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		panel_form.setLayout(gbl_panel_form);
		GridBagConstraints gbc_rdbtn_Kunde = new GridBagConstraints();
		gbc_rdbtn_Kunde.gridx = 0;
		gbc_rdbtn_Kunde.gridy = 0;
		gbc_rdbtn_Kunde.insets = new Insets(0, 0, 5, 5);
		buttonGroup.add(rdbtn_Kunde);
		
		this.rdbtn_Kunde.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_rdbtn_Kunde_actionPerformed(e);
			}
		});

		this.rdbtn_Kunde.setSelected(true);

		rdbtn_Kunde.setForeground(Color.WHITE);
		rdbtn_Kunde.setBackground(Color.DARK_GRAY);
		panel_form.add(rdbtn_Kunde, gbc_rdbtn_Kunde);

		GridBagConstraints gbc_rdbtnMitarbeiter = new GridBagConstraints();
		gbc_rdbtnMitarbeiter.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnMitarbeiter.gridy = 0;
		gbc_rdbtnMitarbeiter.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnMitarbeiter.gridx = 1;
		buttonGroup.add(rdbtnMitarbeiter);
		this.rdbtnMitarbeiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_rdbtnMitarbeiter_actionPerformed(e);
			}
		});

		rdbtnMitarbeiter.setForeground(Color.WHITE);
		rdbtnMitarbeiter.setBackground(Color.DARK_GRAY);
		panel_form.add(rdbtnMitarbeiter, gbc_rdbtnMitarbeiter);

		GridBagConstraints gbc_textField_userName = new GridBagConstraints();
		gbc_textField_userName.gridwidth = 2;
		gbc_textField_userName.insets = new Insets(0, 0, 5, 0);
		gbc_textField_userName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_userName.gridx = 0;
		gbc_textField_userName.gridy = 1;
		this.textField_userName.setColumns(10);
		this.panel_form.add(this.textField_userName, gbc_textField_userName);

		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 2;
		this.panel_form.add(this.passwordField, gbc_passwordField);

		GridBagConstraints gbc_panel_btns = new GridBagConstraints();
		gbc_panel_btns.fill = GridBagConstraints.BOTH;
		gbc_panel_btns.gridx = 0;
		gbc_panel_btns.gridy = 2;
		panel_btns.setBackground(Color.DARK_GRAY);
		panel_LoginContainer.add(panel_btns, gbc_panel_btns);
		this.btn_Registrieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btn_Registrieren_actionPerformed(e);
			}
		});

		this.panel_btns.add(this.btn_Registrieren);
		this.btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btn_login_actionPerformed(e);
			}
		});

		panel_btns.add(btn_login);
	}

	protected void do_rdbtn_Kunde_actionPerformed(ActionEvent e) {
		if (rdbtn_Kunde.isSelected()) {
			btn_Registrieren.setVisible(true);
		}
		
	}

	protected void do_rdbtnMitarbeiter_actionPerformed(ActionEvent e) {
		btn_Registrieren.setVisible(false);
	}

	protected void do_btn_login_actionPerformed(ActionEvent e) {
		String userName = textField_userName.getText().trim();
		char[] passwordArr = passwordField.getPassword();
		String password = new String(passwordArr).trim();

		if (userName.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle erforderlichen Felder aus");

		} else {

			if (rdbtnMitarbeiter.isSelected()) {
				try {
					loggedNutzer = shop.mitarbeiterEinloggen(userName, password);
					JOptionPane.showMessageDialog(null, "Du hast erfolgreich angemeldet!", "Info Message",
							JOptionPane.INFORMATION_MESSAGE);
					loginStatus = true;
					loginSuccessListener.onLoginSuccess(loggedNutzer);
				} catch (NutzernameOderPasswortFalschException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Info Message", JOptionPane.ERROR_MESSAGE);
				}
			}else if(rdbtn_Kunde.isSelected()) {
				try {
					loggedNutzer = shop.kundenEinloggen(userName, password);
					JOptionPane.showMessageDialog(null, "Du hast erfolgreich angemeldet!", "Info Message",
							JOptionPane.INFORMATION_MESSAGE);
					loginStatus = true;
					loginSuccessListener.onLoginSuccess(loggedNutzer);
				} catch (NutzernameOderPasswortFalschException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Info Message", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	protected void do_btn_Registrieren_actionPerformed(ActionEvent e) {
		this.registrieren_Panel = new Registrieren_Panel(shop, loginSuccessListener, panelChangeListener );
		panelChangeListener.onPanelChange(registrieren_Panel);
		
		
	}
	
	public Nutzer getLoggedNutzer() {
		return loggedNutzer;
	}

	public boolean isLoginStatus() {
		return loginStatus;
	}

}
