package moleculardynamics;

import java.applet.Applet;
import java.awt.BorderLayout;

import moleculardynamics.physics.UnitCell;
import moleculardynamics.ui.InterfaceGraphique;


/**
 * Molecular dynamics program
 */
@SuppressWarnings("serial")
public class MolecularDynamics extends Applet
{
    /**
     * Main function. Starts the program
     */
    public void init ()
    {
        setSize(800, 600);
        /* Create and start the simulation */
        UnitCell uc = new UnitCell(Parameters.PARTICLE_COUNT);
        Thread t = new Thread(uc, "UC#");
        
        InterfaceGraphique iG = new InterfaceGraphique(uc);
        uc.addUnitCellListener(iG);
        setLayout(new BorderLayout());
        add(iG, BorderLayout.CENTER);
        
        t.start();
        //while(uc.getElapsedTime() < Parameters.TF)
      
    }
}
