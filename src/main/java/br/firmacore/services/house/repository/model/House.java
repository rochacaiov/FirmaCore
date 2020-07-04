package br.firmacore.services.house.repository.model;

import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_house")
public class House implements Serializable {
    private String uuid;
    private String owner;
    private int size;
    private int x;
    private int y;
    private int z;

    public House(){

    }

    public House(Player owner, int size){
        this.uuid = owner.getUniqueId().toString();
        this.owner = owner.getName();
        this.size = size;
        this.x = owner.getLocation().getBlockX();
        this.z = owner.getLocation().getBlockZ();
        this.y = owner.getWorld().getHighestBlockYAt(x, z) + 1;
    }

    @Id
    public String getUuid() {
        return uuid;
    }

    @Column(unique = true, nullable = false)
    public String getOwner(){
        return this.owner;
    }

    @Column(nullable = false)
    public int getSize() {
        return size;
    }

    @Column(nullable = false)
    public int getX() {
        return x;
    }

    @Column(nullable = false)
    public int getY() {
        return y;
    }

    @Column(nullable = false)
    public int getZ() {
        return z;
    }



    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
