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
        uc.start();
    }
}
