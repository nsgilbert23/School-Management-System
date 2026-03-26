package sms.gui;
// Entry point - launches LoginForm first then MainFrameDesign - BelyseU and nsgilbert23
import sms.view.LoginForm;

public class MainApp {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}