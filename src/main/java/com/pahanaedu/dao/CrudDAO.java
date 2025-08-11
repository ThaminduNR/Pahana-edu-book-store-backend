package com.pahanaedu.dao;

import java.util.List;

public interface CrudDAO <T, ID> {

    List<T> findAll() throws Exception;
    T findById(ID id) throws Exception;
    int create(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(ID id) throws Exception;
}

