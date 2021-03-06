package com.dimemtl.Configuration;

import com.dimemtl.Exception.ApplicationException;
import com.dimemtl.Model.Game;
import com.dimemtl.Model.User;
import com.dimemtl.Model.UserGame;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.sql.SQLException;

public class JdbcDatabaseConfiguration implements DatabaseConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDatabaseConfiguration.class);

    private final ConnectionSource connectionSource;


    public JdbcDatabaseConfiguration(String jdbcConnectionString) {
        try {
            connectionSource = new JdbcConnectionSource(jdbcConnectionString);
            LOGGER.debug(String.format("JdbcConnectionSource is build from {%s}", jdbcConnectionString));
            createTables();
        } catch (SQLException throwables) {
            LOGGER.error(ExceptionUtils.getStackTrace(throwables));
            throw new ApplicationException(String.format("Couldn't construct connection source from {%s}", jdbcConnectionString));
        }
    }

    private void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Game.class);
        TableUtils.createTableIfNotExists(connectionSource, UserGame.class);
    }

    @Override
    public ConnectionSource connectionSource() {
        return connectionSource;
    }

}
