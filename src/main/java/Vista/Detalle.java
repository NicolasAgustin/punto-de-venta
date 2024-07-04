/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;


import ProductosService.ProductosValidacion;
import static Vista.Sistema.bandera;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author adminsti
 */
public class Detalle extends javax.swing.JDialog {
    

 
  // private boolean cancelado = false;
   private boolean aceptado = false;
   /*  public boolean getCancelado() {
        return cancelado;
    }*/

    public boolean getAceptado() {
        return aceptado;
    }
     public void setProveedor(String proveedor) {
        // Establecer el proveedor en el JComboBox correspondiente
        jComboProv.setSelectedItem(proveedor);
    }

    public void setCategoria(String categoria) {
        // Establecer la categoría en el JComboBox correspondiente
        jComboCat.setSelectedItem(categoria);
    }
    // Método para obtener la opción seleccionada en el JComboBox de proveedor
    public String getProveedorSeleccionado() {
        return jComboProv.getSelectedItem().toString();
    }

    // Método para obtener la opción seleccionada en el JComboBox de categoría
    public String getCategoriaSeleccionada() {
        return jComboCat.getSelectedItem().toString();
    }
    int contadorP=0;
    int obligTit=0;
    int obligPrec=0;
    int obligCantI=0;
    int obligCod=0;
    int Borrar=0;
    
    public Detalle(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
       //btnAceptar.setEnabled(false);//Este boton al proncipio esta deshabilitado
       // ActionListener para el botón "Aceptar"
      

    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        PrecioUni = new javax.swing.JTextField();
        CantInici = new javax.swing.JTextField();
        Titulo = new javax.swing.JTextField();
        Desc = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        BtnCancelar = new javax.swing.JButton();
        jComboProv = new javax.swing.JComboBox<>();
        jComboCat = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabelCod = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLabel2.setText("Precio unitario:");

        jLabel3.setText("Cantidad Inicial:");

        jLabel4.setText("Categoria:");

        jLabel5.setText("Titulo:");

        jLabel6.setText("Descripcion:");

        jLabel8.setText("Proveedor:");

        PrecioUni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecioUniActionPerformed(evt);
            }
        });
        PrecioUni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                PrecioUniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PrecioUniKeyTyped(evt);
            }
        });

        CantInici.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CantIniciKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CantIniciKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CantIniciKeyTyped(evt);
            }
        });

        Titulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TituloKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TituloKeyTyped(evt);
            }
        });

        Desc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DescKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DescKeyTyped(evt);
            }
        });

        btnAceptar.setFont(new java.awt.Font("Source Sans Pro Black", 0, 14)); // NOI18N
        btnAceptar.setText("ACEPTAR");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        BtnCancelar.setFont(new java.awt.Font("Source Sans Pro Black", 0, 14)); // NOI18N
        BtnCancelar.setText("CANCELAR");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        jComboProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Proveedor uno", "Proveedor dos", "Proveedor tres" }));

        jComboCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Limpieza", "Alimento", "Lacteos", "Textil" }));

        jLabel7.setText("Codigo:");

        jLabelCod.setText("........................................................");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAceptar)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jComboCat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(BtnCancelar)
                        .addGap(15, 15, 15))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PrecioUni, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(CantInici)
                            .addComponent(Titulo)
                            .addComponent(Desc)
                            .addComponent(jLabelCod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(PrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(CantInici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Desc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelCod))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnCancelar)
                    .addComponent(btnAceptar))
                .addGap(27, 27, 27))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        // TODO add your handling code here:
       
        //this.setVisible(false); //Se oculta si al Aceptar esta todo 
       this.aceptado = true;
        
       // LA LOGICA DE VALIDACION DEBERIA EJECUTARSE ACA
       //JOptionPane.showMessageDialog(null, "EN HANDLER ACEPTAR");
       
       // Cerrar la ventana
       this.dispose();
        
    }//GEN-LAST:event_btnAceptarActionPerformed

    // Método para habilitar o deshabilitar el botón "Aceptar" según el estado actual de los campos obligatorios
    /*private void actualizarBotonAceptar() {
        boolean camposCompletos = (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1);
        btnAceptar.setEnabled(camposCompletos);
    }
    */
    

    
    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarActionPerformed
        // CANCELAR
        //this.cancelado=true;
       
        //Marcar el JDialog como no accesible, o sea que fue marcado para destruccion
        this.setRootPane(null);//poner el rootPane del Dialog sera null, haciendolo inservible
        
        this.dispose();//mandar a destruir de la memoria el JDialog actual
    }//GEN-LAST:event_BtnCancelarActionPerformed

    private void PrecioUniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrecioUniKeyTyped
        
       //  ProductosValidacion.numberDecimalKeyPress(evt, PrecioUni);
        
    }//GEN-LAST:event_PrecioUniKeyTyped

    private void CantIniciKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CantIniciKeyPressed

    private void CantIniciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyTyped
        
        // ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_CantIniciKeyTyped

    private void TituloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TituloKeyTyped
        
      //  ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_TituloKeyTyped

    private void DescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescKeyTyped
        
      // ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_DescKeyTyped

    private void TituloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TituloKeyReleased
      
      

    }//GEN-LAST:event_TituloKeyReleased

    private void PrecioUniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrecioUniKeyReleased
         
        
      /*  obligPrec=ProductosValidacion.camposObligTxt(PrecioUni,jLabel2, btnAceptar, contadorP);
        if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1){
            btnAceptar.setEnabled(true);
             
        }else{
            btnAceptar.setEnabled(false);
        }*/
     
    }//GEN-LAST:event_PrecioUniKeyReleased

    private void CantIniciKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyReleased
        //Valida el campo obligatorio Cantidad
      /*  obligCantI=ProductosValidacion.camposObligTxt(CantInici,jLabel3, btnAceptar, contadorP);
        if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1 ){
            btnAceptar.setEnabled(true);
            
        }else{
            btnAceptar.setEnabled(false);
        }*/
      
      
    
    
    }//GEN-LAST:event_CantIniciKeyReleased

    private void DescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescKeyReleased
      /* if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1 ){
            btnAceptar.setEnabled(true);
            
        }else{
            btnAceptar.setEnabled(false);
        }     */
      
    }//GEN-LAST:event_DescKeyReleased


    
    
    private void PrecioUniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecioUniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrecioUniActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Detalle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detalle dialog = new Detalle(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCancelar;
    public javax.swing.JTextField CantInici;
    public javax.swing.JTextField Desc;
    public javax.swing.JTextField PrecioUni;
    public javax.swing.JTextField Titulo;
    public javax.swing.JButton btnAceptar;
    public javax.swing.JComboBox<String> jComboCat;
    public javax.swing.JComboBox<String> jComboProv;
    private javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel6;
    public static javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JLabel jLabelCod;
    // End of variables declaration//GEN-END:variables
}
