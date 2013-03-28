package moleculardynamics;


/**
 * Parameters class
 * Contains simulation parameters
 */
public class Parameters
{
    /* Number of particles */
    public static final int PARTICLE_COUNT = 10;

    /* Sampling */
    public static final double TE = 0.00002;

    /* Simulation time */
    public static final double TF = 100;

    /* Particle weight */
    public static final double PARTICLE_WEIGHT = 1.0;

    /* Particle radius */
    public static final double PARTICLE_RADIUS = 0.04;

    /* Depth of the Lennard Jones' potential well */
    public static final double EPSILON = 1.0;
}
