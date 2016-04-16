/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventas;

import conexion.Conexion;
import entidades.Categoria;
import entidades.Producto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class modeloProductos extends AbstractTableModel{
    private ArrayList<Producto> productos;
    private String columnas[] = {"Código", "Producto", "Categoría" , "Precio", "Existencias"};
    private Class[] tipos= new Class [] {java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
        java.text.DecimalFormat.class, java.lang.Integer.class};

    public modeloProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    

    @Override
    public int getRowCount() {
        return productos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnas[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return tipos[i];
    }
    
    @Override
    public Object getValueAt(int i, int i1) {
        if(i>-1 && i1 >-1){
            if(i<productos.size() && i1<columnas.length){
                Producto p = productos.get(i);
                switch(i1){
                    case 0: return p.getIdProducto();
                    case 1: return p.getNombre();
                    case 2:
                        EntityManagerFactory emf = Conexion.getConexion().getEmf();
                        Query q = emf.createEntityManager().createNamedQuery("Categoria.findByIdCategoria");
                        q.setParameter("idCategoria", p.getIdCategoria());
                        return ((Categoria)q.getSingleResult()).getNombre();
                    case 3: return (new DecimalFormat("Q#,##0.00")).format(p.getPrecio());
                    case 4: return p.getExistencias();
                    default: return null;
                }
            }
        }
        return null;
    }
    
    public ArrayList<Producto> obtenerProductos(){
        return productos;
    }
    
    public Producto obtenerProducto(String nombre){
        for(Producto p : productos){
            if(p.getNombre().compareTo(nombre)==0)
                return p;
        }
        return null;
    }
}