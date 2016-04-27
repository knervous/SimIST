package controllers;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import views.*;
import models.*;
import javax.swing.Timer;
//import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.zip.GZIPOutputStream;
//import java.util.Base64;
/**
 *
 * @author Paul
 */
public class GameRoomController {

    private GameRoom room;
    private Timer gameTimer;
    private Customer student;
    private CharacterMovement charMovement;
    private FoodStations stations;
    private Randomize randomize;
    private MenuPanel menuPanel;
    private Inventory inventory;
    private TestFrame testFrame;
    private static Timer signTimer;
    private Timer npcTimer;
    private Timer npcSpawnTimer;
    private StoreObjects signObject;
    private static final int CHANGE_INTERVAL = 900000;
    protected Sequencer sequence;
    private ArrayList<NPC> npcs;
    private boolean customerInteracting = false;
    ControlPanelView cpv;

//    static final Base64 base64 = new Base64();
    public GameRoomController(Customer inf_student, GameRoom inf_room) throws Exception {
        testFrame = new TestFrame();
        student = inf_student;
        stations = new FoodStations();
        npcs = new ArrayList<>();
        room = inf_room;
        room.setNPCs(npcs);
        testFrame.setSize(room.getSize());
        room.setFocusable(true);
        randomize = new Randomize();
        menuPanel = new MenuPanel();
        inventory = new Inventory();
        charMovement = new CharacterMovement(student, room, inventory);
        testFrame.add(room, BorderLayout.CENTER);
        
        signObject = randomize.getRandObject(randomize.getAllFood());
        signObject.changeCost((float) (signObject.getCost() * .85));
        if (room instanceof AuBonPainPanel) {
            cpv = new ControlPanelView();
            cpv.setLocationRelativeTo(testFrame);
            cpv.setLocation(cpv.getX(), cpv.getY() - 330);
            room.requestFocus();
            cpv.addMouseListener(new FocusRequester());
            cpv.getSliderOne().addMouseListener(new FocusRequester());
            cpv.getSliderTwo().addMouseListener(new FocusRequester());
        }
        addKeyListeners();

        InputStream is;
        sequence = MidiSystem.getSequencer();
        sequence.open();
        is = new BufferedInputStream(new FileInputStream(new File("jamiroquai-canned_heat.mid")));
        sequence.setSequence(is);
        sequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequence.start();
        is.close();

    }

    private void addKeyListeners() {
        /*
         STARTING TIMER
         */
        gameTimer = new Timer(5, new GameTimer());
        gameTimer.start();
        signTimer = new Timer(CHANGE_INTERVAL, new SignTimer());
        signTimer.start();
        if(room instanceof AuBonPainPanel)
        {
        npcTimer = new Timer(25, new NPCTimer());
        npcTimer.start();
        npcSpawnTimer = new Timer((int) (4500 - (cpv.getRate() * 4.4)), new NPCSpawnTimer());
        npcSpawnTimer.start();
        }

        /*
         SETTING LISTENERS ON BUTTONS FOR ADDING/SUBTRACTING/USING FROM INVENTORY
         */
        menuPanel.addItemsToInv(new AddItemListener());
        menuPanel.removeItemsFromInv(new RemoveItemListener());
        inventory.addUseListener(new UseItemListener());
        menuPanel.buyItemsInInv(new BuyItemListener());

        /*
         SETTING KEYLISTENERS ON THE PANEL TO DETECT MOVEMENT
         */
        room.requestFocusInWindow();
        room.addKeyListener(charMovement);
        room.addKeyListener(new InteractKeyListener());

        /*
         ADD THE RESIZING LISTENER ON FRAME
         */
        testFrame.addComponentListener(new Resizer());

    }

    /*
    
     TIMER RUNS EVERY .05 SECONDS TO REFRESH

     */
    class GameTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            room.repaint();
            
            if (inventory != null) {
                inventory.getContainer().repaint();
            }
            if(room instanceof AuBonPainPanel)
            {
            npcSpawnTimer.setDelay((int) (4500 - (cpv.getRate() * 4.4)));
            npcTimer.setDelay(48-cpv.getSpeed());
            }
        }
    }
    
    private class FocusRequester implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            room.requestFocus();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }

    private class NPCSpawnTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int rand = new Random().nextInt(2);
            if (rand == 0) {
            } else {
                npcs.add(new NPC(new Random().nextInt(16) + 1));
            }

        }

    }

    private class NPCTimer implements ActionListener {

        private final ArrayList<Point> pathingPoints = new ArrayList<>();
        private int animationScaler = 0;
        private int customerWaitTime = 0;

        public NPCTimer() {
            pathingPoints.add(new Point(230, 400));
            pathingPoints.add(new Point(230, 240));
            pathingPoints.add(new Point(570, 240));
            pathingPoints.add(new Point(570, 435));
            pathingPoints.add(new Point(900, 435));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            animationScaler += 10;
            customerWaitTime += 10;
            int xdif;
            int ydif;
            ArrayList<NPC> toRemove = new ArrayList<>();
            for (NPC npc : npcs) {
                System.out.println(customerInteracting);

                if (animationScaler % 100 == 0) {

                    switch (npc.getDirectionMoving()) {
                        case "right":
                            npc.setAnimation(npc.getAnimations()[npc.getFrameAnimation() + 5]);
                            npc.setFrameAnimation(npc.getFrameAnimation() + 1);
                            break;
                        case "up":
                            npc.setAnimation(npc.getAnimations()[npc.getFrameAnimation() + 9]);
                            npc.setFrameAnimation(npc.getFrameAnimation() + 1);
                            break;
                        case "down":
                            npc.setAnimation(npc.getAnimations()[npc.getFrameAnimation() + 1]);
                            npc.setFrameAnimation(npc.getFrameAnimation() + 1);
                            break;
                        case "left":
                            npc.setAnimation(npc.getAnimations()[npc.getFrameAnimation() + 13]);
                            npc.setFrameAnimation(npc.getFrameAnimation() + 1);
                            break;
                    }
                    if (npc.getFrameAnimation() > 3) {
                        npc.setFrameAnimation(0);
                    }
                }
                for (int i = 0; i < pathingPoints.size(); i++) {
                    int oldX = npc.x;
                    int oldY = npc.y;

                    if (pathingPoints.get(i).x == npc.x && pathingPoints.get(i).y == npc.y) {
                        if (i == 2 && customerWaitTime % 5000 != 0) {
                            customerInteracting = true;

                        } else {
                            npc.setPathingPoint(npc.getPathingPoint() + 1);
                            customerInteracting = false;
                        }
                    }
                    if (npc.getPathingPoint() == i) {

                        xdif = pathingPoints.get(i).x - npc.x;
                        ydif = npc.y - pathingPoints.get(i).y;

                        if (xdif > 0) {
                            npc.setLocation(npc.x + 1, npc.y);
                            npc.setDirectionMoving("right");

                        } else if (xdif < 0) {
                            npc.setLocation(npc.x - 1, npc.y);
                            npc.setDirectionMoving("left");
                        }

                        if (ydif > 0) {
                            npc.setLocation(npc.x, npc.y - 1);
                            npc.setDirectionMoving("up");
                        } else if (ydif < 0) {
                            npc.setLocation(npc.x, npc.y + 1);
                            npc.setDirectionMoving("down");
                        }
                    }

                    for (NPC mNpc : npcs) {
                        if ((npc.intersects(mNpc) && npc != mNpc) || npc.intersects(student)) {
                            npc.x = oldX;
                            npc.y = oldY;

                        }
                        if (npc.intersects(mNpc) && npc != mNpc) {
                            if (npc.x < 100) {
                                toRemove.add(npc);
                            }
                        }
                    }

                }

                if (npc.x > 850) {
                    toRemove.add(npc);
                }
            }
            for (NPC removeNPC : toRemove) {
                npcs.remove(removeNPC);
            }
            toRemove.clear();

        }
    }

    class SignTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
//            System.out.printf("%s price returned from $%.2f", signObject.getName(), signObject.getCost());
            signObject.changeCost((float) (signObject.getCost() * 1.176470588));
//            System.out.printf(" to $%.2f\n", signObject.getCost());

            signObject = randomize.getRandObject(randomize.getAllFood());
//            System.out.println("Food Object: " + signObject.getName());
//            System.out.printf("Price changed from $%.2f", signObject.getCost());
            signObject.changeCost((float) (signObject.getCost() * .85));
//            System.out.printf(" to $%.2f\n", signObject.getCost());
        }
        // Got rid of duplicate code here. George
    }


    /*
    
     ADD ITEM FROM STATION BUTTON
    
     */
    public class AddItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            StoreObjects[] objectsTemp = menuPanel.getStoreObjects();

            for (int i = 0; i < objectsTemp.length; i++) {
                student.getInventory().addItem(objectsTemp[i], menuPanel.getSpinnerValue(i));

            }
            menuPanel.dispose();
        }
    }

    public class BuyItemListener implements ActionListener {

        private List keys = new ArrayList(student.getInventory().getMap().keySet());
        private List values = new ArrayList(student.getInventory().getMap().values());

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("keys.size() = " + keys.size());
            System.out.println("menuPanel.getSpinners().size() = " + menuPanel.getSpinners().size());
            System.out.println("values.size() = " + values.size());
//            for (int i = 0; i < menuPanel.getSpinners().size(); i++){
            for (int i = 0; i < keys.size(); i++) {
                System.out.println(student.getInventory().getInventoryObjects().get(i).getName());
                System.out.println(keys.get(i) + "");
                int k = 0;
                for (int j = 0; j < student.getInventory().getInventoryObjects().size(); j++) {
                    if (student.getInventory().getInventoryObjects().get(j).getName().equalsIgnoreCase(keys.get(i) + "")) {
                        System.out.println("Working up to this point.");
                        student.getInventory().getInventoryObjects().get(k).setPaidFor(true);
                        System.out.println("setPaidFor(k) = " + student.getInventory().getInventoryObjects().get(k).getPaidFor());
                        k++;
                        if (k == (int) menuPanel.getSpinners().get(i).getValue()) {
                            break;
                        }
//                        while (k < (int)menuPanel.getSpinners().get(i).getValue()){
//                            student.getInventory().getInventoryObjects().get(k).setPaidFor(true);
//                        }

                    }
                }
            }
        }
    }

    /*
    
     REMOVE ITEM FROM STATION BUTTON
    
     */
    public class RemoveItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            ArrayList<StoreObjects> toRemove = new ArrayList<>();
            for (StoreObjects item : student.getInventory().getInventoryObjects()) {
                student.getInventory().removeItem(item, menuPanel.getSpinnerValue(student.getInventory().getInventoryObjects().indexOf(item)));
                if (student.getInventory().getMap().get(item.getName()) < 1) {
                    toRemove.add(item);
                    //charInventory.getInventoryObjects().remove(charInventory.getInventoryObjects().indexOf(item));
                }

            }
            for (StoreObjects remove : toRemove) {
                student.getInventory().getInventoryObjects().remove(remove);
                student.getInventory().getMap().remove((String) remove.getName());
            }
            menuPanel.dispose();

        }
    }

    /*
    
     USE ITEM FROM INVENTORY
    
     */
    public class UseItemListener implements ActionListener {

        public UseItemListener() {

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            student.getInventory().removeItem(inventory.getItemSelected(), 1.0);

            if (student.getInventory().getMap().get(inventory.getItemSelected().getName()) < 1) {
                student.getInventory().getInventoryObjects().remove(inventory.getItemSelected());
                student.getInventory().getMap().remove((String) inventory.getItemSelected().getName());
                inventory.getSideBar().removeAll();
            }

            inventory.popUpInventory(student.getInventory());
            inventory.getSideBar().repaint();

        }
    }

    /*
    
     INTERACTING WITH STATIONS
    
     */
    public class InteractKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent ke) {

        }

        @Override
        public void keyPressed(KeyEvent ke) {
            if (menuPanel != null) {
                menuPanel.dispose();
            }
            if (room instanceof AuBonPainPanel) {
                for (NPC npc : npcs) {
                    if (ke.getKeyCode() == KeyEvent.VK_P && Math.abs(student.getCenterX() - npc.getCenterX()) < 75 && Math.abs(student.getCenterY() - npc.getCenterY()) < 75) {
                        Point position = new Point((int) npc.getX(), (int) npc.getY());
                        double angle = getAngle(npc.getLocation(), student.getLocation());
                        double scale_y = Math.cos(Math.toRadians(angle)) * 50;
                        double scale_x = Math.sin(Math.toRadians(angle)) * 50;
                        position.setLocation(position.x - scale_x, position.y + scale_y);
                        npc.setLocation(position);
                        for (NPC npc2 : npcs) {
                            while ((npc.intersects(npc2) && npc != npc2) || npc.intersects(student)) {
                                npc2.y--;
                                npc2.x--;
                            }
                        }
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_SPACE && charMovement.getIsInteracting()) {

                    switch (charMovement.getStationNumber()) {

                        case 0:
                            menuPanel.populateCounterMenu(student.getInventory());
                            System.out.println("counter initiated");
                            break;
                        case 1:
//                            System.out.println("coffee initiated");
                            menuPanel.populateFoodMenu(new CoffeeStation(randomize.getCoffeeObjects()));
                            break;
                        case 2:
//                            System.out.println("sign initiated");
                            menuPanel.populateSignMenu(signObject.getName(), signObject.getCost());
                            break;
                        case 3:
//                            System.out.println("trash initiated");
                            menuPanel.populateTrashMenu(new TrashStation(), student.getInventory());
                            break;
                        case 4:
//                            System.out.println("bakery initiated");
                            menuPanel.populateFoodMenu(new BreadStation(randomize.getBakeryObjects()));
                            break;
                        case 5:
//                            System.out.println("fruit initiated");
                            menuPanel.populateFoodMenu(new FruitStation(randomize.getFruitObjects()));
                            break;
                        case 6:
//                            System.out.println("soup initiated");
                            menuPanel.populateFoodMenu(new SoupStation(randomize.getSoupObjects()));
                            break;
                        case 7:
//                            System.out.println("cooler initiated");
                            menuPanel.populateFoodMenu(new CoolerStation(randomize.getCoolerObjects()));
                            break;
                        case 8:
                            break;

                    }
                }

            }

        }

        @Override
        public void keyReleased(KeyEvent ke) {

        }

        public double getAngle(Point center, Point target) {
            double theta = Math.atan2(target.y - center.y, target.x - center.x);
            theta += Math.PI / 2.0;
            double angle = Math.toDegrees(theta);
            if (angle < 0) {
                angle += 360;
            }
            return angle;
        }
    }

    public class Resizer implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent ce) {

            JFrame placeHolder = (JFrame) ce.getSource();
            if (room instanceof AuBonPainPanel) {
                student.setBounds(placeHolder.getWidth() - student.width * 2, Math.round(placeHolder.getHeight() - student.height * 2.5f), student.width, student.height);
            }
        }

        @Override
        public void componentMoved(ComponentEvent ce) {

        }

        @Override
        public void componentShown(ComponentEvent ce) {
        }

        @Override
        public void componentHidden(ComponentEvent ce) {
            sequence.stop();
            testFrame.dispose();
        }

    }
//    public static String serializeObjectToString(Object object){
//        String stringToReturn = "";
//        try (
//                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);){
//            objectOutputStream.writeObject(object);
//            objectOutputStream.flush();
//            stringToReturn = new String(Base64.encode(arrayOutputStream.toByteArray()));
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//        return stringToReturn;
//    }
}
