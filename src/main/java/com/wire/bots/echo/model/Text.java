package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class Text {
    @JsonProperty
    @NotNull
    @Length(min = 1, max = 64000)
    public String data;
}
