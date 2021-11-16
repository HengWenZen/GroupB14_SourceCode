package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    Button btnChangePassword;
    CheckBox cbShowPassword;
    boolean changePassword = false;
    Firebase database = new Firebase();

    public final String SHARED_PREF = "myPreferences";
    public final String KEY_UNIT = "unit";
    public final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = (EditText) findViewById(R.id.etCurrentPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnChangePassword = (Button) findViewById(R.id.btnConfirm);
        cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String spUnit = mPreferences.getString(KEY_UNIT, "");
        String etPassword = mPreferences.getString(KEY_PASSWORD, "");

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etCurrentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    etCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();

                if ((etCurrentPassword.getText().toString().equals("")) || (etNewPassword.getText().toString().equals("")) || (etConfirmPassword.getText().toString().equals(""))){
                    Toast.makeText(ChangePassword.this, "Please Fill In All Field !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString()))
                    {
                        Toast.makeText(ChangePassword.this, "Please Enter the New Password Again Correctly!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        database.getData("users", null, new MyCallback() {
                            @Override
                            public void returnData(ArrayList<Map<String, Object>> docList) {
                                Log.d("firebase example", docList.toString());
                                ArrayList<String> list = new ArrayList<>();

                                for (Map<String, Object> map : docList) {
                                    if (map.get("unit").toString().equals(spUnit) && map.get("password").toString().equals(currentPassword)) {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("password", newPassword);
                                        database.updData("users", user, map.get("id").toString());
                                        Toast.makeText(ChangePassword.this, "Password Changed Successfully...", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ChangePassword.this, ResidenceMenu.class);
                                        startActivity(i);
                                        finish();
                                        changePassword = true;
                                    }
                                }
                                if(changePassword == false)
                                {
                                    Toast.makeText(ChangePassword.this, "Incorrect Current Password....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}