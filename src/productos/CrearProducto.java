/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productos;

import conexion.Observador;
import controladores.CategoriaJpaController;
import controladores.ProductoJpaController;
import entidades.Categoria;
import entidades.Producto;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import vistas.Inicio;
import static vistas.Inicio.conexion;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class CrearProducto extends javax.swing.JInternalFrame implements Observer {

    int idCategoria=0;
    CategoriaJpaController controladorC;
    ProductoJpaController controladorP;
    List<Categoria> listaCategorias;
    Observador op;
    
    /**
     * Creates new form InternoB
     * @param op
     */
    public CrearProducto(Observador op) {
        initComponents();
        setVisible(Boolean.TRUE);
        this.op = op;
        controladorC = new CategoriaJpaController(Inicio.conexion.getEmf());
        controladorP = new ProductoJpaController(Inicio.conexion.getEmf());
        mostrarCategorias();
    }
    
    /**
     * Creates new form InternoB
     */
    public CrearProducto() {
        initComponents();
        setVisible(Boolean.TRUE);
        controladorC = new CategoriaJpaController(Inicio.conexion.getEmf());
        controladorP = new ProductoJpaController(Inicio.conexion.getEmf());
        mostrarCategorias();
    }

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
                Query q = conexion.getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
                q.setParameter("nombre", nombre.getText());
                if(q.getResultList().isEmpty()){
                    Categoria nueva = new Categoria(nombre.getText());
                    controladorC.create(nueva);
                    idCategoria = nueva.getIdCategoria();
                }
                else{
                    JOptionPane.showMessageDialog(this, "La categoría ingresada ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    crearCategoria();
                }
            }
        }
    }
    
    public void mostrarCategorias(){
        listaCategorias = controladorC.findCategoriaEntities();
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("Seleccione una categoría");
        for(int i=0;i<listaCategorias.size();i++){
            modelo.addElement(listaCategorias.get(i).getNombre());
            if(idCategoria!=0){
                if(listaCategorias.get(i).getIdCategoria()==idCategoria){
                    modelo.setSelectedItem(listaCategorias.get(i).getNombre());
                }
            }
        }
        categorias.setModel(modelo);
    }
    
    public void limpiarCampos(){
        idCategoria = 0;
        nombreProducto.setText("");
        descripcionProducto.setText("");
        precioProducto.setText("");
        mostrarCategorias();
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
        nombreProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcionProducto = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        precioProducto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        categorias = new javax.swing.JComboBox<>();
        botonSalir = new javax.swing.JButton();
        botonAceptar = new javax.swing.JButton();
        botonAgregarCategoria = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 153));
        setClosable(true);
        setTitle("Crear producto");

        jLabel1.setText("Nombre:");

        nombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreProductoKeyReleased(evt);
            }
        });

        jLabel2.setText("Descripción:");

        descripcionProducto.setColumns(20);
        descripcionProducto.setRows(5);
        jScrollPane1.setViewportView(descripcionProducto);

        jLabel3.setText("Precio:");

        jLabel4.setText("Categoría:");

        categorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        botonAceptar.setText("Aceptar");
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonAgregarCategoria.setText("+");
        botonAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(nombreProducto)
                    .addComponent(precioProducto)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 234, Short.MAX_VALUE)
                        .addComponent(botonAceptar)
                        .addGap(18, 18, 18)
                        .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(categorias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonAgregarCategoria)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(precioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categorias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAgregarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonSalir)
                    .addComponent(botonAceptar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarCategoriaActionPerformed
        // TODO add your handling code here:
        crearCategoria();
        mostrarCategorias();
    }//GEN-LAST:event_botonAgregarCategoriaActionPerformed

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        // TODO add your handling code here:
        String registro = "";
        if(nombreProducto.getText().matches("[ ]*")){
            registro=registro + "Nombre.\n\r";
        }
        try{
            Double.parseDouble(precioProducto.getText());
        }
        catch(NumberFormatException ex){
            if(precioProducto.getText().matches("[ ]*")){
                registro=registro + "Precio.\n\r";
            }
            else{
                registro=registro + "Precio. (No se permiten letras).\n\r";
            }
        }
        if(((String)categorias.getSelectedItem()).compareTo("Seleccione una categoría")==0){
            registro=registro + "Categoría.\n\r";
        }
        if(registro.compareTo("")==0){
            Query q = conexion.getEmf().createEntityManager().createNamedQuery("Producto.findByNombre");
            q.setParameter("nombre", nombreProducto.getText());
            if(q.getResultList().isEmpty()){
                for(int i = 0;i<listaCategorias.size();i++){
                    if(listaCategorias.get(i).getNombre().compareTo((String)categorias.getSelectedItem())==0){
                        idCategoria=listaCategorias.get(i).getIdCategoria();
                    }
                }
                if(descripcionProducto.getText()==null){
                    descripcionProducto.setText("");
                }
                Producto nuevo = new Producto(nombreProducto.getText(), descripcionProducto.getText(), Double.parseDouble(precioProducto.getText()), idCategoria);
                controladorP.create(nuevo);
                JOptionPane.showMessageDialog(this, "Producto creado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
                if(op!=null){
                    op.deleteObserver(this);
                    op.actualizarObservadores();
                }
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(this, "El producto ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Debe rellenar los siguientes campos:\n\r"+registro, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonAceptarActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_botonSalirActionPerformed

    private void nombreProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreProductoKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonAgregarCategoria;
    private javax.swing.JButton botonSalir;
    private javax.swing.JComboBox<String> categorias;
    private javax.swing.JTextArea descripcionProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nombreProducto;
    private javax.swing.JTextField precioProducto;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
