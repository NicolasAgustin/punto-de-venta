/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Exceptions.StoreException;
import Exceptions.ValidationException;
import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.MockClienteDAO;
import Persistence.HibernateUtil;
import ProductosService.Category;


import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import VentasService.VentasService;

import ProductosService.Product;
import ProductosService.ProductsService;
import ProductosService.ProductosValidacion;
import VentasService.Detail;
//import VentasService.ProductoDetalle;
import VentasService.Sale;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.InputMismatchException;
import java.util.Set;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.RowFilter;
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
    JDialog modalDialog;
    Sale tmpVenta = new Sale();

    //Boton eliminar:
    JButton botonEliminar = new JButton("btnEliminarpro");

    //Para el boton buscar:   
    TableRowSorter trs;

    public Sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
        btnSaveProducto.setEnabled(false);//Este boton al proncipio esta deshabilitado

        updateEnabledGroup.add(btnUpdateEnabled);
        updateEnabledGroup.add(btnUpdateEnabled1);
        
        // Remove the JTabbedPane header
        principalPanel.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex){}
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
        });

        LoadProductos();

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
        jComboProv = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
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
        modalDetalle = new javax.swing.JPanel();
        jId = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTotal = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableVentaDetalle = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        updateEnabledGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        menuVentasBtn = new javax.swing.JButton();
        menuProductosBtn = new javax.swing.JButton();
        ventasButton = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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
        adminPanel = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtPrecioUni = new javax.swing.JTextField();
        txtCantIni = new javax.swing.JTextField();
        cbxProveedorPro = new javax.swing.JComboBox<>();
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

        jComboProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Proveedor uno", "Proveedor dos", "Proveedor tres" }));

        jLabel8.setText("Proveedor:");

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

        jLabel16.setText("Precio unitario:");

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
                    .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboProv, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelUpdateID)
                                .addGroup(modalUpdateLayout.createSequentialGroup()
                                    .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(PrecioUni, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                        .addComponent(CantInici)
                                        .addComponent(Titulo)
                                        .addComponent(Desc)
                                        .addComponent(jLabelCod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGroup(modalUpdateLayout.createSequentialGroup()
                            .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnUpdateEnabled))
                            .addGap(18, 18, 18)
                            .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(modalUpdateLayout.createSequentialGroup()
                                    .addComponent(btnUpdateEnabled1)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jComboCat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        modalUpdateLayout.setVerticalGroup(
            modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalUpdateLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelUpdateID)
                .addGap(18, 18, 18)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(PrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    .addComponent(jLabel8)
                    .addComponent(jComboProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateEnabled)
                    .addComponent(btnUpdateEnabled1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(modalUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnCancelar)
                    .addComponent(btnAceptar))
                .addGap(27, 27, 27))
        );

        jId.setEditable(false);
        jId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIdActionPerformed(evt);
            }
        });

        jLabel17.setText("ID");

        jLabel19.setText("Total");

        jTotal.setEditable(false);

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

        javax.swing.GroupLayout modalDetalleLayout = new javax.swing.GroupLayout(modalDetalle);
        modalDetalle.setLayout(modalDetalleLayout);
        modalDetalleLayout.setHorizontalGroup(
            modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalDetalleLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addComponent(jLabel20))
                .addGap(37, 37, 37)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jId)
                        .addComponent(jTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 892, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        modalDetalleLayout.setVerticalGroup(
            modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalDetalleLayout.createSequentialGroup()
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(modalDetalleLayout.createSequentialGroup()
                        .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jLabel17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalDetalleLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel18))
                            .addGroup(modalDetalleLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)))))
                .addGap(18, 18, 18)
                .addGroup(modalDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(120, Short.MAX_VALUE))
        );

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

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/config.png"))); // NOI18N
        jButton6.setText("Config");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(menuVentasBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuProductosBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ventasButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
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
                .addComponent(jButton6)
                .addContainerGap(264, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 650));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/encabezado.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, -10, 1190, 200));

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
        jLabel10.setText("Total a pagar");

        LabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTotal.setText("----");

        btnSaveSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LabelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkboxAplicaIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addGap(18, 18, 18)
                                .addComponent(btnClearVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                            .addComponent(btnClearVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddProductToSale, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSaveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(nuevaVentaPanelLayout.createSequentialGroup()
                        .addGroup(nuevaVentaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(checkboxAplicaIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(188, 188, 188))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ventasHistPanelLayout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 888, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(ventasHistPanelLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(btnMostrarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ventasHistPanelLayout.setVerticalGroup(
            ventasHistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ventasHistPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMostrarDetalle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        principalPanel.addTab("Ventas", ventasHistPanel);

        jLabel27.setText("RUC");

        jLabel28.setText("NOMBRE");

        jLabel29.setText("TELEFONO");

        jLabel30.setText("DIRECCION");

        jLabel31.setText("RAZON SOCIAL");

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        jButton23.setText("ACTUALIZAR");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel32.setText("DATOS DE LA EMPRESA");

        javax.swing.GroupLayout adminPanelLayout = new javax.swing.GroupLayout(adminPanel);
        adminPanel.setLayout(adminPanelLayout);
        adminPanelLayout.setHorizontalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(166, 166, 166))
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(jLabel27)
                        .addGap(215, 215, 215)
                        .addComponent(jLabel28)
                        .addGap(201, 201, 201)
                        .addComponent(jLabel29))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel30)
                        .addGap(200, 200, 200)
                        .addComponent(jLabel31))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(423, 423, 423)
                        .addComponent(jButton23))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(312, 312, 312)
                        .addComponent(jLabel32)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        adminPanelLayout.setVerticalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel32)
                .addGap(68, 68, 68)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addGap(3, 3, 3)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jButton23)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        principalPanel.addTab("tab6", adminPanel);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Precio Unitario*:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Cantidad inicial *:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Titulo*:");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Proveedor:");

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

        txtCantIni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantIniActionPerformed(evt);
            }
        });
        txtCantIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantIniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantIniKeyTyped(evt);
            }
        });

        cbxProveedorPro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "proveedor uno", "proveedor dos", "proveedor tres" }));
        cbxProveedorPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProveedorProActionPerformed(evt);
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

        txtDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescActionPerformed(evt);
            }
        });
        txtDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescKeyTyped(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 42, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCantIni, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnSaveProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(33, 33, 33)
                                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxProveedorPro, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cbxCatego, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnLimpiar)
                        .addGap(25, 25, 25))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarpro)
                            .addComponent(btnEditarpro))
                        .addGap(21, 21, 21))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtxtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar))
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel25)
                                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel33)
                                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel35)
                                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(cbxProveedorPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel34)
                                    .addComponent(cbxCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addComponent(btnSaveProducto)
                                .addGap(36, 36, 36)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(txtPrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel24)
                                    .addComponent(txtCantIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnEliminarpro, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEditarpro)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        principalPanel.addTab("Productos", jPanel5);

        getContentPane().add(principalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 1010, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuVentasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasBtnActionPerformed
        // TODO add your handling code here:

        principalPanel.setSelectedIndex(0);
        this.tmpVenta = new Sale();
    }//GEN-LAST:event_menuVentasBtnActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        jtxtFiltro.setText("");
        LimpiarTable((DefaultTableModel) TableProducto.getModel());
        LoadProductos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void menuProductosBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosBtnActionPerformed
        // TODO add your handling code here:
        principalPanel.setSelectedIndex(3);
        jtxtFiltro.setText("");
        LimpiarTable((DefaultTableModel) TableProducto.getModel());
        
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
    }//GEN-LAST:event_btnCashConfirmActionPerformed

    private void txtDescActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtTitActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnSaveProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveProductoActionPerformed
        // JENI
        try {
            float precioUnitario = Float.parseFloat(txtPrecioUni.getText());
            int cantidadInicial = Integer.parseInt(txtCantIni.getText());
            String titulo = txtTit.getText();
            String descripcion = txtDesc.getText();
            String codigo = txtCod.getText();
            String proveedor = cbxProveedorPro.getSelectedItem().toString();
            String categoria = cbxCatego.getSelectedItem().toString();
            boolean castEstado = true;

            try {
                // TODO: Se puede cargar un varias veces un producto pero con diferentes proveedores?
                // o permitimos cargar el producto una sola vez y luego agregar todos los proveedores que querramos?
                
                productosService.searchByCodigo(codigo);
                JOptionPane.showMessageDialog(null, "El producto con el codigo " + codigo + " ya existe.");
                Limpiarproducto();//limpia
            } catch (Exception ex) {
                Product nuevoProducto = new Product(
                        0,
                        precioUnitario,
                        cantidadInicial,
                        titulo,
                        descripcion,
                        codigo,
                        proveedor,
                        categoria,
                        castEstado
                );
                this.productosService.add(nuevoProducto);
                LimpiarTable((DefaultTableModel) TableProducto.getModel());
                LoadProductos();
                //Limpiarproducto();
            }

        } catch (NumberFormatException ex) {

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        obligTit = 0;
        obligPrec = 0;
        obligCantI = 0;
        obligCod = 0;
        
        btnSaveProducto.setEnabled(false);
        
    }//GEN-LAST:event_btnSaveProductoActionPerformed

    private void cbxProveedorProActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void txtCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCatActionPerformed

    private void txtCantIniActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtPrecioUniActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtCodigoProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProActionPerformed

    private void btnAddProductToSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductToSaleActionPerformed

        // Cuando agregamos dos veces el mismo producto deberiamos agrupar las dos entradas
        // en lugar de que aparezcan repetidos
        int cantidad;
        String codigoBarras = txtCodigoProducto.getText();

        if (!txtCantidadVenta.getText().equals("")) {
            cantidad = Integer.parseInt(txtCantidadVenta.getText());
        } else {
            cantidad = 1;
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

            // TODO: No se actualiza el total de la venta cuando se agrupan dos productos iguales
            LabelTotal.setText(String.valueOf(this.tmpVenta.getTotal()));

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
        modalFrame.getContentPane().add(PaymentMethodPanel);
        modalFrame.pack();
        modalFrame.setVisible(true);

        // Esto se ejecuta cada vez que se ingresa informacion en el textbox
        paymentCashPago.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (paymentCashPago.getText().equals("")) {
                    return;
                }

                paymentCashErrorLabel.setText("");
                paymentCashPago.setBorder(new LineBorder(Color.GRAY, 1));

                Float pago = Float.parseFloat(paymentCashPago.getText());

                if (pago < tmpVenta.getTotal()) {
                    paymentCashErrorLabel.setForeground(Color.RED);
                    paymentCashErrorLabel.setText("El pago es menor al monto a pagar.");
                    paymentCashPago.setBorder(new LineBorder(Color.RED, 2));
                    return;
                }

                float diff = pago - tmpVenta.getTotal();

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
    }//GEN-LAST:event_btnEliminarventaActionPerformed

    private void ventasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventasButtonActionPerformed
        principalPanel.setSelectedIndex(1);
        LimpiarTable((DefaultTableModel) TableVentasHistorico.getModel());
        LoadVentasHistorico();
    }//GEN-LAST:event_ventasButtonActionPerformed

    private void TableVentasHistoricoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasHistoricoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TableVentasHistoricoMouseClicked

    public void ClearTableVenta() {
        DefaultTableModel dtm = (DefaultTableModel) TableVenta.getModel();
        dtm.setRowCount(0);
    }

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodActionPerformed

    private void txtPrecioUniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUniKeyTyped
        // TODO add your handling code here:
        ProductosValidacion.numberDecimalKeyPress(evt, txtPrecioUni);
    }//GEN-LAST:event_txtPrecioUniKeyTyped

    private void txtCantIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantIniKeyTyped
        // TODO add your handling code here:
        ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantIniKeyTyped

    private void txtTitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyTyped
        // TODO add your handling code here:
        ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_txtTitKeyTyped

    private void txtDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyTyped
        // TODO add your handling code here:
        ProductosValidacion.textKeyPress(evt);
    }//GEN-LAST:event_txtDescKeyTyped

    private void txtCodKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyTyped
        // TODO add your handling code here:

        //ProductosValidacion.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodKeyTyped

    private void txtTitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyReleased
        // Valida el campo obligatorio Titulo

        obligTit = ProductosValidacion.camposObligTxt(txtTit, jLabel25, btnSaveProducto, contadorP);
        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
            btnSaveProducto.setEnabled(true);
        } else {
            btnSaveProducto.setEnabled(false);
        }
    }//GEN-LAST:event_txtTitKeyReleased

    private void txtPrecioUniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUniKeyReleased
        // Valida el campo obligatorio Precio

        obligPrec = ProductosValidacion.camposObligTxt(txtPrecioUni, jLabel23, btnSaveProducto, contadorP);

        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
            btnSaveProducto.setEnabled(true);
        } else {
            btnSaveProducto.setEnabled(false);
        }

    }//GEN-LAST:event_txtPrecioUniKeyReleased

    private void txtCantIniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantIniKeyReleased
        // Valida el campo obligatorio Cantidad
        obligCantI = ProductosValidacion.camposObligTxt(txtCantIni, jLabel24, btnSaveProducto, contadorP);
        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
            btnSaveProducto.setEnabled(true);
        } else {
            btnSaveProducto.setEnabled(false);
        }
    }//GEN-LAST:event_txtCantIniKeyReleased

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        // Valida el campo obligatorio Codigo de barra
        obligCod = ProductosValidacion.camposObligTxt(txtCod, jLabel35, btnSaveProducto, contadorP);
        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
            btnSaveProducto.setEnabled(true);
        } else {
            btnSaveProducto.setEnabled(false);
        }
    }//GEN-LAST:event_txtCodKeyReleased

    private void txtDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyReleased
        if (obligPrec == 1 && obligTit == 1 && obligCantI == 1 && obligCod == 1) {
            btnSaveProducto.setEnabled(true);
        } else {
            btnSaveProducto.setEnabled(false);
        }
    }//GEN-LAST:event_txtDescKeyReleased

    private void txtCodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodKeyPressed

    private void cbxCategoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCategoActionPerformed

    private void btnEliminarproActionPerformed(java.awt.event.ActionEvent evt) {

        // Verificar si hay alguna fila seleccionada
        if (TableProducto.getSelectedRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione al menos una fila para eliminar.");
        }
        
        DefaultTableModel model = (DefaultTableModel) TableProducto.getModel();

        // Usamos StringBuilder para construir la cadena de productos eliminados
        StringBuilder productosEliminadosBuilder = new StringBuilder();
        List<Product> productsToDisable = new ArrayList<>();

        for (int row: TableProducto.getSelectedRows()) {
            int rowIndex = TableProducto.convertRowIndexToModel(row);
            String selectedProductCode = model.getValueAt(rowIndex, model.findColumn("CODIGO")).toString();
            
            try {
                Product found = productosService.searchByCodigo(selectedProductCode);
                productsToDisable.add(found);
                // Agregamos el valor de la celda seguido de una coma y un espacio a la cadena
                productosEliminadosBuilder.append(found.getTitle()).append(", "); 
            } catch (Exception ex) {}
            
        }

        String productosEliminados = productosEliminadosBuilder.toString(); // Convertimos el StringBuilder a String

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar los productos: " + productosEliminados + "?");

        if (confirmacion == JOptionPane.YES_OPTION) {
            
            for (Product toDisable: productsToDisable) {
                toDisable.setEnabled(false);
                try {
                    productosService.update(toDisable);
                } catch (Exception ex) {}
                
            }
            
            LimpiarTable((DefaultTableModel) TableProducto.getModel());
            LoadProductos();
            
            JOptionPane.showMessageDialog(null, "Se deshabilitaron los productos: " + productosEliminados);

        }
        
    }

    private void btnEditarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarproActionPerformed

        if (TableProducto.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione solo una fila para editar.");
            return;
        }

        // Obtener fila seleccionada
        int filaSeleccionada = TableProducto.getSelectedRow();

        // Verificar si hay una fila seleccionada
        if (filaSeleccionada == -1) {
            return;
        }
        
        // Obtener los valores de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) TableProducto.getModel();

        String precioUni = TableProducto.getValueAt(filaSeleccionada, model.findColumn("PRECIO UNITARIO")).toString();
        String cantIni = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CANTIDAD INICIAL")).toString();
        String titulo = TableProducto.getValueAt(filaSeleccionada, model.findColumn("TITULO")).toString();
        
        Object descValue = TableProducto.getValueAt(filaSeleccionada, model.findColumn("DESCRIPCION"));
        
        if (descValue == null) descValue = "";
        String desc = descValue.toString();
        
        String codi = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CODIGO")).toString();
        String proveedor = TableProducto.getValueAt(filaSeleccionada, model.findColumn("PROVEEDOR")).toString();
        String categoria = TableProducto.getValueAt(filaSeleccionada, model.findColumn("CATEGORIA")).toString();
        String ID = TableProducto.getValueAt(filaSeleccionada, model.findColumn("ID")).toString();
        
        String enabledStatus = TableProducto.getValueAt(filaSeleccionada, model.findColumn("ESTADO")).toString();
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

    }//GEN-LAST:event_btnEditarproActionPerformed

    private void txtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProductoActionPerformed

    private void jtxtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFiltroActionPerformed

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
   
      // TODO add your handling code here:

        // TODO: Arreglar colores de los inputs en modalUpdate
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
            PrecioUni.setBorder(new LineBorder(Color.RED, 2));
            PrecioUni.setText(prodToUpdate.getUnitaryPrice() + "");
            return;
            
        }
                 
        // Validar el Nueva cantidad inicial
        int nuevaCantIni = 0;

        try {
         
            nuevaCantIni = Integer.parseInt(CantInici.getText());
            prodToUpdate.setInitialQuantity(nuevaCantIni);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(modalUpdateFrame, "Por favor, ingrese una cantidad inicial valida.");
            CantInici.setBorder(new LineBorder(Color.RED, 2));
            CantInici.setText(prodToUpdate.getInitialQuantity() + "");
            return;
        }

        String nuevoTitulo = Titulo.getText().trim();
        
        //Validar Titulo:
        if (nuevoTitulo.isEmpty()) {
            JOptionPane.showMessageDialog(modalUpdateFrame, "Por favor ingresa un Título válido.");
            Titulo.setBorder(new LineBorder(Color.RED, 2));
            Titulo.setText(prodToUpdate.getTitle());
            return; // Salir del método o tomar otras acciones según sea necesario
        }
        
        prodToUpdate.setTitle(nuevoTitulo);

        String nuevaDesc = Desc.getText();
        
        String nuevoProveedor = jComboProv.getSelectedItem().toString();
        String nuevaCategoria = jComboCat.getSelectedItem().toString();

        prodToUpdate.setDescription(nuevaDesc);
        prodToUpdate.setProvider(nuevoProveedor);
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
                
                jId.setText(ventaEncontrada.getId() + "");
                jTotal.setText(ventaEncontrada.getTotal() + "");
                
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

    private void jIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jIdActionPerformed

    private void btnUpdateEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEnabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEnabledActionPerformed

    private void btnUpdateEnabled1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEnabled1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEnabled1ActionPerformed

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
    private javax.swing.JTable TableVenta;
    private javax.swing.JTable TableVentaDetalle;
    private javax.swing.JTable TableVentasHistorico;
    public javax.swing.JTextField Titulo;
    private javax.swing.JPanel adminPanel;
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
    private javax.swing.JComboBox<String> cbxProveedorPro;
    private java.awt.Checkbox checkboxAplicaIva;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton6;
    public javax.swing.JComboBox<String> jComboCat;
    public javax.swing.JComboBox<String> jComboProv;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JTextField jId;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTotal;
    private javax.swing.JTextField jtxtFiltro;
    private javax.swing.JLabel labelUpdateID;
    private java.awt.List list1;
    private javax.swing.JButton menuProductosBtn;
    private javax.swing.JButton menuVentasBtn;
    private javax.swing.JPanel modalDetalle;
    private javax.swing.JPanel modalProgressBar;
    private javax.swing.JPanel modalUpdate;
    private javax.swing.JPanel nuevaVentaPanel;
    private javax.swing.JLabel paymentCashErrorLabel;
    private javax.swing.JTextField paymentCashMonto;
    private javax.swing.JTextField paymentCashPago;
    private javax.swing.JTextField paymentCashVuelto;
    private javax.swing.JTabbedPane principalPanel;
    private javax.swing.JTextField txtCantIni;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtPrecioUni;
    private javax.swing.JTextField txtTit;
    private javax.swing.ButtonGroup updateEnabledGroup;
    private javax.swing.JButton ventasButton;
    private javax.swing.JPanel ventasHistPanel;
    // End of variables declaration//GEN-END:variables

    private void Limpiarproducto() {//limpia los campos
        // txtCodigoPro.setText("");
        txtPrecioUni.setText("");
        txtCantIni.setText("");
        txtTit.setText("");
        txtDesc.setText("");
        txtCod.setText("");

    }
}
