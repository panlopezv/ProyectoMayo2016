/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import controladores.CompraJpaController;
import controladores.VentaJpaController;
import entidades.Cliente;
import entidades.Compra;
import entidades.Proveedor;
import entidades.Venta;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazOperaciones extends javax.swing.JInternalFrame {

    VentaJpaController controladorV;
    CompraJpaController controladorC;
    ModeloVentas modeloV;
    ModeloCompras modeloC;

    /**
     * Creates new form InterfazOperaciones
     */
    public InterfazOperaciones() {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorV = new VentaJpaController(Conexion.getConexion().getEmf());
        controladorC = new CompraJpaController(Conexion.getConexion().getEmf());
        jDateChooser1.setDate(new java.util.Date());
        jDateChooser2.setDate(new java.util.Date());
        cargarVentas();
        cargarCompras();
    }

    public void cargarVentas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (sdf.format(jDateChooser1.getDate()).compareTo(sdf.format(new Date()))==0) {
            ArrayList<Venta> encontradas = new ArrayList<>();
            Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findLikeNombre");
            q.setParameter("nombre", jTextField1.getText()+"%");
            if(!q.getResultList().isEmpty()){
                ArrayList<Cliente> encontrados = new ArrayList<>(q.getResultList());
                for(Cliente c: encontrados){
                    q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Venta.findByIdCliente");
                    q.setParameter("idCliente", c.getIdCliente());
                    if(!q.getResultList().isEmpty()){
                        for(Object o : q.getResultList()){
                            encontradas.add((Venta)o);
                        }
                    }
                }
            }
            try {
                modeloV = new ModeloVentas(new ArrayList<>(encontradas));
            } catch (Exception ex) {
                modeloV = new ModeloVentas(new ArrayList<>());
            }
            tablaVentas.setModel(modeloV);
            ajustarColumnas(tablaVentas);
        } else if (jDateChooser1.getDate().before(new Date())){
            ArrayList<Venta> encontradas = new ArrayList<>();
            Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findLikeNombre");
            q.setParameter("nombre", jTextField1.getText()+"%");
            if(!q.getResultList().isEmpty()){
                ArrayList<Cliente> encontrados = new ArrayList<>(q.getResultList());
                for(Cliente c : encontrados){
                    q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Venta.findByIntervaloAndIdCliente");
                    q.setParameter("idCliente", c.getIdCliente());
                    q.setParameter("fecha1", jDateChooser1.getDate());
                    q.setParameter("fecha2", new Date());
                    if(!q.getResultList().isEmpty()){
                        for(Object o:q.getResultList()){
                            encontradas.add((Venta)o);
                        }
                    }
                }
            } 
            try {
                modeloV = new ModeloVentas(new ArrayList<>(encontradas));
            } catch (Exception ex) {
                modeloV = new ModeloVentas(new ArrayList<>());
            }
            tablaVentas.setModel(modeloV);
        }  else{
            JOptionPane.showMessageDialog(this, "La fecha seleccionada debe ser anterior.", "Fecha Incorrecta.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarCompras() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (sdf.format(jDateChooser2.getDate()).compareTo(sdf.format(new Date()))==0) {
            ArrayList<Compra> encontradas = new ArrayList<>();
            Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
            q.setParameter("nombre", jTextField2.getText()+"%");
            if(!q.getResultList().isEmpty()){
                ArrayList<Proveedor> encontrados = new ArrayList<>(q.getResultList());
                for(Proveedor p: encontrados){
                    q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Compra.findByIdProveedor");
                    q.setParameter("idProveedor", p.getIdProveedor());
                    if(!q.getResultList().isEmpty()){
                        for(Object o : q.getResultList()){
                            encontradas.add((Compra)o);
                        }
                    }
                }
            }
            try {
                modeloC = new ModeloCompras(new ArrayList<>(encontradas));
            } catch (Exception ex) {
                modeloC = new ModeloCompras(new ArrayList<>());
            }
            tablaCompras.setModel(modeloC);
            ajustarColumnas(tablaCompras);
        } else if(jDateChooser2.getDate().before(new Date())){
            ArrayList<Compra> encontradas = new ArrayList<>();
            Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
            q.setParameter("nombre", jTextField2.getText()+"%");
            if(!q.getResultList().isEmpty()){
                ArrayList<Proveedor> encontrados = new ArrayList<>(q.getResultList());
                for(Proveedor p : encontrados){
                    q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Compra.findByIntervaloAndIdProveedor");
                    q.setParameter("idProveedor", p.getIdProveedor());
                    q.setParameter("fecha1", jDateChooser2.getDate());
                    q.setParameter("fecha2", new Date());
                    if(!q.getResultList().isEmpty()){
                        for(Object o:q.getResultList()){
                            encontradas.add((Compra)o);
                        }
                    }
                }
            }
            try {
                modeloC = new ModeloCompras(new ArrayList<>(encontradas));
            } catch (Exception ex) {
                modeloC = new ModeloCompras(new ArrayList<>());
            }
            tablaVentas.setModel(modeloC);            
        } else{
            JOptionPane.showMessageDialog(this, "La fecha seleccionada debe ser anterior.", "Fecha Incorrecta.", JOptionPane.ERROR_MESSAGE);
        }
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
    
    public void mostrarComprobanteDeVenta(int ventaID){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://"+Inicio.SERVIDOR+":3306/ferreteria", Inicio.USER, Inicio.PASS);
            HashMap parametros = new HashMap();
            parametros.put("ventaid", ventaID);
            JasperPrint print = JasperFillManager.fillReport(Inicio.DIRECTORIO+"Ventas.jasper", parametros, con);
            JasperViewer.viewReport(print, Boolean.FALSE);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            System.out.println(ex.getMessage());
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

        jTabbedOperaciones = new javax.swing.JTabbedPane();
        ventas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        verDetalleVenta = new javax.swing.JButton();
        anularVenta = new javax.swing.JButton();
        salirVenta = new javax.swing.JButton();
        buscarVenta = new javax.swing.JButton();
        limpiarBusquedaV = new javax.swing.JButton();
        mostrarComprobante = new javax.swing.JButton();
        compras = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCompras = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        buscarCompra = new javax.swing.JButton();
        verDetalleCompra = new javax.swing.JButton();
        anularCompra = new javax.swing.JButton();
        salirCompra = new javax.swing.JButton();
        limpiarBusquedaP = new javax.swing.JButton();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setTitle("Operaciones");

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaVentas);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel2.setText("Cliente:");

        jLabel3.setText("Desde:");

        verDetalleVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        verDetalleVenta.setText("Ver detalle");
        verDetalleVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verDetalleVentaActionPerformed(evt);
            }
        });

        anularVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cancelar.png"))); // NOI18N
        anularVenta.setText("Anular");
        anularVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anularVentaActionPerformed(evt);
            }
        });

        salirVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        salirVenta.setText("Salir");
        salirVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirVentaActionPerformed(evt);
            }
        });

        buscarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/buscar.png"))); // NOI18N
        buscarVenta.setText("Buscar");
        buscarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarVentaActionPerformed(evt);
            }
        });

        limpiarBusquedaV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/clear.png"))); // NOI18N
        limpiarBusquedaV.setText("Limpiar");
        limpiarBusquedaV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarBusquedaVActionPerformed(evt);
            }
        });

        mostrarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/reporte.png"))); // NOI18N
        mostrarComprobante.setText("Comprobante");
        mostrarComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarComprobanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ventasLayout = new javax.swing.GroupLayout(ventas);
        ventas.setLayout(ventasLayout);
        ventasLayout.setHorizontalGroup(
            ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(anularVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mostrarComprobante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(verDetalleVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salirVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(ventasLayout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buscarVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(limpiarBusquedaV)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ventasLayout.setVerticalGroup(
            ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buscarVenta)
                                .addComponent(limpiarBusquedaV)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(verDetalleVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anularVenta)
                        .addGap(18, 18, 18)
                        .addComponent(mostrarComprobante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(salirVenta))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedOperaciones.addTab("Ventas", ventas);

        tablaCompras.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaCompras);

        jLabel5.setText("Proveedor:");

        jLabel6.setText("Desde:");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        buscarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/buscar.png"))); // NOI18N
        buscarCompra.setText("Buscar");
        buscarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarCompraActionPerformed(evt);
            }
        });

        verDetalleCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        verDetalleCompra.setText("Ver detalle");
        verDetalleCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verDetalleCompraActionPerformed(evt);
            }
        });

        anularCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cancelar.png"))); // NOI18N
        anularCompra.setText("Anular");
        anularCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anularCompraActionPerformed(evt);
            }
        });

        salirCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        salirCompra.setText("Salir");
        salirCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirCompraActionPerformed(evt);
            }
        });

        limpiarBusquedaP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/clear.png"))); // NOI18N
        limpiarBusquedaP.setText("Limpiar");
        limpiarBusquedaP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarBusquedaPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout comprasLayout = new javax.swing.GroupLayout(compras);
        compras.setLayout(comprasLayout);
        comprasLayout.setHorizontalGroup(
            comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(comprasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(comprasLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(verDetalleCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(anularCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salirCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(comprasLayout.createSequentialGroup()
                        .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(comprasLayout.createSequentialGroup()
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buscarCompra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(limpiarBusquedaP)))
                        .addGap(0, 150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        comprasLayout.setVerticalGroup(
            comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(comprasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(comprasLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(12, 12, 12)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(comprasLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buscarCompra)
                                .addComponent(limpiarBusquedaP)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(comprasLayout.createSequentialGroup()
                        .addComponent(verDetalleCompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anularCompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(salirCompra))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedOperaciones.addTab("Compras", compras);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedOperaciones)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedOperaciones)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirVentaActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_salirVentaActionPerformed

    private void salirCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirCompraActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_salirCompraActionPerformed

    private void verDetalleVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verDetalleVentaActionPerformed
        // TODO add your handling code here:
        int fila = tablaVentas.getSelectedRow();
        if (fila >= 0) {
            if (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada() == null
                    || !((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada()) {
                Object[] message = {
                    "Ventas:", new DetalleVenta(((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getIdVenta())
                };
                JOptionPane.showMessageDialog(this, message, "Detalle venta.", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Detalles de venta no disponibles.\n\r\tDebe seleccionar una venta que no este anulada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una venta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_verDetalleVentaActionPerformed

    private void anularVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anularVentaActionPerformed
        // TODO add your handling code here:
        int fila = tablaVentas.getSelectedRow();
        if (fila >= 0) {
            if (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada() == null
                    || !((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada()) {
                if (!((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getCredito()) {
                    try {
                        //No es al credito, se puede anular.
                        (((ModeloVentas) tablaVentas.getModel()).getVenta(fila)).setAnulada(Boolean.TRUE);
                        controladorV.edit(((ModeloVentas) tablaVentas.getModel()).getVenta(fila));
                        JOptionPane.showMessageDialog(this, "Venta anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        Conexion.getConexion().getEmf().getCache().evictAll();
                        cargarVentas();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getSaldo() >= ((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getTotal()) {
                    try {
                        // No se ha realizado ningun abono, se puede anular.
                        (((ModeloVentas) tablaVentas.getModel()).getVenta(fila)).setAnulada(Boolean.TRUE);
                        controladorV.edit(((ModeloVentas) tablaVentas.getModel()).getVenta(fila));
                        JOptionPane.showMessageDialog(this, "Venta anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        Conexion.getConexion().getEmf().getCache().evictAll();
                        cargarVentas();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No es posible anular esta venta al crédito.\n\r\tDebido a que ya se han efectuado abonos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La venta ya esta anulada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una venta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_anularVentaActionPerformed

    private void verDetalleCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verDetalleCompraActionPerformed
        // TODO add your handling code here:
        int fila = tablaCompras.getSelectedRow();
        if (fila >= 0) {
            if (((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getAnulada() == null
                    || !((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getAnulada()) {
                Object[] message = {
                    "Compras:", new DetalleCompra(((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getIdCompra())
                };
                JOptionPane.showMessageDialog(this, message, "Detalle compra.", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Detalles de compra no disponibles.\n\r\tDebe seleccionar una compra que no este anulada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una compra.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_verDetalleCompraActionPerformed

    private void anularCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anularCompraActionPerformed
        // TODO add your handling code here:
        int fila = tablaCompras.getSelectedRow();
        if (fila >= 0) {
            if (((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getAnulada() == null
                    || !((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getAnulada()) {
                if (!((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getCredito()) {
                    try {
                        //No es al credito, se puede anular.
                        ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).setAnulada(Boolean.TRUE);
                        controladorC.edit(((ModeloCompras) tablaCompras.getModel()).getCompra(fila));
                        JOptionPane.showMessageDialog(this, "Compra anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        Conexion.getConexion().getEmf().getCache().evictAll();
                        cargarCompras();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getSaldo() >= ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getTotal()) {
                    try {
                        // No se ha realizado ningun pago, se puede anular.
                        ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).setAnulada(Boolean.TRUE);
                        controladorC.edit(((ModeloCompras) tablaCompras.getModel()).getCompra(fila));
                        JOptionPane.showMessageDialog(this, "Compra anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        Conexion.getConexion().getEmf().getCache().evictAll();
                        cargarCompras();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No es posible anular esta compra al crédito.\n\r\tDebido a que ya se han efectuado pagos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La compra ya esta anulada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una compra.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_anularCompraActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyChar() == (char) 10) {
            cargarVentas();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void buscarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarVentaActionPerformed
        // TODO add your handling code here:
        cargarVentas();
    }//GEN-LAST:event_buscarVentaActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        if(evt.getKeyChar() == (char)10){
            cargarCompras();
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void buscarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarCompraActionPerformed
        // TODO add your handling code here:
        cargarCompras();
    }//GEN-LAST:event_buscarCompraActionPerformed

    private void limpiarBusquedaVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBusquedaVActionPerformed
        // TODO add your handling code here:
        jDateChooser1.setDate(new java.util.Date());
        jTextField1.setText("");
        cargarVentas();
    }//GEN-LAST:event_limpiarBusquedaVActionPerformed

    private void limpiarBusquedaPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarBusquedaPActionPerformed
        // TODO add your handling code here:
        jDateChooser2.setDate(new java.util.Date());
        jTextField2.setText("");
        cargarCompras();
    }//GEN-LAST:event_limpiarBusquedaPActionPerformed

    private void mostrarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarComprobanteActionPerformed
        // TODO add your handling code here:
        int fila=tablaVentas.getSelectedRow();
        if(fila>=0){
            if(((ModeloVentas)tablaVentas.getModel()).getVenta(fila).getAnulada()==null ||
                    !((ModeloVentas)tablaVentas.getModel()).getVenta(fila).getAnulada()){
                mostrarComprobanteDeVenta(((ModeloVentas)tablaVentas.getModel()).getVenta(fila).getIdVenta());
            }
            else{
                JOptionPane.showMessageDialog(null,"Comprobante de venta no disponible.\n\r\tDebe seleccionar una venta que no este anulada.","Error",JOptionPane.ERROR_MESSAGE);
            }
        } else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar una venta.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mostrarComprobanteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anularCompra;
    private javax.swing.JButton anularVenta;
    private javax.swing.JButton buscarCompra;
    private javax.swing.JButton buscarVenta;
    private javax.swing.JPanel compras;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedOperaciones;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton limpiarBusquedaP;
    private javax.swing.JButton limpiarBusquedaV;
    private javax.swing.JButton mostrarComprobante;
    private javax.swing.JButton salirCompra;
    private javax.swing.JButton salirVenta;
    private javax.swing.JTable tablaCompras;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JPanel ventas;
    private javax.swing.JButton verDetalleCompra;
    private javax.swing.JButton verDetalleVenta;
    // End of variables declaration//GEN-END:variables
}
