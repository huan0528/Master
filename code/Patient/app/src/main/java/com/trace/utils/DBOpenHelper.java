package com.trace.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.trace.data.Casino;
import com.trace.data.Patient;

public class DBOpenHelper extends SQLiteOpenHelper{
    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    private SQLiteDatabase db;

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     */
    public DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
    }

    /**
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS clinicians(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clinicianid TEXT," +
                "clinicianname TEXT," +
                "clinicname TEXT," +
                "contactnumber TEXT," +
                "email TEXT," +
                "password TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS patients(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patientid TEXT," +
                "surname TEXT," +
                "givenname TEXT," +
                "password TEXT," +
                "birth TEXT," +
                "guardianname TEXT," +
                "guardianphone TEXT," +
                "clinicianid TEXT," +
                "clinicianname TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS casinos(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clinicianid TEXT," +
                "casinoname TEXT,"+
                "log TEXT,"+
                "lat TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS records(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patientid TEXT," +
                "starttime TEXT," +
                "endtime TEXT)");

        initDataBase(db);
    }

    private void initDataBase (SQLiteDatabase db) {
//        ContentValues values = new ContentValues();
//        values.put("clinicianid","C0001");
//        values.put("clinicianname","Sherry Chen");
//        values.put("clinicname","Flinders Medical Clinic");
//        values.put("contactnumber","0433721897");
//        values.put("email","chan1616@flinders.edu.au");
//        values.put("password","123456");
//        db.insert("clinicians", null, values);
//
//        ContentValues values1 = new ContentValues();
//        values1.put("patientid","P0001");
//        values1.put("surname","Roy");
//        values1.put("givenname","Lee");
//        values1.put("password","123456");
//        values1.put("birth","04/05/1992");
//        values1.put("guardianname","Roy");
//        values1.put("guardianphone","123456789");
//        values1.put("clinicianid","C0001");
//        values1.put("clinicianname","Sherry Chen");
//        db.insert("patients", null, values1);
        ContentValues values = new ContentValues();
        values.put("clinicianid","C0001");
        values.put("casinoname","Flinders university Tonsley");
        values.put("log","138.5721439");
        values.put("lat","-35.0080474");
        db.insert("casinos",null,values);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onCreate(db);
    }

    /**
     * 接下来写自定义的增删改查方法
     */

    //根据用户名和密码找用户，用于登录
    public boolean findPatientByNameAndPwd(String patientid,String password){
        String sql="select * from patients where patientid=? and password=?";
        Cursor cursor=db.rawQuery(sql, new String[]{patientid,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }

    public Patient getPatient(String patientid){
        Patient patient = null;
        Cursor cursor = db.query("patients", null, "patientid='"+patientid+"'", null, null, null, null);
        if (cursor.moveToNext()) {
            patient = new Patient();
            patient.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            patient.setPatientid(cursor.getString(cursor.getColumnIndex("patientid")));
            patient.setSurname(cursor.getString(cursor.getColumnIndex("surname")));
            patient.setGivenname(cursor.getString(cursor.getColumnIndex("givenname")));
            patient.setClinicianid(cursor.getString(cursor.getColumnIndex("clinicianid")));
            patient.setClinicianname(cursor.getString(cursor.getColumnIndex("clinicianname")));
            patient.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            patient.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
            patient.setGuardianname(cursor.getString(cursor.getColumnIndex("guardianname")));
            patient.setGuardianphone(cursor.getString(cursor.getColumnIndex("guardianphone")));
        }
        return patient;
    }

    public Casino getCasino(String clinicianid){
        Casino data = null;
        Cursor cursor = db.query("casinos",null,"clinicianid='"+clinicianid+"'",null,null,null,null);
        while (cursor.moveToNext()){
            data = new Casino();
            data.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setClicianid(cursor.getString(cursor.getColumnIndex("clinicianid")));
            data.setCasinoname(cursor.getString(cursor.getColumnIndex("casinoname")));
            data.setLog(cursor.getString(cursor.getColumnIndex("log")));
            data.setLat(cursor.getString(cursor.getColumnIndex("lat")));
        }
        return data;
    }
}
