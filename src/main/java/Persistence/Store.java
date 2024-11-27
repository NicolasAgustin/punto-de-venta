/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistence;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;

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
    
    public boolean add(T entity) {
        
        EntityManager em = null;
        
        try {
            
            em = this.emf.createEntityManager();
        
            em.getTransaction().begin();
            em.persist(entity);
            
            return true;
            
        } catch (Exception ex) {
            return false;
        }
    }
    
    public List<T> list() {
    
        EntityManager em = null;

        try {
            em = this.emf.createEntityManager();
        
            em.getTransaction().begin();
            
            return em.createQuery("from " + this.typeClass.getName(), this.typeClass).getResultList();
        } catch (Exception ex) { 
            return new ArrayList<T>();
        } finally {
            if (em != null) em.close();
        }
        
    }
    
    public List<T> search(String columnName, Object value) {
        
        String query = "FROM " + this.typeClass.getSimpleName() + " t WHERE t." + columnName + " = " + value;
        
        EntityManager em = null;
        
        try {
            
            em = this.emf.createEntityManager();
            em.getTransaction().begin();
            return em.createQuery(query, this.typeClass).getResultList();
        } catch (Exception ex) {
            
            System.out.println(ex.toString());
            
            return new ArrayList<T>();   
        }
        
    }
    
    
//    public List<T> search(String columnName, Object value) {
//        
//        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
//        
//        String query = "SELECT t FROM " + this.typeClass.getSimpleName() + " t WHERE " + columnName + " = ?1";
//        
//        try {
//            TypedQuery<T> typedQuery = entityManager.createQuery(query, this.typeClass);
//            
//            typedQuery.setParameter(1, value);
//            
//            return typedQuery.getResultList();
//            
//        } finally {
//            entityManager.close();
//        }
//        
//    }
    
    
//    public List<T> search() {
//        // TODO: Agregar soporte para filtros
//        Session session = this.sessionFactory.openSession();
//        session.beginTransaction();
//        
//        List<T> entities = session.createQuery("from " + this.typeClass.getSimpleName(), this.typeClass).list();
//        
//        session.close();
//        
//        return entities;
//    }
    
    public void update(T entity) {
        // Revisar
//        Session session = this.sessionFactory.openSession();
//        session.beginTransaction();
//        
//        session.merge(entity);
//        
//        session.getTransaction().commit();
//        
//        session.close();

        EntityManager em = null;
        
        try {
            em = this.emf.createEntityManager();
            
            em.merge(entity);
            
        } catch (Exception ex) {}

    }
    
    public void delete(T entity) {
//        Session session = this.sessionFactory.openSession();
//        session.beginTransaction();
//        
//        session.remove(entity);
//        
//        session.getTransaction().commit();
//        
//        session.close();

        EntityManager em = null;
        try {
            em = this.emf.createEntityManager();
            
            em.remove(entity);
            
        } catch(Exception ex) {}
    }
    
    public T fetch(Long id) {
//        Session session = this.sessionFactory.openSession();
//        session.beginTransaction();
//        
//        T entity = session.get(this.typeClass, id);
//        
//        session.close();
//        
//        return entity;
        EntityManager em = null;
        
        try {
            em = this.emf.createEntityManager();
            T entityFound = em.find(this.typeClass, id);
            
            if (entityFound == null) return null;
            
            return entityFound;
            
        } catch (Exception ex) {
            return null;
        }
    }
    
}
