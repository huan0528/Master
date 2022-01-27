package com.tracec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tracec.adapter.PatientAdapter;
import com.tracec.data.Patient;
import com.tracec.utils.DBOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private PatientAdapter adapter;
    private DBOpenHelper dbOpenHelper;
    private List<Patient> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbOpenHelper = new DBOpenHelper(this);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);

        String cur_clinician = sp.getString("USERNAME","");

        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText("Welcome, "+cur_clinician);

        list = dbOpenHelper.getAllPatient(cur_clinician);
        listView = findViewById(R.id.listview);
        adapter = new PatientAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                intent.putExtra("id",list.get(i).getPatientid());
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                break;
            case R.id.action_reg:
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1000);
                break;
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                break;
            case R.id.action_out:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Patient patient = dbOpenHelper.getLast();
            list.add(patient);
            adapter.notifyDataSetChanged();
        }
    }
}