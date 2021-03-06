package com.dimemtl.Deserialization;

import com.dimemtl.Model.Game;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class GameDeserializer extends StdDeserializer<Game> {


    public GameDeserializer(){
        super(Game.class);
    }
    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        int id = root.get("id").asInt();
        String title = root.get("title").asText();
        String description = root.get("description").asText();

        return new Game(id, title, description);
    }


}
