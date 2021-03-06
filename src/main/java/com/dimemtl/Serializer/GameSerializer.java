package com.dimemtl.Serializer;

import com.dimemtl.Model.Game;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GameSerializer extends StdSerializer<Game> {

    public GameSerializer(){
        super(Game.class);
    }

    @Override
    public void serialize(Game game, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeEndObject();
        jsonGenerator.writeNumberField("id", game.getId());
        jsonGenerator.writeStringField("title", game.getTitle());
        jsonGenerator.writeStringField("description", game.getDescription());
        jsonGenerator.writeEndObject();
    }
}
