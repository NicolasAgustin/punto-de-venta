/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProductosService;

import Exceptions.StoreException;
import Modelo.CrudOperations;
import ProductosService.Product;
import java.util.ArrayList;
import java.util.List;
import Persistence.H2Connector;
import Persistence.HibernateUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Nico
 */
@Deprecated
public class ProductosStore implements CrudOperations<Product> {

    private List<Product> store = new ArrayList<>();
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
  
    private H2Connector DBConnector;
    
    public ProductosStore() {
        
//        Session hibernateSession = this.sessionFactory.openSession();
//        hibernateSession.beginTransaction();
//        
//        Product[] productos = new Product[] {
//            new Product(0, 1600, 10, "Jugo de arandanos", "", "0", "proveedor 1", "Alimento", true),
//            new Product(0, 700, 20, "Leche largavida", "", "1", "proveedor 1", "Alimento", true),
//            new Product(0, 2500, 10, "Jugo de naranjas", "", "2", "proveedor 1", "Alimento", true),
//            new Product(0, 2500, 10, "Gaseosa pepsi 2.25lt", "", "3", "proveedor 1", "Alimento", true),
//            new Product(0, 1500, 10, "Monster energy", "", "4", "proveedor 1", "Alimento", true),
//        }; 
//    
//        for (int i = 0;i < productos.length; i++){
//            hibernateSession.save(productos[i]);
//        }
        
//        hibernateSession.getTransaction().commit();
//        
//        hibernateSession.close();


        
    }
    
    @Override
    public boolean save(Product cl) {
        EntityManager em = null;

        try {
            em = this.emf.createEntityManager();
        
            em.getTransaction().begin();
            em.persist(cl);
            
            return true;
            
        } catch (Exception ex) { 
            return false;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public List list() {

        EntityManager em = null;

        try {
            em = this.emf.createEntityManager();
        
            em.getTransaction().begin();
            return em.createQuery("from products", Product.class).getResultList();
        } catch (Exception ex) { 
            return new ArrayList<Product>();
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        
        EntityManager em = null;
        try {
            em = this.emf.createEntityManager();
            Product prod = em.find(Product.class, id);
            
            if (prod != null) em.remove(prod);
            
            return true;
            
        } catch(Exception ex) {
            return false;
        }
    }

    @Override
    public boolean update(Long id, Product cl) {
        
        EntityManager em = null;
        try {
            em = this.emf.createEntityManager();
            Product prod = em.find(Product.class, id);
            
            if (prod == null) throw new Exception("El producto con id " + id + " no existe");
            
            prod.setInitialQuantity(cl.getInitialQuantity());
            prod.setCategory(cl.getCategory());
            prod.setCode(cl.getCode());
            prod.setDescription(cl.getDescription());
            prod.setEnabled(cl.getEnabled());
            prod.setUnitaryPrice(cl.getUnitaryPrice());
            prod.setProvider(cl.getProvider());
            prod.setTitle(cl.getTitle());
            
            em.merge(prod);
            
            return true;
            
        } catch(Exception ex) {
            return false;
        }
        
    }
    
    @Override
    public Product fetch(Long id) throws StoreException {
        EntityManager em = null;
        
        try {
            em = this.emf.createEntityManager();
            Product prod = em.find(Product.class, id);
            
            if (prod == null) throw new Exception("El producto con id " + id + " no existe");
            
            return prod;
            
        } catch (Exception ex) {
            throw new StoreException("El producto con id " + id + " no existe");
        }
    }
    
    
    
}
