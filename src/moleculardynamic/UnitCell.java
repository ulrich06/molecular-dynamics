/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moleculardynamic;

import items.Particle;
import java.util.ArrayList;
import maths.Vector2D;

/**
 *
 * @author Cyril Cecchinel
 */
public class UnitCell {
    
    protected ArrayList<Particle> lstParticle;
    
    public UnitCell(int nbParticle){
        lstParticle = new ArrayList<Particle>();
        lstParticle.add(new Particle(new Vector2D(0,0), new Vector2D(0,0), 0.04, 1));
        lstParticle.add(new Particle(new Vector2D(3,2), new Vector2D(0,0), 0.04, 1));    }
    
    public void nextState(){
        /*
         * Each state, new values computed by Verlet's algorithm are :
         *  v(t+1) = v(t) + a(t+1/2)dt  where "a" is acceleration computed separately with Lennard Jones al
         *  r(t+1) = r(t)+0.5[v(t)+v(t+1)]dt
         */
        
        Vector2D newPosition = new Vector2D();
        Vector2D newAcceleration;
        Vector2D newVelocity;
        
        /* Compute r(t+1/2) */
        for (Particle p : lstParticle){
            newPosition = Vector2D.add(p.getPosition(),Vector2D.mult_scal(p.getPosition(), 0.5*Parameters.TE));
            p.setPosition(newPosition);

        }

        /* Compute acceleration */
        computeForces();
        newPosition = new Vector2D();
        
        for (Particle p : lstParticle){
            newAcceleration = Vector2D.mult_scal(p.getForces(), 1/p.getWeight());
            p.setAcceleration(newAcceleration);
            
            /* Compute new velocity and new position */
            newVelocity = Vector2D.add(p.getVelocity(), Vector2D.mult_scal(p.getAcceleration(), Parameters.TE));
            p.setVelocity(newVelocity);
            
           
            newPosition = Vector2D.add(newPosition, Vector2D.mult_scal(newVelocity, 0.5*Parameters.TE));
            p.setPosition(newPosition);
            
            
            
        }

    }
    
    private void computeForces(){
        /*
         * Lennard Jones potential
         * o(r) = 4*EPSILON[(RADIUS/r)^12-(RADIUS/r)^6] where r is distance between 2 particles
         */
        
        for(Particle p : lstParticle){
            p.setV(0.0);
            Vector2D tmpForce = new Vector2D();
            
            for (Particle q : lstParticle){
                if (!p.equals(q)){
                    
                    // Compute r norm
                    double rn = Vector2D.norm(q.getPosition(),p.getPosition()); 
                   
                    p.setV(p.getV() + 4.0 * Parameters.EPSILON * (Math.pow(Parameters.RADIUS/rn, 12) - Math.pow(Parameters.RADIUS/rn, 6)));
                    
                    // Compute r vector
                    Vector2D rVector = Vector2D.sub(p.getPosition(), q.getPosition());
                    tmpForce = Vector2D.add(tmpForce, Vector2D.sub(Vector2D.mult_scal(rVector, 4.0*Parameters.EPSILON*Math.pow(Parameters.RADIUS, 12)/Math.pow(rn, 14))
                                                                   , 
                                                                   Vector2D.mult_scal(rVector, 6.0*Math.pow(Parameters.RADIUS, 6)/Math.pow(rn, 8))
                                                                   ));
                    
                    p.setForces(tmpForce);
                }
            }
        }
        
    }
    
}
