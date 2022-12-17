package com.snhu.cs360projecttwo.data;

import androidx.annotation.NonNull;

import com.snhu.cs360projecttwo.data.model.EventModel;

import java.util.ArrayList;

public class EventRepository {
    private static volatile EventRepository instance;

    private EventDataSource dataSource;

    // private constructor : singleton access
    private EventRepository(EventDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // assumes EventDataSource is always cannot be initialized more than once
    public static EventRepository getInstance(EventDataSource dataSource) {
        if (instance == null) {
            instance = new EventRepository(dataSource);
        }
        return instance;
    }

    public Result<ArrayList<EventModel>> getUpcomingEvents(Long userId) {
        return dataSource.getUpcomingEvents(userId);
    }
    public Result<EventModel> saveEvent(@NonNull EventModel event) {
        if (!event.getIsDirty()) // translate to no-op, still successful (i.e no errors)
            return new Result.Success<EventModel>(event);;

        if (event.getId() < 0) {
            return dataSource.insertEvent(event);
        } else {
            return dataSource.updateEvent(event);
        }
    }
    public Result<Long> deleteEvent(@NonNull EventModel event) {
        if (!event.getIsNew()) {
            return dataSource.deleteEvent(event);
        } else { // translate to no-op, still successful (i.e no errors)
            return new Result.Success<Boolean>(Boolean.FALSE);
        }
     }
}
