package br.firmacore.controllers.house.repositories.models;

import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Calendar;

@Table(name = "tb_house")
public class House implements Serializable {
    private String uuid;
    private int size;
    private int dayBuy;
    private int hourBuy;
    private int x;
    private int y;
    private int z;

    public House(){

    }

    public House(Player owner, int size){
        this.uuid = owner.getUniqueId().toString();
        this.size = size;
        this.dayBuy = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        this.hourBuy = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        this.x = owner.getLocation().getBlockX();
        this.z = owner.getLocation().getBlockZ();
        this.y = owner.getWorld().getHighestBlockYAt(x, z) + 1;
    }

    @Id
    public String getUuid() {
        return uuid;
    }

    @Column(nullable = false)
    public int getSize() {
        return size;
    }

    @Column(nullable = false)
    public int getDayBuy() {
        return dayBuy;
    }

    @Column(nullable = false)
    public int getHourBuy() {
        return hourBuy;
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

    public void setSize(int size) {
        this.size = size;
    }

    public void setDayBuy(int dayBuy) {
        this.dayBuy = dayBuy;
    }

    public void setHourBuy(int hourBuy) {
        this.hourBuy = hourBuy;
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
