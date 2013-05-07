package moleculardynamics.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

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
	private ThreadGroup group;
	private CyclicBarrier barrier;
	
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
		nbCellsUpdate = 0;
		group = new ThreadGroup("Particules");
		barrier = new CyclicBarrier(n,new Runnable() {
            public void run() { 
                for(Particle p : getParticles()) {
                	p.update();
                }
              }
            });
		/* Create random particles */
		// TODO : Best random creation algorithm
		for (; n > 0; n--)
		{
			int randomX = (int)(Math.random()*Parameters.BOX_WIDTH);
			int randomY = (int)(Math.random()*Parameters.BOX_WIDTH);
					
			Vector2D position = new Vector2D(randomX, randomY);
			Vector2D velocity = new Vector2D(1,1);

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
			Thread t = new Thread(group,particles.get(i), "Particle#" + (i+1));
			t.start();
		}
	}

	/**
	 * Execute one step using Verlet algorithm
	 */
	@Override
	public void run() {
		
		while(elapsedTime < 1) {
			System.out.flush();
			if (!isPaused) {
				elapsedTime += Parameters.DT;
				System.out.println("Updated : " + barrier.getNumberWaiting());
				System.out.println(this.toString());
				update();
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
	
	public synchronized CyclicBarrier getBarrier() {
		return barrier;
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