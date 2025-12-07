package test;

import javax.swing.UIManager;

import view.ProductView;

public class TestProductView {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new ProductView();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
