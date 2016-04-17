/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventas;

import conexion.Conexion;
import entidades.Categoria;
import entidades.Cliente;
import entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import pferreteria.CProducto;
import pferreteria.CVenta;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazVenta extends javax.swing.JInternalFrame {

    modeloProductos mp;
    modeloProductosVenta mpv;
    boolean primerAdd = false;
    ArrayList<CProducto> productosVenta = new ArrayList<>();
    CVenta venta;

    /**
     * Creates new form InternoA
     */
    public InterfazVenta() {
        initComponents();
        setVisible(Boolean.TRUE);
        jDateChooser1.setDate(new java.util.Date());
        jLabel7.setVisible(false);
        jTextField6.setVisible(false);
    }

    public void cargarProductos(String parametro) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Producto.findLikeNombre");
        q.setParameter("nombre", parametro + "%");
        List<Producto> productosBusqueda = q.getResultList();
        ArrayList<Producto> productosB = new ArrayList<>();
        if (!productosBusqueda.isEmpty()) {
            for (Producto P : productosBusqueda) {
                if (!primerAdd) {
                    productosB.add(P);
                } else{
                    for(CProducto CP : productosVenta){
                        if(P.getNombre().compareTo(CP.getNombre())!=0){
                            productosB.add(P);
                        }
                    }
                }
            }
        }
        q = emf.createEntityManager().createNamedQuery("Categoria.findByNombre");
        q.setParameter("nombre", parametro);
        List<Categoria> categorias = q.getResultList();
        if (!categorias.isEmpty()) {
            for (Categoria c : categorias) {
                q = emf.createEntityManager().createNamedQuery("Producto.findByIdCategoria");
                q.setParameter("idCategoria", c.getIdCategoria());
                productosBusqueda = q.getResultList();
                if (!productosBusqueda.isEmpty()) {
                    for (Producto P : productosBusqueda) {
                        productosB.add(P);
                    }
                }
            }
        }
        mp = new modeloProductos(productosB);
        jTable1.setModel(mp);
        ajustarColumnasTabla1();
    }

    public void limpiarTabla1() {
        jTable1.setModel(new DefaultTableModel(new Object[]{"Código", "Producto", "Categoría", "Precio", "Existencias"}, 0));
        ajustarColumnasTabla1();
    }

    public void ajustarColumnasTabla1() {
        jTable1.getColumn("Código").setPreferredWidth(50);
        jTable1.getColumn("Categoría").setPreferredWidth(120);
        jTable1.getColumn("Precio").setPreferredWidth(80);
        jTable1.getColumn("Existencias").setPreferredWidth(80);
        jTable1.getColumn("Código").setMaxWidth(70);
        jTable1.getColumn("Categoría").setMaxWidth(120);
        jTable1.getColumn("Precio").setMaxWidth(80);
        jTable1.getColumn("Existencias").setMaxWidth(80);
        jTable1.getColumn("Código").setMinWidth(30);
        jTable1.getColumn("Categoría").setMinWidth(80);
        jTable1.getColumn("Precio").setMinWidth(50);
        jTable1.getColumn("Existencias").setMinWidth(40);
    }
    
    public void ajustarColumnasTabla2(){
        jTable1.getColumn("Código").setPreferredWidth(50);
        jTable1.getColumn("Cantidad").setPreferredWidth(80);
        jTable1.getColumn("Precio").setPreferredWidth(80); 
        jTable1.getColumn("Subtotal").setPreferredWidth(80);
        jTable1.getColumn("Código").setMinWidth(30);
        jTable1.getColumn("Cantidad").setMinWidth(60);
        jTable1.getColumn("Precio").setMinWidth(60); 
        jTable1.getColumn("Subtotal").setMinWidth(80);
        jTable1.getColumn("Código").setMaxWidth(70);
        jTable1.getColumn("Cantidad").setMaxWidth(100);
        jTable1.getColumn("Precio").setMaxWidth(100); 
        jTable1.getColumn("Subtotal").setMaxWidth(110);
    }

    public int ingresoCantidad(int existencias) {
        JTextField cantidad = new JTextField();
        Object[] objeto = {cantidad};
        int opcion = JOptionPane.showConfirmDialog(this, objeto, "Cantidad a vender:", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                if (!cantidad.getText().matches("[0-9]+")) {
                    throw new NumberFormatException();
                }
                if (cantidad.getText().matches("0+")) {
                    throw new NumberFormatException();
                }
                if (Integer.parseInt(cantidad.getText()) <= existencias) {
                    return Integer.parseInt(cantidad.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Solo hay " + String.valueOf(existencias) + " existencias!",
                            "Existencias insuficientes.", JOptionPane.WARNING_MESSAGE);
                    ingresoCantidad(existencias);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Debe indicar un valor correcto en cantidad a vender!", "ERROR DE FORMATO", JOptionPane.ERROR_MESSAGE);
                ingresoCantidad(existencias);
            }
        }
        return 0;
    }

    /**
     * Crea un cliente si al indicar el cliente de la venta no existe
     *
     * @return true si se agrega el cliente
     */
    public boolean insertarCliente() {
        String registro = "";
        if (!datoNombre.getText().matches("[ ]*") && !datoNit.getText().matches("[ ]*")) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            Query q = emf.createEntityManager().createNamedQuery("Cliente.findByNit");
            q.setParameter("nit", datoNit.getText());
            if (q.getResultList().isEmpty()) {
                venta = new CVenta(emf, Conexion.getConexion().getIdUsuario());
                Cliente creado = venta.crearCliente(datoNombre.getText(), datoDireccion.getText(), datoNit.getText());
                primerAdd = true;
                mostrarDatosCliente(creado);
                crearCliente.setVisible(false);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "El NIT ingresado ya pertenece a un cliente!", "Error de nit.", JOptionPane.ERROR_MESSAGE);
                datoNit.setText("");
                datoNit.requestFocus();
            }
        } else {
            if (datoNombre.getText().matches("[ ]*")) {
                registro += "Nombre\n\r";
            }
            if (datoNit.getText().matches("[ ]*")) {
                registro += "NIT\n\r";
            }
            JOptionPane.showMessageDialog(this, "Los siguientes campos son requeridos:\n\n\r" + registro, "Error.", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void mostrarDatosCliente(Cliente client) {
        codigoCliente.setText(String.valueOf(client.getIdCliente()));
        nombreCliente.setText(client.getNombre());
        direccionCliente.setText(client.getDireccion());
        nitCliente.setText(client.getNit());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crearCliente = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        datoNombre = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        datoNit = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        datoDireccion = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        insertarCliente = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codigoCliente = new javax.swing.JTextField();
        nombreCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        direccionCliente = new javax.swing.JTextField();
        nitCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        numeroVenta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();

        crearCliente.setTitle("Datos de cliente nuevo");
        crearCliente.setName("agregarCliente"); // NOI18N

        jLabel14.setText("Nombre:");

        jLabel15.setText("N I T:");

        jLabel16.setText("Dirección:");

        jButton6.setText("Cancelar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        insertarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/add.png"))); // NOI18N
        insertarCliente.setText("Aceptar");
        insertarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout crearClienteLayout = new javax.swing.GroupLayout(crearCliente.getContentPane());
        crearCliente.getContentPane().setLayout(crearClienteLayout);
        crearClienteLayout.setHorizontalGroup(
            crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crearClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datoNit, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datoDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, crearClienteLayout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(35, 35, 35)
                        .addComponent(insertarCliente)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        crearClienteLayout.setVerticalGroup(
            crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crearClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(datoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datoNit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(8, 8, 8)
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datoDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(32, 32, 32)
                .addGroup(crearClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 153));
        setClosable(true);
        setPreferredSize(new java.awt.Dimension(790, 635));
        setRequestFocusEnabled(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jLabel1.setText("Código");

        codigoCliente.setEnabled(false);
        codigoCliente.setName(""); // NOI18N

        nombreCliente.setEnabled(false);

        jLabel2.setText("Nombre de cliente");

        jLabel3.setText("Dirección");

        direccionCliente.setEnabled(false);

        nitCliente.setEnabled(false);

        jLabel4.setText("N.I.T.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(codigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(direccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(nitCliente))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nitCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(direccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        codigoCliente.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, -1, -1));

        jLabel5.setText("Fecha");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 19, -1, -1));

        numeroVenta.setEnabled(false);
        getContentPane().add(numeroVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 39, 120, -1));

        jLabel6.setText("Número de venta");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 19, -1, -1));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 153));
        jCheckBox1.setText("Venta al crédito");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });
        getContentPane().add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 78, -1, -1));

        jLabel7.setText("Pago inicial:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 64, -1, -1));
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 85, 120, 19));

        jLabel8.setText("Consulta Productos:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 143, -1, -1));

        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 163, 171, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Categoría", "Precio", "Existencias"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(30);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(2).setMinWidth(80);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(3).setMinWidth(50);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(4).setMinWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 194, 571, 122));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Precio", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(30);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable2.getColumnModel().getColumn(2).setMinWidth(60);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(3).setMinWidth(60);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(4).setMinWidth(80);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(4).setMaxWidth(110);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 352, 647, 130));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/purchase.png"))); // NOI18N
        jButton1.setText("Agregar a Venta");
        jButton1.setEnabled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(626, 232, -1, -1));

        jLabel9.setFont(new java.awt.Font("Droid Serif", 3, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(109, 80, 9));
        jLabel9.setText("Productos a vender");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 327, -1, -1));

        jLabel10.setText("TOTAL");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, -1, -1));

        jTextField8.setEnabled(false);
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 500, 121, -1));

        jLabel11.setText("Descuento");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 530, -1, -1));

        jTextField9.setEnabled(false);
        getContentPane().add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 530, 121, -1));

        jLabel12.setText("Recibido");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 560, -1, -1));
        getContentPane().add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 560, 121, 19));

        jTextField11.setEnabled(false);
        getContentPane().add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 560, 121, 19));

        jLabel13.setText("Cambio");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 560, -1, -1));

        jButton2.setText("Limpiar");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 537, -1, 46));

        jButton3.setText("Cancelar");
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 537, -1, 46));

        jButton4.setText("Registrar Venta");
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 537, -1, 46));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 39, 120, -1));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        jButton5.setBorder(null);
        jButton5.setEnabled(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, 40, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
        jButton1.setEnabled(false);
        limpiarTabla1();
        if (!jTextField7.getText().matches("[ ]*")) {
            cargarProductos(jTextField7.getText());
        }
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!primerAdd) {
            JTextField cliente = new JTextField();
            Object[] message = {
                "NIT/Código:", cliente,};
            int opcion = JOptionPane.showConfirmDialog(this, message, "Indicar cliente.", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                EntityManagerFactory emf = Conexion.getConexion().getEmf();
                Query q = emf.createEntityManager().createNamedQuery("Cliente.findByNit");
                q.setParameter("nit", cliente.getText());
                List<Cliente> clienteBusqueda = q.getResultList();
                if (clienteBusqueda.isEmpty()) {
                    int crearcliente = JOptionPane.showConfirmDialog(this, "Desea crear un cliente nuevo?", "El cliente no existe.", JOptionPane.OK_OPTION);
                    if (crearcliente == JOptionPane.OK_OPTION) {
                        crearCliente.setSize(400, 200);
                        crearCliente.setLocationRelativeTo(this);
                        datoNit.setText(cliente.getText());
                        datoDireccion.setText("Ciudad");
                        crearCliente.setVisible(true);
                    }
                } else {
                    primerAdd = true;
                    venta = new CVenta(emf, Conexion.getConexion().getIdUsuario());
                    venta.setIdPersona(clienteBusqueda.get(0).getIdCliente());
                    mostrarDatosCliente(clienteBusqueda.get(0));
                    Producto productoVenta = mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                    int cantidad = ingresoCantidad(productoVenta.getExistencias());
                    if (cantidad != 0) {
                        productosVenta.add(new CProducto(productoVenta.getIdProducto(), productoVenta.getNombre(),
                                cantidad, productoVenta.getPrecio()));
                        mpv = new modeloProductosVenta(productosVenta);
                        jTable2.setModel(mpv);
                        ajustarColumnasTabla2();
                    }

                }
            }
        } else {
            Producto productoVenta = mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
            int cantidad = ingresoCantidad(productoVenta.getExistencias());
            if (cantidad != 0) {
                productosVenta.add(new CProducto(productoVenta.getIdProducto(), productoVenta.getNombre(),
                        cantidad, productoVenta.getPrecio()));
                mpv = new modeloProductosVenta(productosVenta);
                jTable2.setModel(mpv);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        jButton1.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int opc = JOptionPane.showOptionDialog(null,"Que desea hacer?","Elija una acción.",
                               JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                               new Object[]{"Quitar entrada","Editar cantidad"},"Quitar entrada");
//        int opc = JOptionPane.showConfirmDialog(this, "Realmente quiere quitar el producto de esta venta?", "Confirmación de borrado", JOptionPane.OK_CANCEL_OPTION);
        if (opc == 0) {// para verificar si eligió editar la cantidad a vender o eliminar el producto del carrito
            productosVenta.remove(jTable2.getSelectedRow());
            mpv = new modeloProductosVenta(productosVenta);
            jTable2.setModel(mpv);
            ajustarColumnasTabla2();
        } else{
            int cant = ingresoCantidad(productosVenta.get(jTable2.getSelectedRow()).getCantidad());
            if(cant!=0){
                productosVenta.get(jTable2.getSelectedRow()).setCantidad(cant);
                mpv = new modeloProductosVenta(productosVenta);
                jTable2.setModel(mpv);
                ajustarColumnasTabla2();
            }
        }
        jButton5.setEnabled(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        // TODO add your handling code here:
        jButton5.setEnabled(true);
    }//GEN-LAST:event_jTable2MouseReleased

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:
        jLabel7.setVisible(jCheckBox1.isSelected());
        jTextField6.setVisible(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void insertarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertarClienteActionPerformed
        // TODO add your handling code here:
        if (insertarCliente()) {//si se logra insertar el cliente (cuando se inserten datos correctos)
            Producto productoVenta = mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
            int cantidad = ingresoCantidad(productoVenta.getExistencias());
            if (cantidad != 0) {
                productosVenta.add(new CProducto(productoVenta.getIdProducto(), productoVenta.getNombre(),
                        cantidad, productoVenta.getPrecio()));
                mpv = new modeloProductosVenta(productosVenta);
                jTable2.setModel(mpv);
                ajustarColumnasTabla2();
            }
        }
    }//GEN-LAST:event_insertarClienteActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        crearCliente.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codigoCliente;
    private javax.swing.JDialog crearCliente;
    private javax.swing.JTextField datoDireccion;
    private javax.swing.JTextField datoNit;
    private javax.swing.JTextField datoNombre;
    private javax.swing.JTextField direccionCliente;
    private javax.swing.JButton insertarCliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField nitCliente;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField numeroVenta;
    // End of variables declaration//GEN-END:variables
}
