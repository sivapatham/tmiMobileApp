package com.ibm.verifydemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TrustMeInsuranceApp extends Application {

    private static Context context;
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

    private static String tokenUrl;
    private static String authUrl;
    private static String userinfoUrl;
    private static String meUrl ;
    private static String usersUrl;
    private static String factorsUrl;
    private static String deletefactorsUrl;
    private static String enrollSmsOtpUrl;

    private static String consentsUrl;
    private static String dspUrl;
    private static String duaUrl;
    private static String smsotpverifyUrl;
    private static String exchangeJwtUrl;
    private static String strongAuthUrl;
    private static String verifyStatusUrl;
    private static String pwdResetterUrl;
    private static String mePwdUrl;
    private static String createAttrUrl;
    private static String createCustAttrUrl;



    public void onCreate() {
        Log.d("MSG","In trustmeinsapp");
        super.onCreate();
        TrustMeInsuranceApp.context = getApplicationContext();
        SharedPreferences sharedPreferences = getSharedPreferences(MyCREDS,Context.MODE_PRIVATE);

        //baseVerifyURL = sharedPreferences.getString(baseURL,"https://cloudidentity1234.ice.ibmcloud.com");
        baseVerifyURL = sharedPreferences.getString(baseURL,"");
        Log.d("SharedPref",baseVerifyURL);
        tokenUrl=baseVerifyURL + "/oidc/endpoint/default/token";
        authUrl=baseVerifyURL + "/oidc/endpoint/default/authorize";
        //old userinfoUrl
        //userinfoUrl = baseVerifyURL + "/oidc/endpoint/default/userinfo";
        //new userinfoUrl
        // new userinfoUrl
        userinfoUrl = baseVerifyURL + "/v1.0/endpoint/default/userinfo";
        meUrl = baseVerifyURL + "/v2.0/Me";
        usersUrl=baseVerifyURL + "/v2.0/Users";
        factorsUrl=baseVerifyURL + "/v2.0/factors";
        deletefactorsUrl = baseVerifyURL +"/v2.0/factors/smsotp/";

        consentsUrl=baseVerifyURL + "/dpcm/v1.0/privacy/consents";
        dspUrl = baseVerifyURL +"/dpcm/v1.0/privacy/data-subject-presentation";
        duaUrl = baseVerifyURL +"/dpcm/v1.0/privacy/data-usage-approval";
        smsotpverifyUrl=baseVerifyURL + "/v2.0/factors/smsotp/transient/verifications";
        enrollSmsOtpUrl = baseVerifyURL+ "/v2.0/factors/smsotp";
        exchangeJwtUrl = baseVerifyURL + "/v1.0/socialjwt/exchange";
        strongAuthUrl = baseVerifyURL + "/v1.0/authenticators";
        verifyStatusUrl = baseVerifyURL + "/v1.0/authenticators";
        pwdResetterUrl = baseVerifyURL + "/v1.0/usc/password/resetter/";
        mePwdUrl = baseVerifyURL + "/v2.0/Me/password";
        createAttrUrl = baseVerifyURL +"/v2.0/Schema/attributes";
        createCustAttrUrl = baseVerifyURL +"/v1.0/attributes/";

        //appClientId = sharedPreferences.getString(AppClientID,"c3566725-8262-42cc-b612-0207ce30cdf4");
        appClientId = sharedPreferences.getString(AppClientID,"");
        Log.d("SharedPref",appClientId);

        //appClientSecret = sharedPreferences.getString(AppClientSecret,"z9mJ2kZXSx");
        appClientSecret = sharedPreferences.getString(AppClientSecret,"");
        Log.d("SharedPref",appClientSecret);

        //apiClientId = sharedPreferences.getString(ApiClientID,"3c43de62-e7d6-4fa4-aff9-b8c7cf2d1bfe");
        apiClientId = sharedPreferences.getString(ApiClientID,"");
        Log.d("SharedPref",apiClientId);

        //apiClientSecret = sharedPreferences.getString(ApiClientSecret,"IihyiwNCl3");
        apiClientSecret = sharedPreferences.getString(ApiClientSecret,"");
        Log.d("SharedPref",apiClientSecret);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        TrustMeInsuranceApp.context = context;
    }

    public static String getBaseVerifyURL() {
        return baseVerifyURL;
    }

    public static void setBaseVerifyURL(String baseVerifyURL) {
        TrustMeInsuranceApp.baseVerifyURL = baseVerifyURL;
    }

    public static String getAppClientId() {
        return appClientId;
    }

    public static void setAppClientId(String appClientId) {
        TrustMeInsuranceApp.appClientId = appClientId;
    }

    public static String getAppClientSecret() {
        return appClientSecret;
    }

    public static void setAppClientSecret(String appClientSecret) {
        TrustMeInsuranceApp.appClientSecret = appClientSecret;
    }

    public static String getApiClientId() {
        return apiClientId;
    }

    public static void setApiClientId(String apiClientId) {
        TrustMeInsuranceApp.apiClientId = apiClientId;
    }

    public static String getApiClientSecret() {
        return apiClientSecret;
    }

    public static void setApiClientSecret(String apiClientSecret) {
        TrustMeInsuranceApp.apiClientSecret = apiClientSecret;
    }

    public static String getTokenUrl() {
        return tokenUrl;
    }

    public static void setTokenUrl(String tokenUrl) {
        TrustMeInsuranceApp.tokenUrl = tokenUrl;
    }

    public static String getAuthUrl() {
        return authUrl;
    }

    public static void setAuthUrl(String authUrl) {
        TrustMeInsuranceApp.authUrl = authUrl;
    }

    public static String getUserinfoUrl() {
        return userinfoUrl;
    }

    public static void setUserinfoUrl(String userinfoUrl) {
        TrustMeInsuranceApp.userinfoUrl = userinfoUrl;
    }

    public static String getMeUrl() {
        return meUrl;
    }

    public static void setMeUrl(String meUrl) {
        TrustMeInsuranceApp.meUrl = meUrl;
    }

    public static String getMePwdUrl() {
        return mePwdUrl;
    }

    public static void setMePwdUrl(String mePwdUrl) {
        TrustMeInsuranceApp.mePwdUrl = mePwdUrl;
    }

    public static String getPwdResetterUrl() {
        return pwdResetterUrl;
    }

    public static void setPwdResetterUrl(String pwdResetterUrl) {
        TrustMeInsuranceApp.pwdResetterUrl = pwdResetterUrl;
    }

    public static String getUsersUrl() {
        return usersUrl;
    }

    public static void setUsersUrl(String usersUrl) {
        TrustMeInsuranceApp.usersUrl = usersUrl;
    }

    public static String getConsentsUrl() {
        return consentsUrl;
    }

    public static void setConsentsUrl(String consentsUrl) {
        TrustMeInsuranceApp.consentsUrl = consentsUrl;
    }

    public static String getFactorsUrl() {
        return factorsUrl;
    }

    public static void setFactorsUrl(String factorsUrl) {
        TrustMeInsuranceApp.factorsUrl = factorsUrl;
    }

    public static String getDeleteFactorsUrl() {
        return deletefactorsUrl;
    }

    public static void setDeleteFactorsUrl(String deletefactorsUrl) {
        TrustMeInsuranceApp.deletefactorsUrl = deletefactorsUrl;
    }

    public static String getDspUrl() {
        return dspUrl;
    }

    public static void setDspUrl(String dspUrl) {
        TrustMeInsuranceApp.dspUrl = dspUrl;
    }

    public static String getCreateAttrUrl() {
        return createAttrUrl;
    }

    public static void setCreateAttrUrl(String createAttrUrl) {
        TrustMeInsuranceApp.createAttrUrl = createAttrUrl;
    }

    public static String getCreateCustAttrUrl() {
        return createCustAttrUrl;
    }

    public static void setCreateCustAttrUrl(String createCustAttrUrl) {
        TrustMeInsuranceApp.createCustAttrUrl = createCustAttrUrl;
    }

    public static String getDuaUrl() {
        return duaUrl;
    }

    public static void setDuaUrl(String duaUrl) {
        TrustMeInsuranceApp.duaUrl = duaUrl;
    }

    public static String getEnrollSmsOtpUrl() {
        return enrollSmsOtpUrl;
    }

    public static void setEnrollSmsOtpUrl(String enrollSmsOtpUrl) {
        TrustMeInsuranceApp.enrollSmsOtpUrl = enrollSmsOtpUrl;
    }

    public static String getSmsotpverifyUrl() {
        return smsotpverifyUrl;
    }

    public static void setSmsotpverifyUrl(String smsotpverifyUrl) {
        TrustMeInsuranceApp.smsotpverifyUrl = smsotpverifyUrl;
    }

    public static String getExchangeJwtUrl() {
        return exchangeJwtUrl;
    }

    public static void setExchangeJwtUrl(String exchangeJwtUrl) {
        TrustMeInsuranceApp.exchangeJwtUrl = exchangeJwtUrl;
    }

    public static String getGoogleClientId() {
        return googleClientId;
    }

    public static void setGoogleClientId(String googleClientId) {
        TrustMeInsuranceApp.googleClientId = googleClientId;
    }

    public static String getGoogleClientSecret() {
        return googleClientSecret;
    }

    public static void setGoogleClientSecret(String googleClientSecret) {
        TrustMeInsuranceApp.googleClientSecret = googleClientSecret;
    }

    public static String getGoogleTokenUrl() {
        return googleTokenUrl;
    }

    public static void setGoogleTokenUrl(String googleTokenUrl) {
        TrustMeInsuranceApp.googleTokenUrl = googleTokenUrl;
    }

    public static String getStrongAuthUrl() {
        return strongAuthUrl;
    }

    public static void setStrongAuthUrl(String strongAuthUrl) {
        TrustMeInsuranceApp.strongAuthUrl = strongAuthUrl;
    }

    public static String getVerifyStatusUrl() {
        return verifyStatusUrl;
    }

    public static void setVerifyStatusUrl(String verifyStatusUrl) {
        TrustMeInsuranceApp.verifyStatusUrl = verifyStatusUrl;
    }

    //private static String baseVerifyURL = "https://cloudidentity1234.ice.ibmcloud.com";
    //private static String appClientId = "c3566725-8262-42cc-b612-0207ce30cdf4";
    //private static String appClientSecret = "z9mJ2kZXSx";
    //private static String apiClientId = "3c43de62-e7d6-4fa4-aff9-b8c7cf2d1bfe";
    //private static String apiClientSecret = "IihyiwNCl3";

    //private static String tokenUrl=baseVerifyURL + "/oidc/endpoint/default/token";
    //private static String authUrl=baseVerifyURL + "/oidc/endpoint/default/authorize";
    //old userinfoUrl
    //private static String userinfoUrl = baseVerifyURL + "/oidc/endpoint/default/userinfo";
    // new userinfoUrl
    //private static String userinfoUrl = baseVerifyURL + "/v1.0/endpoint/default/userinfo";
    //private static String meUrl = baseVerifyURL + "/v2.0/Me";
    //private static String usersUrl=baseVerifyURL + "/v2.0/Users";
    //private static String consentsUrl=baseVerifyURL + "/dpcm/v1.0/privacy/consents";
    //private static String dspUrl = baseVerifyURL +"/dpcm/v1.0/privacy/data-subject-presentation";
    //private static String smsotpverifyUrl=baseVerifyURL + "/v2.0/factors/smsotp/transient/verifications";
    //private static String exchangeJwtUrl = TrustMeInsuranceApp.getBaseVerifyURL() + "/v1.0/socialjwt/exchange";
    //private static String factorsUrl = TrustMeInsuranceApp.getBaseVerifyURL() + "/v2.0/factors";
    //private static String strongAuthUrl = TrustMeInsuranceApp.getBaseVerifyURL() + "/v1.0/authenticators";
    //private static String verifyStatusUrl = TrustMeInsuranceApp.getBaseVerifyURL() + "/v1.0/authenticators";

    private static String googleClientId = "351794736086-flnfei34vssl05j18894ldfdcnhj36rf.apps.googleusercontent.com";
    //private static final String gClientId = "144961894759-hlnufe7ah43td4hu22b528g4d9608lth.apps.googleusercontent.com";
    private static String googleClientSecret = "6_EXUSoIPGHjYkFpHA51iTk1";
    //private static final String gClientSecret = "b5CSBOcYjWF1_UDkX3zhPOkh";
    private static String googleTokenUrl = "https://oauth2.googleapis.com/token";

}
