package ProductosService;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "precios_proveedor_producto")
public class PrecioProveedorProducto {

    @EmbeddedId // Usamos @EmbeddedId para la clave primaria compuesta
    private PrecioProveedorProductoId id;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con Producto
    @MapsId("productoId") // Mapea el ID de Producto a la propiedad 'productoId' en el ID embebido
    @JoinColumn(name = "producto_id") // Columna de la clave foránea
    private Product producto;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con Proveedor
    @MapsId("proveedorId") // Mapea el ID de Proveedor a la propiedad 'proveedorId' en el ID embebido
    @JoinColumn(name = "proveedor_id") // Columna de la clave foránea
    private Provider proveedor;

    private double precioCompra;

    // Getters y setters para el ID, producto, proveedor y precio

    public PrecioProveedorProductoId getId() {
        return id;
    }

    public void setId(PrecioProveedorProductoId id) {
        this.id = id;
    }

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
        if (this.id == null) {
            this.id = new PrecioProveedorProductoId();
        }
        // Asegúrate de que el ID del producto se asigne al ID compuesto
        this.id.setProductoId(producto.getId());
    }

    public Provider getProveedor() {
        return proveedor;
    }

    public void setProveedor(Provider proveedor) {
        this.proveedor = proveedor;
        if (this.id == null) {
            this.id = new PrecioProveedorProductoId();
        }
        // Asegúrate de que el ID del proveedor se asigne al ID compuesto
        this.id.setProveedorId(proveedor.getId());
    }

    public double getPrecioCompra() {
        return this.precioCompra;
    }

    public void setPrecioCompra(double precio) {
        this.precioCompra = precio;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrecioProveedorProducto that = (PrecioProveedorProducto) o;
        return Objects.equals(id, that.id); // Delega la comparación al ID compuesto
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Delega el cálculo del hash al ID compuesto
    }

    // Clase de la clave primaria compuesta (debe ser @Embeddable)
    @Embeddable
    public static class PrecioProveedorProductoId implements Serializable {
        // Estas propiedades deben ser del tipo de la clave primaria de Product y Provider
        // product.id es 'int'
        @Column(name = "producto_id")
        private int productoId;

        // provider.id es 'Long'
        @Column(name = "proveedor_id")
        private int proveedorId;

        // Constructor vacío (obligatorio para Hibernate)
        public PrecioProveedorProductoId() {
        }

        // Constructor con todos los campos
        public PrecioProveedorProductoId(int productoId, int proveedorId) {
            this.productoId = productoId;
            this.proveedorId = proveedorId;
        }

        // Getters y setters para las propiedades de la clave compuesta
        public int getProductoId() {
            return productoId;
        }

        public void setProductoId(int productoId) {
            this.productoId = productoId;
        }

        public int getProveedorId() {
            return proveedorId;
        }

        public void setProveedorId(int proveedorId) {
            this.proveedorId = proveedorId;
        }

        // Métodos equals y hashCode (obligatorios para claves compuestas)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrecioProveedorProductoId that = (PrecioProveedorProductoId) o;
            return productoId == that.productoId && Objects.equals(proveedorId, that.proveedorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productoId, proveedorId);
        }
    }
}