package sms.gui;

import sms.dao.DatabaseConnection;
import sms.gui.MainFrameDesign;
import javax.swing.SwingUtilities;
import sms.dao.DatabaseConnection;
import sms.gui.MainFrameDesign;

public class MainApp {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {
            new MainFrameDesign().setVisible(true);
        });

    }
}