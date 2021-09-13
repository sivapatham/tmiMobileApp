package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUp extends AppCompatActivity {


    public static final String MyCREDS = "MyCreds";
    public static final String baseURL = "baseURL";
    public static final String AppClientID = "AppClientID";
    public static final String AppClientSecret = "AppClientSecret";
    public static final String ApiClientID = "ApiClientID";
    public static final String ApiClientSecret = "ApiClientSecret";

    private static String baseVerifyURL ;
    private static String appClientId ;
    private static String appClientSecret ;
    private static String apiClientId ;
    private static String apiClientSecret ;

    public static boolean setUpDone = false;

    Button setup;
    Button clear;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_up);
        sharedPreferences = getSharedPreferences(MyCREDS, Context.MODE_PRIVATE);

        loadCreds();

        setup=(Button)findViewById(R.id.get_quote);

        clear = (Button) findViewById(R.id.button_Clr);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Write to SharedPrefrences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(baseURL,"");
                editor.putString(AppClientID,"");
                editor.putString(AppClientSecret,"");
                editor.putString(ApiClientID,"");
                editor.putString(ApiClientSecret,"");
                editor.putBoolean("setUpDone",false);
                editor.commit();

                loadCreds();

                updateCreds();

                Toast.makeText(SetUp.this,"Cleared data",Toast.LENGTH_LONG).show();
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //baseVerifyURL
                TextInputLayout textInputLayout = findViewById(R.id.TILbaseVerifyURL);
                String baseVerifyURL = textInputLayout.getEditText().getText().toString();
                Log.d("String",baseVerifyURL);
                if(baseVerifyURL.matches("")){
                    Toast.makeText(SetUp.this, "Please enter baseVerifyURL", Toast.LENGTH_SHORT).show();
                    return;
                }

                //appClientID
                TextInputLayout textInputLayout1 = findViewById(R.id.TILappClientId);
                String appClientId = textInputLayout1.getEditText().getText().toString();
                if(appClientId.matches("")){
                    Toast.makeText(SetUp.this, "Please enter appClientId", Toast.LENGTH_SHORT).show();
                    return;
                }

                //appClientSecret
                TextInputLayout textInputLayout2 = findViewById(R.id.TILappClientSecret);
                String appClientSecret = textInputLayout2.getEditText().getText().toString();
                if(appClientSecret.matches("")){
                    Toast.makeText(SetUp.this, "Please enter appClientSecret", Toast.LENGTH_SHORT).show();
                    return;
                }

                //apiClientId
                TextInputLayout textInputLayout3 = findViewById(R.id.TILapiClientId);
                String apiClientId = textInputLayout3.getEditText().getText().toString();
                if(apiClientId.matches("")){
                    Toast.makeText(SetUp.this, "Please enter apiClientId", Toast.LENGTH_SHORT).show();
                    return;
                }

                //apiClientSecret
                TextInputLayout textInputLayout4 = findViewById(R.id.TILapiClientSecret);
                String apiClientSecret = textInputLayout4.getEditText().getText().toString();
                if(apiClientSecret.matches("")){
                    Toast.makeText(SetUp.this, "Please enter apiClientSecret", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Write to SharedPrefrences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(baseURL,baseVerifyURL);
                editor.putString(AppClientID,appClientId);
                editor.putString(AppClientSecret,appClientSecret);
                editor.putString(ApiClientID,apiClientId);
                editor.putString(ApiClientSecret,apiClientSecret);
                editor.putBoolean("setUpDone",true);
                editor.commit();

                setUpDone = true;

                //Set all URLs
                updateCreds();
                showCustomAttCreateDialog();
                //showDialog();
            }
        });

    }

    private void getCreds(){
        sharedPreferences = getSharedPreferences(MyCREDS, Context.MODE_PRIVATE);

        baseVerifyURL = sharedPreferences.getString(baseURL,"");
        Log.d("SharedPref",baseVerifyURL);

        appClientId = sharedPreferences.getString(AppClientID,"");
        Log.d("SharedPref",appClientId);

        appClientSecret = sharedPreferences.getString(AppClientSecret,"");
        Log.d("SharedPref",appClientSecret);

        apiClientId = sharedPreferences.getString(ApiClientID,"");
        Log.d("SharedPref",apiClientId);

        apiClientSecret = sharedPreferences.getString(ApiClientSecret,"");
        Log.d("SharedPref",apiClientSecret);
    }

    private void updateCreds() {
        getCreds();
        TrustMeInsuranceApp.setBaseVerifyURL(baseVerifyURL);
        TrustMeInsuranceApp.setAppClientId(appClientId);
        TrustMeInsuranceApp.setAppClientSecret(appClientSecret);
        TrustMeInsuranceApp.setApiClientId(apiClientId);
        TrustMeInsuranceApp.setApiClientSecret(apiClientSecret);

        String tokenUrl = baseVerifyURL + "/oidc/endpoint/default/token";
        String authUrl = baseVerifyURL + "/oidc/endpoint/default/authorize";
        String userinfoUrl = baseVerifyURL + "/oidc/endpoint/default/userinfo";
        String meUrl = baseVerifyURL + "/v2.0/Me";
        String usersUrl = baseVerifyURL + "/v2.0/Users";
        String factorsUrl = baseVerifyURL + "/v2.0/factors";
        String deletefactorsUrl = baseVerifyURL + "/v2.0/factors/smsotp/";

        String consentsUrl = baseVerifyURL + "/dpcm/v1.0/privacy/consents";
        String dspUrl = baseVerifyURL + "/dpcm/v1.0/privacy/data-subject-presentation";
        String duaUrl = baseVerifyURL + "/dpcm/v1.0/privacy/data-usage-approval";
        String smsotpverifyUrl = baseVerifyURL + "/v2.0/factors/smsotp/transient/verifications";
        String exchangeJwtUrl = baseVerifyURL + "/v1.0/socialjwt/exchange";
        String strongAuthUrl = baseVerifyURL + "/v1.0/authenticators";
        String verifyStatusUrl = baseVerifyURL + "/v1.0/authenticators";
        String enrollSmsOtpUrl = baseVerifyURL+ "/v2.0/factors/smsotp";
        String pwdResetterUrl = baseVerifyURL + "/v1.0/usc/password/resetter/";
        String  mePwdUrl = baseVerifyURL + "/v2.0/Me/password";
        String createAttrUrl = baseVerifyURL +"/v2.0/Schema/attributes";
        String createCustAttrUrl = baseVerifyURL +"/v1.0/attributes/";

        TrustMeInsuranceApp.setTokenUrl(tokenUrl);
        TrustMeInsuranceApp.setAuthUrl(authUrl);
        TrustMeInsuranceApp.setUserinfoUrl(userinfoUrl);
        TrustMeInsuranceApp.setMeUrl(meUrl);
        TrustMeInsuranceApp.setUsersUrl(usersUrl);
        TrustMeInsuranceApp.setFactorsUrl(factorsUrl);
        TrustMeInsuranceApp.setDeleteFactorsUrl(deletefactorsUrl);
        TrustMeInsuranceApp.setEnrollSmsOtpUrl(enrollSmsOtpUrl);
        TrustMeInsuranceApp.setPwdResetterUrl(pwdResetterUrl);
        TrustMeInsuranceApp.setMePwdUrl(mePwdUrl);
        TrustMeInsuranceApp.setCreateAttrUrl(createAttrUrl);
        TrustMeInsuranceApp.setCreateCustAttrUrl(createCustAttrUrl);

        TrustMeInsuranceApp.setConsentsUrl(consentsUrl);
        TrustMeInsuranceApp.setDspUrl(dspUrl);
        TrustMeInsuranceApp.setDuaUrl(duaUrl);
        TrustMeInsuranceApp.setSmsotpverifyUrl(smsotpverifyUrl);
        TrustMeInsuranceApp.setExchangeJwtUrl(exchangeJwtUrl);
        TrustMeInsuranceApp.setStrongAuthUrl(strongAuthUrl);
        TrustMeInsuranceApp.setVerifyStatusUrl(verifyStatusUrl);
    }

    private void loadCreds() {
        getCreds();

        TextInputLayout textInputLayout = findViewById(R.id.TILbaseVerifyURL);
        textInputLayout.getEditText().setText(baseVerifyURL);

        TextInputLayout textInputLayout1 = findViewById(R.id.TILappClientId);
        textInputLayout1.getEditText().setText(appClientId);

        TextInputLayout textInputLayout2 = findViewById(R.id.TILappClientSecret);
        textInputLayout2.getEditText().setText(appClientSecret);

        TextInputLayout textInputLayout3 = findViewById(R.id.TILapiClientId);
        textInputLayout3.getEditText().setText(apiClientId);

        TextInputLayout textInputLayout4 = findViewById(R.id.TILapiClientSecret);
        textInputLayout4.getEditText().setText(apiClientSecret);

    }

    public void showCustomAttCreateDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetUp.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage("Please click the below button and create custom attributes setup"
                        )
                        .setPositiveButton("Setup Custom Attributes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                               //get access token
                                VerifyAPIService.generateApiAccessToken();

                                //create custom att
                                String attributes = "[{\n"+
                                       "\"name\": \"birthday\","+
                                        "\"type\": \"string\"\n"+
                                "},\n"+
                                "{\n"+
                                    "\"name\": \"consentPaperless\","+
                                        "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                     "\"name\": \"consentNotifications\","+
                                        "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"consentPromotions\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"quoteCount\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"carModel\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"carMake\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"carYear\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"ageHome\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"homeType\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"SSNorAadhar\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"consentAadhar\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"IDV\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"HomeQuoteAccept\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"CarQuoteAccept\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"CarQuoteName\","+
                                    "\"type\": \"string\"\n"+
                                "}\n,"+
                                "{\n"+
                                    "\"name\": \"HomeQuoteName\","+
                                    "\"type\": \"string\"\n"+
                                "}\n"+
                                "]";
                            String access_token = VerifyAPIService.getAccess_token();
                                Log.d("Steup access",access_token);
                                try {
                                    JSONArray att_array = new JSONArray(attributes);
                                    //createAttributes
                                    createAttributes(att_array,access_token);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void createAttributes(JSONArray att_array, String access_token) {
            Log.d("SetUp","Create Attributes");
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url_consent = TrustMeInsuranceApp.getCreateAttrUrl()+ "?access_token="+VerifyAPIService.getAccess_token()+"&filter=custom";

            // prepare the Request
            JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url_consent, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("NewUser", response.toString());
                            try {

                                if(response.has("Resources")){
                                    Log.d("NewUser","has res");
                                    JSONArray factors = response.getJSONArray("Resources");
                                    ArrayList<String> attrNames = new ArrayList<>();
                                    ArrayList<String> scimNames = new ArrayList<>();
                                    ArrayList<String> displayNames = new ArrayList<>();
                                    for(int i=0;i<factors.length();i++) {
                                        //Log.d("Att",i+" "+factors.getJSONObject(i).toString());
                                        attrNames.add(factors.getJSONObject(i).getString("name"));
                                        scimNames.add(factors.getJSONObject(i).getString("scimName"));
                                        displayNames.add(factors.getJSONObject(i).getString("displayName"));
                                    }

                                    int num = 1;
                                    for(int j=0;j<att_array.length();j++){
                                        JSONObject thisAttr = att_array.getJSONObject(j);
                                        //Log.d("Att",j+" "+thisAttr.toString());
                                        if(thisAttr.has("name")) {
                                            String name = thisAttr.getString("name");
                                            if ((!scimNames.contains(name)) && (!displayNames.contains(name))) {
                                                while (attrNames.contains("customAttribute" + num)) {
                                                    num++;
                                                }
                                                String freeAttribute = "customAttribute" + num;
                                                num++;
                                                createCustomAttr(thisAttr, freeAttribute, access_token);
                                            }
                                        }

                                    }
                                    showDialog();
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

    private void createCustomAttr(JSONObject thisAttr, String freeAttribute, String access_token) {
        Log.d("MSG","IN create custom attr");
        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:schemas:ibm:core:2.0:SchemaAttribute");
            jsonBody.put("schemas",arr);
            jsonBody.put("name",freeAttribute);
            jsonBody.put("description","Created for Demo App");
            jsonBody.put("displayName",thisAttr.getString("name"));
            jsonBody.put("type",thisAttr.getString("type"));
            jsonBody.put("scimName",thisAttr.getString("name"));
            jsonBody.put("multiValue",false);
            final String mRequestBody = jsonBody.toString();
            Log.d("Custom Att",mRequestBody);
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";
            String url_consent = TrustMeInsuranceApp.getCreateAttrUrl();
            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Custom Attr", "success! response: " + response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                            //error.printStackTrace();
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

    public void showDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetUp.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage("One time setup done. You are ready to login now"
                        )
                        .setPositiveButton("Return to Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(SetUp.this,MainActivity.class);
                                startActivity(resultIntent);

                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    public void backLogin(View v){
        Intent intent = new Intent(SetUp.this,MainActivity.class);
        startActivity(intent);
    }



}