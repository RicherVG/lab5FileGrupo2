/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cmd;

import javax.swing.SwingUtilities;

/**
 *
 * @author hermi
 */
public class Main {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaCmd v = new VentanaCmd();
                v.setVisible(true);
            }
        });
    }
}
