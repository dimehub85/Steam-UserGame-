package com.dimemtl.Deserialization;

import com.dimemtl.Model.User;
import com.dimemtl.Model.UserRole;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class UserDeserializer extends StdDeserializer<User> {
    public UserDeserializer() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        int id = root.get("id").asInt();
        String firstName = root.get("firstName").asText();
        String lastName = root.get("lastName").asText();
        String login = root.get("login").asText();
        String email = root.get("email").asText();
        String passwordPlain = root.get("password").asText();
        //String role  = root.get()
        if (!EmailValidator.getInstance().isValid(email))
            throw new IOException("Invalid Email");
        return new User(id, firstName, lastName, login, email, BCrypt.hashpw(passwordPlain, BCrypt.gensalt()), UserRole.COMMON);
    }
}