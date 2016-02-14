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
@Table(name = "detalleventa", catalog = "ferreteria", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleventa.findAll", query = "SELECT d FROM Detalleventa d"),
    @NamedQuery(name = "Detalleventa.findByIdDetalleVenta", query = "SELECT d FROM Detalleventa d WHERE d.idDetalleVenta = :idDetalleVenta"),
    @NamedQuery(name = "Detalleventa.findByIdVenta", query = "SELECT d FROM Detalleventa d WHERE d.idVenta = :idVenta"),
    @NamedQuery(name = "Detalleventa.findByIdProducto", query = "SELECT d FROM Detalleventa d WHERE d.idProducto = :idProducto"),
    @NamedQuery(name = "Detalleventa.findByCantidad", query = "SELECT d FROM Detalleventa d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detalleventa.findByPrecio", query = "SELECT d FROM Detalleventa d WHERE d.precio = :precio"),
    @NamedQuery(name = "Detalleventa.findBySubdescuento", query = "SELECT d FROM Detalleventa d WHERE d.subdescuento = :subdescuento"),
    @NamedQuery(name = "Detalleventa.findBySubtotal", query = "SELECT d FROM Detalleventa d WHERE d.subtotal = :subtotal")})
public class Detalleventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleVenta", nullable = false)
    private Long idDetalleVenta;
    @Basic(optional = false)
    @Column(name = "idVenta", nullable = false)
    private int idVenta;
    @Basic(optional = false)
    @Column(name = "idProducto", nullable = false)
    private int idProducto;
    @Basic(optional = false)
    @Column(name = "Cantidad", nullable = false)
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "Precio", nullable = false)
    private double precio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Subdescuento", precision = 22)
    private Double subdescuento;
    @Column(name = "Subtotal", precision = 22)
    private Double subtotal;

    public Detalleventa() {
    }

    public Detalleventa(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Detalleventa(Long idDetalleVenta, int idVenta, int idProducto, int cantidad, double precio) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Long getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Double getSubdescuento() {
        return subdescuento;
    }

    public void setSubdescuento(Double subdescuento) {
        this.subdescuento = subdescuento;
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
        hash += (idDetalleVenta != null ? idDetalleVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleventa)) {
            return false;
        }
        Detalleventa other = (Detalleventa) object;
        if ((this.idDetalleVenta == null && other.idDetalleVenta != null) || (this.idDetalleVenta != null && !this.idDetalleVenta.equals(other.idDetalleVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detalleventa[ idDetalleVenta=" + idDetalleVenta + " ]";
    }
    
}
