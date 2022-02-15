
import java.util.logging.Level;
import java.util.logging.Logger;
import moteurfusionmultimodal.MoteurFusionMultimodal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author chatonbrutal
 */
public class Main {   
     public static void main(String[] args) {       
        try {
            MoteurFusionMultimodal moteurFusionMultimodal = new MoteurFusionMultimodal();
        } catch (Exception ex) {
            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
