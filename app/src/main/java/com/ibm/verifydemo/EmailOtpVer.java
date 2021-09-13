package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EmailOtpVer extends AppCompatActivity implements TextWatcher {


    public String txnId;
    public String authtoken;
    public String uid;
    public String email;
    public static boolean newUser;

    public static final String MSG ="com.ibm.verifydemo.txnId";
    public static final String MSG1 ="com.ibm.verifydemo.authtoken";
    public static final String MSG3 ="com.ibm.verifydemo.otp";

    private EditText etPrev;
    private EditText etNext;

    public EmailOtpVer(){

    }

    public EmailOtpVer(EditText etNext, EditText etPrev) {
        this.etPrev = etPrev;
        this.etNext = etNext;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (text.length() == 1)
            etNext.requestFocus();
        else if (text.length() == 0)
            etPrev.requestFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email_otp_ver);

        Intent intent = getIntent();

        txnId = intent.getStringExtra("txnId");
        authtoken = intent.getStringExtra("access_token");
        newUser = intent.getBooleanExtra("NewUser",false);

        EditText e1 = (EditText) findViewById(R.id.otp1);
        EditText e2 = (EditText) findViewById(R.id.otp2);
        EditText e3 = (EditText) findViewById(R.id.otp3);
        EditText e4 = (EditText) findViewById(R.id.otp4);
        EditText e5 = (EditText) findViewById(R.id.otp5);
        EditText e6 = (EditText) findViewById(R.id.otp6);

        e1.addTextChangedListener(new VerifyOtp(e2, e1));
        e2.addTextChangedListener(new VerifyOtp(e3, e1));
        e3.addTextChangedListener(new VerifyOtp(e4, e2));
        e4.addTextChangedListener(new VerifyOtp(e5, e3));
        e5.addTextChangedListener(new VerifyOtp(e6, e4));
        e6.addTextChangedListener(new VerifyOtp(e6, e5));
    }

    //Function for Verifying OTP
    public void onClickVerifyEmailOTP(View v) {

        //Verify OTP
        String otp = "";

        EditText[] otp_digits = new EditText[]{
                (EditText) findViewById(R.id.otp1),
                (EditText) findViewById(R.id.otp2),
                (EditText) findViewById(R.id.otp3),
                (EditText) findViewById(R.id.otp4),
                (EditText) findViewById(R.id.otp5),
                (EditText) findViewById(R.id.otp6)

        };
        for (int x = 0; x < otp_digits.length; x++) {
            otp += otp_digits[x].getText().toString();
        }
        Log.d("Otp: " , otp);

        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = new Intent(EmailOtpVer.this,ResetPwd.class);
        intent.putExtra(MSG,txnId);
        intent.putExtra(MSG1,authtoken);
        intent.putExtra(MSG3,otp);
        startActivity(intent);
    }

    public void backLogin(View v){
        Intent intent = new Intent(EmailOtpVer.this,MainActivity.class);
        startActivity(intent);
    }
}