package comcse5324projutacatering.httpsgithub.utacatering;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseInterface extends SQLiteOpenHelper {

    private static DatabaseInterface instance;

    public static final String TABLE_NAME_PROFILE      = "profile";
    public static final String TABLE_NAME_REGISTRATION = "registration_requests";
    public static final String COLUMN_NAME_USERNAME    = "username";
    public static final String COLUMN_NAME_PASSWORD    = "password";
    public static final String COLUMN_NAME_ROLE        = "role";
    public static final String COLUMN_NAME_UTAID       = "uta_id";
    public static final String COLUMN_NAME_PERSONAL    = "personal_details";
    public static final String COLUMN_NAME_CONTACT     = "contact_details";

    private static final String SQL_CREATE_PROFILE_TABLE =
            "CREATE TABLE " + TABLE_NAME_PROFILE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT,"    +
                    COLUMN_NAME_PASSWORD + " TEXT,"    +
                    COLUMN_NAME_ROLE     + " TEXT,"    +
                    COLUMN_NAME_UTAID    + " INTEGER," +
                    COLUMN_NAME_PERSONAL + " TEXT,"    +
                    COLUMN_NAME_CONTACT  + " TEXT)"   ;

    private static final String SQL_CREATE_REGISTRATION_TABLE =
            "CREATE TABLE " + TABLE_NAME_REGISTRATION + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT,"    +
                    COLUMN_NAME_PASSWORD + " TEXT,"    +
                    COLUMN_NAME_ROLE     + " TEXT,"    +
                    COLUMN_NAME_UTAID    + " INTEGER," +
                    COLUMN_NAME_PERSONAL + " TEXT,"    +
                    COLUMN_NAME_CONTACT  + " TEXT)"   ;

    private Context mContext;

    public String SQL_CREATE_EVENT_TABLE ;
    public String EVENT_REQ_USER_COL;
    public String EVENT_REQ_USER_ID_COL;
    public String EVENT_STIME_COL;
    public String EVENT_DUR_COL;
    public String EVENT_ETIME_COL;
    public String EVENT_HALL_COL;
    public String EVENT_AC_COL;
    public String EVENT_STAT_COL;
    public String EVENT_ATT_COL;
    public String EVENT_ALC_COL;
    public String EVENT_FORM_COL;
    public String EVENT_MT_COL;
    public String EVENT_PRC_COL;
    public String EVENT_OCTYP_COL;
    public String EVENT_ENT_COL;
    public String SQL_EVENT_TRIGGER_CALC_END_TIME;

    private static final String SQL_DELETE_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_PROFILE;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        SQL_CREATE_EVENT_TABLE = mContext.getString(R.string.SQL_CREATE_EVENT_TABLE_STRING);
        EVENT_REQ_USER_COL = mContext.getString(R.string.EVENT_REQ_USER_COL);
        EVENT_REQ_USER_ID_COL = mContext.getString(R.string.EVENT_REQ_USER_ID_COL);
        EVENT_STIME_COL = mContext.getString(R.string.EVENT_STIME_COL);
        EVENT_DUR_COL = mContext.getString(R.string.EVENT_DUR_COL);
        EVENT_HALL_COL = mContext.getString(R.string.EVENT_HALL_COL);
        EVENT_AC_COL = mContext.getString(R.string.EVENT_AC_COL);
        EVENT_STAT_COL = mContext.getString(R.string.EVENT_STAT_COL);
        EVENT_ATT_COL = mContext.getString(R.string.EVENT_ATT_COL);
        EVENT_ALC_COL = mContext.getString(R.string.EVENT_ALC_COL);
        EVENT_FORM_COL = mContext.getString(R.string.EVENT_FORM_COL);
        EVENT_MT_COL = mContext.getString(R.string.EVENT_MT_COL);
        EVENT_PRC_COL = mContext.getString(R.string.EVENT_PRC_COL);
        EVENT_OCTYP_COL = mContext.getString(R.string.EVENT_OCTYP_COL);
        EVENT_ENT_COL = mContext.getString(R.string.EVENT_ENT_COL);
        SQL_EVENT_TRIGGER_CALC_END_TIME = mContext.getString(R.string.SQL_EVENT_TRIGGER_CALC_END_TIME);
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
        db.execSQL(SQL_EVENT_TRIGGER_CALC_END_TIME);

        createBaseProfile(db, "u","u","User",1000555556,"555-555-5556","Base user");
        createBaseProfile(db, "a","a","Admin",1000555555,"555-555-5555","Base admin");
        createBaseProfile(db, "c","c","Caterer",1000555557,"555-555-5557","Base caterer");
        createBaseProfile(db, "cs","cs","CatererStaff",1000555558,"555-555-5558","Base caterer staff");

        createBaseEvent(db, "u",1,"2018-05-13 15:30:00","6", "Arlington","c",1,20,1,
                1,"Pizza",200.50,"Wedding","Play Rap Music");
    }

    // Create system user profiles when the database is first created.
    private void createBaseProfile(SQLiteDatabase db, String username, String password, String role,
                              int uta_id, String contactDetails, String personalDetails) {

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

    private void createBaseEvent(SQLiteDatabase db, String req_user,int req_user_id, String Stime, String dur, String hall,
                                   String assigned_caterer, int status_flag, int attendance, int alco_flag, int formal_flag, String meal_type,
                                    double price, String occ_type, String ent_items) {

        ContentValues values = new ContentValues();
        values.put(EVENT_REQ_USER_COL, req_user);
        values.put(EVENT_REQ_USER_ID_COL, req_user_id);
        values.put(EVENT_STIME_COL, Stime);
        values.put(EVENT_DUR_COL, dur);
        values.put(EVENT_HALL_COL,     hall);
        values.put(EVENT_AC_COL,    assigned_caterer);
        values.put(EVENT_STAT_COL,  status_flag);
        values.put(EVENT_ATT_COL, attendance);
        values.put(EVENT_ALC_COL, alco_flag);
        values.put(EVENT_FORM_COL, formal_flag);
        values.put(EVENT_MT_COL, meal_type);
        values.put(EVENT_PRC_COL, price);
        values.put(EVENT_OCTYP_COL, occ_type);
        values.put(EVENT_ENT_COL, ent_items);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("events", null, values);
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
                              int uta_id, String contactDetails, String personalDetails) {

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
                              int uta_id, String contactDetails, String personalDetails) {

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
        }

        cursor.close();
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
            createProfile(username, password, role, Integer.parseInt(uta_id), contact, personal);
            deleteRegistrationRequest(requestID);
        }
    }

}
