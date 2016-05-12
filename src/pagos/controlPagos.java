/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import entidades.Cliente;
import entidades.Compra;
import entidades.Proveedor;
import entidades.Venta;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author kevin
 */
public class controlPagos extends javax.swing.JInternalFrame {

    List<Cliente> encontradosC;
    List<Proveedor> encontradosP;
    modeloComprasAbono mca;
    modeloVentasAbono mva;
    int idCliente;

    /**
     * Creates new form controlPagos
     */
    public controlPagos() {
        initComponents();
        this.setVisible(Boolean.TRUE);
        idCliente = 0;
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
    
    public void cargarVentas(){
        if (clientesAlCredito.getSelectedIndex() > 0) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            if (!saldo0.isSelected()) {
                Query q = emf.createEntityManager().createNamedQuery("Venta.findByIdClienteAndCreditoSaldo");
                q.setParameter("idCliente", encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
                q.setParameter("credito", true);
                q.setParameter("saldo", 0.00);
                if (!q.getResultList().isEmpty()) {
                    List<Venta> temporal = q.getResultList();
                    ArrayList<Venta> encontradas = new ArrayList<>();
                    for (Venta v : temporal) {
                        encontradas.add(v);
                    }
                    mva = new modeloVentasAbono(encontradas);
                    jTable1.setModel(mva);
                    ajustarColumnas(jTable1);
                } else {
                    mva = new modeloVentasAbono(new ArrayList<>());
                    jTable1.setModel(mva);
                    ajustarColumnas(jTable1);
                }
            } else {
                Query q = emf.createEntityManager().createNamedQuery("Venta.findByIdClienteAndCredito");
                q.setParameter("idCliente", encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
                q.setParameter("credito", true);
                if (!q.getResultList().isEmpty()) {
                    List<Venta> temporal = q.getResultList();
                    ArrayList<Venta> encontradas = new ArrayList<>();
                    for (Venta v : temporal) {
                        encontradas.add(v);
                    }
                    mva = new modeloVentasAbono(encontradas);
                    jTable1.setModel(mca);
                    ajustarColumnas(jTable1);
                } else {
                    mva = new modeloVentasAbono(new ArrayList<>());
                    jTable1.setModel(mva);
                    ajustarColumnas(jTable1);
                }
            }
        }        
    }
    
    public void cargarCompras(){
        if (proveedoresAlCredito.getSelectedIndex() > 0) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            if (!saldo1.isSelected()) {
                Query q = emf.createEntityManager().createNamedQuery("Compra.findByIdProvAndCreditoSaldo");
                q.setParameter("idProveedor", encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
                q.setParameter("credito", true);
                q.setParameter("saldo", 0.00);
                if (!q.getResultList().isEmpty()) {
                    List<Compra> temporal = q.getResultList();
                    ArrayList<Compra> encontradas = new ArrayList<>();
                    for (Compra c : temporal) {
                        encontradas.add(c);
                    }
                    mca = new modeloComprasAbono(encontradas);
                    jTable2.setModel(mca);
                    ajustarColumnas(jTable2);
                } else {
                    mca = new modeloComprasAbono(new ArrayList<>());
                    jTable2.setModel(mca);
                    ajustarColumnas(jTable2);
                }
            } else {
                Query q = emf.createEntityManager().createNamedQuery("Compra.findByIdProvAndCredito");
                q.setParameter("idProveedor", encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
                q.setParameter("credito", true);
                if (!q.getResultList().isEmpty()) {
                    List<Compra> temporal = q.getResultList();
                    ArrayList<Compra> encontradas = new ArrayList<>();
                    for (Compra c : temporal) {
                        encontradas.add(c);
                    }
                    mca = new modeloComprasAbono(encontradas);
                    jTable2.setModel(mca);
                    ajustarColumnas(jTable2);
                } else {
                    mca = new modeloComprasAbono(new ArrayList<>());
                    jTable2.setModel(mca);
                    ajustarColumnas(jTable2);
                }
            }
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nombreCliente = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        clientesAlCredito = new javax.swing.JComboBox<>();
        saldo0 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        abonar = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nombreProveedor = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        proveedoresAlCredito = new javax.swing.JComboBox<>();
        saldo1 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        pagar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);

        jLabel1.setText("Nombre de Cliente con Crédito:");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        clientesAlCredito.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                clientesAlCreditoItemStateChanged(evt);
            }
        });

        saldo0.setText("Incluir ventas saldadas");
        saldo0.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                saldo0StateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Venta", "Total", "Saldo", "Cliente", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
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
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(50);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(1).setMinWidth(80);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(2).setMinWidth(80);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(3).setMinWidth(100);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(140);
            jTable1.getColumnModel().getColumn(4).setMinWidth(80);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        abonar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/payment.png"))); // NOI18N
        abonar.setText("Abonar");

        jButton7.setText("Salir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Ver Historial de Abonos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(jButton1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(clientesAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(saldo0))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(abonar))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(abonar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientesAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saldo0))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Abonos de Clientes", jPanel1);

        jLabel2.setText("Nombre de Proveedor:");

        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        proveedoresAlCredito.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                proveedoresAlCreditoItemStateChanged(evt);
            }
        });

        saldo1.setText("Incluir compras saldadas");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Venta", "Total", "Saldo", "Cliente", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
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
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(50);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable2.getColumnModel().getColumn(1).setMinWidth(80);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(1).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(2).setMinWidth(80);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(3).setMinWidth(100);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(140);
            jTable2.getColumnModel().getColumn(4).setMinWidth(80);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        pagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/paying.png"))); // NOI18N
        pagar.setText("Efectuar Pago");

        jButton5.setText("Salir");

        jButton6.setText("Ver Historial de Pagos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pagar))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(jButton3))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(proveedoresAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(49, 49, 49)
                                        .addComponent(saldo1)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pagar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proveedoresAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saldo1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pagos a Proveedores", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Cliente.findLikeNombre");
        q.setParameter("nombre", "%"+nombreCliente.getText()+"%");
        encontradosC = q.getResultList();
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        if (!encontradosC.isEmpty()) {
            modelo.addElement("Seleccione un cliente");
            if (encontradosC.size() == 1) {
                modelo.addElement(encontradosC.get(0).getNombre() + " " + encontradosC.get(0).getNit());
                modelo.setSelectedItem(encontradosC.get(0).getNombre() + " " + encontradosC.get(0).getNit());
            } else {
                for (int i = 0; i < encontradosC.size(); i++) {
                    modelo.addElement(encontradosC.get(i).getNombre() + " " + encontradosC.get(i).getNit());
                }
            }
        } else {
            encontradosC = new ArrayList<>();
            modelo.addElement("No hay coincidencias.");
        }
        clientesAlCredito.setModel(modelo);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void clientesAlCreditoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_clientesAlCreditoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_clientesAlCreditoItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
        q.setParameter("nombre", "%"+nombreProveedor.getText()+"%");
        encontradosP = q.getResultList();
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        if (!encontradosP.isEmpty()) {
            modelo.addElement("Seleccione un proveedor");
            if (encontradosP.size() == 1) {
                modelo.addElement(encontradosP.get(0).getNombre() + " " + encontradosP.get(0).getNit());
                modelo.setSelectedItem(encontradosP.get(0).getNombre() + " " + encontradosP.get(0).getNit());
            } else {
                for (int i = 0; i < encontradosP.size(); i++) {
                    modelo.addElement(encontradosP.get(i).getNombre() + " " + encontradosP.get(i).getNit());
                }
            }
        } else {
            encontradosP = new ArrayList<>();
            modelo.addElement("No hay coincidencias.");
        }
        proveedoresAlCredito.setModel(modelo);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void proveedoresAlCreditoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_proveedoresAlCreditoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_proveedoresAlCreditoItemStateChanged

    private void saldo0StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_saldo0StateChanged
        // TODO add your handling code here:
        if(clientesAlCredito.getSelectedIndex()>0){
            cargarVentas();
        }
    }//GEN-LAST:event_saldo0StateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abonar;
    private javax.swing.JComboBox<String> clientesAlCredito;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField nombreProveedor;
    private javax.swing.JButton pagar;
    private javax.swing.JComboBox<String> proveedoresAlCredito;
    private javax.swing.JCheckBox saldo0;
    private javax.swing.JCheckBox saldo1;
    // End of variables declaration//GEN-END:variables

}
