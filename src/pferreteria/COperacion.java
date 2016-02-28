 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import javax.persistence.EntityManagerFactory;

public abstract class COperacion {

    protected Boolean credito;
    protected int idPersona; // Cliente/Proveedor
    protected int idUsuario;
    protected EntityManagerFactory emf;
    
    public Boolean getCredito() {
        return credito;
    }

    public void setCredito(Boolean credito) {
        this.credito = credito;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }
    
    public abstract double getTotal();

}
