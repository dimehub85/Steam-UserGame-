package com.dimemtl.Controllers;
import com.dimemtl.Model.Game;
import com.dimemtl.Model.User;
import com.dimemtl.Model.UserRole;
import com.dimemtl.Service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;

public class GameController extends AuthorizedController<Game, Integer> {
    private final Service<User, Integer> userService;

    public GameController(Service<Game, Integer> service, ObjectMapper objectMapper, Service<User, Integer> userService) {
        super(service, objectMapper, Game.class);
        this.userService = userService;
    }


    @Override
    public Service<User, Integer> userService() {
        return userService;
    }

    @Override
    public boolean isAuthorized(User user, Context context) {
        if (context.method().equals("GET")) {
            return true;
        } else {
            return user.getRole() == UserRole.ADMIN;
        }
    }



}