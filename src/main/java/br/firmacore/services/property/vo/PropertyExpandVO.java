package br.firmacore.services.property.vo;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PropertyExpandVO {
    private Player owner;
    private World world;
    private int size;
    private BlockVector3 max;
    private BlockVector3 min;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
