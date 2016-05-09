/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personas;

import entidades.Proveedor;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloProveedores extends AbstractTableModel{
    private final ArrayList<Proveedor> proveedores; // Listado de proveedores
    private final String columnas[] = {"Nombre", "Nit", "Saldo"};   // Datos del proveedor
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return proveedores.size();
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
        Proveedor p = this.proveedores.get(rowIndex);
        switch(columnIndex){
            case 0:{
                return p.getNombre();
            }
            case 1:{
                return p.getNit();
            }
            case 2:{
                return p.getSaldo();
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

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }
    
    public Proveedor getProveedor(int index){
        return proveedores.get(index);
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
