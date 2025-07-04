/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import java.time.Instant;
import Enums.PaymentMethod;
import VentasService.PaymentProcessor.CashProcessor;
import VentasService.PaymentProcessor.PaymentProcessor;

/**
 *
 * @author Nico
 */
@Deprecated
public class PaymentInformation {
    
    private long timestamp;
    
    private String currency;
    private double amount;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public PaymentProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }
    
    /**
     TODO:
     * - Para debito y credito la informacion a guardar cambia, contemplar hacer un refactor de los modelos
     */
    
    // Refactorizar esto, la clase paymentinformation
    // no tiene que procesar el pago.
    private PaymentProcessor processor;
    
    public PaymentInformation(String method, Sale sale) {
        this.timestamp = Instant.now().toEpochMilli();
        
        this.currency = sale.getCurrency();
        this.amount = sale.getTotal();
        
        // En base a este tipo instanciar mediante un strategy
        // un procesador de pago
        
        switch(PaymentMethod.valueOf(method)) {
        
            case Cash: {
                this.processor = new CashProcessor();
                break;
            }
                
            case Debit: {
                this.processor = null;
                break;
            }
            
            case Credit: {
                this.processor = null;
                break;
            }
            
            case PlusPagos: {
                this.processor = null;
                break;
            }
            
        }
    }
    
    public boolean processPayment() {
        
        if(!this.processor.authorize()){
            return false;
        }
        
        return this.processor.processPayment();
    }
    
    @Override
    public String toString() {
        return " " + amount + " " + currency;
    }
    
}
