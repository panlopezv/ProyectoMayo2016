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
@Table(name = "DetalleCompra", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d"),
    @NamedQuery(name = "Detallecompra.findByIdDetalleCompra", query = "SELECT d FROM Detallecompra d WHERE d.idDetalleCompra = :idDetalleCompra"),
    @NamedQuery(name = "Detallecompra.findByCompraidCompra", query = "SELECT d FROM Detallecompra d WHERE d.idCompra = :idCompra"),
    @NamedQuery(name = "Detallecompra.findByProductoidProducto", query = "SELECT d FROM Detallecompra d WHERE d.idProducto = :idProducto"),
    @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallecompra.findByCosto", query = "SELECT d FROM Detallecompra d WHERE d.costo = :costo"),
    @NamedQuery(name = "Detallecompra.findLastId", query = "SELECT MAX(d.idDetalleCompra) FROM Detallecompra d WHERE d.idProducto = :idProducto"),
    @NamedQuery(name = "Detallecompra.findBySubtotal", query = "SELECT d FROM Detallecompra d WHERE d.subtotal = :subtotal")})
public class Detallecompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleCompra", nullable = false)
    private Long idDetalleCompra;
    @Basic(optional = false)
    @Column(name = "idCompra", nullable = false)
    private int idCompra;
    @Basic(optional = false)
    @Column(name = "idProducto", nullable = false)
    private int idProducto;
    @Basic(optional = false)
    @Column(name = "Cantidad", nullable = false)
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "Costo", nullable = false)
    private double costo;
    @Basic(optional = false)
    @Column(name = "Subtotal", nullable = false)
    private double subtotal;

    public Detallecompra() {
    }

    public Detallecompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Detallecompra(int compraidCompra, int productoidProducto, Integer cantidad, Double costo, Double subtotal) {
        this.idCompra = compraidCompra;
        this.idProducto = productoidProducto;
        this.cantidad = cantidad;
        this.costo = costo;
        this.subtotal = subtotal;
    }
    
    public Long getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleCompra != null ? idDetalleCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallecompra)) {
            return false;
        }
        Detallecompra other = (Detallecompra) object;
        if ((this.idDetalleCompra == null && other.idDetalleCompra != null) || (this.idDetalleCompra != null && !this.idDetalleCompra.equals(other.idDetalleCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detallecompra[ idDetalleCompra=" + idDetalleCompra + " ]";
    }
    
}
