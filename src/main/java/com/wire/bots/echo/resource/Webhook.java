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
                message.text.data = "Hi there!";
                return Response.
                        ok(message).
                        build();
            }
            case "conversation.new_text": {
//                TextMessage msg = new TextMessage("You wrote: " + payload.text);
//                return Response.
//                        ok(msg).
//                        build();
                break;
            }
            case "conversation.user_joined": {
                MessageOut message = new MessageOut();
                message.text = new Text();
                message.text.data = "Hey!";

                return Response.
                        ok(message).
                        build();
            }
            case "conversation.ping": {
                MessageOut message = new MessageOut();
                message.type = "call";
                message.call = new Call();
                message.call.response = false;
                message.call.secret = config.sftPassword;
                message.call.sessionId = "";
                message.call.sftUrl = config.sftUrl;
                message.call.type = "CONFSTART";
                message.call.clientId = payload.conversationId.toString();

                final Response post = httpClient.target(config.romanUrl)
                        .path("api")
                        .path("broadcast")
                        .request(MediaType.APPLICATION_JSON)
                        .header("app-key", config.appKey)
                        .post(Entity.entity(message, MediaType.APPLICATION_JSON));

                System.out.printf("Broadcast confstart. confId: %s status: %s\n", message.call.clientId, post.getStatus());

                return Response.
                        ok().
                        build();
            }
        }

        return Response.
                ok().
                build();
    }
}

