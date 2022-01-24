/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestuel;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;
import java.util.HashMap;

/**
 *
 * @author caros
 */
public class gestureListener implements IvyMessageListener{
    
    private Ivy bus;
    private Stroke stroke = new Stroke();
    private HashMap<String, Stroke> collection = new HashMap();

    @Override
    public void receive(IvyClient ic, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
