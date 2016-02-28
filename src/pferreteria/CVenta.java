/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import controladores.ClienteJpaController;
import controladores.DetalleventaJpaController;
import controladores.VentaJpaController;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Venta;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class CVenta extends COperacion {

    private final VentaJpaController controladorVenta;
    private final DetalleventaJpaController controladorDetalleVenta;
    private final ClienteJpaController controladorCliente;
    private double descuento;
    private ArrayList<CProducto> productos;

    /**
     * Constructor de la clase
     * @param emf   EntityManagerFactory para poder manejar los controladores
     * @param idUsuario int del usuario que esta haciendo la compra
     */
    public CVenta(EntityManagerFactory emf, int idUsuario) {
        this.emf = emf;
        this.idUsuario = idUsuario;
        this.productos = new ArrayList<>();
        this.descuento = 0;
        controladorVenta = new VentaJpaController(emf);
        controladorDetalleVenta = new DetalleventaJpaController(emf);
        controladorCliente = new ClienteJpaController(emf);
    }
    
    /**
     * Crea la venta, los detalles de venta y los almacena en la base de datos.
     */
    public void finalizarVenta(){
        Venta nueva = new Venta(new Date(), getTotal(), descuento, credito, idPersona, idUsuario);
        controladorVenta.create(nueva);
        for(int i=0;i<productos.size();i++){
            controladorDetalleVenta.create(new Detalleventa(nueva.getIdVenta(), productos.get(i).getId(), productos.get(i).getCantidad(), productos.get(i).getPrecio(),productos.get(i).getSubtotal()));
        }
    }
    
    /**
     * Agrega un cproducto a la lista de productos de la venta
     * @param nuevoP 
     */
    public void agregarProducto(CProducto nuevoP) {
        productos.add(nuevoP);
    }

    /**
     * Inserta un nuevo cliente en la base de datos
     * @param nombre String
     * @param direccion String
     * @param nit String
     */
    public void crearCliente(String nombre, String direccion, String nit){
        controladorCliente.create(new Cliente(nombre, direccion, nit));
//        Query consulta = emf.createEntityManager().createNamedQuery("Cliente.findMaxID");
//        this.idPersona = (int) consulta.getSingleResult();
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

    /**
     * Retorna el total de la venta
     * @return total double
     */
    @Override
    public double getTotal() {
        double total = 0;
        for(int i=0;i<productos.size();i++){
            total += productos.get(i).getSubtotal();
        }
        return total;
    }

}
