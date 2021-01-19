package dev.babebbu.ev.ocpp.centralsystem.models.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootNotificationConfirm {

    private String status;
    private String currentTime;
    private int heartBeatInterval;

}
