/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pferreteria;

import controladores.CategoriaJpaController;
import controladores.CompraJpaController;
import controladores.DetallecompraJpaController;
import controladores.ProductoJpaController;
import controladores.ProveedorJpaController;
import entidades.Categoria;
import entidades.Compra;
import entidades.Detallecompra;
import entidades.Producto;
import entidades.Proveedor;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;

public class CCompra extends COperacion {

    private final CategoriaJpaController controladorCategoria;
    private final CompraJpaController controladorCompra;
    private final DetallecompraJpaController controladorDetalleCompra;
    private final ProductoJpaController controladorProducto;
    private final ProveedorJpaController controladorProveedor;
    public ArrayList<CProducto> productos;

    /**
     * Constructor de la clase
     * @param emf   EntityManagerFactory para poder manejar los controladores
     * @param idUsuario int del usuario que esta haciendo la compra
     */
    public CCompra(EntityManagerFactory emf, int idUsuario) {
        this.emf = emf;
        this.idUsuario = idUsuario;
        this.productos = new ArrayList<>();
        controladorCompra = new CompraJpaController(emf);
        controladorDetalleCompra = new DetallecompraJpaController(emf);
        controladorProveedor = new ProveedorJpaController(emf);
        controladorCategoria = new CategoriaJpaController(emf);
        controladorProducto = new ProductoJpaController(emf);
    }
    
    /**
     * Agrega un cproducto a la lista de productos de la compra
     * @param nuevoP 
     */
    public void agregarProducto(CProducto nuevoP) {
        productos.add(nuevoP);
    }
    
    /**
     * Crea la compra, los detalles de compra y los almacena en la base de datos.
     */
    public void finalizarCompra(){
        Compra nueva = new Compra(new Date(), getTotal(), credito, idPersona);
        controladorCompra.create(nueva);
        for(int i=0;i<productos.size();i++){
            controladorDetalleCompra.create(new Detallecompra(nueva.getIdCompra(), productos.get(i).getId(), productos.get(i).getCantidad(), productos.get(i).getPrecio(),productos.get(i).getSubtotal()));
        }
    }
    
    /**
     * Inserta un nuevo proveedor a la base de datos
     * @param nombre String
     * @param nit String
     * @param telefono String
     */
    public void crearProveedor(String nombre, String nit,  String telefono){
        controladorProveedor.create(new Proveedor(nombre, nit, telefono));
//        Query consulta = emf.createEntityManager().createNamedQuery("Proveedor.findMaxID");
//        this.idPersona = (int) consulta.getSingleResult();
    }
    
    /**
     * Inserta una nueva categoria a la base de datos
     * @param nombre String
     * @throws Exception 
     */
    public void crearCategoria(String nombre) throws Exception{
        controladorCategoria.create(new Categoria(nombre));        
    }
    
    /**
     * Inserta un nuevo producto a la base de datos
     * @param nombre String
     * @param descripcion String
     * @param precio double
     * @param idCategoria int
     */
    public void crearProducto(String nombre, String descripcion, double precio, int idCategoria){
        controladorProducto.create(new Producto(nombre,descripcion,precio,idCategoria));
    }

    /**
     * Retorna el total de la compra
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
