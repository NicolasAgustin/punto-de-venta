/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Exceptions.StoreException;
import Exceptions.ValidationException;
import Forms.FormError;
import Forms.NewProductForm;

import Persistence.HibernateUtil;
import ProductosService.Category;
import ProductosService.PrecioProveedorProducto;

import Utils.Utils;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import VentasService.VentasService;

import ProductosService.Product;
import ProductosService.ProductsService;
import ProductosService.ProductosValidacion;
import ProductosService.Provider;
import Utils.Pair;
import VentasService.Detail;
//import VentasService.ProductoDetalle;
import VentasService.Sale;
import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Math.round;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.hibernate.Session;


//import org.hibernate.Session;
/**
 *
 * @author JEN
 */
public class Sistema extends javax.swing.JFrame {
//    ClienteDAO client = new ClienteDAO();
//    MockClienteDAO client = new MockClienteDAO()
    
    int contadorP = 0;
    int obligTit = 0;
    int obligPrec = 0;
    int obligCantI = 0;
    int obligCod = 0;

    static int bandera = 0;
    static DecimalFormat df = new DecimalFormat("#.##");
    DefaultTableModel modelo = new DefaultTableModel();
    //Tabla de productos

    DefaultListModel listamodeloTitulo = new DefaultListModel();
    DefaultListModel listamodeloDesc = new DefaultListModel();
    DefaultListModel listamodeloCod = new DefaultListModel();

    JList jList1 = new JList();
    JList jList2 = new JList();
    JList jList3 = new JList();
    
    // Servicio para manejar la creacion y obtencion de ventas
    VentasService ventasService = new VentasService();
    ProductsService productosService = new ProductsService();
    JFrame modalFrame = new JFrame();
    JFrame modalVentaFrame = new JFrame();
    JFrame modalUpdateFrame = new JFrame();
    JFrame modalProviderFrame = new JFrame();
    JFrame modalProviderLinkProductsFrame = new JFrame();
    JFrame modalProviderLinkProductsFramePCP = new JFrame();
    JDialog modalDialog;
    Sale tmpVenta = new Sale();

    //Boton eliminar:
    JButton botonEliminar = new JButton("btnEliminarpro");
    
    Border DEFAULT_BORDER = null;
    Border ERROR_BORDER = new LineBorder(Color.RED, 2);

    //Para el boton buscar:   
    TableRowSorter trs;

    public Sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
//        btnSaveProducto.setEnabled(false);//Este boton al proncipio esta deshabilitado

        updateEnabledGroup.add(btnUpdateEnabled);
        updateEnabledGroup.add(btnUpdateEnabled1);
        
        DEFAULT_BORDER = txtTit.getBorder();
        
        // Remove the JTabbedPane header
        principalPanel.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex){}
        });
        
        TableProvidersProducts.setModel(new DefaultTableModel() {
            
            String[] columns = new String[]{"ID", "PDV", "PCP", "TITULO", "CODIGO"};

            @Override
            public int getColumnCount() {
                return columns.length;
            }
            
            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        });
        
        // Custom model for Products table
        TableProducto.setModel(new DefaultTableModel() {
            String[] columns = Product.getColumnNames();

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
        });

        TableProvider.setModel(new DefaultTableModel() {
            String[] columns = Provider.getColumnNames();

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        });
        
        TableProvider.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Verificar si la selección está finalizada y no hay ajustes en curso
                // Si no chequeamos TableProvider.getSelectedRowCount() este listener se va a
                // llamar en ocasiones inesperadas, mostrando el modal cuando no debe
                if (!e.getValueIsAdjusting() && TableProvider.getSelectedRowCount() > 0) {
                    LoadProvidersProducts();
                }
            }
        });
        
        // Custom model for Ventas table
        TableVenta.setModel(new DefaultTableModel() {
            String[] columns = Detail.getColumnNames();

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
        });
        
        // Custom model for Venta detalle table
        TableVentaDetalle.setModel(new DefaultTableModel() {
            String[] columns = Detail.getColumnNames();

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        });
        
        TableVentasHistorico.setModel(new DefaultTableModel() {
            String[] columns = Sale.getColumnNames();
            
            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int index) {
                return columns[index];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        });
        
        TableVentasHistorico.setRowSelectionAllowed(true);  // Permitir seleccionar filas completas
        TableVentasHistorico.setColumnSelectionAllowed(false);
        
        LoadProductos();
        
        productToLinkTextField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProviderLinkedProductsList(); // Se llama cuando se inserta texto
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProviderLinkedProductsList(); // Se llama cuando se elimina texto
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Este evento es para cambios en atributos (no en texto simple), usualmente no se usa aquí.
            }
        });

        jtxtFiltro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    // Definir las columnas a filtrar (en este caso, las columnas 3 y 5)
                    int[] columnasAFiltrar = {3, 5};

                    // Crear una lista de RowFilter para cada columna
                    List<RowFilter<Object, Object>> filters = new ArrayList<>();
                    for (int columna : columnasAFiltrar) {
                        // Crear un filtro que ignore la distinción entre mayúsculas y minúsculas
                        RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + jtxtFiltro.getText(), columna);
                        filters.add(filter);
                    }

                    // Combinar los filtros en un RowFilter compuesto
                    RowFilter<Object, Object> combinedFilter = RowFilter.orFilter(filters);

                    // Aplicar el filtro a la tabla
                    trs.setRowFilter(combinedFilter);
                } catch (PatternSyntaxException ex) {
                    // Manejar excepción si la expresión regular es inválida
                    System.out.println(ex);
                } catch (IllegalArgumentException ex) {
                    // Manejar excepción si alguna de las columnas especificadas no existe
                    System.out.println(ex);
                }
            }
        });
    }

    public void filterProviderLinkedProductsList() {
        String value = productToLinkTextField.getText().toLowerCase(Locale.ROOT);
        // String providerID = modalProviderLinkIDLabel.getText().split(" ")[0];
        String taxPayerID = modalProviderLinkIDLabel.getText().split(" ")[1];
        
        try{
            
            List<Product> productsNotLinkedWithProvider = 
                    this.productosService.getProductsNotLinkedToProvider(taxPayerID);
            
            if(value.equals("")) {
                LoadProductsToJList(providerLinkedProductsList, productsNotLinkedWithProvider);
                return;
            }
            
            List<Product> filteredList = new ArrayList();
            productsNotLinkedWithProvider.forEach(p -> {
                if (p.getTitle().toLowerCase(Locale.ROOT).contains(value) || 
                    p.getCode().toLowerCase(Locale.ROOT).contains(value)) {
                    filteredList.add(p);
                }
            });
            
            LoadProductsToJList(providerLinkedProductsList, filteredList);
            
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        
    }
    
    public void LoadProvidersProducts() {
        initializeProvidersProductsModal();
        
        if (TableProvider.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione solo una fila para editar.");
            return;
        }

        // Obtener fila seleccionada
        int filaSeleccionada = TableProvider.getSelectedRow();

        // Verificar si hay una fila seleccionada
        if (filaSeleccionada == -1) {
            return;
        }
        
        TableProvider.clearSelection();

        // Obtener los valores de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) TableProvider.getModel();

        int providerID = Integer.parseInt(TableProvider.getValueAt(filaSeleccionada, model.findColumn("ID")).toString());
        String razon_social = TableProvider.getValueAt(filaSeleccionada, model.findColumn("RAZON SOCIAL")).toString();
        String descripcion = TableProvider.getValueAt(filaSeleccionada, model.findColumn("DESCRIPCION")).toString();
        String tax_payer_id = TableProvider.getValueAt(filaSeleccionada, model.findColumn("CUIT/CUIL")).toString();
        
        List<PrecioProveedorProducto> productos = this.productosService.getProductsByProvider(providerID);
        
        providerNombre.setText(razon_social);
        providerTaxPayerID.setText(tax_payer_id);
        
//        public void LimpiarTable(DefaultTableModel tableModel) { //para limpiar la tabla para que no se muetsren filas repetidas
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            tableModel.removeRow(i);
//            i = i - 1;
//        }
//    }
//        DefaultTableModel dtm = (DefaultTableModel) TableProvidersProducts.getModel();
//        dtm.setRowCount(0);
        
        LimpiarTable((DefaultTableModel) TableProvidersProducts.getModel());

        ////////////////////////////////////////
        List<Object[]> objToAdd = new ArrayList();
        
        productos.forEach(prod -> objToAdd.add(
            new Object[]{
                prod.getProducto().getId(),
                prod.getProducto().getPublicSalePrice(),
                prod.getPrecioCompra(),
                prod.getProducto().getTitle(),
                prod.getProducto().getCode()
            }    
        ));
        

        DefaultTableModel modeloPP = (DefaultTableModel) TableProvidersProducts.getModel();
        
        objToAdd.forEach(obj -> modeloPP.addRow(obj));
        
        TableProvidersProducts.setModel(modeloPP);
        ////////////////////////////////////////
        
        modalProviderFrame.getContentPane().add(modalProvider);
        modalProviderFrame.pack();
        modalProviderFrame.setVisible(true);
        modalProviderLinkIDLabel.setVisible(false);
        modalProviderLinkIDLabel.setText(providerID + " " + tax_payer_id);
    }
    
    public void LoadProductos() {
        List<Object[]> objToAdd = new ArrayList();
        try {
            this.productosService.list().forEach(prod -> objToAdd.add(prod.toArray()));
        } catch (StoreException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        }

        modelo = (DefaultTableModel) TableProducto.getModel();
        
        
        for (Object[] obj: objToAdd) {
            obj[obj.length-1] = (Boolean)obj[obj.length-1] ? "Habilitado" : "Deshabilitado";
        }
        
        objToAdd.forEach(obj -> modelo.addRow(obj));
        
        TableProducto.setModel(modelo);
        // Crear un TableRowSorter y aplicarlo a la tabla
        trs = new TableRowSorter(modelo);
        TableProducto.setRowSorter(trs);
    }
    
    public void LoadProviders() {
        List<Object[]> objToAdd = new ArrayList();
        
        this.productosService.getProviders().forEach(prov -> objToAdd.add(prov.toArray()));
        modelo = (DefaultTableModel) TableProvider.getModel();
        objToAdd.forEach(obj -> modelo.addRow(obj));
        TableProvider.setModel(modelo);
    }

    public void LoadVentas() {
        List<Object[]> objToAdd = new ArrayList();

        try {
            this.ventasService.list().forEach(venta -> objToAdd.add(venta.toArray()));
        } catch (StoreException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        }

        modelo = (DefaultTableModel) TableVenta.getModel();
        objToAdd.forEach(obj -> modelo.addRow(obj));
        TableVenta.setModel(modelo);
    }

    public void LoadVentasHistorico() {
        List<Object[]> objToAdd = new ArrayList();

        try {
            this.ventasService.list().forEach(venta -> objToAdd.add(venta.toArray()));
        } catch (StoreException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        }

        modelo = (DefaultTableModel) TableVentasHistorico.getModel();
        objToAdd.forEach(obj -> modelo.addRow(obj));
        TableVentasHistorico.setModel(modelo);
    }

    public void LoadDetalle(List<Detail> detalle) {
        ClearTableVenta();
        detalle.forEach(prod -> AddDetalleProducto(prod));
    }
    
    public void LoadVentaDetalle(List<Detail> detalle) {
        ClearTableVenta();
        DefaultTableModel dtm = (DefaultTableModel) TableVentaDetalle.getModel();
        dtm.setRowCount(0);
        
        modelo = (DefaultTableModel) TableVentaDetalle.getModel();
        
        for (Detail pDetalle: detalle) {
            Object[] toAdd = pDetalle.toObject();
            
            modelo.addRow(toAdd);
        }
        
        TableVentaDetalle.setModel(modelo);
        
    }

    public void AddDetalleProducto(Detail prodDetalle) {
        Object[] toAdd = prodDetalle.toObject();

        modelo = (DefaultTableModel) TableVenta.getModel();
        modelo.addRow(toAdd);
        TableVenta.setModel(modelo);
    }

    public void LimpiarTable(DefaultTableModel tableModel) { //para limpiar la tabla para que no se muetsren filas repetidas
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.removeRow(i);
            i = i - 1;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        list1 = new java.awt.List();
        PaymentMethodPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        paymentCashMonto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        paymentCashPago = new javax.swing.JTextField();
        paymentCashVuelto = new javax.swing.JTextField();
        btnCashConfirm = new java.awt.Button();
        paymentCashErrorLabel = new javax.swing.JLabel();
        modalProgressBar = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        modalUpdate = new javax.swing.JPanel();
        BtnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        jComboCat = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelCod = new javax.swing.JLabel();
        Desc = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        Titulo = new javax.swing.JTextField();
        CantInici = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        PrecioUni = new javax.swing.JTextField();
        btnUpdateEnabled = new javax.swing.JRadioButton();
        btnUpdateEnabled1 = new javax.swing.JRadioButton();
        labelUpdateID = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        productProveedoresTable = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        modalDetalle = new javax.swing.JPanel();
        ventaDetalleID = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ventaDetalleTotal = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableVentaDetalle = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        ventaDetalleMoneda = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        ventaDetalleFactura = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        ventaDetalleFecha = new javax.swing.JTextField();
        updateEnabledGroup = new javax.swing.ButtonGroup();
        modalProvider = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TableProvidersProducts = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        providerNombre = new javax.swing.JTextField();
        providerTaxPayerID = new javax.swing.JTextField();
        vincularProductosButton = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        modalProviderLinkProductos = new javax.swing.JPanel();
        modalProviderLinkIDLabel = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        providerLinkedProductsList = new javax.swing.JList<>();
        productToLinkTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        modalProviderLinkProductosPCP = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        productosPCPTable = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        providerIDPCP = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        menuVentasBtn = new javax.swing.JButton();
        menuProductosBtn = new javax.swing.JButton();
        ventasButton = new javax.swing.JButton();
        menuProveedoresBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        principalPanel = new javax.swing.JTabbedPane();
        nuevaVentaPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnEliminarventa = new javax.swing.JButton();
        txtCodigoProducto = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableVenta = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        LabelTotal = new javax.swing.JLabel();
        btnSaveSale = new javax.swing.JButton();
        btnAddProductToSale = new javax.swing.JButton();
        checkboxAplicaIva = new java.awt.Checkbox();
        btnClearVenta = new javax.swing.JButton();
        ventasHistPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableVentasHistorico = new javax.swing.JTable();
        btnMostrarDetalle = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtPrecioUni = new javax.swing.JTextField();
        txtCantIni = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableProducto = new javax.swing.JTable();
        btnSaveProducto = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        txtDesc = new javax.swing.JTextField();
        txtCod = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        cbxCatego = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jtxtFiltro = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnEliminarpro = new javax.swing.JButton();
        btnEditarpro = new javax.swing.JButton();
        productsErrorDisplay = new javax.swing.JLabel();
        proveedoresPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableProvider = new javax.swing.JTable();

        jFrame1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        jFrame1.getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 300));

        paymentCashMonto.setEnabled(false);
        paymentCashMonto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentCashMontoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel2.setText("Monto");

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel4.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel4.setText("Su pago");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel6.setText("Vuelto");

        paymentCashPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentCashPagoActionPerformed(evt);
            }
        });

        paymentCashVuelto.setEnabled(false);
        paymentCashVuelto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentCashVueltoActionPerformed(evt);
            }
        });

        btnCashConfirm.setBackground(new java.awt.Color(102, 102, 255));
        btnCashConfirm.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnCashConfirm.setForeground(new java.awt.Color(255, 255, 255));
        btnCashConfirm.setLabel("Confirmar");
        btnCashConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashConfirmActionPerformed(evt);
            }
        });

        paymentCashErrorLabel.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCashConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(23, 23, 23)
                                        .addComponent(paymentCashVuelto))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(paymentCashMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(23, 23, 23)
                                        .addComponent(paymentCashPago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(146, 146, 146)
                                .addComponent(paymentCashErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 60, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentCashMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(paymentCashPago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentCashErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentCashVuelto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(btnCashConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("Efectivo", jPanel9);

        javax.swing.GroupLayout PaymentMethodPanelLayout = new javax.swing.GroupLayout(PaymentMethodPanel);
        PaymentMethodPanel.setLayout(PaymentMethodPanelLayout);
        PaymentMethodPanelLayout.setHorizontalGroup(
            PaymentMethodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        PaymentMethodPanelLayout.setVerticalGroup(
            PaymentMethodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Registrando operacion, aguarde...");

        javax.swing.GroupLayout modalProgressBarLayout = new javax.swing.GroupLayout(modalProgressBar);
        modalProgressBar.setLayout(modalProgressBarLayout);
        modalProgressBarLayout.setHorizontalGroup(
            modalProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProgressBarLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        modalProgressBarLayout.setVerticalGroup(
            modalProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProgressBarLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        BtnCancelar.setFont(new java.awt.Font("Source Sans Pro Black", 0, 14)); // NOI18N
        BtnCancelar.setText("CANCELAR");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        btnAceptar.setFont(new java.awt.Font("Source Sans Pro Black", 0, 14)); // NOI18N
        btnAceptar.setText("ACEPTAR");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        jComboCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Limpieza", "Alimento", "Lacteos", "Textil" }));

        jLabel9.setText("Categoria:");

        jLabel12.setText("Codigo:");

        jLabelCod.setText("........................................................");

        Desc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DescKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DescKeyTyped(evt);
            }
        });

        jLabel13.setText("Descripcion:");

        jLabel14.setText("Titulo:");

        Titulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TituloKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TituloKeyTyped(evt);
            }
        });

        CantInici.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CantIniciKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CantIniciKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CantIniciKeyTyped(evt);
            }
        });

        jLabel15.setText("Cantidad Inicial:");

        jLabel16.setText("Precio de compra:");

        PrecioUni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecioUniActionPerformed(evt);
            }
        });
        PrecioUni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                PrecioUniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PrecioUniKeyTyped(evt);
            }
        });

        btnUpdateEnabled.setText("Habilitado");
        btnUpdateEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEnabledActionPerformed(evt);
            }
        });

        btnUpdateEnabled1.setText("Deshabilitado");
        btnUpdateEnabled1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEnabled1ActionPerformed(evt);
            }
        });

        productProveedoresTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "PRECIO DE COMPRA", "RAZON SOCIAL", "CUIT/CUIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(productProveedoresTable);
        if (productProveedoresTable.getColumnModel().getColumnCount() > 0) {
            productProveedoresTable.getColumnModel().getColumn(0).setResizable(false);
            productProveedoresTable.getColumnModel().getColumn(1).setResizable(false);
            productProveedoresTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel26.setText("Proveedores");

        javax.swing.GroupLayout modalUpdateLayout = new javax.swing.GroupLayout(modalUpdate);
        modalUpdate.setLayout(modalUpdateLayout);
        modalUpdateLayout.setHorizontalGroup(
            modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalUpdateLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(modalUpdateLayout.createSequentialGroup()
                        .addComponent(btnAceptar)
                        .addGap(79, 79, 79)
                        .addComponent(BtnCancelar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, modalUpdateLayout.createSequentialGroup()
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelUpdateID)
                            .addGroup(modalUpdateLayout.createSequentialGroup()
                                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdateEnabled))
                                .addGap(18, 18, 18)
                                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUpdateEnabled1)
                                    .addComponent(jComboCat, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(PrecioUni, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                        .addComponent(CantInici)
                                        .addComponent(Titulo)
                                        .addComponent(Desc)
                                        .addComponent(jLabelCod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );
        modalUpdateLayout.setVerticalGroup(
            modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalUpdateLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelUpdateID)
                .addGap(18, 18, 18)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(PrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modalUpdateLayout.createSequentialGroup()
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(CantInici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(Desc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabelCod))
                        .addGap(18, 18, 18)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateEnabled)
                            .addComponent(btnUpdateEnabled1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnCancelar)
                            .addComponent(btnAceptar)))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );

        ventaDetalleID.setEditable(false);
        ventaDetalleID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventaDetalleIDActionPerformed(evt);
            }
        });

        jLabel17.setText("ID");

        jLabel19.setText("Total");

        ventaDetalleTotal.setEditable(false);

        TableVentaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(TableVentaDetalle);

        jLabel20.setText("Detalle");

        jLabel21.setText("Numero de factura");

        ventaDetalleMoneda.setEditable(false);

        jLabel22.setText("Fecha");

        ventaDetalleFactura.setEditable(false);

        jLabel27.setText("Moneda");

        ventaDetalleFecha.setEditable(false);

        javax.swing.GroupLayout modalDetalleLayout = new javax.swing.GroupLayout(modalDetalle);
        modalDetalle.setLayout(modalDetalleLayout);
        modalDetalleLayout.setHorizontalGroup(
            modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalDetalleLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalDetalleLayout.createSequentialGroup()
                                    .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel21)
                                        .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(modalDetalleLayout.createSequentialGroup()
                                            .addGap(32, 32, 32)
                                            .addComponent(jLabel18)))
                                    .addGap(37, 37, 37))
                                .addGroup(modalDetalleLayout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(81, 81, 81)))
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(81, 81, 81)))
                        .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addComponent(ventaDetalleMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ventaDetalleFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ventaDetalleID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                        .addComponent(ventaDetalleTotal, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ventaDetalleFactura, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        modalDetalleLayout.setVerticalGroup(
            modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalDetalleLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ventaDetalleID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ventaDetalleTotal)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ventaDetalleFactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ventaDetalleFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addComponent(ventaDetalleMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23)))
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        TableProvidersProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "PDV", "PCP", "TITULO", "CODIGO"
            }
        ));
        jScrollPane7.setViewportView(TableProvidersProducts);

        jLabel28.setText("RAZON SOCIAL");

        jLabel29.setText("CUIT/CUIL");

        providerNombre.setEditable(false);
        providerNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                providerNombreActionPerformed(evt);
            }
        });

        providerTaxPayerID.setEditable(false);
        providerTaxPayerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                providerTaxPayerIDActionPerformed(evt);
            }
        });

        vincularProductosButton.setText("Vincular Productos");
        vincularProductosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vincularProductosButtonActionPerformed(evt);
            }
        });

        jLabel36.setText("(PDV) Precio de venta");

        jLabel37.setText("(PCP) Precio de compra a proveedor");

        javax.swing.GroupLayout modalProviderLayout = new javax.swing.GroupLayout(modalProvider);
        modalProvider.setLayout(modalProviderLayout);
        modalProviderLayout.setHorizontalGroup(
            modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProviderLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel36)
                    .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(modalProviderLayout.createSequentialGroup()
                            .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(modalProviderLayout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addGap(46, 46, 46)
                                    .addComponent(providerTaxPayerID, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                                .addGroup(modalProviderLayout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addGap(18, 18, 18)
                                    .addComponent(providerNombre)))
                            .addGap(18, 18, 18)
                            .addComponent(vincularProductosButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        modalProviderLayout.setVerticalGroup(
            modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalProviderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(providerNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addGroup(modalProviderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(providerTaxPayerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(vincularProductosButton)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel37)
                .addContainerGap())
        );

        modalProviderLinkIDLabel.setText("jLabel8");

        providerLinkedProductsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(providerLinkedProductsList);

        productToLinkTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productToLinkTextFieldActionPerformed(evt);
            }
        });

        jButton1.setText("Vincular Seleccionados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Productos vinculables:");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel30.setText("(No se encuentran vinculados al proveedor actual)");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setText("Buscar:");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/trash-can (1).png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout modalProviderLinkProductosLayout = new javax.swing.GroupLayout(modalProviderLinkProductos);
        modalProviderLinkProductos.setLayout(modalProviderLinkProductosLayout);
        modalProviderLinkProductosLayout.setHorizontalGroup(
            modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProviderLinkProductosLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30)
                    .addComponent(jLabel8)
                    .addGroup(modalProviderLinkProductosLayout.createSequentialGroup()
                        .addGroup(modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addComponent(modalProviderLinkIDLabel)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalProviderLinkProductosLayout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(productToLinkTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        modalProviderLinkProductosLayout.setVerticalGroup(
            modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProviderLinkProductosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productToLinkTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(2, 2, 2)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modalProviderLinkProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(modalProviderLinkIDLabel)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        productosPCPTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "CODIGO", "TITULO", "PCP*"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(productosPCPTable);

        jButton4.setText("Confirmar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel32.setText("(*) Precio de compra a proveedor");

        providerIDPCP.setText("    ");

        javax.swing.GroupLayout modalProviderLinkProductosPCPLayout = new javax.swing.GroupLayout(modalProviderLinkProductosPCP);
        modalProviderLinkProductosPCP.setLayout(modalProviderLinkProductosPCPLayout);
        modalProviderLinkProductosPCPLayout.setHorizontalGroup(
            modalProviderLinkProductosPCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProviderLinkProductosPCPLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(modalProviderLinkProductosPCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(modalProviderLinkProductosPCPLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(providerIDPCP))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        modalProviderLinkProductosPCPLayout.setVerticalGroup(
            modalProviderLinkProductosPCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalProviderLinkProductosPCPLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalProviderLinkProductosPCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(providerIDPCP))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        providerIDPCP.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 0, 204));
        jPanel2.setForeground(new java.awt.Color(51, 0, 204));

        menuVentasBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Nventa.png"))); // NOI18N
        menuVentasBtn.setText("Nueva venta");
        menuVentasBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentasBtnActionPerformed(evt);
            }
        });

        menuProductosBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/producto.png"))); // NOI18N
        menuProductosBtn.setText("Productos");
        menuProductosBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosBtnActionPerformed(evt);
            }
        });

        ventasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/compras.png"))); // NOI18N
        ventasButton.setText("Ventas");
        ventasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventasButtonActionPerformed(evt);
            }
        });

        menuProveedoresBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/proveedor.png"))); // NOI18N
        menuProveedoresBtn.setText("Proveedores");
        menuProveedoresBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProveedoresBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuVentasBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuProductosBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ventasButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuProveedoresBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(menuVentasBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ventasButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuProductosBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuProveedoresBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 720));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/encabezado.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 1210, 170));

        principalPanel.setEnabled(false);

        jLabel3.setText("Codigo");

        jLabel5.setText("Cantidad");

        btnEliminarventa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarventa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarventaActionPerformed(evt);
            }
        });

        txtCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProductoActionPerformed(evt);
            }
        });

        TableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(TableVenta);
        if (TableVenta.getColumnModel().getColumnCount() > 0) {
            TableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            TableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/money.png"))); // NOI18N
        jLabel10.setText("Total a pagar: ");

        LabelTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTotal.setText("----");

        btnSaveSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnSaveSale.setEnabled(false);
        btnSaveSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSaleActionPerformed(evt);
            }
        });

        btnAddProductToSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnAddProductToSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductToSaleActionPerformed(evt);
            }
        });

        checkboxAplicaIva.setEnabled(false);
        checkboxAplicaIva.setLabel("Aplica IVA");
        checkboxAplicaIva.setState(true);

        btnClearVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar.png"))); // NOI18N
        btnClearVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar.png"))); // NOI18N
        btnClearVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout nuevaVentaPanelLayout = new javax.swing.GroupLayout(nuevaVentaPanel);
        nuevaVentaPanel.setLayout(nuevaVentaPanelLayout);
        nuevaVentaPanelLayout.setHorizontalGroup(
            nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 992, Short.MAX_VALUE)
                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                                .addComponent(btnSaveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkboxAplicaIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(LabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addComponent(btnAddProductToSale, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarventa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(292, 292, 292)
                                .addComponent(btnClearVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 320, Short.MAX_VALUE)))
                .addContainerGap())
        );
        nuevaVentaPanelLayout.setVerticalGroup(
            nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEliminarventa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddProductToSale, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClearVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(LabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkboxAplicaIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(216, 216, 216))
        );

        principalPanel.addTab("Nueva venta", nuevaVentaPanel);

        TableVentasHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TOTAL", "FECHA", "MONEDA", "REFERENCIA", "LOCALIZACION"
            }
        ));
        TableVentasHistorico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentasHistoricoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableVentasHistorico);
        if (TableVentasHistorico.getColumnModel().getColumnCount() > 0) {
            TableVentasHistorico.getColumnModel().getColumn(0).setPreferredWidth(50);
            TableVentasHistorico.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableVentasHistorico.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableVentasHistorico.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableVentasHistorico.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableVentasHistorico.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        btnMostrarDetalle.setText("Mostrar detalle");
        btnMostrarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDetalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ventasHistPanelLayout = new javax.swing.GroupLayout(ventasHistPanel);
        ventasHistPanel.setLayout(ventasHistPanelLayout);
        ventasHistPanelLayout.setHorizontalGroup(
            ventasHistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasHistPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ventasHistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasHistPanelLayout.createSequentialGroup()
                        .addComponent(btnMostrarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE))
                .addContainerGap())
        );
        ventasHistPanelLayout.setVerticalGroup(
            ventasHistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ventasHistPanelLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(btnMostrarDetalle)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        principalPanel.addTab("Ventas", ventasHistPanel);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Precio de venta*:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Cantidad inicial *:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Titulo*:");

        txtPrecioUni.setName("field4"); // NOI18N
        txtPrecioUni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioUniActionPerformed(evt);
            }
        });
        txtPrecioUni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioUniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioUniKeyTyped(evt);
            }
        });

        txtCantIni.setName("field5"); // NOI18N
        txtCantIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantIniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantIniKeyTyped(evt);
            }
        });

        TableProducto=new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        TableProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PREC UNI", "CANT INIC", "TITULO", "DESCRIPCION", "CODIGO", "PROVEEDOR", "CATEGORIA", "ESTADO"
            }
        ));
        jScrollPane4.setViewportView(TableProducto);
        if (TableProducto.getColumnModel().getColumnCount() > 0) {
            TableProducto.getColumnModel().getColumn(0).setPreferredWidth(50);
            TableProducto.getColumnModel().getColumn(1).setPreferredWidth(100);
            TableProducto.getColumnModel().getColumn(2).setPreferredWidth(40);
            TableProducto.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableProducto.getColumnModel().getColumn(4).setPreferredWidth(60);
            TableProducto.getColumnModel().getColumn(5).setPreferredWidth(100);
            TableProducto.getColumnModel().getColumn(6).setPreferredWidth(60);
            TableProducto.getColumnModel().getColumn(7).setPreferredWidth(60);
        }

        btnSaveProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnSaveProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveProductoActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Descripcion:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Categoria:");

        txtTit.setFocusTraversalPolicy(getFocusTraversalPolicy());
        txtTit.setFocusTraversalPolicyProvider(true);
        txtTit.setName("field1"); // NOI18N
        txtTit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitActionPerformed(evt);
            }
        });
        txtTit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTitKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTitKeyTyped(evt);
            }
        });

        txtDesc.setName("field2"); // NOI18N
        txtDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescKeyTyped(evt);
            }
        });

        txtCod.setFocusTraversalPolicyProvider(true);
        txtCod.setName("field3"); // NOI18N
        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });
        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodKeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setText("Codigo*:");

        cbxCatego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCategoActionPerformed(evt);
            }
        });

        jLabel11.setText("Buscar (Titulo o Codigo)");

        jtxtFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtFiltroActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnEliminarpro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarproActionPerformed(evt);
            }
        });

        btnEditarpro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnEditarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarproActionPerformed(evt);
            }
        });

        productsErrorDisplay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productsErrorDisplay.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productsErrorDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSaveProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTit)
                                    .addComponent(txtDesc)
                                    .addComponent(txtCod)
                                    .addComponent(txtCantIni)
                                    .addComponent(txtPrecioUni)
                                    .addComponent(cbxCatego, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnEditarpro, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarpro)
                                .addGap(81, 81, 81)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpiar))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnEliminarpro)
                                    .addComponent(btnEditarpro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jtxtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLimpiar))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTit)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCantIni)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnSaveProducto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(productsErrorDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        principalPanel.addTab("Productos", jPanel5);

        TableProvider.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(TableProvider);

        javax.swing.GroupLayout proveedoresPanelLayout = new javax.swing.GroupLayout(proveedoresPanel);
        proveedoresPanel.setLayout(proveedoresPanelLayout);
        proveedoresPanelLayout.setHorizontalGroup(
            proveedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proveedoresPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        proveedoresPanelLayout.setVerticalGroup(
            proveedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proveedoresPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );

        principalPanel.addTab("Proveedores", proveedoresPanel);

        getContentPane().add(principalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 1010, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuVentasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasBtnActionPerformed
        // TODO add your handling code here:

        principalPanel.setSelectedIndex(0);
        this.tmpVenta = new Sale();
        LimpiarTable((DefaultTableModel) TableVenta.getModel());
        LabelTotal.setText("----");
    }//GEN-LAST:event_menuVentasBtnActionPerformed

    private void menuProductosBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosBtnActionPerformed
        // TODO add your handling code here:
        productsErrorDisplay.setText("");
        principalPanel.setSelectedIndex(2);
        jtxtFiltro.setText("");
        LimpiarTable((DefaultTableModel) TableProducto.getModel());
        
        // FIXME: Arreglar tabbing entre controles
        FocusTraversalPolicy customPolicy = new FocusTraversalOnArray(
            new Component[]{
                txtTit,
                txtDesc,
                txtCod,
                cbxCatego,
                txtPrecioUni,
                txtCantIni,
                btnSaveProducto,
                btnEditarpro,
                btnEliminarpro,
                jtxtFiltro,
                btnLimpiar
            }
        );
        
        jPanel5.setFocusTraversalPolicy(customPolicy);
        
        cbxCatego.removeAllItems();
        
        for (Category cat: productosService.getProductsCategories()) {
            cbxCatego.addItem(cat.getDescription());
        }
        
        LoadProductos();
    }//GEN-LAST:event_menuProductosBtnActionPerformed

    private void initializeModal() {
        modalFrame.setAlwaysOnTop(true); //Esto nos permite que el jFrame sea un modal
        modalFrame.setLocationRelativeTo(null);
        modalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        modalFrame.setVisible(true);
    }

    private void initializeProvidersProductsModal() {
        modalProviderFrame.setAlwaysOnTop(true); //Esto nos permite que el jFrame sea un modal
        modalProviderFrame.setLocationRelativeTo(null);
        modalProviderFrame.setVisible(true);
    }
    
    private void initializeUpdateModal() {
        modalUpdateFrame.setAlwaysOnTop(true); //Esto nos permite que el jFrame sea un modal
        modalUpdateFrame.setLocationRelativeTo(null);
        modalUpdateFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        modalUpdateFrame.setVisible(true);
    }
     private void initializeVentaDetalleModal() {
        modalVentaFrame.setAlwaysOnTop(true); //Esto nos permite que el jFrame sea un modal
        modalVentaFrame.setLocationRelativeTo(null);
       // modalVentaFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        modalVentaFrame.setVisible(true);
    }
     
    private void initializeModal(JFrame modal) {
        modal.setAlwaysOnTop(true);
        modal.setLocationRelativeTo(null);
        modal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        modal.setVisible(true);
    }


    public void ClearVentaInputs() {
        txtCodigoProducto.setText("");
        txtCantidadVenta.setText("");
        LabelTotal.setText("");
    }

    private void paymentCashMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentCashMontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentCashMontoActionPerformed

    public class Observer {

        private boolean flag = true;

        public void setFlag(boolean newValue) {
            this.flag = newValue;
        }

        public boolean getFlag() {
            return this.flag;
        }
    }

    private void btnCashConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashConfirmActionPerformed
        // TODO add your handling code here:

        // Casteo a string
        paymentCashMonto.setText(this.tmpVenta.getTotal() + "");
        paymentCashPago.setText("");
        paymentCashVuelto.setText("");
        
        try {
            // Esto tarda un poco y es muy poco descriptivo
            // TODO: No se estan persistiendo las ventas si hago una nueva venta con
            // el mismo detalle de productos
            ventasService.add(tmpVenta, "Cash");
        } catch (Exception ex) {

        }
        
        List<Detail> detalle = this.tmpVenta.getDetail();
        
        for (Detail prod:  detalle) {
            Product updateRequest = prod.getProducto();

            // La validacion de la cantidad de los productos se hace al momento de ingresar el
            // codigo y la cantidad del producto

            // updateRequest.getCantidadInicial deberia ser siempre mayor o igual que la cantidad
            // del detalle
            int newQuantity = Math.abs(updateRequest.getInitialQuantity() - prod.getQuantity());
            
            updateRequest.setInitialQuantity(newQuantity);
            try {
                productosService.update(updateRequest);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        
        modalFrame.setVisible(false);
        
        ClearVentaInputs();
        LimpiarTable((DefaultTableModel) TableVenta.getModel());
        
        // Reset tmp venta
        btnSaveSale.setEnabled(false);
        this.tmpVenta = new Sale();
    }//GEN-LAST:event_btnCashConfirmActionPerformed

    private void txtDescActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    // TODO add your handling code here:


    private void cbxProveedorProActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void txtCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCatActionPerformed

    private void txtCantIniActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    // TODO add your handling code here:


    private void txtCodigoProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProActionPerformed

    private void btnAddProductToSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductToSaleActionPerformed

        // Cuando agregamos dos veces el mismo producto deberiamos agrupar las dos entradas
        // en lugar de que aparezcan repetidos
        
        
        int cantidad = 0;
        String codigoBarras = txtCodigoProducto.getText();

        String cantidadVentaValue = txtCantidadVenta
                .getText();
//                .replaceAll("\\D", "");
        
        if (!cantidadVentaValue.equals("")) {
            try{
                cantidad = Integer.parseInt(cantidadVentaValue);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad valida");
                txtCantidadVenta.setText("");
                return;
            }
        } else {
            cantidad = 1;
            txtCantidadVenta.setText("");
        }

        try {
            if (cantidad <= 0) {
                return;
            }
            
            Product prodFound = this.productosService.searchByCodigo(codigoBarras);
            
            if (!prodFound.getEnabled()) {
                JOptionPane.showMessageDialog(null, "El producto que se intento agregar se encuentra deshabilitado.");
                return;
            }
            
            if (prodFound.getInitialQuantity() < cantidad) {
                JOptionPane.showMessageDialog(null, "No se puede agregar el producto, stock insuficiente.");
                return;
            }
            
            Detail detalle = this.ventasService.createDetalle(prodFound, cantidad);

            // Revisamos si hay que juntar el detalle con otro existente
            // ej: agregamos dos productos iguales
            if (!ventasService.updateDetalleIfDuplicated(this.tmpVenta.getDetail(), detalle, tmpVenta)) {
                // Add detail to tmp sale
                this.tmpVenta.addDetail(detalle);
            }
//            this.AddDetalleProducto(detalle);
            LoadDetalle(this.tmpVenta.getDetail());
            LabelTotal.setText(
                String.valueOf(Utils.roundDouble(this.tmpVenta.getTotal()))
            );
            
            btnSaveSale.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());

        } finally {
            txtCodigoProducto.setText("");
            txtCantidadVenta.setText("");
        }
    }//GEN-LAST:event_btnAddProductToSaleActionPerformed

    private void btnSaveSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSaleActionPerformed

        initializeModal();

        // Casteo a string
        paymentCashMonto.setText(this.tmpVenta.getTotal() + "");

        // Inicializamos el modal
//        modalFrame.getContentPane().add(PaymentMethodPanel);
//        modalFrame.pack();
//        modalFrame.setVisible(true);
        initializeModalWithPanel(modalFrame, PaymentMethodPanel, true);
        modalFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                paymentCashMonto.setText("0.0");
                paymentCashPago.setText("");
                paymentCashVuelto.setText("");
            }
        });

        // Esto se ejecuta cada vez que se ingresa informacion en el textbox
        paymentCashPago.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (paymentCashPago.getText().equals("")) {
                    return;
                }

                paymentCashErrorLabel.setText("");
                paymentCashPago.setBorder(new LineBorder(Color.GRAY, 1));

                String filteredPaymentCashValue = paymentCashPago.getText().replaceAll("\\D", "");
                
                if (filteredPaymentCashValue.equals("")){
                    paymentCashErrorLabel.setForeground(Color.RED);
                    paymentCashErrorLabel.setText("Debe ingresar un valor numerico valido.");
                    paymentCashPago.setBorder(new LineBorder(Color.RED, 2));
                    return;
                }
                
                Float pago = Float.parseFloat(
                    paymentCashPago.getText().replaceAll("\\D", "")
                );

                if (pago < tmpVenta.getTotal()) {
                    paymentCashErrorLabel.setForeground(Color.RED);
                    paymentCashErrorLabel.setText("El pago es menor al monto a pagar.");
                    paymentCashPago.setBorder(new LineBorder(Color.RED, 2));
                    return;
                }

                double diff = pago - tmpVenta.getTotal();

                if (diff < 0) {
                    paymentCashPago.setBorder(new LineBorder(Color.RED, 2));
                    return;
                }

                paymentCashVuelto.setText(diff + "");
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }//GEN-LAST:event_btnSaveSaleActionPerformed

    private void btnEliminarventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarventaActionPerformed

        String codigoBarras = txtCodigoProducto.getText();

        if (codigoBarras.equals("")) {
            return;
        }

        int cantidad;

        if (!txtCantidadVenta.getText().equals("")) {
            cantidad = Integer.parseInt(txtCantidadVenta.getText());
        } else {
            cantidad = 1;
        }

        for (int i = 0; i < cantidad; i++) {
            this.tmpVenta = this.ventasService.removeProductoFromDetalle(this.tmpVenta, codigoBarras);
        }

        //        AddDetalleProducto(this.tmpVenta.getDetalle());
        this.LoadDetalle(this.tmpVenta.getDetail());

        // Update amount
        String valorVenta = String.valueOf(this.tmpVenta.getTotal());
        if (valorVenta.equals("0")) {
            LabelTotal.setText("----");
        } else {
            LabelTotal.setText(valorVenta);
        }

        txtCodigoProducto.setText("");
        txtCantidadVenta.setText("");
        
        if (this.tmpVenta.getDetail().isEmpty()) btnSaveSale.setEnabled(false);
        
        
    }//GEN-LAST:event_btnEliminarventaActionPerformed

    private void ventasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventasButtonActionPerformed
        principalPanel.setSelectedIndex(1);
        LimpiarTable((DefaultTableModel) TableVentasHistorico.getModel());
        LoadVentasHistorico();
    }//GEN-LAST:event_ventasButtonActionPerformed

    private void TableVentasHistoricoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasHistoricoMouseClicked
        // TODO add your handling code here:
        Long saleId = (Long)TableVentasHistorico.getValueAt(TableVentasHistorico.getSelectedRow(), 0);
        
        try {
            Sale sale = ventasService.fetch(saleId);
            
            initializeVentaDetalleModal();
            modalVentaFrame.getContentPane().add(modalDetalle);
            modalVentaFrame.pack();
            modalVentaFrame.setVisible(true);

            ventaDetalleID.setText(sale.getId() + "");
            ventaDetalleTotal.setText(sale.getTotal() + "");
            ventaDetalleFactura.setText(sale.getInvoiceNumber());

            // Crear un objeto Date usando el timestamp
            Date date = new Date(sale.getTimestamp());

            // Crear un formato de fecha DD/MM/YYYY
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            // Convertir la fecha a formato deseado
            String formattedDate = dateFormat.format(date);
            
            ventaDetalleFecha.setText(formattedDate);
            ventaDetalleMoneda.setText(sale.getCurrency());

            List<Detail> saleDetail = ventasService.getDetailFromSale(saleId);
            
            LoadVentaDetalle(saleDetail);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
    }//GEN-LAST:event_TableVentasHistoricoMouseClicked

    public void ClearTableVenta() {
        DefaultTableModel dtm = (DefaultTableModel) TableVenta.getModel();
        dtm.setRowCount(0);
    }


    private void txtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProductoActionPerformed

    private void paymentCashPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentCashPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentCashPagoActionPerformed

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarActionPerformed
       //esto se lo agregue porque si quiero cancelar queda pintado de rojo entonces esto lo deja en gris
        PrecioUni.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        CantInici.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        Titulo.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        Desc.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        modalUpdateFrame.setVisible(false);
    }//GEN-LAST:event_BtnCancelarActionPerformed

    //BOTON ACEPTAR
    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        
        // Eliminar el borde rojo de todos los campos de entrada
      
        PrecioUni.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        CantInici.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        Titulo.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        Desc.setBorder(new LineBorder(new Color(245, 245, 245), 2));
        String productoCodigo = jLabelCod.getText();
        
//        int productId = Integer.parseInt(labelUpdateID.getText());
        Long productId = Long.parseLong(labelUpdateID.getText());
        
        Product prodToUpdate = null;
        
        try {
            prodToUpdate = this.productosService.fetch(productId);
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(modalUpdateFrame, ex.toString());
            modalUpdateFrame.setVisible(false);
            return;
        }
        
        float nuevoPrecioUni = 0;

        try {
            nuevoPrecioUni = Float.parseFloat(PrecioUni.getText());
            prodToUpdate.setUnitaryPrice(nuevoPrecioUni);
            
        } catch (Exception ex) {  
           
            JOptionPane.showMessageDialog(modalUpdateFrame, "Por favor, ingrese un precio válido.");
//            PrecioUni.setBorder(new LineBorder(Color.RED, 2));
            PrecioUni.setText(prodToUpdate.getPublicSalePrice() + "");
            return;
            
        }
                 
        // Validar el Nueva cantidad inicial
        int nuevaCantIni = 0;

        try {
         
            nuevaCantIni = Integer.parseInt(CantInici.getText());
            prodToUpdate.setInitialQuantity(nuevaCantIni);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modalUpdateFrame, "Por favor, ingrese una cantidad inicial valida.");
//            CantInici.setBorder(new LineBorder(Color.RED, 2));
            CantInici.setText(prodToUpdate.getInitialQuantity() + "");
            return;
        }

        String nuevoTitulo = Titulo.getText().trim();
        
        //Validar Titulo:
        if (nuevoTitulo.isEmpty()) {
            JOptionPane.showMessageDialog(modalUpdateFrame, "Por favor ingresa un Título válido.");
//            Titulo.setBorder(new LineBorder(Color.RED, 2));
            Titulo.setText(prodToUpdate.getTitle());
            return; // Salir del método o tomar otras acciones según sea necesario
        }
        
        prodToUpdate.setTitle(nuevoTitulo);

        String nuevaDesc = Desc.getText();
        String nuevaCategoria = jComboCat.getSelectedItem().toString();

        prodToUpdate.setDescription(nuevaDesc);
//        prodToUpdate.setProvider(nuevoProveedor);
        prodToUpdate.setCategory(nuevaCategoria);
        prodToUpdate.setEnabled(btnUpdateEnabled.isSelected());
        
        try {
            this.productosService.update(prodToUpdate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modalUpdateFrame, ex.toString());
        }

        modalUpdateFrame.setVisible(false); // Se oculta si al Aceptar esta todo
        
        LimpiarTable((DefaultTableModel) TableProducto.getModel());
        LoadProductos();
     
//FIN ACEPTAR
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void DescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescKeyReleased
        /* if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1 ){
            btnAceptar.setEnabled(true);

        }else{
            btnAceptar.setEnabled(false);
        }     */

    }//GEN-LAST:event_DescKeyReleased

    private void DescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescKeyTyped

        // ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_DescKeyTyped

    private void TituloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TituloKeyReleased

    }//GEN-LAST:event_TituloKeyReleased

    private void TituloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TituloKeyTyped

        //  ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_TituloKeyTyped

    private void CantIniciKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CantIniciKeyPressed

    private void CantIniciKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyReleased
        //Valida el campo obligatorio Cantidad
        /*  obligCantI=ProductosValidacion.camposObligTxt(CantInici,jLabel3, btnAceptar, contadorP);
        if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1 ){
            btnAceptar.setEnabled(true);

        }else{
            btnAceptar.setEnabled(false);
        }*/

    }//GEN-LAST:event_CantIniciKeyReleased

    private void CantIniciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantIniciKeyTyped

        // ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_CantIniciKeyTyped

    private void PrecioUniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecioUniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrecioUniActionPerformed

    private void PrecioUniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrecioUniKeyReleased

        /*  obligPrec=ProductosValidacion.camposObligTxt(PrecioUni,jLabel2, btnAceptar, contadorP);
        if(obligPrec==1 && obligTit==1 && obligCantI==1 && obligCod==1){
            btnAceptar.setEnabled(true);

        }else{
            btnAceptar.setEnabled(false);
        }*/

    }//GEN-LAST:event_PrecioUniKeyReleased

    private void PrecioUniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrecioUniKeyTyped

        //  ProductosValidacion.numberDecimalKeyPress(evt, PrecioUni);

    }//GEN-LAST:event_PrecioUniKeyTyped

    private void btnClearVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearVentaActionPerformed
        // TODO add your handling code here:
        LimpiarTable((DefaultTableModel) TableVenta.getModel());
        ClearVentaInputs();
        this.tmpVenta = new Sale();
        
        btnSaveSale.setEnabled(false);
        
    }//GEN-LAST:event_btnClearVentaActionPerformed

    private void btnMostrarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDetalleActionPerformed
        // TODO add your handling code here
        if (TableVentasHistorico.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione solo una fila para mostrar.");
            return;
        }

        // Obtener fila seleccionada
        int filaSeleccionada = TableVentasHistorico.getSelectedRow();

        // Verificar si hay una fila seleccionada
        if (filaSeleccionada != -1) {

            // Obtener los valores de la fila seleccionada
            String VentaID = TableVentasHistorico.getValueAt(filaSeleccionada, 0).toString();
            
            Long castID = Long.parseLong(VentaID);
            
            Sale ventaEncontrada = null;
            
            try {
                ventaEncontrada = ventasService.fetch(castID);
                
                initializeVentaDetalleModal();
                modalVentaFrame.getContentPane().add(modalDetalle);
                modalVentaFrame.pack();
                modalVentaFrame.setVisible(true);
                
                ventaDetalleID.setText(ventaEncontrada.getId() + "");
                ventaDetalleTotal.setText(ventaEncontrada.getTotal() + "");
                
                LoadVentaDetalle(ventaEncontrada.getDetail());
                
            } catch(Exception ex) {
                
            }
         /*  
            // ACA
            initializeUpdateModal();

            // Inicializamos el modal
            modalUpdateFrame.getContentPane().add(modalUpdate);
            modalUpdateFrame.pack();
            modalUpdateFrame.setVisible(true);

            // Crear una instancia de Detalle
//            Detalle detalle = new Detalle(this, true);
            // Establecer los valores obtenidos en los campos del formulario Detalle
            PrecioUni.setText(precioUni);
            CantInici.setText(cantIni);
            Titulo.setText(titulo);
            Desc.setText(desc);
            jLabelCod.setText(codi);
            jComboProv.setSelectedItem(proveedor);
            jComboCat.setSelectedItem(categoria);

            // Mostrar la ventana Detalle
//            detalle.setVisible(true);
            bandera = 1;
            // Cuando la ventana Detalle se cierra, obtener los nuevos valores ingresados
            // y actualizar la fila correspondiente en la tabla TableProducto */
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para mostrar.");
        }

        
    }//GEN-LAST:event_btnMostrarDetalleActionPerformed

    private void ventaDetalleIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventaDetalleIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventaDetalleIDActionPerformed

    private void btnUpdateEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEnabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEnabledActionPerformed

    private void btnUpdateEnabled1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEnabled1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEnabled1ActionPerformed

    private void btnEditarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarproActionPerformed

        if (TableProducto.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione solo una fila para editar.");
            return;
        }
        
        LimpiarTable((DefaultTableModel) productProveedoresTable.getModel());

        // Obtener fila seleccionada
        int filaSeleccionada = TableProducto.getSelectedRow();

        // Verificar si hay una fila seleccionada
        if (filaSeleccionada == -1) {
            return;
        }

        // Obtener los valores de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) TableProducto.getModel();

        String precioVenta = TableProducto.getValueAt(filaSeleccionada, model.findColumn("PRECIO DE VENTA")).toString();
        String cantIni = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CANTIDAD INICIAL")).toString();
        String titulo = TableProducto.getValueAt(filaSeleccionada, model.findColumn("TITULO")).toString();

        Object descValue = TableProducto.getValueAt(filaSeleccionada, model.findColumn("DESCRIPCION"));

        if (descValue == null) descValue = "";
        String desc = descValue.toString();

        String codi = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CODIGO")).toString();
        String proveedor = "";//TableProducto.getValueAt(filaSeleccionada, model.findColumn("PROVEEDOR")).toString();
        String categoria = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CATEGORIA")).toString();
        String ID = TableProducto.getValueAt(filaSeleccionada, model.findColumn("ID")).toString();

        String enabledStatus = TableProducto.getValueAt(filaSeleccionada, model.findColumn("ESTADO")).toString();
        initializeUpdateModal();

        // Crear una instancia de Detalle
        //            Detalle detalle = new Detalle(this, true);
        // Establecer los valores obtenidos en los campos del formulario Detalle
        PrecioUni.setText(precioVenta);
        CantInici.setText(cantIni);
        Titulo.setText(titulo);
        Desc.setText(desc);
        jLabelCod.setText(codi);
        //jComboProv.setSelectedItem(proveedor);
        // TODO: REVISAR CATEGORIAS
        jComboCat.setSelectedItem(categoria);
        labelUpdateID.setText(ID);

        labelUpdateID.setVisible(false);

        if (enabledStatus.equals("Habilitado")) {
            btnUpdateEnabled.doClick();
        } else {
            btnUpdateEnabled1.doClick();
        }

        // Mostrar la ventana Detalle
        //            detalle.setVisible(true);
        bandera = 1;
        // Cuando la ventana Detalle se cierra, obtener los nuevos valores ingresados
        // y actualizar la fila correspondiente en la tabla TableProducto
        
        DefaultTableModel modelProveedores = (DefaultTableModel) productProveedoresTable.getModel();
        
        List<PrecioProveedorProducto> providersProducts = this.productosService.getProvidersByProduct(Integer.parseInt(ID));
        for (PrecioProveedorProducto ppp: providersProducts) {
            modelProveedores.addRow(
                new Object[]{
                    ppp.getPrecioCompra(),
                    ppp.getProveedor().getName(),
                    ppp.getProveedor().getTaxPayerId()
                }
            );
        }
        
        productProveedoresTable.setModel(modelProveedores);
        
        // Inicializamos el modal
        modalUpdateFrame.getContentPane().add(modalUpdate);
        modalUpdateFrame.pack();
        modalUpdateFrame.setVisible(true);
        
    }//GEN-LAST:event_btnEditarproActionPerformed

    private void btnEliminarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarproActionPerformed
        // TODO add your handling code here:
        if (TableProducto.getSelectedRowCount() < 1) {
            return;
        }
        
        int[] rows = TableProducto.getSelectedRows();
        
        DefaultTableModel model = (DefaultTableModel) TableProducto.getModel();
        
        for(int i = 0; i < rows.length; i++){
            int productID = Integer.parseInt(TableProducto.getValueAt(rows[i], model.findColumn("ID")).toString());
            
            try {
                
                productosService.setProductEnabled(productID, false);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.toString());
            }
            
        }
        
        LimpiarTable(model);
        LoadProductos();
    }//GEN-LAST:event_btnEliminarproActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        jtxtFiltro.setText("");
        LimpiarTable((DefaultTableModel) TableProducto.getModel());
        LoadProductos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void jtxtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFiltroActionPerformed

    private void cbxCategoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCategoActionPerformed

    private void txtCodKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyTyped
        // TODO add your handling code here:

        //ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodKeyTyped

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        // Valida el campo obligatorio Codigo de barra
//        obligCod = ProductosValidacion.camposObligTxt(txtCod, jLabel35, btnSaveProducto, contadorP);
//        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
//            btnSaveProducto.setEnabled(true);
//        } else {
//            btnSaveProducto.setEnabled(false);
//        }
    }//GEN-LAST:event_txtCodKeyReleased

    private void txtCodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodKeyPressed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyTyped
        // TODO add your handling code here:
//        ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_txtDescKeyTyped

    private void txtDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyReleased
//        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
//            btnSaveProducto.setEnabled(true);
//        } else {
//            btnSaveProducto.setEnabled(false);
//        }
    }//GEN-LAST:event_txtDescKeyReleased

    private void txtTitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyTyped
        // TODO add your handling code here:
//        ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_txtTitKeyTyped

    private void txtTitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyReleased
        // Valida el campo obligatorio Titulo

//        obligTit = ProductosValidacion.camposObligTxt(txtTit, jLabel25, btnSaveProducto, contadorP);
//        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
//            btnSaveProducto.setEnabled(true);
//        } else {
//            btnSaveProducto.setEnabled(false);
//        }
    }//GEN-LAST:event_txtTitKeyReleased

    private void txtTitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitActionPerformed

    private List<javax.swing.JTextField> findComponentsWithErrors(JPanel parent) {
        
        List<javax.swing.JTextField> componentsFound = new ArrayList();
        
        for (Component comp : parent.getComponents()) {

            if (comp instanceof javax.swing.JTextField && ((javax.swing.JTextField) comp).getBorder() == ERROR_BORDER){
                  // Grosor 2 píxeles
                componentsFound.add((javax.swing.JTextField) comp);
            }

        }
        
        return componentsFound;
    }
    
    
    private void markFieldAsError(javax.swing.JTextField field) {
        field.setBorder(ERROR_BORDER);
    }
    
    
    private void btnSaveProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveProductoActionPerformed
        // JENI
        List<javax.swing.JTextField> componentsToNormalize = findComponentsWithErrors(jPanel5);
        
        for (javax.swing.JTextField component: componentsToNormalize) {
            component.setBorder(DEFAULT_BORDER);
        }
        try {
            String codigo = txtCod.getText();
            boolean castEstado = true;

            NewProductForm form = new NewProductForm(
                txtTit.getText(),
                txtDesc.getText(),
                txtCod.getText(),
                cbxCatego.getSelectedItem().toString(),
                txtPrecioUni.getText(),
                txtCantIni.getText()
            );

            List<FormError> errors = form.validateForm();
            
            if (errors.size() > 0) {
                //                productsErrorDisplay
                StringBuilder sb = new StringBuilder("<html>");
                for (FormError error: errors) {
                    sb.append(error.error_description + "<br>");

                    for (Component comp : jPanel5.getComponents()) {

                        if (error.control_name.equals(comp.getName())){
                              // Grosor 2 píxeles

                            ((javax.swing.JTextField) comp).setBorder(ERROR_BORDER);
                            componentsToNormalize.add((javax.swing.JTextField) comp);
                        }

                    }

                }
                productsErrorDisplay.setText(sb.toString() + "</html>");
                return;
            } else {
                
                for (javax.swing.JTextField component: componentsToNormalize) {
                    component.setBorder(DEFAULT_BORDER);
                }
            
            }

            try {

                productosService.searchByCodigo(form.validatedForm.code);
                productsErrorDisplay.setText("<html>El producto con el codigo " + codigo + " ya existe</html>");
                txtCod.setBorder(ERROR_BORDER);
                return;
            } catch (Exception ex) {
                Product nuevoProducto = new Product(
                    0,
                    form.validatedForm.unitary_price,
                    form.validatedForm.initial_quantity,
                    form.validatedForm.title,
                    form.validatedForm.description,
                    form.validatedForm.code,
                    form.validatedForm.categoria,
                    castEstado
                );
                this.productosService.add(nuevoProducto);
                LimpiarTable((DefaultTableModel) TableProducto.getModel());
                LoadProductos();
                
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        for (javax.swing.JTextField component: componentsToNormalize) {
            component.setBorder(DEFAULT_BORDER);
        }
        
        Limpiarproducto();
        
    }//GEN-LAST:event_btnSaveProductoActionPerformed

    private void txtCantIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantIniKeyTyped
        // TODO add your handling code here:
//        ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantIniKeyTyped

    private void txtCantIniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantIniKeyReleased
        // Valida el campo obligatorio Cantidad
//        obligCantI = ProductosValidacion.camposObligTxt(txtCantIni, jLabel24, btnSaveProducto, contadorP);
//        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
//            btnSaveProducto.setEnabled(true);
//        } else {
//            btnSaveProducto.setEnabled(false);
//        }
    }//GEN-LAST:event_txtCantIniKeyReleased

    private void txtPrecioUniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUniKeyTyped
        // TODO add your handling code here:
//        ProductosValidacion.numberDecimalKeyPress(evt, txtPrecioUni);
    }//GEN-LAST:event_txtPrecioUniKeyTyped

    private void txtPrecioUniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUniKeyReleased
        // Valida el campo obligatorio Precio

//        obligPrec = ProductosValidacion.camposObligTxt(txtPrecioUni, jLabel23, btnSaveProducto, contadorP);
//
//        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
//            btnSaveProducto.setEnabled(true);
//        } else {
//            btnSaveProducto.setEnabled(false);
//        }
    }//GEN-LAST:event_txtPrecioUniKeyReleased

    private void txtPrecioUniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioUniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioUniActionPerformed

    private void menuProveedoresBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProveedoresBtnActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        principalPanel.setSelectedIndex(3);
        LimpiarTable((DefaultTableModel) TableProvider.getModel());
        
        LoadProviders();
    }//GEN-LAST:event_menuProveedoresBtnActionPerformed

    private void providerNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_providerNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_providerNombreActionPerformed

    private void providerTaxPayerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_providerTaxPayerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_providerTaxPayerIDActionPerformed

    private void LoadProductsToJList(JList list, List<Product> productsToAdd) {
//        DefaultListModel<String> lModel = (DefaultListModel) list.getModel();
//        lModel.clear();
        
        DefaultListModel<String> newListModel = new DefaultListModel();
        
        productsToAdd.forEach(p -> {
                // mostrar tambien el id del producto
                String strItem = "<html><p style=\"font:bold\">%s</p>Codigo: %s</span></html>";
                newListModel.addElement(String.format(strItem, p.getTitle(), p.getCode()));
            }
        );
        
        list.setModel(newListModel);
    }
    
    private void vincularProductosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vincularProductosButtonActionPerformed
        // TODO add your handling code here:
        
        String providerID = modalProviderLinkIDLabel.getText().split(" ")[0];
        String taxPayerID = modalProviderLinkIDLabel.getText().split(" ")[1];
        
        try{
            Provider providerFound = this.productosService.getProviderByTaxPayerID(taxPayerID);
            
            if (providerFound == null) {
                JOptionPane.showMessageDialog(null, "Proveedor nulo");
                return;
            }
            
            List<Product> productsNotLinkedWithProvider = 
                    this.productosService.getProductsNotLinkedToProvider(taxPayerID);
            
            
            LoadProductsToJList(providerLinkedProductsList, productsNotLinkedWithProvider);
            
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        // Mostrar modal para linkear productos
//        modalProviderLinkProductsFrame.getContentPane().add(modalProviderLinkProductos);
//        modalProviderLinkProductsFrame.pack();
//        initializeModal(modalProviderLinkProductsFrame);
        
        initializeModalWithPanel(modalProviderLinkProductsFrame, modalProviderLinkProductos, true);
        
    }//GEN-LAST:event_vincularProductosButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        modalProviderLinkProductsFrame.setVisible(false);

        int providerID = Integer.parseInt(modalProviderLinkIDLabel.getText());
        LoadProductsForProviderTable(providerID);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void initializeModalWithPanel(JFrame frame, JPanel panel, boolean allowClose) {
        frame.getContentPane().add(panel);
        frame.pack();
        initializeModal(frame);
        
        if (allowClose) frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    
    private void LoadProductsForProviderTable(int providerID) {
        List<PrecioProveedorProducto> productos = this.productosService.getProductsByProvider(providerID);
        LimpiarTable((DefaultTableModel) TableProvidersProducts.getModel());
        List<Object[]> objToAdd = new ArrayList();

        productos.forEach(prod -> objToAdd.add(
            new Object[]{
                prod.getProducto().getId(),
                prod.getProducto().getPublicSalePrice(),
                prod.getPrecioCompra(),
                prod.getProducto().getTitle(),
                prod.getProducto().getCode()
            }
        ));

        DefaultTableModel modeloPP = (DefaultTableModel) TableProvidersProducts.getModel();

        objToAdd.forEach(obj -> modeloPP.addRow(obj));

        TableProvidersProducts.setModel(modeloPP);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        // TODO: Hacer reload de la tabla de productos en el proveedor

        String taxPayerID = modalProviderLinkIDLabel.getText().split(" ")[1];
        int providerID = Integer.parseInt(modalProviderLinkIDLabel.getText().split(" ")[0]);
        
//        Provider providerFound = this.productosService.getProviderByTaxPayerID(taxPayerID);
        
        List<String> selectedProducts = providerLinkedProductsList.getSelectedValuesList();

        if (selectedProducts.size() == 0) {
            JOptionPane.showMessageDialog(modalProviderLinkProductos, "No hay ningun producto seleccionado");
            return;
        }
        
        // ACA CONFIRMAR LOS PCP PARA CADA PROD
        initializeModalWithPanel(modalProviderLinkProductsFramePCP, modalProviderLinkProductosPCP, true);
//        productosPCPTable
        
        List<Object[]> pcpProducts = new ArrayList();
        
        for (String selectedProduct: selectedProducts) {
            String cleanedProductCode = selectedProduct
                    .replaceAll("<[^>]*>", "") // remove html tags
                    .replaceAll(".*Codigo:", "")
                    .strip();
            String cleanedProductTitle = selectedProduct
                    .replaceAll("<[^>]*>", "") // remove html tags
                    .replaceAll("\n*Codigo:.*", "")
                    .strip();
            
            pcpProducts.add(new Object[] {
                cleanedProductCode,
                cleanedProductTitle,
                ""
            });
        }

        
        DefaultTableModel modeloPCP = (DefaultTableModel) productosPCPTable.getModel();
        LimpiarTable((DefaultTableModel) modeloPCP);
        pcpProducts.forEach(obj -> modeloPCP.addRow(obj));
        productosPCPTable.setModel(modeloPCP);
        
        providerIDPCP.setText(providerID + "");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void productToLinkTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productToLinkTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productToLinkTextFieldActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        providerLinkedProductsList.clearSelection();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        productosPCPTable.getCellEditor().stopCellEditing();
//        TableColumn codigoColumn = productosPCPTable..getColumn("CODIGO");
//        TableColumn pcpColumn = productosPCPTable.getColumn("PCP*");
        int providerID = Integer.parseInt(providerIDPCP.getText());

        int rows = productosPCPTable.getRowCount();
        // Funciona porque cada vez que se apreta el boton se instancia otro TableErrorRenderer
        TableErrorRenderer errorRenderer = new TableErrorRenderer(rows);
        
        boolean errorFlag = false;
        
        productosPCPTable.getColumnModel()
                .getColumn(2)
                .setCellRenderer(errorRenderer);

        List<Pair<String, Double>> toPersist = new ArrayList();
        
        // Check all PCPs
        for (int i = 0; i<rows; i++){
            String value = productosPCPTable.getValueAt(i, 2).toString();
            try {
                double parsedPrice = Double.parseDouble(value);
                toPersist.add(
                    new Pair(productosPCPTable.getValueAt(i, 0).toString(), parsedPrice)
                );
                
                System.out.println(parsedPrice);
            } catch (Exception ex) {
                errorRenderer.addErrorToRow(i);
                errorFlag = true;
            }
        }
        
        // TODO: si no hubo ningun error entonces persistir precios
        
        if (!errorFlag) {
            
            // Para permitir modificar la lista en pleno bucle
            int itemsOk = 0;
            for (int i = 0;i < toPersist.size();i++){
                
                Pair<String, Double> item = toPersist.get(i);
                
                try {
                    
                    Product productFound = this.productosService.searchByCodigo(item.a);
                    Provider providerFound = this.productosService.getProviderByID(providerID);

                    PrecioProveedorProducto pppFound = this.productosService.getPrecioProveedorProducto(providerID, productFound.getId());

                    if (pppFound == null) {

                        this.productosService.addPrecioProveedorProducto(
                            new PrecioProveedorProducto.PrecioProveedorProductoId(productFound.getId(), providerFound.getId()),
                            item.b
                        );

                        JOptionPane.showMessageDialog(
                            modalProviderLinkProductos,
                            "Producto " +
                            item.a +
                            " vinculado al proveedor " +
                            providerFound.getTaxPayerId()
                        );

                        itemsOk += 1;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
            
            if (toPersist.size() == itemsOk){
                modalProviderLinkProductsFramePCP.setVisible(false);
                modalProviderLinkProductsFrame.setVisible(false);
                LoadProductsForProviderTable(providerID);
                return;
            } else {
                // Handle error
            }
                
        } else {
        
            if (productosPCPTable.getCellEditor() != null)
                productosPCPTable.getCellEditor().stopCellEditing();

            productosPCPTable.clearSelection();
            productosPCPTable.repaint();
        
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void paymentCashVueltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentCashVueltoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentCashVueltoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCancelar;
    public javax.swing.JTextField CantInici;
    public javax.swing.JTextField Desc;
    private javax.swing.JLabel LabelTotal;
    private javax.swing.JPanel PaymentMethodPanel;
    public javax.swing.JTextField PrecioUni;
    private javax.swing.JTable TableProducto;
    private javax.swing.JTable TableProvider;
    private javax.swing.JTable TableProvidersProducts;
    private javax.swing.JTable TableVenta;
    private javax.swing.JTable TableVentaDetalle;
    private javax.swing.JTable TableVentasHistorico;
    public javax.swing.JTextField Titulo;
    public javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAddProductToSale;
    private java.awt.Button btnCashConfirm;
    private javax.swing.JButton btnClearVenta;
    private javax.swing.JButton btnEditarpro;
    private javax.swing.JButton btnEliminarpro;
    private javax.swing.JButton btnEliminarventa;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMostrarDetalle;
    private javax.swing.JButton btnSaveProducto;
    private javax.swing.JButton btnSaveSale;
    private javax.swing.JRadioButton btnUpdateEnabled;
    private javax.swing.JRadioButton btnUpdateEnabled1;
    private javax.swing.JComboBox<String> cbxCatego;
    private java.awt.Checkbox checkboxAplicaIva;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    public javax.swing.JComboBox<String> jComboCat;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel13;
    public static javax.swing.JLabel jLabel14;
    public static javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JLabel jLabelCod;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jtxtFiltro;
    private javax.swing.JLabel labelUpdateID;
    private java.awt.List list1;
    private javax.swing.JButton menuProductosBtn;
    private javax.swing.JButton menuProveedoresBtn;
    private javax.swing.JButton menuVentasBtn;
    private javax.swing.JPanel modalDetalle;
    private javax.swing.JPanel modalProgressBar;
    private javax.swing.JPanel modalProvider;
    private javax.swing.JLabel modalProviderLinkIDLabel;
    private javax.swing.JPanel modalProviderLinkProductos;
    private javax.swing.JPanel modalProviderLinkProductosPCP;
    private javax.swing.JPanel modalUpdate;
    private javax.swing.JPanel nuevaVentaPanel;
    private javax.swing.JLabel paymentCashErrorLabel;
    private javax.swing.JTextField paymentCashMonto;
    private javax.swing.JTextField paymentCashPago;
    private javax.swing.JTextField paymentCashVuelto;
    private javax.swing.JTabbedPane principalPanel;
    private javax.swing.JTable productProveedoresTable;
    private javax.swing.JTextField productToLinkTextField;
    private javax.swing.JTable productosPCPTable;
    private javax.swing.JLabel productsErrorDisplay;
    private javax.swing.JPanel proveedoresPanel;
    private javax.swing.JLabel providerIDPCP;
    private javax.swing.JList<String> providerLinkedProductsList;
    private javax.swing.JTextField providerNombre;
    private javax.swing.JTextField providerTaxPayerID;
    private javax.swing.JTextField txtCantIni;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtPrecioUni;
    private javax.swing.JTextField txtTit;
    private javax.swing.ButtonGroup updateEnabledGroup;
    private javax.swing.JTextField ventaDetalleFactura;
    private javax.swing.JTextField ventaDetalleFecha;
    private javax.swing.JTextField ventaDetalleID;
    private javax.swing.JTextField ventaDetalleMoneda;
    private javax.swing.JTextField ventaDetalleTotal;
    private javax.swing.JButton ventasButton;
    private javax.swing.JPanel ventasHistPanel;
    private javax.swing.JButton vincularProductosButton;
    // End of variables declaration//GEN-END:variables

    private void Limpiarproducto() {//limpia los campos
        // txtCodigoPro.setText("");
        txtPrecioUni.setText("");
        txtCantIni.setText("");
        txtTit.setText("");
        txtDesc.setText("");
        txtCod.setText("");
        cbxCatego.setSelectedIndex(0);
        productsErrorDisplay.setText("");

    }
}
