package com.snhu.cs360projecttwo.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.snhu.cs360projecttwo.data.LoginRepository;
import com.snhu.cs360projecttwo.R;
import com.snhu.cs360projecttwo.data.Result;
import com.snhu.cs360projecttwo.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        super();
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
// async
        LoginTask loginTask = new LoginTask(loginRepository, loginResult);
        loginTask.execute(username, password);
// sync
/*
        Result<LoggedInUser> result = loginRepository.login(username, password);
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
*/
    }

    public void register(String username, String password) {
// async
        RegisterTask registerTask = new RegisterTask(loginRepository, loginResult);
        registerTask.execute(username, password);

// sync
/*
        Result<Long> result = loginRepository.register(username, password);
        if (result instanceof Result.Success) {
            loginResult.setValue(new LoginResult(R.string.register_success));
        } else {
            loginResult.setValue(new LoginResult(R.string.register_failed));
        }
*/

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}