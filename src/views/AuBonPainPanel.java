package views;

import controllers.GameRoomController;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class AuBonPainPanel extends GameRoom {

    public static final int COUNTERWIDTH = 160;
    public static final int COUNTERHEIGHT = 192;
    public static final int COFFEEWIDTH = 309;
    public static final int COFFEEHEIGHT = 40;
    public static final int SIGNWIDTH = 50;
    public static final int SIGNHEIGHT = 45;
    public static final int TRASHWIDTH = 114;
    public static final int TRASHHEIGHT = 62;
    public static final int BAKERYWIDTH = 50;
    public static final int BAKERYHEIGHT = 150;
    public static final int FRUITWIDTH = 75;
    public static final int FRUITHEIGHT = 65;
    public static final int SOUPWIDTH = 144;
    public static final int SOUPHEIGHT = 40;
    public static final int COOLERWIDTH = 310;
    public static final int COOLERHEIGHT = 30;

    private Rectangle counter;
    private Rectangle coffee;
    private Rectangle sign;
    private Rectangle trash;
    private Rectangle bakery;
    private Rectangle fruit;
    private Rectangle soup;
    private Rectangle cooler;
    private Customer student;
    private ArrayList<NPC> npcs;
    private NPC cashier;
    

    private JLabel temp = new JLabel();

    public AuBonPainPanel(Customer inf_Student) {
        super();
        student = inf_Student;
        setSize(800,600);
        cashier = new NPC(new Random().nextInt(15));
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        add(temp);
        temp.setBounds(200, 200, 200, 200);
        init();

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                temp.setText(e.getPoint().toString());
            }
        });
        this.setFocusable(true);
    }

    private void init() {
        counter = new Rectangle();
        coffee = new Rectangle();
        sign = new Rectangle();
        trash = new Rectangle();
        bakery = new Rectangle();
        fruit = new Rectangle();
        soup = new Rectangle();
        cooler = new Rectangle();

    }
    
    @Override
    public void setNPCs(ArrayList<NPC> set)
    {
        npcs = set;
    }
    
    @Override
    public ArrayList<NPC> getNPCs()
    {
        return npcs;
    }
    public ArrayList<Rectangle> getStations() {
        return new ArrayList<>(Arrays.asList(counter, coffee, sign, trash, bakery, fruit, soup, cooler));
    }

    public void setKeyListener(KeyListener kl) {
        addKeyListener(kl);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setPreferredSize(getParent().getSize());
        super.paintComponent(g);
        double playerHeight = getParent().getHeight() * .125;
        double playerWidth = getParent().getWidth() * .06;
        g.drawImage(new ImageIcon("floor.png").getImage(), 0, 0, getParent().getWidth(), getParent().getHeight(), null);
        g.drawImage(new ImageIcon(student.getAnimation()).getImage(), student.x, student.y, (int) playerWidth, (int) playerHeight, null);
        for(NPC npc : npcs)
        {
            g.drawImage(new ImageIcon(npc.getAnimation()).getImage(), npc.x, npc.y, npc.width, npc.height, null);
        }
        g.drawImage(new ImageIcon(cashier.getAnimations()[13]).getImage(), 660, 225, cashier.width, cashier.height, null);
        refreshStations();
        
    }

    public void refreshStations() {
        student.setBounds(Math.round(student.x), student.y, Math.round(getParent().getWidth() * .06f), Math.round(getParent().getHeight() * .125f));
        counter.setBounds(Math.round(getParent().getWidth() * .8f), Math.round(getParent().getHeight() * .26f), Math.round(getParent().getWidth() * .2f), Math.round(getParent().getHeight() * .32f));
        coffee.setBounds(Math.round(getParent().getWidth() * .25f), Math.round(getParent().getHeight() * .86f), Math.round(getParent().getWidth() * .4f), Math.round(getParent().getHeight() * .067f));
        sign.setBounds(Math.round(getParent().getWidth() * .105f), Math.round(getParent().getHeight() * .6f), Math.round(getParent().getWidth() * .06f), Math.round(getParent().getHeight() * .075f));
        trash.setBounds(Math.round(getParent().getWidth() * .074f), Math.round(getParent().getHeight() * .82f), Math.round(getParent().getWidth() * .14f), Math.round(getParent().getHeight() * .1f));
        bakery.setBounds(0, Math.round(getParent().getHeight() * .22f), Math.round(getParent().getWidth() * .063f), Math.round(getParent().getHeight() * .25f));
        fruit.setBounds(0, 0, Math.round(getParent().getWidth() * .15f), Math.round(getParent().getHeight() * .15f));
        soup.setBounds(Math.round(getParent().getWidth() * .18f), 0, Math.round(getParent().getWidth() * .18f), Math.round(getParent().getHeight() * .067f));
        cooler.setBounds(Math.round(getParent().getWidth() * .4f), 0, Math.round(getParent().getWidth() * .4f), Math.round(getParent().getHeight() * .08f));
    }

}
