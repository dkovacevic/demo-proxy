package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageOut {
    @NotNull
    @JsonProperty
    @OneOf(value = {"text", "image"})
    public String type;

    @JsonProperty
    public String text;

    @JsonProperty
    public String image;
}
