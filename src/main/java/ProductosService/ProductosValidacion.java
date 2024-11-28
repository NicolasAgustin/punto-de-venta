/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProductosService;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author adminsti
 */
public class ProductosValidacion {
    
    //Este sirve para permitir solo caracteres
    static public void textKeyPress(KeyEvent evt){
	//declaramos una variable y le asignamos un evento
//	char car = evt.getKeyChar();
//	if((car < 'a' || car >'z') && (car < 'A' || car > 'Z')
//		&& (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE)){
//		evt.consume();
//	}
    }
    //Nos permite solo numeros
    public static void numberKeyPress(KeyEvent evt){
	//declaramos una variable y le asignamos un evento
	char car = evt.getKeyChar();
        
        JTextField textField = (JTextField) evt.getComponent();
        
        if (textField.getText().equals("0")) {
            textField.setText("");
            evt.consume();
        }
        
	if((car < '0' || car >'9') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
	}
    }
    //Nos permite solo decimales
    public  static void numberDecimalKeyPress(KeyEvent evt, JTextField textField){
	//declaramos una variable y le asignamos un evento
	char car= evt.getKeyChar();
	if((car < '0' || car < '9')  && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)){
            evt.consume();
        }else if ((car< '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }
    
    //Validar campos obligatorios:
    public static int camposObligTxt(JTextField textField,JLabel label,JButton btnSaveProducto, int contador){
       
        if(textField.getText().isEmpty()){
            //JOptionPane.showMessageDialog(null, "Por favor ingrese un valor en " + label.getText());
            label.setForeground(Color.RED);
            contador=0;
        
        } else {
            label.setForeground(Color.BLACK);
            contador = 1;
        }
        
        return contador;
    
    }
    
    public static void limpiarProducto(JTextField textField) {//limpia los campos de producto
        textField.setText("");
    }
    
    
    
    
      //--------------------------------------------
    
    //Funcion para validar existencia de registro en Jlist
    public static boolean ExisteEnLista(DefaultListModel modelLista, String Dto){
	boolean bandera = false;
	
        for(int i=0; i<modelLista.getSize(); i++){
            if(modelLista.getElementAt(i).equals(Dto)){
                bandera = true;
            }
        }
    
        return bandera;
    }

    //Metodo tipo void para registrar en Lista
    public static boolean AgregarLista(DefaultListModel modelLista, DefaultListModel modellista1, DefaultListModel modellista2,String cod, String desc, String tit,JList list1, JList list2, JList list3){
	if(!ExisteEnLista(modelLista, cod)){
            modelLista.addElement(cod); 
            modellista1.addElement (desc); 
            modellista2.addElement(tit);
            list1.setModel(modelLista); 
            list2.setModel(modellista1); 
            list3.setModel(modellista2);
            return true;    
        } else {
            JOptionPane.showMessageDialog(null, "El Producto con el Codigo "+cod+" ya existe", "!Aviso del programa", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    
    
    //VALIDACION PARA DETALLE:
    public static boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean contieneNumeros(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
            return true; // Se encontró al menos un dígito numérico
            }
        }
        return false; // No se encontraron dígitos numéricos
    }

    
   /* public static boolean validarString(String str) {
         // Verificar si la cadena no es nula y no está vacía
        if (str != null && !str.isEmpty()) {
            return true; // La cadena es válida
        } else {
            return false; // La cadena es nula o está vacía
        }
    }*/


 
    
    
}
