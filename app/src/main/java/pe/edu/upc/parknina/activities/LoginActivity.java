package pe.edu.upc.parknina.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import pe.edu.upc.parknina.R;
import pe.edu.upc.parknina.networks.ParkninaAsyncTask;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout emailAndPasswordLinearLayout;
    private LinearLayout logInAndSignUpLinearLayout;
    private LinearLayout orAndGuestLinearLayout;
    private ImageView logoImageView;
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    //REMEMBER: Buttons has to be public yo be able to use in class MyAsyncTask
    public Button logInButton;
    public Button signUpButton;
    public Button guestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAndPasswordLinearLayout = findViewById(R.id.emailAndPasswordLinearLayout);
        logInAndSignUpLinearLayout = findViewById(R.id.logIngAndSignUpLinearLayout);
        orAndGuestLinearLayout = findViewById(R.id.orAndGuestLinearLayout);
        logoImageView = findViewById(R.id.loginLogoImageView);
        emailTextInputEditText = findViewById(R.id.emailTextInputEditText);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        logInButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);
        guestButton = findViewById(R.id.guestButton);

        startAnimation();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    logInButton.setEnabled(false);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", emailTextInputEditText.getText().toString());
                        jsonObject.put("password", passwordTextInputEditText.getText().toString());

                        if (jsonObject.length() > 0) {
                            new MyAsyncTask().execute(String.valueOf(jsonObject));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), GuestMapsActivity.class));
                finish();
            }
        });
    }

    private void startAnimation() {
        final Animator logo_fade_in = AnimatorInflater.loadAnimator(this, R.animator.fade_in)
                .setDuration(2000);
        logo_fade_in.setTarget(logoImageView);
        final Animator emailAndPasswordLayout_fade_in = AnimatorInflater.loadAnimator(this, R.animator.fade_in)
                .setDuration(1500);
        emailAndPasswordLayout_fade_in.setTarget(emailAndPasswordLinearLayout);
        final Animator logInAndSignUpLayout_fade_in = AnimatorInflater.loadAnimator(this, R.animator.fade_in)
                .setDuration(1500);
        logInAndSignUpLayout_fade_in.setTarget(logInAndSignUpLinearLayout);
        final Animator orAndGuestLayout_fade_in = AnimatorInflater.loadAnimator(this, R.animator.fade_in)
                .setDuration(1500);
        orAndGuestLayout_fade_in.setTarget(orAndGuestLinearLayout);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            emailAndPasswordLinearLayout.setVisibility(View.INVISIBLE);
            logInAndSignUpLinearLayout.setVisibility(View.INVISIBLE);
            orAndGuestLinearLayout.setVisibility(View.INVISIBLE);

            /*float density = getResources().getDisplayMetrics().density;
            float aa = getResources().getDisplayMetrics().widthPixels / 2;
            float bb = getResources().getDisplayMetrics().heightPixels / 2;
            float a = (getResources().getDimensionPixelSize(R.dimen.width_loginLogoImageWiew) / density) / 2;
            float b = (getResources().getDimensionPixelSize(R.dimen.height_loginLogoImageWiew) / density) / 2;
            logoImageView.setY(bb - b);*/

            final AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet.play(logo_fade_in);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    emailAndPasswordLinearLayout.setVisibility(View.VISIBLE);
                    logInAndSignUpLinearLayout.setVisibility(View.VISIBLE);
                    orAndGuestLinearLayout.setVisibility(View.VISIBLE);
                    animatorSet2.playTogether(emailAndPasswordLayout_fade_in, logInAndSignUpLayout_fade_in, orAndGuestLayout_fade_in);
                    animatorSet2.start();
                }
            });
            animatorSet.start();
        }
        else {
            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(emailAndPasswordLayout_fade_in, logInAndSignUpLayout_fade_in, orAndGuestLayout_fade_in);
            animatorSet.start();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<String, String, String> {
        private JSONObject jsonResponse = null;
        private String jsonRequest = null;
        private String myData = null;
        private HttpURLConnection urlConnection = null;
        private InputStream inputStream = null;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonRequest = params[0];

            urlConnection = ParkninaAsyncTask.connectionConfigure("/login", "POST");
            ParkninaAsyncTask.sendHeadersAndMethod(urlConnection, jsonRequest);
            try {
                inputStream = urlConnection.getInputStream();

                myData = ParkninaAsyncTask.convertInputStreamToString(inputStream);
                try {
                    jsonResponse = new JSONObject(myData);
                    if (jsonResponse.getString("StatusCode").equals("200")) {
                        SharedPreferences pref = getApplication().getSharedPreferences("preferences", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("auth_token", jsonResponse.getString("token"));
                        editor.apply();
                        Log.i("TAG", pref.getString("auth_token", null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("TAG", myData);

                return myData;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (jsonResponse.getString("StatusCode").equals("200")) {
                    SharedPreferences pref = getApplication().getSharedPreferences("preferences", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("auth_token", jsonResponse.getString("token"));
                    editor.apply();
                    Log.i("TAG", pref.getString("auth_token", null));

                    startActivity(new Intent(LoginActivity.this, LoggedMapsActivity.class));
                    finish();
                }
                else if (jsonResponse.getString("StatusCode").equals("1000")) {
                    Toast.makeText(getBaseContext(), "Email and password incorrect. Please, try again.", Toast.LENGTH_SHORT).show();
                }

                logInButton.setEnabled(true);
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private boolean validate(){
        if (emailTextInputEditText.getText().toString().trim().equals("") && passwordTextInputEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Please, write your email and password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (emailTextInputEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Please, write your email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTextInputEditText.getText()).matches()) {
            Toast.makeText(getBaseContext(), "This email address is invalid.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (passwordTextInputEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Please, write your password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (passwordTextInputEditText.getText().length() <= 4) {
            Toast.makeText(getBaseContext(), "This password is to short.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
