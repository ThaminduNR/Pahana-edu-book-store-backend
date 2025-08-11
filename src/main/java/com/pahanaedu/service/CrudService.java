package com.pahanaedu.service;

import java.util.List;

public interface CrudService <T, ID>{
    List<T> findAll() throws Exception;
    T findById(ID id) throws Exception;
    int create(T entity) throws Exception;
    boolean update(T entity, ID id) throws Exception;
    boolean delete(ID id) throws Exception;
}
