/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package moteurfusionmultimodal;

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
            case IDLE -> current_state = State.DRAW_RECT;
            case DRAW_RECT -> current_state = State.DRAW_RECT;
            case DRAW_ELLIPSE -> current_state = State.DRAW_RECT;
            case MOVE -> current_state = State.DRAW_RECT;
            case DELETE -> current_state = State.DRAW_RECT;
            case MOVING -> current_state = State.DRAW_RECT;
        }
    }

    public void draw_ellipse() {
        switch (current_state) {
            case IDLE -> current_state = State.DRAW_ELLIPSE;
            case DRAW_RECT -> current_state = State.DRAW_ELLIPSE;
            case DRAW_ELLIPSE -> current_state = State.DRAW_ELLIPSE;
            case MOVE -> current_state = State.DRAW_ELLIPSE;
            case DELETE -> current_state = State.DRAW_ELLIPSE;
            case MOVING -> current_state = State.DRAW_ELLIPSE;
        }
    }

    public void draw_m() {
        switch (current_state) {
            case IDLE -> current_state = State.MOVE;
            case DRAW_RECT -> current_state = State.MOVE;
            case DRAW_ELLIPSE -> current_state = State.MOVE;
            case MOVE -> current_state = State.MOVE;
            case DELETE -> current_state = State.MOVE;
            case MOVING -> current_state = State.MOVE;
        }
    }

    public void draw_cross() {
        switch (current_state) {
            case IDLE -> current_state = State.DELETE;
            case DRAW_RECT -> current_state = State.DELETE;
            case DRAW_ELLIPSE -> current_state = State.DELETE;
            case MOVE -> current_state = State.DELETE;
            case DELETE -> current_state = State.DELETE;
            case MOVING -> current_state = State.DELETE;
        }
    }

    public void voice_draw() {
        switch (current_state) {
            case IDLE -> System.out.println("Impossible voice from Idle");
            case DRAW_RECT -> {
                System.out.println("Parameters for drawing rect");
                current_state = State.IDLE;
            }
            case DRAW_ELLIPSE -> {
                System.out.println("Parameters for drawing ellispe");
                current_state = State.IDLE;
            }
            case MOVE -> System.out.println("Impossible voice from Move");
            case DELETE -> System.out.println("Impossible voice from Delete");
            case MOVING -> System.out.println("Impossible voice from Moving");
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
            case IDLE -> System.out.println("Impossible voice from Idle");
            case DRAW_RECT -> System.out.println("Impossible voice from Draw_rect");
            case DRAW_ELLIPSE -> System.out.println("Impossible voice from Draw_ellipse");
            case MOVE -> System.out.println("Impossible voice from Move");
            case DELETE -> System.out.println("Impossible voice from Delete");
            case MOVING -> {
                System.out.println("Parameters for moving object");
                current_state = State.IDLE;
            }
        }
    }

    public void voice_object() {
        switch (current_state) {
            case IDLE -> System.out.println("Impossible voice from Idle");
            case DRAW_RECT -> System.out.println("Impossible voice from Draw_rect");
            case DRAW_ELLIPSE -> System.out.println("Impossible voice from Draw_ellipse");
            case MOVE -> {
                System.out.println("Parameters for moving object");
                current_state = State.MOVING;
            }
            case DELETE -> {
                System.out.println("deleting");
                current_state = State.IDLE;
            }
            case MOVING -> System.out.println("Impossible voice from Moving");
        }
    }

    public State getCurrent_state() {
        return current_state;
    }

}
