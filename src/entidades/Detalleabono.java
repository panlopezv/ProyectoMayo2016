/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
@Entity
@Table(name = "detalleabono", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleabono.findAll", query = "SELECT d FROM Detalleabono d"),
    @NamedQuery(name = "Detalleabono.findByIdDetalleAbono", query = "SELECT d FROM Detalleabono d WHERE d.idDetalleAbono = :idDetalleAbono"),
    @NamedQuery(name = "Detalleabono.findByMonto", query = "SELECT d FROM Detalleabono d WHERE d.monto = :monto"),
    @NamedQuery(name = "Detalleabono.findByIdAbono", query = "SELECT d FROM Detalleabono d WHERE d.idAbono = :idAbono"),
    @NamedQuery(name = "Detalleabono.findByIdVenta", query = "SELECT d FROM Detalleabono d WHERE d.idVenta = :idVenta")})
public class Detalleabono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleAbono", nullable = false)
    private Integer idDetalleAbono;
    @Basic(optional = false)
    @Column(name = "Monto", nullable = false)
    private double monto;
    @Basic(optional = false)
    @Column(name = "idAbono", nullable = false)
    private int idAbono;
    @Basic(optional = false)
    @Column(name = "idVenta", nullable = false)
    private int idVenta;

    public Detalleabono() {
    }

    public Detalleabono(Integer idDetalleAbono) {
        this.idDetalleAbono = idDetalleAbono;
    }

    public Detalleabono(Integer idDetalleAbono, double monto, int idAbono, int idVenta) {
        this.idDetalleAbono = idDetalleAbono;
        this.monto = monto;
        this.idAbono = idAbono;
        this.idVenta = idVenta;
    }

    public Integer getIdDetalleAbono() {
        return idDetalleAbono;
    }

    public void setIdDetalleAbono(Integer idDetalleAbono) {
        this.idDetalleAbono = idDetalleAbono;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(int idAbono) {
        this.idAbono = idAbono;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleAbono != null ? idDetalleAbono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleabono)) {
            return false;
        }
        Detalleabono other = (Detalleabono) object;
        if ((this.idDetalleAbono == null && other.idDetalleAbono != null) || (this.idDetalleAbono != null && !this.idDetalleAbono.equals(other.idDetalleAbono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detalleabono[ idDetalleAbono=" + idDetalleAbono + " ]";
    }
    
}
