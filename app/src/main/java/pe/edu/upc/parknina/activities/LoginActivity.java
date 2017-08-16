package pe.edu.upc.parknina.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import pe.edu.upc.parknina.R;
import pe.edu.upc.parknina.networks.ParkninaAsyncTask;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailTextInputEditText = (TextInputEditText) findViewById(R.id.emailTextInputEditText);
        passwordTextInputEditText = (TextInputEditText) findViewById(R.id.passwordTextInputEditText);
        Button logInButton = (Button) findViewById(R.id.logInButton);
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        Button guestButton = (Button) findViewById(R.id.guestButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
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
                startActivity(new Intent(v.getContext(), MapsActivity.class));
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        private JSONObject jsonResponse = null;
        private String jsonRequest = null;
        private String myData = null;
        private HttpURLConnection urlConnection = null;
        private InputStream inputStream = null;

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

                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                }
                else if (jsonResponse.getString("StatusCode").equals("1000")) {
                    Toast.makeText(getBaseContext(), "Email and password incorrect. Please, try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
