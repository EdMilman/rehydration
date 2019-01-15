package rehydrate.converters;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;

public interface FieldSetter {
    void setField(Object result, Field field, JsonNode val) throws IllegalAccessException;
}
