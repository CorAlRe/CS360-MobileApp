package com.snhu.cs360projecttwo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.snhu.cs360projecttwo.data.EventDataSource;
import com.snhu.cs360projecttwo.data.EventRepository;
import com.snhu.cs360projecttwo.data.LoginRepository;
import com.snhu.cs360projecttwo.data.model.EventModel;
import com.snhu.cs360projecttwo.ui.login.LoginActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "com.snhu.cs360projecttwo";
    private final static String KEY_EVENT_ID = "EventId";
    private final static String KEY_EVENT_USER_ID = "UserId";
    private final static String KEY_EVENT_NAME = "EventName";
    private final static String KEY_EVENT_DATE = "EventDateTime";
    private final static String KEY_EVENT_LOC = "EventLoc";
    private final static String KEY_EVENT_ICON = "EventIcon";

    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_ID = "UserId";

    private String mUserName;
    private Long mUserId;
    private ActivityResultLauncher<Intent> activityLauncher;
    private EventListViewModel eventListViewModel;
    private static long newEventId = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventAdapter eventAdapter = new EventAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView eventRV = findViewById(R.id.eventRecycler);
        eventRV.setLayoutManager(linearLayoutManager);

        eventListViewModel = new ViewModelProvider(this, new EventListViewModelFactory(getApplicationContext()))
                .get(EventListViewModel.class);

        eventListViewModel.getFetchEventsResult().observe(this, new Observer<EventsResult>() {
            @Override
            public void onChanged(EventsResult eventsResult) {
                if (eventsResult == null) {
                    return;
                }
                if (eventsResult.getError() != null) {
                    showError(eventsResult.getError());
                    return;
                }
                if (eventsResult.getEvents() != null) {
                    eventAdapter.setData(eventsResult.getEvents());
                    eventRV.setAdapter(eventAdapter);
                }
            }
        });

        eventListViewModel.geUpdateEventResult().observe(this, new Observer<EventResult>() {
            @Override
            public void onChanged(EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }
                if (eventResult.getError() != null) {
                    showError(eventResult.getError());
                    return;
                }
                if (eventResult.getEvent() != null) {
                    eventAdapter.replaceInsertItem(eventResult.getEvent());
                    eventRV.setAdapter(eventAdapter);
                }
            }
        });

        eventListViewModel.getDeleteEventResult().observe(this, new Observer<LongResult>() {
            @Override
            public void onChanged(LongResult longResult) {
                if (longResult == null) {
                    return;
                }
                if (longResult.getError() != null) {
                    showError(longResult.getError());
                    return;
                }
                if (longResult.getResult() != null) {
                    eventAdapter.deleteItem(longResult.getResult());
                    eventRV.setAdapter(eventAdapter);
                }
            }
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK
                        && result.getData() != null) {

                    Intent intent = result.getData();
                    EventModel model = new EventModel();

                    model.setId(intent.getLongExtra(KEY_EVENT_ID, -1L));
                    model.setUserId(intent.getLongExtra(KEY_USER_ID, -1L));
                    long epoch = intent.getLongExtra(KEY_EVENT_DATE, -1L);

                    if (epoch != -1L) {
                        model.setDate(new Date(epoch));
                    }

                    model.setLocation(intent.getStringExtra(KEY_EVENT_LOC));
                    model.setName(intent.getStringExtra(KEY_EVENT_NAME));

                    eventListViewModel.saveEvent(model);
                } else if (result.getResultCode() == RESULT_CANCELED
                        && result.getData() != null) {

                    getData();
//                    eventListViewModel.getUpcomingEvents(mUserId);

                }
            }
        });

        // resume state
        if (savedInstanceState != null) {

            onRestoreInstanceState(savedInstanceState);

        } else { // get state from activity that called us (login activity)

            Intent intent = getIntent();
            if (intent != null) {
                mUserName = intent.getStringExtra(KEY_USER_NAME);
                mUserId = intent.getLongExtra(KEY_USER_ID, -1L);
            } else { // default values
                mUserName = "";
                mUserId = -1L;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void showError(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                // Add selected
                EventModel newEvent = new EventModel();
                newEvent.setId(--newEventId);
                newEvent.setUserId(mUserId);

                Intent intent = getIntentFromModel(newEvent);

                activityLauncher.launch(intent);
                return true;

            case R.id.action_enable_notifications:
                // notification selected
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("UserName", mUserName);
        savedInstanceState.putLong("UserId", mUserId);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mUserName = savedInstanceState.getString(KEY_USER_NAME);
            mUserId = savedInstanceState.getLong(KEY_USER_ID, -1L);
            getData();
        }
    }

    @Override
    public void onClick(View view) {
        EventModel model = (EventModel) view.getTag();

        Intent intent = getIntentFromModel(model);

        activityLauncher.launch(intent);
    }

    private void getData() {
        if (mUserId > -1L) {
            eventListViewModel.getUpcomingEvents(mUserId);
        }
        else {
            Log.d(TAG, String.format("UserId: %d", mUserId));
        }

        //
//        // fake data
//        ArrayList<EventModel> eventList = new ArrayList<EventModel>();
//        eventList.add(new EventModel(-1L, mUserId, "Fake Event 1", "Auditorium"));
//        eventList.add(new EventModel(-2L, mUserId, "Fake Event 2", "Conf Room A"));
//        eventList.add(new EventModel(-3L, mUserId, "Fake Event 3"));
//        eventList.add(new EventModel(-4L, mUserId, "Fake Event 4"));
//        eventList.add(new EventModel(-5L, mUserId, "Fake Event 5", "https://zoom.com"));
//        eventList.add(new EventModel(-6L, mUserId, "Fake Event 6"));
//        eventList.add(new EventModel(-7L, mUserId, "Fake Event 7", "123 Main St."));
//        eventList.add(new EventModel(-8L, mUserId, "Fake Event 8", "Smokey Bandit"));
//        eventList.add(new EventModel(-9L, mUserId, "Fake Event 9"));
//        eventList.add(new EventModel(-10L, mUserId, "Fake Event 10", "888-555-1234"));
//        eventList.add(new EventModel(-11L, mUserId, "Fake Event 11"));
//
//        return eventList;
    }

    private Intent getIntentFromModel(EventModel event) {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra( KEY_EVENT_ID, event.getId() );
        intent.putExtra( KEY_EVENT_USER_ID, event.getUserId() );
        intent.putExtra( KEY_EVENT_NAME, event.getName() );
        Date dateTime = event.getDate();
        if (dateTime != null)
            intent.putExtra( KEY_EVENT_DATE, dateTime.getTime() ); // milliseconds since 1970
        intent.putExtra( KEY_EVENT_LOC, event.getLocation() );
        return intent;
    }
}