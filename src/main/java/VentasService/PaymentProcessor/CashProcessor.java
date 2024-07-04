/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService.PaymentProcessor;

/**
 *
 * @author Nico
 */
public class CashProcessor implements PaymentProcessor {

    @Override
    public boolean authorize() {
        return true;
    }

    @Override
    public boolean processPayment() {
        return true;
    }
    
}
