package comcse5324projutacatering.httpsgithub.utacatering;
//TODO 2x if issues with changing roles with the db layout...
import android.content.ContentValues;
import android.content.Context;
//import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DatabaseInterface extends SQLiteOpenHelper {

    private static DatabaseInterface instance;
    private Context mContext;

    private static final String TABLE_NAME_PROFILE      = "profile";
    private static final String TABLE_NAME_REGISTRATION = "registration_requests";
    public static final String COLUMN_NAME_USERNAME    = "username";
    public static final String COLUMN_NAME_PASSWORD    = "password";
    public static final String COLUMN_NAME_ROLE        = "role";
    public static final String COLUMN_NAME_UTAID       = "uta_id";
    public static final String COLUMN_NAME_PERSONAL    = "personal_details";
    public static final String COLUMN_NAME_CONTACT     = "contact_details";
    public static final String COLUMN_NAME_DATETIME    = "datetime";
    private ArrayList<String> resultsList;

    private static final String SQL_CREATE_PROFILE_TABLE =
            "CREATE TABLE " + TABLE_NAME_PROFILE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT UNIQUE,"    +
                    COLUMN_NAME_PASSWORD + " TEXT,"    +
                    COLUMN_NAME_ROLE     + " TEXT,"    +
                    COLUMN_NAME_UTAID    + " INTEGER," +
                    COLUMN_NAME_PERSONAL + " TEXT,"    +
                    COLUMN_NAME_CONTACT  + " TEXT)"   ;

    private static final String SQL_CREATE_REGISTRATION_TABLE =
            "CREATE TABLE " + TABLE_NAME_REGISTRATION + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT UNIQUE,"    +
                    COLUMN_NAME_PASSWORD + " TEXT,"    +
                    COLUMN_NAME_ROLE     + " TEXT,"    +
                    COLUMN_NAME_UTAID    + " INTEGER," +
                    COLUMN_NAME_PERSONAL + " TEXT,"    +
                    COLUMN_NAME_CONTACT  + " TEXT,"    +
                    COLUMN_NAME_DATETIME + " INTEGER)" ;
            //Made username unique, so that caterer's profileID isnt needed in some function.. can just use the username.


    private String SQL_CREATE_EVENT_TABLE ;
    private String SQL_EVENT_TABLE_NAME ;
    public String EVENT_REQ_USER_COL;
    public String EVENT_REQ_USER_ID_COL;
    public String EVENT_STIME_COL;
    public String EVENT_STIME_EPOCH_COL;
    public String EVENT_DUR_COL;
    public String EVENT_ETIME_COL;
    public String EVENT_HALL_COL;
    public String EVENT_VENUE_COL;
    public String EVENT_AC_ID_COL;
    public String EVENT_APPROVAL_COL;
    public String EVENT_ATT_COL;
    public String EVENT_ALC_COL;
    public String EVENT_FORM_COL;
    public String EVENT_MT_COL;
    public String EVENT_PRC_COL;
    public String EVENT_OCTYP_COL;
    public String EVENT_ENT_COL;
    public String SQL_CREATE_EVENT_CS_ASSIGN_TABLE_STRING;
    public String SQL_EVENT_TRIGGER_CALC_END_TIME;
    public String SQL_EVENT_TRIGGER_PROFILE_DELETION;
    public String SQL_EVENT_TRIGGER_EVENT_DELETION;
    public String EVENT_ID_COL;
    public String EVENT_CS_PROFILE_ID_COL;
    public String EVENT_CS_ASSIGN_TABLE_NAME;
    public String SQL_EVENT_TRIGGER_PROFILE_USERNAME_CHANGE;
    public String SQL_EVENT_TRIGGER_BECOMES_UNAPPROVED;

    private static final String SQL_DELETE_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_PROFILE;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    private DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        SQL_CREATE_EVENT_TABLE = mContext.getString(R.string.SQL_CREATE_EVENT_TABLE_STRING);
        SQL_EVENT_TABLE_NAME = mContext.getString(R.string.EVENT_TABLE_NAME);
        EVENT_REQ_USER_COL = mContext.getString(R.string.EVENT_REQ_USER_COL);
        EVENT_REQ_USER_ID_COL = mContext.getString(R.string.EVENT_REQ_USER_ID_COL);
        EVENT_STIME_COL = mContext.getString(R.string.EVENT_STIME_COL);
        EVENT_STIME_EPOCH_COL = mContext.getString(R.string.EVENT_STIME_EPOCH_COL);
        EVENT_DUR_COL = mContext.getString(R.string.EVENT_DUR_COL);
        EVENT_ETIME_COL=mContext.getString(R.string.EVENT_ETIME_COL);
        EVENT_HALL_COL = mContext.getString(R.string.EVENT_HALL_COL);
        EVENT_VENUE_COL = mContext.getString(R.string.EVENT_VENUE_COL);
        EVENT_AC_ID_COL = mContext.getString(R.string.EVENT_AC_ID_COL);
        EVENT_APPROVAL_COL = mContext.getString(R.string.EVENT_APPROVAL_COL);
        EVENT_ATT_COL = mContext.getString(R.string.EVENT_ATT_COL);
        EVENT_ALC_COL = mContext.getString(R.string.EVENT_ALC_COL);
        EVENT_FORM_COL = mContext.getString(R.string.EVENT_FORM_COL);
        EVENT_MT_COL = mContext.getString(R.string.EVENT_MT_COL);
        EVENT_PRC_COL = mContext.getString(R.string.EVENT_PRC_COL);
        EVENT_OCTYP_COL = mContext.getString(R.string.EVENT_OCTYP_COL);
        EVENT_ENT_COL = mContext.getString(R.string.EVENT_ENT_COL);
        SQL_CREATE_EVENT_CS_ASSIGN_TABLE_STRING = mContext.getString(R.string.SQL_CREATE_EVENT_CS_ASSIGN_TABLE_STRING);
        SQL_EVENT_TRIGGER_CALC_END_TIME = mContext.getString(R.string.SQL_EVENT_TRIGGER_CALC_END_TIME);
        SQL_EVENT_TRIGGER_PROFILE_DELETION = mContext.getString(R.string.SQL_EVENT_TRIGGER_PROFILE_DELETION);
        SQL_EVENT_TRIGGER_EVENT_DELETION = mContext.getString(R.string.SQL_EVENT_TRIGGER_EVENT_DELETION);
        SQL_EVENT_TRIGGER_PROFILE_USERNAME_CHANGE = mContext.getString(R.string.SQL_EVENT_TRIGGER_PROFILE_USERNAME_CHANGE);
        SQL_EVENT_TRIGGER_BECOMES_UNAPPROVED = mContext.getString(R.string.SQL_EVENT_TRIGGER_BECOMES_UNAPPROVED);
        EVENT_ID_COL = mContext.getString(R.string.EVENT_ID_COL);
        EVENT_CS_PROFILE_ID_COL = mContext.getString(R.string.EVENT_CS_PROFILE_ID_COL);
        EVENT_CS_ASSIGN_TABLE_NAME = mContext.getString(R.string.EVENT_CS_ASSIGN_TABLE_NAME);
    }
    public static DatabaseInterface getInstance(Context context) {
        // Lazy initializer

        if (instance == null) {
            instance = new DatabaseInterface(context);
        }
        return instance;
    }

    //String SQL_CREATE_EVENT_TABLE;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROFILE_TABLE);
        db.execSQL(SQL_CREATE_REGISTRATION_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_EVENT_CS_ASSIGN_TABLE_STRING);
        db.execSQL(SQL_EVENT_TRIGGER_CALC_END_TIME);
        db.execSQL(SQL_EVENT_TRIGGER_PROFILE_DELETION);
        db.execSQL(SQL_EVENT_TRIGGER_EVENT_DELETION);
        db.execSQL(SQL_EVENT_TRIGGER_PROFILE_USERNAME_CHANGE);
        db.execSQL(SQL_EVENT_TRIGGER_BECOMES_UNAPPROVED);

        createBaseProfile(db, "u","u","User",1000555556,"555-555-5556","Base user");
        createBaseProfile(db, "a","a","Admin",1000555555,"555-555-5555","Base admin");
        createBaseProfile(db, "c","c","Caterer",1000555557,"555-555-5557","Base caterer");
        createBaseProfile(db, "cs","cs","Caterer Staff",1000555558,"555-555-5558","Base caterer staff");
        createBaseProfile(db, "u2","u2","User",1000555566,"555-556-5556","Base user2");
        createBaseProfile(db, "c2","c2","Caterer",1000555567,"555-556-5566","Base caterer2");
        createBaseProfile(db, "cs2","cs2","Caterer Staff",1000555568,"555-555-5568","Base caterer staff2");
        createBaseProfile(db, "cs3","cs3","Caterer Staff",1000555561,"555-555-5568","Base caterer staff3");
        createBaseProfile(db, "cs4","cs4","Caterer Staff",1000555562,"555-555-5568","Base caterer staff4");
        createBaseProfile(db, "cs5","cs5","Caterer Staff",1000555563,"555-555-5568","Base caterer staff5");
        createBaseProfile(db, "cs6","cs6","Caterer Staff",1000555564,"555-555-5568","Base caterer staff6");

        //if req_user_id is left null, it searches based on username.
        createBaseEvent(db, "u",null,"2018-04-01 13:30:00","2", "Liberty", "Italian",3,1,50,0,
                0,"Lunch",900.00,"Classy Gathering","Classical Music");
        createBaseEvent(db, "u",null,"2018-05-16 16:30:00","6", "Arlington", "Italian",3,1,40,1,
                1,"Dinner",2280.00,"Wedding","Play 80's Music");
        createBaseEvent(db, "u",null,"2018-05-16 15:30:00","6", "Maverick", "Indian",3,1,100,1,
                1,"Dinner",5400.00,"Wedding","Play 70's Music");
        createBaseEvent(db, "u",null,"2018-05-16 14:30:00","6", "KC", "American",3,1,20,1,
                1,"Dinner",1140.00,"Wedding","Play 60's Music");
        createBaseEvent(db, "u",null,"2018-05-16 13:30:00","6", "Shard", "Chinese",6,1,25,1,
                1,"Dinner",1350.00,"Wedding","Play 50's Music");
        createBaseEvent(db, "u",null,"2018-05-16 12:00:00","6", "Liberty", "French",6,1,70,1,
                1,"Dinner",3840.00,"Wedding","Play 40's Music");
        createBaseEvent(db, "u",null,"2018-05-15 12:00:00","6", "Liberty", "Chinese",6,1,70,1,
                1,"Dinner",3840.00,"Wedding","Play Chinese Music");
        createBaseEvent(db, "u2",null,"2018-05-15 13:30:00","2", "Liberty", "Mexican",null,0,50,0,
                0,"Lunch",900.00,"Fiesta","Mariachi band");
        createBaseEvent(db, "u2",null,"2018-05-17 13:30:00","2", "Liberty", "Pizza",null,0,50,0,
                0,"Lunch",900.00,"Fiesta","Mariachi band");
        createBaseEvent(db, "u2",null,"2018-05-18 13:30:00","2", "Liberty", "Italian",6,1,50,0,
                0,"Lunch",900.00,"Classy Gathering","Classical Music");

        createBaseEventAssignedCS(db,2,"cs",null); //username not necessary if profileID is known.
        createBaseEventAssignedCS(db,3,"cs2",null);
        createBaseEventAssignedCS(db,4,"cs3",null);
        createBaseEventAssignedCS(db,5,"cs4",null);
        createBaseEventAssignedCS(db,6,"cs5",null);
        createBaseEventAssignedCS(db,7,"cs2",null);
        createBaseEventAssignedCS(db,10,"cs2",null);
        //Default "cs" caterer staff (profile table 4th db row) user is assigned the the default dummy event (event table 1st db row)




    }


    // Create system user profiles when the database is first created.
    private void createBaseProfile(SQLiteDatabase db, String username, String password, String role,
                              long uta_id, String contactDetails, String personalDetails) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, username);
        values.put(COLUMN_NAME_PASSWORD, password);
        values.put(COLUMN_NAME_ROLE,     role);
        values.put(COLUMN_NAME_UTAID,    uta_id);
        values.put(COLUMN_NAME_CONTACT,  contactDetails);
        values.put(COLUMN_NAME_PERSONAL, personalDetails);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_PROFILE, null, values);
    }

    public void createBaseEvent(SQLiteDatabase db, String req_user,Integer req_user_id, String Stime, String dur, String hall, String venue,
                                   Integer assigned_caterer, int approval_flag, int attendance, int alco_flag, int formal_flag, String meal_type,
                                    double price, String occ_type, String ent_items) {

        if(req_user_id==null){
            req_user_id=getProfileID(db, req_user);
        }

        ContentValues values = new ContentValues();
        values.put(EVENT_REQ_USER_COL, req_user);
        values.put(EVENT_REQ_USER_ID_COL, req_user_id);
        values.put(EVENT_STIME_COL, Stime);
        values.put(EVENT_DUR_COL, dur);
        values.put(EVENT_HALL_COL,     hall);
        values.put(EVENT_VENUE_COL,     venue);
        values.put(EVENT_AC_ID_COL,    assigned_caterer);
        values.put(EVENT_APPROVAL_COL,  approval_flag);
        values.put(EVENT_ATT_COL, attendance);
        values.put(EVENT_ALC_COL, alco_flag);
        values.put(EVENT_FORM_COL, formal_flag);
        values.put(EVENT_MT_COL, meal_type);
        values.put(EVENT_PRC_COL, price);
        values.put(EVENT_OCTYP_COL, occ_type);
        values.put(EVENT_ENT_COL, ent_items);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(SQL_EVENT_TABLE_NAME, null, values);
    }

    private void createBaseEventAssignedCS(SQLiteDatabase db, int EventID, String username, Integer profileID) {

        if(profileID==null){
            profileID=getProfileID(db, username);
        }

        ContentValues values = new ContentValues();
        values.put(EVENT_ID_COL, EventID);
        values.put(EVENT_CS_PROFILE_ID_COL, profileID);


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(EVENT_CS_ASSIGN_TABLE_NAME, null, values);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PROFILE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void createProfile(String username, String password, String role,
                              long uta_id, String contactDetails, String personalDetails) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, username);
        values.put(COLUMN_NAME_PASSWORD, password);
        values.put(COLUMN_NAME_ROLE,     role);
        values.put(COLUMN_NAME_UTAID,    uta_id);
        values.put(COLUMN_NAME_CONTACT,  contactDetails);
        values.put(COLUMN_NAME_PERSONAL, personalDetails);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_PROFILE, null, values);
    }

    public void createRegistrationRequest(String username, String password, String role,
                                          long uta_id, String contactDetails, String personalDetails) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, username);
        values.put(COLUMN_NAME_PASSWORD, password);
        values.put(COLUMN_NAME_ROLE,     role);
        values.put(COLUMN_NAME_UTAID,    uta_id);
        values.put(COLUMN_NAME_CONTACT,  contactDetails);
        values.put(COLUMN_NAME_PERSONAL, personalDetails);
        values.put(COLUMN_NAME_DATETIME,  new Date().getTime());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_REGISTRATION, null, values);
    }

    public Cursor getRegistrationRequests() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_DATETIME,
                BaseColumns._ID
        };

        String sortOrder = COLUMN_NAME_DATETIME + " ASC";

        return db.query(
                TABLE_NAME_REGISTRATION,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
    }

    public Cursor getRegistrationRequest(String req_id) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_PASSWORD,
                COLUMN_NAME_ROLE,
                COLUMN_NAME_UTAID,
                COLUMN_NAME_CONTACT,
                COLUMN_NAME_PERSONAL
        };

        String selection =
                BaseColumns._ID + " = ?";
        String[] selectionArgs = { req_id };

        return db.query(
                TABLE_NAME_REGISTRATION,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
    }

    public void updateProfile(String profID, String username, String password, String role,
                              long uta_id, String contactDetails, String personalDetails) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, username);
        values.put(COLUMN_NAME_PASSWORD, password);
        values.put(COLUMN_NAME_ROLE,     role);
        values.put(COLUMN_NAME_UTAID,    uta_id);
        values.put(COLUMN_NAME_CONTACT,  contactDetails);
        values.put(COLUMN_NAME_PERSONAL, personalDetails);

        String selection =
                BaseColumns._ID + " = ?";
        String[] selectionArgs = { profID };

        db.update(TABLE_NAME_PROFILE, values, selection, selectionArgs);
    }

    public void deleteProfile(String profileID) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { profileID };

        int deletedRows = db.delete(TABLE_NAME_PROFILE, selection, selectionArgs);
    }

    public void deleteEvent(String eventID) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {eventID};

        int deletedRows = db.delete(SQL_EVENT_TABLE_NAME, selection, selectionArgs);
    }

    public void deleteRegistrationRequest(String requestID) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { requestID };

        int deletedRows = db.delete(TABLE_NAME_REGISTRATION, selection, selectionArgs);
    }

    public String login(String username, String password) {
        String ret = null;
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_ROLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                COLUMN_NAME_USERNAME + " = ? AND " +
                COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { username, password};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME_PROFILE,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ROLE));
            String id  = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID));

            final SharedPreferences sharedPref = mContext.getSharedPreferences(
                    "MavCat.preferences", Context.MODE_PRIVATE
            );
            final SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("active_username", username);
            editor.putString("active_id", id);
            editor.commit();

            ret = role;
            cursor.close();
        }


        return ret;
    }

    public Intent logout(Context context) {
        Intent intent = new Intent(context, sysuser_uc2_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        //Makes sure shared preference is reset
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("active_username");
        editor.remove("active_id");
        editor.commit();
        //------
        return intent;
    }

    public String getActiveUsername() throws Exception {
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );

        String user = sharedPref.getString("active_username"," ");
        if(user.equals(" ")){
            throw new Exception("No valid username in shared preferences", null);
        }
        return user;
    }
    public String getActiveID() throws Exception {
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );
        String id = sharedPref.getString("active_id"," ");
        if(id.equals(" ")){
            throw new Exception("No valid id in shared preferences", null);
        }
        return id;
    }
    public Cursor searchUsername(String usernameQuery) {
        usernameQuery = "%" + usernameQuery + "%";
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_ROLE,
                BaseColumns._ID
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                COLUMN_NAME_USERNAME + " LIKE ?";
        String[] selectionArgs = { usernameQuery };
        String orderBy = COLUMN_NAME_USERNAME + " ASC";

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                TABLE_NAME_PROFILE,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                orderBy              // The sort order
        );
    }

    public boolean searchUsernameRegistrationConflictCheck(String usernameQuery) {
        boolean foundConflict;
        String currentTable=TABLE_NAME_PROFILE;
        foundConflict=subcheck(usernameQuery, currentTable);
        if(!foundConflict){
            currentTable=TABLE_NAME_REGISTRATION;
            foundConflict=subcheck(usernameQuery, currentTable);
            return foundConflict;
        }
        else{
            return foundConflict;
        }
    }

    public boolean subcheck(String usernameQuery, String currentTable) {
        boolean foundConflict=false;
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                COLUMN_NAME_USERNAME + " LIKE ?";
        String[] selectionArgs = { usernameQuery };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        Cursor resultCursor = db.query(
                currentTable,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
        resultsList = new ArrayList<>();
        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String username = resultCursor.getString(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
                resultsList.add(username);
            }
            if(resultsList.contains(usernameQuery) ){
                foundConflict=true;
            }
        }
        return foundConflict;
    }

    public Cursor getProfileByUsername(String username) {

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_PASSWORD,
                COLUMN_NAME_ROLE,
                COLUMN_NAME_UTAID,
                COLUMN_NAME_CONTACT,
                COLUMN_NAME_PERSONAL
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = { username };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                TABLE_NAME_PROFILE,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
    }

    public Cursor getProfileByID(String profileID) {

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_PASSWORD,
                COLUMN_NAME_ROLE,
                COLUMN_NAME_UTAID,
                COLUMN_NAME_CONTACT,
                COLUMN_NAME_PERSONAL
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                BaseColumns._ID + " = ?";
        String[] selectionArgs = { profileID };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                TABLE_NAME_PROFILE,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
    }

    public void convertRequestToProfile(String requestID) {

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME,
                COLUMN_NAME_PASSWORD,
                COLUMN_NAME_ROLE,
                COLUMN_NAME_UTAID,
                COLUMN_NAME_CONTACT,
                COLUMN_NAME_PERSONAL
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                BaseColumns._ID + " = ?";
        String[] selectionArgs = { requestID };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        Cursor req = db.query(
                TABLE_NAME_REGISTRATION,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );

        if (req != null) {
            req.moveToNext();
            String username = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
            String password = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PASSWORD));
            String role     = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_ROLE));
            String uta_id   = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_UTAID));
            String contact  = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_CONTACT));
            String personal = req.getString(req.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PERSONAL));
            createProfile(username, password, role, Long.parseLong(uta_id), contact, personal);
            deleteRegistrationRequest(requestID);
            req.close();
        }
    }

    public Cursor searchAvailHalls(String startTime, String endTime) {
        //Input is in Epoch (since 1970) time
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                EVENT_HALL_COL
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                //COLUMN_NAME_USERNAME + " LIKE ?";
                "(CAST(((julianday("+EVENT_STIME_COL+")-2440587.5)*86400.0) AS INTEGER) < CAST(? AS INTEGER)) AND (CAST(((julianday("+EVENT_ETIME_COL+")-2440587.5)*86400.0) AS INTEGER) > CAST(? AS INTEGER))";
                //EVENT_HALL_COL+ " LIKE ?";
        String[] selectionArgs = {endTime, startTime };
        //String test ="Arlington";
        //String[] selectionArgs = {test};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                true,
                SQL_EVENT_TABLE_NAME,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null,           // The sort order
                null                //limitation on rows returned


        );
    }

    private Cursor searchProfileId(SQLiteDatabase db,String username) {
        //SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                "username = ?";
        String[] selectionArgs = {username};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                TABLE_NAME_PROFILE,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
        );
    }
    private Integer getProfileID(SQLiteDatabase db,String username){
        List<String> profileIDs = new ArrayList<>();
        Cursor resultCursor = searchProfileId(db,username);
        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String resultID = resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                profileIDs.add(resultID);
            }
        }
        if(profileIDs.size()>1 || profileIDs.size()==0){
            //should not occur
            return -1; //should error
        }
        return Integer.parseInt(profileIDs.get(0));
    }

    public String getProfileRole(String profileID){
        //SQLiteDatabase db,
        List<String> results = new ArrayList<>();
        Cursor resultCursor = getProfileByID(profileID);
        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String result = resultCursor.getString(resultCursor.getColumnIndexOrThrow(COLUMN_NAME_ROLE));
                results.add(result);
            }
        }
        if(results.size()>1 || results.size()==0){
            //should not occur
            return "null"; //should error
        }
        return results.get(0);
    }

    private Cursor searchProfileEvents(SQLiteDatabase db,String username) {
        //SQLiteDatabase db = getReadableDatabase();
        long tempID= getProfileID(db,username);
        String selection;
        if(getProfileRole(String.valueOf(tempID)).equals("User")){
            selection =
                    EVENT_REQ_USER_ID_COL+" = ?";
        }
        else{
            selection =
                    EVENT_AC_ID_COL+" = ?";
        }

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                EVENT_STIME_EPOCH_COL,
                EVENT_HALL_COL,
                EVENT_DUR_COL
        };

        // Filter results WHERE "title" = 'My Title'

        String[] selectionArgs = {String.valueOf(tempID)};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                SQL_EVENT_TABLE_NAME,     // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                EVENT_STIME_EPOCH_COL              // The sort order
        );
    }

    private Cursor searchProfileEventRequests(SQLiteDatabase db) {
        //SQLiteDatabase db = getReadableDatabase();
        String selection = EVENT_APPROVAL_COL+" = 0";


        // How you want the results sorted in the resulting Cursor
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

        return db.query(
                SQL_EVENT_TABLE_NAME,     // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                EVENT_STIME_EPOCH_COL              // The sort order
        );
    }

//    public void updateProfile(String profID, String username, String password, String role,
//                              long uta_id, String contactDetails, String personalDetails) {
//
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAME_USERNAME, username);
//        values.put(COLUMN_NAME_PASSWORD, password);
//        values.put(COLUMN_NAME_ROLE,     role);
//        values.put(COLUMN_NAME_UTAID,    uta_id);
//        values.put(COLUMN_NAME_CONTACT,  contactDetails);
//        values.put(COLUMN_NAME_PERSONAL, personalDetails);
//
//        String selection =
//                BaseColumns._ID + " = ?";
//        String[] selectionArgs = { profID };
//
//        db.update(TABLE_NAME_PROFILE, values, selection, selectionArgs);
//    }

    public void updateEventCaterer(String eventID, String catererID) {

        ContentValues values = new ContentValues();
        values.put(EVENT_AC_ID_COL, catererID);
        values.put(EVENT_APPROVAL_COL, 1);

        String selection =
                BaseColumns._ID + " = ?";
        String[] selectionArgs = { eventID };

        SQLiteDatabase db = getWritableDatabase();
        int retVal =  db.update(SQL_EVENT_TABLE_NAME, values, selection, selectionArgs);
        int doNothing;
    }

    public List<Long> getEpochs(String username){
        List<Long> eventEpochs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor resultCursor = searchProfileEvents(db,username);
        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                Long resultEpoch = (long) (resultCursor.getDouble(resultCursor.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL))*(double)1000);
                eventEpochs.add(resultEpoch);
            }
        }
        return eventEpochs;
    }

    public class eventSummarySet{
        String hall;
        String dur;
        String price;
        Long epoch;
        int req_user_id;
        int approval_flag;
        int attendance;
        String mealtype;
        String venue;
        int alco_flag;
        int formal_flag;
        String occ_type;
        String ent_items;
        String eventID;

    }



    public class eventRequestSummarySet{
        String hall;
        Long epoch;
        String eventID;

    }

    public List<eventSummarySet> getEventSummary(String username, Date selectedDate){



        List<eventSummarySet> eventSummary = new ArrayList<>();
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
        SQLiteDatabase db = getReadableDatabase();
        //Calendar selectedDateCal = Calendar.getInstance();
        //selectedDateCal.setTimeInMillis(selectedDate.getTime());
        Calendar iteratingDateCal = Calendar.getInstance();


        Cursor resultCursor = searchProfileEvents(db,username);
        if(resultCursor != null) {

            int i=0;

            while (resultCursor.moveToNext()) {
                eventSummarySet[] resultData = new eventSummarySet[1];
                iteratingDateCal.clear();
                long tempEpoch=(long) (resultCursor.getDouble(resultCursor.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL))*(double)1000);
                iteratingDateCal.setTimeInMillis(tempEpoch);
                if(ymd.format(selectedDate).equals(ymd.format(iteratingDateCal.getTime()))){
                    resultData[0]=new eventSummarySet();
                    resultData[0].hall=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_HALL_COL));
                    resultData[0].dur=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_DUR_COL));
                    resultData[0].price=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_PRC_COL));
                    resultData[0].epoch=tempEpoch;
                    resultData[0].req_user_id = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_REQ_USER_ID_COL));
                    resultData[0].approval_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_APPROVAL_COL));
                    resultData[0].attendance = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ATT_COL));
                    resultData[0].mealtype=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_MT_COL));
                    resultData[0].venue=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_VENUE_COL));
                    resultData[0].alco_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ALC_COL));
                    resultData[0].formal_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_FORM_COL));
                    resultData[0].occ_type=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_OCTYP_COL));
                    resultData[0].ent_items=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_ENT_COL));
                    resultData[0].eventID=resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                    eventSummary.add(resultData[0]);
                }
            }

        }
        return eventSummary;
    }

    public List<eventSummarySet> getEventRequestSummaries(){
        List<eventSummarySet> eventRequestSummaries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor resultCursor = searchProfileEventRequests(db);
        if(resultCursor != null) {

            int i=0;

            while (resultCursor.moveToNext()) {
                eventSummarySet[] resultData = new eventSummarySet[1];
                resultData[0] = new eventSummarySet();

                resultData[0].hall=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_HALL_COL));
                resultData[0].dur=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_DUR_COL));
                resultData[0].price=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_PRC_COL));
                resultData[0].epoch=(long) (resultCursor.getDouble(resultCursor.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL))*(double)1000);
                resultData[0].req_user_id = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_REQ_USER_ID_COL));
                resultData[0].approval_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_APPROVAL_COL));
                resultData[0].attendance = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ATT_COL));
                resultData[0].mealtype=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_MT_COL));
                resultData[0].venue=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_VENUE_COL));
                resultData[0].alco_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ALC_COL));
                resultData[0].formal_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_FORM_COL));
                resultData[0].occ_type=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_OCTYP_COL));
                resultData[0].ent_items=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_ENT_COL));
                resultData[0].eventID=resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));

                eventRequestSummaries.add(resultData[0]);
            }

        }
        return eventRequestSummaries;
    }


//TODO in progress here

    public List<eventSummarySet> getEventSummaryCatererStaff(String ID, Date selectedDate){



        List<eventSummarySet> eventSummary = new ArrayList<>();
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
        SQLiteDatabase db = getReadableDatabase();
        //Calendar selectedDateCal = Calendar.getInstance();
        //selectedDateCal.setTimeInMillis(selectedDate.getTime());
        Calendar iteratingDateCal = Calendar.getInstance();



        String[] arg = new String[]{ ID };
        Cursor assignedEventIDs = db.rawQuery("SELECT "+EVENT_ID_COL+" FROM "+EVENT_CS_ASSIGN_TABLE_NAME+" WHERE "+EVENT_CS_PROFILE_ID_COL+" = ?",
                arg);

        while (assignedEventIDs.moveToNext()) {
            String selection = "rowid"+" = ?";
            String[] projection = {
                    EVENT_STIME_EPOCH_COL, //not using this currently
            };
            String[] selectionArgs = {assignedEventIDs.getString(assignedEventIDs.getColumnIndexOrThrow(EVENT_ID_COL))};

            Cursor resultCursor = db.query(
                    SQL_EVENT_TABLE_NAME,     // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    EVENT_STIME_EPOCH_COL              // The sort order
            );
            if(resultCursor != null) {

                while (resultCursor.moveToNext()) {
                    eventSummarySet[] resultData = new eventSummarySet[1];
                    iteratingDateCal.clear();
                    long tempEpoch=(long) (resultCursor.getDouble(resultCursor.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL))*(double)1000);
                    iteratingDateCal.setTimeInMillis(tempEpoch);
                    if(ymd.format(selectedDate).equals(ymd.format(iteratingDateCal.getTime()))){
                        resultData[0]=new eventSummarySet();
                        resultData[0].hall=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_HALL_COL));
                        resultData[0].dur=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_DUR_COL));
                        resultData[0].price=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_PRC_COL));
                        resultData[0].epoch=tempEpoch;
                        resultData[0].req_user_id = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_REQ_USER_ID_COL));
                        resultData[0].approval_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_APPROVAL_COL));
                        resultData[0].attendance = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ATT_COL));
                        resultData[0].mealtype=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_MT_COL));
                        resultData[0].venue=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_VENUE_COL));
                        resultData[0].alco_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_ALC_COL));
                        resultData[0].formal_flag = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(EVENT_FORM_COL));
                        resultData[0].occ_type=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_OCTYP_COL));
                        resultData[0].ent_items=resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_ENT_COL));
                        resultData[0].eventID=resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                        eventSummary.add(resultData[0]);
                    }
                }
            }
        }
        if(eventSummary!=null){
            //events will be out of order without this
            eventSummarySet[] tempArray = new eventSummarySet[eventSummary.size()];
            tempArray = eventSummary.toArray(tempArray);

            Arrays.sort(tempArray, new SortObjectsByEpoch());
            eventSummary=Arrays.asList(tempArray);
        }
        return eventSummary;
    }

    class SortObjectsByEpoch implements Comparator<eventSummarySet> {
        public int compare(eventSummarySet A, eventSummarySet B) {
            if ( A.epoch < B.epoch ) return -1;
            else if ( A.epoch == B.epoch ) return 0;
            else return 1;
        }
    }

    public List<Long> getEventEpochByCatStaffID(String ID) {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> eventEpochs = new ArrayList<>();

        String[] arg = new String[]{ ID };
        Cursor assignedEventIDs = db.rawQuery("SELECT "+EVENT_ID_COL+" FROM "+EVENT_CS_ASSIGN_TABLE_NAME+" WHERE "+EVENT_CS_PROFILE_ID_COL+" = ?",
                arg);

        while (assignedEventIDs.moveToNext()) {
            String selection = "rowid"+" = ?";
            String[] projection = {
                    EVENT_STIME_EPOCH_COL, //not using this currently
            };
            String[] selectionArgs = {assignedEventIDs.getString(assignedEventIDs.getColumnIndexOrThrow(EVENT_ID_COL))};

            Cursor eventInfo = db.query(
                    SQL_EVENT_TABLE_NAME,     // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    EVENT_STIME_EPOCH_COL              // The sort order
            );
            while (eventInfo.moveToNext()) {
                eventEpochs.add((long) (eventInfo.getDouble(eventInfo.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL))*(double)1000));
            }

        }

        return eventEpochs;

    }

    public List<String> getCSofEvent(String ID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] arg = new String[]{ ID };

        List<String> CSAssignedUsernames = new ArrayList<>();

        Cursor assignedCSIDs = db.rawQuery("SELECT "+EVENT_CS_PROFILE_ID_COL+" FROM "+EVENT_CS_ASSIGN_TABLE_NAME+" WHERE "+EVENT_ID_COL+" = ?",
                arg);

        while (assignedCSIDs.moveToNext()) {
                String tempProfID = assignedCSIDs.getString(assignedCSIDs.getColumnIndexOrThrow(EVENT_CS_PROFILE_ID_COL));
                Cursor usernameCursor = getProfileByID(tempProfID);
                while (usernameCursor.moveToNext()) {
                    CSAssignedUsernames.add(usernameCursor.getString(usernameCursor.getColumnIndexOrThrow(COLUMN_NAME_USERNAME)));
                }

        }
        java.util.Collections.sort(CSAssignedUsernames);
        return CSAssignedUsernames;

    }

    public List<String> getFreeCS(String ID) {
        List<String> conflictedCSIds= new ArrayList<>();
        List<String> freeCSUsernames= new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String[] arg = new String[]{ ID };
        String startTime="";
        String endTime="";
        Cursor eventInfo = db.rawQuery("SELECT "+EVENT_STIME_EPOCH_COL+" , "+EVENT_DUR_COL+" FROM "+SQL_EVENT_TABLE_NAME+" WHERE "+"rowid = CAST(? AS INTEGER)",
            arg);

        while(eventInfo.moveToNext()){
            double startTimeval=eventInfo.getDouble(eventInfo.getColumnIndexOrThrow(EVENT_STIME_EPOCH_COL));
            startTime = String.format("%.1f",startTimeval);
            double endTimeval= Double.parseDouble(startTime)+ (((double) 3600)*Double.parseDouble(eventInfo.getString(eventInfo.getColumnIndexOrThrow(EVENT_DUR_COL))));

            endTime = String.format("%.1f",endTimeval);
            //3600 seconds per hour
        }

        if(!startTime.isEmpty() && !endTime.isEmpty()){
            String[] projection = {
                    BaseColumns._ID
            };

            // Filter results WHERE "title" = 'My Title'
            String selection =
                    //COLUMN_NAME_USERNAME + " LIKE ?";
                    "(CAST(((julianday(" + EVENT_STIME_COL + ")-2440587.5)*86400.0) AS INTEGER) < CAST(? AS INTEGER)) AND (CAST(((julianday(" + EVENT_ETIME_COL + ")-2440587.5)*86400.0) AS INTEGER) > CAST(? AS INTEGER))";
            //EVENT_HALL_COL+ " LIKE ?";
            String[] selectionArgs = {endTime, startTime};


            Cursor conflictingEventIDs= db.query(
                    true,
                    SQL_EVENT_TABLE_NAME,     // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,          // don't group the rows
                    null,           // don't filter by row groups
                    null,           // The sort order
                    null                //limitation on rows returned


            );
            while(conflictingEventIDs.moveToNext()){
                Cursor assignedConflictedCSIDs = db.rawQuery("SELECT "+EVENT_CS_PROFILE_ID_COL+" FROM "+EVENT_CS_ASSIGN_TABLE_NAME+" WHERE "+EVENT_ID_COL+" = ?",
                        arg);
                while(assignedConflictedCSIDs.moveToNext()){
                    conflictedCSIds.add(assignedConflictedCSIDs.getString(assignedConflictedCSIDs.getColumnIndexOrThrow(EVENT_CS_PROFILE_ID_COL)));
                }
            }
        }
        String[] arg2 = new String[]{ "Caterer Staff" };
        Cursor allCSids = db.rawQuery("SELECT "+COLUMN_NAME_USERNAME+", _id FROM "+TABLE_NAME_PROFILE+" WHERE "+COLUMN_NAME_ROLE+" = ?",
                arg2);
        while(allCSids.moveToNext()){
            if(!conflictedCSIds.contains(allCSids.getString(allCSids.getColumnIndexOrThrow(BaseColumns._ID)))){
                freeCSUsernames.add(allCSids.getString(allCSids.getColumnIndexOrThrow(COLUMN_NAME_USERNAME)));
            }
        }
        java.util.Collections.sort(freeCSUsernames);
        return freeCSUsernames;
    }



    public void assignCatStaff(String eventID, String username){

    }

    public void removeCatStaff(String eventID, String username) {

    }
}
