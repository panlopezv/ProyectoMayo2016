/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario;

import conexion.Conexion;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.persistence.Query;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import productos.CrearProducto;
import conexion.Observador;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import productos.ModificarProducto;
import productos.VerProducto;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class InterfazInventario extends javax.swing.JInternalFrame implements Observer{
    ModeloProductosInventario modelo;
    Observador observador = new Observador();
    public static Boolean secundario;
    CrearProducto cp;
    VerProducto vp;
    ModificarProducto mp;
    /**
     * Creates new form InternoB
     */
    public InterfazInventario() {
        initComponents();
        observador.addObserver(this);
        cargarProductos();
        secundario = Boolean.FALSE;
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
    
    public void cargarProductos(){
        
        setVisible(Boolean.TRUE);
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Producto.findAll");
        try {
            modelo = new ModeloProductosInventario(new ArrayList<>(q.getResultList()));
        } catch (Exception ex) {
            modelo = new ModeloProductosInventario(new ArrayList<>());
        }
        tablaProductos.setModel(modelo);
        ajustarColumnas(tablaProductos);
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
        tablaProductos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 153));
        setClosable(true);
        setMaximizable(true);
        setTitle("Inventario");

        jLabel1.setText("Productos:");

        jScrollPane1.setViewportView(tablaProductos);

        jButton1.setText("Ver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        jButton4.setText("Crear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        cerrarComponentes();
        cp = new CrearProducto(observador);
        observador.addObserver(cp);
        this.getParent().add(cp);
        cp.setLocation(this.getX()+(this.getWidth()/2 - cp.getWidth()/2), this.getY()+(this.getHeight()/2 - cp.getHeight()/2));
        cp.toFront();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int fila=tablaProductos.getSelectedRow();
        if(fila>=0){
            cerrarComponentes();
            ModeloProductosInventario modeloP =(ModeloProductosInventario) tablaProductos.getModel();
            vp = new VerProducto(modeloP.getProducto(fila), (String) modeloP.getValueAt(fila, 3));
            this.getParent().add(vp);
            vp.setLocation(this.getX()+(this.getWidth()/2 - vp.getWidth()/2), this.getY()+(this.getHeight()/2 - vp.getHeight()/2));
            vp.toFront();
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un producto.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int fila=tablaProductos.getSelectedRow();
        if(fila>=0){
            cerrarComponentes();
            ModeloProductosInventario modeloP =(ModeloProductosInventario) tablaProductos.getModel();
            mp = new ModificarProducto(observador, modeloP.getProducto(fila));
            observador.addObserver(mp);
            this.getParent().add(mp);
            mp.setLocation(this.getX()+(this.getWidth()/2 - mp.getWidth()/2), this.getY()+(this.getHeight()/2 - mp.getHeight()/2));
            mp.toFront();
        }
        else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un producto.","",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        cargarProductos();
    }
    
    public void cerrarComponentes(){
        try{observador.deleteObserver(mp);}
        catch(Exception ex){}
        cerrarComponente(mp);
        cerrarComponente(vp);
        try{observador.deleteObserver(cp);}
        catch(Exception ex){}
        cerrarComponente(cp);
    }
    public void cerrarComponente(JInternalFrame jif){
        try{jif.dispose();}
        catch (Exception ex){}
    }
}
