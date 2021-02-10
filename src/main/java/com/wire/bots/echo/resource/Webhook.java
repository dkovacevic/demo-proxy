package com.wire.bots.echo.resource;

import com.wire.bots.echo.filters.WireAuthorization;
import com.wire.bots.echo.model.FileMessage;
import com.wire.bots.echo.model.MessageIn;
import com.wire.bots.echo.model.TextMessage;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo/webhook")
@Produces(MediaType.APPLICATION_JSON)
public class Webhook {
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
                FileMessage msg = new FileMessage("cool.jpg", payload.image, payload.mimeType);
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

