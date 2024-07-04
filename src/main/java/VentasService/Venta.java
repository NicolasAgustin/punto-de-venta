/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import Modelo.BaseTableModel;
import ProductosService.Producto;
import VentasService.ProductoDetalle;
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
public class Venta implements BaseTableModel {
    private int id;
    private float total = 0;
    private List<ProductoDetalle> detalle;
    // TODO: Agregar clase para la informacion del pago
    private PaymentInformation paymentInfo;
    
    public Venta() {
        id = 0;
        total = 0;
        detalle = new ArrayList<>();
    }
    
    public Venta(int id, int total, String compradorDNI, List<ProductoDetalle> detalle) {
        this.id = id;
        this.total = total;
        this.detalle = detalle;
    }
    
    /////// ESTA LOGICA DEBERIA ESTAR EN VENTAS-SERVICE
    public void addDetalle(ProductoDetalle prod) {
        this.detalle.add(prod);
        this.total += prod.getTotal();
    }
    
    public void setPaymentInformation(PaymentInformation paymentInfo) {
        this.paymentInfo = paymentInfo;
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

    public List<ProductoDetalle> getDetalle() {
        return this.detalle;
    }
    
    public void setDetalle(List<ProductoDetalle> detalle) {
        this.detalle = detalle;
    }
    
    public int getId() {
        return id;
    }

    public void setTotal(float total){
        this.total = total;
    }
    
    public float getTotal() {
        return this.total;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Object[] toArray() {
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        return new Object[]{
            this.id,
            this.paymentInfo.getAmount(), //total
            df.format(new Date(this.paymentInfo.getTimestamp())),//fecha
            this.paymentInfo.getCurrency(), //moneda
            this.paymentInfo.getRef(),
            this.paymentInfo.getLocation()
        };
    }
    
    public static final String[] getColumnNames() {
        return new String[]{ "ID", "TOTAL", "FECHA", "MONEDA", "REFERENCIA", "LOCALIZACION" };
    }
}
