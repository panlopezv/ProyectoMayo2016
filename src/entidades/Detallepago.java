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
@Table(name = "detallepago", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallepago.findAll", query = "SELECT d FROM Detallepago d"),
    @NamedQuery(name = "Detallepago.findByIdDetallePago", query = "SELECT d FROM Detallepago d WHERE d.idDetallePago = :idDetallePago"),
    @NamedQuery(name = "Detallepago.findByMonto", query = "SELECT d FROM Detallepago d WHERE d.monto = :monto"),
    @NamedQuery(name = "Detallepago.findByIdCompra", query = "SELECT d FROM Detallepago d WHERE d.idCompra = :idCompra"),
    @NamedQuery(name = "Detallepago.findByIdPago", query = "SELECT d FROM Detallepago d WHERE d.idPago = :idPago")})
public class Detallepago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetallePago", nullable = false)
    private Integer idDetallePago;
    @Basic(optional = false)
    @Column(name = "Monto", nullable = false)
    private double monto;
    @Basic(optional = false)
    @Column(name = "idCompra", nullable = false)
    private int idCompra;
    @Basic(optional = false)
    @Column(name = "idPago", nullable = false)
    private int idPago;

    public Detallepago() {
    }

    public Detallepago(Integer idDetallePago) {
        this.idDetallePago = idDetallePago;
    }

    public Detallepago(Integer idDetallePago, double monto, int idCompra, int idPago) {
        this.idDetallePago = idDetallePago;
        this.monto = monto;
        this.idCompra = idCompra;
        this.idPago = idPago;
    }

    public Integer getIdDetallePago() {
        return idDetallePago;
    }

    public void setIdDetallePago(Integer idDetallePago) {
        this.idDetallePago = idDetallePago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetallePago != null ? idDetallePago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallepago)) {
            return false;
        }
        Detallepago other = (Detallepago) object;
        if ((this.idDetallePago == null && other.idDetallePago != null) || (this.idDetallePago != null && !this.idDetallePago.equals(other.idDetallePago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detallepago[ idDetallePago=" + idDetallePago + " ]";
    }
    
}
