/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import java.util.Date;

public abstract class COperacion {

    public Date fecha;
    public double total;
    public Boolean credito;
    public int idPersona;

    public abstract double getTotal();

}
