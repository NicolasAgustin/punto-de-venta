package ProductosService;

import Exceptions.StoreException;
import Exceptions.ValidationException;
import java.sql.SQLException;
import java.util.List;

import Persistence.Store;
import ProductosService.PrecioProveedorProducto.PrecioProveedorProductoId;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
            new Product(0, (float) 2500, 25, "Jugo de naranjas", "", "2", "Alimento", true),
            new Product(0, (float) 2500, 150, "Gaseosa pepsi 2.25lt", "", "3", "Alimento", true),
            new Product(0, (float) 1500, 8, "Monster energy", "", "4", "Alimento", true),
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
                Product currentProduct = product; // 'product' ya es una entidad gestionada

                for (Provider provider : managedProviders) { 
                    // 1. Crear el ID compuesto
                    PrecioProveedorProducto.PrecioProveedorProductoId pppId = 
                        new PrecioProveedorProducto.PrecioProveedorProductoId(currentProduct.getId(), provider.getId());

                    // 2. Intentar encontrar si ya existe un PrecioProveedorProducto con este ID en la sesión/DB
                    PrecioProveedorProducto ppp = session.find(PrecioProveedorProducto.class, pppId);

                    if (ppp == null) {
                        // Si no existe, crear una nueva instancia de PrecioProveedorProducto
                        ppp = new PrecioProveedorProducto();
                        ppp.setId(pppId); 
                        ppp.setProducto(currentProduct); 
                        ppp.setProveedor(provider);
                        session.persist(ppp);
                        
                        // Añadir a las colecciones del producto y proveedor
                        // Esto ahora añade una instancia ya gestionada.
                        currentProduct.agregarPrecioProveedor(ppp); 
                        provider.agregarPrecioProducto(ppp);
                    }
                    
                    // En este punto, 'ppp' es una instancia gestionada (ya sea encontrada o recién persistida).

                    double randomPrice = 1000.0 + (5000.0 - 1000.0) * random.nextDouble();
                    BigDecimal bd = new BigDecimal(randomPrice).setScale(2, RoundingMode.HALF_UP);
                    double roundedPrice = bd.doubleValue();
                    ppp.setPrecioCompra(roundedPrice); // Actualizar el precio (siempre)

                    // Actualizar el precio de venta al público del producto
                    //currentProduct.setPublicSalePrice(new BigDecimal(roundedPrice + roundedPrice * 0.10).setScale(2, RoundingMode.HALF_UP).doubleValue());
                }
                // Fusionar el producto al final del bucle interno de proveedores.
                // Esto cascadeará los cambios (incluyendo las actualizaciones de PrecioProveedorProducto).
                // Si el PrecioProveedorProducto ya fue persistido explícitamente, 'merge' simplemente lo gestionará.
                session.merge(currentProduct); 
            }
            session.getTransaction().commit(); 
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); 
            }
            System.err.println("Error durante ProvidersIngest: " + ex.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); 
            }
        }
    }
    
    public List<Product> getProductsNotLinkedToProvider(String taxPayerID) {
        Session session = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            
//            String hql = (
//                "SELECT p FROM Product p " +
//                "LEFT JOIN p.preciosPorProveedor ppp " +
//                "WHERE ppp.proveedor.taxPayerId = :taxPayerID AND p.id NOT IN (" +
//                "SELECT ppp2.producto.id FROM PrecioProveedorProducto ppp2 " +
//                "WHERE ppp2.proveedor.taxPayerId = :taxPayerID" +
//                ")"
//            );
            
//            String hql2 = "SELECT p FROM Product p " +
//                          "WHERE p.id NOT IN (SELECT ppp.producto.id FROM PrecioProveedorProducto ppp WHERE ppp.proveedor.id = :proveedorId)";
            String hql = "SELECT p FROM Product p " +
                         "LEFT JOIN p.preciosPorProveedor ppp " +
                         "WHERE (ppp.id IS NULL) " + // Condición 1: no tiene NINGÚN proveedor asociado (sin entradas en ppp)
                         "OR (" +
                         "    p.id NOT IN (SELECT ppp_sub.producto.id FROM PrecioProveedorProducto ppp_sub WHERE ppp_sub.proveedor.taxPayerId = :taxPayerID)" +
                         ")";
    

            List<Product> products = session
                    .createQuery(hql, Product.class)
                    .setParameter("taxPayerID", taxPayerID)
                    .getResultList();
            
            return products;
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            System.err.println("Error al buscar entidad por ID: " + ex.getMessage()); // Usa System.err para errores
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        } 
    }
    
    public Provider getProviderByTaxPayerID(String taxPayerID) {
        Session session = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            
            Provider provider = session
                    .createQuery("SELECT p FROM Provider p WHERE p.taxPayerId = :taxPayerID", Provider.class)
                    .setParameter("taxPayerID", taxPayerID)
                    .getSingleResult();
            return provider;
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            System.err.println("Error al buscar entidad por ID: " + ex.getMessage()); // Usa System.err para errores
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        }       
    }
    
    public void addPrecioProveedorProducto(PrecioProveedorProductoId compoundKey, double precio) {
        Session session = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            
            // Get managed entities
            Product product = session.find(Product.class, compoundKey.getProductoId());
            Provider provider = session.find(Provider.class, compoundKey.getProveedorId());
            
            PrecioProveedorProducto newPpp = new PrecioProveedorProducto();
            newPpp.setId(compoundKey);
            newPpp.setPrecioCompra(precio);
            newPpp.setProducto(product);
            newPpp.setProveedor(provider);
            
            session.persist(newPpp);
            
            product.agregarPrecioProveedor(newPpp);
            provider.agregarPrecioProducto(newPpp);
            
            session.merge(product);
            session.merge(provider);
            
            session.getTransaction().commit(); 
            
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            System.err.println("Error al buscar entidad por ID: " + ex.getMessage()); // Usa System.err para errores
            
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        }       
    }
    
    public List<PrecioProveedorProducto> getProductsByProvider(int providerID) {
        Session session = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            List<PrecioProveedorProducto> products = session
                    .createQuery("FROM PrecioProveedorProducto ppp WHERE ppp.proveedor.id = :proveedorId", PrecioProveedorProducto.class)
                    .setParameter("proveedorId", providerID)
                    .getResultList();
            return products;
        } catch (Exception ex) {
//            if (session != null && session.getTransaction().isActive()) {
//                session.getTransaction().rollback(); // Rollback en caso de error
//            }
            return null;
        } finally {
//            if (session != null && session.isOpen()) {
//                session.close(); // Cierra la sesión
//            }
        }
    }
    
    public Provider getProviderByID(int providerID) {
        Session session = null;
        Provider entity = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            
            entity = session.find(Provider.class, providerID); // Usa session.find() para buscar por ID
            session.getTransaction().commit(); // Confirma la transacción
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback en caso de error
            }
            System.err.println("Error al buscar entidad por ID: " + ex.getMessage()); // Usa System.err para errores
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        }
        return entity;
    }
    
    public List<PrecioProveedorProducto> getProvidersByProduct(int productID) {
        Session session = null;
        
        try {
            session = this.store.createSession(); 
            session.beginTransaction();
            List<PrecioProveedorProducto> proveedores = session
                    .createQuery("FROM PrecioProveedorProducto ppp WHERE ppp.producto.id = :productId", PrecioProveedorProducto.class)
                    .setParameter("productId", productID)
                    .getResultList();
                
            return proveedores;
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        }
    }
    
    public PrecioProveedorProducto getPrecioProveedorProducto(int proveedorID, int productoID){
        
        Session session = null;
        
        session = this.store.createSession(); 
        session.beginTransaction();
        
        try{
            PrecioProveedorProducto.PrecioProveedorProductoId pppId = new PrecioProveedorProducto
                .PrecioProveedorProductoId(productoID, proveedorID);

            PrecioProveedorProducto ppp = session.find(PrecioProveedorProducto.class, pppId);
            return ppp;
        }catch(Exception ex){
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Cierra la sesión
            }
        }
    }
    
    public List<Category> getProductsCategories() {
        return this.categories_store.list();
    }
    
    public List<Provider> getProviders() {
        
        Session session = null;
        try {
            session = this.store.createSession(); 
            session.beginTransaction(); 
            
            List<Provider> existingProviders = session.createQuery("FROM Provider", Provider.class).getResultList();
            
            session.getTransaction().commit();
            return existingProviders;
        } catch (Exception ex) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("Error al obtener proveedores: " + ex.getMessage());
            return new ArrayList<>(); 
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
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
              this.store.delete(producto);
        } catch (Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
}