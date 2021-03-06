package com.dimemtl.Service;


import com.dimemtl.Model.UserGame;
import com.j256.ormlite.dao.Dao;

public class UserGameService extends AbstractService<UserGame, Integer> {
    public UserGameService(Dao<UserGame, Integer> dao) {
        super(dao);
    }
}
