/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author Cyril Cecchinel
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(){
        this(0.0,0.0);
    }
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public static Vector2D add(Vector2D a, Vector2D b){
        return new Vector2D(a.getX()+b.getX(), a.getY()+b.getY());
    }
    
     public static Vector2D sub(Vector2D a, Vector2D b){
        return new Vector2D(a.getX()-b.getX(), a.getY()-b.getY());
    }
     
     public static Vector2D mult_scal(Vector2D a, double k){
        return new Vector2D(k*a.getX(),k*a.getY());
    }
     
     public static double norm(Vector2D a, Vector2D b){
         return Math.sqrt(Math.pow(b.getX()-a.getX(), 2)+Math.pow(b.getY()-a.getY(), 2));
     }
     
    @Override
     public String toString(){
         return "(" + x + ";" + y + ")";
     }
}
