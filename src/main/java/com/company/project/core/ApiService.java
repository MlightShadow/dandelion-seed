package com.company.project.core;

import java.util.Map;

public interface ApiService {
    <T> T doPost(String url, Object params, Class<T> clazz);
    <T> T doGet(String url, Map<String, String> params, Class<T> clazz);
}