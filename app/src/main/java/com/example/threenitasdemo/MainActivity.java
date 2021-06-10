package com.example.threenitasdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.threenitasdemo.networking.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private static final String POST_URL = "https://3nt-demo-backend.azurewebsites.net/Access/Login";
    private int password_shown = 0;
    String username= "";
    String password = "";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText userEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passEditText = (EditText) findViewById(R.id.passwordEditText);
        Button logInButton = (Button) findViewById(R.id.loginButton);
        TextView previewTextView = (TextView) findViewById(R.id.previewText);

        //logInButton.setBackground(this.getDrawable(R.drawable.custom_button));

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( validateUsername(userEditText.getText().toString())
                    && validatePassword(passEditText.getText().toString()) ){

                   username = userEditText.getText().toString();
                   password = passEditText.getText().toString();
                   //Go to other Activity
                   String token = "";
                   DownloadTask task = new DownloadTask();
                   try {
                       token = task.execute(POST_URL).get();
                   }catch (Exception e ){
                       e.printStackTrace();
                   }
                   if(!token.isEmpty()){
                       System.out.println("--------------TOKEN IS ====>" + token);
                       Intent i = new Intent(getApplicationContext(), ListActivity.class);
                       i.putExtra("TOKEN", token);
                       startActivity(i);
                   }else{
                       throwAlertDialog();
                   }

               }


            }
        });

        previewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_shown == 0){
                    password_shown = 1;
                    passEditText.setTransformationMethod(null);
                    previewTextView.setText(R.string.hide);
                }else{
                    password_shown = 0;
                    passEditText.setTransformationMethod(new PasswordTransformationMethod());
                    previewTextView.setText(R.string.preview);
                }
            }
        });

    }

    private boolean validateUsername(String username){
        if(username.isEmpty()){
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*if(!username.equals("TH1234")){
            //Not correct username
            throwAlertDialog();
            return false;
        }*/

        return true; //if not empty
        //TODO: Regex validation

    }

    private boolean validatePassword(String password){
        if(password.isEmpty()){
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*if(!password.equals("3NItas1!")){
            //Not correct password
            throwAlertDialog();
            return false;
        }*/

        return true; //if not empty
        //TODO: Regex validation
    }

    private void throwAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Wrong credentials")
                .setMessage("Username or Password is incorrect. Please try again.")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Return", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String token = "";
            //if (stringUrl == null) return token;
            String result = "";
            InputStream inputStream = null;
            URL url;
            try{
                url= new URL(strings[0]);

                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");

                con.setRequestProperty("Content-Type", "application/json; utf-8");

                con.setRequestProperty("Accept", "application/json");

                con.setDoOutput(true);

                String jsonInputString = "{ \"UserName\": \""+username+"\", \"Password\":\""+password+"\" }";
                Log.e("-----JSON INPUT: ", jsonInputString);//for debugging

                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if(con.getResponseCode() == 200){
                    inputStream = con.getInputStream();
                    result = readFromStream(inputStream);

                    token = extractTokenFromJson(result);
                }else{
                    System.out.println("*************COULD NOT FIND ANYTHING*****************");
                    System.out.println("***********RESPONSE CODE: "+con.getResponseCode()+"*******************");
                    return token; //tha epistrefei keno kai meta tha kanw elegxo kai na einai keno tha petaei alert dialog
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            Log.e("---ACCESS TOKEN---: ", token);
            return token;
        }

        public String extractTokenFromJson(String result){
            String t = "";
            if(TextUtils.isEmpty(result)) return null;

            try{
                JSONObject root = new JSONObject(result);
                t = root.getString("access_token");

            }catch(JSONException e){
                e.printStackTrace();
            }


            return t;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();

            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            }

            return output.toString(); //jsonResponse
        }
    }

}