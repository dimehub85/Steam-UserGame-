package com.dimemtl.Service;


import com.dimemtl.Model.User;
import com.j256.ormlite.dao.Dao;

public class UserService extends AbstractService<User, Integer> {
    public UserService(Dao<User, Integer> dao) {
        super(dao);
    }
}
