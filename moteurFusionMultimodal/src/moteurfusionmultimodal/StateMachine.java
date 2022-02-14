/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package moteurfusionmultimodal;

import gui.PaletteController;

/**
 *
 * @author caros
 */
public class StateMachine {

    public enum State {
        IDLE, DRAW_RECT, DRAW_ELLIPSE, DELETE, MOVE, MOVING
    }

    private State current_state;

    public StateMachine() {
        current_state = State.IDLE;
    }

    public void draw_rect() {
        switch (current_state) {
            case IDLE:
                current_state = State.DRAW_RECT;
                break;
            case DRAW_RECT:
                current_state = State.DRAW_RECT;
                break;
            case DRAW_ELLIPSE:
                current_state = State.DRAW_RECT;
                break;
            case MOVE:
                current_state = State.DRAW_RECT;
                break;
            case DELETE:
                current_state = State.DRAW_RECT;
                break;
            case MOVING:
                current_state = State.DRAW_RECT;
                break;
        }
    }

    public void draw_ellipse() {
        switch (current_state) {
            case IDLE:
                current_state = State.DRAW_ELLIPSE;
                break;
            case DRAW_RECT:
                current_state = State.DRAW_ELLIPSE;
                break;
            case DRAW_ELLIPSE:
                current_state = State.DRAW_ELLIPSE;
                break;
            case MOVE:
                current_state = State.DRAW_ELLIPSE;
                break;
            case DELETE:
                current_state = State.DRAW_ELLIPSE;
                break;
            case MOVING:
                current_state = State.DRAW_ELLIPSE;
                break;
        }
    }

    public void draw_m() {
        switch (current_state) {
            case IDLE:
                current_state = State.MOVE;
                break;
            case DRAW_RECT:
                current_state = State.MOVE;
                break;
            case DRAW_ELLIPSE:
                current_state = State.MOVE;
                break;
            case MOVE:
                current_state = State.MOVE;
                break;
            case DELETE:
                current_state = State.MOVE;
                break;
            case MOVING:
                current_state = State.MOVE;
                break;
        }
    }

    public void draw_cross() {
        switch (current_state) {
            case IDLE:
                current_state = State.DELETE;
                break;
            case DRAW_RECT:
                current_state = State.DELETE;
                break;
            case DRAW_ELLIPSE:
                current_state = State.DELETE;
                break;
            case MOVE:
                current_state = State.DELETE;
                break;
            case DELETE:
                current_state = State.DELETE;
                break;
            case MOVING:
                current_state = State.DELETE;
                break;
        }
    }

    public void voice_draw() {
        switch (current_state) {
            case IDLE:
                System.out.println("Impossible voice from Idle");
                break;
            case DRAW_RECT:
                System.out.println("Parameters for drawing rect");
                current_state = State.IDLE;
                break;
            case DRAW_ELLIPSE:
                System.out.println("Parameters for drawing ellispe");
                current_state = State.IDLE;
                break;
            case MOVE:
                System.out.println("Impossible voice from Move");
                break;
            case DELETE:
                System.out.println("Impossible voice from Delete");
                break;
            case MOVING:
                System.out.println("Impossible voice from Moving");
                break;
        }
    }

    /*public void voice_delete() {
        switch (current_state) {
            case IDLE:
                System.out.println("Impossible voice from Idle");
                break;
            case DRAW_RECT:
                System.out.println("Impossible voice from Rect");
                break;
            case DRAW_ELLIPSE:
                System.out.println("Impossible voice from Ellipse");
                break;
            case MOVE:
                System.out.println("Impossible voice from Move");
                break;
            case DELETE:
                System.out.println("Parameters for drawing rect");
                current_state = State.IDLE;
                break;
            case MOVING:
                System.out.println("Impossible voice from Moving");
                break;
        }
    }*/

    public void voice_move_position() {
        switch (current_state) {
            case IDLE:
                System.out.println("Impossible voice from Idle");
                break;
            case DRAW_RECT:
                System.out.println("Impossible voice from Draw_rect");
                break;
            case DRAW_ELLIPSE:
                System.out.println("Impossible voice from Draw_ellipse");
                break;
            case MOVE:
                System.out.println("Impossible voice from Move");
                break;
            case DELETE:
                System.out.println("Impossible voice from Delete");
                break;
            case MOVING:
                System.out.println("Parameters for moving object");
                current_state = State.IDLE;
                break;
        }
    }

    public void voice_object() {
        switch (current_state) {
            case IDLE:
                System.out.println("Impossible voice from Idle");
                break;
            case DRAW_RECT:
                System.out.println("Impossible voice from Draw_rect");
                break;
            case DRAW_ELLIPSE:
                System.out.println("Impossible voice from Draw_ellipse");
                break;
            case MOVE:
                System.out.println("Parameters for moving object");
                current_state = State.MOVING;
                break;
            case DELETE:
                System.out.println("deleting");
                current_state = State.IDLE;
                break;
            case MOVING:
                System.out.println("Impossible voice from Moving");
                break;
        }
    }

    public State getCurrent_state() {
        return current_state;
    }

}
