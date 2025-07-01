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
import jakarta.persistence.FetchType;
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

import Utils.Utils;

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
    private double total = 0;
    
    @Column(name="invoice_number")
    private String invoice_number;
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy="sale")
    private List<Detail> detail;
    
    @Column(name="timestamp")
    private long timestamp;
    
    @Column(name="currency")
    private String currency;
    
    public Sale() {
        id = new Long(0);
        total = 0;
        detail = new ArrayList<>();
        invoice_number = "";
    }
    
    public Sale(Long id, int total, String compradorDNI, List<Detail> detail) {
        this.id = id;
        this.total = total;
        this.detail = detail;
    }
    
    /////// ESTA LOGICA DEBERIA ESTAR EN VENTAS-SERVICE
    public void addDetail(Detail prod) {
        prod.setSale(this);
        this.detail.add(prod);
        this.total += prod.getTotal();
        this.total = Utils.roundDouble(this.total);
    }
    
    // TODO: IMPLEMENTAR ESTO
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
    
    public String getCurrency() {
        return this.currency;
    }
    
    public String getInvoiceNumber() {
        return this.invoice_number;
    }
    
    public void setInvoiceNumber(String invoice_number) {
        this.invoice_number = invoice_number;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }
    
    public Long getId() {
        return id;
    }

    public void setTotal(double total){
        this.total = total;
    }
    
    public double getTotal() {
        return this.total;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    

    @Override
    public Object[] toArray() {
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        return new Object[]{
            this.id,
            this.total, //total
            this.invoice_number,
            df.format(new Date(this.timestamp)),//fecha
            this.currency //moneda
        };
    }
    
    public static final String[] getColumnNames() {
        return new String[]{ "ID", "TOTAL", "NUMERO DE FACTURA", "FECHA", "MONEDA" };
    }
}
