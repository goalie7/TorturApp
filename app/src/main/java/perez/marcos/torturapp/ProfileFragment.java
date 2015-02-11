package perez.marcos.torturapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import extras.UserHelper;

import java.util.List;


public class ProfileFragment extends Fragment implements OnClickListener, LocationListener{
    SharedPreferences sp;
    UserHelper uh;
    TextView user;
    TextView record;
    TextView direccion;
    TextView mail;
    TextView coord1;
    TextView coord2;
    ImageView img;
    String logged;
    Button button;
    Switch gps;
    EditText add;
    boolean state;
    LocationManager lm;
    List<Address> l;
    private String photoPath;
    private Bitmap thumbnail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("CHIV","OCV");
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle(R.string.title_section3);
        uh = new UserHelper(getActivity().getApplicationContext());
        sp = this.getActivity().getSharedPreferences("logged", Context.MODE_PRIVATE);
        logged = sp.getString("logged", null);
        user = (TextView) rootView.findViewById(R.id.res_username);
        record = (TextView) rootView.findViewById(R.id.res_record);
        direccion = (TextView) rootView.findViewById(R.id.res_direccion);
        mail = (TextView) rootView.findViewById(R.id.res_mail);
        img = (ImageView) rootView.findViewById(R.id.avatar);
        gps = (Switch) rootView.findViewById(R.id.toggleButton);
        coord1 = (TextView) rootView.findViewById(R.id.coord1);
        coord2 = (TextView) rootView.findViewById(R.id.coord2);
        button = (Button) rootView.findViewById(R.id.button_direccion);
        gps.setOnClickListener(this);
        button.setOnClickListener(this);
        add = (EditText) rootView.findViewById(R.id.add);
        img.setOnClickListener(this);
        lm = (LocationManager) this
                .getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(savedInstanceState != null) {
            photoPath = sp.getString("path",null);
        }
        if (photoPath == null){
            img.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        }
        else {
            thumbnail = BitmapFactory.decodeFile(photoPath);
            img.setImageBitmap(thumbnail);
        }
        sp.getBoolean("state", false);
        changeState();
        new AsyncCaller().execute();
        return rootView;
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("OSI","LELELELEL");
        SharedPreferences.Editor edit = sp.edit();
        outState.putString("path",photoPath);
        edit.putBoolean("state",state);
        edit.apply();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && resultCode == 1) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            photoPath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            thumbnail = BitmapFactory.decodeFile(photoPath);
            img.setImageBitmap(thumbnail);
            cursor.close();
            int x = uh.updateImage(logged, photoPath);
            if (x == 0) Toast.makeText(getActivity(), "ERROR INSERTANDO", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(),"Foto cancelada",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(),"ERROR",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleButton:
                state = !state;
                changeState();
                break;
            case R.id.button_direccion:
                String dir = add.getText().toString();
                if(dir.equals("")){
                    Toast.makeText(getActivity(), "La dirección no puede ser vacía", Toast.LENGTH_SHORT).show();
                }
                else {
                    int x = uh.updateAddress(logged,dir);
                    if (x != 0){
                        direccion.setText(dir);
                    }
                    else {
                        Toast.makeText(getActivity(), "No se ha modificado nada", Toast.LENGTH_SHORT).show();
                    }
                    add.setText("");
                }
                break;
            case R.id.avatar:
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
                break;
        }
    }

    private void changeState() {
        Log.v("STATE", "CHANGE STATE: " + state);
        gps.setChecked(state);
        if (state) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            lm.removeUpdates(this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        double x = location.getLatitude();
        double y = location.getLongitude();
        coord1.setText(String.valueOf(x));
        coord2.setText(String.valueOf(y));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            Cursor c = uh.getUser(logged);
            if (c != null) {
                logged = c.getString(c.getColumnIndex("username"));
                user.setText(logged);

                String address ="";
                if (!c.isNull(c.getColumnIndex("address"))) address = c.getString(c.getColumnIndex("address"));
                direccion.setText(address);

                String correo ="";
                if (!c.isNull(c.getColumnIndex("mail"))) correo = c.getString(c.getColumnIndex("mail"));
                mail.setText(correo);


                String best = "-";
                if (!c.isNull(c.getColumnIndex("best"))) best = c.getString(c.getColumnIndex("best"));
                record.setText(best);

                if (!c.isNull(c.getColumnIndex("avatar"))) {
                    img.setImageBitmap(BitmapFactory.decodeFile(c.getString(c.getColumnIndex("avatar"))));
                }
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroy();

    }
}

