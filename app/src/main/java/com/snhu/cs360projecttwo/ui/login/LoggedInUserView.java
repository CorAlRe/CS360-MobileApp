package com.snhu.cs360projecttwo.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    private Long userId;

    //... other data fields that may be accessible to the UI

    public LoggedInUserView(Long userId, String displayName) {
        this.displayName = displayName;
        this.userId = userId;
    }

    String getDisplayName() {
        return displayName;
    }

    Long getUserId() { return userId; }
}