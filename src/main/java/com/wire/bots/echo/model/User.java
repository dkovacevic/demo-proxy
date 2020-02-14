package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty
    public UUID id;

    @JsonProperty
    public String name;

    @JsonProperty("accent_id")
    public int accent;

    @JsonProperty
    public String handle;

}
