package login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import perez.marcos.torturapp.R;


public class LoginIncorrecto extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_incorrecto);
    }

    public void back(View v) {
        finish();
    }
}
