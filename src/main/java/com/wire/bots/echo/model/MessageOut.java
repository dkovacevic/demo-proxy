package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageOut {
    @NotNull
    @OneOf(value = {"text", "attachment", "poll", "call"})
    @JsonProperty
    public String type;

    @JsonProperty
    public Text text;

    @JsonProperty
    public Call call;
}
