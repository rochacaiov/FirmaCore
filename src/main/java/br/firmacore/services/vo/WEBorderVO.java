package br.firmacore.services.vo;

import org.bukkit.World;

public class WEBorderVO {
    World world;
    int wallXMax;
    int wallZMax;
    int wallXMin;
    int wallZMin;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getWallXMax() {
        return wallXMax;
    }

    public void setWallXMax(int wallXMax) {
        this.wallXMax = wallXMax;
    }

    public int getWallZMax() {
        return wallZMax;
    }

    public void setWallZMax(int wallZMax) {
        this.wallZMax = wallZMax;
    }

    public int getWallXMin() {
        return wallXMin;
    }

    public void setWallXMin(int wallXMin) {
        this.wallXMin = wallXMin;
    }

    public int getWallZMin() {
        return wallZMin;
    }

    public void setWallZMin(int wallZMin) {
        this.wallZMin = wallZMin;
    }
}
