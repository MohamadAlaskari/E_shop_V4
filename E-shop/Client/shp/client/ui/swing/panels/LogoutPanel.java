package shp.client.ui.swing.panels;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import shp.common.entities.Nutzer;
import shp.common.interfaces.E_ShopInterface;
import shp.server.domin.E_Shop;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.Cursor;

public class LogoutPanel extends JPanel {
	private JButton btn_Logout;
	private E_ShopInterface shop;
	private Nutzer loggedNutzer;
	private PanelChangeBeiLogout panelChangeBeiLogout;

	public interface PanelChangeBeiLogout {
		void onPanelChangeBeiLogout();
	}

	//

	/**
	 * Create the panel.
	 */
	public LogoutPanel(E_ShopInterface shop, Nutzer loggNutzer,PanelChangeBeiLogout panelChangeBeiLogout) {

		this.shop = shop;
		this.loggedNutzer = loggNutzer;
		this.panelChangeBeiLogout = panelChangeBeiLogout;

		initGUI();
	}

	private void initGUI() {
		setBackground(UIManager.getColor("InternalFrame.resizeIconHighlight"));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		{
			this.btn_Logout = new JButton("Logout");
			this.btn_Logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.btn_Logout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btn_Logout_actionPerformed(e);
				}
			});
			add(this.btn_Logout);
		}
	}

	protected void do_btn_Logout_actionPerformed(ActionEvent e) {

		// shop.loggeMitarbeiterAus(loggedMitarbeiter);
		try {
			JOptionPane.showMessageDialog(null, "Logout Erfolgreich. Ihe �nderungen wurden gespeichert", "Info Message",
					JOptionPane.INFORMATION_MESSAGE);

			shop.schreibeArtikel();
			shop.schreibeMitarbeiter();
			shop.schreibeVerlauf();
			shop.schreibeKunde();
			this.loggedNutzer = null;
			this.panelChangeBeiLogout.onPanelChangeBeiLogout();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
