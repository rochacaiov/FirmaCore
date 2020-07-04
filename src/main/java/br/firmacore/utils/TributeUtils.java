package br.firmacore.utils;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.tribute.exceptions.TributeTypeException;

public final class TributeUtils {
    public static PropertyType stringToPropertyType(String _propertyType) throws TributeTypeException {
        for(PropertyType propertyType : PropertyType.values()){
            if(propertyType.name().equalsIgnoreCase(_propertyType)){
                return propertyType;
            }
        }
        throw new TributeTypeException();
    }
}
