/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import conexion.Conexion;
import controladores.CategoriaJpaController;
import controladores.CompraJpaController;
import controladores.DetallecompraJpaController;
import controladores.PagoJpaController;
import controladores.ProductoJpaController;
import controladores.ProveedorJpaController;
import entidades.Categoria;
import entidades.Compra;
import entidades.Detallecompra;
import entidades.Pago;
import entidades.Producto;
import entidades.Proveedor;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Calendar;

public class CCompra extends COperacion {

    private final CategoriaJpaController controladorCategoria;
    private final CompraJpaController controladorCompra;
    private final DetallecompraJpaController controladorDetalleCompra;
    private final ProductoJpaController controladorProducto;
    private final ProveedorJpaController controladorProveedor;
    private final PagoJpaController controladorPago;
    public ArrayList<CProducto> productos;
    private double pagoInicial;

    /**
     * Constructor de la clase
     *
     * @param emf EntityManagerFactory para poder manejar los controladores
     * @param idUsuario int del usuario que esta haciendo la compra
     */
    public CCompra(EntityManagerFactory emf, int idUsuario) {
        this.emf = emf;
        this.idUsuario = idUsuario;
        this.productos = new ArrayList<>();
        this.pagoInicial = 0.0;
        this.credito = false;
        controladorCompra = new CompraJpaController(emf);
        controladorDetalleCompra = new DetallecompraJpaController(emf);
        controladorProveedor = new ProveedorJpaController(emf);
        controladorCategoria = new CategoriaJpaController(emf);
        controladorProducto = new ProductoJpaController(emf);
        controladorPago = new PagoJpaController(emf);
    }

    /**
     * Agrega un cproducto a la lista de productos de la compra
     *
     * @param nuevoP
     */
    public void agregarProducto(CProducto nuevoP) {
        productos.add(nuevoP);
    }

    /**
     * Crea la compra, los detalles de compra y los almacena en la base de
     * datos.
     */
    public void finalizarCompra() {
        if(credito){
            if(pagoInicial>=getTotal()){
                credito=false;
            }
        }
        Compra nueva = new Compra(new Date(), getTotal(), credito, idPersona);
        controladorCompra.create(nueva);
        for (int i = 0; i < productos.size(); i++) {
            controladorDetalleCompra.create(new Detallecompra(nueva.getIdCompra(), productos.get(i).getId(), productos.get(i).getCantidad(), productos.get(i).getPrecio(), productos.get(i).getSubtotal()));
        }
        if (credito) {
            if (pagoInicial > 0) {
                crearPago();
            }
        }
        Conexion.getConexion().getEmf().getCache().evictAll();
    }

    /**
     * Inserta un nuevo proveedor a la base de datos
     *
     * @param nombre String
     * @param nit String
     * @param telefono String
     */
    public Proveedor crearProveedor(String nombre, String nit, String telefono) {
        Proveedor nuevo = new Proveedor(nombre, nit, telefono);
        controladorProveedor.create(nuevo);
        this.idPersona = nuevo.getIdProveedor();
        return nuevo;
    }

    /**
     * Inserta una nueva categoria a la base de datos
     *
     * @param nombre String
     * @return retornará la categoría creada para asignarla al producto
     * @throws Exception
     */
    public Categoria crearCategoria(String nombre) throws Exception {
        Categoria nueva = new Categoria(nombre);
        controladorCategoria.create(nueva);
        return nueva;
    }

    /**
     * Inserta un nuevo producto a la base de datos
     *
     * @param nombre String
     * @param descripcion String
     * @param precio double
     * @param idCategoria int
     */
    public void crearProducto(String nombre, String descripcion, double precio, int idCategoria) {
        controladorProducto.create(new Producto(nombre, descripcion, precio, idCategoria));
    }

    public ArrayList<CProducto> getProductos() {
        return productos;
    }

    /**
     * Quita un producto de la lista de productos a vender
     *
     * @param posicion
     */
    public void quitarProducto(int posicion) {
        productos.remove(posicion);
    }

    public void crearPago() {
        Pago nuevo = new Pago(new Date(), getPagoInicial(), getIdPersona());
        controladorPago.create(nuevo);
    }

    public double getPagoInicial() {
        return pagoInicial;
    }

    public int obtenerIdCompra() {
        Query q = emf.createEntityManager().createNamedQuery("Compra.findMaxId");
        if ((Integer) q.getSingleResult() == null) {
            return 0;
        } else {
            return ((Integer) q.getSingleResult());
        }
    }

    public void setPagoInicial(double pagoInicial) {
        this.pagoInicial = pagoInicial;
    }

    /**
     * Retorna el total de la compra
     *
     * @return total double
     */
    @Override
    public double getTotal() {
        double total = 0;
        for (int i = 0; i < productos.size(); i++) {
            total += productos.get(i).getSubtotal();
        }
        return total;
    }

}
