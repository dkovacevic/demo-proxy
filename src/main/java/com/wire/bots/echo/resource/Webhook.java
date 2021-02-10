package com.wire.bots.echo.resource;

import com.wire.bots.echo.model.FileMessage;
import com.wire.bots.echo.model.MessageIn;
import com.wire.bots.echo.model.TextMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo/webhook")
@Produces(MediaType.APPLICATION_JSON)
public class Webhook {
    private final String authentication;

    public Webhook(String authentication) {
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

        switch (payload.type) {
            case "conversation.init": {
                TextMessage msg = new TextMessage("Hi there!");
                return Response.
                        ok(msg).
                        build();
            }
            case "conversation.new_text": {
                TextMessage msg = new TextMessage("You wrote: " + payload.text);
                return Response.
                        ok(msg).
                        build();
            }
            case "conversation.new_image": {
                FileMessage msg = new FileMessage("cool.jpg", payload.image, "image/jpeg");
                return Response.
                        ok(msg).
                        build();
            }
            case "conversation.user_joined": {
                TextMessage msg = new TextMessage("Hey!");
                return Response.
                        ok(msg).
                        build();
            }
        }

        return Response.
                ok().
                build();
    }
}

