/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import Modelo.BaseTableModel;
import VentasService.Detail;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Nico
 * 
 * Este modelo solamente es para mantener la informacion
 */
@Entity
@Table(name = "sales")
public class Sale implements BaseTableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="total")
    private float total = 0;
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sale_id") // Esta columna va a estar en la tabla sale_details
    private List<Detail> detail;
    
    @Column(name="timestamp")
    private long timestamp;
    
    @Column(name="currency")
    private String currency;
    
    public Sale() {
        id = new Long(0);
        total = 0;
        detail = new ArrayList<>();
    }
    
    public Sale(Long id, int total, String compradorDNI, List<Detail> detail) {
        this.id = id;
        this.total = total;
        this.detail = detail;
    }
    
    /////// ESTA LOGICA DEBERIA ESTAR EN VENTAS-SERVICE
    public void addDetail(Detail prod) {
        this.detail.add(prod);
        this.total += prod.getTotal();
    }
    
//    public void removeProduct(String codigo) {
//        int indexToRemove = -1;
//        boolean flagRemove = false;
//        
//        // Search across all detail
//        for (int i = 0; i < this.detalle.size(); i++){
//            
//            ProductoDetalle pDetalle = this.detalle.get(i);
//            
//            // Check if code is equals
//            if (pDetalle.getCodigo().equals(codigo)){
//                
//                // If quantity is greater than 1 then we must only decrease quantity
//                // but not remove element
//                if (pDetalle.getCantidad() > 1) {
//                    pDetalle.setCantidad(pDetalle.getCantidad() - 1);
//                    break;
//                }
//                
//                indexToRemove = i;
//                flagRemove = true;
//                break;
//            }
//            
//        }
//        
//        if (flagRemove) {
//            this.total -= this.detalle.get(indexToRemove).getTotal();
//            this.detalle.remove(indexToRemove);
//        } 
//    }
    
    /////////////////////////////////////////////////////////////////////

    public List<Detail> getDetail() {
        return this.detail;
    }
    
    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }
    
    public Long getId() {
        return id;
    }

    public void setTotal(float total){
        this.total = total;
    }
    
    public float getTotal() {
        return this.total;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Object[] toArray() {
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        return new Object[]{
            this.id,
            this.total, //total
            df.format(new Date(this.timestamp)),//fecha
            this.currency //moneda
        };
    }
    
    public static final String[] getColumnNames() {
        return new String[]{ "ID", "TOTAL", "FECHA", "MONEDA" };
    }
}
