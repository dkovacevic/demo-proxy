package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config extends Configuration {
    @NotNull
    @JsonProperty("service_token")
    public String serviceToken;

    @NotNull
    @JsonProperty("app_key")
    public String appKey;

    @NotNull
    @JsonProperty("roman_url")
    public String romanUrl;

    @JsonProperty("sft_url")
    public String sftUrl;

    @JsonProperty("sft_password")
    public String sftPassword;
}
