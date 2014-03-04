package com.example.rehab_coachv1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExternalDbOpenHelper extends SQLiteOpenHelper {

     //Path to the device folder with databases
    public static String DB_PATH;

     //Database file name
    public static String DB_NAME;
    public SQLiteDatabase database;
    public final Context context;

     public SQLiteDatabase getDb() {
        return database;
    }

     public ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;
     //Write a full path to the databases of your application
     String packageName = context.getPackageName();
     DB_PATH = String.format("//data//data//%s//databases//", packageName);
     DB_NAME = databaseName;
     openDataBase();
    }

     //This piece of code will create a database if it’s not yet created
    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }

    //Performing a database existence check
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                          SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //Android doesn’t like resource leaks, everything should 
        // be closed
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //Method for copying the database
    private void copyDataBase() throws IOException {
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

         //Path to the created empty database on your Android device
        String outFileName = DB_PATH + DB_NAME;

         //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

         //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}




//package com.example.rehab_coachv1;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DataBaseHelper extends SQLiteOpenHelper{
//
//    //The Android's default system path of your application database.
//	
//	//PACKAGE NAME USED HERE
//    private static String DB_PATH = "/data/data/com.example.rehab_coachv1/databases/";
// 
//    public static String DB_NAME = "rehab_coach";
//    public static final int DATABASE_VERSION = 1;
// 
//    private SQLiteDatabase myDataBase; 
// 
//    private final Context myContext;
// 
//    /**
//     * Constructor
//     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
//     * @param context
//     */
//    public DataBaseHelper(Context context) {
// 
//    	super(context, DB_NAME, null, 1);
//        this.myContext = context;
//    }	
//    
//	public Cursor getAllActivities(){
//		return this.myDataBase.query("activity", new String[] { "_id", "name", "times_complete","last_time_completed",
//				"category_1"},null,null,null,null,null);//, CATEGORY2_COLUMN, CATEGORY3_COLUMN, CATEGORY4_COLUMN}, null, null, null, null, null);
//	}
// 
//  /**
//     * Creates a empty database on the system and rewrites it with your own database.
//     * */
//    public void createDataBase() throws IOException{
// 
//    	boolean dbExist = checkDataBase();
// 
//    	if(dbExist){
//    		//do nothing - database already exist
//    	}else{
// 
//    		//By calling this method and empty database will be created into the default system path
//               //of your application so we are gonna be able to overwrite that database with our database.
//        	this.getReadableDatabase();
//        	this.close();
//        	try {
//        		this.close();
//    			copyDataBase();
// 
//    		} catch (IOException e) {
// 
//        		throw new Error("Error copying database");
// 
//        	}
//    	}
// 
//    }//CREATE TABLE prerequisite (_id INTEGER PRIMARY KEY, activity_id NUMERIC, name TEXT)
// 
//    /**
//     * Check if the database already exist to avoid re-copying the file each time you open the application.
//     * @return true if it exists, false if it doesn't
//     */
//    private boolean checkDataBase(){
// 
//    	SQLiteDatabase checkDB = null;
// 
//    	try{
//    		String myPath = DB_PATH + DB_NAME;
//    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
// 
//    	}catch(SQLiteException e){
// 
//    		//database does't exist yet.
// 
//    	}
// 
//    	if(checkDB != null){
// 
//    		checkDB.close();
// 
//    	}
// 
//    	return checkDB != null ? true : false;
//    }
// 
//    /**
//     * Copies your database from your local assets-folder to the just created empty database in the
//     * system folder, from where it can be accessed and handled.
//     * This is done by transfering bytestream.
//     * */
//    private void copyDataBase() throws IOException{
// 
//    	//Open your local db as the input stream
//    	InputStream myInput = myContext.getAssets().open(DB_NAME);
// 
//    	// Path to the just created empty db
//    	String outFileName = DB_PATH + DB_NAME;
// 
//    	//Open the empty db as the output stream
//    	OutputStream myOutput = new FileOutputStream(outFileName);
// 
//    	//transfer bytes from the inputfile to the outputfile
//    	byte[] buffer = new byte[1024];
//    	int length;
//    	while ((length = myInput.read(buffer))>0){
//    		myOutput.write(buffer, 0, length);
//    	}
// 
//    	//Close the streams
//    	myOutput.flush();
//    	myOutput.close();
//    	myInput.close();
// 
//    }
// 
//    public void openDataBase() throws SQLException{
// 
//    	//Open the database
//        String myPath = DB_PATH + DB_NAME;
//    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
// 
//    }
// 
//    @Override
//	public synchronized void close() {
// 
//    	    if(myDataBase != null)
//    		    myDataBase.close();
// 
//    	    super.close();
// 
//	}
// 
//	@Override
//	public void onCreate(SQLiteDatabase db) {
// 
//	}
// 
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// 
//	}
// 
//        // Add your public helper methods to access and get content from the database.
//       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
//       // to you to create adapters for your views.
// 
//}
//
