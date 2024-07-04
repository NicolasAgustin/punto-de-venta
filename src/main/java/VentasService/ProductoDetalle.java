/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import ProductosService.Producto;

/**
 *
 * @author Nico
 */
public class ProductoDetalle {
    
    private String productoCodigo;
    private Producto producto;
    private int cantidad;
    private int descuento;
    private float subtotal;
    private float iva;
    private float total;

    public ProductoDetalle(Producto prod, int cantidad, int descuento, float subtotal, float iva, float total) {
        this.producto = prod;
        this.productoCodigo = this.producto.getCodigo();
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }
    
    @Override
    public boolean equals(Object obj) {
    
        // To use contains
        ProductoDetalle pd2 = (ProductoDetalle) obj;
        
        return this.productoCodigo.equals(pd2.getCodigo());
    }
    
    public static final String[] getColumnNames() {
        return new String[]{ "Codigo", "Producto", "Cantidad", "Precio unitario", "Descuento", "Subtotal", "Iva", "Total"};
    }
    
    public Object[] toObject() {
        return new Object[]{
            this.productoCodigo,
            this.producto,
            this.cantidad,
            this.producto.getPrecioUnitario(),
            this.descuento,
            this.subtotal,
            this.iva,
            this.total
        };
    }
    
    public Producto getProducto() {
        return producto;
    }

    public String getCodigo() {
        return this.productoCodigo;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.productoCodigo = producto.getCodigo();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        float uPrice = this.producto.getPrecioUnitario();
        
        
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    public void add(ProductoDetalle detalle) {
        this.cantidad += detalle.cantidad;
        this.subtotal += detalle.subtotal;
        this.iva += detalle.iva;
        this.total += detalle.total;
    }
}
