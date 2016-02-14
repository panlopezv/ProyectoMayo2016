package conexion;

import controladores.UsuarioJpaController;
import entidades.Usuario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Conexion {

    private EntityManagerFactory emf;
    private static Conexion unicaConexion;
    private int idUsuario;

    public static Conexion getConexion(String usuario, String contrasenya) {
        if (unicaConexion == null) {
            try {
                unicaConexion = new Conexion(usuario, contrasenya);
            } catch (Exception ex) {
                unicaConexion = null;
            }
        }
        return unicaConexion;
    }

    private Conexion(String usuario, String contrasenya) throws Exception {
        emf = Persistence.createEntityManagerFactory("PFerreteriaPU");
        UsuarioJpaController controladorU = new UsuarioJpaController(emf);
        Query q = emf.createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenya");
        q.setParameter("usuario", usuario);
        q.setParameter("contrasenya", contrasenya);
        Usuario verificar = (Usuario) q.getSingleResult();
        if (verificar.getContrasenya().compareTo(contrasenya) == 0) {
            idUsuario = verificar.getIdUsuario();
        }
    }

    public void cerrarConexion() {
        emf.close();
        emf = null;
        idUsuario = 0;
        unicaConexion = null;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
