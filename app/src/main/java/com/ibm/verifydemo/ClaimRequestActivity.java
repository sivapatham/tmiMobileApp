package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ClaimRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private static Timer timeChecker;
    public static boolean timerRunning = false;
    public int startCounter = 120;
    public int counter = startCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_request);
        Log.e("MSG", "Inside onCreate method of ClaimRequestActivity");
        VerifyAPIService.setPush_state("Push status - Unknown");
        VerifyAPIService.setVerify_state("Verify status - Unknown");

        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
        String strMsg = "We have sent a push verification request to your Verify Authenticator... Approve to proceed with claim request.";
        int iStart = strMsg.indexOf("Verify");
        int iEnd = strMsg.indexOf("...");
        txtMsg.setMovementMethod(LinkMovementMethod.getInstance());
        txtMsg.setText(strMsg, TextView.BufferType.SPANNABLE);
        Spannable txtSpan = (Spannable)txtMsg.getText();
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
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
        };
        txtSpan.setSpan(clickSpan, iStart, iEnd + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        VerifyAPIService.performStrongAuth();
        refreshScreen();
        startCounter();
    }

    public void refreshScreen() {
        //Log.e("MSG", "Inside refreshScreen method");
        ((TextView) findViewById(R.id.txtPushState)).setText(VerifyAPIService.getPush_state());
        ((TextView) findViewById(R.id.txtVerifyState)).setText(VerifyAPIService.getVerify_state());
        ((TextView) findViewById(R.id.txtCounter)).setText(String.valueOf(counter));

        if ( VerifyAPIService.isVerifyCheckRunning() ) {
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.imgVerified)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.btnProceed)).setVisibility(View.INVISIBLE);
        } else {
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
            ((ImageView) findViewById(R.id.imgVerified)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.btnProceed)).setVisibility(View.VISIBLE);
            stopCounter();
        }

        if (VerifyAPIService.getPush_state().toUpperCase().contains("UNKNOWN")) {
            ((TextView) findViewById(R.id.txtPushState)).setTextColor(Color.GRAY);
        } else if (VerifyAPIService.getPush_state().toUpperCase().contains("SUCCESS")) {
            ((TextView) findViewById(R.id.txtPushState)).setTextColor(Color.GREEN);
        } else {
            ((TextView) findViewById(R.id.txtPushState)).setTextColor(Color.RED);
        }

        if (VerifyAPIService.getVerify_state().toUpperCase().contains("UNKNOWN")) {
            ((TextView) findViewById(R.id.txtVerifyState)).setTextColor(Color.GRAY);
        } else if (VerifyAPIService.getVerify_state().toUpperCase().contains("SUCCESS")) {
            ((TextView) findViewById(R.id.txtVerifyState)).setTextColor(Color.GREEN);
        } else {
            ((TextView) findViewById(R.id.txtVerifyState)).setTextColor(Color.RED);
        }

        if (counter > (startCounter/3)*2) {
            ((TextView) findViewById(R.id.txtCounter)).setTextColor(Color.GREEN);
        } else if (counter <= (startCounter/3)*2 && counter > (startCounter/3) ) {
            ((TextView) findViewById(R.id.txtCounter)).setTextColor(Color.BLUE);
        } else if ( counter <= (startCounter/3) ) {
            ((TextView) findViewById(R.id.txtCounter)).setTextColor(Color.RED);
        }

        if (!VerifyAPIService.isVerifyCheckRunning() && VerifyAPIService.getVerify_state().toUpperCase().contains("SUCCESS")) {
            ((ImageView) findViewById(R.id.imgVerified)).setImageResource(R.drawable.ic_thumb_up_foreground);
            ((Button)findViewById(R.id.btnProceed)).setText("  Proceed to Claim Request  ");
            ((Button)findViewById(R.id.btnProceed)).setEnabled(true);
            ((Button)findViewById(R.id.btnProceed)).setBackgroundColor(Color.BLUE);
        } else if (!VerifyAPIService.isVerifyCheckRunning() && !VerifyAPIService.getVerify_state().toUpperCase().contains("SUCCESS") ){
            ((ImageView) findViewById(R.id.imgVerified)).setImageResource(R.drawable.ic_thumb_down_foreground);
            ((Button)findViewById(R.id.btnProceed)).setText("  Denied to Claim Request  ");
            ((Button)findViewById(R.id.btnProceed)).setEnabled(false);
            ((Button)findViewById(R.id.btnProceed)).setBackgroundColor(Color.GRAY);
        }
    }

    public void onClick(View v) {
        Log.e("onClick", "Clicked - " + ((Button) v).getText());
        switch (v.getId()) {
            case R.id.btnProceed:
                showDialog("You are successfully authenticated to proceed with Claim Request...!Please go ahead and upload the needed scanned documents in order to claim the policy");
        }
    }

    public void startCounter() {
        if(timeChecker != null) {
            return;
        }
        TimerTask toDo = new TimerTask() {
            public void run() {
                //Log.e("MSG", "Counter value - " + counter);
                counter--;
                if (counter == 0 )
                    stopCounter();
                runOnUiThread(new Runnable() {
                    public void run() {
                        refreshScreen();
                    }
                });
            }
        };

        counter = startCounter;
        timeChecker = new Timer();
        timeChecker.scheduleAtFixedRate(toDo, 0, 1000);
        timerRunning = true;
    }

    public void stopCounter() {
        if (timeChecker != null )
            timeChecker.cancel();
        timeChecker = null;
        timerRunning = false;
        counter = startCounter;
    }

    private void showDialog(final String message) {
        if (message == null) {
            Log.e("ERR","Something went wrong");
        } else {
            ClaimRequestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClaimRequestActivity.this);
                    builder.setTitle("TrustMeInsurance")
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Create intent for sending back to profile page
                                    Intent resultIntent = new Intent(ClaimRequestActivity.this,MainActivity.class);
                                    startActivity(resultIntent);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

}