package dev.babebbu.ev.ocpp.centralsystem.websocket;

import dev.babebbu.ev.ocpp.centralsystem.InMemoryDatabase;
import dev.babebbu.ev.ocpp.centralsystem.models.enums.MessageType;
import dev.babebbu.ev.ocpp.centralsystem.models.schemas.*;
import dev.babebbu.ev.ocpp.centralsystem.utils.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;

@Component
public class OcppWebSocketHandler extends TextWebSocketHandler {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("Connected ... " + session.getId());
        InMemoryDatabase.sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        LOG.info("message received from {}: {}", session.getId(), jsonTextMessage.getPayload());

        final int MESSAGE_TYPE = 0;
        Object[] message = ObjectMapperFactory.getObjectMapper().readValue(jsonTextMessage.getPayload(), Object[].class);

        switch ((int) message[MESSAGE_TYPE]) {
            case MessageType.CALL -> handleCall(session, message);
            case MessageType.CALL_RESULT -> handleCallResult(session, message);
            case MessageType.CALL_ERROR -> handleCallError(session, message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOG.error("error occured at sender " + session, throwable);
    }

    /**
     *  --------------                     --------------
     *  |   Client   | ==========[Call]==> |   Server   |
     *  --------------                     --------------
     *  Server is accepting Call from client and Call must be handle
     */
    public void handleCall(WebSocketSession session, Object[] call) throws IOException {
        final int ACTION = 2;
        final int PAYLOAD = 3;

        String action = call[ACTION].toString();
        Object payload = call[PAYLOAD];

        if (payload instanceof BootNotificationRequest request) {
            session.sendMessage(
                new TextMessage(
                    ObjectMapperFactory.getObjectMapper().writeValueAsString(
                        BootNotificationConfirm.builder()
                            .status("Accepted")
                            .currentTime(new Date().toString())
                            .heartBeatInterval(300)
                            .build()
                    )
                )
            );
        } else if (payload instanceof AuthorizeRequest request) {
            session.sendMessage(new TextMessage(""));
        }
    }
    
    /**        
     *  --------------                     --------------
     *  |   Client   | <==[Call]========== |   Server   |
     *  --------------                     --------------
     *  --------------                     --------------
     *  |   Client   | ====[CallResult]==> |   Server   |
     *  --------------                     --------------
     *  Server is accepting Call from client and Call must be handle  
     */                                                              
    public void handleCallResult(WebSocketSession session, Object[] callResult) throws IOException {
        final int PAYLOAD = 2;
        Object payload = callResult[PAYLOAD];

        if (payload instanceof RemoteStartTransactionConfirm confirm) {
            session.sendMessage(new TextMessage(confirm.toString()));
        } else if (payload instanceof RemoteStopTransactionConfirm confirm) {
            session.sendMessage(new TextMessage(confirm.toString()));
        }

    }

    /**
     *  --------------                     --------------
     *  |   Client   | <==[Call]========== |   Server   |
     *  --------------                     --------------
     *  --------------                     --------------
     *  |   Client   | =====[CallError]==> |   Server   |
     *  --------------                     --------------
     *  Server is accepting Call from client and Call must be handle
     */
    public void handleCallError(WebSocketSession session, Object[] callError) {

    }

}
