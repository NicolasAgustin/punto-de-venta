/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Forms;

/**
 *
 * @author Nico
 */
public class NewProductValidatedForm {
    public String title;
    public String description;
    public String code;
    public String proveedor;
    public String categoria;
    public Float unitary_price;
    public int initial_quantity;
    
    public NewProductValidatedForm(
            String title,
            String description, 
            String code, 
            String proveedor, 
            String categoria, 
            Float unitary_price, 
            int initial_quantity
    ) {
        this.title = title;
        this.description = description;
        this.code = code;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.unitary_price = unitary_price;
        this.initial_quantity = initial_quantity;
    }
}
