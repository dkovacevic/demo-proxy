package com.wire.bots.echo.resource;

import com.wire.bots.echo.filters.WireAuthorization;
import com.wire.bots.echo.model.*;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/wire")
@Produces(MediaType.APPLICATION_JSON)
public class Webhook {
    private final Client httpClient;
    private final Config config;

    public Webhook(Client httpClient, Config config) {

        this.httpClient = httpClient;
        this.config = config;
    }

    @GET
    public Response status() {
        return Response.
                ok().
                build();
    }

    @POST
    @WireAuthorization
    public Response post(@Valid MessageIn payload) {
        System.out.printf("Webhook: bot: %s `%s` from: %s\n", payload.botId, payload.type, payload.userId);

        switch (payload.type) {
            case "conversation.init": {
                MessageOut message = new MessageOut();
                message.text = new Text();
                message.type = "text";
                message.text.data = "Hi there!";
                return Response.
                        ok(message).
                        build();
            }
            case "conversation.new_text": {
                MessageOut message = new MessageOut();
                message.type = "text";
                message.text = new Text();
                message.text.data = "You wrote: " + payload.text.data;
                return Response.
                        ok(message).
                        build();
            }
            case "conversation.user_joined": {
                MessageOut message = new MessageOut();
                message.type = "text";
                message.text = new Text();
                message.text.data = "Hey!";

                return Response.
                        ok(message).
                        build();
            }
            case "conversation.call": {
                final Call call = payload.call;
                System.out.printf("conversation.call: type: %s\n", call.type);

                if (call.confId == null) {
                    MessageOut message = new MessageOut();
                    message.type = "call";
                    message.call = call;
                    message.call.confId = payload.conversationId.toString();

                    broadcast(message);
                }
                break;
            }
        }

        return Response.
                ok().
                build();
    }

    private void broadcast(MessageOut message) {
        final Response post = httpClient.target(config.romanUrl)
                .path("api")
                .path("broadcast")
                .request(MediaType.APPLICATION_JSON)
                .header("app-key", config.appKey)
                .post(Entity.entity(message, MediaType.APPLICATION_JSON));

        System.out.printf("Broadcast %s. confId: %s status: %s\n",
                message.call.type,
                message.call.confId,
                post.getStatus());
    }
}

