/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventas;

import entidades.Producto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import pferreteria.CProducto;

/**
 *
 * @author kevin
 */
public class modeloProductosVenta extends AbstractTableModel{
    private ArrayList<CProducto> productosVenta;
    private String columnas[];
    private Class[] tipos= new Class [] {java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class,
        java.text.DecimalFormat.class, java.text.DecimalFormat.class};

    public modeloProductosVenta(ArrayList<CProducto> productos, int opc) {
        if(opc==0){
            columnas = new String[]{"Código", "Producto", "Cantidad" , "Precio", "Precio Total"};
        } else{
            columnas = new String[]{"Código", "Producto", "Cantidad" , "Costo", "Costo Total"};
        }
        this.productosVenta = productos;
    }   
    @Override
    public int getRowCount() {
        return productosVenta.size();
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
        if(i > -1 && i1 > -1){
            if(i < productosVenta.size() && i1 < columnas.length){
                CProducto p = productosVenta.get(i);
                switch(i1){
                    case 0: return p.getId();
                    case 1: return p.getNombre();
                    case 2: return p.getCantidad();
                    case 3: return (new DecimalFormat("Q#,##0.00")).format(p.getPrecio());
                    case 4: return (new DecimalFormat("Q#,##0.00")).format(p.getSubtotal());
                    default: return null;
                }
            }
        }
        return null;
    }
    
    public ArrayList<CProducto> obtenerProductosVenta(){
        return this.productosVenta;
    }
    
    public void borrarCProductos(){
        this.productosVenta.clear();
    }
    
    public CProducto obtenerProductoVenta(String nombre){
        for(CProducto cp : productosVenta){
            if(cp.getNombre().compareTo(nombre)==0){
                return cp;
            }
        }
        return null;
    }
}
