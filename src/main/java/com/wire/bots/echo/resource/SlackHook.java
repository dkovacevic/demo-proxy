package com.wire.bots.echo.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo/slack/webhook")
@Produces(MediaType.APPLICATION_JSON)
public class SlackHook {
    private final String authentication;

    public SlackHook(String authentication) {
        this.authentication = authentication;
    }

    @POST
    public Response post(@Valid Payload payload) {


        return Response.
                ok().
                build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Payload {

        @JsonProperty("token")
        private String token;

        @JsonProperty("callback_id")
        private String callbackId;

        @JsonProperty("type")
        private String type;

        @JsonProperty("trigger_id")
        private String triggerId;

        @JsonProperty("response_url")
        private String responseUrl;

        @JsonProperty("message")
        private Message message;

        @JsonProperty("action_ts")
        private String actionTs;

        @JsonProperty("message_ts")
        private String messageTs;

        @JsonProperty("attachment_id")
        private String attachmentId;

    }

    static class Message {
        @JsonProperty("type")
        public String type;

        @JsonProperty("user")
        public String user;

        @JsonProperty("ts")
        public String ts;

        @JsonProperty("text")
        public String text;
    }

    public static class SlackResponse {
        @JsonProperty("username")
        public String username;

        @JsonProperty("icon_emoji")
        public String iconEmoji;

        @JsonProperty("channel")
        public String channel;

        @JsonProperty("text")
        public String text;

        @JsonProperty("response_type")
        public String responseType;

        @JsonProperty("type")
        public String type;

        @JsonProperty("sub_type")
        public String subType;

        @JsonProperty("ts")
        public String ts;

        @JsonProperty("thread_ts")
        public String threadTs;

        @JsonProperty("replace_original")
        public Boolean replaceOriginal;

        @JsonProperty("delete_original")
        public Boolean deleteOriginal;
    }
}

