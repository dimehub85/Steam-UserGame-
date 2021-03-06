package com.dimemtl;


import com.dimemtl.Configuration.DatabaseConfiguration;
import com.dimemtl.Configuration.JdbcDatabaseConfiguration;
import com.dimemtl.Controllers.Controller;
import com.dimemtl.Controllers.GameController;
import com.dimemtl.Controllers.UserController;
import com.dimemtl.Controllers.UserGameController;
import com.dimemtl.Deserialization.GameDeserializer;
import com.dimemtl.Deserialization.UserDeserializer;
import com.dimemtl.Deserialization.UserGameDeserializer;
import com.dimemtl.Model.Game;
import com.dimemtl.Model.User;
import com.dimemtl.Model.UserGame;
import com.dimemtl.Model.UserRole;
import com.dimemtl.Service.GameService;
import com.dimemtl.Service.Service;
import com.dimemtl.Service.UserGameService;
import com.dimemtl.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.j256.ormlite.dao.DaoManager;


import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.core.security.SecurityUtil.roles;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static Role getRole(Context context, Service<User, Integer> userService) {
        if (context.basicAuthCredentialsExist()) {
            String login = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            User actor = userService.findByColumnUnique("login", login);
            LOGGER.debug("found user={}", actor);
            if (BCrypt.checkpw(password, actor.getPassword())) {
                return actor.getRole();
            } else {
                throw new UnauthorizedResponse();
            }
        } else {
            throw new UnauthorizedResponse();
        }
    }
    public static void main(String[] args) throws SQLException {
        //LOGGER.info(BCrypt.hashpw("qwerty", BCrypt.gensalt()));  //Dimemtl
        //LOGGER.info(BCrypt.hashpw("All", BCrypt.gensalt()));     //Rassvet
        //LOGGER.info(BCrypt.hashpw("Pizza1337", BCrypt.gensalt())); //Aldik007
        //LOGGER.info(BCrypt.hashpw("Holied7328", BCrypt.gensalt())); //Amina1

        DatabaseConfiguration databaseConfiguration = new JdbcDatabaseConfiguration("jdbc:sqlite:C:\\Users\\nurzh\\OneDrive\\Рабочий стол\\Library.db");
        Service<User, Integer> userService = new UserService(DaoManager.createDao(databaseConfiguration.connectionSource(), User.class));
        Service<Game, Integer> gameService = new GameService(DaoManager.createDao(databaseConfiguration.connectionSource(), Game.class));
        Service<UserGame, Integer> userGameService = new UserGameService(DaoManager.createDao(databaseConfiguration.connectionSource(), UserGame.class));

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(
                        new SimpleModule()
                                .addDeserializer(User.class, new UserDeserializer())
                                .addDeserializer(Game.class, new GameDeserializer())
                                .addDeserializer(UserGame.class, new UserGameDeserializer())
                );

        Controller<User, Integer> userController = new UserController(userService, objectMapper);
        Controller<Game, Integer> gameController = new GameController(gameService, objectMapper, userService);
        Controller<UserGame, Integer> userGameController = new UserGameController(userGameService, objectMapper, userService, gameService);

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
            javalinConfig.prefer405over404 = true;
            javalinConfig.enableDevLogging();
            javalinConfig.accessManager((handler, context, set) -> {
                Role userRole = getRole(context, userService);
                if (set.contains(userRole)) {
                    handler.handle(context);
                } else {
                    throw new ForbiddenResponse();
                }
            });
        });

        app.routes(() -> {
            path("users", () -> {
                get(userController::getAll, roles(UserRole.ADMIN));
                post(userController::postOne);
                path(":id", () -> {
                    get((ctx) -> userController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    patch((ctx) -> userController.patchOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    delete((ctx) -> userController.deleteOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                });
            });

            path("games", () -> {
                get(gameController::getAll);
                post(gameController::postOne);
                path(":id", () -> {
                    get((ctx) -> gameController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    patch((ctx) -> gameController.patchOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    delete((ctx) -> gameController.deleteOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                });
            });

            path("library", () -> {
                get(userGameController::getAll);
                path(":userId/:gameId", () -> post(userGameController::postOne));
                path(":id", () -> {
                    get((ctx) -> userGameController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    patch((ctx) -> userGameController.patchOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                    delete((ctx) -> userGameController.deleteOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.COMMON, UserRole.ADMIN));
                });
            });
        });
        app.start(7000);
    }
}
