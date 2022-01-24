/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestuel;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import gui.PaletteController;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caros
 */
public class GestureListener implements IvyMessageListener {

    private Ivy bus;
    private Stroke stroke = new Stroke();
    private HashMap<String, Stroke> collection = new HashMap();
    private PaletteController controler;

    public GestureListener(Ivy aBus, PaletteController aControler) {
        this.bus = aBus;
        this.controler = aControler;

    }

    public void drawPoint(String x, String y, String color) {

        String msg = "Palette:CreerEllipse x=" + x + " y=" + y + " longueur=1" + " hauteur=1" + " couleurFond=" + color + " couleurContour=" + color;
        try {
            bus.sendMsg(msg);
        } catch (IvyException ex) {
            Logger.getLogger(GestureListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receive(IvyClient ic, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void initiateStroke() {
        stroke = new Stroke();
        stroke.init();
    }

    public void addPointToStroke(int x, int y) {
        stroke.addPoint(x, y);
    }

    public void normalizeStroke() {
        stroke.normalize();
    }

    public void registerStroke() {
        String name = controler.getjTextField1().getText();
        collection.put(name, stroke);
    }

    public void findStroke() {

        String best = "unknown";
        double bestScore = 100000000;
        double thisDist = stroke.getPathLength();
        System.out.println("Initial dist : " + thisDist);

        for (String k : collection.keySet()) {
            double dist = collection.get(k).getPathLength();
            System.out.println(k + " : " + dist + " -> " + Math.abs(dist - thisDist));
            if (Math.abs(dist - thisDist) < bestScore) {
                best = k;
                bestScore = Math.abs(dist - thisDist);
            }
        }
        System.out.println("Choix : " + best);
    }

    
    

}