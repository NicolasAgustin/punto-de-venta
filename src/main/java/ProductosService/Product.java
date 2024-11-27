/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProductosService;

import Modelo.BaseTableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 *
 * @author Nico
 */
@Entity
@Table(name = "products")
public class Product implements BaseTableModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name="unitary_price")
    private float unitaryPrice;
    
    @Column(name="initial_quantity")
    private int initialQuantity;
    
    @Column(name="title", length=100)
    private String title;
    
    @Column(name="description", length=100)
    private String description = null;
    
    @Column(name="code", length=50)
    private String code;
    
    @Column(name="provider", length=50)
    private String provider;
    // TODO: Convertir en enum (ni idea)
    
    @Column(name="category", length=50)
    private String category;
    
    @Column(name="enabled")
    private boolean enabled;
    
    public Product() {}
    
    @Override
    public String toString() {
        return this.title;
    }
    
    @Override
    public Object[] toArray() {
        Object[] values = new Object[]{
            this.id,
            this.unitaryPrice,
            this.initialQuantity,
            this.title,
            this.description,
            this.code,
            this.provider,
            this.category,
            this.enabled,
                //agregar los campos nuevos
        };    
        return values;
    }
    
    public static String[] getColumnNames() {
        return new String[]{ "ID", "PRECIO UNITARIO", "CANTIDAD INICIAL", "TITULO", "DESCRIPCION", "CODIGO", "PROVEEDOR", "CATEGORIA", "ESTADO"};
    }
    
    public Product(
        int id,
        float unitaryPrice,
        int initialQuantity,
        String title,
        String descripcion,
        String code,
        String provider,
        String category,
        boolean enabled
    ) {
       
        this.id = id;
        this.unitaryPrice = unitaryPrice;
        this.initialQuantity = initialQuantity;
        this.title = title;
        this.description = description;
        //Esto es para el codigo de barra Ej 12312312312312312312423346544324124123
        this.code = code;
        this.provider = provider;
        this.category = category;
        this.enabled = enabled;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;

    }
    
    public int getId() {
        return id;
    }

    public void setUnitaryPrice(float unitaryPrice){
        this.unitaryPrice = unitaryPrice;
    }
    
    public float getUnitaryPrice() {
        return this.unitaryPrice;
    }
    
    public void setCode(String code){
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
     public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
   
}
