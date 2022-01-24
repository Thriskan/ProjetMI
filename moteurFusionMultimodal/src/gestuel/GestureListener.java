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
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
        System.out.println("initiateStroke");
        stroke = new Stroke();
        stroke.init();
    }

    public void addPointToStroke(int x, int y) {
        System.out.println("addPointToStroke");
        stroke.addPoint(x, y);
    }

    public void normalizeStroke() {
        System.out.println("normalizeStroke");
        stroke.normalize();
    }

    public void registerStroke() {
        System.out.println("registerStroke");
        String name = controler.getjTextField1().getText();
        collection.put(name, stroke);
    }

    public String findStroke() {
        System.out.println("findStroke");
        String best = "unknown";
        double bestScore = 100000000;
        double thisScore;

        for (String k : collection.keySet()) {
            thisScore = processScore(stroke.getPoints(), collection.get(k).getPoints());
            if (thisScore < bestScore){
                bestScore = thisScore;
                best = k;
            }
        }
        System.out.println("Choix : " + best);
        return best;
    }
    
    public double processScore(ArrayList<Point2D.Double> c, ArrayList<Point2D.Double> t){
        double d = 0;
        int n = c.size();
        for (int i =0; i < n; i++){
            double cx = c.get(i).x;
            double tx = t.get(i).x;
            double cy = c.get(i).y;
            double ty = t.get(i).y;
            d += Math.sqrt((cx - tx)*(cx - tx) + (cy - ty)*(cy - ty));
        }
        return (d/n);
    }

    
    

}