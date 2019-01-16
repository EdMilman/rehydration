package rehydrate.converters;

import com.fasterxml.jackson.databind.JsonNode;
import rehydrate.Address;

import java.lang.reflect.Field;

public class AddressFieldSetter implements FieldSetter {

    @Override
    public void setField(Object result, Field field, JsonNode val) throws IllegalAccessException {
        Address address = new Address();
        address.setBuildingNumber(val.get("buildingNumber").intValue());
        address.setStreetName(val.get("streetName").textValue());
        field.set(result, address);
    }
}
