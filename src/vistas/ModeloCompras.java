/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import entidades.Compra;
import entidades.Proveedor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloCompras extends AbstractTableModel{
    private final ArrayList<Compra> compras; // Listado de compras
    private final String columnas[] = {"Fecha", "Proveedor", "Tipo", "Saldo", "Anulada", "Total"};   // Datos de la compra
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloCompras(ArrayList<Compra> compras) {
        this.compras = compras;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return compras.size();
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
        Compra c = this.compras.get(rowIndex);
        switch(columnIndex){
            case 0:{
                return c.getFecha();
            }
            case 1:{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findByIdProveedor");
                q.setParameter("idProveedor", c.getIdProveedor());
                return ((Proveedor)q.getSingleResult()).getNombre();
            }
            case 2:{
                if(c.getCredito())
                    return "Al crédito";
                else
                    return "Al contado";
            }
            case 3:{
                return new DecimalFormat("Q#,##0.00").format(c.getSaldo());
            }
            case 4:{
                return c.getAnulada();
            }
            case 5:{
                return new DecimalFormat("Q#,##0.00").format(c.getTotal());
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

    public ArrayList<Compra> getCompras() {
        return compras;
    }
    
    public Compra getCompra(int index){
        return compras.get(index);
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
