package test;

import javax.swing.UIManager;
import view.SignUpView;
public class TestSignUp {
public static void main(String[] args) {
	try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new SignUpView();
	}catch (Exception e) {
		e.printStackTrace();
	}
}
}
