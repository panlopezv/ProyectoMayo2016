/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import java.util.Date;

/**
 *
 * @author kevin
 */
public class CPago {
    private int id;
    private Date fecha;
    private double monto;
    
    public CPago(int id, Date fecha, double monto){
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
