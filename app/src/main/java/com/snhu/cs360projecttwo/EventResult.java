package com.snhu.cs360projecttwo;

import androidx.annotation.Nullable;

import com.snhu.cs360projecttwo.data.model.EventModel;

import java.util.ArrayList;

class EventResult {
    @Nullable
    private EventModel mEvent;
    @Nullable
    private Integer mError;

    public EventResult(@Nullable EventModel event) {
        mEvent = event;
    }
    public EventResult(@Nullable Integer error) {
        mError = error;
    }

    @Nullable
    public Integer getError() {
        return mError;
    }

    @Nullable
    public EventModel getEvent() {
        return mEvent;
    }
}
