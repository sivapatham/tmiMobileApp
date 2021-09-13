package com.ibm.verifydemo.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.ibm.verifydemo.ChooseQuote;
import com.ibm.verifydemo.GetQuote;
import com.ibm.verifydemo.HomeActivity;
import com.ibm.verifydemo.MainActivity;
import com.ibm.verifydemo.R;
import com.ibm.verifydemo.TrustMeInsuranceApp;
import com.ibm.verifydemo.VerifyAPIService;
import com.ibm.verifydemo.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CarQuoteDetailsActual extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;
    private Spinner makespinner,modelspinner;
    public static String carYear, carMake,carModel, bday, zip;
    String api_access_token,id ;
    int quote_count=0;
    String email, firstName,lastName;
    String cQuoteName;
    boolean loggedIn;
    public static final String MSG ="com.ibm.verifydemo.token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_car_quote_details);

        makespinner = (Spinner)findViewById(R.id.spinner2);
        String [] car_makes = getResources().getStringArray(R.array.car_make);
        ArrayAdapter makeadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,car_makes);
        makeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makespinner.setAdapter(makeadapter);

        modelspinner = (Spinner)findViewById(R.id.spinner3);
        String [] car_models = getResources().getStringArray(R.array.car_model);
        ArrayAdapter modeladapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,car_models);
        modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelspinner.setAdapter(modeladapter);

        dateView = (EditText) findViewById(R.id.bday_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month+1, day);

        api_access_token = VerifyAPIService.getAccess_token();
        Log.d("APIAccesstokencardet:",api_access_token);

        email = GetQuote.email_id;
        firstName = GetQuote.first_name;
        lastName = GetQuote.last_name;
        loggedIn= HomeFragment.loggedIn;
        Log.d("Loggedin", String.valueOf(loggedIn));
        
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    public String getRandomInt(){
        return ""+((int)(Math.random()*9000)+1000);
    }
    public void getCarQuote(View v){

        EditText et_bday = (EditText)findViewById(R.id.bday_date);
        bday = et_bday.getText().toString();
        Log.d("Bday",bday);

        EditText et_zip = (EditText)findViewById(R.id.const_year);
        zip = et_zip.getText().toString();
        Log.d("ZIP",zip);

       carMake = null;
       Spinner s_make = (Spinner)findViewById(R.id.spinner2);
       if(s_make != null && s_make.getSelectedItem() != null && !(s_make.getSelectedItem().toString().equals("None"))){
           carMake = (String)s_make.getSelectedItem();
           Log.d("Car Make",carMake);
       }else{
           Toast.makeText(CarQuoteDetailsActual.this, "Please select car Make", Toast.LENGTH_SHORT).show();
           return;
       }

        carModel = null;
        Spinner s_model = (Spinner)findViewById(R.id.spinner3);
        if(s_model != null && s_model.getSelectedItem() != null && !(s_model.getSelectedItem().toString().equals("None"))){
            carModel = (String)s_model.getSelectedItem();
            Log.d("Car Model",carModel);
        }else{
            Toast.makeText(CarQuoteDetailsActual.this, "Please select car Model", Toast.LENGTH_SHORT).show();
            return;
        }


        EditText et_car_year = (EditText)findViewById(R.id.idv);
        carYear = et_car_year.getText().toString();
        Log.d("Car Year",carYear);
        if(carYear.matches("")){
            Toast.makeText(CarQuoteDetailsActual.this, "Please enter Car year", Toast.LENGTH_SHORT).show();
            return;
        }
        cQuoteName = "QC-"+getRandomInt()+"-"+getRandomInt();
        if(loggedIn){
            id=HomeFragment.uid;
            getUserByID(id);

        }
        else
            getUserID();


    }

    public void getUserID() {
        Log.d("MSG","getuserid");
        String email = GetQuote.email_id;
        Log.d("Email:",email);
        String email_quote= '"'+email+'"';
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //final String url = "https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users?filter=userName+eq+"${emailAddress}"&attributes=id,emails;
        String url = TrustMeInsuranceApp.getUsersUrl() + "?access_token="+api_access_token +"&filter=userName+eq+"+email_quote+"&attributes=id,emails";
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        JSONObject jsonBody = response;
                        JSONArray jsonarr;

                        int total_results=0;
                        try {
                            total_results = jsonBody.getInt("totalResults");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //if jsonBody.totalResults != 0
                        if(total_results!=0){
                            try {
                                jsonarr = jsonBody.getJSONArray("Resources");
                                JSONObject res = jsonarr.getJSONObject(0);
                                id = res.getString("id");
                                Log.d("ID",id);
                                getUserByID(id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                            String userInfo = "{\n" +
                                    "\"schemas\": [\n" +
                                    "\"urn:ietf:params:scim:schemas:core:2.0:User\",\n" +
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User\"],\n" +
                                    "\"userName\":" +"\""+email+"\",\n"+
                                    "\"name\":{\n"+
                                    "\"familyName\":"+"\""+ lastName+"\",\n"+
                                    "\"givenName\":"+"\""+ firstName+"\"\n},"+
                                    "\"preferredLanguage\": \"en-US\",\n"+
                                    "\"active\": true,\n"+
                                    "\"emails\": [\n"+
                                    "{\n"+
                                    "\"value\":"+"\""+ email+"\",\n"+
                                    "\"type\": \"work\"\n"+
                                    "}\n],\n"+
                                    "\"addresses\": [\n{\n"+
                                    "\"postalCode\":\""+zip+"\"\n}\n],\n"+
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User\": {\n"+
                                    "\"userCategory\": \"regular\",\n"+
                                    "\"twoFactorAuthentication\": false,\n"+
                                    "\"customAttributes\": [\n{\n"+
                                        "\"name\": \"birthday\",\n"+
                                        "\"values\":[\""+ bday +"\"]},\n"+
                                    "{\n"+
                                        "\"name\": \"carModel\",\n"+
                                        "\"values\":[\""+ carModel +"\"]},\n"+
                                    "{\n"+
                                        "\"name\": \"carYear\",\n"+
                                        "\"values\":[\""+ carYear +"\"]},\n"+
                                    "{\n"+
                                        "\"name\": \"carMake\",\n"+
                                        "\"values\":[\""+ carMake +"\"]},\n"+
                                    "{\n"+
                                        "\"name\": \"quoteCount\",\n"+
                                        "\"values\":[\"1\"]" +"},\n"+
                                    "{\n"+
                                        "\"name\": \"CarQuoteAccept\",\n"+
                                        "\"values\":[\"false\"]"+ "},\n"+
                                    "{\n"+
                                        "\"name\": \"CarQuoteName\",\n"+
                                        "\"values\":[\""+ cQuoteName +"\"]}]\n}}"
                                    ;
                            Log.d("Userinfo",userInfo);
                            try {
                                JSONObject json_userinfo = new JSONObject(userInfo);
                                Log.d("JSON:", json_userinfo.toString());
                                //Create new user
                                createUser(json_userinfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    private void createUser(JSONObject operations) {
        Log.d("CarQD","CreateUser");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String mRequestBody = operations.toString();
        Log.d("body:",mRequestBody);
        //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/";
        String url_sms = TrustMeInsuranceApp.getUsersUrl() ;

        StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("UpdateAttHttpClient", "success! response: " + response);
                        ChooseQuote.new_user=true;
                        showDialog("Congratulations\n" +
                                "Your quote is ready and account is setup!\nA password has been generated for you and sent to the email you provided us. ","Access your account to accept your quote");

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
            /*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                System.out.println("getbody");
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Map<String,String> getParams() {
                System.out.println("get par");
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                System.out.println("gethead");
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/scim+json");
                //params.put("X-HTTP-Method-Override","PATCH");
                String bearer_token = "Bearer "+api_access_token;
                params.put("Authorization",bearer_token);
                return params;
            }
        };
        queue.add(sr);

    }

    public void getUserByID(String user_id) {
        Log.d("MSG","get user by id");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url_consent = TrustMeInsuranceApp.getUsersUrl()+"/"+user_id + "?access_token="+api_access_token;
        //cQuoteName = "QC-"+getRandomInt()+"-"+getRandomInt();

        // prepare the Request
        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url_consent, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CCResponse", response.toString());

                        try {
                            JSONObject before_custom_att = response.getJSONObject("urn:ietf:params:scim:schemas:extension:ibm:2.0:User");
                            //Custom attributes is a JSONArray
                            JSONArray custom_att = before_custom_att.getJSONArray("customAttributes");
                            String prev_value="";
                            boolean quotecount_found=false;
                            for(int i=0;i<custom_att.length();i++)
                            {
                                JSONObject item = custom_att.getJSONObject(i);
                                Iterator<String> keys = item.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    //quoteCount
                                    if((key.equals("name")) && (item.get(key).equals("quoteCount"))){
                                        Log.d("In quote count",prev_value);
                                        if(prev_value.equals("undefined")||prev_value.equals("0")||prev_value.equals(""))
                                            quote_count=1;
                                        else
                                            quote_count=Integer.parseInt(prev_value)+1;
                                        quotecount_found=true;
                                        break;
                                    }
                                    if(quotecount_found)
                                        break;
                                    if(key.equals("values"))
                                    {
                                        prev_value=item.getJSONArray(key).getString(0);
                                    }
                                }
                                if(quotecount_found)
                                    break;
                            }

                            String op_bday = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"birthday\",\"values\":[\""+bday+"\"]}]}";
                            String op_CarYear = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"carYear\",\"values\":[\""+carYear+"\"]}]}";
                            String op_CarMake = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"carMake\",\"values\":[\""+carMake+"\"]}]}";
                            String op_CarModel = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"carModel\",\"values\":[\""+carModel+"\"]}]}";
                            String op_zip = "{\"op\":\"add\",\"path\":\"addresses\",\"value\":[{\"postalCode\":\""+zip+"\"}]}";
                            String op_quoteCount = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"quoteCount\",\"values\":[\""+quote_count+"\"]}]}";
                            String op_carQuoteAccept = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"CarQuoteAccept\",\"values\":[\""+"false"+"\"]}]}";
                            String op_carQuoteName = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"CarQuoteName\",\"values\":[\""+cQuoteName+"\"]}]}";
                            String operations ="["+ op_bday+","+op_CarYear+","+op_CarMake+","+op_CarModel+","+op_zip+","+op_quoteCount+","+op_carQuoteAccept+","+op_carQuoteName+"]";
                            Log.d("operations",operations);
                            updateAttributes(operations);

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

    private void updateAttributes(String operations) {
        Log.d("MSG CarQuoDet","update att");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        try
        {
            JSONArray op_arr = new JSONArray(operations);
            JSONObject jsonBody = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.put("urn:ietf:params:scim:api:messages:2.0:PatchOp");
            jsonBody.put("Operations", op_arr);
            jsonBody.put("schemas",arr);
            final String mRequestBody = jsonBody.toString();
            Log.d("Body",mRequestBody);
            //final String url_sms="https://cloudidentity1234.ice.ibmcloud.com/v2.0/Users/"+uid;
            String url_sms = TrustMeInsuranceApp.getUsersUrl() + "/"+ id;

            StringRequest sr = new StringRequest(Request.Method.POST, url_sms,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("UpdateAttHttpClient", "success! response: " + response);
                            if(!loggedIn)
                                showDialog("Congratulations\nYour quote is ready. Login to access the quote","Login");
                            else{
                                Intent intent;
                                intent = new Intent(CarQuoteDetailsActual.this, HomeActivity.class);
                                intent.putExtra(MSG, MainActivity.access_token);
                                startActivity(intent);
                            }

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

    public void showDialog(String msg,String btn_name) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarQuoteDetailsActual.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage(msg
                        )
                        .setPositiveButton(btn_name, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(CarQuoteDetailsActual.this,MainActivity.class);
                                startActivity(resultIntent);

                            }
                        });
                        /*.setNegativeButton("Quote", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(CarQuoteDetails.this,GetQuote.class);
                                startActivity(resultIntent);

                            }
                        });*/


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


}