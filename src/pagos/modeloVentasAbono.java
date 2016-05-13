/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import entidades.Proveedor;
import entidades.Venta;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class modeloVentasAbono extends AbstractTableModel{
    private final ArrayList<Venta> ventas;
    private final String columnas[] = {"No. Venta", "Total", "Saldo" , "Cliente", "Fecha"};
    private final Class[] tipos= new Class [] {java.lang.Integer.class, java.text.DecimalFormat.class, java.text.DecimalFormat.class,
        java.lang.String.class, java.lang.String.class};

    public modeloVentasAbono(ArrayList<Venta> ventas) {        
        this.ventas = ventas;
    }
    
    @Override
    public int getRowCount() {
        return ventas.size();
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
            if(i < ventas.size() && i1 < columnas.length){
                Venta v = ventas.get(i);
                switch(i1){
                    case 0: return v.getIdVenta();
                    case 1: return (new DecimalFormat("Q#,##0.00")).format(v.getTotal());
                    case 2: return (new DecimalFormat("Q#,##0.00")).format(v.getSaldo());
                    case 3: 
                        EntityManagerFactory emf = Conexion.getConexion().getEmf();
                        Query q = emf.createEntityManager().createNamedQuery("Cliente.findByIdCliente");
                        q.setParameter("idCliente", v.getIdCliente());
                        if(q.getResultList().isEmpty()){
                            return "";
                        }else{
                            return ((Proveedor)q.getSingleResult()).getNombre();                            
                        }
                    case 4: return (new SimpleDateFormat("dd/MM/yyyy").format(v.getFecha()));
                    default: return null;
                }
            }
        }
        return null;
    }
    
    public ArrayList<Venta> obtenerCompras(){
        return this.ventas;
    }
    
    public void borrarVentas(){
        this.ventas.clear();
    }
    
    public Venta obtenerVenta(Integer id){
        if(id==null)
        {
            return null;
        }
        for(Venta v : ventas){
            if(v.getIdVenta()==id){
                return v;
            }
        }
        return null;
    }
    
    
}
