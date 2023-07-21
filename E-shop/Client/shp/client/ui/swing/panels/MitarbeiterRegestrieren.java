package shp.client.ui.swing.panels;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;

import shp.common.entities.Mitarbeiter;
import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import javax.swing.JPasswordField;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MitarbeiterRegestrieren extends JPanel {
	private JLabel lbl_Name;
	private JLabel lbl_Vorname;
	private JLabel lbl_Nutzername;
	private JLabel lbl_Password;
	private JTextField textField_Name;
	private JTextField textField_nutzerName;
	private JPasswordField passwordField_Password;
	private JTextField textField_Vorname;
	private JButton btn_MA_regestrieren;

	private E_ShopInterface shop;
	private OnMitarbeiter_Register onMitarbeiter_Register;

	/**
	 * Create the panel.
	 */
	public interface OnMitarbeiter_Register {
		public void centerpanel_switcher();
	}

	public MitarbeiterRegestrieren(E_ShopInterface shop, OnMitarbeiter_Register onMitarbeiter_Register) {
		this.shop = shop;
		this.onMitarbeiter_Register = onMitarbeiter_Register;
		initGUI();

	}

	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		{
			this.lbl_Name = new JLabel("Name:");
			GridBagConstraints gbc_lbl_Name = new GridBagConstraints();
			gbc_lbl_Name.anchor = GridBagConstraints.EAST;
			gbc_lbl_Name.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Name.gridx = 0;
			gbc_lbl_Name.gridy = 0;
			add(this.lbl_Name, gbc_lbl_Name);
		}
		{
			this.textField_Name = new JTextField();
			GridBagConstraints gbc_textField_Name = new GridBagConstraints();
			gbc_textField_Name.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_Name.insets = new Insets(0, 0, 5, 0);
			gbc_textField_Name.gridx = 1;
			gbc_textField_Name.gridy = 0;
			add(this.textField_Name, gbc_textField_Name);
			this.textField_Name.setColumns(10);
		}
		{
			this.lbl_Vorname = new JLabel("Vorname:");
			GridBagConstraints gbc_lbl_Vorname = new GridBagConstraints();
			gbc_lbl_Vorname.anchor = GridBagConstraints.EAST;
			gbc_lbl_Vorname.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Vorname.gridx = 0;
			gbc_lbl_Vorname.gridy = 1;
			add(this.lbl_Vorname, gbc_lbl_Vorname);
		}
		{
			this.textField_Vorname = new JTextField();
			GridBagConstraints gbc_textField_Vorname = new GridBagConstraints();
			gbc_textField_Vorname.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_Vorname.insets = new Insets(0, 0, 5, 0);
			gbc_textField_Vorname.gridx = 1;
			gbc_textField_Vorname.gridy = 1;
			add(this.textField_Vorname, gbc_textField_Vorname);
			this.textField_Vorname.setColumns(10);
		}
		{
			this.lbl_Nutzername = new JLabel("Nutzername:");
			GridBagConstraints gbc_lbl_Nutzername = new GridBagConstraints();
			gbc_lbl_Nutzername.anchor = GridBagConstraints.EAST;
			gbc_lbl_Nutzername.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Nutzername.gridx = 0;
			gbc_lbl_Nutzername.gridy = 2;
			add(this.lbl_Nutzername, gbc_lbl_Nutzername);
		}
		{
			this.textField_nutzerName = new JTextField();
			GridBagConstraints gbc_textField_nutzerName = new GridBagConstraints();
			gbc_textField_nutzerName.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_nutzerName.insets = new Insets(0, 0, 5, 0);
			gbc_textField_nutzerName.gridx = 1;
			gbc_textField_nutzerName.gridy = 2;
			add(this.textField_nutzerName, gbc_textField_nutzerName);
			this.textField_nutzerName.setColumns(10);
		}
		{
			this.lbl_Password = new JLabel("Password:");
			GridBagConstraints gbc_lbl_Password = new GridBagConstraints();
			gbc_lbl_Password.anchor = GridBagConstraints.EAST;
			gbc_lbl_Password.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Password.gridx = 0;
			gbc_lbl_Password.gridy = 3;
			add(this.lbl_Password, gbc_lbl_Password);
		}
		{
			this.passwordField_Password = new JPasswordField();
			GridBagConstraints gbc_passwordField_Password = new GridBagConstraints();
			gbc_passwordField_Password.insets = new Insets(0, 0, 5, 0);
			gbc_passwordField_Password.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField_Password.gridx = 1;
			gbc_passwordField_Password.gridy = 3;
			add(this.passwordField_Password, gbc_passwordField_Password);
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
			gbc_btn_MA_regestrieren.fill = GridBagConstraints.HORIZONTAL;
			gbc_btn_MA_regestrieren.gridwidth = 2;
			gbc_btn_MA_regestrieren.insets = new Insets(0, 0, 0, 5);
			gbc_btn_MA_regestrieren.gridx = 0;
			gbc_btn_MA_regestrieren.gridy = 4;
			add(this.btn_MA_regestrieren, gbc_btn_MA_regestrieren);
		}
	}

	protected void do_btn_MA_regestrieren_actionPerformed(ActionEvent e) {
		String name = textField_Name.getText().trim();
		String vorName = textField_Vorname.getText().trim();
		String nutzerName = textField_nutzerName.getText().trim();
		char[] passwordArr = passwordField_Password.getPassword();
		String password = new String(passwordArr).trim();

		if (name.isEmpty() || vorName.isEmpty() || nutzerName.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle erforderlichen Felder aus");
		} else {
			try {
				 shop.regestiereNeueMitarbeiter(name, vorName, nutzerName, password);
				onMitarbeiter_Register.centerpanel_switcher();

			} catch (MitarbeiterUsernameIstBenutztException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);

			}
		}
	}
}
