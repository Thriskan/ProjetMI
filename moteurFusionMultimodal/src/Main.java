
import java.util.logging.Level;
import java.util.logging.Logger;
import moteurfusionmultimodal.MoteurFusionMultimodal;

/**
 * Main class to launch project
 * @author chatonbrutal
 */
public class Main {   
    
    /**
     * IP address is different between UNIX/MAC OS and Windows OS
     */
    private static final String WINDOWS_IP = "127.255.255.255:2010";
    private static final String MAC_IP = "127.0.0.1:2010";

     public static void main(String[] args) {       
        try {
            MoteurFusionMultimodal moteurFusionMultimodal = new MoteurFusionMultimodal();
            moteurFusionMultimodal.start(WINDOWS_IP);
        } catch (Exception ex) {
            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
