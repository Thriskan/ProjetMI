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
        IDLE, DRAW_RECT, DRAW_ELLIPSE, DELETE, MOVE
    }
    
    private State current_state;

    public StateMachine() {
        current_state = State.IDLE;
    }
    
    public void draw_rect(){
        switch (current_state){
            case IDLE:
                current_state = State.DRAW_RECT;
            case DRAW_RECT:
                current_state = State.DRAW_RECT;
            case DRAW_ELLIPSE:
                current_state = State.DRAW_RECT;
            case MOVE:
                current_state = State.DRAW_RECT;
            case DELETE:
                current_state = State.DRAW_RECT;
        }
    }
    
    public void draw_ellipse(){
        switch (current_state){
            case IDLE:
                current_state = State.DRAW_ELLIPSE;
            case DRAW_RECT:
                current_state = State.DRAW_ELLIPSE;
            case DRAW_ELLIPSE:
                current_state = State.DRAW_ELLIPSE;
            case MOVE:
                current_state = State.DRAW_ELLIPSE;
            case DELETE:
                current_state = State.DRAW_ELLIPSE;
        }
    }
    
        public void draw_m() {
        switch (current_state) {
            case IDLE:
                current_state = State.MOVE;
            case DRAW_RECT:
                current_state = State.MOVE;
            case DRAW_ELLIPSE:
                current_state = State.MOVE;
            case MOVE:
                current_state = State.MOVE;
            case DELETE:
                current_state = State.MOVE;
        }
    }
    
        public void draw_cross(){
        switch (current_state){
            case IDLE:
                current_state = State.DELETE;
            case DRAW_RECT:
                current_state = State.DELETE;
            case DRAW_ELLIPSE:
                current_state = State.DELETE;
            case MOVE:
                current_state = State.DELETE;
            case DELETE:
                current_state = State.DELETE;
        }
    }
        public void voice_draw(){
        switch (current_state){
            case IDLE:
                System.out.println("Impossible voice from Idle");
            case DRAW_RECT:
                System.out.println("Parameters for drawing rect");
            case DRAW_ELLIPSE:
                System.out.println("Parameters for drawing ellispe");
            case MOVE:
                System.out.println("Impossible voice from Move");
            case DELETE:
                System.out.println("Impossible voice from Delete");
        }
    }
        
        public void voice_move(){
        switch (current_state){
            case IDLE:
                System.out.println("Impossible voice from Idle");
            case DRAW_RECT:
                System.out.println("Impossible voice from Idle");
            case DRAW_ELLIPSE:
                
            case MOVE:
                System.out.println("Parameters for moving object");
            case DELETE:
                System.out.println("Impossible voice from Delete");
        }
    }
}
