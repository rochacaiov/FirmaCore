package br.firmacore.services.tribute.repository.models;

import br.firmacore.enums.PropertyType;
import org.bukkit.Bukkit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

@Table(name = "tb_tribute")
public class Tribute implements Serializable {
    private long id;
    private PropertyType propertyType;
    private String owner;
    private String uuid;
    private int amount;
    private int dayApplyTribute;
    private int hourApplyTribute;
    private double value;

    public Tribute(){

    }

    public Tribute(String uuid, PropertyType propertyType, double value){
        this.owner = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        this.uuid = uuid;
        this.propertyType = propertyType;
        this.amount = 0;
        this.dayApplyTribute = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        this.hourApplyTribute = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(updatable = false, nullable = false)
    public String getOwner(){
        return this.owner;
    }

    @Column(updatable = false, nullable = false)
    public String getUuid() {
        return uuid;
    }

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    public PropertyType getPropertyType() {
        return propertyType;
    }

    @Column(nullable = false)
    public int getAmount() {
        return amount;
    }

    @Column(updatable = false, nullable = false)
    public int getDayApplyTribute() {
        return dayApplyTribute;
    }

    @Column(updatable = false, nullable = false)
    public int getHourApplyTribute() {
        return hourApplyTribute;
    }

    @Column(nullable = false)
    public double getValue() {
        return value;
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
