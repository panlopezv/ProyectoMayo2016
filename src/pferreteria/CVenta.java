/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import conexion.Conexion;
import controladores.AbonoJpaController;
import controladores.ClienteJpaController;
import controladores.DetalleventaJpaController;
import controladores.ProductoJpaController;
import controladores.VentaJpaController;
import entidades.Abono;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Producto;
import entidades.Venta;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class CVenta extends COperacion {

    private final VentaJpaController controladorVenta;
    private final DetalleventaJpaController controladorDetalleVenta;
    private final ClienteJpaController controladorCliente;
    private final AbonoJpaController controladorAbono;
    private final ProductoJpaController controladorProducto;
    private int idVenta;
    private double descuento;
    private double saldo;
    private double pagoInicial;
    private Date fecha;
    private ArrayList<CProducto> productos;

    /**
     * Constructor de la clase
     *
     * @param emf EntityManagerFactory para poder manejar los controladores
     * @param idUsuario int del usuario que esta haciendo la compra
     */
    public CVenta(EntityManagerFactory emf, int idUsuario) {
        this.emf = emf;
        this.idUsuario = idUsuario;
        this.productos = new ArrayList<>();
        this.descuento = 0.0;
        this.saldo = 0.0;
        this.pagoInicial = 0.0;
        this.idVenta = 0;
        this.credito = false;
        controladorVenta = new VentaJpaController(emf);
        controladorDetalleVenta = new DetalleventaJpaController(emf);
        controladorCliente = new ClienteJpaController(emf);
        controladorAbono = new AbonoJpaController(emf);
        controladorProducto = new ProductoJpaController(emf);
    }

    /**
     * Crea la venta, los detalles de venta y los almacena en la base de datos.
     *
     * @return 
     */
    public boolean finalizarVenta() {
        Conexion.getConexion().getEmf().getCache().evictAll();
        boolean verificacionDeExistencias = Boolean.TRUE;
        for (CProducto cp : productos) {
            Query q = emf.createEntityManager().createNamedQuery("Producto.findByIdProducto");
            q.setParameter("idProducto", cp.getId());
            if (((Producto) q.getSingleResult()).getExistencias() < cp.getCantidad()) {
                verificacionDeExistencias = Boolean.FALSE;
                break;
            }
        }
        if (verificacionDeExistencias) {
            if (credito) {
                saldo = getTotal() - this.descuento;
                if (pagoInicial >= saldo) {
                    credito = false;
                    saldo = 0.00;
                }
            }
            Venta nueva = new Venta(fecha, getTotal(), saldo, descuento, credito, idPersona, idUsuario);//esto no inserta en la DB, nueva no tiene ID aun
            controladorVenta.create(nueva);
            idVenta = nueva.getIdVenta();
            for (CProducto cp : productos) {
                controladorDetalleVenta.create(new Detalleventa(nueva.getIdVenta(), cp.getId(), cp.getCantidad(), cp.getPrecio(), cp.getSubtotal()));
            }
            if (credito) {
                if (pagoInicial > 0.00) {
                    crearAbono();
                }
            }
            Conexion.getConexion().getEmf().getCache().evictAll();
        } 
        return verificacionDeExistencias;
    }

    /**
     * Agrega un cproducto a la lista de productos a vender
     *
     * @param nuevoP
     */
    public void agregarProducto(CProducto nuevoP) {
        productos.add(nuevoP);
    }

    /**
     * Quita un producto de la lista de productos a vender
     *
     * @param posicion
     */
    public void quitarProducto(int posicion) {
        productos.remove(posicion);
    }
    
    public void borrarProductos(){
        productos.clear();
    }

    /**
     * Inserta un nuevo cliente en la base de datos
     *
     * @param nombre String
     * @param direccion String
     * @param nit String
     * @return cliente creado
     */
    public Cliente crearCliente(String nombre, String direccion, String nit) {
        if (direccion.compareTo("") == 0) {
            direccion = "Ciudad";
        }
        Cliente nuevo = new Cliente(nombre, direccion, nit);
        controladorCliente.create(nuevo);
        this.idPersona = nuevo.getIdCliente();
        return nuevo;
    }

    public void crearAbono() {
        Abono nuevo = new Abono(fecha, getPagoInicial(), getIdPersona());
        controladorAbono.create(nuevo);
    }
    
    public Producto crearProducto(String nombre, double precio, int existencias, int idCategoria) {
        Producto nuevo = new Producto(nombre, existencias, precio, idCategoria);
        controladorProducto.create(nuevo);
        return nuevo;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public ArrayList<CProducto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<CProducto> productos) {
        this.productos = productos;
    }

    public double getPagoInicial() {
        return pagoInicial;
    }

    public void setPagoInicial(double pagoInicial) {
        this.pagoInicial = pagoInicial;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int obtenerIdVenta() {
        Query q = emf.createEntityManager().createNamedQuery("Venta.findMaxID");
        if ((Integer) q.getSingleResult() == null) {
            return 0;
        } else {
            return ((Integer) q.getSingleResult());
        }
    }

    /**
     * Retorna el total de la venta
     *
     * @return total double
     */
    @Override
    public double getTotal() {
        double total = 0.00;
        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).getSubtotal();
        }
        return total;
    }

}
