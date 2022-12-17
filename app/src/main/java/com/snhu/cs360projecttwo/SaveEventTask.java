package com.snhu.cs360projecttwo;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.snhu.cs360projecttwo.data.EventRepository;
import com.snhu.cs360projecttwo.data.Result;
import com.snhu.cs360projecttwo.data.model.EventModel;

import java.util.ArrayList;

public class SaveEventTask extends AsyncTask<EventModel, Void, Result<EventModel>> {

    EventRepository mEventRepository;
    MutableLiveData<EventResult> mEventResult;

    public SaveEventTask(EventRepository eventRepository, MutableLiveData<EventResult> eventResult) {
        super();
        mEventRepository = eventRepository;
        mEventResult = eventResult;
    }

    @Override
    protected Result<EventModel> doInBackground(EventModel... events) {
        return mEventRepository.saveEvent(events[0]);
    }

    @Override
    protected void onPostExecute(Result<EventModel> result) {
        if (result instanceof Result.Success) {
            EventModel data = ((Result.Success<EventModel>) result).getData();
            mEventResult.setValue(new EventResult(data));
        } else {
            mEventResult.setValue(new EventResult(R.string.upcoming_events_fail));
        }
    }
}
