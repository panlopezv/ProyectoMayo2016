/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import controladores.AbonoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Abono;
import entidades.Cliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import pferreteria.CPago;
import vistas.Inicio;

/**
 *
 * @author kevin
 */
public class historialDeAbonos extends javax.swing.JInternalFrame {

    Cliente client;
    modeloPagos mcp;
    controlPagos cp;

    /**
     * Creates new form historialDeAbonos
     */
    public historialDeAbonos(Cliente c) {
        initComponents();
        borrarAbono.setEnabled(Boolean.FALSE);
        verComprobante.setEnabled(Boolean.FALSE);
        this.client = c;
        encontrarAbonos();
    }

    public void setCp(controlPagos cp) {
        this.cp = cp;
    }

    public void encontrarAbonos() {
        queCliente.setText(queCliente.getText() + client.getNombre());
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Abono.findByIdCliente");
        q.setParameter("idCliente", client.getIdCliente());
        List<Abono> abonosEncontrados = q.getResultList();
        ArrayList<CPago> listaDeCPagos = new ArrayList<>();
        if (!abonosEncontrados.isEmpty()) {
            info.setText("");
            for (Abono a : abonosEncontrados) {
                listaDeCPagos.add(new CPago(a.getIdAbono(), a.getFecha(), a.getTotal()));
            }
        } else {
            info.setText("Este Cliente no ha efectuado ningun Abono.");
        }
        mcp = new modeloPagos(listaDeCPagos, 0);
        jTable3.setModel(mcp);
    }

    public void mostrarComprobanteAbono(int abonoID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://" + Inicio.SERVER + ":3306/ferreteria", Inicio.USER, Inicio.PASS);
            HashMap parametros = new HashMap();
            parametros.put("abonoID", abonoID);
            JasperPrint print = JasperFillManager.fillReport(Inicio.DIRECTORY + "ComprobanteAbono.jasper", parametros, con);
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

        jPanel1 = new javax.swing.JPanel();
        queCliente = new javax.swing.JLabel();
        info = new javax.swing.JLabel();
        borrarAbono = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        verComprobante = new javax.swing.JButton();

        setClosable(true);
        setTitle("Historial de Pagos");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        queCliente.setText("Abonos Efectuados por: ");

        info.setText("jLabel6");

        borrarAbono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/borrar.png"))); // NOI18N
        borrarAbono.setText("Eliminar");
        borrarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarAbonoActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idAbono", "Monto", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        verComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        verComprobante.setText("Ver Comprobante");
        verComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verComprobanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(borrarAbono)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(verComprobante))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(queCliente)
                    .addComponent(info))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(queCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(info)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verComprobante)
                    .addComponent(borrarAbono))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void borrarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarAbonoActionPerformed
        // TODO add your handling code here:
        AbonoJpaController eliminador = new AbonoJpaController(Conexion.getConexion().getEmf());
        int opc = JOptionPane.showConfirmDialog(this, "¿Realmente desea Eliminar éste abono?",
                "Confirmación de Eliminación.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opc == JOptionPane.OK_OPTION) {
            try {
                eliminador.destroy(mcp.obtenerPagos().get(jTable3.getSelectedRow()).getId());
            } catch (NonexistentEntityException ex) {
                JOptionPane.showMessageDialog(this, "Algo salió mal, por favor intentelo de nuevo.", "Error al eliminar.", opc);
            }
        }
        Conexion.getConexion().getEmf().getCache().evictAll();
        encontrarAbonos();
        borrarAbono.setEnabled(Boolean.FALSE);
        verComprobante.setEnabled(Boolean.FALSE);
    }//GEN-LAST:event_borrarAbonoActionPerformed

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        // TODO add your handling code here:
        borrarAbono.setEnabled(Boolean.TRUE);
        verComprobante.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_jTable3MouseReleased

    private void verComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verComprobanteActionPerformed
        // TODO add your handling code here:
        mostrarComprobanteAbono(mcp.obtenerPagos().get(jTable3.getSelectedRow()).getId());
    }//GEN-LAST:event_verComprobanteActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        // TODO add your handling code here:
        cp.buscarClientes(1);
        cp.cargarVentas();
    }//GEN-LAST:event_formInternalFrameClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrarAbono;
    private javax.swing.JLabel info;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel queCliente;
    private javax.swing.JButton verComprobante;
    // End of variables declaration//GEN-END:variables
}
