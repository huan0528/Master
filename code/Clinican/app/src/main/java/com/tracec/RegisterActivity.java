package com.tracec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tracec.data.Casino;
import com.tracec.data.Clinician;
import com.tracec.data.Patient;
import com.tracec.utils.DBOpenHelper;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_patientid;
    private EditText et_surname;
    private EditText et_givenname;
    private TextView tv_birth;
    private EditText et_guardianname;
    private EditText et_guardianphone;
    private EditText et_password;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbOpenHelper = new DBOpenHelper(this);

        et_patientid = findViewById(R.id.et_patientid);
        et_surname = findViewById(R.id.et_surname);
        et_givenname = findViewById(R.id.et_givenname);
        tv_birth = findViewById(R.id.tv_birth);
        et_guardianname = findViewById(R.id.et_guardianname);
        et_guardianphone = findViewById(R.id.et_guardianphone);
        tv_birth = findViewById(R.id.tv_birth);
        tv_birth.setOnClickListener(this);
        et_password = findViewById(R.id.et_password);

        findViewById(R.id.backBtn).setOnClickListener(this);
        findViewById(R.id.regBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birth:
                Calendar cal = Calendar.getInstance();
                DatePickerDialog date = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        tv_birth.setText(day+"/"+(month+1)+"/"+year);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+1);
                date.show();
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.regBtn:
                toreg();
                break;
        }
    }

    private void toreg(){
        String patientid = et_patientid.getText().toString().trim();
        String surname = et_surname.getText().toString().trim();
        String givenname = et_givenname.getText().toString().trim();
        String guardianname = et_guardianname.getText().toString().trim();
        String guardianphone = et_guardianphone.getText().toString().trim();
        String birth = tv_birth.getText().toString().trim();
        String password= et_password.getText().toString().trim();

        if(patientid.equals("")||surname.equals("")||givenname.equals("")||guardianname.equals("")||guardianphone.equals("")||birth.equals("")||password.equals("")){
            Toast.makeText(RegisterActivity.this,"All is not empty!",Toast.LENGTH_SHORT).show();
        }else{
            Patient patient = new Patient();
            patient.setPatientid(patientid);
            patient.setSurname(surname);
            patient.setGivenname(givenname);
            SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String cur_clinician = sp.getString("USERNAME","");
            Clinician clinician = dbOpenHelper.getClinician(cur_clinician);
            patient.setClinicianid(cur_clinician);
            patient.setClinicianname(clinician.getClinicianname());
            patient.setBirth(birth);
            patient.setPassword(password);
            patient.setGuardianname(guardianname);
            patient.setGuardianphone(guardianphone);

            int rs = dbOpenHelper.findPatient(patientid);
            if(rs>0){
                Toast.makeText(RegisterActivity.this,"The patient exists!",Toast.LENGTH_SHORT).show();
            }else{
                boolean flag = dbOpenHelper.addPatient(patient);
                if(flag){
                    Toast.makeText(RegisterActivity.this,"Register Success!",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,new Intent());
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"Register Fail!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}