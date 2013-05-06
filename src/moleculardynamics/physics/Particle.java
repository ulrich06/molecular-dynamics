package moleculardynamics.physics;

import moleculardynamics.Parameters;
import moleculardynamics.maths.Vector2D;


/**
 * Particle class
 * Particle running asynchronously from each other
 */
public class Particle implements Runnable
{
    private UnitCell unitCell;

    private Vector2D position;
    private Vector2D newPosition;
    private Vector2D velocity;
    private Vector2D newVelocity;
    private Vector2D acceleration;
    private Vector2D newAcceleration;

    private double radius;
    private double weight;

    private boolean isPaused;


    /**
     * Create a particle with default properties
     * @param unitCell The particle unit cell
     */
    public Particle (UnitCell unitCell)
    {
        this.unitCell = unitCell;
        this.position = new Vector2D();
        this.velocity = new Vector2D();
        this.acceleration = new Vector2D();
        this.radius = 1.0;
        this.weight = 1.0;
        this.isPaused = false;
    }

    /**
     * Create a particle with initial properties
     * @param unitCell The particle unit cell
     * @param position Initial position
     * @param velocity Initial velocity
     * @param radius Particle radius
     * @param weight Particle weight
     */
    public Particle (UnitCell unitCell, Vector2D position, Vector2D velocity, double radius, double weight)
    {
        this.unitCell = unitCell;
        this.position = new Vector2D(position);
        this.velocity = new Vector2D(velocity);
        this.acceleration = new Vector2D();
        this.radius = radius;
        this.weight = weight;
        this.isPaused = false;
    }

    /**
     * Run the particle thread routine
     */
    public synchronized void run ()
    {
        final double dtOver2 = Parameters.DT * 0.5;
        final double dtSquaredOver2 = Math.pow(Parameters.DT, 2) * 0.5;

        while (true)
        { 
            if (!isPaused) {
                // nextPosition = position + (velocity * dt) + (acceleration * 0.5 dt * dt)
                newPosition = Vector2D.add(getPosition(), 
                        Vector2D.add(Vector2D.mult_scal(getVelocity(), Parameters.DT), Vector2D.mult_scal(getAcceleration(), dtSquaredOver2)));

                // nextVelocity = velocity + (acceleration * 0.5 * dt)
                newVelocity = Vector2D.add(getVelocity(),
                        Vector2D.mult_scal(getAcceleration(), dtOver2));

                computeAccelerations();

                newVelocity = Vector2D.add(newVelocity,
                        Vector2D.mult_scal(getAcceleration(), dtOver2));

                unitCell.cellFinished();

            }
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void computeAccelerations(){
        // Check if particles are going outside the box
        // Initial values
        Vector2D initialAcceleration = new Vector2D();
        // Check horizontal position
        if (getPosition().getX() < 0)
        {

            initialAcceleration.setX(- getPosition().getX() * Parameters.WALL_STIFFNESS );

        } 
        else
        {

            if (getPosition().getX() > Parameters.BOX_WIDTH) {

                initialAcceleration.setX( Parameters.WALL_STIFFNESS * (Parameters.BOX_WIDTH - getPosition().getX()) );

            }
        }

        // Check vertical position
        if (getPosition().getY() < 0)
        {

            initialAcceleration.setY(- getPosition().getY() * Parameters.WALL_STIFFNESS );

        } 
        else
        {

            if (getPosition().getY() > Parameters.BOX_WIDTH) {

                initialAcceleration.setY( Parameters.WALL_STIFFNESS * (Parameters.BOX_WIDTH - getPosition().getY()) );

            }
        }

        // Gravity force (vertical force)
        initialAcceleration.setY(initialAcceleration.getY() - Parameters.GRAVITY);


        // We set initial acceleration vector to the current particle
        newAcceleration = initialAcceleration;

        // We compute interactions forces using Lennard-Jones potential
        for (Particle j : unitCell.getParticles()){

            // We need to use distinct pairs
            if (!this.equals(j)){

                // Compute distance between pairs
                double distance = Vector2D.norm(getNewPosition(), j.getPosition());

                // Horizontal and vertical distance
                double dx = getNewPosition().getX() - j.getPosition().getX();
                double dy = getNewPosition().getY() - j.getPosition().getY();

                double distanceInv = 1.0 / distance;

                //Compute attractive and repulsive forces
                double attract = Math.pow(Parameters.PARTICLE_RADIUS * distanceInv, 6);
                double repul = Math.pow(Parameters.PARTICLE_RADIUS * distanceInv, 12);


                // Force over the distance between particles
                // F = - 24 * epsilon * [2 * (repul * 1/distance) - (attract * 1/distance)]
                double fOverDistance = -24 * Parameters.EPSILON * ( 2 * (repul * distanceInv) - (attract * distanceInv) );

                // Lennard Jones force
                Vector2D lennardForce = new Vector2D(fOverDistance * dx, fOverDistance * dy);

                // New accelerations for both particles
                Vector2D accelerationP = Vector2D.add(getNewAcceleration(), lennardForce);
                Vector2D accelerationJ = Vector2D.sub(j.getAcceleration(), lennardForce); // Newton's 3rd law

                // We set computed values to the particles
                newAcceleration = accelerationP;
                j.setNewAcceleration(accelerationJ);
            }
        }
    }

    public Vector2D getPosition ()
    {
        return position;
    }

    public void setPosition (Vector2D position)
    {
        this.position = position;
    }

    public Vector2D getVelocity ()
    {
        return velocity;
    }

    public void setVelocity (Vector2D velocity)
    {
        this.velocity = velocity;
    }

    public Vector2D getAcceleration ()
    {
        return acceleration;
    }

    public void setAcceleration (Vector2D acceleration)
    {
        this.acceleration = acceleration;
    }

    public double getRadius ()
    {
        return radius;
    }

    public void setRadius (double radius)
    {
        this.radius = radius;
    }

    public double getWeight ()
    {
        return weight;
    }

    public void setWeight (double weight)
    {
        this.weight = weight;
    }

    public UnitCell getUnitCell ()
    {
        return unitCell;
    }

    public void setUnitCell (UnitCell unitCell)
    {
        this.unitCell = unitCell;
    }

    public void setNewAcceleration(Vector2D acc){
        this.newAcceleration = acc;
    }

    public Vector2D getNewAcceleration(){
        return newAcceleration;
    }

    public Vector2D getNewPosition(){
        return newPosition;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    @Override
    public String toString ()
    {
        return "Particle [position=" + position + ", velocity=" + velocity
                + ", acceleration=" + acceleration + ", radius=" + radius + ", weight=" + weight + "]";
    }

    public void update(){
        this.acceleration = newAcceleration;
        this.position = newPosition;
        this.velocity = newVelocity;
    }
}
