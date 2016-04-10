/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventas;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import pferreteria.CProducto;

/**
 *
 * @author kevin
 */
public class modeloProductos extends AbstractTableModel{
    private ArrayList<CProducto> productoVendido;
    private String columnas[] = {"ID", "Nombre", "Cantidad" , "Precio", "Subtotal"};
    private Class[] tipos= new Class [] {java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class,
        java.lang.Double.class, java.lang.Double.class};

    public modeloProductos(ArrayList<CProducto> productos) {
        this.productoVendido = productos;
    }
    

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
