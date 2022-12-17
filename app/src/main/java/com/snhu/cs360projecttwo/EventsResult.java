package com.snhu.cs360projecttwo;

import androidx.annotation.Nullable;

import com.snhu.cs360projecttwo.data.model.EventModel;

import java.util.ArrayList;

class EventsResult {
    @Nullable
    private ArrayList<EventModel> mEvents;
    @Nullable
    private Integer mError;

    public EventsResult(@Nullable ArrayList<EventModel> events) {
        mEvents = events;
    }
    public EventsResult(@Nullable Integer error) {
        mError = error;
    }

    @Nullable
    public Integer getError() {
        return mError;
    }

    @Nullable
    public ArrayList<EventModel> getEvents() {
        return mEvents;
    }
}
