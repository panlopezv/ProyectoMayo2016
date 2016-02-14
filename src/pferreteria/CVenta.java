/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import controladores.ClienteJpaController;
import controladores.VentaJpaController;
import java.util.ArrayList;

public class CVenta extends COperacion {

    private VentaJpaController controladorV;
    private ClienteJpaController controladorC;
    public double descuento;
    public ArrayList<CProducto> productos;

    public void agregarProducto(int idProducto, int cantidad, double precio) {
    }

    @Override
    public double getTotal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
