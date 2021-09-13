package com.ibm.verifydemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ibm.verifydemo.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuoteOrPolicyAdapter extends RecyclerView.Adapter<QuoteOrPolicyAdapter.Viewholder> {
        public Context context;
        private ArrayList<QuoteOrPolicy> courseModelArrayList;
        public static final String MSG ="com.ibm.verifydemo.quoteName";
        public String uid = HomeFragment.uid;
        public String access_token=VerifyAPIService.access_token;

        public QuoteOrPolicyAdapter(Context context, ArrayList<QuoteOrPolicy> courseModelArrayList) {
            this.context = context;
            this.courseModelArrayList = courseModelArrayList;
        }

        @NonNull
        @Override
        public QuoteOrPolicyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuoteOrPolicyAdapter.Viewholder holder, int position) {
            // to set data to textview and imageview of each card layout
            QuoteOrPolicy model = courseModelArrayList.get(position);
            holder.quoteName.setText(model.getQuote_name());
            holder.quoteDesc.setText("" + model.getQuote_desc());
            holder.quoteImage.setImageResource(model.getQuote_image());
            String quoteName = model.getQuote_name();
            holder.btnAccept.setOnClickListener(new View.OnClickListener()  {
                public void onClick(View v)     {
                    if ("Accept".equalsIgnoreCase(holder.btnAccept.getText().toString())) {
                        if(quoteName.charAt(1)=='H'){
                            //twoFactorAuth
                            JSONObject obj1 = new JSONObject();
                            try {
                                obj1.put("op","add");
                                String var ="homeQuoteAccept";
                                obj1.put("path","urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes");
                                JSONArray arr = new JSONArray();
                                JSONObject obj = new JSONObject();
                                obj.put("name","HomeQuoteAccept");
                                JSONArray arr1 = new JSONArray();
                                arr1.put("true");
                                obj.put("values",arr1);
                                arr.put(obj);
                                obj1.put("value",arr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Create a json array
                            JSONArray operations1 = new JSONArray();
                            operations1.put(obj1);
                            //Function for updating mfa no
                            updateAttributes(operations1);

                        }
                        else{
                            //twoFactorAuth
                            JSONObject obj1 = new JSONObject();
                            try {
                                obj1.put("op","add");
                                obj1.put("path","urn:ietf:params:scim:schemas:extension:ibm:2.0:User:customAttributes");
                                JSONArray arr = new JSONArray();
                                JSONObject obj = new JSONObject();
                                obj.put("name","CarQuoteAccept");
                                JSONArray arr1 = new JSONArray();
                                arr1.put("true");
                                obj.put("values",arr1);
                                arr.put(obj);

                                obj1.put("value",arr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Create a json array
                            JSONArray operations1 = new JSONArray();
                            operations1.put(obj1);
                            //Function for updating quote realted parameters
                            updateAttributes(operations1);
                        }
                        Intent intent = new Intent(v.getContext(), QuoteApproval.class);
                        v.getContext().startActivity(intent);
                    } else if ("Reject".equalsIgnoreCase(holder.btnAccept.getText().toString())) {
                        //do nothing as of now
                    }
                }
            });

            holder.btnReject.setOnClickListener(new View.OnClickListener()  {
                public void onClick(View v) {
                    if ("Claim".equalsIgnoreCase(holder.btnReject.getText().toString())) {
                        Intent intent = new Intent(v.getContext(), ClaimRequestActivity.class);
                        v.getContext().startActivity(intent);
                    } else if ("Renew".equalsIgnoreCase(holder.btnAccept.getText().toString())) {
                        //do nothing as of now
                    }
                }
            });

            if ( "Q".equalsIgnoreCase(model.getType()) ) {
                holder.btnAccept.setText("Accept");
                holder.btnReject.setText("Reject");
            } else {
                holder.btnAccept.setText("Renew");
                holder.btnReject.setText("Claim");
            }
        }

        @Override
        public int getItemCount() {
            return courseModelArrayList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            private ImageView quoteImage;
            private TextView quoteName, quoteDesc;
            private Button btnAccept, btnReject;

            public Viewholder(@NonNull View itemView) {
                super(itemView);
                quoteImage = itemView.findViewById(R.id.idQuoteImage);
                quoteName = itemView.findViewById(R.id.idQuoteName);
                quoteDesc = itemView.findViewById(R.id.idQuoteDesc);
                btnAccept = itemView.findViewById(R.id.button_accept);
                btnReject = itemView.findViewById(R.id.button_reject);
            }
        }
    private void updateAttributes(JSONArray operations) {
        RequestQueue queue = Volley.newRequestQueue(TrustMeInsuranceApp.getContext());
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


}
