package com.vrsoftware.client;

import com.vrsoftware.client.ui.MainFrame;

import javax.swing.SwingUtilities;

public class SwingApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}