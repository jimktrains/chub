/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Alert {

    public static void warning(String text) {
        show(text, JOptionPane.WARNING_MESSAGE, "Warning!");
    }

    public static void info(String text) {
        show(text, JOptionPane.INFORMATION_MESSAGE, "Information!");
    }
    
    public static void show(String text, int messageType, String title) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text, messageType);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
