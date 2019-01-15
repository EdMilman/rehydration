package rehydrate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class AggregateBuilder extends HydrationImpl {


    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        AggregateBuilder builder = new AggregateBuilder();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"firstName\":\"old first name\",\"balance\":\"50.0\"}");
        JsonNode node1 = mapper.readTree("{\"firstName\":\"new first name\"}");
        JsonNode node2 = mapper.readTree("{\"lastName\":\"old last name\"}");
        JsonNode node3 = mapper.readTree("{\"lastName\":\"new last name\"}");
        JsonNode node4 = mapper.readTree("{\"lastName\":\"another last name\"}");
        JsonNode node5 = mapper.readTree("{ \"address\": {\"buildingNumber\": 100, \"streetName\":\"road name\"} }");
        Collection<Event> lst = List.of(
                new Event(node),
                new Event(node1),
                new Event(node2),
                new Event(node3),
                new Event(node4),
                new Event(node5)
        );
        AccountHolderAggregate ag = (AccountHolderAggregate) builder.applyEvents(AccountHolderAggregate.class, lst);
        System.out.println(ag);
    }
}
