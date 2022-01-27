package com.tracec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.tracec.data.Patient;
import com.tracec.utils.DBOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbOpenHelper = new DBOpenHelper(this);
        tryShowExtra();
    }

    private void tryShowExtra() {
        try {
            Intent intent = getIntent();
            String pid = intent.getStringExtra("pid");
            String flag = intent.getStringExtra("flag");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            if(flag.equals("start")){
                Patient patient = dbOpenHelper.getPatient(pid);
                String mes="Warning!! Patient "+patient.getPatientid()+" "+patient.getSurname()+" "+patient.getGivenname()+" close to Casino. Please stop patient and call to patient";
                //insert record
                String starttime = df.format(curDate);
                boolean rs = dbOpenHelper.addRecord(pid,starttime);
                if(rs){
                   System.out.println("insert record success");
                    AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                            .setTitle("ALERT")//标题
                            .setMessage(mes)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }else{
                    System.out.println("insert record fail");
                }
            }
            else if(flag.equals("end")){
                //update record
                String endtime = df.format(curDate);
                int id=dbOpenHelper.getLastRecord(pid);
                boolean rs=dbOpenHelper.updateRecord(endtime,id);
                if(rs){
                    System.out.println("update record success");
                }else{
                    System.out.println("update record fail");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}