package com.epam.alextuleninov.taxiservice.controller.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class Request extends HttpServletRequestWrapper {

    private final Map<String, Object> attributes = new HashMap<>();
    private final HttpSession session = new Session();
    private String encoding;

    public Request(HttpServletRequest request) {
        super(request);
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return session;
    }

    @Override
    public String getCharacterEncoding() {
        return encoding;
    }

    @Override
    public void setCharacterEncoding(String enc) {
        encoding = enc;
    }
}
