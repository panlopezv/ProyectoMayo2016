/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import conexion.Conexion;
import controladores.UsuarioJpaController;
import entidades.Usuario;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazPerfil extends javax.swing.JInternalFrame {

    Usuario us;
    UsuarioJpaController controladorU;
    /**
     * Creates new form InternoB
     * @param idUsuario
     */
    public InterfazPerfil(int idUsuario) {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorU = new UsuarioJpaController(Conexion.getConexion().getEmf());
        this.us = controladorU.findUsuario(idUsuario);
        cargarUsuario();
    }
    
    public void modificarUsuario(){
        JTextField usuario = new JTextField(us.getUsuario());
        JTextField nombre = new JTextField(us.getNombre());
        Object[] message = {
            "Usuario:", usuario,
            "Nombre:", nombre,
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
                modificarUsuario();
            }
            else{
                if(usuario.getText().compareTo(us.getUsuario())!=0){
                    Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Usuario.findByUsuario");
                    q.setParameter("usuario", usuario.getText()); 
                    if(q.getResultList().isEmpty()){
                        try {
                            us.setUsuario(usuario.getText());
                            us.setNombre(nombre.getText());
                            controladorU.edit(us);
                            JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            cargarUsuario();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "El usuario ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                        modificarUsuario();
                    }
                }
                else{
                    try {
                        us.setNombre(nombre.getText());
                        controladorU.edit(us);
                        JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                        cargarUsuario();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    public void modificarContrasenya(){
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
                modificarContrasenya();
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
    
    public void cargarUsuario(){
        campoUsuario.setText(us.getUsuario());
        campoNombre.setText(us.getNombre());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        campoUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        salir = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        cambiarContrasenya = new javax.swing.JButton();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Perfil");

        jLabel1.setText("Usuario:");

        jLabel2.setText("Nombre:");

        salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        modificar.setText("Modificar datos");
        modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarActionPerformed(evt);
            }
        });

        cambiarContrasenya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/editar.png"))); // NOI18N
        cambiarContrasenya.setText("Cambiar contraseña");
        cambiarContrasenya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarContrasenyaActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(campoUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 115, Short.MAX_VALUE))
                            .addComponent(campoNombre, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(modificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cambiarContrasenya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modificar))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cambiarContrasenya))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(salir)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_salirActionPerformed

    private void modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarActionPerformed
        // TODO add your handling code here:
        modificarUsuario();
    }//GEN-LAST:event_modificarActionPerformed

    private void cambiarContrasenyaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarContrasenyaActionPerformed
        // TODO add your handling code here:
        modificarContrasenya();
    }//GEN-LAST:event_cambiarContrasenyaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cambiarContrasenya;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton modificar;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
