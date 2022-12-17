package com.snhu.cs360projecttwo.data.model;

import android.graphics.drawable.Drawable;

import java.time.Instant;
import java.util.Date;

public class EventModel {

    private final static long HOUR_MILLISECONDS = 3600000L;

    private Long mEventId;
    private Long mUserId;
    private String mEventName;
    private Date mEventDateTime;
    private String mEventLocation;
    private Drawable mIcon;  // TODO create a custom drawable based on First Letter of event name

    // changing the Id does not make dirty, only user editable fields can
    private boolean isDirty;
    private boolean isNew;

    public EventModel() {
        this(-1L, -1L, "", Date.from(Instant.now()), "");
    }
    public EventModel(String eventName) {
        this(-1L, -1L, eventName, Date.from(Instant.now()), "");
    }
    public EventModel(String eventName, String location) {
        this(-1L, -1L, eventName, new Date(Instant.now().toEpochMilli() + HOUR_MILLISECONDS), location);
    }
    public EventModel(String eventName, Date eventDateTime) {
        this(-1L, -1L, eventName, eventDateTime, "");
    }
    public EventModel(String eventName, Date eventDateTime, String eventLocation) {
        this(-1L, -1L, eventName, eventDateTime, eventLocation);
    }
    public EventModel(Long eventId, Long userId, String eventName) {
        this(eventId, userId, eventName, new Date(Instant.now().toEpochMilli() + HOUR_MILLISECONDS), "");
    }
    public EventModel(Long eventId, Long userId, String eventName, String eventLocation) {
        this(eventId, userId, eventName, new Date(Instant.now().toEpochMilli() + HOUR_MILLISECONDS), eventLocation);
    }
    public EventModel(Long eventId, Long userId, String eventName, Date eventDateTime, String eventLocation) {
        mEventId = eventId;
        mEventDateTime = eventDateTime;
        mEventLocation = eventLocation == null ? "" : eventLocation;
        mEventName = eventName == null ? "" : eventName;
        mUserId = userId;
    }

    public Long getId() {
        return mEventId;
    }

    // app settable, not user settable, does not make dirty
    public void setId(Long id) {
        mEventId = id;
    }

    public String getName() {
        return mEventName;
    }
    public void setName(String name) {
        if (mEventName.compareTo(name) != 0) {
            mEventName = name;
            isDirty = true;
        }
    }

    public Date getDate() {
        return mEventDateTime;
    }
    public void setDate(Date eventDateTime) {
        if (mEventDateTime.compareTo(eventDateTime) != 0) {
            mEventDateTime = eventDateTime;
            isDirty = true;
        }
    }

    public String getLocation() {
        return mEventLocation;
    }
    public void setLocation(String location) {
        if (mEventLocation.compareTo(location) != 0) {
            mEventLocation = location;
            isDirty = true;
        }
    }

    public Long getUserId() { return mUserId; }

    // app settable, not directly editable by user, change doesn't make dirty
    public void setUserId(Long userId) { mUserId = userId; }

    public boolean getIsDirty() {
        return isDirty;
    }
    public void clearDirty() {
        isDirty = false;
    }

    // an event is considered new (created/not yet saved) if it has a negative id value
    public boolean getIsNew() {
        return this.mEventId < 0;
    }
}
