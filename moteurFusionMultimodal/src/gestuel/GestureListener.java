package gestuel;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tool class to manage stroke recognition
 * @author caros
 */
public class GestureListener {

    /** 
     * Stroke saving file
     * File: ../moteurfusionmultimodal/savings
     * Contains Stroke objets with a String identifier
     */
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
        stroke = new Stroke();
        stroke.init();
    }

    public void addPointToStroke(int x, int y) {       
        stroke.addPoint(x, y);
    }

    public void normalizeStroke() {       
        stroke.normalize();
    }

    public void registerStroke(String aName) {        
        String name = aName;
        strokes.put(name, stroke);
    }

    /**
     * Find the best matching stroke between the current stroke and saved strokes
     * @return Selected stroke name after founding the best score
     */
    public String findStroke() {        
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
        return best;
    }

    /**
     * Calculate the correspondance between two Point2D lists
     * @param c: first point2D list (current stroke)
     * @param t: second point2D list (one stroke of strokes list)
     * @return result of the calcul
     */
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
