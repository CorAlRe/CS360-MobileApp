package com.snhu.cs360projecttwo;

import androidx.annotation.Nullable;

import com.snhu.cs360projecttwo.data.model.EventModel;

class BooleanResult {
    @Nullable
    private Boolean mResult;
    @Nullable
    private Integer mError;

    public BooleanResult(@Nullable Boolean result) {
        mResult = result;
    }
    public BooleanResult(@Nullable Integer error) {
        mError = error;
    }

    @Nullable
    public Integer getError() {
        return mError;
    }

    @Nullable
    public Boolean getResult() {
        return mResult;
    }
}
