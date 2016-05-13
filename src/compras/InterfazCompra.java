/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compras;

import conexion.Conexion;
import controladores.CategoriaJpaController;
import controladores.ProductoJpaController;
import entidades.Categoria;
import entidades.Detallecompra;
import entidades.Producto;
import entidades.Proveedor;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
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
import vistas.Inicio;
import static vistas.Inicio.conexion;

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
    int idCategoria;
    double costo;
    CategoriaJpaController controladorC;
    ProductoJpaController controladorP;
    List<Categoria> listaCategorias;

    /**
     * Creates new form InternoA
     */
    public InterfazCompra() {
        initComponents();
        this.setVisible(Boolean.TRUE);
        this.setSize(780, 635);
        primerAdd = false;
        cantidad = 0;
        costo = 0.00;
        idCategoria = 0;
        compra = new CCompra(Conexion.getConexion().getEmf(), Conexion.getConexion().getIdUsuario());
        controladorC = new CategoriaJpaController(Inicio.conexion.getEmf());
        controladorP = new ProductoJpaController(Inicio.conexion.getEmf());
        numeroCompra.setText(String.valueOf(compra.obtenerIdCompra() + 1));
        Date hoy = new Date();
        jLabel14.setText(new SimpleDateFormat("dd/MM/yyyy").format(hoy));
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
                P.setPrecio(encontrarCostoActual(P));
//                if (compra.getProductos().isEmpty()) {//si no hay productos en el carrito solo lo agrega sin buscarlo
                productosB.add(P);
//                } else {
//                    for (CProducto cp : compra.getProductos()) {//de lo contrario busca coincidencias en los productos encontrados
//                        if (cp.getId().equals(P.getIdProducto())) {
//                            P.setExistencias(P.getExistencias() - cp.getCantidad()); // al encontrar una coincidencia actualiza las existencias temporales
//                        }
//                    }
//                    productosB.add(P);
//                }
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
                        P.setPrecio(encontrarCostoActual(P));
//                        if (!primerAdd) {
                        productosB.add(P);
//                        } else {
//                            for (CProducto cp : compra.getProductos()) {
//                                if (cp.getId().equals(P.getIdProducto())) {
//                                    P.setExistencias(P.getExistencias() - cp.getCantidad());
//                                }
//                                productosB.add(P);
//                            }
//                        }
                    }
                }
            }
        }
        mp = new modeloProductos(productosB, 1);
        jTable1.setModel(mp);
        ajustarColumnas(jTable1);
        agregarAlCarrito.setEnabled(false);
    }

    public void limpiarTabla1() {
        mp.borrarProductos();
        jTable1.setModel(new DefaultTableModel(new Object[]{"Código", "Producto", "Categoría", "Precio", "Existencias"}, 0));
        ajustarColumnas(jTable1);
    }

    public void limpiarFormulario() {
        primerAdd = false;
        cantidad = 0;
        compra = new CCompra(Conexion.getConexion().getEmf(), Conexion.getConexion().getIdUsuario());
        numeroCompra.setText(String.valueOf(compra.obtenerIdCompra() + 1));
        productoBusqueda.setText("");
        limpiarTabla1();
        mpv.borrarCProductos();
        jTable2.setModel(new DefaultTableModel(new Object[]{"Código", "Producto", "Cantidad", "Precio", "Subtotal"}, 0));
        ajustarColumnas(jTable2);
        codigoProveedor.setText("");
        nombreProveedor.setText("");
        nitProveedor.setText("");
        telefonoProveedor.setText("");
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

    public double encontrarCostoActual(Producto p) {
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Detallecompra.findLastId");
        q.setParameter("idProducto", p.getIdProducto());
        Long maxIdDetalleCompraByProducto = (Long) q.getSingleResult();
        if (maxIdDetalleCompraByProducto == null) {
            return 0.00;
        } else {
            q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Detallecompra.findByIdDetalleCompra");
            q.setParameter("idDetalleCompra", maxIdDetalleCompraByProducto);
            return ((Detallecompra) q.getSingleResult()).getCosto();
        }

    }

    public void ingresoCostoCantidad(double costoActual) {
        JTextField cant = new JTextField();
        JTextField cost = new JTextField(String.valueOf(costoActual));
        Object[] objeto = {
            "Cantidad:", cant,
            "Costo de compra:", cost
        };
        int opcion = JOptionPane.showConfirmDialog(this, objeto, "Datos de Compra.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if (!cant.getText().matches("[0-9]+")) {
                registro += "Cantidad: (Error de formato)\n\r";
            }
            if (cant.getText().matches("0+")) {
                registro += "Cantidad: (No puede ser cero)";
            }
            if (!cost.getText().matches("[0-9]*(\\.[0-9]+)?")) {
                registro += "Costo: (Error de formato)";
            }
            if (cost.getText().matches("0*(\\.0+)?")) {
                registro += "Costo: (Se requiere un valor mayor a cero)";
            }
            if (registro.compareTo("") == 0) {
                cantidad = Integer.parseInt(cant.getText());
                costo = Double.parseDouble(cost.getText());
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Registro de Error:\n\n\r" + registro, "Error", JOptionPane.ERROR_MESSAGE);
                ingresoCostoCantidad(costoActual);
            }
        } else{
            cantidad = 0;
        }
    }

    /**
     * Crea un proveedor si al indicar el proveedor de la compra no existe
     *
     * @param NIT
     */
    public void insertarProveedor(String NIT, Producto prod) {
        JTextField nombre = new JTextField();
        JTextField nit = new JTextField(NIT);
        JTextField telefono = new JTextField();
        Object[] message = {
            "Nombre:", nombre,
            "NIT:", nit,
            "Teléfono: (opcional)", telefono,};
        int opcion = JOptionPane.showConfirmDialog(this, message, "Crear proveedor.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if (nombre.getText().matches("[ ]*")) {
                registro += "Nombre\n\r";
            }
            if (nit.getText().matches("[ ]*")) {
                registro += "NIT\n\r";
            }
            if (!telefono.getText().matches("([0-9]+(-[0-9])*)|[ ]*")) {//pueden ser numeros con/sin guiones o vacío
                registro += "Tel: (dato erróneo)";
            }
            if (telefono.getText().length() >= 15) {
                registro += "Tel: (Excede el largo máximo)";
            }
            if (registro.compareTo("") == 0) {
                EntityManagerFactory emf = Conexion.getConexion().getEmf();
                Query q = emf.createEntityManager().createNamedQuery("Proveedor.findByNit");
                q.setParameter("nit", nit.getText());
                if (q.getResultList().isEmpty()) {
                    Proveedor creado = compra.crearProveedor(nombre.getText(), nit.getText(), telefono.getText());
                    compra.setIdPersona(creado.getIdProveedor());
                    primerAdd = true;
                    mostrarDatosProveedor(creado);
                    Producto productoCompra = prod;
                    if (cantidad != 0) {
                        insertarCproducto(productoCompra);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "El NIT ingresado ya pertenece a un proveedor!", "Error de nit.", JOptionPane.ERROR_MESSAGE);
                    nit.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Los siguientes campos son requeridos:\n\n\r" + registro, "Error.", JOptionPane.ERROR_MESSAGE);
                insertarProveedor(NIT,prod);
            }
        }
    }

    public void crearCategoria() {
        String categoria = JOptionPane.showInputDialog(crearProducto, "Nombre:", "Crear categoría.", JOptionPane.QUESTION_MESSAGE);
        if (categoria != null) {
            if (categoria.matches("[ ]*")) {
                JOptionPane.showMessageDialog(crearProducto, "No se ha podido crear la categoria!", "Campo requerido.", JOptionPane.ERROR_MESSAGE);
                crearCategoria();
            } else {
                Query q = conexion.getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
                q.setParameter("nombre", categoria);
                if (q.getResultList().isEmpty()) {
                    try {
                        idCategoria = compra.crearCategoria(categoria).getIdCategoria();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(crearProducto, "Algo salió mal.", "Error al crear la categoría.", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(crearProducto, "La categoría ingresada ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearCategoria();
                }
            }
        }
    }

    public void mostrarCategorias() {
        listaCategorias = controladorC.findCategoriaEntities();
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("Seleccione una categoría");
        for (int i = 0; i < listaCategorias.size(); i++) {
            modelo.addElement(listaCategorias.get(i).getNombre());
            if (idCategoria != 0) {
                if (listaCategorias.get(i).getIdCategoria() == idCategoria) {
                    modelo.setSelectedItem(listaCategorias.get(i).getNombre());
                }
            }
        }
        categorias.setModel(modelo);
    }

    public void mostrarDatosProveedor(Proveedor prov) {
        codigoProveedor.setText(String.valueOf(prov.getIdProveedor()));
        nombreProveedor.setText(prov.getNombre());
        telefonoProveedor.setText(prov.getTelefono());
        nitProveedor.setText(prov.getNit());
    }

    public void agregarAlCarrito(Producto p){
        Producto productoCompra = p;
        ingresoCostoCantidad(encontrarCostoActual(productoCompra));
        if (!primerAdd) {
            JTextField proveedor = new JTextField();
            Object[] message = {
                "NIT:", proveedor,};
            int opcion = JOptionPane.showConfirmDialog(this, message, "Indicar Proveedor.", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                EntityManagerFactory emf = Conexion.getConexion().getEmf();
                Query q = emf.createEntityManager().createNamedQuery("Proveedor.findByNit");
                q.setParameter("nit", proveedor.getText());
                List<Proveedor> proveedorBusqueda = q.getResultList();
                if (proveedorBusqueda.isEmpty()) {
                    int crearProveedor = JOptionPane.showConfirmDialog(this, "¿Desea crear un proveedor nuevo?", "El proveedor no existe.", JOptionPane.OK_OPTION);
                    if (crearProveedor == JOptionPane.OK_OPTION) {
                        insertarProveedor(proveedor.getText(),p);
                    }
                } else {
                    primerAdd = true;
                    compra.setIdPersona(proveedorBusqueda.get(0).getIdProveedor());
                    mostrarDatosProveedor(proveedorBusqueda.get(0));
                    if (cantidad != 0) {
                        insertarCproducto(productoCompra);
                    }
                }
            }
        } else if (cantidad != 0) {
            insertarCproducto(productoCompra);
        }        
    }
    
    public void insertarCproducto(Producto prod) {
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
                    cantidad, costo));
            if (compra.getProductos().size() == 1) {
                commitCompra.setEnabled(true);
            }
            totalCompra.setText("Q. " + compra.getTotal());
        }
        mpv = new modeloProductosVenta(compra.getProductos(),1);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crearProducto = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        nombreProducto = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        descripcionProducto = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        precioProducto = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        categorias = new javax.swing.JComboBox<>();
        botonAgregarCategoria = new javax.swing.JButton();
        botonAceptar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codigoProveedor = new javax.swing.JTextField();
        nombreProveedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        telefonoProveedor = new javax.swing.JTextField();
        nitProveedor = new javax.swing.JTextField();
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
        modificarEntradaCarrito = new javax.swing.JButton();
        cancelar1 = new javax.swing.JButton();
        nuevoProducto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        crearProducto.setTitle("Datos de producto nuevo");
        crearProducto.setName("agregarCliente"); // NOI18N
        crearProducto.setResizable(false);

        jLabel11.setText("Nombre:");

        nombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreProductoKeyReleased(evt);
            }
        });

        jLabel13.setText("Descripción: (opcional)");

        descripcionProducto.setColumns(20);
        descripcionProducto.setRows(5);
        jScrollPane3.setViewportView(descripcionProducto);

        jLabel17.setText("Precio:");

        jLabel18.setText("Categoría:");

        categorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        botonAgregarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/addItem.png"))); // NOI18N
        botonAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarCategoriaActionPerformed(evt);
            }
        });

        botonAceptar.setText("Aceptar");
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout crearProductoLayout = new javax.swing.GroupLayout(crearProducto.getContentPane());
        crearProducto.getContentPane().setLayout(crearProductoLayout);
        crearProductoLayout.setHorizontalGroup(
            crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crearProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(nombreProducto)
                    .addComponent(precioProducto)
                    .addGroup(crearProductoLayout.createSequentialGroup()
                        .addGroup(crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel17))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, crearProductoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonAceptar)
                        .addGap(18, 18, 18)
                        .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(crearProductoLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(199, 199, 199))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, crearProductoLayout.createSequentialGroup()
                        .addComponent(categorias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonAgregarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        crearProductoLayout.setVerticalGroup(
            crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crearProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categorias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAgregarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(crearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonSalir)
                    .addComponent(botonAceptar))
                .addContainerGap())
        );

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setTitle("Compra");
        setPreferredSize(new java.awt.Dimension(790, 635));
        setRequestFocusEnabled(false);

        jPanel1.setBackground(new java.awt.Color(181, 232, 205));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Proveedor"));

        jLabel1.setText("Código");

        codigoProveedor.setEnabled(false);
        codigoProveedor.setName(""); // NOI18N

        nombreProveedor.setEnabled(false);

        jLabel2.setText("Nombre de proveedor");

        jLabel3.setText("Teléfono");

        telefonoProveedor.setEnabled(false);

        nitProveedor.setEnabled(false);

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
                            .addComponent(codigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(telefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(nitProveedor))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(telefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        codigoProveedor.getAccessibleContext().setAccessibleName("");

        jLabel5.setText("Fecha");

        numeroCompra.setEditable(false);
        numeroCompra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 11)); // NOI18N
        numeroCompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel6.setText("Número de compra");

        esAlCredito.setBackground(new java.awt.Color(181, 232, 205));
        esAlCredito.setText("Compra al crédito");
        esAlCredito.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                esAlCreditoStateChanged(evt);
            }
        });

        jLabel8.setText("Consulta Productos:");

        productoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productoBusquedaKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Categoría", "Costo Actual", "Existencias"
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
            jTable1.getColumnModel().getColumn(2).setMinWidth(60);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(3).setMinWidth(50);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(4).setMinWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Costo", "Subtotal"
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

        jLabel9.setFont(new java.awt.Font("Droid Serif", 3, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(109, 80, 9));
        jLabel9.setText("Productos a comprar");

        jLabel10.setText("TOTAL:");

        totalCompra.setEditable(false);
        totalCompra.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        totalCompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setText("Efectivo:");

        efectivo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/clear.png"))); // NOI18N
        cancelar.setText("Limpiar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        commitCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/addItem.png"))); // NOI18N
        commitCompra.setText("Registrar Compra");
        commitCompra.setEnabled(false);
        commitCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitCompraActionPerformed(evt);
            }
        });

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

        cancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cancelar.png"))); // NOI18N
        cancelar1.setText("Cancelar");

        nuevoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/addItem.png"))); // NOI18N
        nuevoProducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoProductoActionPerformed(evt);
            }
        });

        jLabel7.setText("Nuevo Producto");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(commitCompra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelar1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(numeroCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(esAlCredito))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(totalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarEntradaCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(agregarAlCarrito))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(productoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nuevoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7))
                            .addComponent(jLabel9))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nuevoProducto)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(productoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numeroCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(esAlCredito))
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(agregarAlCarrito))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(commitCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(modificarEntradaCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

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
        agregarAlCarrito(mp.obtenerProducto((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
    }//GEN-LAST:event_agregarAlCarritoActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        agregarAlCarrito.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseReleased

    private void modificarEntradaCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarEntradaCarritoActionPerformed
        // TODO add your handling code here:
        int opc = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Elija una acción.",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Quitar entrada", "Editar entrada", "Cancelar"}, "Editar entrada");
//        int opc = JOptionPane.showConfirmDialog(this, "Realmente quiere quitar el producto de esta compra?", "Confirmación de borrado", JOptionPane.OK_CANCEL_OPTION);
        if (opc == 0) {// para verificar si eligió editar la cant a vender o eliminar el producto del carrito
            compra.quitarProducto(jTable2.getSelectedRow());
            totalCompra.setText("Q. " + compra.getTotal());
            if (compra.getProductos().isEmpty()) {
                commitCompra.setEnabled(false);
            }
            mpv = new modeloProductosVenta(compra.getProductos(),1);
            jTable2.setModel(mpv);
            ajustarColumnas(jTable2);
            productoBusqueda.requestFocus();
            cargarProductos(productoBusqueda.getText());
        } else if (opc == 1) {
            EntityManagerFactory emf = Conexion.getConexion().getEmf();
            Query q = emf.createEntityManager().createNamedQuery("Producto.findByIdProducto");
            q.setParameter("idProducto", (Integer) jTable2.getValueAt(jTable2.getSelectedRow(), 0));
            ingresoCostoCantidad((Double) jTable2.getValueAt(jTable2.getSelectedRow(), 3));
            if (cantidad != 0) {
                compra.getProductos().get(jTable2.getSelectedRow()).setCantidad(cantidad);
                compra.getProductos().get(jTable2.getSelectedRow()).setPrecio(costo);
                totalCompra.setText("Q. " + compra.getTotal());
                mpv = new modeloProductosVenta(compra.getProductos(),1);
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

    private void commitCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitCompraActionPerformed
        // TODO add your handling code here:
        double total = compra.getTotal();
        boolean mayor = obtenerMonto() >= total;
        if (mayor) {
            compra.setPagoInicial(obtenerMonto());
            compra.finalizarCompra();
            commitCompra.setEnabled(false);
        } else if (compra.getCredito()) {
//          int opc = JOptionPane.showConfirmDialog(this, "¿Asignar como pago inicial?", "El pago indicado es insuficiente!",
//                 JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//          if (opc == JOptionPane.YES_OPTION) {
                compra.setPagoInicial(obtenerMonto());
                compra.finalizarCompra();
                commitCompra.setEnabled(false);
//      }
        } else {
            JOptionPane.showMessageDialog(this, "Intente efectuar la compra al crédito.", "El monto de efectivo es insuficiente!", JOptionPane.ERROR_MESSAGE);
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

    private void nuevoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoProductoActionPerformed
        // TODO add your handling code here:
        crearProducto.setLocation(550, 250);
        crearProducto.setSize(330, 360);
        crearProducto.setAlwaysOnTop(true);
        crearProducto.setVisible(true);
        mostrarCategorias();
    }//GEN-LAST:event_nuevoProductoActionPerformed

    private void nombreProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreProductoKeyReleased

    private void botonAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarCategoriaActionPerformed
        // TODO add your handling code here:
        crearCategoria();
        mostrarCategorias();
    }//GEN-LAST:event_botonAgregarCategoriaActionPerformed

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        // TODO add your handling code here:
        String registro = "";
        if (nombreProducto.getText().matches("[ ]*")) {
            registro = registro + "Nombre.\n\r";
        }
        try {
            Double.parseDouble(precioProducto.getText());
        } catch (NumberFormatException ex) {
            if (precioProducto.getText().matches("[ ]*")) {
                registro = registro + "Precio.\n\r";
            } else {
                registro = registro + "Precio. (No se permiten letras).\n\r";
            }
        }
        if (((String) categorias.getSelectedItem()).compareTo("Seleccione una categoría") == 0) {
            registro = registro + "Categoría.\n\r";
        }
        if (registro.compareTo("") == 0) {
            Query q = conexion.getEmf().createEntityManager().createNamedQuery("Producto.findByNombre");
            q.setParameter("nombre", nombreProducto.getText());
            if (q.getResultList().isEmpty()) {
                for (int i = 0; i < listaCategorias.size(); i++) {
                    if (listaCategorias.get(i).getNombre().compareTo((String) categorias.getSelectedItem()) == 0) {
                        idCategoria = listaCategorias.get(i).getIdCategoria();
                    }
                }
                if (descripcionProducto.getText() == null) {
                    descripcionProducto.setText("");
                }
                Producto nuevo = new Producto(nombreProducto.getText(), descripcionProducto.getText(), Double.parseDouble(precioProducto.getText()), idCategoria);
                controladorP.create(nuevo);
                crearProducto.setVisible(false);
                int opc = JOptionPane.showConfirmDialog(this, "¿Desea agregarlo a la compra?", "Producto creado exitosamente.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opc == JOptionPane.OK_OPTION){
                    encontrarCostoActual(nuevo);
                    agregarAlCarrito(nuevo);
                }
            } else {
                JOptionPane.showMessageDialog(crearProducto, "El producto ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(crearProducto, "Debe rellenar los siguientes campos:\n\r" + registro, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonAceptarActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        crearProducto.setVisible(false);
    }//GEN-LAST:event_botonSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarAlCarrito;
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonAgregarCategoria;
    private javax.swing.JButton botonSalir;
    private javax.swing.JButton cancelar;
    private javax.swing.JButton cancelar1;
    private javax.swing.JComboBox<String> categorias;
    private javax.swing.JTextField codigoProveedor;
    private javax.swing.JButton commitCompra;
    private javax.swing.JDialog crearProducto;
    private javax.swing.JTextArea descripcionProducto;
    private javax.swing.JTextField efectivo;
    private javax.swing.JCheckBox esAlCredito;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton modificarEntradaCarrito;
    private javax.swing.JTextField nitProveedor;
    private javax.swing.JTextField nombreProducto;
    private javax.swing.JTextField nombreProveedor;
    private javax.swing.JButton nuevoProducto;
    private javax.swing.JTextField numeroCompra;
    private javax.swing.JTextField precioProducto;
    private javax.swing.JTextField productoBusqueda;
    private javax.swing.JTextField telefonoProveedor;
    private javax.swing.JTextField totalCompra;
    // End of variables declaration//GEN-END:variables
}
