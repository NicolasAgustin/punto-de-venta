
package ProductosService;

import Modelo.BaseTableModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author calisto
 */
@Entity
@Table(name = "providers")
public class Provider implements BaseTableModel
{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PrecioProveedorProducto> preciosPorProducto = new HashSet<>();
    
//    @ManyToMany(mappedBy = "providers")
//    private List<Product> products;
    
    @Column(name="name")
    private String name;
    
    @Column(name="description")
    private String description;
    
    @Column(name="tax_payer_id")
    private String taxPayerId;
    
    public Provider() {}
    
    public Provider(String name, String description, String taxPayerId, List<Product> products) {
        this.name = name;
        this.description = description;
        this.taxPayerId = taxPayerId;
    }
 
    public void setPreciosPorProducto(Set<PrecioProveedorProducto> preciosPorProducto) {
        this.preciosPorProducto = preciosPorProducto;
    }
    
    public Set<PrecioProveedorProducto> getPreciosPorProducto() {
        return preciosPorProducto;
    }
    
    public void agregarPrecioProducto(PrecioProveedorProducto precioProveedorProducto) {
        this.preciosPorProducto.add(precioProveedorProducto);
        precioProveedorProducto.setProveedor(this);
    }

    public void removerPrecioProducto(PrecioProveedorProducto precioProveedorProducto) {
        this.preciosPorProducto.remove(precioProveedorProducto);
        precioProveedorProducto.setProveedor(null);
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaxPayerId() {
        return taxPayerId;
    }

    public void setTaxPayerId(String taxPayerId) {
        this.taxPayerId = taxPayerId;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public Object[] toArray() {
        Object[] values = new Object[]{
            this.id,
            this.name,
            this.description,
            this.taxPayerId
                //agregar los campos nuevos
        };    
        return values;
    }
    
    public static String[] getColumnNames() {
        return new String[]{ "ID", "RAZON SOCIAL", "DESCRIPCION", "CUIT/CUIL" };
    }
    
}
