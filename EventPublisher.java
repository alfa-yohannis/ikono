package org.example.util.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPublisher {
    private Map<String, List<EventListener<?>>> listeners = new HashMap<>();

    public void subscribe(String eventType, EventListener<?> listener) {
        List<EventListener<?>> typeListeners = listeners.getOrDefault(eventType, new ArrayList<>());
        typeListeners.add(listener);
        listeners.put(eventType, typeListeners);
    }

    public void unsubscribe(String eventType, EventListener<?> listener) {
        List<EventListener<?>> typeListeners = listeners.get(eventType);
        if (typeListeners != null) {
            typeListeners.remove(listener);
        }
    }

    public <T> void publish(String eventType, T data) {
        List<EventListener<?>> typeListeners = listeners.get(eventType);
        if (typeListeners != null) {
            for (EventListener<?> listener : typeListeners) {
                ((EventListener<T>) listener).update(eventType, data);
            }
        }
    }
}