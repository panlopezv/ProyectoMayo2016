/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personas;

import entidades.Cliente;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloClientes extends AbstractTableModel{
    private final ArrayList<Cliente> clientes; // Listado de clientes
    private final String columnas[] = {"Nombre", "NIT", "Saldo"};   // Datos del cliente
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return clientes.size();
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
        Cliente c = this.clientes.get(rowIndex);
        switch(columnIndex){
            case 0:{
                return c.getNombre();
            }
            case 1:{
                return c.getNit();
            }
            case 2:{
                return "Q " + c.getSaldo();
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

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
    
    public Cliente getCliente(int index){
        return clientes.get(index);
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
