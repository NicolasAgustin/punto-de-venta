/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistence;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.ArrayList;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/**
 *
 * @author Nico
 * @param <T>
 */
public class Store<T> {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    
    final Class<T> typeClass;
    
    public Store(Class<T> typeClass) {
        this.typeClass = typeClass;
    }
    
    public Session createSession() {
        return this.sessionFactory.openSession();
    }
    
    public boolean add(T entity) {
        
        try {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
            
            session.close();
            return true;
            
        } catch (Exception ex) {
            return false;
        }
    }
    
    public List<T> list() {
    
        try {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();

            List<T> entities = session.createQuery("from " + this.typeClass.getSimpleName(), this.typeClass).list();

            session.close();

            return entities;
            
        } catch (Exception ex) {
            return new ArrayList<T>();
        }
    }
    
    public List<T> search(String columnName, Object value) {
        
        String query = "FROM " + this.typeClass.getSimpleName() + " t WHERE t." + columnName + " = " + value;
        
        try {
            
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            
            List<T> results = session.createQuery(query, this.typeClass).getResultList();
            
            session.close();
            
            return results;
        } catch (Exception ex) {
            
            System.out.println(ex.toString());
            
            return new ArrayList<T>();   
        }
        
    }
    
    
    public void update(T entity) {
        
        try {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            
            session.merge(entity);
            
            session.getTransaction().commit();
            
            session.close();
            
        } catch (Exception ex) {
            System.out.println();
        }

    }
    
    public void delete(T entity) {

        try {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();

            session.remove(entity);

            session.getTransaction().commit();

            session.close();
        } catch (Exception ex) {
            System.out.println();
        }
    }
    
    public T fetch(Long id) {

        try {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            T entity = session.find(this.typeClass, id);

            session.close();

            return entity;
            
        } catch (Exception ex) {
            System.out.println();
            return null;
        }
    }
    
}
