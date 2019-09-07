package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageOut {
    @JsonProperty
    public String type;
    @JsonProperty
    public String text;
    @JsonProperty
    public String image;
}
