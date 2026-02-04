package com.inventory.model.device;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RfidEvent {

    public enum EventType {
        ENTRY, EXIT
    }

    public enum EventLocation {
        STOCK, STORE
    }

    private Long id;
    private Long productId;
    private EventType eventType;
    private EventLocation location;
    private String esp32Id;
    private LocalDateTime createdAt;
}
