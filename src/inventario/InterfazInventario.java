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
import entidades.Categoria;
import entidades.Detallecompra;
import entidades.Producto;
import java.text.DecimalFormat;
import java.util.List;
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
    CrearProducto cp;
    VerProducto vp;
    ModificarProducto mp;
    
    /**
     * Creates new form InterfazInventario
     */
    public InterfazInventario() {
        initComponents();
        observador.addObserver(this);
        cargarProductos();
        if(!Conexion.getConexion().getEsAdministrador()){
            botonCrear.setVisible(Boolean.FALSE);
            botonModificar.setVisible(Boolean.FALSE);
            jLabel2.setVisible(Boolean.FALSE);
            capitalInvertido.setVisible(Boolean.FALSE);
        }
        else{
            encontrarCapitalInvertido();
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

    private void buscarProducto(String valor) {
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Producto.findLikeNombre");
        q.setParameter("nombre", valor + "%");
        List productosBusqueda = q.getResultList();
        if(!productosBusqueda.isEmpty()){
            modelo = new ModeloProductosInventario(new ArrayList<>(productosBusqueda));
        }
        else{
            Query q2 = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findByNombre");
            q2.setParameter("nombre", valor);
            Categoria categoriaBusqueda = (Categoria) q2.getSingleResult();
            if(q2.getSingleResult()!=null){
                q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Producto.findByIdCategoria");
                q.setParameter("idCategoria", categoriaBusqueda.getIdCategoria());
                modelo = new ModeloProductosInventario(new ArrayList<>(q.getResultList()));
            }
            else{
                modelo = new ModeloProductosInventario(new ArrayList<>());
            }
        }
        tablaProductos.setModel(modelo);
        ajustarColumnas(tablaProductos);
    }
   
    public void encontrarCapitalInvertido(){
        double ci=0.0;
        for(int i=0;i<modelo.getProductos().size();i++){
            ci+=encontrarCostoActual(modelo.getProducto(i));
        }
        capitalInvertido.setText(new DecimalFormat("Q#,##0.00").format(ci));
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
            return ((Detallecompra) q.getSingleResult()).getCosto()*p.getExistencias();
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        botonVer = new javax.swing.JButton();
        botonModificar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        botonCrear = new javax.swing.JButton();
        busquedaProducto = new javax.swing.JTextField();
        capitalInvertido = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(181, 232, 205));
        setClosable(true);
        setMaximizable(true);
        setTitle("Inventario");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jLabel1.setText("Buscar:");

        jScrollPane1.setViewportView(tablaProductos);

        botonVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/ver.png"))); // NOI18N
        botonVer.setText("Ver");
        botonVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerActionPerformed(evt);
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

        botonCrear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/addItem.png"))); // NOI18N
        botonCrear.setText("Crear");
        botonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearActionPerformed(evt);
            }
        });

        busquedaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaProductoKeyReleased(evt);
            }
        });

        capitalInvertido.setEditable(false);

        jLabel2.setText("Capital apróximado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(busquedaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(capitalInvertido, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonCrear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonVer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(busquedaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capitalInvertido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonVer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearActionPerformed
        // TODO add your handling code here:
        cerrarComponentes();
        cp = new CrearProducto(observador);
        observador.addObserver(cp);
        this.getParent().add(cp);
        cp.setLocation(this.getX()+(this.getWidth()/2 - cp.getWidth()/2), this.getY()+(this.getHeight()/2 - cp.getHeight()/2));
        cp.toFront();
    }//GEN-LAST:event_botonCrearActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        cerrarComponentes();
        this.dispose();
    }//GEN-LAST:event_botonSalirActionPerformed

    private void botonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerActionPerformed
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
    }//GEN-LAST:event_botonVerActionPerformed

    private void botonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarActionPerformed
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
    }//GEN-LAST:event_botonModificarActionPerformed

    private void busquedaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busquedaProductoKeyReleased
        // TODO add your handling code here:
        if (busquedaProducto.getText().compareTo("") != 0) {
            buscarProducto(busquedaProducto.getText());
        }
        else{
            cargarProductos();
        }
    }//GEN-LAST:event_busquedaProductoKeyReleased

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarComponentes();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCrear;
    private javax.swing.JButton botonModificar;
    private javax.swing.JButton botonSalir;
    private javax.swing.JButton botonVer;
    private javax.swing.JTextField busquedaProducto;
    private javax.swing.JTextField capitalInvertido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        busquedaProducto.setText("");
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
