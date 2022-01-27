package com.tracec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tracec.adapter.PatientAdapter;
import com.tracec.data.Patient;
import com.tracec.utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_surname,et_givenname;
    private TextView tv_birth;
    private PatientAdapter adapter;
    private DBOpenHelper dbOpenHelper;
    private List<Patient> list = new ArrayList<>();
    private ListView lv_rs;
    private TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        dbOpenHelper = new DBOpenHelper(this);

        et_surname = findViewById(R.id.et_surname);
        et_givenname = findViewById(R.id.et_givenname);
        tv_birth = findViewById(R.id.tv_birth);
        tv_birth.setOnClickListener(this);
        findViewById(R.id.queryBtn).setOnClickListener(this);
        lv_rs = findViewById(R.id.lv_rs);
        adapter = new PatientAdapter(this,list);
        lv_rs.setAdapter(adapter);
        lv_rs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QueryActivity.this,RecordActivity.class);
                intent.putExtra("id",list.get(position).getPatientid());
                startActivity(intent);
            }
        });
        tv_nodata = findViewById(R.id.tv_nodata);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birth:
                Calendar cal = Calendar.getInstance();
                DatePickerDialog date = new DatePickerDialog(QueryActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        tv_birth.setText(day+"/"+(month+1)+"/"+year);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+1);
                date.show();
                break;
            case R.id.queryBtn:
                toquery();
                break;
        }
    }

    private void toquery(){
        String surname = et_surname.getText().toString().trim();
        String givenname = et_givenname.getText().toString().trim();
        String birth = tv_birth.getText().toString().trim();
        if(surname.equals("")&&birth.equals("")&&givenname.equals("")){
            Toast.makeText(QueryActivity.this,"Fill in at least one patient name and DOB!",Toast.LENGTH_SHORT).show();
        }else{
            List<Patient> templist=dbOpenHelper.queryPatient(surname,givenname,birth);
            list.clear();
            for(int i=0;i<templist.size();i++){
                list.add(templist.get(i));
            }
            adapter.notifyDataSetChanged();
            if(list.size()==0){
                tv_nodata.setVisibility(View.VISIBLE);
                lv_rs.setVisibility(View.GONE);
            }else{
                tv_nodata.setVisibility(View.GONE);
                lv_rs.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(QueryActivity.this,MainActivity.class));
                break;
            case R.id.action_reg:
                Intent intent = new Intent(QueryActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1000);
                break;
            case R.id.action_search:
                startActivity(new Intent(QueryActivity.this,SearchActivity.class));
                break;
            case R.id.action_out:
                startActivity(new Intent(QueryActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}