/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compras;

import conexion.Conexion;
import entidades.Categoria;
import entidades.Cliente;
import entidades.Producto;
import entidades.Proveedor;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import pferreteria.CProducto;
import pferreteria.CCompra;
import ventas.modeloProductos;
import ventas.modeloProductosVenta;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazCompra extends javax.swing.JInternalFrame {

    modeloProductos mp;
    modeloProductosVenta mpv;
    boolean primerAdd;
    CCompra compra;
    int cantidad;

    /**
     * Creates new form InternoA
     */
    public InterfazCompra() {
        initComponents();
        primerAdd = false;
        cantidad = 0;
        compra = new CCompra(Conexion.getConexion().getEmf(), Conexion.getConexion().getIdUsuario());
        numeroCompra.setText(String.valueOf(compra.obtenerIdCompra()+1));
        setVisible(true);
        jDateChooser1.setDate(new java.util.Date());
    }

    /**
     * Busca por producto o por categoría, compara si el producto ya fue
     * agregado al carrito y muestra existencias temporales
     *
     * @param parametro puede ser una parte del nombre de los productos o el
     * nombre completo de una categoria
     */
    public void cargarProductos(String parametro) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Producto.findLikeNombre");
        q.setParameter("nombre", parametro + "%");
        List<Producto> productosBusqueda = q.getResultList();
        ArrayList<Producto> productosB = new ArrayList<>();
        if (!productosBusqueda.isEmpty()) {
            for (Producto P : productosBusqueda) {
                if (compra.getProductos().isEmpty()) {//si no hay productos en el carrito solo lo agrega sin buscarlo
                    productosB.add(P);
                } else {
                    for (CProducto cp : compra.getProductos()) {//de lo contrario busca coincidencias en los productos encontrados
                        if (cp.getId().equals(P.getIdProducto())) {
                            P.setExistencias(P.getExistencias() - cp.getCantidad()); // al encontrar una coincidencia actualiza las existencias temporales
                        }
                    }
                    productosB.add(P);
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
                        if (!primerAdd) {
                            productosB.add(P);
                        } else {
                            for (CProducto cp : compra.getProductos()) {
                                if (cp.getId().equals(P.getIdProducto())) {
                                    P.setExistencias(P.getExistencias() - cp.getCantidad());
                                }
                                productosB.add(P);
                            }
                        }
                    }
                }
            }
        }
        mp = new modeloProductos(productosB);
        jTable1.setModel(mp);
        ajustarColumnas(jTable1);
        agregarAlCarrito.setEnabled(false);
    }

    public void limpiarTabla1() {
        mp.borrarProductos();
        jTable1.setModel(new DefaultTableModel(new Object[]{"Código", "Producto", "Categoría", "Precio", "Existencias"}, 0));
        ajustarColumnas(jTable1);
    }
    
    public void limpiarFormulario(){
        primerAdd = false;
        cantidad = 0;
        compra = new CCompra(Conexion.getConexion().getEmf(), Conexion.getConexion().getIdUsuario());
        numeroCompra.setText(String.valueOf(compra.obtenerIdCompra()+1));
        jDateChooser1.setDate(new java.util.Date());
        productoBusqueda.setText("");
        limpiarTabla1();
        mpv.borrarCProductos();
        jTable2.setModel(new DefaultTableModel(new Object[]{"Código", "Producto", "Cantidad", "Precio", "Subtotal"}, 0));
        ajustarColumnas(jTable2);
        codigoCliente.setText("");
        nombreCliente.setText("");
        nitCliente.setText("");
        direccionCliente.setText("");
        efectivo.setText("");
        totalCompra.setText("");
        commitCompra.setEnabled(false);
    }

    public void ajustarColumnas(JTable tabla) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int column = 0; column < tabla.getColumnCount(); column++) {
            TableColumn tableColumn = tabla.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();
            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
                Component c = tabla.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tabla.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            TableColumn columna = tabla.getColumnModel().getColumn(column);
            TableCellRenderer headerRenderer = columna.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = columna.getHeaderValue();
            Component headerComp
                    = headerRenderer.getTableCellRendererComponent(tabla, headerValue, false, false, 0, column);
            preferredWidth = Math.max(preferredWidth, headerComp.getPreferredSize().width);
            tableColumn.setPreferredWidth(preferredWidth);
        }
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
     * Crea un proveedor si al indicar el proveedor de la compra no existe
     *
     * @return true si se agrega el proveedor
     */
    public boolean insertarProveedor() {
        String registro = "";
        if (!datoNombre.getText().matches("[ ]*") && !datoNit.getText().matches("[ ]*")) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            Query q = emf.createEntityManager().createNamedQuery("Cliente.findByNit");
            q.setParameter("nit", datoNit.getText());
            if (q.getResultList().isEmpty()) {
                Proveedor creado = compra.crearProveedor(datoNombre.getText(), datoDireccion.getText(), datoNit.getText());
                primerAdd = true;
                mostrarDatosProveedor(creado);
                Producto productoVenta = mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                if (cantidad != 0) {
                    agrearCproducto(productoVenta, cantidad);
                }
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

    public void mostrarDatosProveedor(Proveedor prov) {
        codigoCliente.setText(String.valueOf(prov.getIdProveedor()));
        nombreCliente.setText(prov.getNombre());
        direccionCliente.setText(prov.getTelefono());
        nitCliente.setText(prov.getNit());
    }

    public void agrearCproducto(Producto prod, int cantidad) {
        boolean yaEstaAgregado = false;
        for (CProducto cp : compra.getProductos()) {
            if (prod.getNombre().compareTo(cp.getNombre()) == 0) {
                //Verifica si se está agregando un producto ya agregado anteriormente y suma las cantidades
                cp.setCantidad(cp.getCantidad() + cantidad);
                compra.getProductos().set(compra.getProductos().indexOf(cp), cp);
                totalCompra.setText("Q. " + compra.getTotal());
                yaEstaAgregado = true;
                break;
            }
        }
        if (!yaEstaAgregado) {
            compra.agregarProducto(new CProducto(prod.getIdProducto(), prod.getNombre(),
                    cantidad, prod.getPrecio()));
            if (compra.getProductos().size() == 1) {
                commitCompra.setEnabled(true);
            }
            totalCompra.setText("Q. " + compra.getTotal());
        }
        mpv = new modeloProductosVenta(compra.getProductos());
        jTable2.setModel(mpv);
        ajustarColumnas(jTable2);
        productoBusqueda.requestFocus();
        cargarProductos(productoBusqueda.getText());
    }

    public Double obtenerMonto() {
        try {
            if (!efectivo.getText().matches("[0-9]*(\\.[0-9])*")) {
                throw new NumberFormatException();
            }
            return Double.parseDouble(efectivo.getText());
        } catch (NumberFormatException ex) {
            if (efectivo.getText().length() > 0) {
                efectivo.setText(efectivo.getText().substring(0, efectivo.getText().length() - 1));
                if (efectivo.getText().length() > 0) {
                    return Double.parseDouble(efectivo.getText());
                }
            }
        }
        return 0.0;
    }

    public boolean autenticacionDeAdministrador() {
        JTextField usuario = new JTextField();
        JTextField contraseña = new JTextField();
        Object[] objeto = {"Usuario Administrador:", usuario, "Contraseña:", contraseña};
        int opcion = JOptionPane.showConfirmDialog(this, objeto, "Permisos de administrador requeridos!", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            Query q = emf.createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenya");
            q.setParameter("usuario", usuario.getText());
            q.setParameter("contrasenya", contraseña.getText());
            if (!q.getResultList().isEmpty()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos!", "Error de autenticación!", JOptionPane.ERROR_MESSAGE);
                autenticacionDeAdministrador();
            }
        }
        return false;
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
        numeroCompra = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        esAlCredito = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        productoBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        agregarAlCarrito = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        totalCompra = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        efectivo = new javax.swing.JTextField();
        cancelar = new javax.swing.JButton();
        commitCompra = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        modificarEntradaCarrito = new javax.swing.JButton();
        cancelar1 = new javax.swing.JButton();

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
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Proveedor"));

        jLabel1.setText("Código");

        codigoCliente.setEnabled(false);
        codigoCliente.setName(""); // NOI18N

        nombreCliente.setEnabled(false);

        jLabel2.setText("Nombre de proveedor");

        jLabel3.setText("Teléfono");

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

        numeroCompra.setEditable(false);
        numeroCompra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 11)); // NOI18N
        numeroCompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(numeroCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 39, 120, -1));

        jLabel6.setText("Número de compra");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 19, -1, -1));

        esAlCredito.setBackground(new java.awt.Color(255, 255, 153));
        esAlCredito.setText("Compra al crédito");
        esAlCredito.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                esAlCreditoStateChanged(evt);
            }
        });
        getContentPane().add(esAlCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 78, -1, -1));

        jLabel8.setText("Consulta Productos:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 143, -1, -1));

        productoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productoBusquedaKeyReleased(evt);
            }
        });
        getContentPane().add(productoBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 163, 171, -1));

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

        agregarAlCarrito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/purchase.png"))); // NOI18N
        agregarAlCarrito.setText("Agregar a Compra");
        agregarAlCarrito.setEnabled(false);
        agregarAlCarrito.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        agregarAlCarrito.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        agregarAlCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarAlCarritoActionPerformed(evt);
            }
        });
        getContentPane().add(agregarAlCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(626, 232, -1, -1));

        jLabel9.setFont(new java.awt.Font("Droid Serif", 3, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(109, 80, 9));
        jLabel9.setText("Productos a comprar");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 327, -1, -1));

        jLabel10.setText("TOTAL:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 550, -1, -1));

        totalCompra.setEditable(false);
        totalCompra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        totalCompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(totalCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 540, 121, -1));

        jLabel12.setText("Efectivo:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 550, -1, -1));

        efectivo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(efectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 550, 121, 19));

        cancelar.setText("Limpiar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        getContentPane().add(cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 540, -1, 46));

        commitCompra.setText("Registrar Compra");
        commitCompra.setEnabled(false);
        commitCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitCompraActionPerformed(evt);
            }
        });
        getContentPane().add(commitCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, -1, 46));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(597, 39, 120, -1));

        modificarEntradaCarrito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        modificarEntradaCarrito.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        modificarEntradaCarrito.setEnabled(false);
        modificarEntradaCarrito.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        modificarEntradaCarrito.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        modificarEntradaCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarEntradaCarritoActionPerformed(evt);
            }
        });
        getContentPane().add(modificarEntradaCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, 40, 40));

        cancelar1.setText("Cancelar");
        getContentPane().add(cancelar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, -1, 46));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void productoBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productoBusquedaKeyReleased
        // TODO add your handling code here:
        if (mp != null) {
            limpiarTabla1();
        }
        if (productoBusqueda.getText().compareTo("") != 0) {
            cargarProductos(productoBusqueda.getText());
        }
    }//GEN-LAST:event_productoBusquedaKeyReleased

    private void agregarAlCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarAlCarritoActionPerformed
        // TODO add your handling code here:
        Producto productoCompra = mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        cantidad = ingresoCantidad(productoCompra.getExistencias());
        if (!primerAdd) {
            JTextField proveedor = new JTextField();
            Object[] message = {
                "NIT/Código:", proveedor,};
            int opcion = JOptionPane.showConfirmDialog(this, message, "Indicar Proveedor.", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                EntityManagerFactory emf = Conexion.getConexion().getEmf();
                Query q = emf.createEntityManager().createNamedQuery("Proveedor.findByNit");
                q.setParameter("nit", proveedor.getText());
                List<Proveedor> proveedorBusqueda = q.getResultList();
                if (proveedorBusqueda.isEmpty()) {
                    int crearcliente = JOptionPane.showConfirmDialog(this, "¿Desea crear un proveedor nuevo?", "El proveedor no existe.", JOptionPane.OK_OPTION);
                    if (crearcliente == JOptionPane.OK_OPTION) {
                        crearCliente.setSize(400, 200);
                        crearCliente.setLocationRelativeTo(this);
                        datoNit.setText(proveedor.getText());
                        datoDireccion.setText("Ciudad");
                        crearCliente.setVisible(true);
                    }
                } else {
                    primerAdd = true;
                    compra.setIdPersona(proveedorBusqueda.get(0).getIdProveedor());
                    mostrarDatosProveedor(proveedorBusqueda.get(0));
                    if (cantidad != 0) {
                        agrearCproducto(productoCompra, cantidad);
                    }
                }
            }
        } else if (cantidad != 0) {
            agrearCproducto(productoCompra, cantidad);
        }
    }//GEN-LAST:event_agregarAlCarritoActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        agregarAlCarrito.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseReleased

    private void modificarEntradaCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarEntradaCarritoActionPerformed
        // TODO add your handling code here:
        int opc = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Elija una acción.",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Quitar entrada", "Editar cantidad", "Cancelar"}, "Editar cantidad");
//        int opc = JOptionPane.showConfirmDialog(this, "Realmente quiere quitar el producto de esta compra?", "Confirmación de borrado", JOptionPane.OK_CANCEL_OPTION);
        if (opc == 0) {// para verificar si eligió editar la cantidad a vender o eliminar el producto del carrito
            compra.quitarProducto(jTable2.getSelectedRow());
            totalCompra.setText("Q. " + compra.getTotal());
            if (compra.getProductos().isEmpty()) {
                commitCompra.setEnabled(false);
            }
            mpv = new modeloProductosVenta(compra.getProductos());
            jTable2.setModel(mpv);
            ajustarColumnas(jTable2);
            productoBusqueda.requestFocus();
            cargarProductos(productoBusqueda.getText());
        } else if (opc == 1) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            Query q = emf.createEntityManager().createNamedQuery("Producto.findByIdProducto");
            q.setParameter("idProducto", (Integer) jTable2.getValueAt(jTable2.getSelectedRow(), 0));
            int cant = ingresoCantidad(((Producto) q.getSingleResult()).getExistencias());
            if (cant != 0) {
                compra.getProductos().get(jTable2.getSelectedRow()).setCantidad(cant);
                totalCompra.setText("Q. " + compra.getTotal());
                mpv = new modeloProductosVenta(compra.getProductos());
                jTable2.setModel(mpv);
                ajustarColumnas(jTable2);
                productoBusqueda.requestFocus();
                cargarProductos(productoBusqueda.getText());
            }
        }
        modificarEntradaCarrito.setEnabled(false);
    }//GEN-LAST:event_modificarEntradaCarritoActionPerformed

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        // TODO add your handling code here:
        modificarEntradaCarrito.setEnabled(true);
    }//GEN-LAST:event_jTable2MouseReleased

    private void insertarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertarClienteActionPerformed
        // TODO add your handling code here:
        insertarProveedor();
    }//GEN-LAST:event_insertarClienteActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        crearCliente.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void commitCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitCompraActionPerformed
        // TODO add your handling code here:
        double total = compra.getTotal();
        boolean mayor = obtenerMonto() >= total;
            if (mayor) {
                    compra.setFecha(jDateChooser1.getDate());
                    compra.setPagoInicial(obtenerMonto());
                    compra.finalizarCompra();
                    commitCompra.setEnabled(false);
            } else if (compra.getCredito()) {
//          int opc = JOptionPane.showConfirmDialog(this, "¿Asignar como pago inicial?", "El pago indicado es insuficiente!",
//                 JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//          if (opc == JOptionPane.YES_OPTION) {
                if (Conexion.getConexion().getEsAdministrador()) {
                    compra.setPagoInicial(obtenerMonto());
                    compra.setFecha(jDateChooser1.getDate());
                    compra.finalizarCompra();
                    commitCompra.setEnabled(false);
                } else if (autenticacionDeAdministrador()) {
                    compra.setPagoInicial(obtenerMonto());
                    compra.setFecha(jDateChooser1.getDate());
                    compra.finalizarCompra();
                    commitCompra.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "No es posible vender al crédito!", "Permisos insuficientes!", JOptionPane.ERROR_MESSAGE);
                }
//      }
            } else {
                JOptionPane.showMessageDialog(this, "Intente las siguientes opciones:\n\n\r\t- Vender al crédito.\n\r\t- Agregar un descuento.",
                        "El monto de efectivo es insuficiente!", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_commitCompraActionPerformed

    private void esAlCreditoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_esAlCreditoStateChanged
        // TODO add your handling code here:
        compra.setCredito(esAlCredito.isSelected());
    }//GEN-LAST:event_esAlCreditoStateChanged

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_cancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarAlCarrito;
    private javax.swing.JButton cancelar;
    private javax.swing.JButton cancelar1;
    private javax.swing.JTextField codigoCliente;
    private javax.swing.JButton commitCompra;
    private javax.swing.JDialog crearCliente;
    private javax.swing.JTextField datoDireccion;
    private javax.swing.JTextField datoNit;
    private javax.swing.JTextField datoNombre;
    private javax.swing.JTextField direccionCliente;
    private javax.swing.JTextField efectivo;
    private javax.swing.JCheckBox esAlCredito;
    private javax.swing.JButton insertarCliente;
    private javax.swing.JButton jButton6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton modificarEntradaCarrito;
    private javax.swing.JTextField nitCliente;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField numeroCompra;
    private javax.swing.JTextField productoBusqueda;
    private javax.swing.JTextField totalCompra;
    // End of variables declaration//GEN-END:variables
}
