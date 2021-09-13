package com.ibm.verifydemo.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibm.verifydemo.ChooseQuote;
import com.ibm.verifydemo.ForgotPassword;
import com.ibm.verifydemo.GetQuote;
import com.ibm.verifydemo.QuoteApproval;
import com.ibm.verifydemo.QuoteOrPolicy;
import com.ibm.verifydemo.QuoteOrPolicyAdapter;
import com.ibm.verifydemo.R;
import com.ibm.verifydemo.TrustMeInsuranceApp;
import com.ibm.verifydemo.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class QuoteFragment extends Fragment {

    //private QuoteViewModel quoteViewModel;
    private RecyclerView quoteRV;
    private ArrayList<QuoteOrPolicy> quoteOrPolicyArrayList;
    private String authtoken,access_token;
    JSONObject obj=new JSONObject();
    View root;
    public static boolean homeQuoteAccept=false,carQuoteAccept=false;
    public static String homeQuoteName,carQuoteName,homeQuoteNo,carQuoteNo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_quote, container, false);
        super.onCreate(savedInstanceState);
        //root.setContentView(R.layout.fragment_quote);
        quoteRV = root.findViewById(R.id.idRVQuote);

        //get access token
        authtoken= HomeFragment.tokenforfragments;

        FloatingActionButton myFab = (FloatingActionButton)root.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(TrustMeInsuranceApp.getContext(), GetQuote.class);
                startActivity(intent);

            }
        });

        // here we have created new array list and added data to it.
        //JSON = QuoteOrPolicyService.getUserInfoAPI()
        getUserInfoAPI();
        //ArrayList quoteOrPolicyArrayList = QuoteOrPolicyService.createQuoteObjectList(JSON)
        quoteOrPolicyArrayList = new ArrayList<>();
        return root;
    }

    public void getUserInfoAPI() {

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
                            boolean carquotename_found=false;
                            for(int i=0;i<custom_att.length();i++)
                            {
                                JSONObject item = custom_att.getJSONObject(i);
                                Iterator<String> keys = item.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();

                                    //HomeType
                                    if((key.equals("name")) && (item.get(key).equals("homeType"))){
                                        if(prev_value.equals("none"))
                                            obj.put("homeType","Apartment");
                                        else
                                            obj.put("homeType",prev_value);
                                    }

                                    //ageHome
                                    if((key.equals("name")) && (item.get(key).equals("ageHome"))){
                                        if(prev_value.equals("undefined"))
                                            obj.put("ageHome","unknown");
                                        else
                                            obj.put("ageHome",prev_value);
                                    }

                                    //carYear
                                    if((key.equals("name")) && (item.get(key).equals("carYear"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carYear","Unknown");
                                        else
                                            obj.put("carYear",prev_value);
                                    }

                                    //carMake
                                    if((key.equals("name")) && (item.get(key).equals("carMake"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carMake","Unknown");
                                        else
                                            obj.put("carMake",prev_value);
                                    }

                                    //carModel
                                    if((key.equals("name")) && (item.get(key).equals("carModel"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("carModel","Unknown");
                                        else
                                            obj.put("carModel",prev_value);
                                    }

                                    //idv
                                    if((key.equals("name")) && (item.get(key).equals("idv"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals(""))
                                            obj.put("idv","$700000");
                                        else
                                            obj.put("idv","$"+prev_value);
                                    }

                                    //CarQuoteAccept
                                    if((key.equals("name")) && (item.get(key).equals("carquoteaccept"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals("")||prev_value.equals("false")||prev_value.equals(false))
                                            carQuoteAccept=false;
                                        else if(prev_value.equals("true")||prev_value.equals(true))
                                            carQuoteAccept=true;
                                    }

                                    //HomeQuoteAccept
                                    if((key.equals("name")) && (item.get(key).equals("homeQuoteAccept"))){
                                        if(prev_value.equals("undefined")||prev_value.equals("none")||prev_value.equals("")||prev_value.equals("false")||prev_value.equals(false))
                                            homeQuoteAccept=false;
                                        else if(prev_value.equals("true")||prev_value.equals(true))
                                            homeQuoteAccept=true;
                                    }

                                    //CarQuoteAccept
                                    if((key.equals("name")) && (item.get(key).equals("CarQuoteName"))){
                                        carQuoteName=prev_value;
                                        carquotename_found=true;
                                        break;
                                    }

                                    //HomeQuoteAccept
                                    if((key.equals("name")) && (item.get(key).equals("HomeQuoteName"))){
                                        homeQuoteName=prev_value;
                                    }

                                    if(carquotename_found)
                                        break;

                                    if(key.equals("values"))
                                    {
                                        prev_value=item.getJSONArray(key).getString(0);
                                    }
                                }
                            }
                            quoteDisplay();
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

    public void quoteDisplay() {
        //ArrayList quoteOrPolicyArrayList = QuoteOrPolicyService.createPolicyObjectList(JSON)
        //quoteOrPolicyArrayList.add(new QuoteOrPolicy("Q","QL-9876-0702", "Age : 38yrs\nCoverage : Upto 80yrs\nSI : $2,55,000", R.drawable.life));

        //For car
        if(obj.has("carMake") && !carQuoteAccept) {

            if(carQuoteName.equals("")) {
                String randomFirstFour = ""+((int)(Math.random()*9000)+1000);
                String randomLastFour = ""+((int)(Math.random()*9000)+1000);
                carQuoteNo = randomFirstFour + "-" + randomLastFour;
                carQuoteName = "QC-" + carQuoteNo;
            }
            try {

                String car_policy_desc = "Car Model : " + obj.get("carMake") + " " + obj.get("carModel") + "\n" + "Make Year : " + obj.get("carYear")+"\nIDV : $65,000" ;
                quoteOrPolicyArrayList.add(new QuoteOrPolicy("Q", carQuoteName, car_policy_desc, R.drawable.car));
                //
            } catch (JSONException e) {
                e.printStackTrace();
                quoteOrPolicyArrayList.add(new QuoteOrPolicy("Q", carQuoteName, "Car model : Audi XYZ\nMake Year : 2005\nIDV : $65,000", R.drawable.car));
            }
        }

        //For home
        if(obj.has("idv")&& !homeQuoteAccept) {
            if(homeQuoteName.equals("")) {
                String randomFirstFour = "" + ((int) (Math.random() * 9000) + 1000);
                String randomLastFour = "" + ((int) (Math.random() * 9000) + 1000);
                homeQuoteNo = randomFirstFour + "-" + randomLastFour;
                homeQuoteName = "QH-" + homeQuoteNo;
            }
            try {
                String home_policy_desc = "Home Type : " + obj.get("homeType") + "\n" + "Year : " + obj.get("ageHome") + "\nIDV : " + obj.get("idv");
                quoteOrPolicyArrayList.add(new QuoteOrPolicy("Q", homeQuoteName, home_policy_desc, R.drawable.home));

            } catch (JSONException e) {
                e.printStackTrace();
                quoteOrPolicyArrayList.add(new QuoteOrPolicy("Q", homeQuoteName, "Home Type : Apartment\nYear : 2010\nIDV : $1,22,000", R.drawable.home));
            }
        }

        // we are initializing our adapter class and passing our arraylist to it.
        QuoteOrPolicyAdapter courseAdapter = new QuoteOrPolicyAdapter(TrustMeInsuranceApp.getContext(), quoteOrPolicyArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TrustMeInsuranceApp.getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        quoteRV.setLayoutManager(linearLayoutManager);
        quoteRV.setAdapter(courseAdapter);

    }


}