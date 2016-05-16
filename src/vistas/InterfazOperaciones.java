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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazOperaciones extends javax.swing.JInternalFrame {

    VentaJpaController controladorV;
    CompraJpaController controladorC;
    ModeloVentas modeloV;
    ModeloCompras modeloC;
    List<Cliente> encontradosC;
    List<Proveedor> encontradosP;

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
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q;
        double totalAlCredito = 0.00;
        double totalAlContado = 0.00;
        double totalisimo = 0.00;
        if (!porFechaV.isSelected()) {
            if (nombreCliente.getText().matches("[ ]*") || clientes.getSelectedItem().equals("Todos los Clientes")) {
                q = emf.createEntityManager().createNamedQuery("Venta.findAll");
                if (!q.getResultList().isEmpty()) {
                    ArrayList<Venta> todasLasVentas = new ArrayList<>(q.getResultList());
                    modeloV = new ModeloVentas(todasLasVentas);
                    tablaVentas.setModel(modeloV);
                    ajustarColumnas(tablaVentas);
                    for (Venta v : todasLasVentas) {
                        if (v.getAnulada() == null || !v.getAnulada()) {
                            if (v.getCredito()) {
                                totalAlCredito += v.getTotal();
                                totalAlCredito -= v.getDescuento();
                            } else {
                                totalAlContado += v.getTotal();
                                totalAlContado -= v.getDescuento();
                            }
                        }
                    }
                    totalisimo += totalAlCredito;
                    totalisimo += totalAlContado;
                } else {
                    modeloV = new ModeloVentas(new ArrayList<>());
                    tablaVentas.setModel(modeloV);
                    ajustarColumnas(tablaVentas);
                }
            } else if (clientes.getSelectedIndex() > 1) {
                q = emf.createEntityManager().createNamedQuery("Venta.findByIdCliente");
                q.setParameter("idCliente", encontradosC.get(clientes.getSelectedIndex() - 2).getIdCliente());
                if (!q.getResultList().isEmpty()) {
                    ArrayList<Venta> encontradas = new ArrayList<>(q.getResultList());
                    modeloV = new ModeloVentas(encontradas);
                    tablaVentas.setModel(modeloV);
                    ajustarColumnas(tablaVentas);
                    for (Venta v : encontradas) {
                        if (v.getAnulada() == null || !v.getAnulada()) {
                            if (v.getCredito()) {
                                totalAlCredito += v.getTotal();
                                totalAlCredito -= v.getDescuento();
                            } else {
                                totalAlContado += v.getTotal();
                                totalAlContado -= v.getDescuento();
                            }
                        }
                    }
                    totalisimo += totalAlCredito;
                    totalisimo += totalAlContado;
                } else {
                    modeloV = new ModeloVentas(new ArrayList<>());
                    tablaVentas.setModel(modeloV);
                    ajustarColumnas(tablaVentas);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un dato de la lista desplegable.", "Debe seleccionar un Cliente", JOptionPane.WARNING_MESSAGE);
            }
        } else if (jDateChooser1.getDate().before(new Date())) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                String fechaI = formato.format(jDateChooser1.getDate()); //se obtiene solo la fecha sin hora del jDateChooser1
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
                        for (Venta v : todasLasVentas) {
                            if (v.getAnulada() == null || !v.getAnulada()) {
                                if (v.getCredito()) {
                                    totalAlCredito += v.getTotal();
                                    totalAlCredito -= v.getDescuento();
                                } else {
                                    totalAlContado += v.getTotal();
                                    totalAlContado -= v.getDescuento();
                                }
                            }
                        }
                        totalisimo += totalAlCredito;
                        totalisimo += totalAlContado;
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
                        for (Venta v : encontradas) {
                            if (v.getAnulada() == null || !v.getAnulada()) {
                                if (v.getCredito()) {
                                    totalAlCredito += v.getTotal();
                                    totalAlCredito -= v.getDescuento();
                                } else {
                                    totalAlContado += v.getTotal();
                                    totalAlContado -= v.getDescuento();
                                }
                            }
                        }
                        totalisimo += totalAlCredito;
                        totalisimo += totalAlContado;
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
        } else {
            modeloV = new ModeloVentas(new ArrayList<>());
            tablaVentas.setModel(modeloV);
            ajustarColumnas(tablaVentas);
            JOptionPane.showMessageDialog(this, "La fecha seleccionada debe ser anterior.", "Fecha Incorrecta.", JOptionPane.ERROR_MESSAGE);
        }
        totalContadoV.setText(new DecimalFormat("Q #,###,##0.00").format(totalAlContado));
        totalCreditoV.setText(new DecimalFormat("Q #,###,##0.00").format(totalAlCredito));
        granTotalV.setText(new DecimalFormat("Q #,###,##0.00").format(totalisimo));
    }

    public void cargarCompras() {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        Query q;
        double totalAlCredito = 0.00;
        double totalAlContado = 0.00;
        double totalisimo = 0.00;
        if (!porFechaC.isSelected()) {
            if (nombreProveedor.getText().matches("[ ]*") || proveedores.getSelectedItem().equals("Todos los Proveedores")) {
                q = emf.createEntityManager().createNamedQuery("Compra.findAll");
                if (!q.getResultList().isEmpty()) {
                    ArrayList<Compra> todasLasCompras = new ArrayList<>(q.getResultList());
                    modeloC = new ModeloCompras(todasLasCompras);
                    tablaCompras.setModel(modeloC);
                    ajustarColumnas(tablaCompras);
                    for (Compra c : todasLasCompras) {
                        if (c.getAnulada() == null || !c.getAnulada()) {
                            if (c.getCredito()) {
                                totalAlCredito += c.getTotal();
                            } else {
                                totalAlContado += c.getTotal();
                            }
                        }
                    }
                    totalisimo += totalAlCredito;
                    totalisimo += totalAlContado;
                } else {
                    modeloC = new ModeloCompras(new ArrayList<>());
                    tablaCompras.setModel(modeloC);
                    ajustarColumnas(tablaCompras);
                }
            } else if (proveedores.getSelectedIndex() > 1) {
                q = emf.createEntityManager().createNamedQuery("Compra.findByIdProveedor");
                q.setParameter("idProveedor", encontradosP.get(proveedores.getSelectedIndex() - 2).getIdProveedor());
                if (!q.getResultList().isEmpty()) {
                    ArrayList<Compra> encontradas = new ArrayList<>(q.getResultList());
                    modeloC = new ModeloCompras(encontradas);
                    tablaCompras.setModel(modeloC);
                    ajustarColumnas(tablaCompras);
                    for (Compra c : encontradas) {
                        if (c.getAnulada() == null || !c.getAnulada()) {
                            if (c.getCredito()) {
                                totalAlCredito += c.getTotal();
                            } else {
                                totalAlContado += c.getTotal();
                            }
                        }
                    }
                    totalisimo += totalAlCredito;
                    totalisimo += totalAlContado;
                } else {
                    modeloC = new ModeloCompras(new ArrayList<>());
                    tablaCompras.setModel(modeloC);
                    ajustarColumnas(tablaCompras);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un dato de la lista desplegable.", "Debe seleccionar un Proveedor", JOptionPane.WARNING_MESSAGE);
            }
        } else if (jDateChooser2.getDate().before(new Date())) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                String fechaI = formato.format(jDateChooser2.getDate()); //se obtiene solo la fecha sin hora del jDateChooser1
                String strFecha = fechaI + " 00:00:00"; //para que sea la fecha del jDateChooser1 pero a las 00:00:00
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date fechaF = formatoFecha.parse(strFecha); //la fecha seleccionada a las 00:00:00 se transforma a tipo Date
                if (nombreProveedor.getText().matches("[ ]*") || proveedores.getSelectedItem().equals("Todos los Proveedores")) {
                    q = emf.createEntityManager().createNamedQuery("Compra.findSinceFecha");
                    q.setParameter("fecha", fechaF);//para poder encontrar todas desde esa fecha desde las 00:00:00
                    if (!q.getResultList().isEmpty()) {
                        ArrayList<Compra> todasLasCompras = new ArrayList<>(q.getResultList());
                        modeloC = new ModeloCompras(todasLasCompras);
                        tablaCompras.setModel(modeloC);
                        ajustarColumnas(tablaCompras);
                        for (Compra c : todasLasCompras) {
                            if (c.getAnulada() == null || !c.getAnulada()) {
                                if (c.getCredito()) {
                                    totalAlCredito += c.getTotal();
                                } else {
                                    totalAlContado += c.getTotal();
                                }
                            }
                        }
                        totalisimo += totalAlCredito;
                        totalisimo += totalAlContado;
                    } else {
                        modeloC = new ModeloCompras(new ArrayList<>());
                        tablaCompras.setModel(modeloC);
                        ajustarColumnas(tablaCompras);
                    }
                } else if (proveedores.getSelectedIndex() > 1) {
                    q = emf.createEntityManager().createNamedQuery("Compra.findSinceFechaAndIdProveedor");
                    q.setParameter("idProveedor", encontradosP.get(proveedores.getSelectedIndex() - 2).getIdProveedor());
                    q.setParameter("fecha", fechaF);
                    if (!q.getResultList().isEmpty()) {
                        ArrayList<Compra> encontradas = new ArrayList<>(q.getResultList());
                        modeloC = new ModeloCompras(encontradas);
                        tablaCompras.setModel(modeloC);
                        ajustarColumnas(tablaCompras);
                        for (Compra c : encontradas) {
                            if (c.getAnulada() == null || !c.getAnulada()) {
                                if (c.getCredito()) {
                                    totalAlCredito += c.getTotal();
                                } else {
                                    totalAlContado += c.getTotal();
                                }
                            }
                        }
                        totalisimo += totalAlCredito;
                        totalisimo += totalAlContado;
                    } else {
                        modeloC = new ModeloCompras(new ArrayList<>());
                        tablaCompras.setModel(modeloC);
                        ajustarColumnas(tablaCompras);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un dato de la lista desplegable.", "Debe seleccionar un Proveedor", JOptionPane.WARNING_MESSAGE);
                }
            } catch (ParseException ex) {
            }
        } else {
            modeloC = new ModeloCompras(new ArrayList<>());
            tablaCompras.setModel(modeloC);
            JOptionPane.showMessageDialog(this, "La fecha seleccionada debe ser anterior.", "Fecha Incorrecta.", JOptionPane.ERROR_MESSAGE);
        }
        totalContadoC.setText(new DecimalFormat("Q #,###,##0.00").format(totalAlContado));
        totalCreditoC.setText(new DecimalFormat("Q #,###,##0.00").format(totalAlCredito));
        granTotalC.setText(new DecimalFormat("Q #,###,##0.00").format(totalisimo));
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

    public void buscarProveedores(int opc) {
        EntityManagerFactory emf = Conexion.getConexion().getEmf();
        DefaultComboBoxModel modelo;
        if (!nombreProveedor.getText().matches("[ ]*")) {
            Query q = emf.createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
            q.setParameter("nombre", nombreProveedor.getText() + "%");
            if (!q.getResultList().isEmpty()) {
                modelo = new DefaultComboBoxModel();
                encontradosP = q.getResultList();
                if (opc == 0) {
                    modelo.addElement("Seleccione un Proveedor");
                    modelo.addElement("Todos los Proveedores");
                    for (Proveedor p : encontradosP) {
                        modelo.addElement(p.getNombre() + ", " + p.getNit());
                    }
                    proveedores.setModel(modelo);
                }
            } else {
                modelo = new DefaultComboBoxModel();
                modelo.addElement("Sin Coincidencias");
                modelo.addElement("Todos los Proveedores");
                proveedores.setModel(modelo);
            }
        } else {
            modelo = new DefaultComboBoxModel();
            modelo.addElement("Sin Coincidencias");
            modelo.addElement("Todos los Proveedores");
            proveedores.setModel(modelo);
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

        jTabbedOperaciones = new javax.swing.JTabbedPane();
        ventas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        nombreCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        verDetalleVenta = new javax.swing.JButton();
        anularVenta = new javax.swing.JButton();
        salirVenta = new javax.swing.JButton();
        buscarVenta = new javax.swing.JButton();
        mostrarComprobante = new javax.swing.JButton();
        clientes = new javax.swing.JComboBox<>();
        porFechaV = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        totalCreditoV = new javax.swing.JTextField();
        totalContadoV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        granTotalV = new javax.swing.JTextField();
        ventas1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCompras = new javax.swing.JTable();
        nombreProveedor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        salirCompra = new javax.swing.JButton();
        buscarCompra = new javax.swing.JButton();
        proveedores = new javax.swing.JComboBox<>();
        porFechaC = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        totalCreditoC = new javax.swing.JTextField();
        totalContadoC = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        granTotalC = new javax.swing.JTextField();
        verDetalleCompra = new javax.swing.JButton();
        anularCompra = new javax.swing.JButton();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setTitle("Operaciones");

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Cliente", "Tipo", "Descuento", "Saldo", "Anulada", "Total"
            }
        ));
        jScrollPane2.setViewportView(tablaVentas);
        if (tablaVentas.getColumnModel().getColumnCount() > 0) {
            tablaVentas.getColumnModel().getColumn(3).setHeaderValue("Descuento");
        }

        nombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreClienteKeyReleased(evt);
            }
        });

        jLabel2.setText("Nombre de Cliente:");

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
        buscarVenta.setText("Cargar Ventas");
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

        porFechaV.setText("Filtrar Por Fecha");
        porFechaV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                porFechaVMouseReleased(evt);
            }
        });

        jLabel1.setText("Total Ventas Al Crédito:");

        jLabel4.setText("Total Ventas Al Contado:");

        jLabel7.setText("Gran Total:");

        javax.swing.GroupLayout ventasLayout = new javax.swing.GroupLayout(ventas);
        ventas.setLayout(ventasLayout);
        ventasLayout.setHorizontalGroup(
            ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ventasLayout.createSequentialGroup()
                                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(clientes, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nombreCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ventasLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(ventasLayout.createSequentialGroup()
                                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(porFechaV)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buscarVenta)
                                        .addGap(107, 107, 107))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(anularVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mostrarComprobante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(verDetalleVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salirVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalCreditoV, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalContadoV, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(granTotalV, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ventasLayout.setVerticalGroup(
            ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscarVenta))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(porFechaV))
                .addGap(18, 18, 18)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasLayout.createSequentialGroup()
                        .addComponent(verDetalleVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anularVenta)
                        .addGap(18, 18, 18)
                        .addComponent(mostrarComprobante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                        .addComponent(salirVenta))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(totalCreditoV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(totalContadoV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(granTotalV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jTabbedOperaciones.addTab("Ventas", ventas);

        tablaCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Proveedor", "Tipo", "Saldo", "Anulada", "Total"
            }
        ));
        jScrollPane3.setViewportView(tablaCompras);

        nombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreProveedorKeyReleased(evt);
            }
        });

        jLabel8.setText("Nombre de Proveedor:");

        jLabel9.setText("Desde:");

        salirCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        salirCompra.setText("Salir");
        salirCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirCompraActionPerformed(evt);
            }
        });

        buscarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/buscar.png"))); // NOI18N
        buscarCompra.setText("Cargar Compras");
        buscarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarCompraActionPerformed(evt);
            }
        });

        porFechaC.setText("Filtrar Por Fecha");
        porFechaC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                porFechaCMouseReleased(evt);
            }
        });

        jLabel10.setText("Total Compras Al Crédito:");

        jLabel11.setText("Total Compras Al Contado:");

        jLabel12.setText("Gran Total:");

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

        javax.swing.GroupLayout ventas1Layout = new javax.swing.GroupLayout(ventas1);
        ventas1.setLayout(ventas1Layout);
        ventas1Layout.setHorizontalGroup(
            ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventas1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventas1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalCreditoC, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalContadoC, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(granTotalC, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ventas1Layout.createSequentialGroup()
                        .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(proveedores, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreProveedor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ventas1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(ventas1Layout.createSequentialGroup()
                                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(porFechaC)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buscarCompra)
                                .addGap(107, 107, 107))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(salirCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(anularCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(verDetalleCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        ventas1Layout.setVerticalGroup(
            ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventas1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ventas1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(12, 12, 12)
                        .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ventas1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscarCompra))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(porFechaC))
                .addGap(18, 18, 18)
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventas1Layout.createSequentialGroup()
                        .addComponent(verDetalleCompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anularCompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(salirCompra))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ventas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(totalCreditoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(totalContadoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(granTotalC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jTabbedOperaciones.addTab("Compras", ventas1);

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

    private void nombreClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreClienteKeyReleased
        // TODO add your handling code here:
        buscarClientes(0);
    }//GEN-LAST:event_nombreClienteKeyReleased

    private void buscarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarVentaActionPerformed
        // TODO add your handling code here:
        cargarVentas();
    }//GEN-LAST:event_buscarVentaActionPerformed

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

    private void nombreProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreProveedorKeyReleased
        // TODO add your handling code here:
        buscarProveedores(0);
    }//GEN-LAST:event_nombreProveedorKeyReleased

    private void salirCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirCompraActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_salirCompraActionPerformed

    private void buscarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarCompraActionPerformed
        // TODO add your handling code here:
        cargarCompras();
    }//GEN-LAST:event_buscarCompraActionPerformed

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
                        int opc = JOptionPane.showConfirmDialog(this, "¿Realmente desea anular esta Compra?", "Confirmación de Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opc == JOptionPane.OK_OPTION) {
                            ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).setAnulada(Boolean.TRUE);
                            controladorC.edit(((ModeloCompras) tablaCompras.getModel()).getCompra(fila));
                            JOptionPane.showMessageDialog(this, "Compra anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            Conexion.getConexion().getEmf().getCache().evictAll();
                            cargarCompras();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "A ocurrido un error inesperado.\n\r\tVuelva a abrir el formulario e intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getSaldo() >= ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).getTotal()) {
                    try {
                        // No se ha realizado ningun pago, se puede anular.
                        int opc = JOptionPane.showConfirmDialog(this, "¿Realmente desea anular esta Compra?", "Confirmación de Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opc == JOptionPane.OK_OPTION) {
                            ((ModeloCompras) tablaCompras.getModel()).getCompra(fila).setAnulada(Boolean.TRUE);
                            controladorC.edit(((ModeloCompras) tablaCompras.getModel()).getCompra(fila));
                            JOptionPane.showMessageDialog(this, "Compra anulada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            Conexion.getConexion().getEmf().getCache().evictAll();
                            cargarCompras();
                        }
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

    private void porFechaVMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_porFechaVMouseReleased
        // TODO add your handling code here:
        cargarVentas();
    }//GEN-LAST:event_porFechaVMouseReleased

    private void porFechaCMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_porFechaCMouseReleased
        // TODO add your handling code here:
        cargarCompras();
    }//GEN-LAST:event_porFechaCMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anularCompra;
    private javax.swing.JButton anularVenta;
    private javax.swing.JButton buscarCompra;
    private javax.swing.JButton buscarVenta;
    private javax.swing.JComboBox<String> clientes;
    private javax.swing.JTextField granTotalC;
    private javax.swing.JTextField granTotalV;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedOperaciones;
    private javax.swing.JButton mostrarComprobante;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField nombreProveedor;
    private javax.swing.JCheckBox porFechaC;
    private javax.swing.JCheckBox porFechaV;
    private javax.swing.JComboBox<String> proveedores;
    private javax.swing.JButton salirCompra;
    private javax.swing.JButton salirVenta;
    private javax.swing.JTable tablaCompras;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField totalContadoC;
    private javax.swing.JTextField totalContadoV;
    private javax.swing.JTextField totalCreditoC;
    private javax.swing.JTextField totalCreditoV;
    private javax.swing.JPanel ventas;
    private javax.swing.JPanel ventas1;
    private javax.swing.JButton verDetalleCompra;
    private javax.swing.JButton verDetalleVenta;
    // End of variables declaration//GEN-END:variables
}
