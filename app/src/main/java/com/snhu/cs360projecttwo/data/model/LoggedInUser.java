package com.snhu.cs360projecttwo.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private long userId;
    private String displayName;

    public LoggedInUser(long userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}