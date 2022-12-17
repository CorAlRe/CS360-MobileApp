package com.snhu.cs360projecttwo;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.snhu.cs360projecttwo.data.EventRepository;
import com.snhu.cs360projecttwo.data.Result;
import com.snhu.cs360projecttwo.data.model.EventModel;

import java.util.ArrayList;

public class UpcomingEventsTask extends AsyncTask<Long, Void, Result<ArrayList<EventModel>>> {

    EventRepository mEventRepository;
    MutableLiveData<EventsResult> mEventsResult;

    public UpcomingEventsTask(EventRepository eventRepository, MutableLiveData<EventsResult> eventResult) {
        super();
        mEventRepository = eventRepository;
        mEventsResult = eventResult;
    }

    @Override
    protected Result<ArrayList<EventModel>> doInBackground(Long... userIds) {
        if (userIds.length > 0) {
            return mEventRepository.getUpcomingEvents(userIds[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result<ArrayList<EventModel>> result) {
        if (result instanceof Result.Success) {
            ArrayList<EventModel> data = ((Result.Success<ArrayList<EventModel>>) result).getData();
            mEventsResult.setValue(new EventsResult(data));
        } else {
            mEventsResult.setValue(new EventsResult(R.string.upcoming_events_fail));
        }
    }
}
