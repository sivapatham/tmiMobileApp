package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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

public class EULA extends AppCompatActivity {

    String uid,app_token;
    public String EULAPurposeId="ea7e3b65-218c-42cf-96c4-a3783feb2a8a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);
        Intent intent = getIntent();
        String version= String.valueOf(intent.getIntExtra("Version",1));
        String date = intent.getStringExtra("date");
        String ref = intent.getStringExtra("ref");
        uid = intent.getStringExtra("uid");
        app_token = intent.getStringExtra("App token");

        TextView EULATitle = (TextView)findViewById(R.id.EULATitle);
        String Eula = "End User License Agreement (Version " + version +")";
        EULATitle.setText(Eula);

        TextView EULADesc = (TextView)findViewById(R.id.EULADescription);
        String EulaDesc = "The terms of service was updated on Tue ,"+ date+". Please review and accept the new terms before accessing your profile.";
        EULADesc.setText(EulaDesc);

        WebView webView = (WebView) findViewById(R.id.webviewEULA);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        final String descriptionUsingWebView = "<h1>Interpretation and Definitions</h1>"+
                "<h2>Interpretation</h2>"+
                "<p>The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.</p>"+
                "<h2>Definitions</h2>"+
                "<p>For the purposes of this End-User License Agreement:</p>"+
                "<ul>"+
                "<li>"+
                "<p><strong>Agreement</strong> means this End-User License Agreement that forms the entire agreement between You and the Company regarding the use of the Application. This Agreement has been created with the help of the <a href=\"https://www.termsfeed.com/eula-generator/\">EULA Generator</a>.</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Application</strong> means the software program provided by the Company downloaded by You to a Device, named TrustMeInsurance</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Company</strong> (referred to as either &quot;the Company&quot;, &quot;We&quot;, &quot;Us&quot; or &quot;Our&quot; in this Agreement) refers to IBM, EGL, Karnataka, Bengaluru.</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Content</strong> refers to content such as text, images, or other information that can be posted, uploaded, linked to or otherwise made available by You, regardless of the form of that content.</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Country</strong> refers to: Karnataka,India</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Device</strong> means any device that can access the Application such as a computer, a cellphone or a digital tablet.</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>Third-Party Services</strong> means any services or content (including data, information, applications and other products services) provided by a third-party that may be displayed, included or made available by the Application.</p>"+
                "</li>"+
                "<li>"+
                "<p><strong>You</strong> means the individual accessing or using the Application or the company, or other legal entity on behalf of which such individual is accessing or using the Application, as applicable.</p>"+
                "</li>"+
                "</ul>"+
                "<h1>Acknowledgment</h1>"+
                "<p>By clicking the &quot;I Agree&quot; button, downloading or using the Application, You are agreeing to be bound by the terms and conditions of this Agreement. If You do not agree to the terms of this Agreement, do not click on the &quot;I Agree&quot; button, do not download or do not use the Application.</p>"+
                "<p>This Agreement is a legal document between You and the Company and it governs your use of the Application made available to You by the Company.</p>"+
                "<p>The Application is licensed, not sold, to You by the Company for use strictly in accordance with the terms of this Agreement.</p>"+
                "<h1>License</h1>"+
                "<h2>Scope of License</h2>"+
                "<p>The Company grants You a revocable, non-exclusive, non-transferable, limited license to download, install and use the Application strictly in accordance with the terms of this Agreement.</p>"+
                "<p>The license that is granted to You by the Company is solely for your personal, non-commercial purposes strictly in accordance with the terms of this Agreement.</p>"+
                "<h1>Third-Party Services</h1>"+
                "<p>The Application may display, include or make available third-party content (including data, information, applications and other products services) or provide links to third-party websites or services.</p>"+
                "<p>You acknowledge and agree that the Company shall not be responsible for any Third-party Services, including their accuracy, completeness, timeliness, validity, copyright compliance, legality, decency, quality or any other aspect thereof. The Company does not assume and shall not have any liability or responsibility to You or any other person or entity for any Third-party Services.</p>"+
                "<p>You must comply with applicable Third parties' Terms of agreement when using the Application. Third-party Services and links thereto are provided solely as a convenience to You and You access and use them entirely at your own risk and subject to such third parties' Terms and conditions.</p>"+
                "<h1>Term and Termination</h1>"+
                "<p>This Agreement shall remain in effect until terminated by You or the Company."+
                        "The Company may, in its sole discretion, at any time and for any or no reason, suspend or terminate this Agreement with or without prior notice.</p>"+
                "<p>This Agreement will terminate immediately, without prior notice from the Company, in the event that you fail to comply with any provision of this Agreement. You may also terminate this Agreement by deleting the Application and all copies thereof from your Device or from your computer.</p>"+
                "<p>Upon termination of this Agreement, You shall cease all use of the Application and delete all copies of the Application from your Device.</p>"+
                "<p>Termination of this Agreement will not limit any of the Company's rights or remedies at law or in equity in case of breach by You (during the term of this Agreement) of any of your obligations under the present Agreement.</p>"+
                                "<h1>Indemnification</h1>"+
                "<p>You agree to indemnify and hold the Company and its parents, subsidiaries, affiliates, officers, employees, agents, partners and licensors (if any) harmless from any claim or demand, including reasonable attorneys' fees, due to or arising out of your: (a) use of the Application; (b) violation of this Agreement or any law or regulation; or (c) violation of any right of a third party.</p>"+
                                "<h1>No Warranties</h1>"+
                "<p>The Application is provided to You &quot;AS IS&quot; and &quot;AS AVAILABLE&quot; and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Application, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Application will meet your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.</p>"+
                "<p>Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Application, or the information, content, and materials or products included thereon; (ii) that the Application will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Application; or (iv) that the Application, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.</p>"+
                                "<p>Some jurisdictions do not allow the exclusion of certain types of warranties or limitations on applicable statutory rights of a consumer, so some or all of the above exclusions and limitations may not apply to You. But in such a case the exclusions and limitations set forth in this section shall be applied to the greatest extent enforceable under applicable law. To the extent any warranty exists under law that cannot be disclaimed, the Company shall be solely responsible for such warranty.</p>"+
                "<h1>Limitation of Liability</h1>"+
                "<p>Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Agreement and your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You for the Application or through the Application or 100 USD if You haven't purchased anything through the Application.</p>"+
                                "<p>To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Application, third-party software and/or third-party hardware used with the Application, or otherwise in connection with any provision of this Agreement), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.</p>"+
                "<p>Some states/jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to You.</p>"+
                "<h1>Severability and Waiver</h1>"+
                "<h2>Severability</h2>"+
                "<p>If any provision of this Agreement is held to be unenforceable or invalid, such provision will be changed and interpreted to accomplish the objectives of such provision to the greatest extent possible under applicable law and the remaining provisions will continue in full force and effect.</p>"+
                "<h2>Waiver</h2>"+
                "<p>Except as provided herein, the failure to exercise a right or to require performance of an obligation under this Agreement shall not effect a party's ability to exercise such right or require such performance at any time thereafter nor shall be the waiver of a breach constitute a waiver of any subsequent breach.</p>"+
                                "<h1>Product Claims</h1>"+
                "<p>The Company does not make any warranties concerning the Application.</p>"+
                "<h1>United States Legal Compliance</h1>"+
                "<p>You represent and warrant that (i) You are not located in a country that is subject to the United States government embargo, or that has been designated by the United States government as a &quot;terrorist supporting&quot; country, and (ii) You are not listed on any United States government list of prohibited or restricted parties.</p>"+
                "<h1>Changes to this Agreement</h1>"+
                "<p>The Company reserves the right, at its sole discretion, to modify or replace this Agreement at any time. If a revision is material we will provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at the sole discretion of the Company.</p>"+
                                "<p>By continuing to access or use the Application after any revisions become effective, You agree to be bound by the revised terms. If You do not agree to the new terms, You are no longer authorized to use the Application.</p>"+
                "<h1>Governing Law</h1>"+
                "<p>The laws of the Country, excluding its conflicts of law rules, shall govern this Agreement and your use of the Application. Your use of the Application may also be subject to other local, state, national, or international laws.</p>"+
                "<h1>Entire Agreement</h1>"+
                "<p>The Agreement constitutes the entire agreement between You and the Company regarding your use of the Application and supersedes all prior and contemporaneous written or oral agreements between You and the Company.</p>"+
                "<p>You may be subject to additional terms and conditions that apply when You use or purchase other Company's services, which the Company will provide to You at the time of such use or purchase.</p>"+
                                "<h1>Contact Us</h1>"+
                "<p>If you have any questions about this Agreement, You can contact Us:</p>"+
                "<ul>"+
                "<li>By email: Saloni.Deepak.Kumar.Rathi@ibm.com</li>"+
                "</ul>";
        webView.loadDataWithBaseURL(null, descriptionUsingWebView, "text/html", "utf-8", null);




    }
    public void onAgree(View v){
        CheckBox cb_EULA = (CheckBox)findViewById(R.id.checkBox_EULA);
        //if checkbox checked
        if(cb_EULA.isChecked()){
            //Create Consent
            consentCreation(EULAPurposeId,1);

        }
        else{
            //Toast
            showToast("Please accept the updated End User License Agreement");
        }





    }

    public void consentCreation(String purposeId,int state)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        try
        {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("isGlobal",true);
            jsonBody.put("subjectId",uid);
            jsonBody.put("purposeId",purposeId);
            jsonBody.put("state",state);
            jsonBody.put("accessTypeId","default");
            jsonBody.put("isExternalSubject", false);
            final String mRequestBody = jsonBody.toString();
            Log.d("EULACC",mRequestBody);
            //final String url_consent="https://cloudidentity1234.ice.ibmcloud.com/dpcm/v1.0/privacy/consents";
            String url_consent = TrustMeInsuranceApp.getConsentsUrl();

            System.out.println("Calling API - " + url_consent);
            System.out.println("JSON Input to API - " + mRequestBody.toString());
            StringRequest sr = new StringRequest(Request.Method.POST, url_consent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("EULACCHttpClient", "success! response: " + response);
                            if(state==1){
                                Intent intent;
                                intent = new Intent(EULA.this, HomeActivity.class);
                                intent.putExtra("App token", app_token);
                                startActivity(intent);
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

    private void showToast(final String message) {
        EULA.this.runOnUiThread(() -> {
            Toast toast = Toast.makeText(EULA.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
    }
}