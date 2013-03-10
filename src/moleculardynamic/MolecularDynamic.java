/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moleculardynamic;

/**
 *
 * @author Cyril Cecchinel
 */
public class MolecularDynamic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        UnitCell a = new UnitCell(3);
        System.out.println("Initial :" + a.lstParticle.toString());
        for (int i=0;i<100;i++){
            a.nextState();
           // System.out.println("Tour " + i + " :" + a.lstParticle.toString());
        }
        
    }
}
