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
    boolean save(T cl);
    List list();
    boolean delete(Long id);
    boolean update(Long id, T cl);
    T fetch(Long id) throws StoreException;
}