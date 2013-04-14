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
	private double elapsedTime = 0;

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
			Vector2D velocity = new Vector2D(10, 10);

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
	public synchronized void oneStep(){

		// Current physical parameters

		Vector2D currentPosition;
		Vector2D currentVelocity;
		Vector2D currentAcceleration;

		// Next positions computed using Verlet algorithm

		Vector2D nextPosition;
		Vector2D nextVelocity;


		// Constant values

		final double dtOver2 = Parameters.DT * 0.5;
		final double dtSquaredOver2 = Math.pow(Parameters.DT, 2) * 0.5;

		// Compute next position and velocity for each particle
		for (Particle p : particles){
			currentPosition = p.getPosition();
			currentVelocity = p.getVelocity();
			currentAcceleration = p.getAcceleration(); 

			// nextPosition = position + (velocity * dt) + (acceleration * 0.5 dt * dt)
			nextPosition = Vector2D.add(currentPosition, 
					Vector2D.add(Vector2D.mult_scal(p.getVelocity(), Parameters.DT), Vector2D.mult_scal(currentAcceleration, dtSquaredOver2)));

			// nextVelocity = velocity + (acceleration * 0.5 * dt)
			nextVelocity = Vector2D.add(currentVelocity,
					Vector2D.mult_scal(currentAcceleration, 0.5 * Parameters.DT));

			// We set computed values to the particle
			p.setPosition(nextPosition);
			p.setVelocity(nextVelocity);
		}

		// We compute accelerations between each particle
		computeAccelerations();

		// Finish updating velocity using new accelerations
		for (Particle p : particles)
		{
			currentVelocity = p.getVelocity();

			// nextVelocity = velocity + (acceleration * 0.5 * dt)
			nextVelocity = Vector2D.add(currentVelocity,
					Vector2D.mult_scal(p.getAcceleration(), dtOver2));

			// We set the computed value to the particle
			p.setVelocity(nextVelocity);
		}

		elapsedTime += Parameters.DT;

	}

	/*
	 * Compute acceleration for each particle. Use Lennard-Jones potential
	 */
	private void computeAccelerations(){

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

			// Gravity force
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

	public String toString(){
		String result = "UnitCell [T= " + elapsedTime + " ] : ";
		for (Particle p : particles){
			result += p.toString() + " ";
		}
		return result;

	}

}