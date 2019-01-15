package rehydrate.converters;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;

public class DoubleFieldSetter implements FieldSetter {
    @Override
    public void setField(Object result, Field field, JsonNode val) throws IllegalAccessException {
        field.set(result, Double.parseDouble(val.textValue()));
    }

}
