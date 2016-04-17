/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.util.Observable;

/**
 * Clase que generaliza la clase Observable y que permite actualizar una vista al modificar
 * datos que muestra esta en otra vista.
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class Observador extends Observable {
    
    public Observador() {
    }
    
    /**
     * Metodo que se ejecuta desde cualquier observador y da la orden de actualizar la vista
     * a los otros observadores.
     */
    public void actualizarObservadores(){
        setChanged();
        notifyObservers();
    }
}
