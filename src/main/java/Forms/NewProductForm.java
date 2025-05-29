/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Forms;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nico
 */

public class NewProductForm {
    private String title;
    private String description;
    private String code;
    private String proveedor;
    private String categoria;
    private String unitary_price;
    private String initial_quantity;
    
    public NewProductValidatedForm validatedForm;
    
    public NewProductForm(
            String title,
            String description, 
            String code,
            String categoria, 
            String unitary_price, 
            String initial_quantity
    ) {
        this.title = title;
        this.description = description;
        this.code = code;
        this.categoria = categoria;
        this.unitary_price = unitary_price;
        this.initial_quantity = initial_quantity;
    }
    
    public List<FormError> validateForm() {
        List<FormError> errors = new ArrayList<>();
        
        Float unitaryPrice = null;
        int initialQuantity = 0;
        
        if (this.title.equals("")) {
            errors.add(new FormError("El campo titulo es obligatorio", "field1"));
        }
        
        if (this.code.equals("")) {
            errors.add(new FormError("El campo codigo es obligatorio", "field3"));
        }
        
        try {
            unitaryPrice = Float.parseFloat(this.unitary_price);
            if (unitaryPrice <= 0) {
                errors.add(new FormError("El precio unitario debe ser un numero mayor a cero", "field4"));
            }
        } catch(Exception ex) {
            errors.add(new FormError("El precio unitario no puede estar vacio", "field4"));
        }
        
        try {
            initialQuantity = Integer.parseInt(this.initial_quantity);
            
            if (initialQuantity < 0) {
                errors.add(new FormError("La cantidad inicial debe ser un numero mayor a cero", "field5"));
            }
            
        } catch(Exception ex) {
            errors.add(new FormError("La cantidad inicial no puede estar vacia", "field5"));
        }
        
        if (errors.size() == 0) {
            this.validatedForm = new NewProductValidatedForm(
                    this.title,
                    this.description,
                    this.code,
                    this.proveedor,
                    this.categoria,
                    unitaryPrice,
                    initialQuantity
            );
        }
        
        return errors;
    }
}
