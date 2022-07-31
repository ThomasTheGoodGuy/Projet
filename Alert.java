package com.thomasBonan.weather.utilities;

import com.thomasBonan.weather.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Alert {
    public static void error(Component parentComponent, String title, String message){
        JOptionPane.showMessageDialog(parentComponent,message, title,JOptionPane.ERROR_MESSAGE);

    }

}
