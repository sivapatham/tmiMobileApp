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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.ibm.security.verifysdk.OAuthToken;
import com.ibm.security.verifysdk.VerifySdkException;
import com.ibm.verifydemo.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VerifyOtp extends AppCompatActivity implements TextWatcher {

    public String txnId;
    public String authtoken;
    public String uid;
    public String number;
    private EditText etPrev;
    private EditText etNext;

    public VerifyOtp(){
    }

    public VerifyOtp(EditText etNext, EditText etPrev) {
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
        setContentView(R.layout.activity_verify_otp);
        Intent intent = getIntent();
        txnId = intent.getStringExtra(ProfileActivity.MSG);
        authtoken = intent.getStringExtra(ProfileActivity.MSG1);
        uid = intent.getStringExtra(ProfileActivity.MSG2);
        number = intent.getStringExtra(ProfileActivity.MSG3);

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
    public void onClickVerifyOTP(View v) {
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

        Log.d("OTP",otp);

        RequestQueue queue = Volley.newRequestQueue(this);

        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("otp", otp);
            final String mRequestBody = jsonBody.toString();
            Log.d("HttpClient",mRequestBody);

            //For version 2
            //String concat_url="https://cloudidentity1234.ice.ibmcloud.com/v2.0/factors/smsotp/transient/verifications/"+txnId+"?returnJwt=true";
            String url_verification=TrustMeInsuranceApp.getSmsotpverifyUrl()+"/"+txnId+"?returnJwt=true";

            StringRequest sr1 = new StringRequest(Request.Method.POST, url_verification,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            afterVerify();
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
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody()  {
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
                    params.put("Content-Type","application/json;charset=UTF-8");
                    //params.put("Accept","text/html");
                    String bearer_token = "Bearer "+authtoken;
                    params.put("Authorization",bearer_token);
                    return params;
                }
            };

            sr1.setRetryPolicy(new DefaultRetryPolicy(5000,4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(sr1);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void afterVerify() {
        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("enabled", true);
            jsonBody.put("phoneNumber",number);
            jsonBody.put("userId",uid);
            final String mRequestBody = jsonBody.toString();

            //For version 2
            //String concat_url="https://cloudidentity1234.ice.ibmcloud.com/v2.0/factors/smsotp";
            String url_verification=TrustMeInsuranceApp.getEnrollSmsOtpUrl();

            StringRequest sr1 = new StringRequest(Request.Method.POST, url_verification,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if(jsonObj.has("id"))
                                    HomeFragment.mfa_Sms_txnid=jsonObj.getString("id");
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
                        }
                    })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody()  {
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
                    params.put("Content-Type","application/json;charset=UTF-8");
                    //params.put("Accept","text/html");
                    String bearer_token = "Bearer "+authtoken;
                    params.put("Authorization",bearer_token);
                    return params;
                }
            };
            queue.add(sr1);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void showDialog() {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("TrustMeInsurance")
                            .setMessage("Your new security factor is now enrolled and ready to be used the next time you log in.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Create intent for sending back to profile page
                                    Intent resultIntent = new Intent();
                                    boolean enroll_flag =true;
                                    resultIntent.putExtra("result",enroll_flag);
                                    setResult(RESULT_OK,resultIntent);
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
}
