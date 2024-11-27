/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ProductosService;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Nico
 */
public class ProductsServiceTest {
    
    public ProductsServiceTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
//    @BeforeAll
//    public static void setUpClass() {
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//    }
//    
//    @BeforeEach
//    public void setUp() {
//    }
//    
//    @AfterEach
//    public void tearDown() {
//    }

    /**
     * Test of searchByCodigo method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testSearchByCodigo() throws Exception {
        System.out.println("searchByCodigo");
        String codigo = "";
        ProductsService instance = new ProductsService();
        Product expResult = null;
        Product result = instance.searchByCodigo(codigo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetch method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testFetch() throws Exception {
        System.out.println("fetch");
        Long id = null;
        ProductsService instance = new ProductsService();
        Product expResult = null;
        Product result = instance.fetch(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testAdd() throws Exception {
        System.out.println("add");
        Product producto = null;
        ProductsService instance = new ProductsService();
        instance.add(producto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of list method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testList() throws Exception {
        System.out.println("list");
        ProductsService instance = new ProductsService();
        List<Product> expResult = null;
        List<Product> result = instance.list();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Product producto = null;
        ProductsService instance = new ProductsService();
        instance.update(producto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class ProductsService.
     */
    @org.junit.jupiter.api.Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        Product producto = null;
        ProductsService instance = new ProductsService();
        instance.delete(producto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
