package ProductosService;

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
        private Long producto;

        @Column(name = "proveedor_id")
        private Long proveedor;

        // Constructor, equals y hashCode

        public PrecioProveedorProductoId() {
        }

        public PrecioProveedorProductoId(Long producto, Long proveedor) {
            this.producto = producto;
            this.proveedor = proveedor;
        }

        public Long getProducto() {
            return producto;
        }

        public void setProducto(Long producto) {
            this.producto = producto;
        }

        public Long getProveedor() {
            return proveedor;
        }

        public void setProveedor(Long proveedor) {
            this.proveedor = proveedor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrecioProveedorProductoId that = (PrecioProveedorProductoId) o;
            return Objects.equals(producto, that.producto) && Objects.equals(proveedor, that.proveedor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(producto, proveedor);
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

    private double precio;

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}