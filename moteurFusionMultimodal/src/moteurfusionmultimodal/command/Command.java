/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package moteurfusionmultimodal.command;

import fr.irit.elipse.enseignement.isia.Couleur;

/**
 *
 * @author chatonbrutal
 */
public abstract class Command {
    
    private String action = "";
    private String objet = "";
    
    int posX = -1;
    int posY = -1;
    
    Couleur couleur;

    public Command() {
    }
    
    public Command addAction(String action) {
        this.action = action;
        return this;
    }
    
    public Command addObjet(String objet) {
        this.objet = objet;
        return this;
    }
    
    public Command addPositions(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        return this;
    }
    
    public Command addCouleur(Couleur couleur) {
        this.couleur = couleur;
        return this;
    }
    
    public boolean isComplete() {
        return false;
    }
}
