/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import conexion.Conexion;
import controladores.UsuarioJpaController;
import entidades.Usuario;
import java.awt.Component;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazUsuarios extends javax.swing.JInternalFrame {

    ModeloUsuarios modelo;
    UsuarioJpaController controladorU;
    
    /**
     * Creates new form VistaUsuarios
     */
    public InterfazUsuarios() {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorU = new UsuarioJpaController(vistas.Inicio.conexion.getEmf());
        cargarUsuarios();
        
    }
    
    public void cargarUsuarios(){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Usuario.findAll");
        try {
            modelo = new ModeloUsuarios(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modelo = new ModeloUsuarios(new ArrayList<>());
        }
        tablaUsuarios.setModel(modelo);
        ajustarColumnas(tablaUsuarios);
    }
    
    public void crearUsuario(){
        JTextField usuario = new JTextField();
        JTextField nombre = new JTextField();
        JCheckBox esAdministrador = new JCheckBox("¿Es administrador?");
        JTextField contrasenya = new JPasswordField();
        JTextField verificacion = new JPasswordField();
        Object[] message = {
            "Usuario:", usuario,
            "Nombre:", nombre,
            "Contraseña:", contrasenya,
            "Verificar contraseña:", verificacion,
            esAdministrador,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Crear usuario.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String registro = "";
            if(usuario.getText()==null || usuario.getText().compareTo("")==0){
                registro+="Usuario.\n\r";
            }
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(contrasenya.getText()==null || contrasenya.getText().compareTo("")==0){
                registro+="Contraseña.\n\r";
            }
            if(verificacion.getText()==null || verificacion.getText().compareTo("")==0){
                registro+="Verificar contraseña.\n\r";
            }
            if(contrasenya.getText().compareTo(verificacion.getText())!=0){
                registro+="La contraseña debe coincidir con la verificación.\n\r";
            }
            if(contrasenya.getText().length()<4){
                registro+="La contraseña debe tener una longitud minima de 4 caracteres.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                crearUsuario();
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Usuario.findByUsuario");
                q.setParameter("usuario", usuario.getText());
                if(q.getResultList().isEmpty()){
                    Usuario nuevo = new Usuario(usuario.getText(), contrasenya.getText(), esAdministrador.isSelected(), nombre.getText());
                    controladorU.create(nuevo);
                    JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                }
                else{
                    JOptionPane.showMessageDialog(this, "El usuario ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearUsuario();
                }
            }
        }
    }
    
    public void modificarUsuario(Usuario us){
        JTextField usuario = new JTextField(us.getUsuario());
        JTextField nombre = new JTextField(us.getNombre());
        JCheckBox esAdministrador = new JCheckBox("¿Es administrador?",us.getEsAdministrador());
        Object[] message = {
            "Usuario:", usuario,
            "Nombre:", nombre,
            esAdministrador,
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Modificar usuario.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {String registro = "";
            if(usuario.getText()==null || usuario.getText().compareTo("")==0){
                registro+="Usuario.\n\r";
            }
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                registro+="Nombre.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                crearUsuario();
            }
            else{
                if(usuario.getText().compareTo(us.getUsuario())!=0){
                    Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Usuario.findByUsuario");
                    q.setParameter("usuario", usuario.getText()); 
                    if(q.getResultList().isEmpty()){
                        try {
                            us.setUsuario(usuario.getText());
                            us.setNombre(nombre.getText());
                            us.setEsAdministrador(esAdministrador.isSelected());
                            controladorU.edit(us);
                            JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            cargarUsuarios();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "El usuario ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                        modificarUsuario(us);
                    }
                }
                else{
                    try {
                        us.setNombre(nombre.getText());
                        us.setEsAdministrador(esAdministrador.isSelected());
                        controladorU.edit(us);
                        JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        cargarUsuarios();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    public void modificarContrasenya(Usuario us){
        JTextField contrasenya = new JPasswordField();
        JTextField verificacion = new JPasswordField();
        Object[] message = {
            "Usuario: " + us.getUsuario() + ".",
            "Contraseña:", contrasenya,
            "Verificar contraseña:", verificacion,
        };
        
        int opcion = JOptionPane.showConfirmDialog(this, message, "Modificar contraseña.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
             String registro = "";
            if(contrasenya.getText()==null || contrasenya.getText().compareTo("")==0){
                registro+="Contraseña.\n\r";
            }
            if(verificacion.getText()==null || verificacion.getText().compareTo("")==0){
                registro+="Verificar contraseña.\n\r";
            }
            if(contrasenya.getText().compareTo(verificacion.getText())!=0){
                registro+="La contraseña debe coincidir con la verificación.\n\r";
            }
            if(contrasenya.getText().length()<4){
                registro+="La contraseña debe tener una longitud minima de 4 caracteres.\n\r";
            }
            if(registro.compareTo("")!=0){
                JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
                modificarContrasenya(us);
            }
            else{
                try {
                    us.setContrasenya(contrasenya.getText());
                    controladorU.edit(us);
                    JOptionPane.showMessageDialog(this, "Contraseña modificada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        botonCrear = new javax.swing.JButton();
        botonModificar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        botonContrasenya = new javax.swing.JButton();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Usuarios");

        jScrollPane1.setViewportView(tablaUsuarios);

        jLabel1.setText("Usuarios:");

        botonCrear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/add.png"))); // NOI18N
        botonCrear.setText("Crear");
        botonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearActionPerformed(evt);
            }
        });

        botonModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        botonModificar.setText("Modificar");
        botonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarActionPerformed(evt);
            }
        });

        botonSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        botonContrasenya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        botonContrasenya.setText("Contraseña");
        botonContrasenya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonContrasenyaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonContrasenya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonCrear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonModificar)
                        .addGap(28, 28, 28)
                        .addComponent(botonContrasenya)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(botonSalir))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearActionPerformed
        // TODO add your handling code here:
        crearUsuario();
    }//GEN-LAST:event_botonCrearActionPerformed

    private void botonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarActionPerformed
        // TODO add your handling code here:
        int fila=tablaUsuarios.getSelectedRow();
        if(fila>=0){
            modificarUsuario(((ModeloUsuarios)tablaUsuarios.getModel()).getUsuario(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un usuario.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonModificarActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_botonSalirActionPerformed

    private void botonContrasenyaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonContrasenyaActionPerformed
        // TODO add your handling code here:
        int fila=tablaUsuarios.getSelectedRow();
        if(fila>=0){
           modificarContrasenya(((ModeloUsuarios)tablaUsuarios.getModel()).getUsuario(fila));
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un usuario.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonContrasenyaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonContrasenya;
    private javax.swing.JButton botonCrear;
    private javax.swing.JButton botonModificar;
    private javax.swing.JButton botonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaUsuarios;
    // End of variables declaration//GEN-END:variables
}
