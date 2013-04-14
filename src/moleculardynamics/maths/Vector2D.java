package moleculardynamics.maths;


/**
 * 2D vector class
 * Represents a two-dimensional vector
 */
public class Vector2D
{
    private double x;
    private double y;


    /**
     * Create a vector this x = 0 and y = 0
     */
    public Vector2D ()
    {
        this(0.0, 0.0);
    }

    /**
     * Create a vector
     * @param x Initial X
     * @param y Initial Y
     */
    public Vector2D (double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor of a vector
     * @param v The initial vector to copy
     */
    public Vector2D (Vector2D v)
    {
        this(v.getX(), v.getY());
    }

    /**
     * Add the vector with another vector
     * @param v The other vector
     */
    public void add (Vector2D v)
    {
        this.x += v.getX();
        this.y += v.getY();
    }

    /**
     * Substract the vector with another vector
     * @param v The other vector
     */
    public void sub (Vector2D v)
    {
        this.x -= v.getX();
        this.y -= v.getY();
    }

    /**
     * Multiply the vector with the constant k
     * @param k The constant
     */
    public void mult_scal (double k)
    {
        this.x *= k;
        this.y *= k;
    }

    /**
     * @return The vector norm
     */
    public double norm ()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Returns the vector norm relative to another vector
     * @param v The other vector
     * @return The relative vector norm
     */
    public double norm (Vector2D v)
    {
        return Math.sqrt(Math.pow(this.x - v.getX(), 2) + Math.pow(this.y - v.getY(), 2));
    }

    /**
     * Add a vector with another and returns a newly allocated vector
     * @param a The first vector
     * @param b The other vector
     * @return The new vector
     */
    public static Vector2D add (Vector2D a, Vector2D b)
    {
        return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
    }

    /**
     * Substract a vector with another and returns a newly allocated vector
     * @param a The first vector
     * @param b The other vector
     * @return The new vector
     */
    public static Vector2D sub (Vector2D a, Vector2D b)
    {
        return new Vector2D(a.getX() - b.getX(), a.getY() - b.getY());
    }

    /**
     * Multiply a vector with the constant k and returns a newly allocated vector
     * @param v The vector
     * @return The new vector
     */
    public static Vector2D mult_scal (Vector2D v, double k)
    {
        return new Vector2D(v.getX() * k, v.getY() * k);
    }

    /**
     * Compute the opposite of a vector and returns a newly allocated vector
     * @param v The vector
     * @return The new vector
     */
    public static Vector2D opposite(Vector2D v)
    {
    	return Vector2D.mult_scal(v, -1);
    }
    
    /**
     * Get the distance between two vectors
     * @param a The first vector
     * @param b The second vector
     * @return Distance between the vectors
     */
    public static double norm(Vector2D a, Vector2D b)
    {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getX(), 2));
    }
    

    public double getX ()
    {
        return x;
    }

    public void setX (double x)
    {
        this.x = x;
    }

    public double getY ()
    {
        return y;
    }

    public void setY (double y)
    {
        this.y = y;
    }

    @Override
    public String toString ()
    {
        return "Vector2D [x=" + x + ", y=" + y + "]";
    }
}
