package shp.client.ui.swing.panels;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import shp.common.exceptions.MitarbeiterUsernameIstBenutztException;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JPasswordField;

public class MARegestrierenDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_name;
	private JTextField textField_vorname;
	private JTextField textField_nutzerName;
	private JPasswordField passwordField;
	private E_ShopInterface shop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			E_ShopInterface shop= null;
			MARegestrierenDialog dialog = new MARegestrierenDialog(shop);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MARegestrierenDialog(E_ShopInterface shop) {
		this.shop = shop;
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setBounds(100, 100, 450, 186);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lbl_name = new JLabel("Name:");
			GridBagConstraints gbc_lbl_name = new GridBagConstraints();
			gbc_lbl_name.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_name.anchor = GridBagConstraints.EAST;
			gbc_lbl_name.gridx = 0;
			gbc_lbl_name.gridy = 0;
			this.contentPanel.add(lbl_name, gbc_lbl_name);
		}
		{
			this.textField_name = new JTextField();
			GridBagConstraints gbc_textField_name = new GridBagConstraints();
			gbc_textField_name.insets = new Insets(0, 0, 5, 0);
			gbc_textField_name.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_name.gridx = 1;
			gbc_textField_name.gridy = 0;
			this.contentPanel.add(this.textField_name, gbc_textField_name);
			this.textField_name.setColumns(10);
		}
		{
			JLabel lbl_Vorname = new JLabel("Vorname:");
			GridBagConstraints gbc_lbl_Vorname = new GridBagConstraints();
			gbc_lbl_Vorname.anchor = GridBagConstraints.EAST;
			gbc_lbl_Vorname.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Vorname.gridx = 0;
			gbc_lbl_Vorname.gridy = 1;
			this.contentPanel.add(lbl_Vorname, gbc_lbl_Vorname);
		}
		{
			this.textField_vorname = new JTextField();
			GridBagConstraints gbc_textField_vorname = new GridBagConstraints();
			gbc_textField_vorname.insets = new Insets(0, 0, 5, 0);
			gbc_textField_vorname.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_vorname.gridx = 1;
			gbc_textField_vorname.gridy = 1;
			this.contentPanel.add(this.textField_vorname, gbc_textField_vorname);
			this.textField_vorname.setColumns(10);
		}
		{
			JLabel lbl_Nutzername = new JLabel("Nutzer Name:");
			GridBagConstraints gbc_lbl_Nutzername = new GridBagConstraints();
			gbc_lbl_Nutzername.anchor = GridBagConstraints.EAST;
			gbc_lbl_Nutzername.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_Nutzername.gridx = 0;
			gbc_lbl_Nutzername.gridy = 2;
			this.contentPanel.add(lbl_Nutzername, gbc_lbl_Nutzername);
		}
		{
			this.textField_nutzerName = new JTextField();
			GridBagConstraints gbc_textField_nutzerName = new GridBagConstraints();
			gbc_textField_nutzerName.insets = new Insets(0, 0, 5, 0);
			gbc_textField_nutzerName.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_nutzerName.gridx = 1;
			gbc_textField_nutzerName.gridy = 2;
			this.contentPanel.add(this.textField_nutzerName, gbc_textField_nutzerName);
			this.textField_nutzerName.setColumns(10);
		}
		{
			JLabel lbl_Password = new JLabel("Password:");
			GridBagConstraints gbc_lbl_Password = new GridBagConstraints();
			gbc_lbl_Password.anchor = GridBagConstraints.EAST;
			gbc_lbl_Password.insets = new Insets(0, 0, 0, 5);
			gbc_lbl_Password.gridx = 0;
			gbc_lbl_Password.gridy = 3;
			this.contentPanel.add(lbl_Password, gbc_lbl_Password);
		}
		{
			this.passwordField = new JPasswordField();
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 1;
			gbc_passwordField.gridy = 3;
			this.contentPanel.add(this.passwordField, gbc_passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btn_regestrieren = new JButton("regestrieren");
				btn_regestrieren.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btn_regestrieren_actionPerformed(e);
					}
				});

				btn_regestrieren.setActionCommand("Cancel");
				buttonPane.add(btn_regestrieren);
			}
			{
				JButton btn_cancel = new JButton("Cancel");
				btn_cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_btn_cancel_actionPerformed(e);
					}
				});
				buttonPane.add(btn_cancel);
			}
		}
	}

	protected void do_btn_regestrieren_actionPerformed(ActionEvent e) {
		String name = textField_name.getText().trim();
		String vorName = textField_vorname.getText().trim();
		String nutzerName = textField_nutzerName.getText().trim();
		char[] passwordArr = passwordField.getPassword();
		String password = new String(passwordArr).trim();

		if (name.isEmpty() || vorName.isEmpty() || nutzerName.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle erforderlichen Felder aus");
		} else {
			try {

				shop.regestiereNeueMitarbeiter(name, vorName, nutzerName, password);
				this.dispose();
			} catch (MitarbeiterUsernameIstBenutztException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);

			}
		}
	}

	protected void do_btn_cancel_actionPerformed(ActionEvent e) {
		this.dispose();

	}
}