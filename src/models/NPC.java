/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Rectangle;

/**
 *
 * @author Paul
 */
public class NPC extends Rectangle {

    private final String[] animations = new String[17];
    private String animation;
    private int pathingPoint = 0;
    private int frame = 0;
    private String directionMoving = "left";

    public NPC(int npcType) {
        for (int i = 1; i < 17; i++) {
            animations[i] = "src/sprites/" + npcType + " (" + i + ").png";
        }
        animation = "src/sprites/" + npcType + " (1).png";
        height = 60;
        width = 50;
        x = 0;
        y = 400;
    }

    public String getDirectionMoving() {
        return directionMoving;
    }

    public void setDirectionMoving(String set) {
        directionMoving = set;
    }

    public int getFrameAnimation() {
        return frame;
    }

    public void setFrameAnimation(int set) {
        frame = set;
    }

    public String[] getAnimations() {
        return animations;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String set) {
        animation = set;
    }

    public int getPathingPoint() {
        return pathingPoint;
    }

    public void setPathingPoint(int set) {
        pathingPoint = set;
    }
}
