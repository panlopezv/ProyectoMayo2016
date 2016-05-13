/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import entidades.Detallecompra;
import entidades.Producto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloDetalleCompra extends AbstractTableModel{
    private final ArrayList<Detallecompra> detallesCompra; // Listado de detalles de compra
    private final String columnas[] = {"Producto", "Cantidad", "Costo", "Subtotal"};   // Datos de la compra
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class, java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloDetalleCompra(ArrayList<Detallecompra> detallesVentas) {
        this.detallesCompra = detallesVentas;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return detallesCompra.size();
    }

    /**
     * 
     * @return numero de columnas de la tabla. 
     */
    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return valor de la fila, columna.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Detallecompra dv = this.detallesCompra.get(rowIndex);
        switch(columnIndex){
            case 0:{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Producto.findByIdProducto");
                q.setParameter("idProducto", dv.getIdProducto());
                return ((Producto)q.getSingleResult()).getNombre();
            }
            case 1:{
                return dv.getCantidad();
            }
            case 2:{
                return new DecimalFormat("Q#,##0.00").format(dv.getCosto());
            }
            case 3:{
                return new DecimalFormat("Q#,##0.00").format(dv.getSubtotal());
            }
            default: return null;
        }
    }
    
    /**
     * 
     * @param columnIndex
     * @return nombre de la columna. 
     */
    @Override
    public String getColumnName(int columnIndex){
        return columnas[columnIndex];
    }
    
    /**
     * 
     * @param columnIndex
     * @return tipo de dato de la columna
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return tipos [columnIndex];
    }

    public ArrayList<Detallecompra> getCompras() {
        return detallesCompra;
    }
    
    public Detallecompra getCompra(int index){
        return detallesCompra.get(index);
    }
    /**
     * Bloquea la edicion de cualquier celda
     * @param rowIndex
     * @param columnIndex
     * @return boolean
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
