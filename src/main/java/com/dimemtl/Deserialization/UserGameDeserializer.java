package com.dimemtl.Deserialization;

import com.dimemtl.Model.Game;
import com.dimemtl.Model.User;
import com.dimemtl.Model.UserGame;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;

public class UserGameDeserializer extends StdDeserializer<UserGame> {

    Dao<Game, Integer> gameDao;
    Dao<User, Integer> userDao;

    public UserGameDeserializer(){
        super(UserGame.class);
    }

    @Override
    public UserGame deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        int id = root.get("id").asInt();
        int userId = root.get("userId").asInt();
        int gameId = root.get("game").asInt();

        try {
            return new UserGame(id, userDao.queryForId(userId), gameDao.queryForId(gameId));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }
}
