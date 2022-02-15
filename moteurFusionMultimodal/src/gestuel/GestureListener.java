/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestuel;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author caros
 */
public class GestureListener {

    private static final String FILENAME = "savings";
    private static final String FILEPATH = "../moteurfusionmultimodal/";
    private String filenameFullPath = FILEPATH + FILENAME;

    private Stroke stroke = new Stroke();
    private HashMap<String, Stroke> strokes = new HashMap();

    public GestureListener() throws Exception {
        FileInputStream file = new FileInputStream(filenameFullPath);
        ObjectInputStream ob = new ObjectInputStream(file);
        strokes = (HashMap) ob.readObject();
        ob.close();
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

    public void registerStroke(String aName) {
        System.out.println("registerStroke");
        String name = aName;
        strokes.put(name, stroke);
    }

    public String findStroke() {
        System.out.println("findStroke");
        String best = "unknown";
        double bestScore = 100000000;
        double thisScore;

        for (String k : strokes.keySet()) {
            thisScore = processScore(stroke.getPoints(), strokes.get(k).getPoints());
            if (thisScore < bestScore) {
                bestScore = thisScore;
                best = k;
            }
        }
        System.out.println("Choix : " + best);
        return best;
    }

    public double processScore(ArrayList<Point2D.Double> c, ArrayList<Point2D.Double> t) {
        double d = 0;
        int n = c.size();
        for (int i = 0; i < n; i++) {
            double cx = c.get(i).x;
            double tx = t.get(i).x;
            double cy = c.get(i).y;
            double ty = t.get(i).y;
            d += Math.sqrt((cx - tx) * (cx - tx) + (cy - ty) * (cy - ty));
        }
        return (d / n);
    }

}
