/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import entidades.Compra;
import entidades.Proveedor;
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
public class modeloComprasAbono extends AbstractTableModel{
    private final ArrayList<Compra> compras;
    private final String columnas[] = {"No. Compra", "Total", "Saldo" , "Proveedor", "Fecha"};
    private final Class[] tipos= new Class [] {java.lang.Integer.class, java.text.DecimalFormat.class, java.text.DecimalFormat.class,
        java.lang.String.class, java.lang.String.class};

    public modeloComprasAbono(ArrayList<Compra> compras) {        
        this.compras = compras;
    }
    
    @Override
    public int getRowCount() {
        return compras.size();
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
            if(i < compras.size() && i1 < columnas.length){
                Compra c = compras.get(i);
                switch(i1){
                    case 0: return c.getIdCompra();
                    case 1: return (new DecimalFormat("Q#,##0.00")).format(c.getTotal());
                    case 2: return (new DecimalFormat("Q#,##0.00")).format(c.getSaldo());
                    case 3: 
                        EntityManagerFactory emf = Conexion.getConexion().getEmf();
                        Query q = emf.createEntityManager().createNamedQuery("Proveedor.findByIdProveedor");
                        q.setParameter("idProveedor", c.getIdProveedor());
                        if(q.getResultList().isEmpty()){
                            return "";
                        }else{
                            return ((Proveedor)q.getSingleResult()).getNombre();                            
                        }
                    case 4: return (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getFecha()));
                    default: return null;
                }
            }
        }
        return null;
    }
    
    public ArrayList<Compra> obtenerCompras(){
        return this.compras;
    }
    
    public void borrarCompras(){
        this.compras.clear();
    }
    
    public Compra obtenerCompra(Integer id){
        if(id==null)
        {
            return null;
        }
        for(Compra c : compras){
            if(c.getIdCompra()==id){
                return c;
            }
        }
        return null;
    }
    
}
