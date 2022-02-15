
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
    
    private static final String WINDOWS_IP = "127.255.255.255:2010";
    private static final String MAC_IP = "127.0.0.1:2010";

     public static void main(String[] args) {       
        try {
            MoteurFusionMultimodal moteurFusionMultimodal = new MoteurFusionMultimodal(MAC_IP);
        } catch (Exception ex) {
            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
