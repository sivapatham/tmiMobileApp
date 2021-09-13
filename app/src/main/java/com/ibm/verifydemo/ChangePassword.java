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
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    public String access_token;
    public String uid;
    public  String prev_access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        access_token = intent.getStringExtra(ProfileActivity.MSG1);
        uid = intent.getStringExtra(ProfileActivity.MSG2);
        prev_access_token = intent.getStringExtra(ProfileActivity.MSG4);
    }

    //method for changing password
    public void changePwd(View v) {

        String currentPw, newPw, confirmPw;

        //Current Password
        TextView textView = findViewById(R.id.first_name);
        currentPw = textView.getText().toString();

        //New password
        TextView textView1 = findViewById(R.id.last_name);
        newPw = textView1.getText().toString();

        //Confirm Password
        TextView textView3 = findViewById(R.id.email_id);
        confirmPw = textView3.getText().toString();

        if(newPw.equals(confirmPw)){

            Log.d("MSG","password match");

            RequestQueue queue = Volley.newRequestQueue(this);
            try
            {
                JSONObject jsonBody = new JSONObject();
                JSONArray arr = new JSONArray();
                arr.put("urn:ietf:params:scim:schemas:ibm:core:2.0:ChangePassword");
                arr.put("urn:ietf:params:scim:schemas:extension:ibm:2.0:Notification");
                jsonBody.put("currentPassword", currentPw);
                jsonBody.put("newPassword",newPw);
                jsonBody.put("schemas",arr);
                JSONObject obj=new JSONObject();
                obj.put("notifyType", "EMAIL");
                obj.put("notifyPassword", true);
                obj.put("notifyManager", true);

                jsonBody.put("urn:ietf:params:scim:schemas:extension:ibm:2.0:Notification",obj);

                final String mRequestBody = jsonBody.toString();

                //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Me/password";
                final String url_sms= TrustMeInsuranceApp.getMePwdUrl();

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
                                Toast toast = Toast.makeText(ChangePassword.this, "There was an error, please try again.", Toast.LENGTH_LONG);
                                toast.show();
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
                        params.put("Content-Type","application/scim+json");
                        params.put("Accept","application/scim+json");
                        String bearer_token = "Bearer "+prev_access_token;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                builder.setTitle("TrustMeInsurance")
                        .setMessage("Password changed successfully")
                        .setPositiveButton("Return Home", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    public void backProfile(View v){
        finish();
    }


}