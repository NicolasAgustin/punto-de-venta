/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package VentasService.PaymentProcessor;

/**
 *
 * @author Nico
 */
public interface PaymentProcessor {
    public boolean authorize();
    public boolean processPayment();
}
