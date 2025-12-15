package com.monframework;

import java.lang.reflect.Method;

public class RouteMapping {
    public final String httpMethod;
    public final String path;
    public final Method method;
    public final Object controller;

    public RouteMapping(String httpMethod, String path, Method method, Object controller) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.method = method;
        this.controller = controller;
    }

    public String getPath() {
        return this.path;
    }
    
    public String getHttpMethod() {
        return this.httpMethod;
    }
    
    public Method getMethod() {
        return this.method;
    }
}
