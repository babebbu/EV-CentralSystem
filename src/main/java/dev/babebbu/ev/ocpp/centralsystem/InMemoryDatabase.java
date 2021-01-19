package dev.babebbu.ev.ocpp.centralsystem;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {

    public static List<WebSocketSession> sessions = new ArrayList<>();

}
