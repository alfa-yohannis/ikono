
package org.example.util.observer;

import org.example.database.entity.Users;

public class UserUpdateListener implements EventListener<Users> {

    private String name;

    public UserUpdateListener(String name) {
        this.name = name;
    }

    @Override
    public void update(String eventType, Users user) {
        System.out.println("[" + name + "] Received event: " + eventType + " for User: " + user.getNama());
    }
}