package br.firmacore.services.tribute.cache;

import br.firmacore.services.tribute.exceptions.TributeNotExistsException;
import br.firmacore.services.tribute.repository.models.Tribute;
import br.firmacore.enums.PropertyType;

import java.util.HashSet;
import java.util.Set;

public class TributeManager {
    private Set<Tribute> tributes = new HashSet<>();

    public TributeManager(){

    }

    public void saveOrUpdate(Tribute tribute){
        remove(tribute);
        add(tribute);
    }

    public void add(Tribute tribute){
        this.tributes.add(tribute);
    }

    public void remove(Tribute tribute){
        this.tributes.remove(tribute);
    }

    public Set<Tribute> getTributesOwner(String owner) throws TributeNotExistsException {
        Set<Tribute> ownerTributes = new HashSet<>();
        for(Tribute tribute : this.tributes){
            if(tribute.getOwner().equals(owner)){
                ownerTributes.add(tribute);
            }
        }
        if(ownerTributes.isEmpty()) throw new TributeNotExistsException();
        return ownerTributes;
    }

    public Tribute getTributeType(String owner, PropertyType propertyType) throws TributeNotExistsException {
        for(Tribute tribute : this.tributes){
            if(tribute.getPropertyType().equals(propertyType)){
                if(tribute.getOwner().equals(owner)){
                    return tribute;
                }
            }
        }
        throw new TributeNotExistsException();
    }

    public Set<Tribute> getTributes() {
        return tributes;
    }
}
