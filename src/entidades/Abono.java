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
 * @author panlo
 */
@Entity
@Table(name = "abono", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Abono.findAll", query = "SELECT a FROM Abono a"),
    @NamedQuery(name = "Abono.findByIdAbono", query = "SELECT a FROM Abono a WHERE a.idAbono = :idAbono"),
    @NamedQuery(name = "Abono.findByFecha", query = "SELECT a FROM Abono a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Abono.findByTotal", query = "SELECT a FROM Abono a WHERE a.total = :total"),
    @NamedQuery(name = "Abono.findByIdCliente", query = "SELECT a FROM Abono a WHERE a.idCliente = :idCliente")})
public class Abono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAbono", nullable = false)
    private Integer idAbono;
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Total", precision = 22)
    private Double total;
    @Basic(optional = false)
    @Column(name = "idCliente", nullable = false)
    private int idCliente;

    public Abono() {
    }

    public Abono(Integer idAbono) {
        this.idAbono = idAbono;
    }

    public Abono(Integer idAbono, int idCliente) {
        this.idAbono = idAbono;
        this.idCliente = idCliente;
    }

    public Integer getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(Integer idAbono) {
        this.idAbono = idAbono;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAbono != null ? idAbono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Abono)) {
            return false;
        }
        Abono other = (Abono) object;
        if ((this.idAbono == null && other.idAbono != null) || (this.idAbono != null && !this.idAbono.equals(other.idAbono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Abono[ idAbono=" + idAbono + " ]";
    }
    
}
