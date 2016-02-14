/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import controladores.CategoriaJpaController;
import controladores.CompraJpaController;
import controladores.ProductoJpaController;
import controladores.ProveedorJpaController;
import entidades.Producto;
import java.util.ArrayList;

public class CCompra extends COperacion {

    private CategoriaJpaController controladorCat;
    private CompraJpaController controladorCom;
    private ProductoJpaController controladorProd;
    private ProveedorJpaController controladorProv;
    public ArrayList<Producto> productos;

    public void agregarProducto(Producto nuevoP) {
    }

    @Override
    public double getTotal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
