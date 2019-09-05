package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
}
