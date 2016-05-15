/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personas;

import conexion.Conexion;
import controladores.ClienteJpaController;
import controladores.ProveedorJpaController;
import entidades.Cliente;
import entidades.Proveedor;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazPersonas extends javax.swing.JInternalFrame {

    ModeloClientes modeloC;
    ModeloProveedores modeloP;
    ClienteJpaController controladorC;
    ProveedorJpaController controladorP;
    /**
     * Creates new form InternoB
     */
    public InterfazPersonas() {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorC = new ClienteJpaController(Conexion.getConexion().getEmf());
        cargarClientes();
        if(Conexion.getConexion().getEsAdministrador()){
            cargarProveedores();
            controladorP = new ProveedorJpaController(Conexion.getConexion().getEmf());
        }
        else{
            jTabbedPersonas.setEnabledAt(1, Boolean.FALSE);
        }
    }
    
    public void cargarClientes(){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findAll");
        try {
            modeloC = new ModeloClientes(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modeloC = new ModeloClientes(new ArrayList<>());
        }
        tablaClientes.setModel(modeloC);
        ajustarColumnas(tablaClientes);
    }
    
    public void buscarCliente(String nombre){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findLikeNombre");
        q.setParameter("nombre", nombre + "%");
        try {
            modeloC = new ModeloClientes(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modeloC = new ModeloClientes(new ArrayList<>());
        }
        tablaClientes.setModel(modeloC);
        ajustarColumnas(tablaClientes);
    }
    
    public void cargarProveedores(){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findAll");
        try {
            modeloP = new ModeloProveedores(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modeloP = new ModeloProveedores(new ArrayList<>());
        }
        tablaProveedores.setModel(modeloP);
        ajustarColumnas(tablaProveedores);
    }
    
    public void buscarProveedor(String nombre){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findLikeNombre");
        q.setParameter("nombre", nombre + "%");
        try {
            modeloP = new ModeloProveedores(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modeloP = new ModeloProveedores(new ArrayList<>());
        }
        tablaProveedores.setModel(modeloP);
        ajustarColumnas(tablaProveedores);
    }
    
    public void crearCliente(){
        JTextField nombre = new JTextField();
        JTextField direccion = new JTextField();
        JTextField nit = new JTextField();
        JTextField telefono = new JTextField();
        Object[] message = {
            "Nombre:", nombre,
            "Dirección:", direccion,
            "NIT:", nit,
            "Teléfono: (opcional)", telefono,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Crear cliente.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(direccion.getText()==null || direccion.getText().compareTo("")==0){
                registro+="Dirección.\n\r";
            }
            if(nit.getText()==null || nit.getText().compareTo("")==0){
                registro+="NIT.\n\r";
            }
            else if(!validarNIT(nit.getText())){
                    registro+="El NIT no es válido.\n\r";
            }
            if(telefono.getText()!=null && telefono.getText().compareTo("")!=0){
                if(!validarTelefono(telefono.getText()))
                    registro+="El teléfono debe poseer solo 8 dígitos.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                crearCliente();
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findByNit");
                q.setParameter("nit", nit.getText());
                if(!nit.getText().matches("CF|cf|C/F|c/f|c.f.|C.F.")||q.getResultList().isEmpty()){
                    controladorC.create(new Cliente(nombre.getText(), direccion.getText(), nit.getText(), telefono.getText()));
                    JOptionPane.showMessageDialog(this, "Cliente creado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                    cargarClientes();
                }
                else{
                    JOptionPane.showMessageDialog(this, "El cliente con el NIT ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearCliente();
                }
            }
        }
    }
    
    public void crearProveedor(){
        JTextField nombre = new JTextField();
        JTextField nit = new JTextField();
        JTextField telefono = new JTextField();
        Object[] message = {
            "Nombre:", nombre,
            "NIT:", nit,
            "Teléfono: (opcional)", telefono,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Crear proveedor.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(nit.getText()==null || nit.getText().compareTo("")==0){
                registro+="NIT.\n\r";
            }
            else if(!validarNIT(nit.getText())){
                    registro+="El NIT no es válido.\n\r";
            }
            if(telefono.getText()!=null && telefono.getText().compareTo("")!=0){
                if(!validarTelefono(telefono.getText()))
                    registro+="El teléfono debe poseer solo 8 dígitos.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                crearProveedor();
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findByNit");
                q.setParameter("nit", nit.getText());
                if(nit.getText().matches("CF|cf|C/F|c/f|c.f.|C.F.")||q.getResultList().isEmpty()){
                    controladorP.create(new Proveedor(nombre.getText(),nit.getText(),telefono.getText()));
                    JOptionPane.showMessageDialog(this, "Proveedor creado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                    cargarProveedores();
                }
                else{
                    JOptionPane.showMessageDialog(this, "El proveedor con el NIT ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearProveedor();
                }
            }
        }
    }
    
    public void verCliente(Cliente cl){
        JTextField nombre = new JTextField(cl.getNombre());
        JTextField direccion = new JTextField(cl.getDireccion());
        JTextField nit = new JTextField(cl.getNit());
        JTextField telefono = new JTextField(cl.getTelefono());
        JTextField saldo = new JTextField("Q "+new DecimalFormat("Q#,###.00").format(cl.getSaldo()));
        Object[] message = {
            "Nombre:", nombre,
            "Dirección:", direccion,
            "NIT:", nit,
            "Teléfono:", telefono,
            "Saldo:", saldo,
        };
        JOptionPane.showMessageDialog(this, message, "Ver cliente.", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void verProveedor(Proveedor pr){
        JTextField nombre = new JTextField(pr.getNombre());
        JTextField nit = new JTextField(pr.getNit());
        JTextField telefono = new JTextField(pr.getTelefono());
        JTextField saldo = new JTextField("Q "+new DecimalFormat("Q#,###.00").format(pr.getSaldo()));
        Object[] message = {
            "Nombre:", nombre,
            "NIT:", nit,
            "Teléfono:", telefono,
            "Saldo:", saldo,
        };
        JOptionPane.showMessageDialog(this, message, "Ver proveedor.", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void modificarCliente(Cliente c){
        JTextField nombre = new JTextField(c.getNombre());
        JTextField direccion = new JTextField(c.getDireccion());
        JTextField nit = new JTextField(c.getNit());
        JTextField telefono = new JTextField(c.getTelefono());
        Object[] message = {
            "Nombre:", nombre,
            "Dirección:", direccion,
            "NIT:", nit,
            "Teléfono: (opcional)", telefono,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Modificar cliente.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(direccion.getText()==null || direccion.getText().compareTo("")==0){
                registro+="Dirección.\n\r";
            }
            if(nit.getText()==null || nit.getText().compareTo("")==0){
                registro+="NIT.\n\r";
            }
            else if(!validarNIT(nit.getText())){
                    registro+="El NIT no es válido.\n\r";
            }
            if(telefono.getText()!=null && telefono.getText().compareTo("")!=0){
                if(!validarTelefono(telefono.getText()))
                    registro+="El teléfono debe poseer solo 8 dígitos.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                modificarCliente(c);
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findByNit");
                q.setParameter("nit", nit.getText());
                if(nit.getText().matches("CF|cf|C/F|c/f|c.f.|C.F.")||q.getResultList().isEmpty()||nit.getText().compareTo(c.getNit())==0){
                    try {
                        c.setNombre(nombre.getText());
                        c.setDireccion(direccion.getText());
                        c.setNit(nit.getText());
                        c.setTelefono(telefono.getText());
                        controladorC.edit(c);
                        JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        cargarClientes();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "El cliente con el NIT ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    modificarCliente(c);
                }
            }
        }
    }
    
     public void modificarProveedor(Proveedor p){
        JTextField nombre = new JTextField(p.getNombre());
        JTextField nit = new JTextField(p.getNit());
        JTextField telefono = new JTextField(p.getTelefono());
        Object[] message = {
            "Nombre:", nombre,
            "NIT:", nit,
            "Teléfono: (opcional)", telefono,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Modificar proveedor.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(nit.getText()==null || nit.getText().compareTo("")==0){
                registro+="NIT.\n\r";
            }
            else if(!validarNIT(nit.getText())){
                    registro+="El NIT no es válido.\n\r";
            }
            if(telefono.getText()!=null && telefono.getText().compareTo("")!=0){
                if(!validarTelefono(telefono.getText()))
                    registro+="El teléfono debe poseer solo 8 dígitos.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                modificarProveedor(p);
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findByNit");
                q.setParameter("nit", nit.getText());
                if(nit.getText().matches("CF|cf|C/F|c/f|c.f.|C.F.")||q.getResultList().isEmpty()||nit.getText().compareTo(p.getNit())==0){
                    try {
                        p.setNombre(nombre.getText());
                        p.setNit(nit.getText());
                        p.setTelefono(telefono.getText());
                        controladorP.edit(p);
                        JOptionPane.showMessageDialog(this, "Proveedor modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        cargarProveedores();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "El Proveedor con el NIT ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    modificarProveedor(p);
                }
            }
        }
    }
     
    /**
     * Metodo para validar el NIT.
     * Acepta el NIT si el resto de la sumatoria de los digitos del nit * su posicion -1 MOD 11 = ultimo digito.
     * Se aceptan como validos los NIT para consumidor final.
     * @param numero
     * @return 
     */
    public Boolean validarNIT(String numero){
        Boolean valido = Boolean.FALSE;
        if(numero.matches("[0-9]+-[0-9]")){
//            String correlativo = numero.split("-")[0];
//            String verificador = numero.split("-")[1];
//            int sumatoria = 0;
//            for(int i = 0; i<correlativo.length();i++){
//                sumatoria += Integer.parseInt(correlativo.charAt(i)+"")*(numero.length()-1-i);
//            }
//            if((sumatoria%11)==Integer.parseInt(verificador)){
                valido = Boolean.TRUE;
//            }
        }
        else if(numero.matches("([0-9]+-(|K|k))")){
            valido = Boolean.TRUE;
        }
        return valido;
    }
    
    /**
     * Metodo para validar un numero telefonico.
     * Debe cumplir con la condicion de no tener caracteres y tener longitud 8.
     * @param numero
     * @return 
     */
    public Boolean validarTelefono(String numero){
        Boolean valido = Boolean.FALSE;
        try{
            Integer.parseInt(numero);
            if(numero.length()==8){
                valido = Boolean.TRUE;
            }
        }catch(NumberFormatException ex){}
        return valido;
    }
    
    public void ajustarColumnas(JTable tabla){
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (int column = 0; column < tabla.getColumnCount(); column++)
        {
            TableColumn tableColumn = tabla.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();
            for (int row = 0; row < tabla.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
                Component c = tabla.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tabla.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
                if (preferredWidth >= maxWidth)
                {
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
            tableColumn.setPreferredWidth( preferredWidth );
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

        jTabbedPersonas = new javax.swing.JTabbedPane();
        panelClientes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        botonCrearCliente = new javax.swing.JButton();
        botonVerCliente = new javax.swing.JButton();
        botonModificarCliente = new javax.swing.JButton();
        botonSalirCliente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        busquedaCliente = new javax.swing.JTextField();
        panelProveedores = new javax.swing.JPanel();
        botonCrearProveedor = new javax.swing.JButton();
        botonVerProveedor = new javax.swing.JButton();
        botonModificarProveedor = new javax.swing.JButton();
        botonSalirProveedor = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        busquedaProveedor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setTitle("Personas");

        jScrollPane1.setViewportView(tablaClientes);

        botonCrearCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/add.png"))); // NOI18N
        botonCrearCliente.setText("Crear");
        botonCrearCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearClienteActionPerformed(evt);
            }
        });

        botonVerCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        botonVerCliente.setText("Ver");
        botonVerCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerClienteActionPerformed(evt);
            }
        });

        botonModificarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        botonModificarCliente.setText("Modificar");
        botonModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarClienteActionPerformed(evt);
            }
        });

        botonSalirCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        botonSalirCliente.setText("Salir");
        botonSalirCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirClienteActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar:");

        busquedaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClientesLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(botonModificarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonCrearCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonVerCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonSalirCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelClientesLayout.createSequentialGroup()
                        .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(busquedaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 219, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelClientesLayout.setVerticalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(busquedaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClientesLayout.createSequentialGroup()
                        .addComponent(botonVerCliente)
                        .addGap(18, 18, 18)
                        .addComponent(botonCrearCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(botonSalirCliente))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPersonas.addTab("Clientes", panelClientes);

        botonCrearProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/add.png"))); // NOI18N
        botonCrearProveedor.setText("Crear");
        botonCrearProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearProveedorActionPerformed(evt);
            }
        });

        botonVerProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        botonVerProveedor.setText("Ver");
        botonVerProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerProveedorActionPerformed(evt);
            }
        });

        botonModificarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        botonModificarProveedor.setText("Modificar");
        botonModificarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarProveedorActionPerformed(evt);
            }
        });

        botonSalirProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        botonSalirProveedor.setText("Salir");
        botonSalirProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirProveedorActionPerformed(evt);
            }
        });

        jLabel2.setText("Buscar:");

        busquedaProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaProveedorKeyReleased(evt);
            }
        });

        jScrollPane3.setViewportView(tablaProveedores);

        javax.swing.GroupLayout panelProveedoresLayout = new javax.swing.GroupLayout(panelProveedores);
        panelProveedores.setLayout(panelProveedoresLayout);
        panelProveedoresLayout.setHorizontalGroup(
            panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProveedoresLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonModificarProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonCrearProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonVerProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonSalirProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelProveedoresLayout.createSequentialGroup()
                        .addGroup(panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(busquedaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 219, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelProveedoresLayout.setVerticalGroup(
            panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(busquedaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProveedoresLayout.createSequentialGroup()
                        .addComponent(botonVerProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonCrearProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(botonSalirProveedor))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPersonas.addTab("Proveedores", panelProveedores);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPersonas)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPersonas)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCrearClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearClienteActionPerformed
        // TODO add your handling code here:
        crearCliente();
    }//GEN-LAST:event_botonCrearClienteActionPerformed

    private void botonVerClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerClienteActionPerformed
        // TODO add your handling code here:
        int fila=tablaClientes.getSelectedRow();
        if(fila>=0){
            verCliente(((ModeloClientes)tablaClientes.getModel()).getCliente(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un cliente.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonVerClienteActionPerformed

    private void botonModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarClienteActionPerformed
        // TODO add your handling code here:
        int fila=tablaClientes.getSelectedRow();
        if(fila>=0){
            modificarCliente(((ModeloClientes)tablaClientes.getModel()).getCliente(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un cliente.","",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_botonModificarClienteActionPerformed

    private void botonSalirClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirClienteActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_botonSalirClienteActionPerformed

    private void botonCrearProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearProveedorActionPerformed
        // TODO add your handling code here:
        crearProveedor();
    }//GEN-LAST:event_botonCrearProveedorActionPerformed

    private void botonVerProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerProveedorActionPerformed
        // TODO add your handling code here:
        int fila=tablaProveedores.getSelectedRow();
        if(fila>=0){
            verProveedor(((ModeloProveedores)tablaProveedores.getModel()).getProveedor(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un proveedor.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonVerProveedorActionPerformed

    private void botonModificarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarProveedorActionPerformed
        // TODO add your handling code here:
        int fila=tablaProveedores.getSelectedRow();
        if(fila>=0){
            modificarProveedor(((ModeloProveedores)tablaProveedores.getModel()).getProveedor(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un proveedor.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonModificarProveedorActionPerformed

    private void botonSalirProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirProveedorActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_botonSalirProveedorActionPerformed

    private void busquedaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busquedaClienteKeyReleased
        // TODO add your handling code here:
        if (busquedaCliente.getText().compareTo("") != 0) {
            buscarCliente(busquedaCliente.getText());
        }
        else{
            cargarClientes();
        }
    }//GEN-LAST:event_busquedaClienteKeyReleased

    private void busquedaProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busquedaProveedorKeyReleased
        // TODO add your handling code here:
        if (busquedaProveedor.getText().compareTo("") != 0) {
            buscarProveedor(busquedaProveedor.getText());
        }
        else{
            cargarProveedores();
        }
    }//GEN-LAST:event_busquedaProveedorKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCrearCliente;
    private javax.swing.JButton botonCrearProveedor;
    private javax.swing.JButton botonModificarCliente;
    private javax.swing.JButton botonModificarProveedor;
    private javax.swing.JButton botonSalirCliente;
    private javax.swing.JButton botonSalirProveedor;
    private javax.swing.JButton botonVerCliente;
    private javax.swing.JButton botonVerProveedor;
    private javax.swing.JTextField busquedaCliente;
    private javax.swing.JTextField busquedaProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPersonas;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelProveedores;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaProveedores;
    // End of variables declaration//GEN-END:variables
}
