
package org.example.util.observer;

public interface EventListener<T> {
    void update(String eventType, T data);
}