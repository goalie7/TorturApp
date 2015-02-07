package login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import extras.UserHelper;
import io.fabric.sdk.android.Fabric;
import perez.marcos.torturapp.MainActivity;
import perez.marcos.torturapp.R;

public class Login extends Activity {

    private TwitterLoginButton loginButton;
    SharedPreferences sp;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VnyLBPHJBps36Oh4YOJpjarnM";
    private static final String TWITTER_SECRET = "jUy7Br9MLVzSrub0W28qsSnjNLQqswnbVYNN1QVFgh2Eg54QIP";
    private ImageView mail;
    private ImageView twitter;
    private ImageView facebook;
    Button bLogin;
    UserHelper uh;
    EditText tv_user;
    EditText tv_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        mail = (ImageView) findViewById(R.id.mail);
        bLogin = (Button) findViewById(R.id.buttonLogin);
        tv_pass = (EditText) findViewById(R.id.editText2);
        tv_user = (EditText) findViewById(R.id.editText);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                //Guardar el username en la base de datos de logeados.
                //Crear un usuario sin contraseña en la base de datos de usuarios.
                //TODO CREAR PERFIL E INTRODUCIR EN BD COMO LOGGED
                if(result!= null) Log.v("result", result.data.getUserName());
                else Log.v("result", "RESULT NULL");
                if(result.response == null){
                        Log.v("response","RESPONSE NULL");
                }
                else {
                    Log.v("response", "Reason: " + result.response.getReason());
                    Log.v("response", "URL: " + result.response.getUrl());
                    Log.v("response", "BODY: " + result.response.getBody());
                    Log.v("response", "HEADERS: " + result.response.getHeaders());
                    Log.v("response", "STATUS: " + result.response.getStatus());
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getApplicationContext(), "PROBLEMO", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void mail(View v) {
        giro(mail);
        Runnable clickButton = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Registro.class);
                startActivity(intent);
            }
        };
        mail.postDelayed(clickButton, 500);
    }

    public void tryLogin(View v) {
        String user = tv_user.getText().toString();
        String pass = tv_pass.getText().toString();
        uh = new UserHelper(getApplicationContext());
        Intent i;
        if (uh.login(user, pass)) {
            //Intent intent = new Intent(getApplicationContext(),Calc1.class);
            //startActivity(intent);
            Toast.makeText(getApplicationContext(), "LOGIN CORRECTO", Toast.LENGTH_SHORT).show();
            i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            i = new Intent(getApplicationContext(), LoginIncorrecto.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void giro(ImageView img) {
        RotateAnimation rotateAnimation1 = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setInterpolator(new LinearInterpolator());
        rotateAnimation1.setDuration(500);
        rotateAnimation1.setRepeatCount(0);
        img.startAnimation(rotateAnimation1);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText t1 = (EditText) findViewById(R.id.editText);
        EditText t2 = (EditText) findViewById(R.id.editText2);

        outState.putString("user", t1.getText().toString());
        outState.putString("pass", t2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText t1 = (EditText) findViewById(R.id.editText);
        EditText t2 = (EditText) findViewById(R.id.editText2);
        t1.setText(savedInstanceState.getString("user"));
        t2.setText(savedInstanceState.getString("pass"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

