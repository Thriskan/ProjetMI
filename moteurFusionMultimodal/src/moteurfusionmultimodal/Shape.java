/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package moteurfusionmultimodal;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author caros
 */
public class Shape {

    public enum ShapeType {
        RECTANGLE,
        ELIPSE
    }

    private int id;
    private ShapeType shapeType;
    private Color color;
    private ArrayList<Point2D.Double> trace;
    private Point2D.Double origine;

    public Shape(int aId, ShapeType aShapeType, Color color) {
        this.id = aId;
        this.shapeType = aShapeType;
        this.color = color;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType aShapeType) {
        this.shapeType = aShapeType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Point2D.Double> getTrace() {
        return trace;
    }

    public void setTrace(ArrayList<Point2D.Double> trace) {
        this.trace = trace;
    }

    public Point2D.Double getOrigine() {
        return origine;
    }

    public void setOrigine(Point2D.Double origine) {
        this.origine = origine;
    }

}
