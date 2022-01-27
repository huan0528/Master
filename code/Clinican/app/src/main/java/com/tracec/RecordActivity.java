package com.tracec;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tracec.adapter.RecordAdapter;
import com.tracec.data.ExcelData;
import com.tracec.data.Patient;
import com.tracec.data.Record;
import com.tracec.utils.DBOpenHelper;
import com.tracec.utils.ExcelUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {

    private DBOpenHelper dbOpenHelper;
    private List<Record> list = new ArrayList<>();
    private RecordAdapter adapter;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        dbOpenHelper = new DBOpenHelper(this);

        String pid=getIntent().getStringExtra("id");
        list = dbOpenHelper.getAllRecord(pid);
        patient = dbOpenHelper.getPatient(pid);

        TextView tv_id = findViewById(R.id.tv_id);
        tv_id.setText("Patient ID : "+patient.getPatientid());

        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText("Patient Name : "+patient.getSurname()+" "+patient.getGivenname());

        TextView tv_dob = findViewById(R.id.tv_dob);
        tv_dob.setText("Patient DOB : "+patient.getBirth());

        TextView tv_guardianname=findViewById(R.id.tv_guardianname);
        tv_guardianname.setText("Guardian Name : "+patient.getGuardianname());

        TextView tv_guardianphone = findViewById(R.id.tv_guardianphone);
        tv_guardianphone.setText("Guardian Phone : "+patient.getGuardianphone());

        TextView tv_stat = findViewById(R.id.tv_stat);
        tv_stat.setText(list.size()+" times close to casino,"+"\n" +list.size()+" times trigger to warn patient.");

        ListView listView=findViewById(R.id.listview);
        adapter = new RecordAdapter(this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recordmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(RecordActivity.this,MainActivity.class));
                break;
            case R.id.action_reg:
                Intent intent = new Intent(RecordActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1000);
                break;
            case R.id.action_search:
                startActivity(new Intent(RecordActivity.this,SearchActivity.class));
                break;
            case R.id.action_report:
                toreport();
                break;
            case R.id.action_out:
                startActivity(new Intent(RecordActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toreport(){
        List<ExcelData> excellist = dbOpenHelper.getExcelData(patient.getPatientid());
        if(excellist.size()==0){
            Toast.makeText(RecordActivity.this,"No Record for Report!",Toast.LENGTH_LONG).show();
        }else {
            try {
                ExcelUtil.writeExcel(RecordActivity.this,
                        excellist, "report_" + new Date().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}