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
        bus.bindMsg("Palette:MousePressed x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.initiateStroke();
            gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        });

        bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.normalizeStroke();

            if (controler.getCurrentMode() == PaletteController.Mode.APP) {
                gestureListener.registerStroke(controler.getTfApprentissage().getText());
            } else {
                String best = gestureListener.findStroke();
                manageCommande(best);
                controler.getLbReconnaissance().setText(best);
            }
        });

        bus.bindMsg("Palette:MouseDragged x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        });

        bus.bindMsg("Palette:MouseMoved x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            pointeur.setPosition(new Point2D.Double(Double.valueOf(args[0]), Double.valueOf(args[1])));
        });

        bus.bindMsg("Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", (IvyClient client, String[] args) -> {
            registeredShapes.add(args[2]);
        });

        bus.bindMsg("Palette:FinTesterPoint x=(.*) y=(.*)", (IvyClient client, String[] args) -> {
            requestShapes = false;
        });
        bus.bindMsg("Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", (IvyClient client, String[] args) -> {
            if (args[5].equals(Utils.convertToEng(requestedColor))) {
                colorMatching = true;
                selectedShape = args[0];
            }
            infoReceived = true;
        });

        //sra5 Text=chaîne_orthographique Confidence=taux_de_confiance
        bus.bindMsg("sra5 Text=(.*) Confidence=(.*)", (IvyClient client, String[] args) -> {
            if (controler.isMicroActivated()) {
                Commande voice_commande = findParametersType(args[0]);
                switch (voice_commande) {
                    case DRAW -> {
                        if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE || machine.getCurrent_state() == StateMachine.State.DRAW_RECT) {
                            String color = Utils.defineColor(args[0]);
                            String msg;
                            if (machine.getCurrent_state() == StateMachine.State.DRAW_ELLIPSE) {
                                msg = "Palette:CreerEllipse x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=100" + " couleurFond=" + color + " couleurContour=" + color;
                            } else {
                                msg = "Palette:CreerRectangle x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y + " longueur=100" + " hauteur=60" + " couleurFond=" + color + " couleurContour=" + color;

                            }
                            machine.voice_draw();
                            try {
                                bus.sendMsg(msg);
                            } catch (IvyException ex) {
                                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        machine.voice_draw();
                    }
                    case CHOSE -> {
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
                                selectedShape = registeredShapes.get(0);
                            } else {
                                System.out.println("erreur when requesting move");
                            }
                            machine.voice_object();
                        }
                    }

                    case COULEUR -> {
                        selectedShape = "";
                        requestedColor = args[0];
                        colorMatching = false;
                        int i = 0;
                        String msg;
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
                        if (!"".equals(selectedShape)) {
                            if (null == machine.getCurrent_state()) {
                                System.out.println("erreur when select color. State is null");
                            } else {
                                switch (machine.getCurrent_state()) {
                                    case MOVE ->
                                        System.out.println("need position");
                                    case DELETE -> {
                                        msg = "Palette:SupprimerObjet nom=" + selectedShape;
                                        try {
                                            bus.sendMsg(msg);
                                        } catch (IvyException ex) {
                                            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    default ->
                                        System.out.println("erreur. State is not defined at this part");
                                }
                            }
                            machine.voice_object();
                        }
                    }

                    case DEPLACER -> {
                        String msg = "Palette:DeplacerObjetAbsolu nom=" + selectedShape + " x=" + (int) pointeur.getPosition().x + " y=" + (int) pointeur.getPosition().y;
                        try {
                            bus.sendMsg(msg);
                            selectedShape = "";
                        } catch (IvyException ex) {
                            Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    case SUPPRIMER ->
                        System.out.println("voix donne supprimer");
                    default ->
                        System.out.println("commande vocale inconnue");
                }

            }
        });

        controler.getBtCleanAll().addActionListener((ActionEvent e) -> {
            String msg = "Palette:SupprimerTout";
            try {
                bus.sendMsg(msg);
            } catch (IvyException ex) {
                Logger.getLogger(MoteurFusionMultimodal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
