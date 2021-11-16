package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class AdminLogin extends AppCompatActivity {

    Button btnLoginAdminLogin;
    Firebase database = new Firebase();
    EditText etPasswordAdminLogin, etEmployeeNumberAdminLogin;
    TextView tvResidenceLogin;
    boolean loginSuccessAdminLogin = false;
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_EMPLOYEE_ID = "employeeid";
    private final String KEY_EMPLOYEE_NAME = "employeename";
    private final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        btnLoginAdminLogin = (Button) findViewById(R.id.btnLoginAdminLogin);
        etEmployeeNumberAdminLogin = (EditText) findViewById(R.id.etEmployeeNumberAdminLogin);
        etPasswordAdminLogin = (EditText) findViewById(R.id.etPasswordAdminLogin);
        tvResidenceLogin = (TextView) findViewById(R.id.tvResidenceLogin);

        mPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        if (mPreferences.contains(KEY_EMPLOYEE_ID) && mPreferences.contains(KEY_PASSWORD) && mPreferences.contains(KEY_EMPLOYEE_NAME)) {
            Intent i = new Intent(AdminLogin.this, AdminMenu.class);
            startActivity(i);
            finish();
        }

        tvResidenceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminLogin.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLoginAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getData("employee", null, new com.example.residentialmanagementapplication.MyCallback() {
                    @Override
                    public void returnData(ArrayList<Map<String, Object>> docList) {
                        Log.d("firebase example",docList.toString());
                        ArrayList<String> list = new ArrayList<>();

                        for (Map<String, Object> map : docList){
                            if (map.get("employeeID").toString().equals(etEmployeeNumberAdminLogin.getText().toString()) && map.get("password").toString().equals(etPasswordAdminLogin.getText().toString()))
                            {
                                Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AdminLogin.this, AdminMenu.class);
                                startActivity(i);
                                finish();
                                loginSuccessAdminLogin = true;

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(KEY_EMPLOYEE_ID, etEmployeeNumberAdminLogin.getText().toString());
                                editor.putString(KEY_PASSWORD, etPasswordAdminLogin.getText().toString());
                                editor.putString(KEY_EMPLOYEE_NAME, map.get("employeeName").toString());
                                editor.apply();
                            }
//                            Log.d("firebase 12",map.toString());
//                            Log.d("firebase 12", (String) map.get("firstName"));
                            //list.add(map.get("firstName").toString() + " " + map.get("lastName").toString());
                        }
                        if(loginSuccessAdminLogin == false)
                        {
                            Toast.makeText(AdminLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}