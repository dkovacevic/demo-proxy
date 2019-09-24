package com.wire.bots.echo.resource;

import com.wire.bots.echo.model.MessageIn;
import com.wire.bots.echo.model.MessageOut;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo/webhook/v2")
@Produces(MediaType.APPLICATION_JSON)
public class WebhookV2 {
    private final String authentication;

    public WebhookV2(String authentication) {
        this.authentication = authentication;
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

        MessageOut messageOut = new MessageOut();

        switch (payload.type) {
            case "conversation.init": {
                messageOut.type = "text";
                messageOut.text = "Hi there!";
                return Response.
                        ok(messageOut).
                        build();
            }
            case "conversation.new_text": {
                messageOut.type = "text";
                messageOut.text = "You wrote: " + payload.text;
                return Response.
                        ok(messageOut).
                        build();
            }
            case "conversation.new_image": {
                messageOut.type = "image";
                messageOut.image = payload.image;
                return Response.
                        ok(messageOut).
                        build();
            }
            case "conversation.user_joined": {
                messageOut.type = "text";
                messageOut.text = "Hello!";
                return Response.
                        ok(messageOut).
                        build();
            }
        }

        return Response.
                ok().
                build();
    }
}

