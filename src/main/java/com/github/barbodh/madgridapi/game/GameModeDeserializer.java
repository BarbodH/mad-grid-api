package com.github.barbodh.madgridapi.game;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GameModeDeserializer extends JsonDeserializer<GameMode> {
    @Override
    public GameMode deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return GameMode.fromValue(parser.getIntValue());
    }
}
