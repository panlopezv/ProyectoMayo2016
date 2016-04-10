/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario;

import conexion.Conexion;
import entidades.Categoria;
import entidades.Producto;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ModeloProductosInventario extends AbstractTableModel{
    private final ArrayList<Producto> productos; // Listado de productos
    private final String columnas[] = {"Nombre", "Existencias", "Precio", "Categoria"};   // Datos del producto
    private final Class[] tipos= new Class [] {java.lang.String.class, java.lang.Integer.class, java.lang.String.class,
        java.lang.String.class};    // Tipo de dato de cada columna.

    public ModeloProductosInventario(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    
    /**
     * 
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto p= this.productos.get(rowIndex);
        switch(columnIndex){
            case 0: return p.getNombre();
            case 1: return p.getExistencias();
            case 2: return p.getPrecio();
            case 3:{
                Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Categoria.findByIdCategoria");
                q.setParameter("idCategoria", p.getIdCategoria());
                try {
                    return ((Categoria)q.getResultList().get(0)).getNombre();
                } catch (Exception ex) {
                    return null;
                }
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
