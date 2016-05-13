/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import controladores.AbonoJpaController;
import controladores.PagoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Abono;
import entidades.Cliente;
import entidades.Compra;
import entidades.Pago;
import entidades.Proveedor;
import entidades.Venta;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import pferreteria.CPago;

/**
 *
 * @author kevin
 */
public class controlPagos extends javax.swing.JInternalFrame {

    List<Cliente> encontradosC;
    List<Proveedor> encontradosP;
    modeloComprasAbono mca;
    modeloVentasAbono mva;
    modeloPagos mcp;

    /**
     * Creates new form controlPagos
     */
    public controlPagos() {
        initComponents();
        this.setVisible(Boolean.TRUE);
        borrarAbono.setEnabled(Boolean.FALSE);
        borrarPago.setEnabled(Boolean.FALSE);
        jTabbedPane1.setEnabledAt(1, Conexion.getConexion().getEsAdministrador());
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

    public void limpiarTabla1() {
        if (mva != null) {
            mva.borrarVentas();
            jTable1.setModel(new DefaultTableModel(new Object[]{"No. Venta", "Total", "Saldo", "Cliente", "Fecha"}, 0));
            ajustarColumnas(jTable1);
        }
    }

    public void limpiarTabla2() {
        if (mca != null) {
            mca.borrarCompras();
            jTable2.setModel(new DefaultTableModel(new Object[]{"No. Compra", "Total", "Saldo", "Proveedor", "Fecha"}, 0));
            ajustarColumnas(jTable2);
        }
    }

    public void buscarClientes(int opc) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        DefaultComboBoxModel modelo;
        Query q = emf.createEntityManager().createNamedQuery("Cliente.findLikeNombre");
        q.setParameter("nombre", nombreCliente.getText() + "%");
        if (!q.getResultList().isEmpty()) {
            modelo = new DefaultComboBoxModel();
            encontradosC = q.getResultList();
            if (opc == 0) {
                modelo.addElement("Seleccione un Cliente.");
                for (Cliente c : encontradosC) {
                    modelo.addElement(c.getNombre() + ", " + c.getNit());
                }
                clientesAlCredito.setModel(modelo);
            }
        } else {
            modelo = new DefaultComboBoxModel();
            modelo.addElement("No ha resultados.");
            clientesAlCredito.setModel(modelo);
        }
    }

    public void buscarProveedores(int opc) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        DefaultComboBoxModel modelo;
        Query q = emf.createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
        q.setParameter("nombre", nombreProveedor.getText() + "%");
        if (!q.getResultList().isEmpty()) {
            modelo = new DefaultComboBoxModel();
            encontradosP = q.getResultList();
            if (opc == 0) {
                modelo.addElement("Seleccione un Proveedor.");
                for (Proveedor p : encontradosP) {
                    modelo.addElement(p.getNombre() + ", " + p.getNit());
                }
                proveedoresAlCredito.setModel(modelo);
            }
        } else {
            modelo = new DefaultComboBoxModel();
            modelo.addElement("No ha resultados.");
            proveedoresAlCredito.setModel(modelo);
        }
    }

    public void cargarVentas() {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        if (!saldo0.isSelected()) {
            Query q = emf.createEntityManager().createNamedQuery("Venta.findByIdClienteAndCreditoSaldo");
            q.setParameter("idCliente", encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
            q.setParameter("credito", Boolean.TRUE);
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
                limpiarTabla1();
            }

            saldoCliente.setText(String.valueOf(encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getSaldo()));
        } else {
            Query q = emf.createEntityManager().createNamedQuery("Venta.findByIdClienteAndCredito");
            q.setParameter("idCliente", encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
            q.setParameter("credito", Boolean.TRUE);
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
                limpiarTabla1();
            }
            saldoCliente.setText(String.valueOf(encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getSaldo()));
        }
    }

    public void cargarCompras() {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        if (!saldo1.isSelected()) {
            Query q = emf.createEntityManager().createNamedQuery("Compra.findByIdProvAndCreditoSaldo");
            q.setParameter("idProveedor", encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
            q.setParameter("credito", Boolean.TRUE);
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
                limpiarTabla2();
            }
            saldoProveedor.setText(String.valueOf(encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getSaldo()));
        } else {
            Query q = emf.createEntityManager().createNamedQuery("Compra.findByIdProvAndCredito");
            q.setParameter("idProveedor", encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
            q.setParameter("credito", Boolean.TRUE);
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
                limpiarTabla2();
            }
            saldoProveedor.setText(String.valueOf(encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getSaldo()));
        }
    }

    public void efectuarAbono() {
        JTextField abono = new JTextField();
        Object[] objetos = {
            "Monto: ", abono
        };
        int opc = JOptionPane.showConfirmDialog(this, objetos, "Abono del Cliente " + encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getNombre(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opc == JOptionPane.OK_OPTION) {
            String registro = "";
            if (!abono.getText().matches("[0-9]*(\\.[0-9]+)?")) {
                registro += "Monto: (Error de formato)";
            }
            if (abono.getText().matches("0*(\\.0+)?")) {
                registro += "Monto: (Se requiere un valor mayor a cero)";
            }
            if (registro.compareTo("") == 0) {
                double valor = Double.parseDouble(abono.getText());
                if (encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getSaldo() > 0.00) {
                    AbonoJpaController controladorAbonos = new AbonoJpaController(Conexion.getConexion().getEmf());
                    Abono abono1;
                    if (valor > encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getSaldo()) {
                        abono1 = new Abono(new Date(), encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getSaldo(),
                                encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
                    } else {
                        abono1 = new Abono(new Date(), valor,
                                encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
                    }
                    controladorAbonos.create(abono1);
                    Conexion.getConexion().getEmf().getCache().evictAll();
                    opc = JOptionPane.showConfirmDialog(this, "¿Desea ver el comprobante?", "El abono se realizó con éxito.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    buscarClientes(1);
                    cargarVentas();
                    if (opc == JOptionPane.OK_OPTION) {
                        //mostrar reporte
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "El cliente indicado tiene saldo = 0.00", "No se ha efectuado el Abono", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Registro de Error:\n\r" + registro, "Error", JOptionPane.ERROR_MESSAGE);
                efectuarAbono();
            }
        }
    }

    public void efectuarPago() {
        JTextField pago = new JTextField();
        Object[] objetos = {
            "Monto: ", pago
        };
        int opc = JOptionPane.showConfirmDialog(this, objetos, "Datos de pago",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opc == JOptionPane.OK_OPTION) {
            String registro = "";
            if (!pago.getText().matches("[0-9]*(\\.[0-9]+)?")) {
                registro += "Monto: (Error de formato)";
            }
            if (pago.getText().matches("0*(\\.0+)?")) {
                registro += "Monto: (Se requiere un valor mayor a cero)";
            }
            if (registro.compareTo("") == 0) {
                double valor = Double.parseDouble(pago.getText());
                if (encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getSaldo() > 0.00) {
                    PagoJpaController controladorPagos = new PagoJpaController(Conexion.getConexion().getEmf());
                    Pago pago1;
                    if (valor > encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getSaldo()) {
                        pago1 = new Pago(new Date(), encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getSaldo(),
                                encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
                    } else {
                        pago1 = new Pago(new Date(), valor,
                                encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
                    }
                    controladorPagos.create(pago1);
                    Conexion.getConexion().getEmf().getCache().evictAll();
                    JOptionPane.showMessageDialog(this, "El pago se realizó con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                    buscarProveedores(1);
                    cargarCompras();
                } else {
                    JOptionPane.showMessageDialog(this, "El proveedor indicado tiene saldo = 0.00", "No se ha efectuado el Pago", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Registro de Error:\n\r" + registro, "Error", JOptionPane.ERROR_MESSAGE);
                efectuarPago();
            }
        }
    }

    public void encontrarAbonos() {
        queCliente.setText(queCliente.getText() + encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getNombre());
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Abono.findByIdCliente");
        q.setParameter("idCliente", encontradosC.get(clientesAlCredito.getSelectedIndex() - 1).getIdCliente());
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

    public void encontrarPagos() {
        queProveedor.setText(queProveedor.getText() + encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getNombre());
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q = emf.createEntityManager().createNamedQuery("Pago.findByIdProveedor");
        q.setParameter("idProveedor", encontradosP.get(proveedoresAlCredito.getSelectedIndex() - 1).getIdProveedor());
        List<Pago> pagosEncontrados = q.getResultList();
        ArrayList<CPago> listaDeCPagos = new ArrayList<>();
        if (!pagosEncontrados.isEmpty()) {
            info1.setText("");
            for (Pago p : pagosEncontrados) {
                listaDeCPagos.add(new CPago(p.getIdPago(), p.getFecha(), p.getTotal()));
            }
        } else {
            info1.setText("No se ha efectuado ningún pago a este Proveedor.");
        }
        mcp = new modeloPagos(listaDeCPagos, 1);
        jTable4.setModel(mcp);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        historialDeAbonos = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        borrarAbono = new javax.swing.JButton();
        Salir = new javax.swing.JButton();
        queCliente = new javax.swing.JLabel();
        info = new javax.swing.JLabel();
        historialDePagos = new javax.swing.JDialog();
        borrarPago = new javax.swing.JButton();
        Salir1 = new javax.swing.JButton();
        queProveedor = new javax.swing.JLabel();
        info1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nombreCliente = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        saldo0 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        abonar = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        historialAbonos = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        saldoCliente = new javax.swing.JTextField();
        clientesAlCredito = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nombreProveedor = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        saldo1 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        pagar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        historialPagos = new javax.swing.JButton();
        saldoProveedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        proveedoresAlCredito = new javax.swing.JComboBox<>();

        historialDeAbonos.setTitle("Historial de Abonos");
        historialDeAbonos.setResizable(false);

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

        borrarAbono.setText("Eliminar");
        borrarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarAbonoActionPerformed(evt);
            }
        });

        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        queCliente.setText("Abonos Efectuados por: ");

        info.setText("jLabel6");

        javax.swing.GroupLayout historialDeAbonosLayout = new javax.swing.GroupLayout(historialDeAbonos.getContentPane());
        historialDeAbonos.getContentPane().setLayout(historialDeAbonosLayout);
        historialDeAbonosLayout.setHorizontalGroup(
            historialDeAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historialDeAbonosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historialDeAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(historialDeAbonosLayout.createSequentialGroup()
                        .addGroup(historialDeAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(info)
                            .addComponent(queCliente))
                        .addGap(176, 176, 176)
                        .addComponent(borrarAbono))
                    .addComponent(Salir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        historialDeAbonosLayout.setVerticalGroup(
            historialDeAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialDeAbonosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(queCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(historialDeAbonosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(borrarAbono)
                    .addComponent(info))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Salir)
                .addGap(7, 7, 7))
        );

        historialDePagos.setTitle("Historial de Pagos");
        historialDePagos.setPreferredSize(new java.awt.Dimension(395, 250));
        historialDePagos.setResizable(false);

        borrarPago.setText("Eliminar");
        borrarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarPagoActionPerformed(evt);
            }
        });

        Salir1.setText("Salir");
        Salir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Salir1ActionPerformed(evt);
            }
        });

        queProveedor.setText("Pagos Efectuados a: ");

        info1.setText("jLabel6");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idPago", "Monto", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable4MouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout historialDePagosLayout = new javax.swing.GroupLayout(historialDePagos.getContentPane());
        historialDePagos.getContentPane().setLayout(historialDePagosLayout);
        historialDePagosLayout.setHorizontalGroup(
            historialDePagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historialDePagosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historialDePagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(historialDePagosLayout.createSequentialGroup()
                        .addComponent(queProveedor)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialDePagosLayout.createSequentialGroup()
                        .addGap(0, 52, Short.MAX_VALUE)
                        .addGroup(historialDePagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialDePagosLayout.createSequentialGroup()
                                .addComponent(info1)
                                .addGap(194, 194, 194)
                                .addComponent(borrarPago))
                            .addComponent(Salir1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        historialDePagosLayout.setVerticalGroup(
            historialDePagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialDePagosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(queProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(historialDePagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrarPago)
                    .addComponent(info1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(Salir1)
                .addContainerGap())
        );

        setClosable(true);
        setMaximizable(true);

        jLabel1.setText("Nombre de Cliente con Crédito:");

        nombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreClienteKeyReleased(evt);
            }
        });

        jButton1.setText("Cargar Ventas Al Crédito");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        saldo0.setText("Incluir ventas saldadas");
        saldo0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saldo0MouseClicked(evt);
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
        abonar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abonarActionPerformed(evt);
            }
        });

        jButton7.setText("Salir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        historialAbonos.setText("Ver Historial de Abonos");
        historialAbonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historialAbonosActionPerformed(evt);
            }
        });

        jLabel3.setText("Saldo Cliente:");

        saldoCliente.setEditable(false);

        clientesAlCredito.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(historialAbonos)
                        .addGap(27, 27, 27)
                        .addComponent(jButton7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(saldoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(abonar, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(nombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(clientesAlCredito, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo0)
                    .addComponent(jButton1))
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(abonar)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saldo0))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clientesAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(saldoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(historialAbonos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Abonos de Clientes", jPanel1);

        jLabel2.setText("Nombre de Proveedor:");

        nombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreProveedorKeyReleased(evt);
            }
        });

        jButton3.setText("Cargar Compras al Crédito");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        saldo1.setText("Incluir compras saldadas");
        saldo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saldo1MouseClicked(evt);
            }
        });

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
        pagar.setText("Pagar");
        pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagarActionPerformed(evt);
            }
        });

        jButton5.setText("Salir");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        historialPagos.setText("Ver Historial de Pagos");
        historialPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historialPagosActionPerformed(evt);
            }
        });

        saldoProveedor.setEditable(false);

        jLabel4.setText("Saldo Proveedor:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(historialPagos)
                .addGap(36, 36, 36)
                .addComponent(jButton5))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(saldoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nombreProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(proveedoresAlCredito, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saldo1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pagar, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jButton3)
                                    .addGap(14, 14, 14))))))
                .addGap(200, 200, 200))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pagar)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saldo1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(proveedoresAlCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(saldoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5)
                    .addComponent(historialPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pagos a Proveedores", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        historialDeAbonos.setVisible(Boolean.FALSE);
        borrarAbono.setEnabled(Boolean.FALSE);
        buscarClientes(1);
        cargarVentas();
    }//GEN-LAST:event_SalirActionPerformed

    private void Salir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Salir1ActionPerformed
        // TODO add your handling code here:
        historialDePagos.setVisible(Boolean.FALSE);
        borrarPago.setEnabled(Boolean.FALSE);
        buscarProveedores(1);
        cargarCompras();
    }//GEN-LAST:event_Salir1ActionPerformed

    private void jTable4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseReleased
        // TODO add your handling code here:
        borrarPago.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_jTable4MouseReleased

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        // TODO add your handling code here:
        borrarAbono.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_jTable3MouseReleased

    private void borrarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarAbonoActionPerformed
        // TODO add your handling code here:
        AbonoJpaController eliminador = new AbonoJpaController(Conexion.getConexion().getEmf());
        int opc = JOptionPane.showConfirmDialog(historialDeAbonos, "¿Realmente desea Eliminar éste abono?",
                "Confirmación de Eliminación.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opc == JOptionPane.OK_OPTION) {
            try {
                eliminador.destroy(mcp.obtenerPagos().get(jTable3.getSelectedRow()).getId());
            } catch (NonexistentEntityException ex) {
                JOptionPane.showMessageDialog(historialDeAbonos, "Algo salió mal, por favor intentelo de nuevo.", "Error al eliminar.", opc);
            }
        }
        Conexion.getConexion().getEmf().getCache().evictAll();
        encontrarAbonos();
        borrarAbono.setEnabled(Boolean.FALSE);
    }//GEN-LAST:event_borrarAbonoActionPerformed

    private void borrarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarPagoActionPerformed
        // TODO add your handling code here:
        PagoJpaController eliminador = new PagoJpaController(Conexion.getConexion().getEmf());
        int opc = JOptionPane.showConfirmDialog(historialDePagos, "¿Realmente desea Eliminar éste pago?",
                "Confirmación de Eliminación.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opc == JOptionPane.OK_OPTION) {
            try {
                eliminador.destroy(mcp.obtenerPagos().get(jTable4.getSelectedRow()).getId());
            } catch (NonexistentEntityException ex) {
                JOptionPane.showMessageDialog(historialDePagos, "Algo salió mal, por favor intentelo de nuevo.", "Error al eliminar.", opc);
            }
        }
        Conexion.getConexion().getEmf().getCache().evictAll();
        encontrarPagos();
        borrarPago.setEnabled(Boolean.FALSE);
    }//GEN-LAST:event_borrarPagoActionPerformed

    private void historialPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialPagosActionPerformed
        // TODO add your handling code here:
        if (proveedoresAlCredito.getSelectedIndex() > 0) {
            historialDePagos.setAlwaysOnTop(Boolean.TRUE);
            historialDePagos.setLocation(450, 350);
            historialDePagos.setSize(420, 290);
            historialDePagos.setVisible(Boolean.TRUE);
            encontrarPagos();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Proveedor.", "Ningun proveedor seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_historialPagosActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagarActionPerformed
        // TODO add your handling code here:
        if (proveedoresAlCredito.getSelectedIndex() > 0) {
            efectuarPago();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Proveedor.", "Ningun Proveedor seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_pagarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (proveedoresAlCredito.getSelectedIndex() > 0) {
            cargarCompras();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Proveedor.", "Ningun Proveedor seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void historialAbonosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialAbonosActionPerformed
        // TODO add your handling code here:
        if (clientesAlCredito.getSelectedIndex() > 0) {
            historialDeAbonos.setAlwaysOnTop(Boolean.TRUE);
            historialDeAbonos.setLocation(450, 350);
            historialDeAbonos.setSize(420, 290);
            historialDeAbonos.setVisible(Boolean.TRUE);
            encontrarAbonos();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente.", "Ningun cliente seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_historialAbonosActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void abonarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abonarActionPerformed
        // TODO add your handling code here:
        if (clientesAlCredito.getSelectedIndex() > 0) {
            efectuarAbono();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente.", "Ningun cliente seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_abonarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (clientesAlCredito.getSelectedIndex() > 0) {
            cargarVentas();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente.", "Ningun cliente seleccionado.", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void saldo0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saldo0MouseClicked
        // TODO add your handling code here:
        if (clientesAlCredito.getSelectedIndex() > 0) {
            cargarVentas();
        }
    }//GEN-LAST:event_saldo0MouseClicked

    private void saldo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saldo1MouseClicked
        // TODO add your handling code here:
        if (proveedoresAlCredito.getSelectedIndex() > 0) {
            cargarCompras();
        }
    }//GEN-LAST:event_saldo1MouseClicked

    private void nombreProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreProveedorKeyReleased
        // TODO add your handling code here:
        buscarProveedores(0);
    }//GEN-LAST:event_nombreProveedorKeyReleased

    private void nombreClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreClienteKeyReleased
        // TODO add your handling code here:
        buscarClientes(0);
    }//GEN-LAST:event_nombreClienteKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Salir;
    private javax.swing.JButton Salir1;
    private javax.swing.JButton abonar;
    private javax.swing.JButton borrarAbono;
    private javax.swing.JButton borrarPago;
    private javax.swing.JComboBox<String> clientesAlCredito;
    private javax.swing.JButton historialAbonos;
    private javax.swing.JDialog historialDeAbonos;
    private javax.swing.JDialog historialDePagos;
    private javax.swing.JButton historialPagos;
    private javax.swing.JLabel info;
    private javax.swing.JLabel info1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField nombreProveedor;
    private javax.swing.JButton pagar;
    private javax.swing.JComboBox<String> proveedoresAlCredito;
    private javax.swing.JLabel queCliente;
    private javax.swing.JLabel queProveedor;
    private javax.swing.JCheckBox saldo0;
    private javax.swing.JCheckBox saldo1;
    private javax.swing.JTextField saldoCliente;
    private javax.swing.JTextField saldoProveedor;
    // End of variables declaration//GEN-END:variables

}
