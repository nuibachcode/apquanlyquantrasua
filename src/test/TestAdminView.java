package test;

import javax.swing.UIManager;

import view.AdminView;

public class TestAdminView {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new AdminView(null);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
