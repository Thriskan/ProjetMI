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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Pointeur;

/**
 *
 * @author caros
 */
public class MoteurFusionMultimodal {


    private final Ivy bus;
    private final PaletteController controler;
    private final Pointeur pointeur;
    private final StateMachine machine;

    private boolean requestShapes = false;
    private final ArrayList<String> registeredShapes;
    private boolean colorMatching = false;
    private String selectedShape;
    private String requestedColor;
    private boolean infoReceived;

    private final GestureListener gestureListener;   


    public enum Commande {
        AUCUNE,
        RECTANGLE,
        ELLIPSE,
        DEPLACER,
        SUPPRIMER,
        DRAW,
        CHOSE,
        COULEUR
    }

    private Commande current_commande = Commande.AUCUNE;

    public MoteurFusionMultimodal() throws Exception{
        bus = new Ivy("BUS IVY", "IVY Ready", null);
        
        controler = new PaletteController();
        controler.setVisible(true);

        pointeur = new Pointeur();

        gestureListener = new GestureListener();

        machine = new StateMachine();

        registeredShapes = new ArrayList<String>();

        /* Initiate components */
        // controler.setInitialState();
        //controler.setServeur(this);

        //FOR WINDOWS
        //bus.start("127.255.255.255:2010");
        
        //FOR MAC
        bus.start("127.0.0.1:2010");
        
        
        bus.sendToSelf(true);
        initiateBusActivity();
    }

    private void initiateBusActivity() throws IvyException {

        bus.bindMsg("Palette:MousePressed x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
                        
            /*   
            String st = "Le point de départ est (" + args[0] + " , " + args[1] + ")\n";
            System.out.println(st);
            
            drawPoint(args[0], args[1], "GREEN");
            */
            
            gestureListener.initiateStroke();
            gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        });
         
         bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
             String st = "Le point d'arrivée est (" + args[0] + " , " + args[1] + ")\n";
             
             //drawPoint(args[0], args[1], "RED");
             gestureListener.normalizeStroke();
             
             if (controler.getCurrentMode() == PaletteController.Mode.APP) {
                 gestureListener.registerStroke(controler.getTfApprentissage().getText());
             } else {
                 String best = gestureListener.findStroke();
                 manageCommande(best);
                 controler.getLbReconnaissance().setText(best);
             }
             
             /*String msg = "Palette:ModifierCurseur type=DEFAULT";
             try {
             bus.sendMsg(msg);
             } catch (IvyException ex) {
             Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        });
         bus.bindMsg("Palette:MouseDragged x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println(args[0]);
                String st = "Drag à (" + args[0] + " , " + args[1] + ")\n";
                System.out.println(st);
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
        
        bus.bindMsg("Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println("pointeur : " + args[0]);
                registeredShapes.add(args[2]);
                // System.out.println("Position pointeur : " + pointeur.getPosition().x + " , " + pointeur.getPosition().y);
            }
        ;
        }

    );
        
         bus.bindMsg("Palette:FinTesterPoint x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println("pointeur : " + args[0]);
                System.out.println("fin tester");
                requestShapes = false;
            }
        ;
        }

    );
         bus.bindMsg("Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                //System.out.println("pointeur : " + args[0]);
                System.out.println("resultat " + args[0] + " : " + args[5] + " " + args[5].length());
                System.out.println("recherche " + convertToEng(requestedColor) + convertToEng(requestedColor).length());
                System.out.println(args[5].equals(convertToEng(requestedColor)));
                if (args[5].equals(convertToEng(requestedColor))) {
                    colorMatching = true;
                    selectedShape = args[0];
                }
                infoReceived = true;
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
                if (controler.isMicroActivated()) {
                    Commande voice_commande = findParametersType(args[0]);
                    switch (voice_commande) {
                        case DRAW:
                            if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE || machine.getCurrent_state() == StateMachine.State.DRAW_RECT) {
                                String color = defineColor(args[0]);
                                String msg;
                                if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE) {
                                    msg = "Palette:CreerEllipse x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=100" + " couleurFond=" + color + " couleurContour=" + color;
                                } else {
                                    msg = "Palette:CreerRectangle x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=60" + " couleurFond=" + color + " couleurContour=" + color;

                                }
                                //System.out.println("Position pointeur : " + pointeur.getPosition().x + " - " + pointeur.getPosition().y);
                                machine.voice_draw();
                                try {
                                    bus.sendMsg(msg);
                                } catch (IvyException ex) {
                                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            machine.voice_draw();
                            break;
                        case CHOSE:
                            requestShapes = true;
                            registeredShapes.clear();
                            String msg = "Palette:TesterPoint x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y;
                             {
                                try {
                                    bus.sendMsg(msg);
                                } catch (IvyException ex) {
                                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            while (requestShapes) {
                                System.out.println("requesting");
                            }
                            System.out.println("finish requetsing : " + registeredShapes);
                            if (registeredShapes.size() > 1) {
                                System.out.println("need color to select");
                            } else {
                                System.out.println("registered : " + registeredShapes);
                                if (machine.getCurrent_state() == StateMachine.State.DELETE) {
                                    System.out.println("deleted : " + registeredShapes.get(0));
                                    msg = "Palette:SupprimerObjet nom=" + registeredShapes.get(0);
                                    try {
                                        bus.sendMsg(msg);
                                    } catch (IvyException ex) {
                                        Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else if (machine.getCurrent_state() == StateMachine.State.MOVE) {
                                    System.out.println("want to move");
                                    selectedShape = registeredShapes.get(0);
                                    /*msg = "Palette:ModifierCurseur type=HAND";
                                    try {
                                        bus.sendMsg(msg);
                                    } catch (IvyException ex) {
                                        Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                    }*/
                                } else {
                                    System.out.println("erreur");
                                }
                                machine.voice_object();
                            }
                            break;

                        case COULEUR:
                            System.out.println("voix donne couleur");
                            selectedShape = "";
                            requestedColor = args[0];
                            colorMatching = false;
                            int i = 0;
                            while (!colorMatching && i < registeredShapes.size()) {
                                infoReceived = false;
                                msg = "Palette:DemanderInfo nom=" + registeredShapes.get(i);
                                try {
                                    bus.sendMsg(msg);
                                } catch (IvyException ex) {
                                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                while (!infoReceived) {

                                }
                                i++;
                            }
                            System.out.println("selected from color : " + selectedShape);
                            if (!"".equals(selectedShape)) {
                                if (null == machine.getCurrent_state()) {
                                    System.out.println("erreur");
                                } else {
                                    switch (machine.getCurrent_state()) {
                                        case MOVE:
                                            System.out.println("need position");
                                            break;
                                        case DELETE:
                                            msg = "Palette:SupprimerObjet nom=" + selectedShape;
                                            try {
                                                bus.sendMsg(msg);
                                            } catch (IvyException ex) {
                                                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            break;
                                        default:
                                            System.out.println("erreur");
                                            break;
                                    }
                                }
                                machine.voice_object();
                            }
                            break;

                        case DEPLACER:
                            System.out.println("voix donne deplacer");
                            msg = "Palette:DeplacerObjetAbsolu nom=" + selectedShape + " x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y;
                            System.out.println("Deplacer : " + msg);
                             {
                                try {
                                    bus.sendMsg(msg);
                                } catch (IvyException ex) {
                                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        case SUPPRIMER:
                            System.out.println("voix donne supprimer");
                            break;
                        default:
                            System.out.println("commande vocale inconnue");
                            break;
                    }

                }

            }
        }
        );

        controler.getBtCleanAll().addActionListener(new ActionListener() {

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
        if ("...".equals(mots[0])) {
            System.out.println("1 : ... -> " + mots[1]);
            if ("la".equals(mots[1]) || "ici".equals(mots[1]) || "a cette position".equals(mots[1])) {
                c = mots[2];
            } else {
                c = mots[1];
            }
        } else {
            System.out.println("1 : OK -> " + mots[0]);
            if ("la".equals(mots[0]) || "ici".equals(mots[0]) || "a cette position".equals(mots[0])) {
                c = mots[1];
            } else {
                c = mots[0];
            }
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

    public void manageCommande(String commande) {
        System.out.println(commande);
        switch (commande) {
            case "RECTANGLE":
                current_commande = Commande.RECTANGLE;
                machine.draw_rect();
                break;
            case "ELLIPSE":
                current_commande = Commande.ELLIPSE;
                machine.draw_ellipse();
                break;
            case "DEPLACER":
                current_commande = Commande.DEPLACER;
                machine.draw_m();
                break;
            case "SUPPRIMER":
                current_commande = Commande.SUPPRIMER;
                machine.draw_cross();
                break;
            default:
                System.out.println("commande impossible");
                break;
        }
    }

    public Commande findParametersType(String voiceCommand) {
        switch (voiceCommand) {
            case "cet objet":
                return Commande.CHOSE;
            case "cet rectangle":
                return Commande.CHOSE;
            case "cette ellipse":
                return Commande.CHOSE;
            case "ici":
                return Commande.DEPLACER;
            case "la":
                return Commande.DEPLACER;
            case "a cette position":
                return Commande.DEPLACER;
            case "noir":
                return Commande.COULEUR;
            case "bleu":
                return Commande.COULEUR;
            case "rouge":
                return Commande.COULEUR;
            default:
                return Commande.DRAW;
        }
        /*if (voiceCommand.contains("cet objet") || voiceCommand.contains("ce rectangle") || voiceCommand.contains("cette ellipse")) {
            if (voiceCommand.contains("ici") || voiceCommand.contains("la") || voiceCommand.contains("a cette position")) {
                return Commande.DEPLACER;
            } else {
                return Commande.CHOSE;
            }
        } else {
            return Commande.DRAW;
        }*/
    }

    /*public void searchColor(String color) throws IvyException {
        
    }*/
    public String convertToEng(String c) {
        switch (c) {
            case "noir":
                return "black";
            case "rouge":
                return "red";
            case "bleu":
                return "blue";
            default:
                return "black";
        }
    }
}
