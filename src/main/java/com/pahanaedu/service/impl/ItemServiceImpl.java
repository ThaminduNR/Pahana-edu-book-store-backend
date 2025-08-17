package com.pahanaedu.service.impl;

import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.dao.impl.ItemDaoImpl;
import com.pahanaedu.dto.ItemDto;
import com.pahanaedu.exception.NotFoundException;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;
import com.pahanaedu.service.ItemService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    ItemDAO itemDAO = new ItemDaoImpl();

    @Override
    public ItemDto findByCode(String code) throws Exception {
        Item byCode = itemDAO.findByCode(code);
        if (byCode == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(byCode.getId());
        dto.setCode(byCode.getCode());
        dto.setName(byCode.getName());
        dto.setDescription(byCode.getDescription());
        dto.setUnitPrice(byCode.getUnitPrice());
        dto.setQty(byCode.getQty());
        return dto;

    }

    @Override
    public List<ItemDto> findAll() throws Exception {
        List<Item> itemList = itemDAO.findAll();
        List<ItemDto> dtoList = new ArrayList<>();

        for (Item item : itemList) {
            ItemDto dto = new ItemDto(
                    item.getId(),
                    item.getCode(),
                    item.getName(),
                    item.getDescription(),
                    item.getUnitPrice(),
                    item.getQty()
            );
            dtoList.add(dto);
        }

        return dtoList;

    }

    @Override
    public ItemDto findById(Integer id) throws Exception {
        Item byId = itemDAO.findById(id);

        if (byId == null) throw new NotFoundException("Item not found");

        ItemDto dto = new ItemDto();
        dto.setId(byId.getId());
        dto.setCode(byId.getCode());
        dto.setName(byId.getName());
        dto.setDescription(byId.getDescription());
        dto.setUnitPrice(byId.getUnitPrice());
        dto.setQty(byId.getQty());
        return dto;
    }

    @Override
    public int create(ItemDto entity) throws Exception {
        Item newItem = new Item();
        newItem.setCode(entity.getCode());
        newItem.setName(entity.getName());
        newItem.setDescription(entity.getDescription());
        newItem.setUnitPrice(entity.getUnitPrice());
        newItem.setQty(entity.getQty());

        return itemDAO.create(newItem);
    }

    @Override
    public boolean update(ItemDto dto, Integer id) throws Exception {
        Item item = itemDAO.findById(id);
        if (item == null) throw new NotFoundException("Item not found");

        if (dto.getCode() != null) item.setCode(dto.getCode());
        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getUnitPrice() != null) item.setUnitPrice(dto.getUnitPrice());
        if (dto.getQty() != 0) item.setQty(dto.getQty());

        return itemDAO.update(item);
    }


    @Override
    public boolean delete(Integer id) throws Exception {
        boolean ok = itemDAO.delete(id);
        if (!ok) throw new NotFoundException("Item not found");
        return true;
    }
}
