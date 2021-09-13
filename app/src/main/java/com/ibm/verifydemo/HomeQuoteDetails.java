package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ibm.verifydemo.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeQuoteDetails extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextInputEditText dateView;
    private int year, month, day;
    private Spinner constructionspinner;
    public static String  address,city,state,construction_type,bday,aadhar,zip,homeYear,coverage_val;
    String api_access_token,id ;
    int quote_count=0;
    String email, firstName,lastName;
    String hQuoteName,uid;
    CheckBox cb_background;
    boolean loggedIn;
    public static final String MSG ="com.ibm.verifydemo.token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_quote_details);

        constructionspinner = (Spinner)findViewById(R.id.spinner4_cons_type);
        String [] construction_types = getResources().getStringArray(R.array.construction_type);
        ArrayAdapter makeadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,construction_types);
        makeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        constructionspinner.setAdapter(makeadapter);

        TextInputLayout dateView1 = (TextInputLayout) findViewById(R.id.TILbday);
        dateView =(TextInputEditText)dateView1.getEditText();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        api_access_token = VerifyAPIService.getAccess_token();
        Log.d("APIAccesstokencardet:",api_access_token);

        email = GetQuote.email_id;
        firstName = GetQuote.first_name;
        lastName = GetQuote.last_name;
        loggedIn= HomeFragment.loggedIn;
        Log.d("Loggedin", String.valueOf(loggedIn));

        TextInputLayout text1 = (TextInputLayout) findViewById(R.id.TILIDV);
        TextInputEditText text = (TextInputEditText)text1.getEditText();

        TextInputLayout ed_ssn1 = (TextInputLayout) findViewById(R.id.TILSSNorAadhar);
        TextInputEditText ed_ssn =(TextInputEditText)ed_ssn1.getEditText();
        cb_background = (CheckBox)findViewById(R.id.checkBox_EULA);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("MSG","Here I am");
            }
            @Override
            public void afterTextChanged(Editable s) {
                String coverage = text.getText().toString();
                int coverage_value = Integer.parseInt(coverage);
                Log.d("Coverage val:",coverage);
                if(coverage_value>=700000){
                    ed_ssn1.setVisibility(View.VISIBLE);
                    ed_ssn.setVisibility(View.VISIBLE);
                }
                else {
                    ed_ssn1.setVisibility(View.INVISIBLE);
                    ed_ssn.setVisibility(View.INVISIBLE);
                    cb_background.setVisibility(View.INVISIBLE);
                    ed_ssn.setText("");
                }
            }
        });

        ed_ssn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String regex = "^[2-9]{1}[0-9]{3}[-][0-9]{4}[-][0-9]{4}$";
                aadhar = ed_ssn.getText().toString();
                if(aadhar.matches(regex)){
                    cb_background.setVisibility(View.VISIBLE);
                    ed_ssn1.setError("");
                }
                else{
                    if(ed_ssn.getVisibility()==View.VISIBLE) {
                        System.out.println("   Enter Aadhar/SSN in valid format");
                        ed_ssn1.setError("     Aadhar/SSN should be of the form 2345-1111-1111");
                        //Toast.makeText(getApplicationContext(), "Aadhar/SSN should be of the form 2345-1111-1111", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

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
    public void getHomeQuote(View v){

        TextInputLayout et_bday1 = (TextInputLayout)findViewById(R.id.TILbday);
        TextInputEditText et_bday = (TextInputEditText) et_bday1.getEditText();
        bday = et_bday.getText().toString();
        Log.d("Bday",bday);

        TextInputLayout et_address1 = (TextInputLayout)findViewById(R.id.TILAddress);
        TextInputEditText et_address = (TextInputEditText) et_address1.getEditText();
        address = et_address.getText().toString();
        Log.d("Home address",address);
        if(address.matches("")){
            Toast.makeText(HomeQuoteDetails.this, "Please enter Home address", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputLayout et_city1 = (TextInputLayout)findViewById(R.id.TILCity);
        TextInputEditText et_city = (TextInputEditText) et_city1.getEditText();
        city = et_city.getText().toString();
        Log.d("City",city);
        if(city.matches("")){
            Toast.makeText(HomeQuoteDetails.this, "Please enter Home City", Toast.LENGTH_SHORT).show();
            return;
        }
        TextInputLayout et_state1 = (TextInputLayout)findViewById(R.id.TILState);
        TextInputEditText et_state = (TextInputEditText) et_state1.getEditText();
        state = et_state.getText().toString();
        Log.d("State",state);
        if(state.matches("")){
            Toast.makeText(HomeQuoteDetails.this, "Please enter Home State", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputLayout et_zip1 = (TextInputLayout)findViewById(R.id.TILZip);
        TextInputEditText et_zip = (TextInputEditText) et_zip1.getEditText();
        zip = et_zip.getText().toString();
        if(zip.matches("")){
            Toast.makeText(HomeQuoteDetails.this, "Please enter zip code", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("ZIP",zip);

        TextInputLayout et_cons_year1 = (TextInputLayout)findViewById(R.id.TILConstYear);
        TextInputEditText et_cons_year = (TextInputEditText) et_cons_year1.getEditText();
        homeYear = et_cons_year.getText().toString();
        Log.d("Home Year",homeYear);
        if(homeYear.matches("")){
            //et_cons_year1.setError("Please enter Home year");
            Toast.makeText(HomeQuoteDetails.this, "Please enter Home year", Toast.LENGTH_SHORT).show();
            return;
        }

        construction_type = null;
        Spinner s_ct = (Spinner)findViewById(R.id.spinner4_cons_type);
        //Log.d("Construction type out",construction_type);
        if(s_ct != null && s_ct.getSelectedItem() != null && !(s_ct.getSelectedItem().toString().equals("--Construction Type--"))){
            construction_type = (String)s_ct.getSelectedItem();
            Log.d("Construction type",construction_type);
        }else{
            Toast.makeText(HomeQuoteDetails.this, "Please select construction type", Toast.LENGTH_SHORT).show();
            return;
        }



        TextInputLayout et_idv1 = (TextInputLayout)findViewById(R.id.TILIDV);
        TextInputEditText et_idv = (TextInputEditText) et_idv1.getEditText();
        coverage_val = et_idv.getText().toString();
        Log.d("Coverage",coverage_val);
        if(coverage_val.matches("")){
            Toast.makeText(HomeQuoteDetails.this, "Please enter Coverage val", Toast.LENGTH_SHORT).show();
            return;
        }


        TextInputLayout et_ssn1 = (TextInputLayout)findViewById(R.id.TILSSNorAadhar);
        TextInputEditText ed_ssn = (TextInputEditText) et_ssn1.getEditText();
        if(ed_ssn.getVisibility()==View.VISIBLE){
            aadhar = ed_ssn.getText().toString();
            Log.d("aadhar",aadhar);
            if(aadhar.matches("")){
                Toast.makeText(HomeQuoteDetails.this, "Please enter Aadhar/SSN value", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        hQuoteName = "QH-"+getRandomInt()+"-"+getRandomInt();

        //If background check checkbox checked
        //consentCreation("####-####-####", "mobileKYCnew", 1, "13982bf1-8c35-4b92-a1a2-94087b59dea6");

        if(loggedIn){
            id=HomeFragment.uid;
            getUserByID(id);
            uid = id;
            if(cb_background.isChecked())
                consentCreation("####-####-####", "mobileKYCnew", 1, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
            else
                consentCreation("####-####-####", "mobileKYCnew", 2, "13982bf1-8c35-4b92-a1a2-94087b59dea6");

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
                                uid =id;
                                Log.d("ID",id);
                                if(cb_background.isChecked())
                                    consentCreation("####-####-####", "mobileKYCnew", 1, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
                                else
                                    consentCreation("####-####-####", "mobileKYCnew", 2, "13982bf1-8c35-4b92-a1a2-94087b59dea6");

                                getUserByID(id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            String userInfo = "{\n" +
                                    "\"schemas\": [\n" +
                                    "\"urn:ietf:params:scim:schemas:core:2.0:User\",\n" +
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User\",\n" +
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:Notification\"],\n" +
                                    "\"userName\":" +"\""+email+"\",\n"+
                                    "\"name\":{\n"+
                                    "\"familyName\":"+"\""+ lastName+"\",\n"+
                                    "\"givenName\":"+"\""+ firstName+"\"\n},\n"+
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:Notification\": {\n"+
                                    "\"notifyType\": \"EMAIL\",\n"+
                                    "\"notifyPassword\": false,\n"+
                                    "\"notifyManager\": false\n},\n"+
                                    "\"preferredLanguage\": \"en-US\",\n"+
                                    "\"active\": true,\n"+
                                    "\"emails\": [\n"+
                                    "{\n"+
                                    "\"value\":"+"\""+ email+"\",\n"+
                                    "\"type\": \"work\"\n"+
                                    "}\n],\n"+
                                    "\"addresses\": [\n{\n"+
                                    "\"postalCode\":\""+zip+"\",\n"   +
                                    "\"streetAddress\":\""+address+"\",\n"   +
                                    "\"locality\":\""+city+"\",\n"   +
                                    "\"region\":\""+state+"\"\n"   +
                                    "}\n],\n"+
                                    "\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User\": {\n"+
                                    "\"userCategory\": \"regular\",\n"+
                                    "\"twoFactorAuthentication\": false,\n"+
                                    "\"customAttributes\": [\n{\n"+
                                    "\"name\": \"birthday\",\n"+
                                    "\"values\":[\""+ bday +"\"]},\n"+
                                    "{\n"+
                                    "\"name\": \"ageHome\",\n"+
                                    "\"values\":[\""+ homeYear +"\"]},\n"+
                                    "{\n"+
                                    "\"name\": \"homeType\",\n"+
                                    "\"values\":[\""+ construction_type +"\"]},\n"+
                                    "{\n"+
                                    "\"name\": \"SSNorAadhar\",\n"+
                                    "\"values\":[\""+ "####-####-####" +"\"]},\n"+
                                    "{\n"+
                                    "\"name\": \"quoteCount\",\n"+
                                    "\"values\":[\"1\"]" +"},\n"+
                                    "{\n"+
                                    "\"name\": \"HomeQuoteAccept\",\n"+
                                    "\"values\":[\"false\"]"+ "},\n"+
                                    "{\n"+
                                    "\"name\": \"HomeQuoteName\",\n"+
                                    "\"values\":[\""+ hQuoteName +"\"]}]\n}}"
                                    ;
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
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.has("id")){
                                uid = jsonObject.getString("id");
                                ChooseQuote.new_user=true;
                                if(cb_background.isChecked())
                                    consentCreation("####-####-####", "mobileKYCnew", 1, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
                                else
                                    consentCreation("####-####-####", "mobileKYCnew", 2, "13982bf1-8c35-4b92-a1a2-94087b59dea6");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                            String op_HomeYear = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"ageHome\",\"values\":[\""+homeYear+"\"]}]}";
                            String op_HomeType = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"homeType\",\"values\":[\""+construction_type+"\"]}]}";
                            String op_IDV = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"IDV\",\"values\":[\""+coverage_val+"\"]}]}";
                            String op_zip = "{\"op\":\"add\",\"path\":\"addresses\",\"value\":[{\"postalCode\":\""+zip+"\",\"streetAddress\":\""+address+"\",\"locality\":\""+city+"\",\"region\":\""+state+ "\"}]}";
                            String op_quoteCount = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"quoteCount\",\"values\":[\""+quote_count+"\"]}]}";
                            String op_homeQuoteAccept = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"HomeQuoteAccept\",\"values\":[\""+"false"+"\"]}]}";
                            String op_homeQuoteName = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"HomeQuoteName\",\"values\":[\""+hQuoteName+"\"]}]}";
                            String op_aadhar1 = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"SSNorAadhar\",\"values\":[\""+aadhar+"\"]}]}";
                            String op_aadhar2 = "{\"op\":\"add\",\"path\":\"urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes\",\"value\":[{\"name\":\"SSNorAadhar\",\"values\":[\""+"####-####-####"+"\"]}]}";
                            String operations = op_bday+","+op_HomeYear+","+op_HomeType+","+op_IDV+","+op_zip+","+op_quoteCount+","+op_homeQuoteAccept+","+op_homeQuoteName;
                            if(cb_background.isChecked())
                                operations = operations +","+op_aadhar1;
                            else
                                operations = operations +","+op_aadhar2;
                            operations = "["+operations + "]";

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
                            //showDialog("Congratulations\nYour quote is ready. Login to access the quote","Login");
                            if(!loggedIn)
                                showDialog("Congratulations\nYour quote is ready. Login to access the quote","Login");
                            else{
                                Intent intent;
                                intent = new Intent(HomeQuoteDetails.this, HomeActivity.class);
                                intent.putExtra("App token", MainActivity.access_token);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeQuoteDetails.this);

                builder.setTitle("TrustMeInsurance")
                        .setMessage(msg
                        )
                        .setPositiveButton(btn_name, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Create intent for sending back to profile page
                                Intent resultIntent = new Intent(HomeQuoteDetails.this,MainActivity.class);
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
    public void consentCreation(String attributeValue,String purposeId,int state,String attributeId)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
}