package com.wddonline.minigolf.myminigolfscorecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SplashActivity extends AppCompatActivity {
    int sec = 0;
    ImageView imgBG;
    String imgLink = "";
    String policy_str = "\nThank you for choosing to be part of our community at MyMiniGolf.\n" +
            "MyMiniGolf Scorecared does not collect or store personal information.\n" +
            "Names and photos are shared with your local device only.\n" +
            "if you have any questions are conerns about our policy, or our practies with rgards to your personal information,\n" +
            "please contact us at wddr2d2@gmail.com\n\n\n";
    Boolean first;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if(sec == 2){
                timerHandler.removeCallbacks(timerRunnable);
                if(first) {
                    showAlert(policy_str);
                }
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

            }
            else {
                timerHandler.postDelayed(this, 1000);
                sec++;
                Log.d("tag","asdfasdf");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        imgBG = (ImageView)findViewById(R.id.img_bg);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getColor(R.color.transparent));
            }
        }

        disableSSLCertificateChecking();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        first = sharedPreferences.getBoolean("first",true);

        timerHandler.postDelayed(timerRunnable, 0);
    }
    private void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("     Privacy Policy");
        builder.setMessage(message);
        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("first",false);
                editor.apply();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                try {
                    arg0[0].checkValidity();
                } catch (Exception e) {
                    throw new CertificateException("Certificate not valid or trusted.");
                }
            }
        } };

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    if( s.equals("minigolf.wddonline.com") )
                        return true;
                    else if(s.equals("www.myminigolfscorecard.com"))
                        return true;
                    else if(s.equals("svcs.paypal.com"))
                        return true;
                    else if(s.equals("b.stats.paypal.com"))
                        return true;
                    else if(s.equals("www.paypalobjects.com"))
                        return true;
                    else if(s.equals("slc.stats.paypal.com"))
                        return true;
                    else if(s.equals(""))
                        return false;
                    else return true;
                }
            });
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
