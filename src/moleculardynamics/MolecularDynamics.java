package moleculardynamics;

import moleculardynamics.physics.UnitCell;


/**
 * Molecular dynamics program
 */
public class MolecularDynamics
{
    /**
     * Main function. Starts the program
     */
    public static void main (String[] args)
    {
        /* Create and start the simulation */
        UnitCell uc = new UnitCell(Parameters.PARTICLE_COUNT);
       
        //while(uc.getElapsedTime() < Parameters.TF)
        for (int i = 0; i< 5 ; i++)
        {
	        System.out.println(uc.toString());
	        uc.oneStep();
        }
       
      
    }
}
