package com.dimemtl.Controllers;
import com.dimemtl.Model.Game;
import com.dimemtl.Model.User;
import com.dimemtl.Model.UserGame;
import com.dimemtl.Model.UserRole;
import com.dimemtl.Service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;

public class UserGameController extends AuthorizedController<UserGame, Integer> {
    private final Service<User, Integer> userService;
    private final Service<Game, Integer> gameService;

    public UserGameController(Service<UserGame, Integer> service, ObjectMapper objectMapper, Service<User, Integer> userService, Service<Game, Integer> gameService) {
        super(service, objectMapper, UserGame.class);
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public Service<User, Integer> userService() {
        return userService;
    }

    @Override
    public void postOne(Context context) {
        if (isAuthorized(context)) {
            UserGame userGame = extractUserGame(context);
            getService().save(userGame);
        } else {
            throw new ForbiddenResponse();
        }
    }


    @Override
    boolean isAuthorized(User user, Context context) {
        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }
        if (context.method().equals("POST")) {
            User actor = actor(context);
            int gameId = context.pathParam("gameId", Integer.class).get();
            int userId = context.pathParam("userId", Integer.class).get();
            Game game = gameService.findById(gameId);
            User owner = userService.findById(userId);
            return actor.equals(owner);
        } else {
            int id = context.pathParam("id", Integer.class).get();
            UserGame userGame = getService().findById(id);
            return actor(context).equals(userGame.getUser());
        }
    }

    private Game extractGame(Context context) {
        int gameId = context.pathParam("gameId", Integer.class).get();
        return gameService.findById(gameId);
    }

    private User extractUser(Context context) {
        int userId = context.pathParam("userId", Integer.class).get();
        return userService.findById(userId);
    }

    private UserGame extractUserGame(Context context) {
        Game game = extractGame(context);
        User user = extractUser(context);
        return new UserGame(0, user, game);
    }
}
