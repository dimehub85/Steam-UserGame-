package com.dimemtl.Service;
import com.dimemtl.Exception.ApplicationException;
import com.dimemtl.Model.Model;
import com.j256.ormlite.dao.Dao;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;


public abstract class AbstractService<T extends Model<U>, U> implements Service<T, U> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    private Dao<T, U> dao;

    public AbstractService(Dao<T, U> dao) {
        this.dao = dao;
    }

    public Dao<T, U> getDao() {
        return dao;
    }

    public void setDao(Dao<T, U> dao) {
        this.dao = dao;
    }

    @Override
    public List<T> findAll() {
        try {
            List<T> result =  dao.queryForAll();
            LOGGER.debug("Found result={} by querying all records", result);
            return result;
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public T findById(U id) {
        try {
            T result = dao.queryForId(id);
            LOGGER.debug("Found record={} by id={}", result, id);
            return result;
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public void save(T model) {
        try {
            dao.create(model);
            LOGGER.debug("Created record={}", model);
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public void update(T model) {
        try {
            dao.update(model);
            LOGGER.debug("Updated record={}", model);
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public void delete(T model) {
        try {
            dao.delete(model);
            LOGGER.debug("Deleted record={}", model);
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public void deleteById(U id) {
        try {
            dao.deleteById(id);
            LOGGER.debug("Deleted record by id={}", id);
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public<V> List<T> findByColumn(String columnName, V columnValue) {
        try {
            List<T> result = dao.queryBuilder().where().eq(columnName, columnValue).query();
            LOGGER.debug("Found results {} by searching column={} equals value={}", result, columnName, columnValue);
            return result;
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }

    @Override
    public<V> T findByColumnUnique(String columnName, V columnValue) {
        try {
            T result = dao.queryBuilder().where().eq(columnName, columnValue).queryForFirst();
            LOGGER.debug("Found unique result {} by searching column={} equals value={}", result, columnName, columnValue);
            return result;
        } catch (SQLException throwables) {
            String stackTrace = ExceptionUtils.getStackTrace(throwables);
            LOGGER.error(stackTrace);
            throw new ApplicationException(throwables);
        }
    }
}