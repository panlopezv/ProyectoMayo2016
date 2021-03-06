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
@Table(name = "Venta", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.idVenta = :idVenta"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Venta.findSinceFecha", query = "SELECT v FROM Venta v WHERE v.fecha >= :fecha ORDER BY v.fecha DESC"),
    @NamedQuery(name = "Venta.findSinceFechaAndIdCliente", query = "SELECT v FROM Venta v WHERE v.idCliente = :idCliente AND v.fecha >= :fecha ORDER BY v.fecha DESC"),
    @NamedQuery(name = "Venta.findByTotal", query = "SELECT v FROM Venta v WHERE v.total = :total"),
    @NamedQuery(name = "Venta.findBySaldo", query = "SELECT v FROM Venta v WHERE v.saldo = :saldo"),
    @NamedQuery(name = "Venta.findByDescuento", query = "SELECT v FROM Venta v WHERE v.descuento = :descuento"),
    @NamedQuery(name = "Venta.findByCredito", query = "SELECT v FROM Venta v WHERE v.credito = :credito"),
    @NamedQuery(name = "Venta.findByAnulada", query = "SELECT v FROM Venta v WHERE v.anulada = :anulada"),
    @NamedQuery(name = "Venta.findByIdCliente", query = "SELECT v FROM Venta v WHERE v.idCliente = :idCliente"),
    @NamedQuery(name = "Venta.findMaxID", query = "SELECT MAX(v.idVenta) FROM Venta v"),
    @NamedQuery(name = "Venta.findByIdClienteAndCreditoSaldo", query = "SELECT v FROM Venta v WHERE v.idCliente = :idCliente AND v.credito = :credito AND v.saldo > :saldo"),
    @NamedQuery(name = "Venta.findByIdClienteAndCredito", query = "SELECT v FROM Venta v WHERE v.idCliente = :idCliente AND v.credito = :credito"),
    @NamedQuery(name = "Venta.findByIdUsuario", query = "SELECT v FROM Venta v WHERE v.idUsuario = :idUsuario")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idVenta", nullable = false)
    private Integer idVenta;
    @Basic(optional = false)
    @Column(name = "Fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "Total", nullable = false)
    private double total;
    @Basic(optional = false)
    @Column(name = "Saldo", nullable = false)
    private double saldo;
    @Basic(optional = false)
    @Column(name = "Descuento", nullable = false)
    private double descuento;
    @Basic(optional = false)
    @Column(name = "Credito", nullable = false)
    private boolean credito;
    @Column(name = "Anulada")
    private Boolean anulada;
    @Basic(optional = false)
    @Column(name = "idCliente", nullable = false)
    private int idCliente;
    @Basic(optional = false)
    @Column(name = "idUsuario", nullable = false)
    private int idUsuario;

    public Venta() {
    }

    public Venta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Venta(Date fecha, double total, double saldo, double descuento, Boolean credito, int idCliente, int idUsuario) {
        this.fecha = fecha;
        this.total = total;
        this.saldo = saldo;
        this.descuento = descuento;
        this.credito = credito;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.anulada=Boolean.FALSE;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVenta != null ? idVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.idVenta == null && other.idVenta != null) || (this.idVenta != null && !this.idVenta.equals(other.idVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Venta[ idVenta=" + idVenta + " ]";
    }
    
}
