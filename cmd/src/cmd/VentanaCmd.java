/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cmd;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author riche
 */
public class VentanaCmd extends JFrame{
    private JTextArea area;
    private MotorComandos motor;
    private boolean modo;
    private int posicionBloqueo;
    
    public VentanaCmd(){
        motor = new MotorComandos();
        modo = false;
        setTitle("Administrador: Command Prompt");
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        area = new JTextArea();
        area.setBackground(Color.BLACK);
        area.setForeground(Color.LIGHT_GRAY);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Consolas",Font.PLAIN,14));
        area.setLineWrap(false);
        add( new JScrollPane(area));
        area.append("Microsoft Windows [Version 10.0.22621.521]\n");
        area.append("(c) Microsoft Corporation. All rights reserved.\n\n");
        
        prompt();
        
        
        area.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

               if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    procesarComando();
                    return;
                }

                
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (area.getCaretPosition() <= posicionBloqueo) {
                        e.consume();
                        area.setCaretPosition(area.getDocument().getLength());
                        return;
                    }
                }

               
                if (area.getCaretPosition() < posicionBloqueo) {
                    area.setCaretPosition(area.getDocument().getLength());
                }
            }
        }); 
           SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                area.requestFocusInWindow();
                area.setCaretPosition(area.getDocument().getLength());
            }
        });

    }
    
    private void procesarComando(){
        try{
            int fin=area.getDocument().getLength();
            String texto=area.getText(posicionBloqueo,fin - posicionBloqueo);
            texto=texto.trim();
            area.append("\n");
            ResultadoComando r;
            if(modo)
                r=motor.escribirLinea(texto);
            else
                r= motor.ejecutar(texto);
            if (r.getMensaje() != null && !r.getMensaje().equals(""))
                area.append(r.getMensaje() + "\n");

            if (r.isEntrar()) modo = true;
            if (r.isSalir()) modo = false;

            prompt();
        } catch (Exception ex) {
            area.append("Error\n");
            prompt();
        }
       } 
        private void prompt(){
            if(modo)
                area.append("WR> ");
            else
                area.append(motor.getPrompt());
            
             posicionBloqueo = area.getDocument().getLength();
             area.setCaretPosition(posicionBloqueo);
        }
    }

