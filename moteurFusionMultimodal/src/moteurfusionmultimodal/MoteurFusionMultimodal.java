/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package moteurfusionmultimodal;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import gestuel.GestureListener;
import gui.PaletteController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pointage.Pointeur;

/**
 *
 * @author caros
 */
public class MoteurFusionMultimodal {

    public String SAVEFILE = "savings";

    private Ivy bus;
    private PaletteController controler;
    private Pointeur pointeur;

    private GestureListener gestureListener;

    private HashMap<String, Shape> ensemble;

    public enum Commande {
        AUCUNE,
        RECTANGLE,
        ELLIPSE,
        DEPLACER,
        SUPPRIMER
    }

    private Commande current_commande = Commande.AUCUNE;

    public MoteurFusionMultimodal() throws IvyException {

        bus = new Ivy("BUS IVY", "IVY Ready", null);
        controler = new PaletteController();
        controler.setVisible(true);

        pointeur = new Pointeur();

        gestureListener = new GestureListener(bus, controler, this);


        /* Initiate components */
        // controler.setInitialState();
        //controler.setServeur(this);
        bus.start("127.255.255.255:2010");
        bus.sendToSelf(true);
        initiateBusActivity();

    }

    private void initiateBusActivity() throws IvyException {

        bus.bindMsg("Palette:MousePressed x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println(args[0]);
                String st = "Le point de départ est (" + args[0] + " , " + args[1] + ")\n";

                //drawPoint(args[0], args[1], "GREEN");
                gestureListener.initiateStroke();
                gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
            }
        ;
        });
         
         bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String st = "Le point d'arrivée est (" + args[0] + " , " + args[1] + ")\n";

                //drawPoint(args[0], args[1], "RED");
                gestureListener.normalizeStroke();

                if (controler.getCurrentMode() == PaletteController.Mode.APP) {
                    gestureListener.registerStroke();
                } else {
                    String best = gestureListener.findStroke();
                    controler.getjLabel1().setText(best);
                }

            }
        ;

        }

);
         bus.bindMsg("Palette:MouseDragged x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println(args[0]);
                String st = "Drag à (" + args[0] + " , " + args[1] + ")\n";

                //drawPoint(args[0], args[1], "BLACK");
                gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
            }
        ;
        }


);
        bus.bindMsg("Palette:MouseMoved x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println("pointeur : " + args[0]);
                pointeur.setPosition(new Point2D.Double(Double.valueOf(args[0]), Double.valueOf(args[1])));
                // System.out.println("Position pointeur : " + pointeur.getPosition().x + " , " + pointeur.getPosition().y);
            }
        ;
        }

    );
        
        //sra5 Text=chaîne_orthographique Confidence=taux_de_confiance
        bus.bindMsg("sra5 Text=(.*) Confidence=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                //String mots[] = args[0].split(" ");
                //System.out.println("couleur : " + mots[0]);
                if (controler.isMicro()){
                    String color = defineColor(args[0]);
                    String msg = "Palette:CreerEllipse x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=100" + " couleurFond=" + color + " couleurContour=" + color;
                    System.out.println("Position pointeur : " + pointeur.getPosition().x + " - " + pointeur.getPosition().y);
                    try {
                        bus.sendMsg(msg);
                    } catch (IvyException ex) {
                        Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                

            }
        ;
        }

    );
        
        controler.getjButton1().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "Palette:SupprimerTout";
                System.out.println(msg);
                try {
                    bus.sendMsg(msg);
                } catch (IvyException ex) {
                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public String defineColor(String ligne) {
        String mots[] = ligne.split(" ");
        String c = "";
        if (mots[0] == "...") {
            c = mots[1];
        } else {
            c = mots[0];
        }
        System.out.println("couleur = " + c);
        return switch (c) {
            case "rouge" ->
                "255:0:0";
            case "bleu" ->
                "0:0:255";
            default ->
                "0:0:0";
        };
    }

    public void drawPoint(String x, String y, String color) {

        String msg = "Palette:CreerEllipse x=" + x + " y=" + y + " longueur=1" + " hauteur=1" + " couleurFond=" + color + " couleurContour=" + color;
        try {
            bus.sendMsg(msg);
        } catch (IvyException ex) {
            Logger.getLogger(GestureListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IvyException {
        new MoteurFusionMultimodal();
    }

}
