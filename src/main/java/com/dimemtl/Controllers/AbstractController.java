package com.dimemtl.Controllers;



import com.dimemtl.Model.Model;
import com.dimemtl.Service.Service;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;

import java.util.List;

public abstract class AbstractController<T extends Model<U>, U> implements Controller<T, U>{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private Service<T, U> service;
    private ObjectMapper objectMapper;
    private final Class<T> modelClass;

    public AbstractController(Service<T, U> service, ObjectMapper objectMapper, Class<T> modelClass) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.modelClass = modelClass;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public Service<T, U> getService() {
        return service;
    }

    public void setService(Service<T, U> service) {
        this.service = service;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void getOne(Context context, U id) {
        Model<U> model = service.findById(id);
        try {
            context.result(objectMapper.writeValueAsString(model));
        } catch (JsonProcessingException e) {
            logAndRethrowInternalServerError(e);
        }
    }

    @Override
    public void getAll(Context context) {
        List<T> models = service.findAll();
        try {
            context.result(objectMapper.writeValueAsString(models));
        } catch (JsonProcessingException e) {
            logAndRethrowInternalServerError(e);
        }
    }

    @Override
    public void postOne(Context context) {
        try {
            T model = objectMapper.readValue(context.body(), modelClass);
            service.save(model);
            context.status(201);
        } catch (JsonProcessingException e) {
            logAndRethrowInternalServerError(e);
        }
    }

    @Override
    public void patchOne(Context context, U id) {
        try {
            T model = objectMapper.readValue(context.body(), modelClass);
            model.setId(id);
            service.update(model);
        } catch (JsonProcessingException e) {
            logAndRethrowInternalServerError(e);
        }
    }

    @Override
    public void deleteOne(Context context, U id) {
        service.deleteById(id);
        context.status(204);
    }

    private static void logAndRethrowInternalServerError(Exception e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));
        throw new InternalServerErrorResponse();
    }
}
