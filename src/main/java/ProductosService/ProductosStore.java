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
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Nico
 */
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
    public boolean Registrar(Product cl) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        session.save(cl);
        session.getTransaction().commit();
        
        session.close();
        return true;
    }

    @Override
    public List Listar() {
//        Session session = this.sessionFactory.openSession();
//        
//        session.beginTransaction();
//        
//        List<Producto> productos = session.createQuery("from Producto", Producto.class).list();
//        
//        session.close();
//        
//        return productos;

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
    public boolean Eliminar(int id) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        
        session.createQuery("delete from Producto", Producto.class).list();
        
        session.remove(id);
        for (int i=0; i < this.store.size(); i++){
            if (this.store.get(i).getId() == id){
                this.store.remove(i);
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean Modificar(Producto cl) {
        try {
            int index = this.GetIndexByCode(cl.getCodigo());
            
            Producto p = this.store.get(index);
            
            p.setCantidadInicial(cl.getCantidadInicial());
            p.setCategoria(cl.getCategoria());
            p.setCodigo(cl.getCodigo());
            p.setDescripcion(cl.getDescripcion());
            p.setHabilitado(cl.getHabilitado());
            p.setPrecioUnitario(cl.getPrecioUnitario());
            p.setProveedor(cl.getProveedor());
            p.setTitulo(cl.getTitulo());
            
            return true;
            
        } catch(Exception ex) {
            return false;
        }
        
    }

    private int GetIndexByCode(String codigo) throws StoreException {
        for (int i=0; i < this.store.size(); i++){
            if (this.store.get(i).getCodigo().equals(codigo)){
                return i;
            }
        }
        throw new StoreException("");
    }
    
    @Override
    public Producto Obtener(Producto obj) throws StoreException {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        
//        session.get(Producto.class, )
        
        throw new StoreException("");
        // Este metodo deberia tirar una StoreException
        // Pero si agrego eso no cumple la interfaz
        // modificar la interfaz o cambiar el manejo de errores
        
//        throw new StoreException("No se encuentra el producto con codigo: " + obj.getCodigo());
    }
    
    
    
}
