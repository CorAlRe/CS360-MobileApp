package com.snhu.cs360projecttwo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.snhu.cs360projecttwo.data.EventRepository;
import com.snhu.cs360projecttwo.data.model.EventModel;

public class EventListViewModel extends ViewModel {
    EventRepository eventRepository;
    MutableLiveData<EventsResult> fetchEventsResult = new MutableLiveData<>();
    MutableLiveData<EventResult> updateResult = new MutableLiveData<>();
    MutableLiveData<LongResult> deleteResult = new MutableLiveData<>();

    public EventListViewModel(EventRepository eventRepository) {
        super();
        this.eventRepository = eventRepository;
    }

    public void getUpcomingEvents(Long userId) {
        UpcomingEventsTask task = new UpcomingEventsTask(eventRepository, fetchEventsResult);
        task.execute(userId);
    }

    public void saveEvent(EventModel event) {
        SaveEventTask task = new SaveEventTask(eventRepository, updateResult);
        task.execute(event);
    }

    public void deleteEvent(EventModel event) {
        DeleteEventTask task = new DeleteEventTask(eventRepository, deleteResult);
        task.execute(event);
    }

    public LiveData<EventsResult> getFetchEventsResult() {
        return fetchEventsResult;
    }

    public LiveData<EventResult> geUpdateEventResult() {
        return updateResult;
    }

    public LiveData<LongResult> getDeleteEventResult() {
        return deleteResult;
    }
}
