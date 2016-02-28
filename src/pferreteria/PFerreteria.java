/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import conexion.Conexion;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String user = "panlopezv";
        String pass = "1234";
        c = Conexion.getConexion(user,pass);
        if(c!=null){
            System.out.println("Exito");
        }
        else{
            System.out.println("Fracaso");
        }
        FabricaOperacion fp = new FabricaOperacion(c.getEmf());
        
        //CCompra nuevaCompra = (CCompra) fp.crearOperacion(2, 1);
//        nuevaCompra.crearProducto("Martillo", "Martillo", 20, 1);
//        try {
//            nuevaCompra.crearCategoria("Carpinteria");
//        } catch (Exception ex) {
//            Logger.getLogger(PFerreteria.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        nuevaCompra.crearProveedor("Pablo Andres", "12345", "42606299");

//        nuevaCompra.agregarProducto(new CProducto(1,"Martillo",10,20));
//        nuevaCompra.agregarProducto(new CProducto(2,"Desarmador",10,20));
//        nuevaCompra.setCredito(Boolean.TRUE);
//        nuevaCompra.setIdPersona(1);
//        nuevaCompra.finalizarCompra();
        
        CVenta nuevaVenta = (CVenta) fp.crearOperacion(1, 1);
//        nuevaVenta.crearCliente("Pablo Andres", "Ciudad", "12345");

        nuevaVenta.agregarProducto(new CProducto(1,"Martillo",1,30));
        nuevaVenta.agregarProducto(new CProducto(2,"Desarmador",1,25));
        nuevaVenta.setCredito(Boolean.TRUE);
        nuevaVenta.setDescuento(10);
        nuevaVenta.setIdPersona(1);
        nuevaVenta.finalizarVenta();
    }
    
}
