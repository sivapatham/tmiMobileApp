package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ibm.verifydemo.ui.home.HomeFragment;
import com.ibm.verifydemo.ui.quote.QuoteFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class QuoteApproval extends AppCompatActivity {

    public String access_token = VerifyAPIService.access_token;
    public String uid = HomeFragment.uid;
    public String email = HomeFragment.email;
    public String quoteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_approval);
        String purposeId1 = "paperless_communication_email";
        String attributeId1 ="3";
        String attributeValue1 =email;
        getDUA(purposeId1,attributeId1,attributeValue1);
        TextView ds_email = findViewById(R.id.ds_email);
        ds_email.setText(email);

        String purposeId2 = "mobileKYCnew";
        String attributeId2 ="13982bf1-8c35-4b92-a1a2-94087b59dea6";
        String attributeValue2 ="####-####-####";
        getDUA(purposeId2,attributeId2,attributeValue2);
    }

    private void getDUA(String purposeId, String attributeId, String attributeValue) {
        RequestQueue queue = Volley.newRequestQueue(this);

        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("trace",false);
            JSONArray arr = new JSONArray();
            JSONObject jsonBody1=new JSONObject();
            jsonBody1.put("purposeId",purposeId);
            jsonBody1.put("accessTypeId","default");
            jsonBody1.put("attributeId",attributeId);
            jsonBody1.put("attributeValue", attributeValue);
            arr.put(jsonBody1);
            jsonBody.put("items",arr);
            jsonBody.put("isExternalSubject", false);
            jsonBody.put("subjectId",uid);
            final String mRequestBody = jsonBody.toString();
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";
            String url_consent = TrustMeInsuranceApp.getDuaUrl();
            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject obj =arr.getJSONObject(0);
                                JSONArray res = obj.getJSONArray("result");
                                JSONObject obj1 = res.getJSONObject(0);
                                TextView cs=(TextView)findViewById(R.id.consent_status);
                                TextView decision = findViewById(R.id.txt_decision);
                                TextView cs1=(TextView)findViewById(R.id.consent_status2);
                                TextView decision1 = findViewById(R.id.txt_decision3);

                                if(obj1.getBoolean("approved")){
                                    if(purposeId.equals("paperless_communication_email")) {
                                        cs.setText("Approved");
                                        decision.setText("Sending policy document through mail. ");
                                    }
                                    else {
                                        cs1.setText("Approved");
                                        decision1.setText("We are sharing your PII with 3rd party.You will be contacted shortly for background check");

                                    }

                                }
                                else{
                                    if(purposeId=="paperless_communication_email") {
                                        cs.setText("Disapproved");
                                        decision.setText("Sending policy document through post since consent is not active.");
                                    }
                                    else {
                                        cs1.setText("Disapproved");
                                        decision1.setText("Please do background check within a month");

                                    }

                                }
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
                    params.put("Accept","text/html");
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                Intent intent;
                finish();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}