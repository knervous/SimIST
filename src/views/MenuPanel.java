/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;
import models.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;
import java.util.List;
import java.util.Map;

/**
 *
 * @author greg
 */
public class MenuPanel extends JFrame{
    

    private JLabel stationName;
    private JLabel item;
    private JLabel cost;
    private JLabel quantity;
    private JLabel actualItem;
    private JLabel actualCost;
    private JLabel actualQuantity;
    private JLabel signIntro;
    private JLabel signMessage;
    private JLabel signCost;
    private JLabel counterIntro;
    private JButton grabItems = new JButton("Grab Items");
    private JButton tossItems = new JButton("Toss Items");
    private JButton buyItems = new JButton("Buy Items");
    private JSpinner quantSpin;
    private FoodStations infStation = new FoodStations();
    private ArrayList<JSpinner> allSpinners = new ArrayList<JSpinner>();
    private int offset = 2;
    private GridBagConstraints layoutConst = new GridBagConstraints();
    private JPanel content = new JPanel();
    
    
    
    
    public void populateFoodMenu(FoodStations station){
       
        this.infStation = station;
        allSpinners.clear();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        layoutConst.insets = new Insets(10, 10, 10, 10);
        
        stationName = new JLabel(station.getStationName());
        layoutConst.gridx = 1;
        layoutConst.gridy = 0;
        content.add(stationName, layoutConst);

        item = new JLabel("Item");
        layoutConst.gridx = 0;
        layoutConst.gridy = 1;
        content.add(item, layoutConst);
        
        quantity = new JLabel("Quantity");
        layoutConst.gridx = 1;
        layoutConst.gridy = 1;
        content.add(quantity, layoutConst);
        
        cost = new JLabel("Cost");
        layoutConst.gridx = 2;
        layoutConst.gridy = 1;
        content.add(cost, layoutConst);
        
        int i = 0;
                   
        while (i < station.getStationObjects().length){
            
            actualItem = new JLabel(station.getStationObjects()[i].getName());
            layoutConst.gridx = 0;
            layoutConst.gridy = i + offset;
            content.add(actualItem, layoutConst);
            
            actualQuantity = new JLabel(Integer.toString(station.getStationObjects()[i].getQuantity()));
            layoutConst.gridx = 1;
            layoutConst.gridy = i + offset;
            content.add(actualQuantity, layoutConst);
            
            actualCost = new JLabel(String.format("$%.2f",station.getStationObjects()[i].getCost()));
            layoutConst.gridx = 2;
            layoutConst.gridy = i + offset;
            content.add(actualCost, layoutConst);
            
            quantSpin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, station.getStationObjects()[i].getQuantity(), 1.0));
            layoutConst.gridx = 3;
            layoutConst.gridy = i + offset;
            content.add(quantSpin, layoutConst);
            
            ((DefaultEditor) quantSpin.getEditor()).getTextField().setEditable(false);
            i++;
            allSpinners.add(quantSpin);

        }
        //grabItems = new JButton("Grab Items");
        layoutConst.gridx = 0;
        layoutConst.gridy = i + offset + 1;
        content.add(grabItems, layoutConst);
        
        setContentPane(content);
        setTitle(station.getStationName());
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    
    public void addItemsToInv(ActionListener al){    
       grabItems.addActionListener(al);
    }
    public void removeItemsFromInv(ActionListener al){    
       tossItems.addActionListener(al);
    }
    
    public JButton getTossItems()
    {
        return tossItems;
    }
    
    public double getSpinnerValue(int i)
    {
        return (double) allSpinners.get(i).getValue();
    }
    
    public StoreObjects[] getStoreObjects()
    {
        return infStation.getStationObjects();
    }
    
    
    public JButton getGrabItems()
    {
        return grabItems;
    }
    
    public JPanel getContent()
    {
        return content;
    }
    
    public void populateTrashMenu(TrashStation trashStation, CharacterInventory charInventory)
    {
        allSpinners.clear();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        layoutConst.insets = new Insets(10, 10, 10, 10);
        
        item = new JLabel("Item");
        layoutConst.gridx = 0;
        layoutConst.gridy = 1;
        content.add(item, layoutConst);
        
        quantity = new JLabel("Quantity");
        layoutConst.gridx = 1;
        layoutConst.gridy = 1;
        content.add(quantity, layoutConst);
        
        int i = 0;
        List keys = new ArrayList(charInventory.getMap().keySet());
        List values = new ArrayList(charInventory.getMap().values());
        
        while (i < charInventory.getMap().size()){
            actualItem = new JLabel((String)keys.get(i));
            layoutConst.gridx = 0;
            layoutConst.gridy = i + offset;
            content.add(actualItem, layoutConst);
            
            actualQuantity = new JLabel(Integer.toString((int)values.get(i)));
            layoutConst.gridx = 1;
            layoutConst.gridy = i + offset;
            content.add(actualQuantity, layoutConst);
            
            quantSpin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, (int)values.get(i), 1.0));
            layoutConst.gridx = 2;
            layoutConst.gridy = i + offset;
            content.add(quantSpin, layoutConst);
            
            ((DefaultEditor) quantSpin.getEditor()).getTextField().setEditable(false);
            i++;
            allSpinners.add(quantSpin);
        }
        layoutConst.gridx = 0;
        layoutConst.gridy = i + offset + 1;
        content.add(tossItems, layoutConst);
        
        setContentPane(content);
        setTitle("TRASH MENU");
        setVisible(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        
    }
    
    public void populateCounterMenu(CharacterInventory charInventory)
    {
        content.removeAll();
        content.setLayout(new GridBagLayout());
        layoutConst.insets = new Insets(10, 10, 10, 10);
        
        counterIntro = new JLabel("Welcome to Au Bon Pain! It's time to pay the piper.");
        layoutConst.gridx = 1;
        layoutConst.gridy = 0;
        content.add(counterIntro, layoutConst);
        
        int i = 0;
        List keys = new ArrayList(charInventory.getMap().keySet());
        List values = new ArrayList(charInventory.getMap().values());
        
        while (i < charInventory.getMap().size()){
            actualItem = new JLabel((String)keys.get(i));
            layoutConst.gridx = 0;
            layoutConst.gridy = i + offset;
            content.add(actualItem, layoutConst);

            actualQuantity = new JLabel(Integer.toString((int)values.get(i)));
            layoutConst.gridx = 1;
            layoutConst.gridy = i + offset;
            content.add(actualQuantity, layoutConst);

            quantSpin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, (int)values.get(i), 1.0));
            layoutConst.gridx = 2;
            layoutConst.gridy = i + offset;
            content.add(quantSpin, layoutConst);

            ((DefaultEditor) quantSpin.getEditor()).getTextField().setEditable(false);
            i++;
            allSpinners.add(quantSpin);
            }
            
        layoutConst.gridx = 0;
        layoutConst.gridy = i + offset + 1;
        content.add(buyItems, layoutConst);
        
        setContentPane(content);
        setTitle("FRONT COUNTER");
        setVisible(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void populateSignMenu(String objectName, float objectCost)
    {
        content.removeAll();
        content.setLayout(new GridBagLayout());
        layoutConst.insets = new Insets(10, 10, 10, 10);
        signIntro = new JLabel("Welcome to Au Bon Pain!");
        layoutConst.gridx = 0;
        layoutConst.gridy = 0;
        content.add(signIntro, layoutConst);
        
        signMessage = new JLabel("Today's 15% off special is: " + objectName);
        layoutConst.gridx = 0;
        layoutConst.gridy = 1;
        content.add(signMessage, layoutConst);
        
        signCost = new JLabel(String.format("New Cost: $%.2f", objectCost));
        layoutConst.gridx = 0;
        layoutConst.gridy = 2;
        content.add(signCost, layoutConst);
        
        setContentPane(content);
        setTitle("ABP Sign");
        setVisible(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);    
    }
}
