/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import javax.persistence.EntityManagerFactory;

public class FabricaOperacion {

    private final EntityManagerFactory emf;
    
    public FabricaOperacion(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Construye y devuelve la operacion CVenta o CCompra
     * @param tipo int
     * @param idUsuario int
     * @return CVenta/CCompra
     */
    public COperacion crearOperacion(int tipo, int idUsuario) {
        switch(tipo){
            // Venta
            case 1: return new CVenta(emf, idUsuario);
            // Compra
            case 2: return new CCompra(emf, idUsuario);
        }
        return null;
    }
}
