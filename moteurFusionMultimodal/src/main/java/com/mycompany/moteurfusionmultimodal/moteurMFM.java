/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.moteurfusionmultimodal;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import gestuel.GestureListener;
import gestuel.Stroke;
import gui.PaletteController;

/**
 *
 * @author caros
 */
public class moteurMFM {

    private Ivy bus;
    private PaletteController controler;

    private GestureListener gestureListener;

    public moteurMFM() throws IvyException {

        bus = new Ivy("BUS IVY", "IVY Ready", null);
        controler = new PaletteController();

        gestureListener = new GestureListener(bus, controler);

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

                gestureListener.drawPoint(args[0], args[1], "GREEN");
                gestureListener.initiateStroke();
                gestureListener.addPointToStroke(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
            }
        ;
        });
         
         bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String st = "Le point d'arrivée est (" + args[0] + " , " + args[1] + ")\n";

                gestureListener.drawPoint(args[0], args[1], "RED");
                gestureListener.normalizeStroke();

                if (controler.getCurrentMode() == PaletteController.Mode.APP) {
                    gestureListener.registerStroke();
                } else {
                    gestureListener.findStroke();
                }

            }
        ;
    }

);
    }
    
    
}
