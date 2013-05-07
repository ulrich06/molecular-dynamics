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
        Thread t = new Thread(uc, "UC#");
        uc.start();
        System.out.println(uc.toString());
        t.start();
        //while(uc.getElapsedTime() < Parameters.TF)
      
    }
}
