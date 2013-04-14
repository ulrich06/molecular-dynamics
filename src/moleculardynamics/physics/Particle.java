package moleculardynamics.physics;

import moleculardynamics.maths.Vector2D;


/**
 * Particle class
 * Particle running asynchronously from each other
 */
public class Particle implements Runnable
{
    private UnitCell unitCell;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    private double radius;
    private double weight;


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
    }

    /**
     * Run the particle thread routine
     */
    @Override
    public void run ()
    {
        // TODO : Particle thread routine
        while (true)
        {
            System.out.println("Je me balade dans l'espace avec mes camarades...");
            try { Thread.sleep(1000); } catch (InterruptedException exc) {}
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

    @Override
    public String toString ()
    {
        return "Particle [position=" + position + ", velocity=" + velocity
            + ", acceleration=" + acceleration + ", radius=" + radius + ", weight=" + weight + "]";
    }
}
