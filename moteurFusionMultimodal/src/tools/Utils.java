/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 *
 * @author chatonbrutal
 */
public class Utils {
    
    public static final String BLACK_ENG = "black";
    public static final String BLACK_FR = "noir";
    public static final String BLACK_RGB = "0.0.0";
    
    public static final String RED_ENG = "red";
    public static final String RED_FR = "rouge";
    public static final String RED_RGB = "255.0.0";
    
    public static final String BLUE_ENG = "blue";
    public static final String BLUE_FR = "bleu";
    public static final String BLUE_RGB = "0.0.255";
    
    public static String convertToEng(String c) {
        return switch (c) {
            case BLACK_FR -> BLACK_ENG;
            case RED_FR -> RED_ENG;
            case BLUE_FR -> BLUE_ENG;
            default -> BLACK_ENG;
        };
    }
    
    public static String defineColor(String ligne) {
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
            case RED_FR ->
                RED_RGB;
            case BLUE_FR ->
                BLUE_RGB;
            default ->
                BLACK_RGB;
        };
    }
}
