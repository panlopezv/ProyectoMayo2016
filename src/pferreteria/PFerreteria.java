/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import vistas.Inicio;
import conexion.Conexion;

/**
 *
 * @author panlo
 */
public class PFerreteria {

    public static Conexion c;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Inicio nuevo = new Inicio();
        nuevo.setVisible(true);
    }
    
}
