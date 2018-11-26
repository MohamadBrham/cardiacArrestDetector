package ps.wecare.cardiacarrestdetector.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public User insertUser(User user)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, user.getName());
        contentValues.put(myDbHelper.PASSWORD, user.getPassword());
        contentValues.put(myDbHelper.AGE, user.getAge());
        contentValues.put(myDbHelper.PHONE, user.getPhone());
        long id = dbb.insert(myDbHelper.USERS_TABLE, null , contentValues);
        user.setId(id);
        return user;
    }
    public Beloved insertBeloved(Beloved beloved)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.USER_ID, beloved.getUser_id());
        contentValues.put(myDbHelper.STATUS,beloved.getStatus());
        contentValues.put(myDbHelper.PHONE,beloved.getPhone());
        contentValues.put(myDbHelper.NAME,beloved.getName());
        long id = dbb.insert(myDbHelper.BELOVED_TABLE, null , contentValues);
        beloved.setId(id);
        return beloved;
    }
    public Medication insertMedicatin(Medication medication)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.USER_ID, medication.getUser_id());
        contentValues.put(myDbHelper.START,medication.getStart());
        contentValues.put(myDbHelper.END,medication.getEnd());
        contentValues.put(myDbHelper.NAME,medication.getName());
        long id = dbb.insert(myDbHelper.MEDICATIONS_TABLE, null , contentValues);
        medication.setId(id);
        return medication;
    }

    public ArrayList<Medication> getMedications(long user_id)
    {
        ArrayList<Medication> medication = new ArrayList<Medication>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.USER_ID,myDbHelper.START,myDbHelper.END ,myDbHelper.NAME};
        String whereClause = myDbHelper.USER_ID +" = ? ";
        String[] whereArgs = new String[] {""+user_id};
        Cursor cursor = db.query(myDbHelper.MEDICATIONS_TABLE,columns,whereClause,whereArgs,null,null,null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                long id = cursor.getLong(cursor.getColumnIndex(myDbHelper.ID));
                String start = cursor.getString(cursor.getColumnIndex(myDbHelper.START));
                String  end =cursor.getString(cursor.getColumnIndex(myDbHelper.END));
                String  name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
                medication.add(new Medication(id,user_id,name,start,end));
            }
            cursor.close();
        }
        return medication;
    }
    public int deleteMedication(long id){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={""+id};
        int count = db.delete(myDbHelper.MEDICATIONS_TABLE ,myDbHelper.ID+" = ?",whereArgs);
        return  count;
    }
    public int updateMedication(Medication medication){

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,medication.getName());
        contentValues.put(myDbHelper.START,medication.getStart());
        contentValues.put(myDbHelper.END,medication.getEnd());
        String[] whereArgs= {""+medication.getId()};
        int count = db.update(myDbHelper.MEDICATIONS_TABLE,contentValues, myDbHelper.ID+" = ?",whereArgs );
        return count;


    }

    public ArrayList<Beloved> getBeloved(long user_id)
    {
        ArrayList<Beloved> beloved = new ArrayList<Beloved>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.USER_ID,myDbHelper.PHONE,myDbHelper.STATUS ,myDbHelper.NAME};
        String whereClause = myDbHelper.USER_ID +" = ? ";
        String[] whereArgs = new String[] {""+user_id};
        Cursor cursor = db.query(myDbHelper.BELOVED_TABLE,columns,whereClause,whereArgs,null,null,null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                long id = cursor.getLong(cursor.getColumnIndex(myDbHelper.ID));
                long uid = cursor.getLong(cursor.getColumnIndex(myDbHelper.USER_ID));
                String phone = cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE));
                String  status =cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS));
                String  name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
                beloved.add(new Beloved(id,user_id,phone,name,status));
            }
            cursor.close();
        }
        return beloved;
    }
    public int deleteBeloved(long id){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={""+id};
        int count = db.delete(myDbHelper.BELOVED_TABLE ,myDbHelper.ID+" = ?",whereArgs);
        return  count;
    }
    public int updateBeloved(Beloved beloved){

            SQLiteDatabase db = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(myDbHelper.PHONE,beloved.getPhone());
            contentValues.put(myDbHelper.NAME,beloved.getName());
            String[] whereArgs= {""+beloved.getId()};
            int count = db.update(myDbHelper.BELOVED_TABLE,contentValues, myDbHelper.ID+" = ?",whereArgs );
            return count;


    }
    public ArrayList<Dose> getDoses(String medication_id)
    {
        ArrayList<Dose> doses = new ArrayList<Dose>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.MEDICATION_ID,myDbHelper.TIME};
        String whereClause = myDbHelper.MEDICATION_ID+" = ? ";
        String[] whereArgs = new String[] {""+medication_id};
        Cursor cursor = db.query(myDbHelper.DOSES_TABLE,columns,whereClause,whereArgs,null,null,null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                long id = cursor.getLong(cursor.getColumnIndex(myDbHelper.ID));
                long mid = cursor.getLong(cursor.getColumnIndex(myDbHelper.MEDICATION_ID));
                String time = cursor.getString(cursor.getColumnIndex(myDbHelper.TIME));
                doses.add(new Dose(id,mid,time));
            }
            cursor.close();
        }
        return doses;
    }
    public int deleteDose(int id){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={""+id};
        int count = db.delete(myDbHelper.DOSES_TABLE ,myDbHelper.ID+" = ?",whereArgs);
        return  count;
    }
    public int updateDose(Dose dose){

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TIME,dose.getTime());
        String[] whereArgs= {""+dose.getId()};
        int count = db.update(myDbHelper.DOSES_TABLE,contentValues, myDbHelper.ID+" = ?",whereArgs );
        return count;


    }
    public Dose insertDose(Dose dose)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.MEDICATION_ID, dose.getMedication_id());
        contentValues.put(myDbHelper.TIME,dose.getTime());
        long id = dbb.insert(myDbHelper.DOSES_TABLE, null , contentValues);
        dose.setId(id);
        return dose;
    }
    public User getUser( String phone)
    {
        User user = null;
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.NAME,myDbHelper.PASSWORD,myDbHelper.AGE , myDbHelper.PHONE};
        String whereClause = myDbHelper.PHONE +" = ? ";
        String[] whereArgs = new String[] {phone};
        Cursor cursor = db.query(myDbHelper.USERS_TABLE,columns,whereClause,whereArgs,null,null,null);
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(myDbHelper.ID));
                String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
                String  password = cursor.getString(cursor.getColumnIndex(myDbHelper.PASSWORD));
                String  age = cursor.getString(cursor.getColumnIndex(myDbHelper.AGE));

                user = new User(id,name,phone,password,age);
            }
            cursor.close();
        }
        return user;
    }
/*
    public  int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }*/

/*
    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }
    */

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database
        private static final int DATABASE_Version = 5;    // Database Version

        // Users Table
        private static final String USERS_TABLE = "users";   // Table Name
        private static final String ID="id";     // Column I (Primary Key)
        private static final String NAME = "name";    //Column II
        private static final String PASSWORD= "password";    // Column III
        private static final String PHONE = "phone";    //Column IV
        private static final String AGE= "age";    // Column V

        // Beloved people table
        private static final String BELOVED_TABLE = "beloved";   // Table Name
        //private static final String ID="id";     // Column I (Primary Key)
        private static final String USER_ID = "user_id";    //Column II foriegn key for uses (id)
        //private static final String NAME = "name";    //Column III
        //private static final String PHONE = "phone";    //Column IV
        private static final String STATUS = "status";    //Column V

        // medication times table
        private static final String MEDICATIONS_TABLE = "medications";   // Table Name
        //private static final String ID="id";     // Column I (Primary Key)
        //private static final String USER_ID = "user_id";    //Column II foreign key for users (id)
        //private static final String NAME = "name";    //Column III
        private static final String START = "start_date";    //Column IV
        private static final String END = "end_date";    //Column V

        // doses table
        private static final String DOSES_TABLE = "doses";   // Table Name
        //private static final String ID="id";     // Column I (Primary Key)
        private static final String MEDICATION_ID = "medication_id";    //Column II foreign key for medications (id)
        private static final String TIME = "time";    //Column III


        // CREATE TABLES



        private static final String USERS_TABLE_CREATE = "create table "
                + USERS_TABLE + " ("
                + ID + " integer primary key autoincrement, "
                + NAME + " text not null, "
                + PASSWORD + " text not null, "
                + PHONE + " text not null, "
                + AGE + " integer not null);";
        private static final String BELOVED_TABLE_CREATE = "create table "
                + BELOVED_TABLE + " ("
                + ID + " integer primary key autoincrement, "
                + USER_ID + " integer not null, "
                + PHONE + " text not null, "
                + NAME + " text not null, "
                + STATUS + " text not null,"
                + " FOREIGN KEY ("+USER_ID+") REFERENCES "+USERS_TABLE+"("+ID+"));";
        private static final String MEDICATIONS_TABLE_CREATE = "create table "
                + MEDICATIONS_TABLE + " ("
                + ID + " integer primary key autoincrement, "
                + USER_ID + " integer not null, "
                + NAME + " text not null, "
                + START+ " real not null, "
                + END + " real not null,"
                + " FOREIGN KEY ("+USER_ID+") REFERENCES "+USERS_TABLE+"("+ID+"));";

        private static final String DOSES_TABLE_CREATE = "create table "
                + DOSES_TABLE + " ("
                + ID + " integer primary key autoincrement, "
                + MEDICATION_ID + " integer not null, "
                + TIME + " real not null, "
                + " FOREIGN KEY ("+MEDICATION_ID+") REFERENCES "+MEDICATIONS_TABLE+"("+ID+"));";

        //SELECT
        // date(d1),
        // time(d1)
        //FROM
        // datetime_real;


        private static final String DROP_TABLE_USERS ="DROP TABLE IF EXISTS "+USERS_TABLE;
        private static final String DROP_TABLE_BELOVED ="DROP TABLE IF EXISTS "+BELOVED_TABLE;
        private static final String DROP_TABLE_MEDICATIONS="DROP TABLE IF EXISTS "+MEDICATIONS_TABLE;
        private static final String DROP_TABLE_DOSES ="DROP TABLE IF EXISTS "+DOSES_TABLE;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(USERS_TABLE_CREATE);
                db.execSQL(BELOVED_TABLE_CREATE);
                db.execSQL(MEDICATIONS_TABLE_CREATE);
                db.execSQL(DOSES_TABLE_CREATE);
            } catch (Exception e) {
                Message.message(context,""+e);            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE_DOSES);
                db.execSQL(DROP_TABLE_MEDICATIONS);
                db.execSQL(DROP_TABLE_BELOVED);
                db.execSQL(DROP_TABLE_USERS);
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,""+e);            }
        }
    }
}