/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package moteurfusionmultimodal;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import gestuel.GestureListener;
import gui.PaletteController;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Pointeur;
import tools.Utils;

/**
 * Core class of the application Manage gesture + vocal behaviors and define all
 * ivy listeners
 *
 * @author caros
 */
public class MoteurFusionMultimodal {

    private final Ivy bus;
    private final PaletteController controler;
    private final Pointeur pointeur;
    private final StateMachine machine;

    //flag used to activate shape selection if multiples shapes are detected at one coordinate
    private boolean requestShapes = false;

    //list of detected shapes during the process of shape selection
    private final ArrayList<String> registeredShapes;

    //flag used to match selected shape with a detected shape by color
    private boolean colorMatching = false;
    private String selectedShape;
    private String requestedColor;

    //flag used to manage a timer to wait palette response during shape detection
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

    public MoteurFusionMultimodal() throws Exception {
        bus = new Ivy("BUS IVY", "IVY Ready", null);

        controler = new PaletteController();
        controler.setVisible(true);

        pointeur = new Pointeur();

        gestureListener = new GestureListener();

        machine = new StateMachine();

        registeredShapes = new ArrayList<>();
    }

    public void start(String ip) throws Exception {
        bus.start(ip);

        bus.sendToSelf(true);
        initiateBusActivity();

    }

    private void initiateBusActivity() throws IvyException {

        /**
         * Palette mouse pressed event listener Init a stroke object and add the
         * first point coordinate
         */
        bus.bindMsg("Palette:MousePressed x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.initiateStroke();
            gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        });

        /**
         * Palette mouse released event listener Normalize current stroke object
         * If learning mode is activated, add it to the savings file If
         * recognition mode is activated, compare the current stroke object with
         * saved strokes
         */
        bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.normalizeStroke();

            if (controler.getCurrentMode() == PaletteController.Mode.APP) {
                gestureListener.registerStroke(controler.getTfApprentissage().getText());
            } else {
                String best = gestureListener.findStroke();
                manageCommande(best);
                controler.getLbReconnaissance().setText(best);
                controler.getjLabel3().setText("Dictez la consigne");
            }
        });

        /**
         * Palette mouse dragged event listener Add point during the motion
         * event (pressed -> dragged -> released)
         */
        bus.bindMsg("Palette:MouseDragged x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        });

        /**
         * Palette mouse moved event listener Save the last known coordinate to
         * the current pointor object
         */
        bus.bindMsg("Palette:MouseMoved x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            pointeur.setPosition(new Point2D.Double(Double.valueOf(args[0]), Double.valueOf(args[1])));
        });

        /**
         * Palette shape detection event listener Save all object names detected
         * and received from the palette
         */
        bus.bindMsg("Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", (IvyClient client, String[] args) -> {
            registeredShapes.add(args[2]);
        });

        /**
         * Palette shape detection end event listener Stop shape detection
         * request
         */
        bus.bindMsg("Palette:FinTesterPoint x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            requestShapes = false;
        });

        /**
         * Palette object informations event listener Detect current object
         * color and define selected shape if this color is matching the needed
         * color
         */
        bus.bindMsg("Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", (IvyClient client, String[] args) -> {
            if (args[5].equals(Utils.convertToEng(requestedColor))) {
                colorMatching = true;
                selectedShape = args[0];
            }
            infoReceived = true;
        });

        /**
         * voice event listener
         */
        bus.bindMsg("sra5 Text=(.*) Confidence=(.*)", (IvyClient client, String[] args) -> {
            // Acceptance threshold to perform voice processus is 0.6
            if (controler.isMicroActivated() && Utils.findConfidence(args[1]) > 70) {
                 System.out.println("arg : " + args[0]);
                //Select current parameter definition with voice text
                Commande voice_commande = findParametersType(args[0]);
                switch (voice_commande) {
                    //Execute drawing command
                    case DRAW -> {
                        if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE || machine.getCurrent_state() == StateMachine.State.DRAW_RECT) {
                            //Translate french detected color to english color
                            String color = Utils.defineColor(args[0]);
                            String msg;
                            if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE) {
                                msg = "Palette:CreerEllipse x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=100" + " couleurFond=" + color + " couleurContour=" + color;
                            } else {
                                msg = "Palette:CreerRectangle x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=60" + " couleurFond=" + color + " couleurContour=" + color;

                            }
                            controler.getjLabel3().setText("...");
                            try {
                                bus.sendMsg(msg);
                            } catch (IvyException ex) {
                                System.out.println("pb là");
                                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            machine.voice_draw();
                        }
                        
                    }
                    //Define a command parameter (object)
                    case CHOSE -> {
                        requestShapes = true;
                        registeredShapes.clear();

                        //Need to get an object name, detected at the current coordinate
                        String msg = "Palette:TesterPoint x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y;
                        {
                            try {
                                bus.sendMsg(msg);
                            } catch (IvyException ex) {
                                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        //Waiting all palette messages from detecting shapes
                        while (requestShapes) {
                            //System.out.println("requesting");
                        }

                        //If multiples shapes are detected at the coordinate
                        if (registeredShapes.size() > 1) {
                            System.out.println("need color to select");
                            controler.getjLabel3().setText("Donnez une couleur pour choisir la bonne forme");
                            machine.voice_object(false);
                        } else {
                            System.out.println("registered : " + registeredShapes);
                            if (machine.getCurrent_state() == StateMachine.State.DELETE) {
                                msg = "Palette:SupprimerObjet nom=" + registeredShapes.get(0);
                                try {
                                    bus.sendMsg(msg);
                                } catch (IvyException ex) {
                                    Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                controler.getjLabel3().setText("...");
                                machine.voice_object(true);
                            } else if (machine.getCurrent_state() == StateMachine.State.MOVE) {
                                selectedShape = registeredShapes.get(0);
                                controler.getjLabel3().setText("Donnez la position où déplacer l'objet");
                                 machine.voice_object(true);
                            } else {
                                System.out.println("erreur when requesting move");
                            }
                        }
                    }

                    //Define a command parameter (color)
                    case COULEUR -> {
                        selectedShape = "";
                        requestedColor = args[0];
                        colorMatching = false;
                        int i = 0;
                        String msg;

                        //Retrieve the color parameter from detected shapes
                        while (!colorMatching && i < registeredShapes.size()) {
                            infoReceived = false;
                            msg = "Palette:DemanderInfo nom=" + registeredShapes.get(i);
                            try {
                                bus.sendMsg(msg);
                            } catch (IvyException ex) {
                                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            //Waiting for receive the color parameter from the palette
                            while (!infoReceived) {

                            }
                            i++;
                        }
                        //Need a selected object to perform an action 
                        if (!"".equals(selectedShape)) {
                            if (null == machine.getCurrent_state()) {
                                System.out.println("erreur when select color. State is null");
                            } else {
                                switch (machine.getCurrent_state()) {
                                    case MOVE -> {
                                        System.out.println("need position");
                                        controler.getjLabel3().setText("Donnez la position où déplacer l'objet");
                                        machine.voice_color(true);
                                    }
                                    //Execute Delete command after the definition of a selected object
                                    case DELETE -> {
                                        msg = "Palette:SupprimerObjet nom=" + selectedShape;
                                        try {
                                            bus.sendMsg(msg);
                                        } catch (IvyException ex) {
                                            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        controler.getjLabel3().setText("...");
                                        machine.voice_color(true);
                                    }
                                    default ->
                                        System.out.println("erreur. State is not defined at this part");
                                }
                            }
                        }
                    }

                    //Execute Move command 
                    case DEPLACER -> {
                        String msg = "Palette:DeplacerObjetAbsolu nom=" + selectedShape + " x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y;
                        try {
                            bus.sendMsg(msg);
                            selectedShape = "";
                            controler.getjLabel3().setText("...");
                            machine.voice_move_position();
                        } catch (IvyException ex) {
                            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    case SUPPRIMER -> {
                        controler.getjLabel3().setText("...");
                    }
                    default -> {
                        System.out.println("commande vocale inconnue");
                        controler.getjLabel3().setText("...");
                    }
                }

            }
            else {
                controler.getjLabel3().setText("Commande non reconnue, recommencez");
            }
            System.out.println("state after voice : " + machine.getCurrent_state());
        });

        //Method called if a whole clean up is needed. Used with an interface button      
        controler.getBtCleanAll().addActionListener((ActionEvent e) -> {
            String msg = "Palette:SupprimerTout";
            try {
                bus.sendMsg(msg);
            } catch (IvyException ex) {
                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
            }
            machine.initialize();
        });
    }

    //Test method to check palette / system registering process
    public void drawPoint(String x, String y, String color) {
        String msg = "Palette:CreerEllipse x=" + x + " y=" + y + " longueur=1" + " hauteur=1" + " couleurFond=" + color + " couleurContour=" + color;
        try {
            bus.sendMsg(msg);
        } catch (IvyException ex) {
            Logger.getLogger(GestureListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Convert detected command to command object
    public void manageCommande(String commande) {
        switch (commande) {
            case "RECTANGLE" -> {
                current_commande = Commande.RECTANGLE;
                machine.draw_rect();
            }
            case "ELLIPSE" -> {
                current_commande = Commande.ELLIPSE;
                machine.draw_ellipse();
            }
            case "DEPLACER" -> {
                current_commande = Commande.DEPLACER;
                machine.draw_m();
            }
            case "SUPPRIMER" -> {
                current_commande = Commande.SUPPRIMER;
                machine.draw_cross();
            }
            default ->
                System.out.println("commande impossible");
        }
    }

    //Convert detected parameter to parameter object
    public Commande findParametersType(String voiceCommand) {
        return switch (voiceCommand) {
            case "cet objet" ->
                Commande.CHOSE;
            case "cet rectangle" ->
                Commande.CHOSE;
            case "cette ellipse" ->
                Commande.CHOSE;
            case "ici" ->
                Commande.DEPLACER;
            case "la" ->
                Commande.DEPLACER;
            case "a cette position" ->
                Commande.DEPLACER;
            case "noir" ->
                Commande.COULEUR;
            case "bleu" ->
                Commande.COULEUR;
            case "rouge" ->
                Commande.COULEUR;
            default ->
                Commande.DRAW;
        };
    }
}
