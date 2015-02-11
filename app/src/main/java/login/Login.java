package login;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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

    private static final String FILE = "logged";
    private TwitterLoginButton loginButton;
    private SharedPreferences sp;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VnyLBPHJBps36Oh4YOJpjarnM";
    private static final String TWITTER_SECRET = "jUy7Br9MLVzSrub0W28qsSnjNLQqswnbVYNN1QVFgh2Eg54QIP";
    private ImageView mail;
    Button bLogin;
    UserHelper uh;
    EditText tv_user;
    EditText tv_pass;
    Intent i;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uh = new UserHelper(getApplicationContext());
        sp = getSharedPreferences(FILE, Context.MODE_PRIVATE);
        String s = sp.getString("logged", null);
        if(s != null) {
            i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_login);

        mail = (ImageView) findViewById(R.id.mail);
        bLogin = (Button) findViewById(R.id.buttonLogin);
        tv_pass = (EditText) findViewById(R.id.editText2);
        tv_user = (EditText) findViewById(R.id.editText);

        //LOGIN VIA TWITTER
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                         if(result== null) Log.v("result", null);
                else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("logged", result.data.getUserName());
                    editor.apply();
                    ContentValues c = new ContentValues();
                    c.put("username",result.data.getUserName());
                    uh.createUser(c);
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getApplicationContext(), "PROBLEMO", Toast.LENGTH_LONG).show();
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
        Intent i;
        if (uh.login(user, pass)) {
            //Intent intent = new Intent(getApplicationContext(),Calc1.class);
            //startActivity(intent);
            Toast.makeText(getApplicationContext(), "LOGIN CORRECTO", Toast.LENGTH_SHORT).show();
            i = new Intent(getApplicationContext(), MainActivity.class);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("logged",user);
            editor.apply();
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

