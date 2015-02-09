package login;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import extras.MyDialogList;
import extras.UserHelper;
import perez.marcos.torturapp.R;


public class Registro extends Activity{
    ImageView img;
    TextView tv_mail;
    TextView tv_user;
    TextView tv_pass;
    TextView tv_pass2;
    TextView tv_address;
    MyDialogList MDL;
    String photoPath = null;
    Bitmap thumbnail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        img = (ImageView) findViewById(R.id.avatar);
        if (savedInstanceState != null) photoPath = savedInstanceState.getString("path",null);
        if (photoPath == null){
            img.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        }
        else {
            thumbnail = BitmapFactory.decodeFile(photoPath);
            img.setImageBitmap(thumbnail);
        }
        tv_mail = (TextView) findViewById(R.id.mail);
        tv_user = (TextView) findViewById(R.id.username);
        tv_pass = (TextView) findViewById(R.id.pass);
        tv_pass2 = (TextView) findViewById(R.id.pass2);
        tv_address = (TextView) findViewById(R.id.address);

        //evita que se abra el teclado al inciarse la activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void dialog(View v) {
        FragmentManager fm = getFragmentManager();
        MDL = new MyDialogList();
        MDL.show(fm, "tag");
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
            Toast.makeText(getApplicationContext(), "No es un mail valido", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(pass2)) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        } else if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Nombre de usuario no puede ser vacío", Toast.LENGTH_SHORT).show();
        } else {
            valuesToStore.put("username", username);
            valuesToStore.put("mail", mail);
            valuesToStore.put("pass", pass);
            valuesToStore.put("address", address);
            valuesToStore.put("avatar",photoPath);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                        photoPath = getLastPhoto();
                        thumbnail = BitmapFactory.decodeFile(photoPath);
                        img.setImageBitmap(thumbnail);
                   break;
                case 1:
                        Uri pickedImage = data.getData();
                        String[] filePath = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                        cursor.moveToFirst();
                        photoPath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                        // Now we need to set the GUI ImageView data with data read from the picked file.
                        thumbnail = BitmapFactory.decodeFile(photoPath);
                        img.setImageBitmap(thumbnail);
                        // At the end remember to close the cursor or you will end with the RuntimeException!
                        cursor.close();
                    break;
            }
        }
        else {
            Toast.makeText(this, R.string.error_camera, Toast.LENGTH_LONG).show();
        }
    }

    private String getLastPhoto(){
        final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        String fullPath = null;
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        imageCursor.moveToFirst();
        do {
            fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if (fullPath.contains("DCIM")) {
                break;
            }
        }
        while (imageCursor.moveToNext());
        return fullPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path",photoPath);
    }

}
