/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author Nico
 */
public class Validator {
    public static final boolean isDniValid(String dni) {
        return dni.length() == 8;
    }
}
