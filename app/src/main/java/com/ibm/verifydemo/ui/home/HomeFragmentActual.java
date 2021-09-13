package com.ibm.verifydemo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ibm.verifydemo.ChangePassword;
import com.ibm.verifydemo.MainActivity;
import com.ibm.verifydemo.R;
import com.ibm.verifydemo.TrustMeInsuranceApp;
import com.ibm.verifydemo.VerifyAPIService;
import com.ibm.verifydemo.VerifyOtp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HomeFragmentActual extends Fragment {

    public View root;
    public String txnId;
    public String authtoken;
    public static String uid;
    public String access_token;
    public static  String email,first_name,last_name;
    public String mfa_no;
    public static String tokenforfragments;
    public static String mfa_Sms_txnid;
    public static final String MSG ="com.ibm.verifydemo.txnId";
    public static final String MSG1 ="com.ibm.verifydemo.authtoken";
    public static final String MSG2 ="com.ibm.verifydemo.uid";
    public static final String MSG3 ="com.ibm.verifydemo.phone";
    public static final String MSG4 ="com.ibm.verifydemo.prev_accesstoken";
    boolean smsfactorpresent =false;
    public static boolean loggedIn = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        Intent intent = getActivity().getIntent();
        authtoken = intent.getStringExtra(MainActivity.MSG);
        tokenforfragments=authtoken;
        VerifyAPIService.setApp_access_token(intent.getStringExtra(MainActivity.MSG));

        //Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Code for getting Access Token
        access_token=generateAccessToken();
        loggedIn = true;

        TextView enrollTxt = (TextView) root.findViewById(R.id.enroll);
        enrollTxt.setOnClickListener(new View.OnClickListener()       {
            public void onClick(View v)     {
                String txt = enrollTxt.getText().toString();
                Log.d("DisEnroll",txt);
                if(txt.equals("Enroll")) {
                    Log.d("Enroll","in enroll");
                    onClickEnroll(v);
                }
                else{
                    Log.d("DisEnroll","in disenroll");
                    deleteSmsOtpFactor();
                }

            }
        });

        TextView changePwd = (TextView)root.findViewById(R.id.textView27);
        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(v);
            }
        });

        ConstraintLayout cntLayout = (ConstraintLayout) root.findViewById(R.id.linearLayout);
        cntLayout.setOnClickListener(new View.OnClickListener()    {
            public void onClick(View v)     {
                String txt = enrollTxt.getText().toString();
                Log.d("DisEnroll",txt);
                if(txt.equals("Enroll")) {
                    Log.d("Enroll","in enroll");
                    onClickEnroll(v);
                }
                else{
                    Log.d("DisEnroll","in disenroll");
                    deleteSmsOtpFactor();

                }
            }
        });

        Switch plcConsent = (Switch) root.findViewById(R.id.consent_plc2);
        plcConsent.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v)  {
                onClickUpdate(v);
            }
        });

        Switch kycConsent = (Switch) root.findViewById(R.id.consent_kyc2);
        kycConsent.setOnClickListener(new View.OnClickListener()    {
            public void onClick(View v)    {
                onClickUpdate(v);
            }
        });
        Button btnMobile = (Button) root.findViewById(R.id.mobile_btn);
        btnMobile.setOnClickListener(new View.OnClickListener()    {
            public void onClick(View v)    {
                Button btn = (Button)v;
                TextView txtProPhNo = (TextView) root.findViewById(R.id.profile_phone);
                if (">".equalsIgnoreCase(btn.getText().toString())) {
                    String updated_no = txtProPhNo.getText().toString();
                    if(isValid(updated_no)) {
                        //Create a json aobject
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("op","add");
                            obj.put("path","phoneNumbers");
                            JSONObject obj1 = new JSONObject();
                            obj1.put("type","mobile");
                            obj1.put("value",updated_no);
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
                        btn.setText("v");
                        txtProPhNo.setEnabled(false);
                        txtProPhNo.clearFocus();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "InValid Phone no", Toast.LENGTH_SHORT).show();
                    }

                } else if ("v".equalsIgnoreCase(btn.getText().toString())) {
                    btn.setText(">");
                    txtProPhNo.setEnabled(true);
                    txtProPhNo.requestFocus();
                }
            }
        });
        return root;
    }

    public  void deleteSmsOtpFactor() {
        Log.d("MSG","VerifyAPIService - deleteSmsOtpFactor method");
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        //String delSmsOtpFactorUrl = TrustMeInsuranceApp.getBaseVerifyURL() +  "/v2.0/factors/smsotp/" +mfa_Sms_txnid ;
        String delSmsOtpFactorUrl =TrustMeInsuranceApp.getDeleteFactorsUrl()+mfa_Sms_txnid;
        Log.e("MSG", "Calling deleteSmsOtpFactor URL - DELETE -" + delSmsOtpFactorUrl);
        StringRequest srRequest = new StringRequest(Request.Method.DELETE, delSmsOtpFactorUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        TextView enrollTxt = (TextView) root.findViewById(R.id.enroll);
                        enrollTxt.setText("Enroll");
                        enrollTxt.setClickable(true);
                        EditText ed= (EditText) root.findViewById(R.id.idv);
                        ed.setText("");
                        ed.setEnabled(true);
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
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/form-data");
                params.put("Authorization", "Bearer " + VerifyAPIService.getAccess_token());
                return params;
            }
        };
        //srRequest.setShouldCache(false);
        //queue.getCache().clear();
        queue.add(srRequest);
    }

    private void getConsents(String purposeid) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put(purposeid);

            jsonBody.put("purposeId", arr);
            jsonBody.put("subjectId",uid);
            jsonBody.put("isExternalSubject", false);
            final String mRequestBody = jsonBody.toString();
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com:443/dpcm/v1.0/privacy/data-subject-presentation";
            String url_consent = TrustMeInsuranceApp.getDspUrl();
            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if(jsonObj.has("consents")) {
                                    JSONObject consents = jsonObj.getJSONObject("consents");
                                    Iterator<String> keys = consents.keys();
                                    String key = "";
                                    while (keys.hasNext()) {
                                        key = keys.next();
                                    }
                                    if(consents.has(key)) {
                                        JSONObject data = consents.getJSONObject(key);
                                        if (data.has("state")) {
                                            int state = data.getInt("state");
                                            updateConsentSwitch(purposeid, state);
                                        }
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

    private void updateConsentSwitch(String purposeid, int state) {
        if(purposeid.equals("mobileKYCnew")){
            if(state==1)
                ((Switch)root.findViewById(R.id.consent_kyc2)).setChecked(true);
        }

        if(purposeid.equals("paperless_communication_email")){
            if(state==1)
                ((Switch)root.findViewById(R.id.consent_plc2)).setChecked(true);
        }
    }

    private boolean isValid(String updated_no) {
        String regex = "^[+]?[0-9]{10,13}$";
        return updated_no.matches(regex);
    }

    public void getMeInfo() {
        Log.d("MSG","check consent");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url_consent = TrustMeInsuranceApp.getMeUrl() + "?access_token="+authtoken;

        // prepare the Request
        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url_consent, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CCResponse", response.toString());
                        try {

                            //Mobile no update
                            if(response.has("phoneNumbers")){
                            JSONArray phone = response.getJSONArray("phoneNumbers");
                            for(int j=0;j<phone.length();j++){
                                JSONObject item = phone.getJSONObject(j);
                                Iterator<String> keys = item.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    String val = item.getString(key);
                                    if(val.equals("mobile")) {
                                        key=keys.next();
                                        if (key.equals("value")) {
                                            EditText profile_no = (EditText) root.findViewById(R.id.profile_phone);
                                            profile_no.setText(item.getString(key));
                                        }
                                    }

                                }

                            }}
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
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //final String url = "https://cloudidentity1234.ice.ibmcloud.com/oidc/endpoint/default/userinfo?access_token="+authtoken;
        String url = TrustMeInsuranceApp.getUserinfoUrl() + "?access_token="+authtoken;
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
                                TextView textView3 =root.findViewById(R.id.profile_phone);
                                textView3.setText(response.getString("mobile_number"));
                            }

                            //Enter UID
                            if(response.has("uid")) {
                                //TextView textView = root.findViewById(R.id.userid);
                                //textView.setText(response.getString("uid"));
                                uid = response.getString("uid");
                                VerifyAPIService.setLoggedin_uid(uid);
                            }

                            //Enter Fullname
                            if(response.has("name")) {
                                TextView textView1 = root.findViewById(R.id.fullname);
                                textView1.setText(response.getString("name"));
                                String arr[]=response.getString("name").split(" ");
                                first_name=arr[0];
                                last_name=arr[1];
                                Log.d("Name:",first_name+" "+last_name);
                            }

                            //Enter Email
                            if(response.has("email")) {
                                TextView textView2 = root.findViewById(R.id.email);
                                textView2.setText(response.getString("email"));
                                email=response.getString("email");
                            }

                            //Enter MFA_Email
                            if(response.has("email")) {
                                TextView textView4 = root.findViewById(R.id.mfa_email);
                                textView4.setText(response.getString("email"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Get Me Info
                        getMeInfo();

                        //Check for Consents
                        String purpose_id1="paperless_communication_email";
                        String purpose_id2="mobileKYCnew";
                        getConsents(purpose_id1);
                        getConsents(purpose_id2);

                        //check MFA Enrollment
                        getMFAEnrollments(uid,1);

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

    private void getMFAEnrollments(String uid, int i) {

        Log.d("MFA","Get MFA Enroll");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String uid_quote= '"'+uid+'"';
        String type = '"'+"smsotp"+'"';
        String url_consent = TrustMeInsuranceApp.getFactorsUrl() + "?access_token="+access_token+"&search=userId%3D"+uid_quote+"%26type%3D"+type;

        // prepare the Request
        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url_consent, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MFA", response.toString());
                        try {

                            if(response.has("factors")){
                                JSONArray factors = response.getJSONArray("factors");
                                if(factors.length()>0) {
                                    JSONObject jsonObject = factors.getJSONObject(0);
                                    if (jsonObject.has("id")) {
                                        smsfactorpresent = true;
                                        mfa_Sms_txnid = jsonObject.getString("id");
                                    }
                                    if (jsonObject.has("attributes")) {
                                        JSONObject att = jsonObject.getJSONObject("attributes");
                                        if (att.has("phoneNumber")){
                                            mfa_no = att.getString("phoneNumber");
                                        }
                                    }
                                }
                            }
                            if(smsfactorpresent && i==1){
                                TextView textView = root.findViewById(R.id.enroll);
                                textView.setText("(Disenroll)");
                                textView.setClickable(false);

                                EditText ed = (EditText) root.findViewById(R.id.idv);
                                ed.setText(mfa_no);
                                ed.setEnabled(false);
                            }

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


    public  String generateAccessToken() {
        Log.d("MSG","In generate token");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v1.0/endpoint/default/token";
        String url_sms = TrustMeInsuranceApp.getTokenUrl();

        StringRequest tokenRequest = new StringRequest(Request.Method.POST, url_sms,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            access_token =jsonObj.getString("access_token");

                            //Get User Info to display on profile page
                            getUserInfo();

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
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                params.put("client_id", TrustMeInsuranceApp.getApiClientId());
                params.put("client_secret",TrustMeInsuranceApp.getApiClientSecret());
                params.put("scope","openid");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(tokenRequest);
        return access_token;

    }

    private void updateAttributes(JSONArray operations) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:api:messages:2.0:PatchOp");
            jsonBody.put("Operations", operations);
            jsonBody.put("schemas",arr);
            final String mRequestBody = jsonBody.toString();
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/"+uid;
            String url_sms = TrustMeInsuranceApp.getUsersUrl() + "/"+ uid;

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("UpdateAttHttpClient", "success! response: " + response);

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
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";
            String url_consent = TrustMeInsuranceApp.getConsentsUrl();

            System.out.println("Calling API - " + url_consent);
            System.out.println("JSON Input to API - " + mRequestBody.toString());
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
        Switch swt = (Switch)v;
        System.out.println ( "Switch - " + swt.getId() + " - " + swt.isChecked());
        TextView textView4 = root.findViewById(R.id.mfa_email);
        String email = textView4.getText().toString();
        if ( R.id.consent_plc2 == swt.getId()) {
            if (swt.isChecked()) {
                consentCreation(email, "paperless_communication_email", 1, "3");
            } else {
                consentCreation(email, "paperless_communication_email", 2, "3");
            }
        } else if ( R.id.consent_kyc2 == swt.getId()) {
            //KYC
            if (swt.isChecked()) {
                consentCreation("####-####-####", "mobileKYCnew", 1, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
            } else {
                consentCreation("####-####-####", "mobileKYCnew", 2, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
            }
        }
    }

    public void onClickEnroll(View v) {
        EditText phone_mfa;
        phone_mfa=(EditText)root.findViewById(R.id.idv);
        System.out.println(phone_mfa.getText());
        //API for SMS OTP
        String number = phone_mfa.getText().toString();
        String randomPIN = ""+((int)(Math.random()*9000)+1000);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correlation", randomPIN);
            //For version 2
            jsonBody.put("phoneNumber",number);
            final String mRequestBody = jsonBody.toString();

            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/factors/smsotp/transient/verifications";
            String url_sms = TrustMeInsuranceApp.getSmsotpverifyUrl();

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                txnId =jsonObj.getString("id");
                                Intent intent;
                                intent = new Intent(getActivity().getApplicationContext(), VerifyOtp.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == getActivity().RESULT_OK){
                boolean enroll_flag = data.getBooleanExtra("result",false);
                if(enroll_flag){
                    TextView textView = root.findViewById(R.id.enroll);
                    textView.setText("(Disenroll)");
                    textView.setClickable(false);

                    EditText ed= (EditText) root.findViewById(R.id.idv);
                    String mfa_no = ed.getText().toString();
                    ed.setEnabled(false);
                }
            }
        }
    }

    public void changePassword(View v){
        Intent intent;
        intent = new Intent(getActivity().getApplicationContext(), ChangePassword.class);
        intent.putExtra(MSG1,access_token);
        intent.putExtra(MSG2,uid);
        intent.putExtra(MSG4,authtoken);
        startActivityForResult(intent,2);
    }

}