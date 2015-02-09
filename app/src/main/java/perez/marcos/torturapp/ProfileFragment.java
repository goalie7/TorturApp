package perez.marcos.torturapp;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import extras.UserHelper;
import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {
    SharedPreferences sp;
    UserHelper uh;
    TextView user;
    TextView record;
    TextView direccion;
    String logged;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(R.string.title_section3);
        sp = this.getActivity().getSharedPreferences("logged", Context.MODE_PRIVATE);
        logged = sp.getString("logged",null);
        user.setText(logged);
        user = (TextView) rootView.findViewById(R.id.res_username);
        record = (TextView) rootView.findViewById(R.id.res_record);
        direccion = (TextView) rootView.findViewById(R.id.res_direccion);
        new getInfo().execute(null, null, null);
        return rootView;
    }


    private class getInfo extends AsyncTask<Void,Void,Cursor> {
        protected Cursor doInBackground(Void... x) {
            return uh.getUser(logged);
        }

        protected void onProgressUpdate(Void... x) {

        }

        protected void onPostExecute(Cursor... x) {
            Cursor c = x[0];
            c.getString(c.getColumnIndex("username"));
            c.getString(c.getColumnIndex("address"));
            c.getString(c.getColumnIndex("mail"));
            c.getString(c.getColumnIndex("best"));
            c.getString(c.getColumnIndex("avatar"));

        }
    }

}

