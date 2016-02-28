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
@Table(name = "detallecompra", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d"),
    @NamedQuery(name = "Detallecompra.findByIdDetalleCompra", query = "SELECT d FROM Detallecompra d WHERE d.idDetalleCompra = :idDetalleCompra"),
    @NamedQuery(name = "Detallecompra.findByCompraidCompra", query = "SELECT d FROM Detallecompra d WHERE d.compraidCompra = :compraidCompra"),
    @NamedQuery(name = "Detallecompra.findByProductoidProducto", query = "SELECT d FROM Detallecompra d WHERE d.productoidProducto = :productoidProducto"),
    @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallecompra.findByCosto", query = "SELECT d FROM Detallecompra d WHERE d.costo = :costo"),
    @NamedQuery(name = "Detallecompra.findBySubtotal", query = "SELECT d FROM Detallecompra d WHERE d.subtotal = :subtotal")})
public class Detallecompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleCompra", nullable = false)
    private Long idDetalleCompra;
    @Basic(optional = false)
    @Column(name = "Compra_idCompra", nullable = false)
    private int compraidCompra;
    @Basic(optional = false)
    @Column(name = "Producto_idProducto", nullable = false)
    private int productoidProducto;
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
        this.compraidCompra = compraidCompra;
        this.productoidProducto = productoidProducto;
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

    public int getCompraidCompra() {
        return compraidCompra;
    }

    public void setCompraidCompra(int compraidCompra) {
        this.compraidCompra = compraidCompra;
    }

    public int getProductoidProducto() {
        return productoidProducto;
    }

    public void setProductoidProducto(int productoidProducto) {
        this.productoidProducto = productoidProducto;
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
