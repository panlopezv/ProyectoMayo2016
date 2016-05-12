/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import conexion.Conexion;
import entidades.Usuario;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;
import pferreteria.CPago;

/**
 *
 * @author kevin
 */
public class modeloPagos extends AbstractTableModel{

    private ArrayList<CPago> pagos;
    private String columnas[];
    private Class[] tipos = new Class[]{java.lang.Integer.class, java.text.DecimalFormat.class, java.lang.String.class};

    public modeloPagos(ArrayList<CPago> pagos, int opc) {
        if (opc == 0) {
            columnas = new String[]{"idAbono", "Monto", "Fecha"};
        } else {
            columnas = new String[]{"idPago", "Monto", "Fecha"};
        }
        this.pagos = pagos;
    }

    @Override
    public int getRowCount() {
        return pagos.size();
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
        if (i > -1 && i1 > -1) {
            if (i < pagos.size() && i1 < columnas.length) {
                CPago cp = pagos.get(i);
                    switch (i1) {
                        case 0:
                            return cp.getId();
                        case 1:
                            return (new DecimalFormat("Q#,##0.00")).format(cp.getMonto());
                        case 2: return (new SimpleDateFormat("dd/MM/yyyy").format(cp.getFecha()));
                        default:
                            return null;
                    }
            }
        }
        return null;
    }

    public ArrayList<CPago> obtenerPagos() {
        return pagos;
    }

    public CPago obtenerCPago(Integer id){
        if(id==null)
        {
            return null;
        }
        for(CPago cp : pagos){
            if(cp.getId()==id){
                return cp;
            }
        }
        return null;
    }

    public void borrarPagos() {
        pagos.clear();
    }
    
}
