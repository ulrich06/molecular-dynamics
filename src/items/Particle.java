/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import maths.Vector2D;


/**
 * @author Cyril Cecchinel
 */
public class Particle {

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
    
    private Vector2D forces;
    
  
    private double radius;
    private double weight;
    
    private double v = 0.0 ; //Forces applied on the particle  

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
    
    /**
    * Create a particule 
    * @param position   Initial position in the 2-dimentionnal space
    * @param velocity   Initial velocity
    * @param radius     Radius
    * @param weight     Weight
    */
    public Particle(Vector2D position, Vector2D velocity, double radius, double weight) {
        this.position = position;
        this.velocity = velocity;
        this.weight=weight;
        this.radius=radius;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public Vector2D getForces() {
        return forces;
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public double getWeight() {
        return weight;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public void setForces(Vector2D forces) {
        this.forces = forces;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "Particle{" + "position=" + position + '}';
    }
   
    

}
