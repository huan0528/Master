package com.tracec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tracec.adapter.PatientAdapter;
import com.tracec.data.Patient;
import com.tracec.utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText et_query;
    private ListView listView;
    private PatientAdapter adapter;
    private DBOpenHelper dbOpenHelper;
    private List<Patient> list = new ArrayList<>();
    private TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbOpenHelper = new DBOpenHelper(this);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);

        String cur_clinician = sp.getString("USERNAME","");

        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText("Welcome, "+cur_clinician);

        et_query = findViewById(R.id.et_query);

        findViewById(R.id.queryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tosearch();
            }
        });

        findViewById(R.id.advancedqueryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,QueryActivity.class));
            }
        });

        listView = findViewById(R.id.lv_rs);
        adapter = new PatientAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this,RecordActivity.class);
                intent.putExtra("id",list.get(i).getPatientid());
                startActivity(intent);
            }
        });
        tv_nodata = findViewById(R.id.tv_nodata);
    }

    private void tosearch(){
        String key = et_query.getText().toString().trim();
        if(key.equals("")){
            Toast.makeText(SearchActivity.this,"Please input patient id!",Toast.LENGTH_SHORT).show();
        }else{
            Patient patient = dbOpenHelper.getPatient(key);
            if(patient==null){
                list.clear();
                adapter.notifyDataSetChanged();
                listView.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }else{
                list.clear();
                list.add(patient);
                adapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
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
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
                break;
            case R.id.action_reg:
                Intent intent = new Intent(SearchActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1000);
                break;
            case R.id.action_search:
                startActivity(new Intent(SearchActivity.this,SearchActivity.class));
                break;
            case R.id.action_out:
                startActivity(new Intent(SearchActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}