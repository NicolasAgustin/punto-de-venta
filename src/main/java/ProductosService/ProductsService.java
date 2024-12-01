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

public class ProductsService {
    
    private final Store<Product> store;
    private final Store<Category> categories_store;
    
    public ProductsService() {
        this.store = new Store(Product.class);
        this.categories_store = new Store(Category.class);
//        
        Product[] products = new Product[] {
            new Product(0, 1600, 10, "Jugo de arandanos", "", "0", "proveedor 1", "Alimento", true),
            new Product(0, 700, 20, "Leche largavida", "", "1", "proveedor 1", "Alimento", true),
            new Product(0, 2500, 10, "Jugo de naranjas", "", "2", "proveedor 1", "Alimento", true),
            new Product(0, 2500, 10, "Gaseosa pepsi 2.25lt", "", "3", "proveedor 1", "Alimento", true),
            new Product(0, 1500, 10, "Monster energy", "", "4", "proveedor 1", "Alimento", true),
        };
        
        for (Product product : products) {
            this.store.add(product);
        }
        
        String[] categories = new String[] {
            "Almacen",
            "Bebidas",
            "Frescos",
            "Congelados",
            "Limpieza",
            "Perfumeria",
            "Electro",
            "Textil",
            "Hogar"
        };
        
        for (String cat: categories) {
            this.categories_store.add(new Category(cat));
        }
        
    }
    
    public List<Category> getProductsCategories() {
        return this.categories_store.list();
    }
    
    private void validateProducto(Product product) throws ValidationException {
        if (product.getInitialQuantity()< 0)
            throw new ValidationException("La cantidad inicial no puede ser negativa.");
    
        if (product.getUnitaryPrice() < 0)
            throw new ValidationException("El precio unitario no puede ser negativo.");
        
        if ("".equals(product.getTitle()))
            throw new ValidationException("El titulo del producto no puede ser nulo.");
    }
    
    public Product searchByCodigo(String codigo) throws StoreException {
    
        try {

            List<Product> found = this.store.search("code", String.format("'%s'", codigo));
            
            if (found.size() == 0) throw new Exception();
            
            return found.get(0);
            
        } catch (Exception ex) {
            throw new StoreException("Codigo de barras no reconocido");
        }
        
    }
    
    
    public Product fetch(Long id) throws StoreException {
        // Esto no deberia ser un fetch por id?
        try {
            Product found = this.store.fetch(id);
            
            if (found == null) throw new StoreException("Codigo de barras no reconocido");

            return found;
        } catch (Exception ex) {
            throw new StoreException(ex.toString());
        }
    }
    
    public void add(Product producto) throws ValidationException, StoreException {
    
        this.validateProducto(producto);
        
        try {
            this.store.add(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
    public List<Product> list() throws StoreException {
        try {
            return this.store.list();
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    
    public void update(Product producto) throws ValidationException, StoreException {
    
        this.validateProducto(producto);
        
        try {
            this.store.update(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
    public void delete(Product producto) throws ValidationException, StoreException {
        
        try {
//            this.store.Eliminar(producto.getId());
              this.store.delete(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
}
