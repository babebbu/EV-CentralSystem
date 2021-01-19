package dev.babebbu.ev.ocpp.centralsystem.models.schemas;

import lombok.Data;

@Data
public class BootNotificationRequest {
    private String chargePointVendor;
    private String chargePointModel;
}
