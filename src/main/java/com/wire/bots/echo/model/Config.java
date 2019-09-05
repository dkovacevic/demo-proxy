package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Config extends Configuration {
    @NotNull
    @JsonProperty("proxy_url")
    public String proxyUrl;

    @JsonProperty("service_authentication")
    public String authentication;

    @JsonProperty("app_key")
    public String appKey;

    @JsonProperty("websocket_url")
    public String websocketUrl;

}
