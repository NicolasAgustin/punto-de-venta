/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author Nico
 */
public enum PaymentMethod {
    Cash("Cash"),
    Credit("Credit"),
    Debit("Debit"),
    PlusPagos("PlusPagos");
    
    private final String name;
    
    PaymentMethod(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
