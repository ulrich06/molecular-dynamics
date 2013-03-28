package moleculardynamics.physics;

import java.util.ArrayList;
import java.util.List;

import moleculardynamics.Parameters;
import moleculardynamics.maths.Vector2D;


/**
 * Unit cell class
 * Contains a pool of particles
 */
public class UnitCell
{
    private List<Particle> particles;


    /**
     * Create an unit cell with an initial number of particles
     * @param n The number of particles
     */
    public UnitCell (int n)
    {
        particles = new ArrayList<Particle>(n);

        /* Create random particles */
        // TODO : Best random creation algorithm
        for (; n > 0; n--)
        {
            Vector2D position = new Vector2D(Math.random(), Math.random());
            Vector2D velocity = new Vector2D(0, 0);

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
}