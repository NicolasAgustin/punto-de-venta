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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import org.hibernate.Session;


/**
 *
 * @author Nico
 */

public class ProductsService {
    
    private final Store<Product> store;
    private final Store<Category> categories_store;
    private final Store<Provider> providers_store;
    
    public ProductsService() {
        this.store = new Store(Product.class);
        this.categories_store = new Store(Category.class);
        this.providers_store = new Store(Provider.class);
//        
        Product[] products = new Product[] {
            new Product(0, (float) 1600, 10, "Jugo de arandanos", "", "0", "Alimento", true),
            new Product(0, (float) 700, 20, "Leche largavida", "", "1", "Alimento", true),
            new Product(0, (float) 2500, 10, "Jugo de naranjas", "", "2", "Alimento", true),
            new Product(0, (float) 2500, 10, "Gaseosa pepsi 2.25lt", "", "3", "Alimento", true),
            new Product(0, (float) 1500, 10, "Monster energy", "", "4", "Alimento", true),
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
        
        ProvidersIngest();
        
        
        
        // TODO: Hacer carga de PrecioProveedorProducto
        // ver como relacionar un proveedor con un producto al momento de la carga
        
    }
    
    private void ProvidersIngest() {
        
        Random random = new Random();
        Session session = null; 
        try {
            session = this.store.createSession(); 
            session.beginTransaction(); 

            // Define los datos de los proveedores
            List<Map.Entry<String, String>> providerData = Arrays.asList(
                new AbstractMap.SimpleEntry<>("Alimentos SA", "30202020204"),
                new AbstractMap.SimpleEntry<>("La Virginia", "30661945369")
            );

            List<Provider> managedProviders = new ArrayList<>();

            // Paso 1: Asegurarse de que los proveedores estén persistidos y gestionados dentro de ESTA sesión
            for (Map.Entry<String, String> data : providerData) {
                String name = data.getKey();
                String taxPayerId = data.getValue();
                
                List<Provider> existingProviders = session.createQuery(
                    "FROM Provider WHERE taxPayerId = :taxPayerId", Provider.class)
                    .setParameter("taxPayerId", taxPayerId)
                    .getResultList();

                Provider provider;
                if (existingProviders.isEmpty()) {
                    provider = new Provider(name, "", taxPayerId, null);
                    session.persist(provider); 
                } else {
                    provider = existingProviders.get(0);
                }
                managedProviders.add(provider); 
            }

            // Paso 2: Obtener todos los productos gestionados DENTRO DE ESTA SESIÓN
            List<Product> products = session.createQuery("FROM Product", Product.class).list(); 

            // Paso 3: Iterar sobre productos y proveedores para crear o actualizar PrecioProveedorProducto
            for (Product product : products) {
                // Para cada producto, crearemos o actualizaremos sus precios por proveedor.
                // Es crucial que el 'product' que se va a 'merge' esté al día con sus colecciones.
                // Podríamos incluso cargar el producto en este bucle si hay riesgo de que esté "viejo".
                Product currentProduct = (Product) session.merge(product); // Asegura que el producto esté gestionado y sea la última versión

                for (Provider provider : managedProviders) { // 'provider' aquí está gestionado en ESTA sesión

                    // 1. Crear el ID compuesto
                    PrecioProveedorProducto.PrecioProveedorProductoId pppId = 
                        new PrecioProveedorProducto.PrecioProveedorProductoId(currentProduct.getId(), provider.getId());

                    // 2. Intentar encontrar si ya existe un PrecioProveedorProducto con este ID en la sesión/DB
                    PrecioProveedorProducto ppp = session.find(PrecioProveedorProducto.class, pppId);

                    if (ppp == null) {
                        // Si no existe, crear una nueva instancia de PrecioProveedorProducto
                        ppp = new PrecioProveedorProducto();
                        ppp.setId(pppId); // Establecer el ID compuesto
                        ppp.setProducto(currentProduct); // Asociar el producto (gestionado)
                        ppp.setProveedor(provider);     // Asociar el proveedor (gestionado)
                        
                        // Añadir a las colecciones del producto y proveedor
                        // Esto hará que Hibernate detecte la nueva entidad cuando 'merge' a los padres
                        currentProduct.agregarPrecioProveedor(ppp); 
                        provider.agregarPrecioProducto(ppp);
                        
                        // NOTA: No llamamos a session.persist(ppp) aquí directamente.
                        // La cascada desde currentProduct (o provider) lo hará cuando se haga merge.
                    }
                    
                    // En este punto, 'ppp' es una instancia gestionada (si existía) o una nueva instancia (si no existía)
                    // que será gestionada por cascada.

                    double randomPrice = random.nextDouble() * (5000 + Double.MIN_VALUE);
                    ppp.setPrecio(randomPrice); // Actualizar el precio (siempre)

                    // Actualizar el precio de venta al público del producto
                    // Esto se hace sobre 'currentProduct' que es el que se está fusionando
                    currentProduct.setPublicSalePrice(randomPrice * 0.10);

                    // No es necesario llamar a session.merge(ppp) aquí porque la cascada
                    // desde 'currentProduct' ya lo gestionará.
                }
                // Fusionar el producto al final del bucle interno de proveedores.
                // Esto cascadeará los cambios a todos los PrecioProveedorProducto asociados a 'currentProduct'.
                session.merge(currentProduct); 
            }
            session.getTransaction().commit(); 
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); 
            }
            System.err.println("Error durante ProvidersIngest: " + ex.getMessage());
            // Considera lanzar una excepción personalizada o manejar el error adecuadamente
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); 
            }
        }
    }
    
    public List<Category> getProductsCategories() {
        return this.categories_store.list();
    }
    
    public List<Provider> getProviders() {
        return this.providers_store.list();
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
