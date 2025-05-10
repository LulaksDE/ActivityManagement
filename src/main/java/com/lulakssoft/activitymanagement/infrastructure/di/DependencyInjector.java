package com.lulakssoft.activitymanagement.infrastructure.di;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {
    private static final DependencyInjector INSTANCE = new DependencyInjector();

    private final Map<Class<?>, Object> services = new HashMap<>();

    private DependencyInjector() {}

    public static DependencyInjector getInstance() {
        return INSTANCE;
    }

    public <T> void register(Class<T> type, T implementation) {
        services.put(type, implementation);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) services.get(type);
    }

    public <T> T createInstance(Class<T> type) {
        try {
            Constructor<?>[] constructors = type.getConstructors();
            if (constructors.length == 0) {
                throw new IllegalArgumentException("No public constructor found for " + type.getName());
            }

            Constructor<?> constructor = constructors[0];
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = get(paramTypes[i]);
                if (params[i] == null) {
                    throw new IllegalArgumentException("No dependency registered for " + paramTypes[i].getName());
                }
            }

            return (T) constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + type.getName(), e);
        }
    }
}