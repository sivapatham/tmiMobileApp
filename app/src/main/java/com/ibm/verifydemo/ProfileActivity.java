package com.ibm.verifydemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

    public String txnId;
    public String authtoken;
    public String uid;
    //public String clientId = "3c43de62-e7d6-4fa4-aff9-b8c7cf2d1bfe";
    public String clientId = TrustMeInsuranceApp.getApiClientId();
    //public String secret = "IihyiwNCl3";
    public String secret = TrustMeInsuranceApp.getApiClientSecret();
    public String access_token;
    public static final String MSG ="com.ibm.verifydemo.txnId";
    public static final String MSG1 ="com.ibm.verifydemo.authtoken";
    public static final String MSG2 ="com.ibm.verifydemo.uid";
    public static final String MSG3 ="com.ibm.verifydemo.phone";
    public static final String MSG4 ="com.ibm.verifydemo.prev_accesstoken";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        authtoken = intent.getStringExtra(MainActivity.MSG);
        //Log.d("myapp", authtoken+" Hi");
        System.out.println(authtoken);




        //Code for getting Access Token
        generateAccessToken();

        //Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //Get User Info to display on profile page
        getUserInfo();



        //Mobile No
        System.out.println("Mobile");
        EditText text = (EditText) findViewById(R.id.profile_phone);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("MSG","Here I am");
                String regex = "^[+]?[0-9]{10,13}$";
                String number= s.toString();

                String new_string = text.getText().toString();
                if(!new_string.equals(s)) {

                    if (number.matches(regex)) {
                        //do something
                        System.out.println("valid");
                        //Create a json aobject
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("op","add");
                            obj.put("path","phoneNumbers");
                            JSONObject obj1 = new JSONObject();
                            obj1.put("type","mobile");
                            obj1.put("value",new_string);
                            JSONArray arr = new JSONArray();
                            arr.put(obj1);
                            obj.put("value",arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Create a json array
                        JSONArray operations = new JSONArray();
                        operations.put(obj);
                        //Function for updating mobile no
                        updateAttributes(operations);
                    } else {
                        //do something
                        System.out.println("Enter a valid no");
                        Toast.makeText(ProfileActivity.this, "InValid Email or Phone no", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        //Check for Consents
        checkConsents();


    }

    public void checkConsents() {
        Log.d("MSG","consent creation");
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url_consent = "https://cloudidentity1234.ice.ibmcloud.com/v2.0/Me?access_token="+authtoken;

        // prepare the Request
        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url_consent, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Response", response.toString());
                        try {

                            JSONObject before_custom_att = response.getJSONObject("urn:ietf:params:scim:schemas:extension:ibm:2.0:User");
                            //Custom attributes is a JSONArray
                            JSONArray custom_att = before_custom_att.getJSONArray("customAttributes");
                            String prev_value="";
                            boolean aadhar_found=false;
                            for(int i=0;i<custom_att.length();i++)
                            {
                                JSONObject item = custom_att.getJSONObject(i);
                                //System.out.println(item.getString(""));

                                Iterator<String> keys = item.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();

                                    //System.out.println("prev1 "+ prev_value);
                                    //System.out.println("Key:"+key+" Value :"+item.get(key));

                                    if((key.equals("name")) && (item.get(key).equals("consentPaperless"))){
                                        //System.out.println("mila");

                                        //System.out.println("Mila "+ prev_value);
                                        if(prev_value.equals("true"))
                                            ((CheckBox)findViewById(R.id.consent_plc)).setChecked(true);



                                    }
                                    if((key.equals("name")) && (item.get(key).equals("consentaadhar"))){
                                        //System.out.println("mila aa");

                                        //System.out.println("Mila "+ prev_value);
                                        if(!(prev_value.equals("true")))
                                            ((CheckBox)findViewById(R.id.consent_kyc)).setChecked(true);
                                        aadhar_found=true;
                                        break;



                                    }
                                    if(aadhar_found)
                                        break;
                                    if(key.equals("values"))
                                    {
                                        //System.out.println("val: "+item.getJSONArray(key).getString(0));
                                        prev_value=item.getJSONArray(key).getString(0);
                                    }
                                    //System.out.println("prev "+ prev_value);



                                }


                            }






                            //Log.d("Response",response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest1);

    }

    //Populate the user info
    public void getUserInfo() {

        Log.d("MSG","getuserinfo");
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "https://cloudidentity1234.ice.ibmcloud.com/oidc/endpoint/default/userinfo?access_token="+authtoken;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {

                            if(response.has("mobile_number")){
                                System.out.println(response.getString("mobile_number"));
                                //Enter Mobile
                                TextView textView3 =findViewById(R.id.profile_phone);
                                textView3.setText(response.getString("mobile_number"));

                            }



                            //Enter UID
                            if(response.has("uid")) {
                                TextView textView = findViewById(R.id.userid);
                                textView.setText(response.getString("uid"));
                                uid = response.getString("uid");
                            }

                            //Enter Fullname
                            if(response.has("name")) {
                                TextView textView1 = findViewById(R.id.fullname);
                                textView1.setText(response.getString("name"));
                            }

                            //Enter Email
                            if(response.has("email")) {
                                TextView textView2 = findViewById(R.id.email);
                                textView2.setText(response.getString("email"));
                            }



                            //Enter MFA_Email
                            if(response.has("email")) {
                                TextView textView4 = findViewById(R.id.mfa_email);
                                textView4.setText(response.getString("email"));
                            }


                            //Log.d("Response",response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }


    public void generateAccessToken() {

        Log.d("MSG","In generate token");

        RequestQueue queue = Volley.newRequestQueue(this);



        final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/endpoint/default/token";

        StringRequest tokenRequest = new StringRequest(Request.Method.POST, url_sms,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("HttpClient",jsonObj.getString("access_token"));
                            access_token =jsonObj.getString("access_token");
                            Log.e("HttpClient","Hi" + access_token);
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
           /* @Override
            public String getBodyContentType() {
                //return "application/json; charset=utf-8";
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }*/

            /*@Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }*/

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }*/

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                params.put("client_id",clientId);
                params.put("client_secret",secret);
                params.put("scope","openid");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                //params.put("Accept","text/html");
                //String bearer_token = "Bearer "+authtoken;
                //params.put("Authorization",bearer_token);

                return params;
            }
        };
        queue.add(tokenRequest);


    }

    private void updateAttributes(JSONArray operations) {

        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:api:messages:2.0:PatchOp");
            jsonBody.put("Operations", operations);
            jsonBody.put("schemas",arr);
            final String mRequestBody = jsonBody.toString();


            final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/"+uid;

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);

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

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/

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

    //function for creating consent
    public void consentCreation(String attributeValue,String purposeId,int state,String attributeId)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("attributeValue", attributeValue);
            jsonBody.put("isGlobal",true);
            jsonBody.put("subjectId",uid);
            jsonBody.put("purposeId",purposeId);
            jsonBody.put("state",state);
            jsonBody.put("attributeId",attributeId);
            jsonBody.put("accessTypeId","default");
            jsonBody.put("isExternalSubject", false);



            final String mRequestBody = jsonBody.toString();

            //URL for version1
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/";

            //URL for version 2
            final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";

            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);


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

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/

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


    public void onClickUpdate(View v){

        //Paperless communication
        boolean isCheckedPC = ((CheckBox)findViewById(R.id.consent_plc)).isChecked();
        System.out.println("Wheter checked PC:"+ isCheckedPC);
        String attributeValue,purposeId="paperless_communication",attributeId="bdd5301f-0a18-4fc3-aea3-9a3e5be6b23e";
        int state;

        if(isCheckedPC){
            attributeValue="true";
            state=1;
            //Call function for creating consent
            consentCreation(attributeValue,purposeId,state,attributeId);
        }
        else
        {
            attributeValue="false";
            state=0;
            //Call function for creating consent
            consentCreation(attributeValue,purposeId,state,attributeId);
        }


        //KYC
        boolean isCheckedKYC = ((CheckBox)findViewById(R.id.consent_kyc)).isChecked();
        System.out.println("Whether checked KYC:"+ isCheckedKYC);

        if(isCheckedKYC){

            attributeValue="true";
            purposeId="pi_kyc";
            attributeId="9e7a7c32-18cf-4613-a7e8-581fe36b341a";
            state=1;
            //Call function for creating consent
            consentCreation(attributeValue,purposeId,state,attributeId);
        }
        else
        {
            attributeValue="false";
            purposeId="pi_kyc";
            attributeId="9e7a7c32-18cf-4613-a7e8-581fe36b341a";
            state=0;
            //Call function for creating consent
            consentCreation(attributeValue,purposeId,state,attributeId);
        }




    }

    public void onClickEnroll(View v) {

        EditText phone_mfa;
        phone_mfa=(EditText)findViewById(R.id.idv);
        System.out.println(phone_mfa.getText());


        //API for SMS OTP
        String number = phone_mfa.getText().toString();
        String randomPIN = ""+((int)(Math.random()*9000)+1000);


        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correlation", randomPIN);
            //For version 1
            //jsonBody.put("otpDeliveryMobileNumber",number);

            //For version 2
            jsonBody.put("phoneNumber",number);
            final String mRequestBody = jsonBody.toString();

            //URL for version1
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/";

            //URL for version 2
            final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/factors/smsotp/transient/verifications";

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                //Log.e("HttpClient",jsonObj.getString("id"));
                                txnId =jsonObj.getString("id");
                                Log.e("HttpClient","Hi" + txnId);
                                Intent intent;
                                intent = new Intent(ProfileActivity.this,VerifyOtp.class);
                                //String message = "Hi";
                                intent.putExtra(MSG,txnId);
                                intent.putExtra(MSG1,access_token);
                                intent.putExtra(MSG2,uid);
                                intent.putExtra(MSG3,number);
                                startActivityForResult(intent,1);
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

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/

                @Override
                protected Map<String,String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json");
                    //params.put("Accept","text/html");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK){
                boolean enroll_flag = data.getBooleanExtra("result",false);
                if(enroll_flag){
                    TextView textView = findViewById(R.id.enroll);
                    textView.setText("(Enrolled)");
                    textView.setClickable(false);

                    EditText ed= (EditText) findViewById(R.id.idv);
                    ed.setEnabled(false);


                }

            }

        }
    }

    public void changePassword(View v){

        Intent intent;
        intent = new Intent(ProfileActivity.this,ChangePassword.class);
        //String message = "Hi";

        intent.putExtra(MSG1,access_token);
        intent.putExtra(MSG2,uid);
        intent.putExtra(MSG4,authtoken);

        startActivityForResult(intent,2);

    }
}