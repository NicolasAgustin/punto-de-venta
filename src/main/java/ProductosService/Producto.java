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


/**
 *
 * @author Nico
 */
@Entity
public class Producto implements BaseTableModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    
    @Column(name="PRECIO_UNITARIO")
    private float precioUnitario;
    
    @Column(name="CANTIDAD_INICIAL")
    private int cantidadInicial;
    
    @Column(name="TITULO", length=100)
    private String titulo;
    
    @Column(name="DESCRIPCION", length=100)
    private String descripcion = null;
    
    @Column(name="CODIGO", length=50)
    private String codigo;
    
    @Column(name="PROVEEDOR", length=50)
    private String proveedor;
    // TODO: Convertir en enum (ni idea)
    
    @Column(name="CATEGORIA", length=50)
    private String categoria;
    
    @Column(name="HABILITADO")
    private boolean habilitado;
    
    public Producto() {}
    
    @Override
    public String toString() {
        return this.titulo;
    }
    
    @Override
    public Object[] toArray() {
        Object[] values = new Object[]{
            this.id,
            this.precioUnitario,
            this.cantidadInicial,
            this.titulo,
            this.descripcion,
            this.codigo,
            this.proveedor,
            this.categoria,
            this.habilitado,
                //agregar los campos nuevos
        };    
        return values;
    }
    
    public static String[] getColumnNames() {
        return new String[]{ "ID", "PRECIO UNITARIO", "CANTIDAD INICIAL", "TITULO", "DESCRIPCION", "CODIGO", "PROVEEDOR", "CATEGORIA", "ESTADO"};
    }
    
    public Producto(int id, float precioUnitario, int cantidadInicial, String titulo, String descripcion, String codigo, String proveedor, String categoria, boolean habilitado) {
       
        this.id = id;
        this.precioUnitario = precioUnitario;
        this.cantidadInicial = cantidadInicial;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codigo = codigo; //Esto es para el codigo de barra Ej 12312312312312312312423346544324124123
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.habilitado = habilitado;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;

    }
    
    public int getId() {
        return id;
    }

    public void setPrecioUnitario(float precioUnitario){
        this.precioUnitario = precioUnitario;
    }
    
    public float getPrecioUnitario() {
        return this.precioUnitario;
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(int cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
     public boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
   
}
