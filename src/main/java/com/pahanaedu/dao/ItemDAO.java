package com.pahanaedu.dao;

import com.pahanaedu.model.Item;

public interface ItemDAO extends CrudDAO<Item,Integer> {

    Item findByCode(String code) throws Exception;

}