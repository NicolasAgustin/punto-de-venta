/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProductosService;

import Exceptions.StoreException;
import Exceptions.ValidationException;
import java.sql.SQLException;
import java.util.List;

import Persistence.Store;
import java.util.ArrayList;


/**
 *
 * @author Nico
 */
public class ProductosService {
    
    private final Store<Producto> store;
    
    public ProductosService() {
        this.store = new Store<Producto>(Producto.class);
        
        Producto[] productos = new Producto[] {
            new Producto(0, 1600, 10, "Jugo de arandanos", "", "0", "proveedor 1", "Alimento", true),
            new Producto(0, 700, 20, "Leche largavida", "", "1", "proveedor 1", "Alimento", true),
            new Producto(0, 2500, 10, "Jugo de naranjas", "", "2", "proveedor 1", "Alimento", true),
            new Producto(0, 2500, 10, "Gaseosa pepsi 2.25lt", "", "3", "proveedor 1", "Alimento", true),
            new Producto(0, 1500, 10, "Monster energy", "", "4", "proveedor 1", "Alimento", true),
        }; 
        
        for (int i = 0;i < productos.length; i++){
            this.store.add(productos[i]);
        }
    }
    
    private void validateProducto(Producto producto) throws ValidationException {
        if (producto.getCantidadInicial() < 0)
            throw new ValidationException("La cantidad inicial no puede ser negativa.");
    
        if (producto.getPrecioUnitario() < 0)
            throw new ValidationException("El precio unitario no puede ser negativo.");
        
        if ("".equals(producto.getTitulo()))
            throw new ValidationException("El titulo del producto no puede ser nulo.");
        
    }
    
    public Producto searchByCodigo(String codigo) throws StoreException {
    
        try {

            List<Producto> found = this.store.search("codigo", String.format("'%s'", codigo));
            
            if (found.size() == 0) throw new Exception();
            
            return found.get(0);
            
        } catch (Exception ex) {
            throw new StoreException("Codigo de barras no reconocido");
        }
        
    }
    
    
    public Producto fetch(int id) throws StoreException {
        // Esto no deberia ser un fetch por id?
        try {
            return this.store.fetch(id);
//              return this.store.fetch();
        } catch (Exception ex) {
            throw new StoreException("Codigo de barras no reconocido");
        }
    }
    
    public void add(Producto producto) throws ValidationException, StoreException {
    
        this.validateProducto(producto);
        
        try {
            this.store.add(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
    public List<Producto> list() throws StoreException {
        try {
            return this.store.search();
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    
    public void update(Producto producto) throws ValidationException, StoreException {
    
        this.validateProducto(producto);
        
        try {
            this.store.update(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
    public void delete(Producto producto) throws ValidationException, StoreException {
        
        try {
//            this.store.Eliminar(producto.getId());
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
}
