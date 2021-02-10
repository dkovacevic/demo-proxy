package com.wire.bots.echo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.UUID;

public class TextMessage {

    @JsonProperty
    public String type = "text";
    @JsonProperty
    public Text text;

    public TextMessage(String content) {
        this.text = new Text();
        this.text.data = content;
    }

    public TextMessage() {
    }

    static class Text {
        @JsonProperty
        @NotNull
        @Length(min = 1, max = 64000)
        public String data;

        @JsonProperty
        public ArrayList<Mention> mentions;
    }

    static class Mention {
        @NotNull
        public UUID userId;
        @NotNull
        @Min(0)
        public Integer offset;
        @NotNull
        @Min(2)
        public Integer length;
    }
}
