package com.snhu.cs360projecttwo.data;

import android.content.Context;

import com.snhu.cs360projecttwo.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private Context applicationContext;

    public LoginDataSource(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Result<LoggedInUser> login(String username, String password) {
        try {

            EventDatabase db = EventDatabase.getInstance(applicationContext);
            LoggedInUser user = db.login(username, password);
            if (user != null)
                return new Result.Success<LoggedInUser>(user);
            else
                return new Result.Error(new Exception("Error logging in"));

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<Long> register(String username, String password) {
        try {

            EventDatabase db = EventDatabase.getInstance(applicationContext);
            long userId = db.registerUser(username, password);
            if (userId != -1)
                return new Result.Success<Long>(userId);
            else
                return new Result.Error(new Exception("Error registering user"));

        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering user", e));
        }
    }

    public void logout() {
        // revoke authentication
        // intentionally blank
    }
}