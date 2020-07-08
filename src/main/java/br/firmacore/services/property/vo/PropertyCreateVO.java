package br.firmacore.services.property.vo;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PropertyCreateVO {
    private Player owner;
    private World world;
    private BlockVector3 max;
    private BlockVector3 min;
    private int sizeX;
    private int sizeZ;
    private int x;
    private int z;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockVector3 getMax() {
        return max;
    }

    public void setMax(BlockVector3 max) {
        this.max = max;
    }

    public BlockVector3 getMin() {
        return min;
    }

    public void setMin(BlockVector3 min) {
        this.min = min;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int size) {
        this.sizeX = size;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "PropertyCreateVO{" +
                "owner=" + owner +
                ", world=" + world +
                ", max=" + max +
                ", min=" + min +
                ", sizeX=" + sizeX +
                ", sizeZ=" + sizeZ +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
