package br.firmacore.controllers.tribute;

import br.firmacore.Main;
import br.firmacore.controllers.tribute.repositories.models.Tribute;
import br.firmacore.controllers.tribute.runnables.TributeRunnable;
import br.firmacore.enums.PropertyType;

import java.util.HashSet;
import java.util.Set;

public class TributeManager {
    private Set<Tribute> tributes = new HashSet<>();

    public TributeManager(Main plugin){

    }

    public void add(Tribute tribute){
        this.tributes.add(tribute);
    }

    public int getAmountTributes(String uuid, PropertyType propertyType){
        int count = 0;
        for(Tribute tribute : this.tributes){
            if(tribute.getUuid().equals(uuid) && tribute.getPropertyType().equals(propertyType)){
                System.out.println(tribute.getPropertyType());
                count += 1;
            }
        }
        return count;
    }


    public Set<Tribute> getTributes() {
        return tributes;
    }
}
