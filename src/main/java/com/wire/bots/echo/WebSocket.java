package com.wire.bots.echo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wire.bots.echo.model.MessageIn;
import com.wire.bots.echo.model.MessageOut;
import com.wire.bots.echo.model.Text;
import com.wire.bots.echo.model.User;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

import javax.websocket.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint(decoders = WebSocket._Decoder.class)
public class WebSocket {
    private final WebTarget proxy;

    private WebSocket(Client jersey, String proxyUrl) {
        proxy = jersey.target(proxyUrl);
    }

    public static void main(String[] args) throws IOException, DeploymentException, InterruptedException {
        ClientConfig config = new ClientConfig()
                .register(JacksonJsonProvider.class);
        Client client = ClientBuilder
                .newBuilder()
                .withConfig(config)
                .build();

        final String wssUrl = "wss://proxy.services.wire.com";
        final String proxyUrl = "https://proxy.services.wire.com";
        final String appKey = args[0];

        URI wss = client
                .target(wssUrl)
                .path("await")
                .path(appKey)
                .getUri();

        ClientManager container = ClientManager.createClient();

        // Use this handler to automatically reconnect upon session.close() after 5sec
        container.getProperties().put(ClientProperties.RECONNECT_HANDLER, new ClientManager.ReconnectHandler() {
            @Override
            public boolean onDisconnect(CloseReason closeReason) {
                System.out.printf("Websocket onDisconnect: reason: %s\n", closeReason.getCloseCode());
                return true;
            }

            @Override
            public boolean onConnectFailure(Exception e) {
                System.out.printf("Websocket onConnectFailure: reason: %s\n", e);
                return false;
            }

            @Override
            public long getDelay() {
                return 5L;
            }
        });

        container.connectToServer(new WebSocket(client, proxyUrl), wss);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.printf("Websocket open: %s\n", session.getId());
    }

    @OnMessage
    public void onMessage(MessageIn payload) {
        System.out.printf("onMessage: `%s` bot: %s from: %s\n", payload.type, payload.botId, payload.userId);

        // fetch user profile so could extract the name. We dont need it but this way is more fun
        User user = proxy
                .path("users")
                .path(payload.userId.toString())
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + payload.token)
                .get(User.class);

        Response response = null;

        switch (payload.type) {
            case "conversation.init":
            case "conversation.user_joined": {

                // send the text into a conv.
                MessageOut message = new MessageOut();
                message.text = new Text();
                message.text.data = "Hey!";

                response = proxy
                        .path("conversation")
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + payload.token)
                        .post(Entity.entity(message, MediaType.APPLICATION_JSON));
            }
            break;
        }

        if (response != null) {
            if (response.getStatus() != 200) {
                System.out.printf("ERROR sending Message: bot: %s error: %s, code: %d\n",
                        payload.botId,
                        response.readEntity(String.class),
                        response.getStatus());
            } else {
                System.out.printf("Message sent: bot: %s\n", payload.botId);
            }
        }
    }

    @OnClose
    public void onClose(Session closed, CloseReason reason) {
        System.out.printf("Websocket closed: %s: reason: %s\n", closed.getId(), reason.getCloseCode());
    }

    public static class _Decoder implements Decoder.Text<MessageIn> {
        private final static ObjectMapper mapper = new ObjectMapper();

        @Override
        public MessageIn decode(String s) throws DecodeException {
            try {
                return mapper.readValue(s, MessageIn.class);
            } catch (IOException e) {
                throw new DecodeException(s, "oops", e);
            }
        }

        @Override
        public boolean willDecode(String s) {
            return s.startsWith("{") && s.endsWith("}");
        }

        @Override
        public void init(EndpointConfig config) {

        }

        @Override
        public void destroy() {

        }
    }
}
