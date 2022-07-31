package com.thomasBonan.weather;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Application {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainFrame mainFrame = mainFrame = new MainFrame("Weather");
                mainFrame.setResizable(false);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            }
        });
    }
}
