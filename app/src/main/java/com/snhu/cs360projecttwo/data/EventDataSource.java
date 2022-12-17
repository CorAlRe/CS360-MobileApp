package com.snhu.cs360projecttwo.data;

import android.content.Context;

import com.snhu.cs360projecttwo.data.model.EventModel;

import java.io.IOException;
import java.util.ArrayList;

public class EventDataSource {

    private Context applicationContext;

    public EventDataSource(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Result<ArrayList<EventModel>> getUpcomingEvents(long userId) {
        try {
            EventDatabase db = EventDatabase.getInstance(applicationContext);
            ArrayList<EventModel> list = db.getUpcomingEvents(userId);

            if (list != null) {
                return new Result.Success<ArrayList<EventModel>>(list);
            }
            else {
                return new Result.Error(new Exception("Error getting upcoming events"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error getting upcoming events", e));
        }
    }
    public Result<EventModel> insertEvent(EventModel event) {
        EventDatabase db = EventDatabase.getInstance(applicationContext);
        EventModel returnValue = db.insertEvent(event);
        if (returnValue != null ) {
            return new Result.Success<EventModel>(returnValue);
        } else {
            return new Result.Error(new Exception("Error inserting event"));
        }
    }
    public Result<EventModel> updateEvent(EventModel event) {
        EventDatabase db = EventDatabase.getInstance(applicationContext);
        EventModel returnValue = db.updateEvent(event);
        if (returnValue != null ) {
            return new Result.Success<EventModel>(returnValue);
        } else {
            return new Result.Error(new Exception("Error updating event"));
        }
    }
    public Result<Long> deleteEvent(EventModel event) {
        EventDatabase db = EventDatabase.getInstance(applicationContext);
        Long returnValue = db.deleteEvent(event);
        if (returnValue != null) {
            return new Result.Success<Long>(returnValue);
        } else {
            return new Result.Error(new Exception("Error deleting event"));
        }
    }

}
