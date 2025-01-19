/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

/**
 *
 * @author Nico
 */
public class BillerConnector {
    
    public int invoice_number = 0;
    
    public BillerConnector() {}
    
    public int createInvoice() {
        this.invoice_number += 1;
        try {
            Thread.sleep(10);
            return invoice_number;
        } catch (InterruptedException ex) {
            return invoice_number;
        }
    }
    
}
