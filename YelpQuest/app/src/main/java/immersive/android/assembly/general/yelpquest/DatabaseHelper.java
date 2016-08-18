package immersive.android.assembly.general.yelpquest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Skywingz on 8/17/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "immersive.android.assembly.general.yelpquest.database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CURRENT_MARKERS = "table_current_markers";
    private static final String TABLE_DETAIL_OBJECTS = "table_detail_objects";
    private static final String TABLE_COMPLETED_QUEST_MARKERS = "table_completed_quest_markers";
    private static final String TABLE_QUEST_OBJECTS = "table_quest_objects";
    private static final String COL_ID = "_id";

    // Columns for TABLE_CURRENT_MARKERS and TABLE_COMPLETED_QUEST_MARKERS
    private static final String COL_MARKER_TAG = "column_marker_tag";
    private static final String COL_LATITUDE = "column_latitude";
    private static final String COL_LONGITUDE = "column_longitude";
    private static final String COL_BUSINESS_NAME = "column_business_name";
    private static final String COL_BUSINESS_ADDRESS = "column_business_address";
    private static final String COL_ON_QUEST_BOOLEAN = "column_on_quest_boolean";
    private static final String COL_MARKER_STATUS = "column_marker_status";
    private static final String COL_UNLOCK_TIME = "column_unlock_time";

    // Columns for TABLE_DETAIL_OBJECTS
    // TODO

    // Columns for TABLE_QUEST_OBJECTS
    private static final String COL_START_TIME = "column_start_time";
    private static final String COL_END_TIME = "column_end_time";
    private static final String COL_NOTIFICATION_MESSAGE = "column_notification_message";
    private static final String COL_QUERY_INTEREST = "column_query_interest";
    private static final String COL_INCLUDE_FOOD_BOOLEAN = "column_include_food_boolean";
    private static final String COL_FOOD_TYPE = "column_food_type";


    // Table SQL Exec Strings
    private static final String CREATE_TABLE_CURRENT_MARKERS =
            "CREATE TABLE " + TABLE_CURRENT_MARKERS + "("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_MARKER_TAG + " TEXT, "
                    + COL_LATITUDE + " REAL, "
                    + COL_LONGITUDE + " REAL, "
                    + COL_BUSINESS_NAME + " TEXT, "
                    + COL_BUSINESS_ADDRESS + " TEXT, "
                    + COL_ON_QUEST_BOOLEAN + " TEXT, "
                    + COL_MARKER_STATUS + " INTEGER, "
                    + COL_UNLOCK_TIME + " TEXT)";

    private static final String CREATE_TABLE_COMPLETED_QUEST_MARKERS =
            "CREATE TABLE " + TABLE_COMPLETED_QUEST_MARKERS + "("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_LATITUDE + " REAL, "
                    + COL_LONGITUDE + " REAL, "
                    + COL_BUSINESS_NAME + " TEXT, "
                    + COL_BUSINESS_ADDRESS + " TEXT)";

    private static final String CREATE_TABLE_QUEST_OBJECTS =
            "CREATE TABLE " + TABLE_QUEST_OBJECTS + "("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_START_TIME + " INTEGER, "
                    + COL_END_TIME + " INTEGER, "
                    + COL_NOTIFICATION_MESSAGE + " TEXT, "
                    + COL_QUERY_INTEREST + " TEXT, "
                    + COL_INCLUDE_FOOD_BOOLEAN + " TEXT, "
                    + COL_FOOD_TYPE + " TEXT)";

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CURRENT_MARKERS);
        database.execSQL(CREATE_TABLE_COMPLETED_QUEST_MARKERS);
        database.execSQL(CREATE_TABLE_QUEST_OBJECTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_MARKERS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLETED_QUEST_MARKERS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST_OBJECTS);
        onCreate(database);
    }

    public HashMap<String, MarkerObject> getAllCurrentMarkers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CURRENT_MARKERS, null, null, null, null, null, null, null);
        HashMap<String, MarkerObject> map = new HashMap<>();

        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                map.put(cursor.getString(cursor.getColumnIndex(COL_MARKER_TAG)),
                        new MarkerObject(cursor.getString(cursor.getColumnIndex(COL_MARKER_TAG)),
                                cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE)),
                                cursor.getString(cursor.getColumnIndex(COL_BUSINESS_NAME)), cursor.getString(cursor.getColumnIndex(COL_BUSINESS_ADDRESS)),
                                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COL_ON_QUEST_BOOLEAN))),
                                cursor.getInt(cursor.getColumnIndex(COL_MARKER_STATUS)), cursor.getString(cursor.getColumnIndex(COL_UNLOCK_TIME))));

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return map;
    }

    public ArrayList<CompletedQuestMarkerObjects> getAllCompletedQuestMarkerObjects() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPLETED_QUEST_MARKERS, null, null, null, null, null, null, null);
        ArrayList<CompletedQuestMarkerObjects> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                list.add(new CompletedQuestMarkerObjects(cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(COL_BUSINESS_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_BUSINESS_ADDRESS))));

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return list;
    }

    public QuestObject getCurrentQuestObject() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUEST_OBJECTS, null, null, null, null, null, null, null);
        QuestObject quest = null;

        if (cursor.moveToFirst()) {
            quest = new QuestObject(cursor.getInt(cursor.getColumnIndex(COL_START_TIME)), cursor.getInt(cursor.getColumnIndex(COL_END_TIME)),
                    cursor.getString(cursor.getColumnIndex(COL_NOTIFICATION_MESSAGE)), cursor.getString(cursor.getColumnIndex(COL_QUERY_INTEREST)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COL_INCLUDE_FOOD_BOOLEAN))),
                    cursor.getString(cursor.getColumnIndex(COL_FOOD_TYPE)));
        }

        cursor.close();
        db.close();
        return quest;
    }

    public void addCurrentMarkers(HashMap<String, MarkerObject> map) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Set<String> keys = map.keySet();

        for (String key : keys) {
            values.put(COL_MARKER_TAG, key);
            values.put(COL_LATITUDE, map.get(key).getLatitude());
            values.put(COL_LONGITUDE, map.get(key).getLongitude());
            values.put(COL_BUSINESS_NAME, map.get(key).getBusinessName());
            values.put(COL_BUSINESS_ADDRESS, map.get(key).getBusinessAddress());
            values.put(COL_ON_QUEST_BOOLEAN, Boolean.toString(map.get(key).isOnQuest()));
            values.put(COL_MARKER_STATUS, map.get(key).getMarkerStatus());
            values.put(COL_UNLOCK_TIME, map.get(key).getUnlockTime());
            values.clear();
        }

        db.close();
    }

    public void addCompletedQuestMarkerObjects(CompletedQuestMarkerObjects completed) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LATITUDE, completed.getLatitude());
        values.put(COL_LONGITUDE, completed.getLongitude());
        values.put(COL_BUSINESS_NAME, completed.getBusinessName());
        values.put(COL_BUSINESS_ADDRESS, completed.getBusinessAddress());
        db.close();
    }

    public void addQuestObject(QuestObject quest) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_START_TIME, quest.getStartTime());
        values.put(COL_END_TIME, quest.getEndTime());
        values.put(COL_NOTIFICATION_MESSAGE, quest.getNotificationMessage());
        values.put(COL_QUERY_INTEREST, quest.getQueryInterest());
        values.put(COL_INCLUDE_FOOD_BOOLEAN, Boolean.toString(quest.isIncludeFood()));
        values.put(COL_FOOD_TYPE, quest.getFoodType());
        db.close();
    }

    public void cancelQuest() {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_CURRENT_MARKERS, null, null);
        db.delete(TABLE_DETAIL_OBJECTS, null, null);
        db.delete(TABLE_QUEST_OBJECTS, null, null);

        db.close();
    }




}
