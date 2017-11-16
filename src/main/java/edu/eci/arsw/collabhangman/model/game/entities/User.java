/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game.entities;

import edu.eci.arsw.collabhangman.model.game.Score;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author hcadavid
 */
@Document(collection = "users")
public class User {
    @Id
    private int id;
    private String name;
    private String photoUrl;
    private List<Score> scores;
    private long puntajeMaximo;
    
    public User(int id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.scores = new ArrayList<>();
        this.puntajeMaximo = 0;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * @return the scores
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    /**
     * @return the puntajeMaximo
     */
    public long getPuntajeMaximo() {
        return puntajeMaximo;
    }

    /**
     * @param puntajeMaximo the puntajeMaximo to set
     */
    public void setPuntajeMaximo(long puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }
    
    
    
}
