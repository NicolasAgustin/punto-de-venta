/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import Exceptions.StoreException;
import Modelo.CrudOperations;
import VentasService.Venta;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nico
 */
public class VentasStore implements CrudOperations<Venta> {

    private List<Venta> store = new ArrayList<>(); 
    
    @Override
    public boolean Registrar(Venta cl) {
        cl.setId(this.store.size()+1);
        this.store.add(cl);
        return true;
    }

    @Override
    public List Listar() {
        return this.store;
    }

    @Override
    public boolean Eliminar(int id) {
        for (int i=0; i < this.store.size(); i++){
            if (this.store.get(i).getId() == id){
                this.store.remove(i);
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean Modificar(Venta cl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Venta Obtener(Venta obj) throws StoreException {
        for (int i=0; i < this.store.size(); i++){
            if (this.store.get(i).getId() == obj.getId()){
                return this.store.get(i);
            }
        }
        
        throw new StoreException("");
    
    }
    
}
