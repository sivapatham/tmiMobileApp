package com.ibm.verifydemo;

import android.util.Log;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VerifyAPIService {
    private static Timer timeChecker;
    public static int cnt = 0;
    public static boolean verifyCheckRunning = false;
    public static String app_access_token = null;
    public static String access_token = null;
    public static String loggedin_uid = null;
    public static String authr_id = null;
    public static String factr_id = null;
    public static String verify_id = null;
    public static String push_state = "Push status - Unknown";
    public static String verify_state = "Verify status - Unknown";

    public static String getApp_access_token() {
        return app_access_token;
    }

    public static void setApp_access_token(String app_access_token) {
        VerifyAPIService.app_access_token = app_access_token;
    }

    public static String getAccess_token() {
        return VerifyAPIService.access_token;
    }

    public static void setAccess_token(String access_token) {
        VerifyAPIService.access_token = access_token;
    }

    public static String getLoggedin_uid() {
        return loggedin_uid;
    }

    public static void setLoggedin_uid(String loggedin_uid) {
        VerifyAPIService.loggedin_uid = loggedin_uid;
    }

    public static String getAuthr_id() {
        return authr_id;
    }

    public static void setAuthr_id(String authr_id) {
        VerifyAPIService.authr_id = authr_id;
    }

    public static String getFactr_id() {
        return factr_id;
    }

    public static void setFactr_id(String factr_id) {
        VerifyAPIService.factr_id = factr_id;
    }

    public static String getVerify_id() {
        return verify_id;
    }

    public static void setVerify_id(String verify_id) {
        VerifyAPIService.verify_id = verify_id;
    }

    public static String getVerify_state() {
        return verify_state;
    }

    public static void setVerify_state(String verify_state) {
        VerifyAPIService.verify_state = verify_state;
    }

    public static String getPush_state() {
        return push_state;
    }

    public static void setPush_state(String push_state) {
        VerifyAPIService.push_state = push_state;
    }

    public static boolean isVerifyCheckRunning() {
        return verifyCheckRunning;
    }

    public static void setVerifyCheckRunning(boolean running) {
        VerifyAPIService.verifyCheckRunning = running;
    }

    public static void startCheckingVerifyStatus(int period, TimerTask toDo) {
        if(timeChecker != null) {
            return;
        }
        timeChecker = new Timer();
        timeChecker.scheduleAtFixedRate(toDo, 0, period*1000);
        setVerifyCheckRunning(true);
    }

    public static void stopCheckingVerifyStatus() {
        if ( timeChecker != null)
            timeChecker.cancel();
        timeChecker = null;
        setVerifyCheckRunning(false);
    }

    public static void generateApiAccessToken() {
        Log.d("MSG","VerifyAPIService - generateApiAccessToken method");
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());

        String tokenUrl = TrustMeInsuranceApp.getTokenUrl();
        Log.e("MSG", "Calling token URL - POST - " + tokenUrl);
        StringRequest srRequest = new StringRequest(Request.Method.POST, tokenUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            setAccess_token(jsonObj.getString("access_token"));
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
                        Toast.makeText(TrustMeInsuranceApp.getContext(),"Please do setup first",Toast.LENGTH_LONG).show();
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
        queue.add(srRequest);
    }

    public static void exchangeSocialJwtToken ( String socialJwtToken ) {
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        String mRequestBody = null;
        String vExchangeJwtUrlwithParams = TrustMeInsuranceApp.getExchangeJwtUrl() + "?jwt=" + socialJwtToken;
        Log.e("MSG", "Calling exchangeSocialJWT URL - POST - " + vExchangeJwtUrlwithParams);
        StringRequest srRequest = new StringRequest(Request.Method.POST, vExchangeJwtUrlwithParams,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("HttpClient", "Exchange JWT Token API response : \n" + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                        //error.printStackTrace();
                        if(error.networkResponse.data!=null) {
                            try {
                                String body = new String(error.networkResponse.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
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
                params.put("jwt", socialJwtToken);
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Length","0");
                params.put("Authorization", "Bearer " + VerifyAPIService.getAccess_token());
                return params;
            }
        };
        queue.add(srRequest);
    }


    public static void getAuthFactors() {
        Log.d("MSG","VerifyAPIService - getAuthFactors method");
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        String factorsUrl = TrustMeInsuranceApp.getFactorsUrl() + "?search=userId=\"" + VerifyAPIService.getLoggedin_uid() +"\"&enabled=true&type=signature";
        String mRequestBody = null;

        Log.e("MSG", "Calling getFactors URL - GET -" + factorsUrl);
        StringRequest srRequest = new StringRequest(Request.Method.GET, factorsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray arr = jsonObj.getJSONArray("factors");
                            if (arr.length() > 0) {
                                for (int i=0; i<arr.length(); i++) {
                                    JSONObject fac = (JSONObject)arr.get(i);
                                    if (!fac.isNull("subType") && "userPresence".equalsIgnoreCase(fac.getString("subType"))) {
                                        VerifyAPIService.authr_id = fac.getJSONObject("references").getString("authenticatorId");
                                        VerifyAPIService.factr_id = fac.getString("id");
                                    }
                                }
                                Log.e("MSG", "Authr Id - " + VerifyAPIService.authr_id + " - Factor Id - " + VerifyAPIService.factr_id);
                                initPushVerification();
                            }
                            else
                                Log.e("Error", "No mobile factors registered for this user");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Length","0");
                params.put("Authorization", "Bearer " + VerifyAPIService.getAccess_token());
                return params;
            }
        };
        srRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(srRequest);
    }

    public static void initPushVerification() {
        Log.d("MSG","VerifyAPIService - initPushVerification method");
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        String authrsUrl = TrustMeInsuranceApp.getStrongAuthUrl() + "/" + VerifyAPIService.authr_id + "/verifications";
        String mRequestBody = "{\n" +
                "  \"expiresIn\": 300,\n" +
                "  \"pushNotification\": {\n" +
                "    \"sound\": \"default\",\n" +
                "    \"message\": \"Pending Authentication from TMI App\",\n" +
                "    \"send\": true,\n" +
                "    \"title\": \"TMI App - Claim Action\"\n" +
                "  },\n" +
                "  \"authenticationMethods\": [\n" +
                "    {\n" +
                "      \"methodType\": \"signature\",\n" +
                "      \"id\": \"" + VerifyAPIService.factr_id + "\"" +
                "    }\n" +
                "  ],\n" +
                "  \"logic\": \"OR\",\n" +
                "  \"transactionData\": {\n" +
                "    \"additionalData\": [\n" +
                "      {\n" +
                "        \"name\": \"foo\",\n" +
                "        \"value\": \"bar\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"message\": \"Provide your authentication to initiate your claim\",\n" +
                "    \"originIpAddress\": \"192.168.222.222\",\n" +
                "    \"originUserAgent\": \"TMI Mobile App\"\n" +
                "  }\n" +
                "}";
        StringRequest srRequest = new StringRequest(Request.Method.POST, authrsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            VerifyAPIService.verify_id = jsonObj.getString("id");
                            VerifyAPIService.verify_state = "Verify status - " + jsonObj.getString("state");
                            VerifyAPIService.push_state = "Push status - " + ((JSONObject)jsonObj.getJSONObject("pushNotification")).getString("sendState");
                            Log.e("MSG", VerifyAPIService.push_state + " - " + VerifyAPIService.verify_state);
                            startCheckingVerifyStatus(5, new TimerTask() { public void run() {checkVerificationStatus();} });
                        } catch (JSONException e) {
                            Log.e("Error", "Oops..Sending Push notification failed for some reason!");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + VerifyAPIService.getAccess_token());
                return params;
            }
        };
        srRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(srRequest);
    }


    public static void checkVerificationStatus() {
        Log.d("MSG","VerifyAPIService - checkVerificationStatus method");
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        String verifyStatusUrl = TrustMeInsuranceApp.getVerifyStatusUrl() +  "/" + VerifyAPIService.authr_id +  "/verifications/"+ VerifyAPIService.verify_id + "?returnJwt=true";
        String mRequestBody = null;
        StringRequest srRequest = new StringRequest(Request.Method.GET, verifyStatusUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            VerifyAPIService.push_state = "Push status - " + jsonObj.getJSONObject("pushNotification").getString("sendState");
                            VerifyAPIService.verify_state = "Verify status - " + jsonObj.getString("state");
                            boolean jwtTokenAvailable = !jsonObj.isNull("assertion");
                            Log.e("MSG", VerifyAPIService.push_state + " - " + VerifyAPIService.verify_state + " - JWT token - " + jwtTokenAvailable);
                            if (!VerifyAPIService.verify_state.contains("PENDING")) {
                                stopCheckingVerifyStatus();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + VerifyAPIService.getAccess_token());
                return params;
            }
        };
        srRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(srRequest);
    }

    public static void deleteSmsOtpFactor() {
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
        String sms_enroll_id = "39409011-842c-43bb-8245-ab281cea325d";
        String delSmsOtpFactorUrl = TrustMeInsuranceApp.getBaseVerifyURL() +  "/v2.0/factors/smsotp/" + sms_enroll_id;
        String mRequestBody = null;
        StringRequest srRequest = new StringRequest(Request.Method.DELETE, delSmsOtpFactorUrl,
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
        srRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(srRequest);
    }

    public static void performMockStrongAuthForUITesting() {
        int rndNo = (new Random()).nextInt(10);
        Log.e("Verify Counter", "rndNo = " + rndNo);
        startCheckingVerifyStatus(5, new TimerTask() {
            public void run() {
                //Log.e("Verify Counter", "Inside startCheckingVerifyStatus");
                cnt++;
                if ((cnt) >= rndNo) {
                    Log.e("Verify Counter", "Stopped when " + cnt + " >= " + rndNo);
                    stopCheckingVerifyStatus();
                    cnt = 0;
                    if (rndNo % 2 == 0) {
                        VerifyAPIService.setPush_state("Push status - SUCCESS");
                        VerifyAPIService.setVerify_state("Verify status - VERIFY-SUCCESS");
                    } else {
                        VerifyAPIService.setPush_state("Push status - SUCCESS");
                        VerifyAPIService.setVerify_state("Verify status - VERIFY-DENIED");
                    }
                }
                //Log.e("Verify Counter", "cnt = " + cnt);
            }
        });
    }

    public static void performStrongAuth() {
        setVerifyCheckRunning(true);
        getAuthFactors();
        //Just uncomment below line to test only UI flow by avoiding Verify API calls
        //performMockStrongAuthForUITesting();
    }

}
