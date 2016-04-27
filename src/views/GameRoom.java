/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import models.NPC;

/**
 *
 * @author Paul
 */
public abstract class GameRoom extends JPanel{
    
    public abstract ArrayList<Rectangle> getStations();
    public abstract void setNPCs(ArrayList<NPC> set);
    public abstract ArrayList<NPC> getNPCs();
    
}
