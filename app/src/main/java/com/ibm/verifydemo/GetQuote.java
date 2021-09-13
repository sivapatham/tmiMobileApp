package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ibm.verifydemo.ui.home.HomeFragment;

public class GetQuote extends AppCompatActivity {

    public static String first_name, last_name,email_id;
    public static boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_quote);
        loggedIn= HomeFragment.loggedIn;
        if(loggedIn){
            first_name=HomeFragment.first_name;
            last_name=HomeFragment.last_name;
            email_id=HomeFragment.email;

            EditText et_first_name = (EditText)findViewById(R.id.first_name);
            et_first_name.setText(first_name);
            et_first_name.setEnabled(false);

            EditText et_last_name = (EditText)findViewById(R.id.last_name);
            et_last_name.setText(last_name);
            et_last_name.setEnabled(false);

            EditText et_email_id = (EditText)findViewById(R.id.email_id);
            et_email_id.setText(email_id);
            et_email_id.setEnabled(false);

        }
    }
    public void goToChooseQuote(View v){

        EditText et_first_name = (EditText)findViewById(R.id.first_name);
        first_name = et_first_name.getText().toString();
        Log.d("first name",first_name);
        if(first_name.matches("")){
            Toast.makeText(GetQuote.this, "Please enter First Name", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText et_last_name = (EditText)findViewById(R.id.last_name);
        last_name = et_last_name.getText().toString();
        Log.d("last name",last_name);
        if(last_name.matches("")){
            Toast.makeText(GetQuote.this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText et_email_id = (EditText)findViewById(R.id.email_id);
        email_id = et_email_id.getText().toString();
        Log.d("Email id",email_id);
        if(email_id.matches("")){
            Toast.makeText(GetQuote.this, "Please enter Email id", Toast.LENGTH_SHORT).show();
            return;
        }




        //Go to choose quote
        Intent intent;
        intent = new Intent(GetQuote.this, ChooseQuote.class);
        startActivity(intent);
    }
    public void backProfile(View v){
        finish();
    }
}