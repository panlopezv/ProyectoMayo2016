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
 * @author panlo
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
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Subtotal", precision = 22)
    private Double subtotal;

    public Detallecompra() {
    }

    public Detallecompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Detallecompra(Long idDetalleCompra, int compraidCompra, int productoidProducto) {
        this.idDetalleCompra = idDetalleCompra;
        this.compraidCompra = compraidCompra;
        this.productoidProducto = productoidProducto;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
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
