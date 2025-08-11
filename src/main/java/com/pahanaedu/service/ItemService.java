package com.pahanaedu.service;

import com.pahanaedu.dto.ItemDto;

public interface ItemService extends CrudService<ItemDto,Integer>{

    ItemDto findByCode(String code) throws Exception;
}
