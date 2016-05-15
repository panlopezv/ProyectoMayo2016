package conexion;

import entidades.Usuario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

public class Conexion {

    private EntityManagerFactory emf;
    private static Conexion unicaConexion;
    private int idUsuario;
    private boolean esAdministrador;

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

    public static Conexion getConexion(){
        return unicaConexion;
    }

    private Conexion(String usuario, String contrasenya) {
        try{
            emf = Persistence.createEntityManagerFactory("PFerreteriaPU");
            Query q = emf.createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenya");
            q.setParameter("usuario", usuario);
            q.setParameter("contrasenya", contrasenya);
            try{
                Usuario verificar = (Usuario) q.getSingleResult();
                idUsuario = verificar.getIdUsuario();
                esAdministrador = verificar.getEsAdministrador();
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (NoResultException ex) {
                idUsuario=0;
                JOptionPane.showMessageDialog(null, "Usuario y/o contraseña inválidos.", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            emf=null;
            JOptionPane.showMessageDialog(null, "Imposible establecer conexión con el servidor.\n\rRevise su conexión e intentelo de nuevo.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cerrarConexion() {
        idUsuario = 0;
        unicaConexion = null;
        try{
            emf.close();
        } catch(NullPointerException ex){
        }
        emf = null;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public boolean getEsAdministrador(){
        return esAdministrador;
    }
    
    public EntityManagerFactory getEmf() {
        return emf;
    }
}
