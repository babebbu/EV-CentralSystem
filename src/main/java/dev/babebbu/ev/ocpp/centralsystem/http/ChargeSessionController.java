package dev.babebbu.ev.ocpp.centralsystem.http;

import dev.babebbu.ev.ocpp.centralsystem.models.enums.MessageType;
import dev.babebbu.ev.ocpp.centralsystem.models.schemas.RemoteStartTransactionRequest;
import dev.babebbu.ev.ocpp.centralsystem.utils.ObjectMapperFactory;
import dev.babebbu.ev.ocpp.centralsystem.InMemoryDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;

@RestController
public class ChargeSessionController {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     *  --------------                --------------
     *  |   Client   | <=[Call]====== |   Server   |
     *  --------------                --------------
     *  Server is accepting Call from client and Call must be handle
     */
    @GetMapping("/remote-start-transaction")
    public String remoteStartTransaction() {
        // Get Some Session.
        // In this case (for initial testing purpose), We gonna send remote start to every Charge Point..
        // And we do it in parallel.
        InMemoryDatabase.sessions.parallelStream().forEach(session -> {
            try {
                // Generate Request Payload
                RemoteStartTransactionRequest payload = new RemoteStartTransactionRequest();

                // Wrap with CALL message
                // (╬＾д＾)凸 I DON'T UNDERSTAND WHY OCPP USE ARRAY AS IN-FLIGHT MESSAGE INSTEAD OF OBJECT.
                List<Object> message = List.of(
                    MessageType.CALL,
                    "UniqueID",
                    "RemoteStartTransaction",
                    payload
                );
                String callMessage = ObjectMapperFactory.getObjectMapper().writeValueAsString(message);

                // Send Payload
                session.sendMessage(new TextMessage(callMessage));
            } catch (IOException e) {
                LOG.error("IOException");
            }
        });
        return "Running";
    }

}
