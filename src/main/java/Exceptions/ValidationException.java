/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Nico
 */
public class ValidationException extends Exception {
  static int contaP=0;
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }

    public ValidationException() {
        
   }
   
    

    
}

