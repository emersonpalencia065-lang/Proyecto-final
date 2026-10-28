package com.canchapro;

import com.canchapro.gui.FrmPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            FrmPrincipal ventana = new FrmPrincipal();
            ventana.setVisible(true);
        });
    }
}
