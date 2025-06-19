/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import ProductosService.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import Utils.Utils;

/**
 *
 * @author Nico
 */
@Entity
@Table(name = "sale_detail")
public class Detail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name="sale_id", referencedColumnName="id")
    private Sale sale;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "discount")
    private int discount;
    
    @Column(name = "subtotal")
    private double subtotal;
    
    @Column(name = "iva")
    private double iva;
    
    @Column(name = "total")
    private double total;

    public Detail(Product prod, int quantity, int discount, double subtotal, double iva, double total) {
        this.product = prod;
        this.quantity = quantity;
        this.discount = discount;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }
    
    public Detail() {
        this.product = null;
        this.quantity = 0;
        this.discount = 0;
        this.subtotal = 0;
        this.iva = 0;
        this.total = 0;
    }
    
    @Override
    public boolean equals(Object obj) {
    
        // To use contains
        Detail pd2 = (Detail) obj;
        
        return this.product.getCode().equals(pd2.getCode());
    }
    
    public static final String[] getColumnNames() {
        return new String[]{ "Codigo", "Producto", "Cantidad", "Precio unitario", "Descuento", "Subtotal", "Iva", "Total"};
    }
    
    public Sale getSale() {
        return this.sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    
    public Object[] toObject() {
        return new Object[]{
            this.product.getCode(),
            this.product.getTitle(),
            this.quantity,
            this.product.getPublicSalePrice(),
            this.discount,
            this.subtotal,
            this.iva,
            this.total
        };
    }
    
    public Product getProducto() {
        return product;
    }

    public String getCode() {
        return this.product.getCode();
    }
    
    public void setProducto(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setCantidad(int quantity) {
        this.quantity = quantity;
        float uPrice = this.product.getUnitaryPrice(); // ???
        
        
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDescuento(int discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void add(Detail detail) {
        this.quantity += detail.quantity;
        this.subtotal += detail.subtotal;
        // Is an error to sumarize the amount of iva
        // this.iva += detail.iva;
        this.total += detail.total;
        this.total = Utils.roundDouble(this.total);
    }
}
