package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Spinner spUnit;
    Firebase database = new Firebase();
    EditText etPassword;
    TextView tvAdminLogin;
    boolean loginSuccess = false;
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_UNIT = "unit";
    private final String KEY_EMPLOYEE_ID = "employeeid";
    private final String KEY_EMPLOYEE_NAME = "employeename";
    private final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        spUnit = (Spinner) findViewById(R.id.spUnit);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvAdminLogin = (TextView) findViewById(R.id.tvAdminLogin);

        mPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        if (mPreferences.contains(KEY_EMPLOYEE_ID) && mPreferences.contains(KEY_PASSWORD) && mPreferences.contains(KEY_EMPLOYEE_NAME)) {
            Intent i = new Intent(MainActivity.this, AdminMenu.class);
            startActivity(i);
            finish();
        }

        if (mPreferences.contains(KEY_UNIT) && mPreferences.contains(KEY_PASSWORD)) {
            Intent i = new Intent(MainActivity.this, ResidenceMenu.class);
            startActivity(i);
            finish();
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.unit));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(unitAdapter);

        tvAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AdminLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getData("users", null, new com.example.residentialmanagementapplication.MyCallback() {
                    @Override
                    public void returnData(ArrayList<Map<String, Object>> docList) {
                        Log.d("firebase example", docList.toString());
                        ArrayList<String> list = new ArrayList<>();

                        for (Map<String, Object> map : docList) {
                            if (map.get("unit").toString().equals(spUnit.getSelectedItem().toString()) && map.get("password").toString().equals(etPassword.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, ResidenceMenu.class);
                                startActivity(i);
                                finish();
                                loginSuccess = true;

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(KEY_UNIT, spUnit.getSelectedItem().toString());
                                editor.putString(KEY_PASSWORD, etPassword.getText().toString());
                                editor.apply();
                            }
//                            Log.d("firebase 12",map.toString());
//                            Log.d("firebase 12", (String) map.get("firstName"));
                            //list.add(map.get("firstName").toString() + " " + map.get("lastName").toString());
                        }
                        if (loginSuccess == false) {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}