/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package categorias;

import conexion.Conexion;
import controladores.CategoriaJpaController;
import entidades.Categoria;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazCategorias extends javax.swing.JInternalFrame {

    CategoriaJpaController controladorC;
    ModeloCategorias modeloC;
    /**
     * Creates new form InterfazCategorias
     */
    public InterfazCategorias() {
        initComponents();
        controladorC = new CategoriaJpaController(vistas.Inicio.conexion.getEmf());
        cargarCategorias();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCategorias = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 153));
        setClosable(true);
        setMaximizable(true);
        setTitle("Categorías");

        jLabel1.setText("Categorías:");

        jScrollPane1.setViewportView(tablaCategorias);

        jButton4.setText("Crear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Salir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        crearCategoria();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int fila=tablaCategorias.getSelectedRow();
        if(fila>=0){
            modificarCategoria(modeloC.getCategoria(fila).getNombre());
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar una categoría.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed
    
    public void crearCategoria(){
        JTextField nombre = new JTextField();
        Object[] message = {
            "Nombre:", nombre
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Crear categoría.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                crearCategoria();
            }
            else{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
                q.setParameter("nombre", nombre.getText());
                if(q.getResultList().isEmpty()){
                    Categoria nueva = new Categoria(nombre.getText());
                    controladorC.create(nueva);
                    JOptionPane.showMessageDialog(this, "Categoría creada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                    cargarCategorias();
                }
                else{
                    JOptionPane.showMessageDialog(this, "La categoría ingresada ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearCategoria();
                }
            }
        }
    }
     
    public void modificarCategoria(String nombreCategoria){
        JTextField nombre = new JTextField(nombreCategoria);
        Object[] message = {
            "Nombre:", nombre
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Modificar categoría.", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if(nombre.getText()==null || nombre.getText().compareTo("")==0){
                modificarCategoria(nombreCategoria);
            }
            else{
                if(nombre.getText().compareTo(nombreCategoria)!=0){
                    Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
                    q.setParameter("nombre", nombre.getText()); 
                    if(q.getResultList().isEmpty()){
                        try {
                            q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
                            q.setParameter("nombre", nombreCategoria);
                            Categoria aModificar= (Categoria) q.getResultList().get(0);
                            aModificar.setNombre(nombre.getText());
                            controladorC.edit(aModificar);
                            JOptionPane.showMessageDialog(this, "Categoría modificada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                            cargarCategorias();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Algo no ha salido bien, intentelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "La categoría ingresada ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                        modificarCategoria(nombreCategoria);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "Categoría modificada exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                    cargarCategorias();
                }
            }
        }
    }
    
     public void cargarCategorias(){
        setVisible(Boolean.TRUE);
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findAll");
        try {
            modeloC = new ModeloCategorias(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modeloC = new ModeloCategorias(new ArrayList<>());
        }
        tablaCategorias.setModel(modeloC);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCategorias;
    // End of variables declaration//GEN-END:variables
}