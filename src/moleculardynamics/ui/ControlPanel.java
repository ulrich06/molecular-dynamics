package moleculardynamics.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class ControlPanel implements LayoutManager {

    int margin = 5;     // size of margins around all sides, in pixels

    // unused methods:
    public ControlPanel() {}
    public void addLayoutComponent(String name, Component c) {}
    public void removeLayoutComponent(Component c) {}

    // calculate needed width and height of container contents:
    Dimension totalContentSize(Container target) {
        int width = 0;
        int height = 0;
        for (int i = 0; i < target.getComponentCount(); i++) {
            Component c = target.getComponent(i);
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();
                if (d.width > width) width = d.width;
                height += d.height;
            }
        }
        return new Dimension(width + 2 * margin, height + 2 * margin);
    }

    public Dimension preferredLayoutSize(Container target) {
        Dimension contentSize = totalContentSize(target);
        Insets insets = target.getInsets();
        return new Dimension(contentSize.width + insets.left + insets.right,
                             contentSize.height + insets.top + insets.bottom);
    }
    
    public Dimension minimumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }
    
    // this method does the actual layout:
    public void layoutContainer(Container target) {

        Dimension contentSize = totalContentSize(target);
        Insets insets = target.getInsets(); // frame borders
        int vertSpace = target.getSize().height - (insets.top + insets.bottom);   // available space
        int padding = 0;
        
        if (target.getComponentCount() != 0)
            padding = (vertSpace - contentSize.height)/target.getComponentCount();  // padding between each component

        int leftEdge = insets.left + margin;

        int nextY = insets.top + margin;        // y value at top of next component
        for (int i = 0; i < target.getComponentCount(); i++) {  // loop over components
            Component c = target.getComponent(i);
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();
                d.width = contentSize.width - 2*margin;
                c.setLocation(leftEdge, nextY);
                c.setSize(d.width, d.height);
                nextY += d.height + padding;
            }
        }
    }

}
