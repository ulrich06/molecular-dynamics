package moleculardynamics.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import moleculardynamics.Parameters;
import moleculardynamics.physics.UnitCell;

@SuppressWarnings("serial")
public class InterfaceGraphique extends JPanel implements UnitCellListener {

    Panel panelUnitCell;
    UnitCell uc;

    public static final int SCALE_RADIUS = 10;
    public static final int SCALE_POSITION = (int) (800 / Parameters.BOX_WIDTH);


    public InterfaceGraphique(UnitCell uc) { 
        super();
        
        this.uc = uc;
        
        init();
    }


    public void init() {

        setLayout(new BorderLayout()); 

        // Control Panel
        Panel controlPanel = new Panel();
        add(controlPanel, BorderLayout.WEST);
        controlPanel.setBackground(Color.gray);
        controlPanel.setLayout(new ControlPanel());

        Button startButton = new Button("Start");
        startButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                uc.setPaused(false);                
            }
        });
        controlPanel.add(startButton);

        Button pauseButton = new Button("Pause");
        pauseButton.addActionListener(new ActionListener() {
     
            @Override
            public void actionPerformed(ActionEvent arg0) {
                uc.setPaused(true);                
            }
        });
        
        controlPanel.add(pauseButton);
        
        // UnitCell Panel
        panelUnitCell = new Panel();
        add(panelUnitCell, BorderLayout.CENTER);
    }


    public void paint(Graphics g) {

        Graphics guc = panelUnitCell.getGraphics();
        guc.clearRect(0, 0, getWidth(), getHeight());
        guc.setColor(Color.BLUE);
        for (int i = 0; i < uc.getParticles().size(); i++) {
            guc.fillOval( (int) (uc.getParticles().get(i).getPosition().getX() * SCALE_POSITION),
                    (int) (uc.getParticles().get(i).getPosition().getY() * SCALE_POSITION),
                    (int) (Parameters.PARTICLE_RADIUS * SCALE_RADIUS),
                    (int) (Parameters.PARTICLE_RADIUS * SCALE_RADIUS));
        }
        
    }
    
    public void updateUnitCell() {
        repaint();
    }


}
