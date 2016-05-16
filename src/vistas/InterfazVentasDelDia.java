/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import controladores.VentaJpaController;
import entidades.Cliente;
import entidades.Venta;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
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
 * @author kevin
 */
public class InterfazVentasDelDia extends javax.swing.JInternalFrame {
    
    VentaJpaController controladorV;
    ModeloVentas modeloV;
    List<Cliente> encontradosC;
    
    /**
     * Creates new form InterfazOperacionesDelDia
     */
    public InterfazVentasDelDia() {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorV = new VentaJpaController(Conexion.getConexion().getEmf());
        cargarVentas();
    }
    
    
    public void cargarVentas() {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q;
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                String fechaI = formato.format(new Date()); //se obtiene solo la fecha sin hora del jDateChooser1
                String strFecha = fechaI + " 00:00:00"; //para que sea la fecha del jDateChooser1 pero a las 00:00:00
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date fechaF = formatoFecha.parse(strFecha); //la fecha seleccionada a las 00:00:00 se transforma a tipo Date
                if (nombreCliente.getText().matches("[ ]*") || clientes.getSelectedItem().equals("Todos los Clientes")) {
                    q = emf.createEntityManager().createNamedQuery("Venta.findSinceFecha");
                    q.setParameter("fecha", fechaF);//para poder encontrar todas desde esa fecha desde las 00:00:00
                    if (!q.getResultList().isEmpty()) {
                        ArrayList<Venta> todasLasVentas = new ArrayList<>(q.getResultList());
                        modeloV = new ModeloVentas(todasLasVentas);
                        tablaVentas.setModel(modeloV);
                        ajustarColumnas(tablaVentas);
                    } else {
                        modeloV = new ModeloVentas(new ArrayList<>());
                        tablaVentas.setModel(modeloV);
                        ajustarColumnas(tablaVentas);
                    }
                } else if (clientes.getSelectedIndex() > 1) {
                    q = emf.createEntityManager().createNamedQuery("Venta.findSinceFechaAndIdCliente");
                    q.setParameter("idCliente", encontradosC.get(clientes.getSelectedIndex() - 2).getIdCliente());
                    q.setParameter("fecha", fechaF);
                    if (!q.getResultList().isEmpty()) {
                        ArrayList<Venta> encontradas = new ArrayList<>(q.getResultList());
                        modeloV = new ModeloVentas(encontradas);
                        tablaVentas.setModel(modeloV);
                        ajustarColumnas(tablaVentas);
                    } else {
                        modeloV = new ModeloVentas(new ArrayList<>());
                        tablaVentas.setModel(modeloV);
                        ajustarColumnas(tablaVentas);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un dato de la lista desplegable.", "Debe seleccionar un Cliente", JOptionPane.WARNING_MESSAGE);
                }
            } catch (ParseException ex) {
            }
    }
    
    public void buscarClientes(int opc) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        DefaultComboBoxModel modelo;
        if (!nombreCliente.getText().matches("[ ]*")) {
            Query q = emf.createEntityManager().createNamedQuery("Cliente.findLikeNombre");
            q.setParameter("nombre", nombreCliente.getText() + "%");
            if (!q.getResultList().isEmpty()) {
                modelo = new DefaultComboBoxModel();
                encontradosC = q.getResultList();
                if (opc == 0) {
                    modelo.addElement("Seleccione un Cliente");
                    modelo.addElement("Todos los Clientes");
                    for (Cliente c : encontradosC) {
                        modelo.addElement(c.getNombre() + ", " + c.getNit());
                    }
                    clientes.setModel(modelo);
                }
            } else {
                modelo = new DefaultComboBoxModel();
                modelo.addElement("Sin coincidencias");
                modelo.addElement("Todos los Clientes");
                clientes.setModel(modelo);
            }
        } else {
            modelo = new DefaultComboBoxModel();
            modelo.addElement("Sin coincidencias");
            modelo.addElement("Todos los Clientes");
            clientes.setModel(modelo);
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

    public void mostrarComprobanteDeVenta(int ventaID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://" + Inicio.SERVER + ":3306/ferreteria", Inicio.USER, Inicio.PASS);
            HashMap parametros = new HashMap();
            parametros.put("ventaid", ventaID);
            JasperPrint print = JasperFillManager.fillReport(Inicio.DIRECTORY + "ComprobanteVenta.jasper", parametros, con);
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

        ventas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        nombreCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        verDetalleVenta = new javax.swing.JButton();
        anularVenta = new javax.swing.JButton();
        salirVenta = new javax.swing.JButton();
        buscarVenta = new javax.swing.JButton();
        mostrarComprobante = new javax.swing.JButton();
        clientes = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setVisible(true);

        ventas.setBackground(new java.awt.Color(181, 232, 205));

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Cliente", "Tipo", "Descuento", "Saldo", "Anulada", "Total"
            }
        ));
        jScrollPane2.setViewportView(tablaVentas);

        nombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreClienteKeyReleased(evt);
            }
        });

        jLabel2.setText("Nombre de Cliente:");

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
        buscarVenta.setText("Buscar x Cliente");
        buscarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarVentaActionPerformed(evt);
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
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                            .addGroup(ventasLayout.createSequentialGroup()
                                .addComponent(clientes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(anularVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mostrarComprobante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(verDetalleVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salirVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(ventasLayout.createSequentialGroup()
                                .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buscarVenta)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ventasLayout.setVerticalGroup(
            ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarVenta))
                .addGap(10, 10, 10)
                .addComponent(clientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(verDetalleVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anularVenta)
                        .addGap(18, 18, 18)
                        .addComponent(mostrarComprobante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                        .addComponent(salirVenta))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nombreClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreClienteKeyReleased
        // TODO add your handling code here:
        buscarClientes(0);
    }//GEN-LAST:event_nombreClienteKeyReleased

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
                        int opc = JOptionPane.showConfirmDialog(this, "¿Realmente desea anular esta Venta?", "Confirmación de Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opc == JOptionPane.OK_OPTION) {
                            (((ModeloVentas) tablaVentas.getModel()).getVenta(fila)).setAnulada(Boolean.TRUE);
                            controladorV.edit(((ModeloVentas) tablaVentas.getModel()).getVenta(fila));
                            JOptionPane.showMessageDialog(this, "Venta anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            Conexion.getConexion().getEmf().getCache().evictAll();
                            cargarVentas();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getSaldo() >= (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getTotal() - ((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getDescuento())) {
                    try {
                        // No se ha realizado ningun abono, se puede anular.
                        int opc = JOptionPane.showConfirmDialog(this, "¿Realmente desea anular esta Venta?", "Confirmación de Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opc == JOptionPane.OK_OPTION) {
                            (((ModeloVentas) tablaVentas.getModel()).getVenta(fila)).setAnulada(Boolean.TRUE);
                            controladorV.edit(((ModeloVentas) tablaVentas.getModel()).getVenta(fila));
                            JOptionPane.showMessageDialog(this, "Venta anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            Conexion.getConexion().getEmf().getCache().evictAll();
                            cargarVentas();
                        }
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

    private void salirVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirVentaActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_salirVentaActionPerformed

    private void mostrarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarComprobanteActionPerformed
        // TODO add your handling code here:
        int fila = tablaVentas.getSelectedRow();
        if (fila >= 0) {
            if (((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada() == null
                || !((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getAnulada()) {
                mostrarComprobanteDeVenta(((ModeloVentas) tablaVentas.getModel()).getVenta(fila).getIdVenta());
            } else {
                JOptionPane.showMessageDialog(null, "Comprobante de venta no disponible.\n\r\tDebe seleccionar una venta que no este anulada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una venta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mostrarComprobanteActionPerformed

    private void buscarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarVentaActionPerformed
        // TODO add your handling code here:
        cargarVentas();
    }//GEN-LAST:event_buscarVentaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anularVenta;
    private javax.swing.JButton buscarVenta;
    private javax.swing.JComboBox<String> clientes;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton mostrarComprobante;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JButton salirVenta;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JPanel ventas;
    private javax.swing.JButton verDetalleVenta;
    // End of variables declaration//GEN-END:variables
}
