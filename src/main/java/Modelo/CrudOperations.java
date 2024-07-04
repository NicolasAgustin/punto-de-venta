/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

import Exceptions.StoreException;
import java.util.List;

/**
 *
 * @author Nico
 * @param <T>
 */
public interface CrudOperations <T> {
    boolean Registrar(T cl);
    List Listar();
    boolean Eliminar(int id);
    boolean Modificar(T cl);
    T Obtener(T obj) throws StoreException;
}