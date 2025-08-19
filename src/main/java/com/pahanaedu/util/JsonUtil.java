package com.pahanaedu.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.dto.BillItemDTO;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new Gson();

    public static List<BillItemDTO> parseBillItems(String itemsJson) {
        if (itemsJson == null || itemsJson.isEmpty()) return java.util.Collections.emptyList();
        return gson.fromJson(itemsJson, new TypeToken<List<BillItemDTO>>(){}.getType());
    }
}

