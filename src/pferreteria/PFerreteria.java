/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import conexion.Conexion;
import controladores.UsuarioJpaController;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author panlo
 */
public class PFerreteria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String user = "panlopezv";
        String pass = "1234";
        Conexion c = Conexion.getConexion(user,pass);
        if(c!=null){
            System.out.println("Exito");
        }
        else{
            System.out.println("Fracaso");
        }
    }
    
}
