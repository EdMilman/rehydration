package jackson_hydration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Collection;

public interface Hydrate {

    default JsonNode hydrate(Collection<Event> events) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return events.stream().map(Event::getPayload).reduce(mapper.readTree("{}"), (node1, node2) -> {
                    node2.fieldNames().forEachRemaining(field -> {
                        if(node2.get(field).isNull()) {
                            ((ObjectNode) node1).remove(field);
                        } else {
                            ((ObjectNode) node1).set(field, node2.get(field));
                        }
                    });
                    return node1;
                }
        );
    }
}
