package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_org);

        //Extra code
        //Code for GET API using Volley

        /*RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://jsonplaceholder.typicode.com/todos/1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("myapp", "The response is" + response.getString("title"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp","something went wrong");
            }
        });

        requestQueue.add(jsonObjectRequest);*/
    }
    /*public void getUserInfoAPI() {

        Log.d("MSG", "in get user info method");




        Log.d("MSG","consent creation");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url_consent = TrustMeInsuranceApp.getMeUrl() + "?access_token="+authtoken;



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
                            boolean idv_found=false;
                            for(int i=0;i<custom_att.length();i++)
                            {
                                JSONObject item = custom_att.getJSONObject(i);
                                //System.out.println(item.getString(""));
                                Iterator<String> keys = item.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    System.out.println("prev1 "+ prev_value);
                                    System.out.println("Key:"+key+" Value :"+item.get(key));

                                    //HomeType
                                    if((key.equals("name")) && (item.get(key).equals("homeType"))){
                                        //System.out.println("mila");
                                        System.out.println("homeType "+ prev_value);
                                        if(prev_value.equals("none"))
                                            obj.put("homeType","Apartment");
                                        else
                                            obj.put("homeType",prev_value);


                                    }

                                    //ageHome
                                    if((key.equals("name")) && (item.get(key).equals("ageHome"))){
                                        System.out.println("ageHome "+ prev_value);
                                        if(prev_value.equals("undefined"))
                                            obj.put("ageHome","unknown");
                                        else
                                            obj.put("ageHome",prev_value);


                                    }
                                    //carYear
                                    if((key.equals("name")) && (item.get(key).equals("carYear"))){

                                        System.out.println("carYear "+ prev_value);
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carYear","Unknown");
                                        else
                                            obj.put("carYear",prev_value);


                                    }

                                    //carMake
                                    if((key.equals("name")) && (item.get(key).equals("carMake"))){

                                        System.out.println("carMake "+ prev_value);
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carMake","Unknown");
                                        else
                                            obj.put("carMake",prev_value);


                                    }

                                    //carModel
                                    if((key.equals("name")) && (item.get(key).equals("carModel"))){

                                        System.out.println("carModel "+ prev_value);
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carModel","Unknown");
                                        else
                                            obj.put("carModel",prev_value);


                                    }

                                    if((key.equals("name")) && (item.get(key).equals("idv"))){

                                        System.out.println("idv "+ prev_value);
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("idv","$700000");
                                        else
                                            obj.put("idv","$"+prev_value);
                                        idv_found=true;
                                        break;
                                    }
                                    if(idv_found)
                                        break;


                                    if(key.equals("values"))
                                    {
                                        //System.out.println("val: "+item.getJSONArray(key).getString(0));
                                        prev_value=item.getJSONArray(key).getString(0);
                                    }
                                    //System.out.println("prev "+ prev_value);
                                }
                            }
                            System.out.println("obj"+obj);
                            policyDisplay();

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
        // System.out.println("function obj: "+obj.toString());
        //return obj;

    }*/


    /*public void resetPwd() {

        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {

            JSONObject jsonBody = new JSONObject();

            //array for schemas
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:api:messages:2.0:PatchOp");
            jsonBody.put("schemas",arr);

            //Create a json array for operations
            JSONArray operations = new JSONArray();

            //json obj that goes inside operations
            JSONObject obj = new JSONObject();
            obj.put("op","replace");

            //json obj for value
            JSONObject obj1 = new JSONObject();
            obj1.put("password","auto-generate");

            //json obj for notification
            JSONObject obj2=new JSONObject();
            obj2.put("notifyType", "EMAIL");
            obj2.put("notifyPassword", true);
            obj2.put("notifyManager", true);

            obj1.put("urn:ietf:params:scim:schemas:extension:ibm:2.0:Notification",obj2);


            obj.put("value",obj1);


            operations.put(obj);

            jsonBody.put("operations", operations);




            final String mRequestBody = jsonBody.toString();


            final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/"+uid+"/passwordResetter";

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);

                            //show that password reset link send
                            showDialog1();

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
                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }*/

                /*
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                */


                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
                /*
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
    }*/

       /*public void showDialog1() {


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(EmailOtpVer.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage("A new password has been sent to your registered email address if it exists. If you did not receive a code, then please contact support for more information."
                                )
                        .setPositiveButton("Return to Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(EmailOtpVer.this,MainActivity.class);
                                //boolean enroll_flag =true;
                                //resultIntent.putExtra("result",enroll_flag);
                                //setResult(RESULT_OK,resultIntent);
                                startActivity(resultIntent);


                                //finish();

                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }*/

    /*public void generateAccessToken() {

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
            @Override
            public String getBodyContentType() {
                //return "application/json; charset=utf-8";
                return "application/x-www-form-urlencoded; charset=UTF-8";
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
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

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


    }*/

      /*public void getUserId() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String front="\"";
        Log.d("Msg",front);

        final String url = "https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users?filter=userName+eq+"+front+email+front+"&attributes=id,emails&access_token="+access_token;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        int c=0;
                        try {
                            JSONArray resources=response.getJSONArray("Resources");
                            for(int i=0;i<resources.length();i++) {
                                JSONObject item = resources.getJSONObject(i);
                                //System.out.println(item.getString(""));

                                Iterator<String> keys = item.keys();
                                System.out.println("i: " + i);
                                while (keys.hasNext()) {
                                    c++;

                                    String key = keys.next();
                                    //System.out.println("Key:"+key+" Value :"+item.get(key));
                                    if (c == 2)
                                        uid = item.getString(key);
                                }
                            }

                            Log.d("MSG",uid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Once uid is generated generate email otp
                        generateEmailOTP();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));

                        //Show msg looks like the user is not present
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);

    }*/

    /*public void generateEmailOTP() {

        Log.d("MSG","email otp generate");
        String randomPIN = ""+((int)(Math.random()*9000)+1000);

        RequestQueue queue = Volley.newRequestQueue(this);
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correlation", randomPIN);
            jsonBody.put("emailAddress",email);
            final String mRequestBody = jsonBody.toString();


            //URL for version 2
            final String url_sms="https://cloudidentity1234.ice.ibmcloud.com:443/v2.0/factors/emailotp/transient/verifications";

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("HttpClient", "success! response: " + response);

                            //go to the Email OTP Verify page
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                //Log.e("HttpClient",jsonObj.getString("id"));
                                String txnId =jsonObj.getString("id");
                                Log.e("HttpClient","Hi" + txnId);
                                Intent intent;
                                intent = new Intent(ForgotPassword.this,EmailOtpVer.class);
                                //String message = "Hi";
                                intent.putExtra(MSG,txnId);
                                intent.putExtra(MSG1,access_token);
                                intent.putExtra(MSG2,uid);
                                intent.putExtra(MSG3,email);
                               // startActivityForResult(intent,1);
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
                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }*/

                /*
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
                /*
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
    }*/


    public void onClickDisenroll(View v) {
        System.out.println("In DisEnroll");

        /*RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url_consent = TrustMeInsuranceApp.getDeleteFactorsUrl()+mfa_Sms_txnid;
        Log.d("DisEnroll",url_consent);
        Map<String, String> params = new HashMap();
        String bearer_token = "Bearer "+access_token;
        params.put("Authorization",bearer_token);
        //params.put("second_param", 2);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url_consent,parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                    Log.d("DisEnroll", "The response is" + response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DisEnroll",error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);*/
        //RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com:443/dpcm/v1.0/privacy/data-subject-presentation";
        // String url_consent = TrustMeInsuranceApp.getDeleteFactorsUrl()+mfa_Sms_txnid+"?id="+mfa_Sms_txnid;
        //"https://cloudidentity1234.ice.ibmcloud.com:443/v2.0/factors/smsotp/16ee42b6-2913-4107-82fa-0c734bad7740?access_token=mnJwYT3BGfCB8J1mVEExBS3SAYGb6vaGpjLDaLSQ";

        //Log.d("DisEnroll",url_consent);
        String mRequestBody = null;



        /*StringRequest sr = new StringRequest(Request.Method.DELETE, url_consent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response);
                        System.out.println("Delete MFA SMS Enroll API Success!");

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
            }*/

                /*@Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/

            /*
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                Log.d("DisEnroll","In getParams");
                //String bearer_token = "Bearer "+access_token;
                //params.put("Authorization",bearer_token);
                //params.put("id",mfa_Sms_txnid);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Length","0");
                //params.put("Accept","text/html");
                Log.d("DisEnroll","In getHeader");
                String bearer_token = "Bearer "+VerifyAPIService.getAccess_token();
                params.put("Authorization",bearer_token);

                return params;
            }
        };
        //queue.add(sr);



    }*/

    /*String paymentUrl = "https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/"+txnId+"/";
        try {
            JSONObject paymentObj = new JSONObject();
            paymentObj.put("otp", otp);
            //paymentObj.put("transaction", transactionObj);
            //paymentObj.put( "threeDSecure",false);


            RequestQueue mQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, paymentUrl, paymentObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VOLLEY", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof AuthFailureError) {
                                Toast.makeText(VerifyOtp.this, "Auth ERROR: " + error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VerifyOtp.this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
                                Log.e("TAG", error.getMessage(), error);
                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    String bearer_token = "Bearer "+authtoken;
                    headers.put("Authorization",bearer_token);
                    //headers.put("X-Token", " b3394743-4c5b-496f-a0e6-06580ba12b1e");

                    return headers;
                }

            };
           // jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 4, 1));
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(jsonObjectRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }*/

        //For version 1
        //String concat_url="https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/"+txnId+"/";

        //sr1.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /*try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("otp", otp);
            Log.d("HttpClient", String.valueOf(jsonBody));
            //jsonBody.put("otpDeliveryMobileNumber",number);
            final String mRequestBody = jsonBody.toString();
            Log.d("HttpClient",mRequestBody);

            //https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/0794f3d8-8a10-4931-ac67-6389448df4fc
            //For version 1
            //String concat_url="https://cloudidentity1234.ice.ibmcloud.com/v1.0/authnmethods/smsotp/transient/verification/"+txnId+"/";

            //For version 2
            String concat_url="https://cloudidentity1234.ice.ibmcloud.com/v2.0/factors/emailotp/transient/verifications/"+txnId+"?returnJwt=true";
            //Log.d("HttpClient",concat_url);
            String url_verification=concat_url;
            Log.d("HttpClient",url_verification);

            StringRequest sr1 = new StringRequest(Request.Method.POST, url_verification,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("In onResponse method");
                            Log.e("HttpClient", "success! response: " + response);

                            //Reset password
                            resetPwd();

                            //After reset password show a dialog

                            //showDialog1();
                            // finish();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("In onError");
                            Log.e("HttpClient", "error: " + error.toString());
                            error.printStackTrace();
                        }
                    })
            {
                @Override
                public String getBodyContentType() {
                    System.out.println("get Body Content");
                    return "application/json; charset=utf-8";
                    // return "application/x-www-form-urlencoded";
                }

                @Override
                public byte[] getBody()  {
                    System.out.println("getBody");
                    try {
                        System.out.println("In try body");
                        if(mRequestBody==null)
                            System.out.println("Request Body null");
                        else
                            System.out.println(mRequestBody.getBytes("utf-8"));
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                System.out.println("parseNetw");
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

                @Override
                protected Map<String,String> getParams() {
                    System.out.println("getParams");
                    Map<String,String> params = new HashMap<String, String>();
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    System.out.println("getHeaders");
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json;charset=UTF-8");
                    //params.put("Accept","text/html");
                    String bearer_token = "Bearer "+authtoken;
                    params.put("Authorization",bearer_token);

                    return params;
                }
            };



            sr1.setRetryPolicy(new DefaultRetryPolicy(5000,4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            //sr1.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(sr1);


        }
        catch (JSONException e){
            e.printStackTrace();
        }*/


    }
     /*        quoteViewModel =
                new ViewModelProvider(this).get(QuoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quote, container, false);
        final TextView textView = root.findViewById(R.id.text_quotes);
        quoteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        View root = inflater.inflate(R.layout.fragment_quote, container, false);
        return root;
 */

    /*QuoteOrPolicyService qs = new QuoteOrPolicyService();
        qs.getUserInfoAPI();
        JSONObject obj1 = QuoteOrPolicyService.obj;
        System.out.println("Obj1: "+obj1);*/

     /*
        policyViewModel =
                new ViewModelProvider(this).get(PolicyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_policy, container, false);
        final TextView textView = root.findViewById(R.id.text_policy);
        policyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
         */

    //Mobile No
        //System.out.println("Home Frag Mobile");
        /*EditText text = (EditText) root.findViewById(R.id.profile_phone);
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
                System.out.println("Profile PhNo - " + number + " - " + new_string);
                if(new_string.equals(number)) {
                    if (number.matches(regex)) {
                        //do something
                        System.out.println("valid - " + number + " - " + new_string);
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
                        //updateAttributes(operations);
                    } else {
                        //do something
                        System.out.println("Enter a valid no");
                        Toast.makeText(getActivity().getApplicationContext(), "InValid Email or Phone no", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        }); */

    //for consent custom att
                            /*JSONObject before_custom_att = response.getJSONObject("urn:ietf:params:scim:schemas:extension:ibm:2.0:User");
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
                                            ((Switch)root.findViewById(R.id.consent_plc2)).setChecked(true);
                                    }
                                    if((key.equals("name")) && (item.get(key).equals("consentaadhar"))){
                                        //System.out.println("mila aa");
                                        //System.out.println("Mila "+ prev_value);
                                        if((prev_value.equals("true")))
                                            ((Switch)root.findViewById(R.id.consent_kyc2)).setChecked(true);
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
                            }*/
    //Log.d("Response",response.getString("email"));

    //twoFactor
                            /*boolean twofactor=false;
                            JSONObject user = response.getJSONObject("urn:ietf:params:scim:schemas:extension:ibm:2.0:User");
                            if(user.getBoolean("twoFactorAuthentication"))
                                twofactor=true; */

     /*if(twofactor){
                                        if(val.equals("home")) {

                                            key=keys.next();
                                            System.out.println("Key2 mob:"+key+" Value2 mob :"+item.get(key));
                                            if (key.equals("value")) {

                                                TextView textView = root.findViewById(R.id.enroll);
                                                textView.setText("(Enrolled)");
                                                textView.setClickable(false);

                                                EditText ed= (EditText) root.findViewById(R.id.mfa_mobile);
                                                String mfa_no = item.getString(key);
                                                ed.setText(mfa_no);
                                                ed.setEnabled(false);


                                            }

                                        }


                                    }*/

    //mfa no
                   /* JSONObject obj = new JSONObject();
                    try {
                        obj.put("op","add");
                        obj.put("path","phoneNumbers");
                        JSONObject obj1 = new JSONObject();
                        obj1.put("type","home");
                        obj1.put("value",mfa_no);
                        JSONArray arr = new JSONArray();
                        arr.put(obj1);
                        obj.put("value",arr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Create a json array
                    JSONArray operations = new JSONArray();
                    operations.put(obj);
                    //Function for updating mfa no

                    updateAttributes(operations);

                    //twoFactorAuth
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
                    //Function for updating mfa no

                    updateAttributes(operations1); */
}