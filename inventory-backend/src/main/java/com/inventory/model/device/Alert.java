package com.inventory.model.device;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Alert {

    public enum AlertType {
        LOW_WEIGHT
    }

    public enum AlertStatus {
        OPEN, RESOLVED
    }

    private Long id;
    private Long shelfId;
    private AlertType alertType;
    private AlertStatus status;
    private LocalDateTime createdAt;
}
