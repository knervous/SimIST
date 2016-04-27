
package views;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;


public class ControlPanelView extends JFrame{
        
       private JSlider npcSpeed = new JSlider(JSlider.HORIZONTAL,1,48,24);
       private JSlider spawnRate = new JSlider(JSlider.HORIZONTAL,0,1000,0);
       private JLabel speed = new JLabel("NPC Speed");
       private JLabel rate = new JLabel("Spawn Rate");

        public ControlPanelView(){
        setUndecorated(true);
        setVisible(true);
        setSize(784,75);
        setLayout(new GridLayout(1,4));
        add(npcSpeed);
        add(speed);
        add(spawnRate);
        add(rate);
        
        
     
        }
        
        public int getSpeed()
        {
            return npcSpeed.getValue();
        }
        
        public int getRate()
        {
            return spawnRate.getValue();
        }
        
        public JSlider getSliderOne()
        {
            return npcSpeed;
        }
        
        public JSlider getSliderTwo()
        {
            return spawnRate;
        }

       
       

    }