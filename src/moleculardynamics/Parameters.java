package moleculardynamics;


/**
 * Parameters class
 * Contains simulation parameters
 */
public class Parameters
{
    /* Number of particles */
    public static final int PARTICLE_COUNT = 4;
    
    /* Box width */
    public static final double BOX_WIDTH = 100;
    
    /* Wall stiffness */
    public static final double WALL_STIFFNESS = 1;
    
    /* Gravity */
    public static final double GRAVITY = 10;
    
    /* Sampling */
    public static final double DT = 0.02;

    /* Simulation time */
    public static final double TF = 100;

    /* Particle weight */
    public static final double PARTICLE_WEIGHT = 1.0;

    /* Particle radius */
    public static final double PARTICLE_RADIUS = 1;

    /* Depth of the Lennard Jones' potential well */
    public static final double EPSILON = 1.0;
    
    /* Minimal distance for computing interactions between two particles */
    public static final double FORCECUTOFF = 3.0;
}
