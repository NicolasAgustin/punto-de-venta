/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProductosService;

import Modelo.BaseTableModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    
    // This field holds the price that customer will pay for the product
    @Column(name="public_sale_price", nullable = false)
    private double publicSalePrice;
    
    @Column(name="initial_quantity")
    private int initialQuantity;
    
    @Column(name="title", length=100)
    private String title;
    
    @Column(name="description", length=100)
    private String description = null;
    
    @Column(name="code", length=50)
    private String code;
    
    @Column(name="category", length=50)
    private String category;
    
    @Column(name="enabled")
    private boolean enabled;

//    @ManyToMany
//    @JoinTable(
//        name = "products_providers",
//        joinColumns = @JoinColumn(name = "product_id"),
//        inverseJoinColumns = @JoinColumn(name = "provider_id")
//    )
//    private List<Provider> providers;
    
    // TODO: Testear esto
    // - Agregar a pantallas de ABM
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PrecioProveedorProducto> preciosPorProveedor = new HashSet<>();
    
    public Product() {}
    
    @Override
    public String toString() {
        return this.title;
    }
    
    @Override
    public Object[] toArray() {
        Object[] values = new Object[]{
            this.id,
            this.publicSalePrice,
            this.initialQuantity,
            this.title,
            this.description,
            this.code,
            this.category,
            this.enabled,
                //agregar los campos nuevos
        };    
        return values;
    }
    
    public static String[] getColumnNames() {
        return new String[]{ "ID", "PRECIO DE VENTA", "CANTIDAD INICIAL", "TITULO", "DESCRIPCION", "CODIGO", "CATEGORIA", "ESTADO"};
    }
    
    public Product(
        int id,
        Float unitaryPrice,
        int initialQuantity,
        String title,
        String descripcion,
        String code,
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
        this.category = category;
        this.enabled = enabled;
    }

    public Set<PrecioProveedorProducto> getPreciosPorProveedor() {
        return this.preciosPorProveedor;
    }
    
    public void setPreciosPorProveedor(Set<PrecioProveedorProducto> proveedoresPrecios) {
        this.preciosPorProveedor = preciosPorProveedor;
    }
    
    public void agregarPrecioProveedor(PrecioProveedorProducto precioProveedorProducto) {
        this.preciosPorProveedor.add(precioProveedorProducto);
        precioProveedorProducto.setProducto(this);
    }

    public void removerPrecioProveedor(PrecioProveedorProducto precioProveedorProducto) {
        this.preciosPorProveedor.remove(precioProveedorProducto);
        precioProveedorProducto.setProducto(null);
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
    
    public void setPublicSalePrice(double price) {
        this.publicSalePrice = price;
    }
    
    public double getPublicSalePrice() {
        return this.publicSalePrice;
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
