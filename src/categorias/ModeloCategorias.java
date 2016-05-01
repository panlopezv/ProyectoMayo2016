/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package categorias;

import entidades.Categoria;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloCategorias extends AbstractTableModel{
    private final ArrayList<Categoria> categorias;  // Listado de categorias
    private final String columnas[] = {"Nombre"};   // Datos de la categoria
    private final Class[] tipos= new Class [] {java.lang.String.class}; // Tipo de dato de cada columna.

    public ModeloCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return categorias.size();
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
        Categoria c= this.categorias.get(rowIndex);
        switch(columnIndex){
            case 0: return c.getNombre();
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
    
    public ArrayList<Categoria> geCategorias() {
        return categorias;
    }
    
    public Categoria getCategoria(int index){
        return categorias.get(index);
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
