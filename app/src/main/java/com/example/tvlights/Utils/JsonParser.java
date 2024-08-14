package com.example.tvlights.Utils;

import com.google.gson.Gson;

public class JsonParser {

    public String toJsonFormat(String data)
    {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
