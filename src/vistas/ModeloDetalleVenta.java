/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Producto;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloDetalleVenta extends AbstractTableModel{
    private final ArrayList<Detalleventa> detallesVentas; // Listado de detalles ventas
    private final String columnas[] = {"Producto", "Cantidad", "Precio", "Subtotal"};   // Datos de la compra
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class, java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloDetalleVenta(ArrayList<Detalleventa> detallesVentas) {
        this.detallesVentas = detallesVentas;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return detallesVentas.size();
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
        Detalleventa dv = this.detallesVentas.get(rowIndex);
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
                return "Q " + dv.getPrecio();
            }
            case 3:{
                return "Q " + dv.getSubtotal();
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

    public ArrayList<Detalleventa> getVentas() {
        return detallesVentas;
    }
    
    public Detalleventa getVenta(int index){
        return detallesVentas.get(index);
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
