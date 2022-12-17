package com.snhu.cs360projecttwo;

import androidx.annotation.Nullable;

class LongResult {
    @Nullable
    private Long mResult;
    @Nullable
    private Integer mError;

    public LongResult(@Nullable Long result) {
        mResult = result;
    }
    public LongResult(@Nullable Integer error) {
        mError = error;
    }

    @Nullable
    public Integer getError() {
        return mError;
    }

    @Nullable
    public Long getResult() {
        return mResult;
    }
}
