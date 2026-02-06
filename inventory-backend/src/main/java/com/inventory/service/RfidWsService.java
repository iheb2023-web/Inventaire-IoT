package com.inventory.service;

import com.inventory.dto.RfidWsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RfidWsService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyNewProduct(String rfidTag, String location) {
        messagingTemplate.convertAndSend(
                "/topic/rfid",
                new RfidWsMessage("NEW_PRODUCT", rfidTag, location)
        );
    }
}
