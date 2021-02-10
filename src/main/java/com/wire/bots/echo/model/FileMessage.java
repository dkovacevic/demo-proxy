package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.NotNull;

public class FileMessage {
    @JsonProperty
    public String type = "attachment";
    @JsonProperty
    public Attachment attachment;

    public FileMessage(String filename, String data, String mimeType) {
        attachment = new Attachment();
        attachment.filename = filename;
        attachment.data = data;
        attachment.mimeType = mimeType;
    }

    public FileMessage() {
    }

    static class Attachment {
        @JsonProperty
        @NotNull
        public String data;

        @JsonProperty
        public String filename;

        @JsonProperty
        @NotNull
        public String mimeType;

        @JsonIgnore
        @ValidationMethod(message = "`data` is not a Base64 encoded string")
        public boolean isValidAttachment() {
            return data.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
        }

        @JsonIgnore
        @ValidationMethod(message = "Invalid `mimeType`")
        public boolean isValidMimeType() {
            return mimeType.contains("/");
        }
    }
}
