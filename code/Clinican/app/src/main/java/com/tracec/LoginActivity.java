package com.tracec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tracec.utils.DBOpenHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private CheckBox mRememberCheck;
    private SharedPreferences sp;
    private DBOpenHelper dbOpenHelper;
    private String role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbOpenHelper = new DBOpenHelper(this);
        sp = getSharedPreferences("UserInfo", 0);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        mRememberCheck = findViewById(R.id.Login_Remember);

        if(sp.getBoolean("ISCHECK", false)) {
            mRememberCheck.setChecked(true);
            et_username.setText(sp.getString("USERNAME", ""));
            et_password.setText(sp.getString("PASSWORD", ""));
        }

        mRememberCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (mRememberCheck.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tologin();
            }
        });
    }

    public void tologin() {
        if (isUserNameAndPwdValid()) {
            String userName = et_username.getText().toString().trim();
            String userPwd = et_password.getText().toString().trim();

            boolean result = dbOpenHelper.findClinicianByNameAndPwd(userName, userPwd);
            if (result) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                if (mRememberCheck.isChecked()) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USERNAME", userName);
                    editor.putString("PASSWORD", userPwd);
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
				Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show();
			}
        }else {
            Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (et_username.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Username is not empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Password is not empty!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}