package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    public String email;
    public String access_token="", uid;
    public String clientId = TrustMeInsuranceApp.getApiClientId();
    public String secret = TrustMeInsuranceApp.getApiClientSecret();

    public static final String MSG ="com.ibm.verifydemo.txnId";
    public static final String MSG1 ="com.ibm.verifydemo.authtoken";
    public static final String MSG2 ="com.ibm.verifydemo.uid";
    public static final String MSG3 ="com.ibm.verifydemo.email";
    public static final String MSG4 ="com.ibm.verifydemo.prev_accesstoken";
    public static final String MSG5 ="com.ibm.verifydemo.newUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        access_token = VerifyAPIService.getAccess_token();
    }

    public void resetPwd(View v){
        //get email
        EditText text = (EditText) findViewById(R.id.email_id);
        email = text.getText().toString();
        //Send EmailOTP via Password Resetter
        generateEmailOTPResetPwd();
    }

    public void generateEmailOTPResetPwd() {
        Log.d("MSG","email otp generate");
        String randomPIN = ""+((int)(Math.random()*9000)+1000);

        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userName",email);
            JSONArray arr = new JSONArray();
            JSONObject obj = new JSONObject();
            JSONObject obj1 = new JSONObject();
            obj1.put("correlation", randomPIN);
            obj.put("data",obj1);
            obj.put("method","emailotp");
            arr.put(obj);
            jsonBody.put("steps",arr);
            jsonBody.put("stateId","default");
            final String mRequestBody = jsonBody.toString();

            //URL for version 2
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/usc/password/resetter";
            final String url_sms=TrustMeInsuranceApp.getPwdResetterUrl();

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            //go to the Email OTP Verify page
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String txnId =jsonObj.getString("trxId");
                                Intent intent;
                                intent = new Intent(ForgotPassword.this,EmailOtpVer.class);
                                intent.putExtra("txnId",txnId);
                                intent.putExtra("access_token",access_token);
                                intent.putExtra("NewUser",false);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                            error.printStackTrace();
                            //Retry otp generation
                        }
                    })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Map<String,String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json");
                    String bearer_token = "Bearer "+access_token;
                    params.put("Authorization",bearer_token);
                    return params;
                }
            };
            queue.add(sr);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void backLogin(View v){
        Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
        startActivity(intent);
    }
}