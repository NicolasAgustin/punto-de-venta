/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VentasService;

//import VentasService.VentasStore;
import VentasService.Sale;
import VentasService.Detail;
import Utils.Validator;
import Exceptions.ValidationException;
import Exceptions.StoreException;
import ProductosService.Product;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import Persistence.Store;
import java.time.Instant;
import org.hibernate.Session;

/**
 *
 * @author Nico
 * 
 * Este servicio se encarga de toda la logica de negocio relacionada a las ventas
 * validacion de informacion y creacion de ventas
 */

public class VentasService {
//    private VentasStore store = new VentasStore();
    
    private Store<Sale> store = new Store(Sale.class);
    
    private BillerConnector billerConnector = new BillerConnector();
    
    public static final double IVA_PERCENTAGE = 21;
    
    // Agregar logica de negocio y validacion de informacion
    
    public VentasService() {
    
    }
    
    private void validateVenta(Sale sale) throws ValidationException, StoreException {
        // No se van a guardar datos del cliente en las ventas a
        // excepcion de los pagos con tarjetas
//        if (!Validator.isDniValid(venta.getCompradorDni()))
//            throw new ValidationException("El dni no es valido.");
        
        if (sale.getTotal() < 0.0)
            throw new ValidationException("El monto no puede ser negativo.");
    }
    
    private double calculateIva(double amount) {
        return amount * (double)(IVA_PERCENTAGE / 100.0);
    }
    
    public Detail createDetalle(Product found, int cantidad) {
        double subtotal = found.getPublicSalePrice() * cantidad;
        double total = Utils.Utils.roundDouble(subtotal + this.calculateIva(subtotal));
        return new Detail(found, cantidad, 0, subtotal, IVA_PERCENTAGE, total);
    }
    
    public void addDetalleToVenta(Sale sale, Detail detail) {
        sale.getDetail().add(detail);
        sale.setTotal(sale.getTotal() + detail.getTotal());
    }
    
    public boolean updateDetalleIfDuplicated(List<Detail> collected, Detail toCheck, Sale currentSale) {
        if (!collected.contains(toCheck)) {
            return false;
            
        }
        
        int index = collected.indexOf(toCheck);
        collected.get(index).add(toCheck);
        currentSale.setTotal(Utils.Utils.roundDouble(currentSale.getTotal() + toCheck.getTotal()));
        return true;
    }
    
    public Sale removeProductoFromDetalle(Sale sale, String codigo) {
    
        List<Detail> detalleList = sale.getDetail();
        
        int indexToRemove = -1;
        boolean flagRemove = false;
        
        // Search across all detail
        for (int i = 0; i < detalleList.size(); i++){
            
            Detail pDetalle = detalleList.get(i);
            
            // Check if code is equals
            if (pDetalle.getCode().equals(codigo)){
                
                // If quantity is greater than 1 then we must only decrease quantity
                // but not remove element
                if (pDetalle.getQuantity() > 1) {
                    pDetalle.setCantidad(pDetalle.getQuantity() - 1);
                    
                    // Must re calculate total and subtotal
                    double pricePerItemToRemove = pDetalle.getProducto().getUnitaryPrice() + 
                            this.calculateIva(pDetalle.getProducto().getUnitaryPrice());
                    double newTotal = (pDetalle.getTotal() - pricePerItemToRemove);
                    
                    double newSubtotal = pDetalle.getSubtotal() - pDetalle.getProducto().getUnitaryPrice();
                    
                    pDetalle.setSubtotal(newSubtotal);
                    pDetalle.setTotal(newTotal);
                    
                    // Also sale total
                    sale.setTotal(sale.getTotal() - pricePerItemToRemove);
                    
                    break;
                }
                
                indexToRemove = i;
                flagRemove = true;
                break;
            }
            
        }
        
        if (flagRemove) {
            sale.setTotal(sale.getTotal() - detalleList.get(indexToRemove).getTotal());
            detalleList.remove(indexToRemove);
            sale.setDetail(detalleList);
        }
    
        return sale;
    }
    
    public List<Detail> getDetailFromSale(Long id) {
    
        Session session = this.store.createSession();
        session.beginTransaction();
        Sale sale = (Sale)session.find(Sale.class, id);

        List<Detail> detail = sale.getDetail();
        
        session.close();
        
        return detail;
        
    }
    
    public void add(Sale sale, String method) throws ValidationException, StoreException {
        
        this.validateVenta(sale);
        
        UUID uuid = UUID.randomUUID();
        
        // Procesar pago
        PaymentInformation paymentInfo = new PaymentInformation(method, sale);
        paymentInfo.processPayment();
        
        // Enviar informacion para facturacion etc
//        sale.setPaymentInformation(paymentInfo);
        
        for (Detail saleDetail: sale.getDetail()) {
            saleDetail.setSale(sale);
        }

        int invoice_number = billerConnector.createInvoice();
        sale.setInvoiceNumber(invoice_number+"");
        sale.setCurrency("ARS");
        try {
            sale.setTimestamp(Instant.now().toEpochMilli());
            this.store.add(sale);
          
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
        
    }
    
//    public void delete(Venta venta) {
//        // Eliminar una venta implica que se pierda registro de esa transaccion
//        // No puede ocurrir
//    }
    
    public void update(Sale sale) throws ValidationException, StoreException {
        this.validateVenta(sale);
        
        try {
            this.store.update(sale);
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    
    public List<Sale> list() throws StoreException {
        try {
            return this.store.list();
        } catch(Exception ex) {
            throw new StoreException(ex.getMessage());
        }
    }
    public Sale fetch(Long id) throws StoreException {
        try {
            return this.store.fetch(id);
        } catch (Exception ex) {
            throw new StoreException("Codigo de barras no reconocido");
        }
    }
    
}
