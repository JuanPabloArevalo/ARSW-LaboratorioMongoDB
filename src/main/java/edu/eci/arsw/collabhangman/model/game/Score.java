/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game;

import java.util.Date;

/**
 *
 * @author 2087559
 */
public class Score {
    
    private Date fechaObtencion;
    private long valorPuntaje;

    /**
     * @return the fechaObtencion
     */
    public Date getFechaObtencion() {
        return fechaObtencion;
    }

    /**
     * @param fechaObtencion the fechaObtencion to set
     */
    public void setFechaObtencion(Date fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    /**
     * @return the valorPuntaje
     */
    public long getValorPuntaje() {
        return valorPuntaje;
    }

    /**
     * @param valorPuntaje the valorPuntaje to set
     */
    public void setValorPuntaje(long valorPuntaje) {
        this.valorPuntaje = valorPuntaje;
    }
    
    
    
}
