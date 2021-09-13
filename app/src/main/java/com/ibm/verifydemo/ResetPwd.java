package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResetPwd extends AppCompatActivity {

    public String txnId;
    public String authtoken;
    public String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_pwd);
        Intent intent = getIntent();
        txnId = intent.getStringExtra(EmailOtpVer.MSG);
        authtoken = intent.getStringExtra(EmailOtpVer.MSG1);
        otp = intent.getStringExtra(EmailOtpVer.MSG3);
    }

    //method for changing password
    public void changePwd(View v) {
        String currentPw, newPw, confirmPw;

        //New password
        TextView textView1 = findViewById(R.id.last_name);
        newPw = textView1.getText().toString();

        //Confirm Password
        TextView textView3 = findViewById(R.id.email_id);
        confirmPw = textView3.getText().toString();

        if(newPw.equals(confirmPw)){


            RequestQueue queue = Volley.newRequestQueue(this);
            try
            {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("otp",otp);
                jsonBody.put("password",newPw);
                final String mRequestBody = jsonBody.toString();

                //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/usc/password/resetter/"+txnId;
                final String url_sms=TrustMeInsuranceApp.getPwdResetterUrl()+txnId;

                StringRequest sr = new StringRequest(Request.Method.PUT, url_sms,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("HttpClient", "success! response: " + response);
                                if(EmailOtpVer.newUser){
                                    ChooseQuote.new_user=false;
                                    /*SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    String curr = databaseDateTimeFormate.format(new Date());
                                    Log.d("Time",curr);
                                    //Update lastLogin
                                    JSONObject obj1 = new JSONObject();
                                    try {
                                        obj1.put("op","add");
                                        obj1.put("path","urn:ietf:params:scim:schemas:extension:ibm:2.0:User:twoFactorAuthentication");
                                        obj1.put("value",true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //Create a json array
                                    JSONArray operations1 = new JSONArray();
                                    operations1.put(obj1);
                                    //Function for updating quote realted parameters
                                    updateAttributes(operations1);*/
                                    authenticateUser(MainActivity.username,newPw);
                                }
                                else
                                showDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("HttpClient", "error: " + error.toString());
                                error.printStackTrace();
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
                        String bearer_token = "Bearer "+authtoken;
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
        else{
            showToast();
        }

    }

    public void showToast(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getBaseContext(), "New password or old passwords did not match, please try again.", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void showDialog() {


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwd.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage("Your password has been changed.Please go back to login screen and login with the new password."
                        )
                        .setPositiveButton("Return to Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(ResetPwd.this,MainActivity.class);
                                startActivity(resultIntent);

                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void backLogin(View v){
        Intent intent = new Intent(ResetPwd.this,MainActivity.class);
        startActivity(intent);
    }
    private void updateAttributes(JSONArray operations) {
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:api:messages:2.0:PatchOp");
            jsonBody.put("Operations", operations);
            jsonBody.put("schemas",arr);
            final String mRequestBody = jsonBody.toString();
            Log.d("LastLoginBody",mRequestBody);
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/"+uid;
            String url_sms = TrustMeInsuranceApp.getUsersUrl() + "/"+ MainActivity.uid;

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("UpdateAttHttpClient", "success! response: " + response);
                            showDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("updateHttpClient", "error: " + error.toString());
                            error.printStackTrace();
                        }
                    })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

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
                    params.put("Content-Type","application/scim+json");
                    params.put("X-HTTP-Method-Override","PATCH");
                    //params.put("Accept","text/html");
                    String bearer_token = "Bearer "+VerifyAPIService.getAccess_token();
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
    private void authenticateUser(String usr, String pwd) {

        Log.d("MSG","Authenticate user");

        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:schemas:ibm:core:2.0:AuthenticateUser");
            jsonBody.put("userName", usr);
            jsonBody.put("password",pwd);
            jsonBody.put("schemas",arr);

            final String mRequestBody = jsonBody.toString();

            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/authentication?method="compare"&returnUserRecord=true";
            final String url_sms= TrustMeInsuranceApp.getUsersUrl()+"/authentication?method="+"\"compare\""+"&returnUserRecord=true";

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            showDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                            error.printStackTrace();
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
                    //params.put("Accept","application/scim+json");
                    String bearer_token = "Bearer "+authtoken;
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
}