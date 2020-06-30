package br.firmacore.controllers.tribute.repositories.models;

import br.firmacore.enums.PropertyType;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_tribute")
public class Tribute implements Serializable {
    private String uuid;
    private PropertyType propertyType;
    private double value;

    public Tribute(){

    }

    public Tribute(String uuid, PropertyType propertyType, double value){
        this.uuid = uuid;
        this.propertyType = propertyType;
        this.value = value;
    }

    @Id
    public String getUuid() {
        return uuid;
    }

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    public PropertyType getPropertyType() {
        return propertyType;
    }

    @Column(updatable = false, nullable = false)
    public double getValue() {
        return value;
    }



    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
