package comcse5324projutacatering.httpsgithub.utacatering;

import android.content.ContentValues;
import android.content.Context;
//import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseInterface extends SQLiteOpenHelper {

    private static DatabaseInterface instance;

    private static final String TABLE_NAME_PROFILE      = "profile";
    private static final String TABLE_NAME_REGISTRATION = "registration_requests";
    public static final String COLUMN_NAME_USERNAME    = "username";
    public static final String COLUMN_NAME_PASSWORD    = "password";
    public static final String COLUMN_NAME_ROLE        = "role";
    public static final String COLUMN_NAME_UTAID       = "uta_id";
    public static final String COLUMN_NAME_PERSONAL    = "personal_details";
    public static final String COLUMN_NAME_CONTACT     = "contact_details";
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
                    COLUMN_NAME_CONTACT  + " TEXT)"   ;
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
    public String EVENT_ID_COL;
    public String EVENT_CS_PROFILE_ID_COL;
    public String EVENT_CS_ASSIGN_TABLE_NAME;

    private static final String SQL_DELETE_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_PROFILE;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    private DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Context mContext;
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

        createBaseProfile(db, "u","u","User",1000555556,"555-555-5556","Base user");
        createBaseProfile(db, "a","a","Admin",1000555555,"555-555-5555","Base admin");
        createBaseProfile(db, "c","c","Caterer",1000555557,"555-555-5557","Base caterer");
        createBaseProfile(db, "cs","cs","Caterer Staff",1000555558,"555-555-5558","Base caterer staff");
        createBaseProfile(db, "u2","u2","User",1000555566,"555-556-5556","Base user2");
        createBaseProfile(db, "c2","c2","Caterer",1000555567,"555-556-5566","Base caterer2");
        createBaseProfile(db, "cs2","cs2","Caterer Staff",1000555568,"555-555-5568","Base caterer staff2");

        //if req_user_id is left null, it searches based on username.
        createBaseEvent(db, "u",1,"2018-05-13 16:30:00","6", "Arlington", "Italian",3,1,40,1,
                1,"Dinner",2280.00,"Wedding","Play 80's Music");
        createBaseEvent(db, "u",null,"2018-05-13 15:30:00","6", "Maverick", "Indian",3,1,100,1,
                1,"Dinner",5400.00,"Wedding","Play 70's Music");
        createBaseEvent(db, "u",null,"2018-05-13 14:30:00","6", "KC", "American",3,1,20,1,
                1,"Dinner",1140.00,"Wedding","Play 60's Music");
        createBaseEvent(db, "u",null,"2018-05-13 13:30:00","6", "Shard", "Chinese",6,1,25,1,
                1,"Dinner",1350.00,"Wedding","Play 50's Music");
        createBaseEvent(db, "u",null,"2018-05-13 12:00:00","6", "Liberty", "French",6,1,70,1,
                1,"Dinner",3840.00,"Wedding","Play 40's Music");
        createBaseEvent(db, "u",null,"2018-05-15 12:00:00","6", "Liberty", "Chinese",6,1,70,1,
                1,"Dinner",3840.00,"Wedding","Play 40's Music");
        createBaseEvent(db, "u2",null,"2018-05-15 13:30:00","2", "Liberty", "Mexican",null,0,50,0,
                0,"Lunch",900.00,"Fiesta","Mariachi band");
        createBaseEvent(db, "u2",null,"2018-05-17 13:30:00","2", "Liberty", "Pizza",null,0,50,0,
                0,"Lunch",900.00,"Fiesta","Mariachi band");
        createBaseEventAssignedCS(db,1,"cs",null); //username not necessary if profileID is known.
        createBaseEventAssignedCS(db,2,"cs",null);
        createBaseEventAssignedCS(db,3,"cs",null);
        createBaseEventAssignedCS(db,4,"cs",null);
        createBaseEventAssignedCS(db,5,"cs",null);
        createBaseEventAssignedCS(db,6,"cs2",null);
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

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_REGISTRATION, null, values);
    }

    public Cursor getRegistrationRequests() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_USERNAME,
                BaseColumns._ID
        };

        // TODO: Sort on date/time
        //String sortOrder = COLUMN_NAME_USERNAME + " DESC";

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
                //BaseColumns._ID,
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
            ret = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ROLE));
            cursor.close();
        }


        return ret;
    }

    public Cursor searchUsername(String usernameQuery) {
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
                SQL_EVENT_TABLE_NAME,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null              // The sort order
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

    private Cursor searchProfileEvents(SQLiteDatabase db,String username) {
        //SQLiteDatabase db = getReadableDatabase();
        long tempID= getProfileID(db,username);
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                EVENT_STIME_EPOCH_COL,
                EVENT_HALL_COL,
                EVENT_DUR_COL
        };

        // Filter results WHERE "title" = 'My Title'
        String selection =
                "req_user_id = ?";
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
                null              // The sort order
        );
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
                    eventSummary.add(resultData[0]);
                }
            }

        }
        return eventSummary;
    }

}
