package com.snhu.cs360projecttwo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.metrics.Event;

import com.snhu.cs360projecttwo.data.model.EventModel;
import com.snhu.cs360projecttwo.data.model.LoggedInUser;

import java.lang.reflect.Array;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;

public class EventDatabase extends SQLiteOpenHelper {

    private static volatile EventDatabase sEventDatabase;
    private static final String DATABASE_NAME = "events.db";
    private static final int VERSION = 3;

    private static final class EventTable {
        private static final String TABLE = "events";
        private static final String ID = "_id";
        private static final String USER_ID = "user_id";
        private static final String NAME = "name";
        private static final String DATE = "datetime";
        private static final String LOCATION = "location";
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String ID = "_id";
        private static final String USER = "username";
        private static final String PASS = "password";
        private static final String DATE_CREATED = "date_created";
        private static final String DATE_LAST_LOGGED_IN = "date_logged_in";
    }

    private EventDatabase (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static EventDatabase getInstance(Context context) {
        if (sEventDatabase == null) {
            sEventDatabase = new EventDatabase(context);
        }
        return sEventDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + EventTable.TABLE + " (" +
                EventTable.ID + " integer primary key autoincrement, " +
                EventTable.USER_ID + " integer, " +
                EventTable.NAME + " text, " +
                EventTable.DATE + " integer, " +
                EventTable.LOCATION + " text)");

        sqLiteDatabase.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.ID + " integer primary key autoincrement, " +
                UserTable.USER + " text, " +
                UserTable.PASS + " text, " +
                UserTable.DATE_CREATED + " integer," +
                UserTable.DATE_LAST_LOGGED_IN + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            sqLiteDatabase.execSQL("drop table " + UserTable.TABLE);

            sqLiteDatabase.execSQL("create table " + UserTable.TABLE + " (" +
                    UserTable.ID + " integer primary key autoincrement, " +
                    UserTable.USER + " text, " +
                    UserTable.PASS + " text, " +
                    UserTable.DATE_CREATED + " integer," +
                    UserTable.DATE_LAST_LOGGED_IN + " integer)");
        }
        else if (newVersion == 3) {
            sqLiteDatabase.execSQL("drop table " + EventTable.TABLE);

            sqLiteDatabase.execSQL("create table " + EventTable.TABLE + " (" +
                    EventTable.ID + " integer primary key autoincrement, " +
                    EventTable.USER_ID + " integer, " +
                    EventTable.NAME + " text, " +
                    EventTable.DATE + " integer, " +
                    EventTable.LOCATION + " text)");
        }
    }

    // returns LoggedInUser on success, otherwise null
    public LoggedInUser login(String userName, String password) {
        // lookup user
        SQLiteDatabase db = getWritableDatabase(); // writable because we update time on successful login

        String sql = "select " + UserTable.ID +
                " from " + UserTable.TABLE +
                " where " + UserTable.USER + " = ? AND " + UserTable.PASS + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[] { userName, password });

        // determine success
        boolean loggedIn = cursor.moveToFirst(); // if a record exists then we found a match for user and password
        long id = -1;
        LoggedInUser result = null;
        if (loggedIn) {
            id = cursor.getLong(0);
            result = new LoggedInUser(id, userName);
        }
        cursor.close();

        // update last logged in
        if (loggedIn) {
            Instant instant = Instant.now(); // UTC

            ContentValues values = new ContentValues();
            values.put(UserTable.DATE_LAST_LOGGED_IN, instant.toEpochMilli());

            db.update(UserTable.TABLE, values, UserTable.ID + " = ? ", new String[] { Long.toString(id) });
        }

        // return success/fail
        return result;
    }

    // returns the user id on create, if -1 registration failed.
    public long registerUser(String userName, String password) {
        SQLiteDatabase db = getWritableDatabase();

        // determine if there is already a record for this
        String sql = "select " + UserTable.ID +
                " from " + UserTable.TABLE +
                " where " + UserTable.USER + " = ? AND " + UserTable.PASS + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[] { userName, password });
        if (cursor.moveToFirst()) { // if something was returned then record already exists
            return -1;
        }

        // add new user
        ContentValues values = new ContentValues();
        values.put(UserTable.USER, userName);
        values.put(UserTable.PASS, password);

        long id = db.insert(UserTable.TABLE, null, values);

        return id;
    }

    public ArrayList<EventModel> getUpcomingEvents(long userId) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select " + EventTable.ID
                + ", " + EventTable.USER_ID
                + ", " + EventTable.NAME
                + ", " + EventTable.LOCATION
                + ", date(" + EventTable.DATE + ", 'utc')"
                + " from " + EventTable.TABLE
                + " where " + EventTable.USER_ID + " = ?";
//" + EventTable.DATE + " >= date('now') AND " +
        Cursor cursor = db.rawQuery(sql, new String[] { Long.toString(userId) } );
        ArrayList<EventModel> result = new ArrayList<EventModel>();

        while(cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            Long user = cursor.getLong(1);
            String name = cursor.getString(2);
            long epochMilliseconds = cursor.getLong(3);
            Instant date = Instant.ofEpochMilli(epochMilliseconds);
            String loc = cursor.getString(4);

            EventModel model = new EventModel(id, user, name, Date.from(date), loc);
            result.add(model);
        }

        return result;
    }

    public EventModel insertEvent(EventModel event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.USER_ID, event.getUserId());
        Instant dateInstant = event.getDate().toInstant();
        values.put(EventTable.DATE, dateInstant.toEpochMilli());
        values.put(EventTable.LOCATION, event.getLocation());
        values.put(EventTable.NAME, event.getName());

        long id = db.insert(EventTable.TABLE, null, values);
        event.setId(id);
        event.clearDirty();

        return event;
    }

    // assigning events to other users isn't possible
    // changing id's is not possible
    public EventModel updateEvent(EventModel event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        Instant dateInstant = event.getDate().toInstant();
        values.put(EventTable.DATE, dateInstant.toEpochMilli());
        values.put(EventTable.LOCATION, event.getLocation());
        values.put(EventTable.NAME, event.getName());

        int resultCount = db.update(EventTable.TABLE, values, EventTable.ID + " = ?", new String[] { event.getId().toString() } );
        if (resultCount > 0) {
            event.clearDirty();
            return event;
        }
        else
            return null;
    }

    public Long deleteEvent(EventModel event) {
        SQLiteDatabase db = getWritableDatabase();

        int resultCount = db.delete(EventTable.TABLE, EventTable.ID + " = ?", new String[] { event.getId().toString() } );

        return event.getId();
    }
}












