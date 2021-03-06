package com.dimemtl.Serializer;

import com.dimemtl.Model.UserGame;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserGameSerializer extends StdSerializer<UserGame> {

    public UserGameSerializer(){
        super(UserGame.class);
    }

    @Override
    public void serialize(UserGame userGame, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", userGame.getId());
        jsonGenerator.writeNumberField("user", userGame.getUser().getId());
        jsonGenerator.writeNumberField("game", userGame.getGame().getId());
        jsonGenerator.writeEndObject();
    }
}
