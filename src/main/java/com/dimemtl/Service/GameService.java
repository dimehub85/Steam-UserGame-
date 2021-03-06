package com.dimemtl.Service;


import com.dimemtl.Model.Game;
import com.j256.ormlite.dao.Dao;

public class GameService extends AbstractService<Game, Integer> {
    public GameService(Dao<Game, Integer> dao) {
        super(dao);
    }
}
