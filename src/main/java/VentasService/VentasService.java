/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

import VentasService.VentasStore;
import VentasService.Venta;
import Utils.Validator;
import Exceptions.ValidationException;
import Exceptions.StoreException;
import ProductosService.Producto;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Nico
 * 
 * Este servicio se encarga de toda la logica de negocio relacionada a las ventas
 * validacion de informacion y creacion de ventas
 */

public class VentasService {
    private VentasStore store = new VentasStore();
    private BillerConnector billerConnector = new BillerConnector();
    
    public static final float IVA_PERCENTAGE = 21;
    
    // Agregar logica de negocio y validacion de informacion
    
    public VentasService() {
    
    }
    
    private void validateVenta(Venta venta) throws ValidationException, StoreException {
        // No se van a guardar datos del cliente en las ventas a
        // excepcion de los pagos con tarjetas
//        if (!Validator.isDniValid(venta.getCompradorDni()))
//            throw new ValidationException("El dni no es valido.");
        
        if (venta.getTotal() < 0.0)
            throw new ValidationException("El monto no puede ser negativo.");
    }
    
    private float calculateIva(float amount) {
        return amount * (float) (IVA_PERCENTAGE / 100.0);
    }
    
    public ProductoDetalle createDetalle(Producto found, int cantidad) {
        float subtotal = found.getPrecioUnitario() * cantidad;
        float total = subtotal + this.calculateIva(subtotal);
        return new ProductoDetalle(found, cantidad, 0, subtotal, IVA_PERCENTAGE, total);
    }
    
    public void addDetalleToVenta(Venta venta, ProductoDetalle detalle) {
        venta.getDetalle().add(detalle);
        venta.setTotal(venta.getTotal() + detalle.getTotal());
    }
    
    public boolean updateDetalleIfDuplicated(List<ProductoDetalle> collected, ProductoDetalle toCheck, Venta currentVenta) {
        if (!collected.contains(toCheck)) {
            return false;
            
        }
        
        int index = collected.indexOf(toCheck);
        collected.get(index).add(toCheck);
        currentVenta.setTotal(currentVenta.getTotal() + toCheck.getTotal());
        return true;
    }
    
    public Venta removeProductoFromDetalle(Venta venta, String codigo) {
    
        List<ProductoDetalle> detalleList = venta.getDetalle();
        
        int indexToRemove = -1;
        boolean flagRemove = false;
        
        // Search across all detail
        for (int i = 0; i < detalleList.size(); i++){
            
            ProductoDetalle pDetalle = detalleList.get(i);
            
            // Check if code is equals
            if (pDetalle.getCodigo().equals(codigo)){
                
                // If quantity is greater than 1 then we must only decrease quantity
                // but not remove element
                if (pDetalle.getCantidad() > 1) {
                    pDetalle.setCantidad(pDetalle.getCantidad() - 1);
                    
                    // Must re calculate total and subtotal
                    float pricePerItemToRemove = pDetalle.getProducto().getPrecioUnitario() + 
                            this.calculateIva(pDetalle.getProducto().getPrecioUnitario());
                    float newTotal = (pDetalle.getTotal() - pricePerItemToRemove);
                    
                    float newSubtotal = pDetalle.getSubtotal() - pDetalle.getProducto().getPrecioUnitario();
                    
                    pDetalle.setSubtotal(newSubtotal);
                    pDetalle.setTotal(newTotal);
                    
                    // Also sale total
                    venta.setTotal(venta.getTotal() - pricePerItemToRemove);
                    
                    break;
                }
                
                indexToRemove = i;
                flagRemove = true;
                break;
            }
            
        }
        
        if (flagRemove) {
            venta.setTotal(venta.getTotal() - detalleList.get(indexToRemove).getTotal());
            detalleList.remove(indexToRemove);
            venta.setDetalle(detalleList);
        }
    
        return venta;
    }
    
    public void add(Venta venta, String method) throws ValidationException, StoreException {
        
        this.validateVenta(venta);
        
        UUID uuid = UUID.randomUUID();
        
        // Procesar pago
        PaymentInformation paymentInfo = new PaymentInformation(method, "ARS", venta.getTotal(), uuid.toString(), "Argentina");
        paymentInfo.processPayment();
        
        // Enviar informacion para facturacion etc
        venta.setPaymentInformation(paymentInfo);
        billerConnector.createInvoice();
        
        try {
            this.store.Registrar(venta);
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
//    public void delete(Venta venta) {
//        // Eliminar una venta implica que se pierda registro de esa transaccion
//        // No puede ocurrir
//    }
    
    public void update(Venta venta) throws ValidationException, StoreException {
        this.validateVenta(venta);
        
        try {
            this.store.Modificar(venta);
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    
    public List<Venta> list() throws StoreException {
        try {
            return this.store.Listar();
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    public Venta fetch(int id) throws StoreException {
        try {
            Venta request = new Venta();
            request.setId(id);
            return this.store.Obtener(request);
        } catch (Exception ex) {
            throw new StoreException("Codigo de barras no reconocido");
        }
    }
    
}
