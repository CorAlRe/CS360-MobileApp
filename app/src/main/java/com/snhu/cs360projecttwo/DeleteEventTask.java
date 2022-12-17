package com.snhu.cs360projecttwo;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.snhu.cs360projecttwo.data.EventRepository;
import com.snhu.cs360projecttwo.data.Result;
import com.snhu.cs360projecttwo.data.model.EventModel;

public class DeleteEventTask extends AsyncTask<EventModel, Void, Result<Long>> {

    EventRepository mEventRepository;
    MutableLiveData<LongResult> mEventResult;
    EventModel mEvent;

    public DeleteEventTask(EventRepository eventRepository, MutableLiveData<LongResult> eventResult) {
        super();
        mEventRepository = eventRepository;
        mEventResult = eventResult;
    }

    @Override
    protected Result<Long> doInBackground(EventModel... events) {
        return mEventRepository.deleteEvent(events[0]);
    }

    @Override
    protected void onPostExecute(Result<Long> result) {
        if (result instanceof Result.Success) {
            Long data = ((Result.Success<Long>) result).getData();
            mEventResult.setValue(new LongResult(data));
        } else {
            mEventResult.setValue(new LongResult(R.string.delete_error));
        }
    }
}
