package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChooseQuote extends AppCompatActivity {

    public static boolean new_user=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_quote);
    }

    public void backProfile(View view) {
        Intent intent = new Intent(ChooseQuote.this,MainActivity.class);
        startActivity(intent);
    }
    public void goToCarDetails(View v){
        Intent intent;
        intent = new Intent(ChooseQuote.this, CarQuoteDetails.class);
        startActivity(intent);
    }

    public void goToHomeDetails(View v){
        Intent intent;
        intent = new Intent(ChooseQuote.this, HomeQuoteDetails.class);
        startActivity(intent);
    }

}