package moleculardynamics.physics;

import java.util.ArrayList;
import java.util.List;

import moleculardynamics.Parameters;
import moleculardynamics.maths.Vector2D;
import moleculardynamics.ui.UnitCellListener;


/**
 * Unit cell class
 * Contains a pool of particles
 */
public class UnitCell implements Runnable
{
    private List<Particle> particles;
    private double elapsedTime = 0;
    private int nbCells;
    private int nbCellsUpdate;

    public UnitCellListener listener;
    public boolean isPaused = false;

    /**
     * Create an unit cell with an initial number of particles
     * @param n The number of particles
     */
    public UnitCell (int n)
    {
        particles = new ArrayList<Particle>(n);
        nbCells = n;
        /* Create random particles */
        // TODO : Best random creation algorithm
        for (; n > 0; n--)
        {
            int randomX = (int)(Math.random()*Parameters.BOX_WIDTH);
            int randomY = (int)(Math.random()*Parameters.BOX_WIDTH);

            Vector2D position = new Vector2D(randomX, randomY);
            Vector2D velocity = new Vector2D(randomX, randomY);

            Particle p = new Particle(this, position, velocity, Parameters.PARTICLE_RADIUS, Parameters.PARTICLE_WEIGHT);
            particles.add(p);

        }
    }

    /**
     * Start the simulation for each particle
     */
    public void start ()
    {
        /* Launch the thread for each particle */
        for (int i = 0; i < particles.size(); i++)
        {
            Thread t = new Thread(particles.get(i), "Particle#" + (i+1));
            t.start();
        }
    }

    /**
     * Execute one step using Verlet algorithm
     */
    @Override
    public synchronized void run() {

        this.start();

        while(true){
            // Don't remove this flush.
            System.out.flush();

            if (!isPaused) {
                if(nbCellsUpdate == nbCells)
                {
                    for (Particle p : getParticles()){
                        p.update();
                    }
                    nbCellsUpdate = 0;
                    notifyAll();
                }
                elapsedTime += Parameters.DT;
                System.out.println(this.toString());
                update();
            }
        }

    }

    /*
     * Compute acceleration for each particle. Use Lennard-Jones potential
     */
    private synchronized void computeAccelerations(){

        // Check if particles are going outside the box
        for (Particle p : particles){

            // Initial values
            Vector2D initialAcceleration = new Vector2D();

            // Check horizontal position
            if (p.getPosition().getX() < 0)
            {

                initialAcceleration.setX(- p.getPosition().getX() * Parameters.WALL_STIFFNESS );

            } 
            else
            {

                if (p.getPosition().getX() > Parameters.BOX_WIDTH) {

                    initialAcceleration.setX( Parameters.WALL_STIFFNESS * (Parameters.BOX_WIDTH - p.getPosition().getX()) );

                }
            }

            // Check vertical position
            if (p.getPosition().getY() < 0)
            {

                initialAcceleration.setY(- p.getPosition().getY() * Parameters.WALL_STIFFNESS );

            } 
            else
            {

                if (p.getPosition().getY() > Parameters.BOX_WIDTH) {

                    initialAcceleration.setY( Parameters.WALL_STIFFNESS * (Parameters.BOX_WIDTH - p.getPosition().getY()) );

                }
            }

            // Gravity force (vertical force)
            initialAcceleration.setY(initialAcceleration.getY() - Parameters.GRAVITY);


            // We set initial acceleration vector to the current particle
            p.setAcceleration(initialAcceleration);

        }
        // We compute interactions forces using Lennard-Jones potential
        for (Particle p : particles){
            for (Particle j : particles){

                // We need to use distinct pairs
                if (!p.equals(j)){

                    // Compute distance between pairs
                    double distance = Vector2D.norm(p.getPosition(), j.getPosition());

                    // Horizontal and vertical distance
                    double dx = p.getPosition().getX() - j.getPosition().getX();
                    double dy = p.getPosition().getY() - j.getPosition().getY();

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
                    Vector2D accelerationP = Vector2D.add(p.getAcceleration(), lennardForce);
                    Vector2D accelerationJ = Vector2D.sub(j.getAcceleration(), lennardForce); // Newton's 3rd law

                    // We set computed values to the particles
                    p.setAcceleration(accelerationP);
                    p.setAcceleration(accelerationJ);
                }
            }
        }
    }

    public double getElapsedTime()
    {
        return elapsedTime;

    }

    public String toString(){
        String result = "UnitCell [T= " + elapsedTime + " ] : ";
        for (Particle p : particles){

            result += '\n';
            result += '\t';
            result += p.toString();
        }
        return result;

    }

    public synchronized void cellFinished(){
        nbCellsUpdate++;
    }

    public List<Particle> getParticles(){
        return particles;
    }

    public void addUnitCellListener(UnitCellListener ig) {
        listener = ig;
    }

    public void update() {
        listener.updateUnitCell();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}