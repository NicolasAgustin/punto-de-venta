/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CustomDialog {
    public static void showMessageDialog(Component parentComponent, String message) {
        // Crear un JOptionPane personalizado con el mensaje y un botón "Aceptar"
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        // Crear un JDialog para el JOptionPane
        final JDialog dialog = optionPane.createDialog(parentComponent, "Mensaje");

        // Crear un ActionListener para controlar el cierre del diálogo
        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();
            if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                // Cerrar el diálogo cuando se hace clic en el botón "Aceptar"
                dialog.setVisible(false);
            }
        });
         
        // Mostrar el diálogo
        dialog.setVisible(true);
         
             dialog.setVisible(false);
             dialog.dispose();
             
         
    }
}
