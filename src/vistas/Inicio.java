/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import categorias.InterfazCategorias;
import com.toedter.calendar.JDateChooser;
import compras.InterfazCompra;
import conexion.Conexion;
import inventario.InterfazInventario;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import pagos.controlPagos;
import personas.InterfazPersonas;
import usuarios.InterfazPerfil;
import usuarios.InterfazUsuarios;
import ventas.InterfazVenta;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class Inicio extends javax.swing.JFrame {


    public static Conexion conexion;
    public static final String USER = "root";
    public static final String PASS = "root";
    
    /**
     * Creates new form Principal
     */
    public Inicio() {
        initComponents();
        this.setLocationRelativeTo(null);
        //this.getContentPane().setBackground(Color.getHSBColor(0.525f,0.33f,0.91f));
        this.getContentPane().setBackground(Color.getHSBColor(0.144f,0.09f,1f));
        tablero.setVisible(Boolean.FALSE);
        escritorio.setVisible(Boolean.FALSE);
        menuGestionar.setVisible(Boolean.FALSE);
        menuCerrarSesion.setVisible(Boolean.FALSE);
        menuUsuario.setVisible(Boolean.FALSE);
        menuAyuda.setVisible(Boolean.FALSE);
        botonCompras.setVisible(Boolean.FALSE);
        botonOperaciones.setVisible(Boolean.FALSE);
        menuReportes.setVisible(Boolean.FALSE);
    }
    
    public void limpiarEscritorio(){
        for(int i=0;i<escritorio.getComponentCount();i++){
            escritorio.getComponent(i).setVisible(Boolean.FALSE);
        }
        escritorio.removeAll();
    }
    
    /**
     * Muestra el total de ventas por fecha. La fecha debe ir entre apostrofes y de la siguiente manera 'AAAA-MM-DD'
     * @param fecha 
     */
    public void mostrarReporteDeVentasPorFecha(String fecha){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ferreteria", Inicio.USER, Inicio.PASS);
            HashMap parametros = new HashMap();
            parametros.put("fechaFiltro", fecha);
            JasperPrint print = JasperFillManager.fillReport("src\\reportes\\VentasPorFecha.jasper", parametros, con);
            JasperViewer.viewReport(print, Boolean.FALSE);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
        }
    }
        
    public void ajustar(JInternalFrame jif){
        if(jif.getWidth()>escritorio.getWidth() || jif.getHeight()>escritorio.getHeight()){
            if(this.getExtendedState()!=JFrame.MAXIMIZED_BOTH){
                escritorio.setSize(jif.getWidth()+25, jif.getHeight()+25);
                this.setSize(escritorio.getWidth()+260, jif.getHeight()+125);
                this.setLocationRelativeTo(null);
            }
        }
        jif.setLocation((escritorio.getWidth() - jif.getWidth())/2, (escritorio.getHeight() - jif.getHeight())/2);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        escritorio = new javax.swing.JDesktopPane();
        tablero = new javax.swing.JPanel();
        botonVentas = new javax.swing.JButton();
        botonInventario = new javax.swing.JButton();
        botonAbonos_Pagos = new javax.swing.JButton();
        botonCompras = new javax.swing.JButton();
        botonPersonas = new javax.swing.JButton();
        botonOperaciones = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        menuIniciarSesion = new javax.swing.JMenuItem();
        menuCerrarSesion = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuSalir = new javax.swing.JMenuItem();
        menuGestionar = new javax.swing.JMenu();
        menuCategorias = new javax.swing.JMenuItem();
        menuUsuarios = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuUsuario = new javax.swing.JMenu();
        menuPerfil = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        menuAcercade = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        escritorio.setBackground(new java.awt.Color(255, 252, 233));

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 585, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tablero.setBackground(new java.awt.Color(181, 232, 205));

        botonVentas.setText("Ventas");
        botonVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVentasActionPerformed(evt);
            }
        });

        botonInventario.setText("Inventario");
        botonInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInventarioActionPerformed(evt);
            }
        });

        botonAbonos_Pagos.setText("Abonos/Pagos");
        botonAbonos_Pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbonos_PagosActionPerformed(evt);
            }
        });

        botonCompras.setText("Compras");
        botonCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonComprasActionPerformed(evt);
            }
        });

        botonPersonas.setText("Personas");
        botonPersonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPersonasActionPerformed(evt);
            }
        });

        botonOperaciones.setText("Operaciones");
        botonOperaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOperacionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableroLayout = new javax.swing.GroupLayout(tablero);
        tablero.setLayout(tableroLayout);
        tableroLayout.setHorizontalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonAbonos_Pagos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(botonInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonOperaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tableroLayout.setVerticalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonAbonos_Pagos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        menuArchivo.setText("Archivo");

        menuIniciarSesion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menuIniciarSesion.setText("Iniciar sesión");
        menuIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIniciarSesionActionPerformed(evt);
            }
        });
        menuArchivo.add(menuIniciarSesion);

        menuCerrarSesion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuCerrarSesion.setText("Cerrar sesión");
        menuCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCerrarSesionActionPerformed(evt);
            }
        });
        menuArchivo.add(menuCerrarSesion);
        menuArchivo.add(jSeparator1);

        menuSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menuSalir.setText("Salir");
        menuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(menuSalir);

        jMenuBar1.add(menuArchivo);

        menuGestionar.setText("Gestionar");

        menuCategorias.setText("Categorías");
        menuCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCategoriasActionPerformed(evt);
            }
        });
        menuGestionar.add(menuCategorias);

        menuUsuarios.setText("Usuarios");
        menuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUsuariosActionPerformed(evt);
            }
        });
        menuGestionar.add(menuUsuarios);

        jMenuBar1.add(menuGestionar);

        menuReportes.setText("Reportes");

        jMenuItem3.setText("Reporte de ventas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuReportes.add(jMenuItem3);

        jMenuBar1.add(menuReportes);

        menuUsuario.setText("Usuario");

        menuPerfil.setText("Perfil");
        menuPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPerfilActionPerformed(evt);
            }
        });
        menuUsuario.add(menuPerfil);

        jMenuBar1.add(menuUsuario);

        menuAyuda.setText("Ayuda");

        menuAcercade.setText("Acerca de");
        menuAyuda.add(menuAcercade);

        jMenuBar1.add(menuAyuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(escritorio)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(escritorio))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIniciarSesionActionPerformed
        // TODO add your handling code here:
        if(conexion == null){
            JTextField user = new JTextField();
            JTextField pass = new JPasswordField();
            Object[] message = {
                "Usuario:", user,
                "Contraseña:", pass
            };
            int opcion = JOptionPane.showConfirmDialog(this, message, "Inicio de sesión.", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                conexion=Conexion.getConexion(user.getText(), pass.getText());
                if(conexion!=null){
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "", JOptionPane.INFORMATION_MESSAGE);
                    
                    tablero.setVisible(Boolean.TRUE);
                    escritorio.setVisible(Boolean.TRUE);
                    menuCerrarSesion.setVisible(Boolean.TRUE);
                    menuUsuario.setVisible(Boolean.TRUE);
                    menuAyuda.setVisible(Boolean.TRUE);
                    menuReportes.setVisible(Boolean.TRUE);
                    if(Conexion.getConexion().getEsAdministrador()){
                        menuGestionar.setVisible(Boolean.TRUE);
                        botonCompras.setVisible(Boolean.TRUE);
                        botonOperaciones.setVisible(Boolean.TRUE);
                    }
                } else{
                    JOptionPane.showMessageDialog(this, "Usuario y/o contraseña inválidos.", "", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Inicio de sesión cancelado.", "", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Ya existe una sesion abierta.\n\r"
                + "Si desea ingresar como otro usuario, cierre esta sesion e ingrese de nuevo.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_menuIniciarSesionActionPerformed

    private void menuCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCerrarSesionActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(this, "¿Realmente desea cerrar sesión?", "", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if(conexion!=null){
                conexion.cerrarConexion();
                conexion=null;
            }
            limpiarEscritorio();
            tablero.setVisible(Boolean.FALSE);
            escritorio.setVisible(Boolean.FALSE);
            menuGestionar.setVisible(Boolean.FALSE);
            menuCerrarSesion.setVisible(Boolean.FALSE);
            menuUsuario.setVisible(Boolean.FALSE);
            menuAyuda.setVisible(Boolean.FALSE);
            botonCompras.setVisible(Boolean.FALSE);
            botonOperaciones.setVisible(Boolean.FALSE);
            menuReportes.setVisible(Boolean.FALSE);
        }
    }//GEN-LAST:event_menuCerrarSesionActionPerformed

    private void menuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalirActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(this, "¿Realmente desea salir?", "", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if(conexion!=null){
                conexion.cerrarConexion();
                conexion=null;
            }
            System.exit(0);
        }
    }//GEN-LAST:event_menuSalirActionPerformed

    private void menuPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPerfilActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazPerfil ip = new InterfazPerfil(Conexion.getConexion().getIdUsuario());
        escritorio.add(ip);
        ajustar(ip);
    }//GEN-LAST:event_menuPerfilActionPerformed

    private void botonVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVentasActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazVenta venta = new InterfazVenta();
        escritorio.add(venta);
        ajustar(venta);
    }//GEN-LAST:event_botonVentasActionPerformed

    private void botonInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInventarioActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazInventario inventario = new InterfazInventario();
        escritorio.add(inventario);
        ajustar(inventario);
    }//GEN-LAST:event_botonInventarioActionPerformed

    private void botonAbonos_PagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbonos_PagosActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        //        VistaPersonas vp = new VistaPersonas();
        //        escritorio.add(vp);
        //        ajustar(vp);
        //mostrarReporte(3);
        controlPagos cp = new controlPagos();
        escritorio.add(cp);
        ajustar(cp);
    }//GEN-LAST:event_botonAbonos_PagosActionPerformed

    private void botonComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonComprasActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazCompra compra = new InterfazCompra();
        escritorio.add(compra);
        ajustar(compra);
    }//GEN-LAST:event_botonComprasActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(this, "¿Realmente desea salir?", "", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if(conexion!=null){
                conexion.cerrarConexion();
                conexion=null;
            }
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void menuCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCategoriasActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazCategorias inf = new InterfazCategorias();
        escritorio.add(inf);
        ajustar(inf);
    }//GEN-LAST:event_menuCategoriasActionPerformed

    private void menuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUsuariosActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazUsuarios inu = new InterfazUsuarios();
        escritorio.add(inu);
        ajustar(inu);
    }//GEN-LAST:event_menuUsuariosActionPerformed

    private void botonPersonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPersonasActionPerformed
        // TODO add your handling code here:
        limpiarEscritorio();
        InterfazPersonas vp = new InterfazPersonas();
        escritorio.add(vp);
        ajustar(vp);
    }//GEN-LAST:event_botonPersonasActionPerformed

    private void botonOperacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOperacionesActionPerformed
        // TODO add your handling code here:
        InterfazOperaciones io = new InterfazOperaciones();
        limpiarEscritorio();
        escritorio.add(io);
        ajustar(io);
    }//GEN-LAST:event_botonOperacionesActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        JDateChooser jdc = new JDateChooser();
        jdc.setDate(new java.util.Date());
        Object[] message = {
            "Fecha", jdc
        };
        int opcion = JOptionPane.showConfirmDialog(this, message, "Reporte de ventas", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(fmt.format(jdc.getDate()));
            mostrarReporteDeVentasPorFecha("'"+fmt.format(jdc.getDate())+"'");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("System".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAbonos_Pagos;
    private javax.swing.JButton botonCompras;
    private javax.swing.JButton botonInventario;
    private javax.swing.JButton botonOperaciones;
    private javax.swing.JButton botonPersonas;
    private javax.swing.JButton botonVentas;
    public javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem menuAcercade;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenuItem menuCategorias;
    private javax.swing.JMenuItem menuCerrarSesion;
    private javax.swing.JMenu menuGestionar;
    private javax.swing.JMenuItem menuIniciarSesion;
    private javax.swing.JMenuItem menuPerfil;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenuItem menuSalir;
    private javax.swing.JMenu menuUsuario;
    private javax.swing.JMenuItem menuUsuarios;
    private javax.swing.JPanel tablero;
    // End of variables declaration//GEN-END:variables
}
