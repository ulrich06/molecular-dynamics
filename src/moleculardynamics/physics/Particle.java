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
    private Vector2D forces;

    private double radius;
    private double weight;
    private double v;


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
        this.forces = new Vector2D();
        this.radius = 1.0;
        this.weight = 1.0;
        this.v = 0.0;
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
        this.forces = new Vector2D();
        this.radius = radius;
        this.weight = weight;
        this.v = 0.0;
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

    /******************************************************************
     * PRECEDENT ALGORITHME. LE CALCUL EST MAINTENANT FAIT EN PARALLELE
     * POUR CHAQUE PARTICULE. ON PEUT ACCEDER AUX AUTRES PARTICULES PAR
     * LE BIAIS DE L'UNITCELL CONTENU DANS CETTE CLASSE. AU DEBUT DU
     * PROGRAMME, RUN() EST LANCEE. IL FAUT ENSUITE FAIRE LES CALCULS
     * DE LA NOUVELLE POSITION DE CHAQUE PARTICULE EN FONCTION DES
     * AUTRES, SE SYNCHRONISER, ATTENDRE QUE LES AUTRES AIENT FAIT DE
     * MEME, METTRE A JOUR L'AFFICHAGE ET CONTINUER EN BOUCLE.
     *
    public void nextState(){
        //
        // Each state, new values computed by Verlet's algorithm are :
        //  v(t+1) = v(t) + a(t+1/2)dt  where "a" is acceleration computed separately with Lennard Jones al
        //  r(t+1) = r(t)+0.5[v(t)+v(t+1)]dt
        //

        Vector2D newPosition;
        Vector2D newAcceleration;
        Vector2D newVelocity;

        // Compute r(t+1/2)
        for (Particle p : lstParticle){
            newPosition = Vector2D.add(p.getPosition(),Vector2D.mult_scal(p.getVelocity(), 0.5*Parameters.TE));
            p.setPosition(newPosition);
        }

        // Compute acceleration
        computeForces();
        newPosition = new Vector2D();

        for (Particle p : lstParticle){
            newAcceleration = Vector2D.mult_scal(p.getForces(), 1/p.getWeight());
            p.setAcceleration(newAcceleration);

            // Compute new velocity
            newVelocity = Vector2D.add(p.getVelocity(), Vector2D.mult_scal(p.getAcceleration(), Parameters.TE));
            p.setVelocity(newVelocity);

            // Compute new position
            newPosition = Vector2D.add(newPosition, Vector2D.mult_scal(newVelocity, 0.5*Parameters.TE));
            p.setPosition(newPosition);



        }

    }

    private void computeForces(){
        //
        // Compute forces according to Lennard Jones potential
        // o(r) = 4*EPSILON[(RADIUS/r)^12-(RADIUS/r)^6] where r is distance between 2 particles
        //

        for(Particle p : lstParticle){
            p.setV(0.0);
            Vector2D tmpForce = new Vector2D();

            for (Particle q : lstParticle){
                if (!p.equals(q)){

                    // Compute r norm
                    double rn = Vector2D.norm(q.getPosition(),p.getPosition());

                    double newV = p.getV() + 4.0 * Parameters.EPSILON * (Math.pow(Parameters.RADIUS/rn, 12) - Math.pow(Parameters.RADIUS/rn, 6));
                    p.setV(newV);
                    //p.setV(p.getV() + 4.0 * Parameters.EPSILON * (Math.pow(Parameters.RADIUS/rn, 12) - Math.pow(Parameters.RADIUS/rn, 6)));

                    // Compute r vector
                    Vector2D rVector = Vector2D.sub(p.getPosition(), q.getPosition());
                    //tmpForce = Vector2D.add(tmpForce, Vector2D.sub(Vector2D.mult_scal(rVector, 4.0*Parameters.EPSILON*Math.pow(Parameters.RADIUS, 12)/Math.pow(rn, 14))
                    //                                             ,
                    //                                             Vector2D.mult_scal(rVector, 6.0*Math.pow(Parameters.RADIUS, 6)/Math.pow(rn, 8))
                    //                                             ));
                    tmpForce = Vector2D.add(p.getForces(),
                               Vector2D.sub(Vector2D.mult_scal(rVector, 4.0*Parameters.EPSILON*Math.pow(Parameters.RADIUS, 12)/Math.pow(rn, 14))
                                                                   ,
                                                                   Vector2D.mult_scal(rVector, 6.0*Math.pow(Parameters.RADIUS, 6)/Math.pow(rn, 8))
                                                                   ));
                    System.out.println("rVector norm : " + Vector2D.norm(rVector));
                    p.setForces(tmpForce);
                   // System.out.println("tmpForce : " + tmpForce);
                }
            }
        }
    }

    public void computeLennardJones(Particle p) // calcul des forces de lennard jones
    {
        double forces = 0.0;
        for (Particle q : lstParticle)
        {
            if (!p.equals(q))
            {
                double dist = Vector2D.norm(p.getPosition(), q.getPosition());
                forces = 4*Parameters.EPSILON*(Math.pow(Parameters.RADIUS/dist,12) - Math.pow(Parameters.RADIUS/dist,6));
                p.setForces(new Vector2D(
                        (p.getPosition().getX() - q.getPosition().getX())*forces,
                        (p.getPosition().getY() - q.getPosition().getY())*forces));
            }
        }
    }

    public void verlet(double delta, Particle p) // implémentation de l'algorithme de verlet
    {
        Vector2D newPos = new Vector2D(0,0);
        Vector2D newVitHalf = new Vector2D(0,0);
        Vector2D newAcc = new Vector2D(0,0);

        newVitHalf = Vector2D.add(p.getVelocity(), Vector2D.mult_scal(p.getAcceleration(), (1/2)*delta));

        newPos = Vector2D.add(p.getPosition(), Vector2D.mult_scal(newVitHalf, delta));

        computeLennardJones(p);

        //a finir
    }
    *****************************************************************/

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

    public Vector2D getForces ()
    {
        return forces;
    }

    public void setForces (Vector2D forces)
    {
        this.forces = forces;
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

    public double getV ()
    {
        return v;
    }

    public void setV (double v)
    {
        this.v = v;
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
            + ", acceleration=" + acceleration + ", forces=" + forces
            + ", radius=" + radius + ", weight=" + weight + ", v=" + v + "]";
    }
}
