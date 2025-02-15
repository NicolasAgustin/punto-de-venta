
package ProductosService;

import Modelo.BaseTableModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

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
    
    @ManyToMany(mappedBy = "providers")
    private List<Product> products;
    
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
        this.products = products;
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
