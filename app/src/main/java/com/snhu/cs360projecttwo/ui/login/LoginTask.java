package com.snhu.cs360projecttwo.ui.login;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.snhu.cs360projecttwo.R;
import com.snhu.cs360projecttwo.data.LoginRepository;
import com.snhu.cs360projecttwo.data.Result;
import com.snhu.cs360projecttwo.data.model.LoggedInUser;

public class LoginTask extends AsyncTask<String, Void, Result<LoggedInUser>> {

    LoginRepository mLoginRepository;
    MutableLiveData<LoginResult> mLoginResult;

    public LoginTask(LoginRepository loginRepository, MutableLiveData<LoginResult> loginResult) {
        super();
        mLoginRepository = loginRepository;
        mLoginResult = loginResult;
    }

    @Override
    protected Result<LoggedInUser> doInBackground(String... parameters) {
        if (parameters.length > 1) { // require two parameters
            return mLoginRepository.login(parameters[0], parameters[1]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result<LoggedInUser> result) {
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();

            mLoginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserId(), data.getDisplayName())));
        } else {
            mLoginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }
}
