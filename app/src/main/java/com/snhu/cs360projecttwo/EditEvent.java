package com.snhu.cs360projecttwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class EditEvent extends AppCompatActivity {
    private final String KEY_EVENT_ID = "EventId";
    private final String KEY_EVENT_USER_ID = "UserId";
    private final String KEY_EVENT_NAME = "EventName";
    private final String KEY_EVENT_DATE = "EventDateTime";
    private final String KEY_EVENT_LOC = "EventLoc";
    private final String KEY_EVENT_ICON = "EventIcon";

    private Long mEventId;
    private Long mUserId;
    private String mName;
    private Date mDate;
    private String mLocation;
    private Drawable mIcon;

    private TextView mEditName;
    private TextView mEditDate;
    private TextView mEditTime;
    private TextView mEditLoc;
    private ImageButton mSaveButton;

    DateFormat mDateFormat;
    DateFormat mTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        mEditName = findViewById(R.id.editTextName);
        mEditDate = findViewById(R.id.editTextDate);
        mEditTime = findViewById(R.id.editTextTime);
        mEditLoc = findViewById(R.id.editTextLocation);
        mSaveButton = findViewById(R.id.imageButton);

        mDateFormat = android.text.format.DateFormat.getDateFormat(this.getApplicationContext());
        mTimeFormat = android.text.format.DateFormat.getTimeFormat(this.getApplicationContext());

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                onSaveInstanceState(bundle);
                Intent intent = new Intent();

                intent.putExtra(KEY_EVENT_ID, bundle.getLong(KEY_EVENT_ID, -1L));
                intent.putExtra(KEY_EVENT_USER_ID, bundle.getLong(KEY_EVENT_USER_ID, -1L));
                intent.putExtra(KEY_EVENT_NAME, bundle.getString(KEY_EVENT_NAME));
                intent.putExtra(KEY_EVENT_DATE, bundle.getLong(KEY_EVENT_DATE, -1L));
                intent.putExtra(KEY_EVENT_LOC, bundle.getString(KEY_EVENT_LOC));

                // expect to overwrite the default result
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if (savedInstanceState != null) {

            onRestoreInstanceState(savedInstanceState);

        } else { // get state from activity that called us

            Intent intent = getIntent();

            if (intent != null) {
                mEventId = intent.getLongExtra (KEY_EVENT_ID, -1L);
                mUserId = intent.getLongExtra(KEY_EVENT_USER_ID, -1L);

                // set the default result, we primarily want to return user id
                // this should be the result if the user clicks on the back button
                setResult(RESULT_CANCELED, intent);

                mEditName.setText(intent.getStringExtra(KEY_EVENT_NAME));
                mEditLoc.setText(intent.getStringExtra(KEY_EVENT_LOC));

                long epoch = intent.getLongExtra( KEY_EVENT_DATE, -1L );

                if (epoch != -1L) {
                    Date date = new Date(epoch);

                    try {
                        mEditDate.setText(mDateFormat.format(date));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        mEditTime.setText(mTimeFormat.format(date));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            else { // default values
                mEventId = -1L;
                mUserId = -1L;
            }

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong( KEY_EVENT_ID, mEventId );
        savedInstanceState.putLong( KEY_EVENT_USER_ID, mUserId);
        savedInstanceState.putString( KEY_EVENT_NAME, mEditName.getText().toString().trim());
        Date date = null;
        Date time = null;

        // TODO fix bug on edit between UTC and local time
        try {
            date = mDateFormat.parse(mEditDate.getText().toString().trim());

            try {
                time = mTimeFormat.parse(mEditTime.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // should have date and maybe time at this point
            if (date != null && time != null) {
                // combine date and time together as one value
                date = new Date(date.getTime() + time.getTime());
            }

            if (date != null) {
                savedInstanceState.putLong(KEY_EVENT_DATE, date.getTime()); // milliseconds since 1970
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        savedInstanceState.putString( KEY_EVENT_LOC, mEditLoc.getText().toString().trim() );
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            mEventId = savedInstanceState.getLong( KEY_EVENT_ID, -1L );
            mUserId = savedInstanceState.getLong( KEY_EVENT_USER_ID, -1L );
            mEditName.setText(savedInstanceState.getString( KEY_EVENT_NAME ));
            long epoch = savedInstanceState.getLong( KEY_EVENT_DATE, -1L );

            if (epoch != -1L) {
                Date date = new Date(epoch);

                try {
                    mEditDate.setText(mDateFormat.format(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    mEditTime.setText(mTimeFormat.format(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mEditLoc.setText(savedInstanceState.getString( KEY_EVENT_LOC ));

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Refactor
        Bundle bundle = new Bundle();
        onSaveInstanceState(bundle);
        Intent intent = new Intent();

        intent.putExtra(KEY_EVENT_ID, bundle.getLong(KEY_EVENT_ID, -1L));
        intent.putExtra(KEY_EVENT_USER_ID, bundle.getLong(KEY_EVENT_USER_ID, -1L));
        intent.putExtra(KEY_EVENT_NAME, bundle.getString(KEY_EVENT_NAME));
        intent.putExtra(KEY_EVENT_DATE, bundle.getLong(KEY_EVENT_DATE, -1L));
        intent.putExtra(KEY_EVENT_LOC, bundle.getString(KEY_EVENT_LOC));

        // expect to overwrite the default result
        setResult(RESULT_CANCELED, intent);

        super.onBackPressed();
    }
}