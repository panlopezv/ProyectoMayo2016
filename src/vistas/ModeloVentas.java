/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import conexion.Conexion;
import entidades.Cliente;
import entidades.Venta;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloVentas extends AbstractTableModel{
    private final ArrayList<Venta> ventas; // Listado de ventas
    private final String columnas[] = {"Fecha", "Cliente", "Tipo", "Descuento", "Saldo", "Anulada", "Total"};   // Datos de la compra
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloVentas(ArrayList<Venta> ventas) {
        this.ventas = ventas;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return ventas.size();
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
        Venta v = this.ventas.get(rowIndex);
        switch(columnIndex){
            case 0:{
                return v.getFecha();
            }
            case 1:{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Cliente.findByIdCliente");
                q.setParameter("idCliente", v.getIdCliente());
                return ((Cliente)q.getSingleResult()).getNombre();
            }
            case 2:{
                if(v.getCredito())
                    return "Al crédito";
                else
                    return "Al contado";
            }
            case 3:{
                return new DecimalFormat("Q#,##0.00").format(v.getDescuento());
            }
            case 4:{
                return new DecimalFormat("Q#,##0.00").format(v.getSaldo());
            }
            case 5:{
                return v.getAnulada();
            }
            case 6:{
                return new DecimalFormat("Q#,##0.00").format(v.getTotal());
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

    public ArrayList<Venta> getVentas() {
        return ventas;
    }
    
    public Venta getVenta(int index){
        return ventas.get(index);
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
