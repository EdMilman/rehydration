package jackson_hydration;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalTime;
import java.util.UUID;

public class Event {
    private JsonNode payload;

    public Event(JsonNode payload) {
        this.payload = payload;
    }

    public UUID getAggregateId() {
        return UUID.randomUUID();
    }

    public LocalTime createdOn() {
        return LocalTime.now();
    }

    public JsonNode getPayload() {
        return payload;
    }
}
