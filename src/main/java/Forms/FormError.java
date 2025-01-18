/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Forms;

/**
 *
 * @author Nico
 */
public class FormError {
    public String error_description;
    public String control_name;
    
    public FormError(String error_description, String control_name) {
        this.error_description = error_description;
        this.control_name = control_name;
    }
}
