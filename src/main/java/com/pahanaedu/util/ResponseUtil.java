package com.pahanaedu.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    private static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static <T> void send(HttpServletResponse resp, String msg, int status, T data, boolean success) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(gson.toJson(new ApiResponse<>(msg, status, data, success)));
    }

    public static void error(HttpServletResponse resp, Exception e) throws IOException {
        send(resp, "Server error: " + e.getMessage(), 500, null, false);
    }
}
