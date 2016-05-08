/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import entidades.Usuario;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloUsuarios extends AbstractTableModel{
    private final ArrayList<Usuario> usuarios; // Listado de usuarios
    private final String columnas[] = {"Usuario", "Nombre", "Administrador"};   // Datos del usuario
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.String.class, 
        java.lang.Boolean.class};    // Tipo de dato de cada columna.

    public ModeloUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return usuarios.size();
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
        Usuario u= this.usuarios.get(rowIndex);
        switch(columnIndex){
            case 0:{
                return u.getUsuario();
            }
            case 1:{
                return u.getNombre();
            }
            case 2:{
                return u.getEsAdministrador();
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

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public Usuario getUsuario(int index){
        return usuarios.get(index);
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
