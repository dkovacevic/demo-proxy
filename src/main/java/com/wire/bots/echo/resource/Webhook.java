package com.wire.bots.echo.resource;

import com.wire.bots.echo.model.MessageIn;
import com.wire.bots.echo.model.MessageOut;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo/webhook")
@Produces(MediaType.APPLICATION_JSON)
public class Webhook {
    private final WebTarget proxy;
    private final String authentication;

    public Webhook(Client client, String proxyUrl, String authentication) {
        this.authentication = authentication;
        this.proxy = client.target(proxyUrl);
    }

    @GET
    public Response status() {
        return Response.
                ok().
                build();
    }

    @POST
    public Response post(@NotNull @HeaderParam("Authorization") String authentication,
                         @Valid MessageIn payload) {

        if (this.authentication != null && !this.authentication.equals(authentication)) {
            System.out.printf("Webhook: Invalid authentication `%s`\n", authentication);
            return Response.
                    status(401).
                    build();
        }

        System.out.printf("Webhook: bot: %s `%s` from: %s\n", payload.botId, payload.type, payload.userId);

        Response response = null;
        MessageOut messageOut = new MessageOut();

        switch (payload.type) {
            case "conversation.init": {
                messageOut.type = "text";
                messageOut.text = "Hi there!";
                response = proxy
                        .path("conversation")
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + payload.token)
                        .post(Entity.entity(messageOut, MediaType.APPLICATION_JSON));
            }
            break;
            case "conversation.new_text": {
                messageOut.type = "text";
                messageOut.text = "You wrote: " + payload.text;
                response = proxy
                        .path("conversation")
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + payload.token)
                        .post(Entity.entity(messageOut, MediaType.APPLICATION_JSON));
            }
            break;
            case "conversation.new_image": {
                messageOut.type = "image";
                messageOut.image = payload.image;
                response = proxy
                        .path("conversation")
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + payload.token)
                        .post(Entity.entity(messageOut, MediaType.APPLICATION_JSON));
            }
            break;
            case "conversation.user_joined": {
                messageOut.type = "text";
                messageOut.text = "Hello!";
                response = proxy
                        .path("conversation")
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + payload.token)
                        .post(Entity.entity(messageOut, MediaType.APPLICATION_JSON));
            }
            break;
        }

        if (response != null && response.getStatus() != 200) {
            System.out.printf("Webhook: %s %d\n", response.readEntity(String.class), response.getStatus());
        }

        return Response.
                ok().
                build();
    }
}

