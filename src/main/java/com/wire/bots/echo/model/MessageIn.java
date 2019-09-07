package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageIn {
    @NotNull
    public UUID botId;

    @NotNull
    public UUID userId;

    @NotNull
    public String type;

    public String token;

    public String text;
    public String image;

    @ValidationMethod(message = "`text` cannot be empty")
    @JsonIgnore
    public boolean isValidText() {
        if (!type.equals("conversation.new_text"))
            return true;
        return text != null && !text.isEmpty();
    }

    @ValidationMethod(message = "`image` is not a Base64 encoded string")
    @JsonIgnore
    public boolean isValidImage() {
        if (!type.equals("conversation.new_image"))
            return true;
        return image != null && image.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
    }
}
