import ProductosService.Product;
import ProductosService.Provider;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "precios_proveedor_producto")
@IdClass(PrecioProveedorProducto.PrecioProveedorProductoId.class)
public class PrecioProveedorProducto {

    @Embeddable
    public static class PrecioProveedorProductoId implements Serializable {
        @Column(name = "producto_id")
        private Long productoId;

        @Column(name = "proveedor_id")
        private Long proveedorId;

        // Constructor, equals y hashCode

        public PrecioProveedorProductoId() {
        }

        public PrecioProveedorProductoId(Long productoId, Long proveedorId) {
            this.productoId = productoId;
            this.proveedorId = proveedorId;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Long getProveedorId() {
            return proveedorId;
        }

        public void setProveedorId(Long proveedorId) {
            this.proveedorId = proveedorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrecioProveedorProductoId that = (PrecioProveedorProductoId) o;
            return Objects.equals(productoId, that.productoId) && Objects.equals(proveedorId, that.proveedorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productoId, proveedorId);
        }
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Provider proveedor;

    private Double precio;

    // Getters y setters

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }

    public Provider getProveedor() {
        return proveedor;
    }

    public void setProveedor(Provider proveedor) {
        this.proveedor = proveedor;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}