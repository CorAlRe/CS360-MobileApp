package com.snhu.cs360projecttwo.ui.login;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.snhu.cs360projecttwo.R;
import com.snhu.cs360projecttwo.data.LoginRepository;
import com.snhu.cs360projecttwo.data.Result;

public class RegisterTask extends AsyncTask<String, Void, Result<Long>> {
    LoginRepository mLoginRepository;
    MutableLiveData<LoginResult> mLoginResult;

    public RegisterTask(LoginRepository loginRepository, MutableLiveData<LoginResult> loginResult) {
        super();
        mLoginRepository = loginRepository;
        mLoginResult = loginResult;
    }

    @Override
    protected Result<Long> doInBackground(String... parameters) {
        if (parameters.length > 1) { // require two parameters
            return mLoginRepository.register(parameters[0], parameters[1]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result<Long> result) {
        if (result instanceof Result.Success) {
            mLoginResult.setValue(new LoginResult(R.string.register_success));
        } else {
            mLoginResult.setValue(new LoginResult(R.string.register_failed));
        }
    }
}
