package com.inventory.model.device;

import lombok.Data;

@Data
public class Device {

    public enum DeviceType {
        ESP32
    }

    public enum DeviceLocation {
        STOCK, STORE
    }

    private Long id;
    private String name;
    private DeviceType type;
    private DeviceLocation location;
    private String ipAddress;
}

