/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 * Tools class with useful methods and attributes
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

    /**
     * French to English color convertor
     * @param c: French color
     * @return : English color
     */
    public static String convertToEng(String c) {
        return switch (c) {
            case BLACK_FR ->
                BLACK_ENG;
            case RED_FR ->
                RED_ENG;
            case BLUE_FR ->
                BLUE_ENG;
            default ->
                BLACK_ENG;
        };
    }
    
    /**
     * Method used to extract color parameter from Ivy message
     * Manage "...rouge" and "rouge" possibilities when receveid on Ivy bus
     * @param ligne : Ivy message with color parameter
     * @return Normalized color parameter
     */
    public static String defineColor(String ligne) {
        String mots[] = ligne.split(" ");
        String c = "";
        if ("...".equals(mots[0])) {            
            if ("la".equals(mots[1]) || "ici".equals(mots[1]) || "a cette position".equals(mots[1])) {
                c = mots[2];
            } else {
                c = mots[1];
            }
        } else {            
            if ("la".equals(mots[0]) || "ici".equals(mots[0]) || "a cette position".equals(mots[0])) {
                c = mots[1];
            } else {
                c = mots[0];
            }
        }        
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
