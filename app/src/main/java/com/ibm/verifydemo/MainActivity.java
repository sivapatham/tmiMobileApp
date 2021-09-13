package com.ibm.verifydemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ibm.security.verifysdk.AuthenticationMethod;
import com.ibm.security.verifysdk.AuthenticatorContext;
import com.ibm.security.verifysdk.CloudAuthenticator;
import com.ibm.security.verifysdk.CloudQRLoginScan;
import com.ibm.security.verifysdk.CloudQRScan;
import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.HmacAlgorithm;
import com.ibm.security.verifysdk.HotpGeneratorContext;
import com.ibm.security.verifysdk.IAuthenticator;
import com.ibm.security.verifysdk.IMfaAuthenticator;
import com.ibm.security.verifysdk.IQRScanResult;
import com.ibm.security.verifysdk.IResultCallback;
import com.ibm.security.verifysdk.KeyStoreHelper;
import com.ibm.security.verifysdk.NetworkHandler;
import com.ibm.security.verifysdk.OAuthContext;
import com.ibm.security.verifysdk.OAuthToken;
import com.ibm.security.verifysdk.OtpQRScan;
import com.ibm.security.verifysdk.SignatureAuthenticationMethod;
import com.ibm.security.verifysdk.TotpAuthenticationMethod;
import com.ibm.security.verifysdk.UIQRScanView;
import com.ibm.security.verifysdk.VerifySdkException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MSG ="com.ibm.verifydemo.token";
    public static final String MSG1 ="com.ibm.verifydemo.authtoken";
    public static final String MSG5 ="com.ibm.verifydemo.newUser";
    private static final int FORGETPWD_REQUEST_ID = 20;
    private static final int SCANQR_REQUEST_ID = 24;
    private static final int GOOGLE_AUTHCODE_REQUEST_ID = 50;
    private static final int GOOGLE_OAUTH_REQUEST_ID = 55;
    private static final boolean IGNORE_SSL = true;
    public static final String MyCREDS = "MyCreds";
    public static final String baseURL = "baseURL";
    public static final String AppClientID = "AppClientID";
    public static final String AppClientSecret = "AppClientSecret";
    public static final String ApiClientID = "ApiClientID";
    public static final String ApiClientSecret = "ApiClientSecret";
    public static String access_token,api_access_token;
    public static  String uid,username;
    public String EULAPurposeId="ea7e3b65-218c-42cf-96c4-a3783feb2a8a";
    public int version;
    public String lastModifiedTime;
    public String ref;

    private OAuthToken token;
    private String gooAccessToken;
    private String gooIdToken;
    GoogleSignInClient mGoogleSignInClient;
    View v;
    TextView clear;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ContextHelper.sharedInstance().setContext(TrustMeInsuranceApp.getContext());
        NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        SharedPreferences sharedPreferences = getSharedPreferences(MyCREDS,Context.MODE_PRIVATE);
        boolean setUpDone = sharedPreferences.getBoolean("setUpDone",false);

        if(setUpDone){
            VerifyAPIService.generateApiAccessToken();
        }
        else{
            showSetUpDialog("Please complete the SetUp first and then come back to the login screen");

        }





        api_access_token=VerifyAPIService.getAccess_token();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(TrustMeInsuranceApp.getGoogleClientId())
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        ImageButton gsignInButton = findViewById(R.id.gsign_in_button);
        gsignInButton.setOnClickListener(MainActivity.this);
    }

    public void performLoginWithCreds(View v) {
        final String usr = ((EditText)findViewById(R.id.username)).getText().toString();
        final String pwd = ((EditText)findViewById(R.id.password)).getText().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("client_secret", TrustMeInsuranceApp.getAppClientSecret());
        NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        if (usr.isEmpty()) {
            showToast("Username is required");
        } else {

            //Check if new user
            /*if(ChooseQuote.new_user){
                Log.d("MA","New user");
                //Authenticate temporary password
                authenticateUser(usr,pwd);

                //Send reset pwd link ... Reset pwd ... Come back and login again...not natural

            }*/

                Log.d("MA", "Old user");

                OAuthContext.sharedInstance().authorize(TrustMeInsuranceApp.getTokenUrl(), TrustMeInsuranceApp.getAppClientId(), usr, pwd, map,
                        IGNORE_SSL, new IResultCallback<OAuthToken>() {
                            public void handleResult(OAuthToken oAuthToken, VerifySdkException e) {
                                if (oAuthToken != null) {
                                    token = oAuthToken;
                                    if (token == null) {
                                        showToast("Something went wrong");
                                    }
                                    access_token = token.getAccessToken();
                                    getDUA(EULAPurposeId);

                                } else {
                                    showToast(e.toString());
                                }
                            }
                        });

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
                    String bearer_token = "Bearer "+api_access_token;
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

    public void performForgetPwd(View v){
        Intent intent = new Intent(this,ForgotPassword.class);
        this.startActivityForResult(intent, FORGETPWD_REQUEST_ID);

    }

    private void performQRScan(View v) {
        final PackageManager pm = getPackageManager();
        boolean verifyAppInstalled = false;
        try {
            pm.getPackageInfo("com.ibm.security.verifyapp", PackageManager.GET_ACTIVITIES);
            verifyAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            verifyAppInstalled = false;
        }

        if ( verifyAppInstalled ) {
            Log.e("MSG", "Trying to invoke Verify app");
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.ibm.security.verifyapp");
            startActivity(LaunchIntent);
        } else {
            showDialog("IBM Security Verify App NOT installed in this device! Please secure your apps with Verify app...");
        }
    }

    private void performGoogleSignIn(View v) {
        Intent intent = new Intent(this, WebViewActivity.class);
        this.startActivityForResult(intent, GOOGLE_OAUTH_REQUEST_ID);
    }

    private void performGoogleSignOut(View v) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Signed out...",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                performLoginWithCreds(v);
                break;
            case R.id.btn_forgetpwd:
                performForgetPwd(v);
                break;
            case R.id.btn_qr_login:
                performQRScan(v);
                break;
            case R.id.gsign_in_button:
                performGoogleSignIn(v);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dt = (data!=null)?data.getExtras().toString():"No Data";
        Log.e("QR Scan", "Req code - " + requestCode + " Data - " + dt);
        if (requestCode == SCANQR_REQUEST_ID && data != null ) {
            IQRScanResult scanResult = (IQRScanResult) data.getExtras().get(IQRScanResult.class.getName());
            if (scanResult instanceof CloudQRLoginScan) {
                CloudQRLoginScan cloudQRLogin = (CloudQRLoginScan) scanResult;
                String msg = "Cloud QR Code Scan \n";
                msg += "Lsi - " + cloudQRLogin.getLsi() + "\n";
                msg += "Srv Name - " + cloudQRLogin.getServiceName() + "\n";
                msg += "Location - " + cloudQRLogin.getLocation() + "\n";
                msg += "Expiry - " + cloudQRLogin.getExpiry() + "\n";
                showDialog(msg);
                Log.e("CloudQRLoginScan", msg);
            } else {
                showToast("Unknown QR code");
            }
        } else if (requestCode == GOOGLE_AUTHCODE_REQUEST_ID) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else if (requestCode == GOOGLE_OAUTH_REQUEST_ID && data != null && resultCode == RESULT_OK) {
            try {
                if (data.hasExtra("error")) {
                    VerifySdkException verifySdkException = (VerifySdkException)data.getExtras().get("error");
                    showDialog(verifySdkException.toString());
                }
                else if (data.hasExtra("token"))    {
                    OAuthToken oAuthToken = (OAuthToken)data.getExtras().get("token");
                    Log.e("Google Sign-in",oAuthToken.serializeToJson());
                    JSONObject jsonObj = new JSONObject(oAuthToken.serializeToJson());
                    String accToken = jsonObj.getString("accessToken");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    access_token = accToken;
                    intent.putExtra("App token", accToken);
                    startActivity(intent);
                }
                else {
                    showDialog("Something went wrong");
                }
            } catch (VerifySdkException | JSONException e) {
                e.printStackTrace();
                showDialog(e.getMessage());
            }
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                Log.e("Google SignIn Result","personName - "+acct.getDisplayName() + " - " + "personEmail - "+acct.getEmail() + " - " + "id - "+acct.getServerAuthCode() );
                Toast.makeText(MainActivity.this,"Signed in with Google!",Toast.LENGTH_SHORT).show();
                getGoogleTokens(acct.getServerAuthCode());
            }
        } catch (ApiException e) {
            Log.w("google", "signInResult:failed code=" + e.getStatusCode() + " - " + e.getMessage());
            showDialog(e.getMessage());
        }
    }

    private void getGoogleTokens(String gooAuthCode) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("grant_type", "authorization_code");
            jsonBody.put("client_id", TrustMeInsuranceApp.getGoogleClientId());
            jsonBody.put("client_secret", TrustMeInsuranceApp.getGoogleClientSecret());
            jsonBody.put("redirect_uri", "");
            jsonBody.put("code", gooAuthCode);
            String mRequestBody = jsonBody.toString();

            StringRequest sr = new StringRequest(Request.Method.POST, TrustMeInsuranceApp.getGoogleTokenUrl(),
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                gooAccessToken = jsonObj.getString("access_token");
                                gooIdToken = jsonObj.getString("id_token");
                                Log.e("Google Tokens","Id Token : " + gooIdToken + " - Access Token : " + gooAccessToken);
                                callExchangeSocialJwtToken ();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                            error.printStackTrace();
                        }
                    })
                {
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }

                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
            queue.add(sr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callExchangeSocialJwtToken () {
        DecodedJWT decodedJWT = JWT.decode(gooIdToken);
        Log.e("Google Decoded JWT", "iss - " + decodedJWT.getIssuer() + " - sub - " + decodedJWT.getSubject());

        com.auth0.jwt.algorithms.Algorithm algo = com.auth0.jwt.algorithms.Algorithm.HMAC256("tmi123");
        String newJwtToken = JWT.create()
                .withClaim("plat","google")
                .withClaim("iss", decodedJWT.getIssuer())
                .withClaim("sub", decodedJWT.getSubject())
                .withClaim("token", gooAccessToken)
                .withClaim("typ", "urn:com:ibm:cloudidentity:social")
                .withClaim("userinfo", "{\"email\":\"msiva2202@gmail.com\"}")
                .sign(algo);
        VerifyAPIService.exchangeSocialJwtToken(newJwtToken);
    }

    private void showToast(final String message) {
        MainActivity.this.runOnUiThread(() -> {
            Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
    }

    private void showDialog(final String message) {
        if (message == null) {
            showToast("Something went wrong");
        } else {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("TrustMeInsurance")
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    private void showSetUpDialog(final String message) {
        if (message == null) {
            showToast("Something went wrong");
        } else {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("TrustMeInsurance")
                            .setMessage(message)
                            .setPositiveButton("SetUp", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onSetUp(v);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    public void onSetUp(View v){

        Intent intent;
        intent = new Intent(MainActivity.this, SetUp.class);
        startActivity(intent);

    }

    public void onSignUp(View v){
        Intent intent;
        intent = new Intent(MainActivity.this, GetQuote.class);
        startActivity(intent);

    }
    public void checkNewUser(View v){

        Log.d("Main","Check new user");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        EditText userName = ((EditText)findViewById(R.id.username));
        EditText password = ((EditText)findViewById(R.id.password));
        Button btn_submit = (Button)findViewById(R.id.btn_Submit);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        final String usr = ((EditText)findViewById(R.id.username)).getText().toString();
        username = usr;
        String usr_quote= '"'+usr+'"';
        String url_consent = TrustMeInsuranceApp.getUsersUrl() + "?access_token="+VerifyAPIService.getAccess_token()+"&filter=userName+eq+"+usr_quote;

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
                                if(factors.length()>0) {
                                    JSONObject jsonObject = factors.getJSONObject(0);
                                    if(jsonObject.has("id")) {

                                        uid = jsonObject.getString("id");
                                        Log.d("Has id",uid);
                                    }
                                    if (jsonObject.has("urn:ietf:params:scim:schemas:extension:ibm:2.0:User")) {
                                        JSONObject att = jsonObject.getJSONObject("urn:ietf:params:scim:schemas:extension:ibm:2.0:User");
                                        if (att.has("lastLogin")){
                                            Log.d("NewUser","OLd user");
                                            //Populate username in Username field and make it non editable
                                            userName.setText(usr);
                                            userName.setEnabled(false);
                                            //Make pwd field visible
                                            password.setVisibility(View.VISIBLE);
                                            //Make Submit Button gone
                                            btn_submit.setVisibility(View.GONE);
                                            //Make Login Button visible
                                            btn_login.setVisibility(View.VISIBLE);

                                        }
                                        else{
                                            Log.d("NewUser","New user");
                                            generateEmailOTPResetPwd(usr);

                                        }

                                    }

                                }
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
    public void generateEmailOTPResetPwd(String email) {
        Log.d("MSG","email otp generate");
        String randomPIN = ""+((int)(Math.random()*9000)+1000);
        Log.d("Email",email);
        String email_access_token=VerifyAPIService.getAccess_token();
        Log.d("access_token",email_access_token);

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
                                intent = new Intent(MainActivity.this,EmailOtpVer.class);
                                intent.putExtra("txnId",txnId);
                                intent.putExtra("access_token",email_access_token);
                                intent.putExtra("NewUser",true);
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
                    String bearer_token = "Bearer "+email_access_token;
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
    private void getDUA(String purposeId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("trace",false);
            JSONArray arr = new JSONArray();
            JSONObject jsonBody1=new JSONObject();
            jsonBody1.put("purposeId",purposeId);
            jsonBody1.put("accessTypeId","default");
            arr.put(jsonBody1);
            jsonBody.put("items",arr);
            jsonBody.put("isExternalSubject", false);
            jsonBody.put("subjectId",uid);
            Log.d("Uid",uid);
            final String mRequestBody = jsonBody.toString();
            Log.d("DUABody",mRequestBody);
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";
            String url_consent = TrustMeInsuranceApp.getDuaUrl();
            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("DUAHttpClient", "success! response: " + response);
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject obj =arr.getJSONObject(0);
                                JSONArray res = obj.getJSONArray("result");
                                JSONObject obj1 = res.getJSONObject(0);

                                if(obj1.getBoolean("approved")){
                                    Intent intent;
                                    intent = new Intent(MainActivity.this, HomeActivity.class);
                                    intent.putExtra("App token", access_token);
                                    startActivity(intent);
                                }
                                else{

                                    getDSP(EULAPurposeId);
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

    private void getDSP(String purposeid) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                            Log.e("DSPHttpClient", "success! response: " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if(jsonObj.has("purposes")){
                                    JSONObject purposes = jsonObj.getJSONObject("purposes");
                                    if(purposes.has(purposeid)){
                                        JSONObject EULAPurposeId = purposes.getJSONObject(purposeid);
                                        version = EULAPurposeId.getInt("version");
                                        Date date = new Date(EULAPurposeId.getInt("lastModifiedTime")*1000L);
                                        // format of the date
                                        SimpleDateFormat jdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                                        jdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        String java_date = jdf.format(date);
                                        System.out.println("\n"+java_date+"\n");
                                        lastModifiedTime = java_date;
                                        ref = EULAPurposeId.getJSONObject("termsOfUse").getString("ref");
                                        Intent intent;
                                        intent = new Intent(MainActivity.this, EULA.class);
                                        intent.putExtra("Version",version);
                                        intent.putExtra("date",lastModifiedTime);
                                        intent.putExtra("ref",ref);
                                        intent.putExtra("uid",uid);
                                        intent.putExtra("App token",access_token);
                                        startActivity(intent);

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

}
