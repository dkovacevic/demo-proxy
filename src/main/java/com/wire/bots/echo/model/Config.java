package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config extends Configuration {
    @NotNull
    @JsonProperty("service_authentication")
    public String authentication;
}
