package login;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import extras.MyDialogList;
import extras.UserHelper;
import perez.marcos.torturapp.R;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Registro extends Activity {
    ImageView img;
    TextView tv_mail;
    TextView tv_user;
    TextView tv_pass;
    TextView tv_pass2;
    TextView tv_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tv_mail = (TextView) findViewById(R.id.mail);
        tv_user = (TextView) findViewById(R.id.username);
        tv_pass = (TextView) findViewById(R.id.pass);
        tv_pass2 = (TextView) findViewById(R.id.pass2);
        tv_address = (TextView) findViewById(R.id.address);


        //evita que se abra el teclado al inciarse la activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //
        img = (ImageView) findViewById(R.id.avatar);
    }

    public void dialog(View v) {
        new MyDialogList().show(getFragmentManager(), "regdialogfragment");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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

    public void reg(View v) {
        ContentValues valuesToStore = new ContentValues();
        String mail = tv_mail.getText().toString();
        String username = tv_user.getText().toString();
        String pass = tv_pass.getText().toString();
        String pass2 = tv_pass2.getText().toString();
        String address = tv_address.getText().toString();

        if (!mail.contains("@")) {
            //TOAST
            Toast.makeText(getApplicationContext(), "No es un mail valido", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(pass2)) {
            //TOAST
            Toast.makeText(getApplicationContext(), "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        } else if (username.equals("")) {
            //TOAST no vacío
            Toast.makeText(getApplicationContext(), "Nombre de usuario no puede ser vacío", Toast.LENGTH_SHORT).show();
        } else {
            valuesToStore.put("username", username);
            valuesToStore.put("mail", mail);
            valuesToStore.put("pass", pass);
            valuesToStore.put("address", address);
            //valuesToStore.put("avatar", );
            UserHelper uh = new UserHelper(getApplicationContext());
            if (uh.checkMail(mail)) {
                Toast.makeText(getApplicationContext(), "Ya existe este email", Toast.LENGTH_SHORT).show();
            }
            if (uh.checkUser(username)) {
                Toast.makeText(getApplicationContext(), "Ya existe el nombre de usuario", Toast.LENGTH_SHORT).show();
            } else {
                uh.createUser(valuesToStore);
                Toast.makeText(getApplicationContext(), "Registrado correctamente", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
