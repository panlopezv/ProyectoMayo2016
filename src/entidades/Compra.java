/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
@Entity
@Table(name = "Compra", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findByIdCompra", query = "SELECT c FROM Compra c WHERE c.idCompra = :idCompra"),
    @NamedQuery(name = "Compra.findByFecha", query = "SELECT c FROM Compra c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Compra.findByTotal", query = "SELECT c FROM Compra c WHERE c.total = :total"),
    @NamedQuery(name = "Compra.findByCredito", query = "SELECT c FROM Compra c WHERE c.credito = :credito"),
    @NamedQuery(name = "Compra.findByAnulada", query = "SELECT c FROM Compra c WHERE c.anulada = :anulada"),
    @NamedQuery(name = "Compra.findBySaldo", query = "SELECT c FROM Compra c WHERE c.saldo = :saldo"),
    @NamedQuery(name = "Compra.findByIdProvAndCreditoSaldo", query = "SELECT c FROM Compra c WHERE c.idProveedor = :idProveedor AND c.credito = :credito AND c.saldo > :saldo"),
    @NamedQuery(name = "Compra.findByIdProvAndCredito", query = "SELECT c FROM Compra c WHERE c.idProveedor = :idProveedor AND c.credito = :credito"),
    @NamedQuery(name = "Compra.findMaxId", query = "SELECT MAX(c.idCompra) FROM Compra c"),
    @NamedQuery(name = "Compra.findByIdProveedor", query = "SELECT c FROM Compra c WHERE c.idProveedor = :idProveedor")})
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCompra", nullable = false)
    private Integer idCompra;
    @Basic(optional = false)
    @Column(name = "Fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "Total", nullable = false)
    private double total;
    @Basic(optional = false)
    @Column(name = "Credito", nullable = false)
    private boolean credito;
    @Column(name = "Anulada")
    private Boolean anulada;
    @Basic(optional = false)
    @Column(name = "Saldo", nullable = false)
    private double saldo;
    @Basic(optional = false)
    @Column(name = "idProveedor", nullable = false)
    private int idProveedor;

    public Compra() {
    }

    public Compra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Compra(Date fecha, double total, Boolean credito, int idProveedor) {
        this.fecha = fecha;
        this.total = total;
        this.credito = credito;
        this.idProveedor = idProveedor;
        if(this.credito){
            this.saldo = this.total;
        }
        else{
            this.saldo = 0.0;
        }
        this.anulada = Boolean.FALSE;
    }
    
    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean getCredito() {
        return credito;
    }

    public void setCredito(boolean credito) {
        this.credito = credito;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompra != null ? idCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.idCompra == null && other.idCompra != null) || (this.idCompra != null && !this.idCompra.equals(other.idCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Compra[ idCompra=" + idCompra + " ]";
    }
    
}
