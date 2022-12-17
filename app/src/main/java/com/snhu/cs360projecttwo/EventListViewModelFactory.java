package com.snhu.cs360projecttwo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snhu.cs360projecttwo.data.EventDataSource;
import com.snhu.cs360projecttwo.data.EventRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class EventListViewModelFactory implements ViewModelProvider.Factory {

    private Context applicationContext;

    public EventListViewModelFactory(Context applicationContext) {
        super();
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventListViewModel.class)) {
            return (T) new EventListViewModel(EventRepository.getInstance(new EventDataSource(applicationContext)));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}