package perez.marcos.torturapp;

import android.app.Fragment;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import extras.MyBoundService;


/**
 * @author Carles Guivernau
 */
public class ReproductorFragment extends Fragment implements View.OnClickListener{
    MyBoundService bService = null;
    boolean bound = false;
    boolean play;
    ImageView played;
    ImageView stop;
    public static final String FILE = "reproductor";
    Intent intent;
    IntentFilter filter;


    BroadcastReceiver receiver;

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder s) {
            MyBoundService.MyBinder binder = (MyBoundService.MyBinder) s;
            bService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (play) {
                    makePlay();
                    bService.pause();
                    play = false;
                } else {
                    makePause();
                    bService.play();
                    play = true;
                }
                break;
            case R.id.button2:
                makePlay();
                play = false;
                bService.stop();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver,filter);
    }

    @Override

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("play", play);
        editor.commit();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reproductor, container, false);
        getActivity().setTitle(R.string.title_section3);

        stop = (ImageView) rootView.findViewById(R.id.button2);
        played = (ImageView) rootView.findViewById(R.id.button);
        stop.setOnClickListener(this);
        played.setOnClickListener(this);

        filter = new IntentFilter();
        filter.addAction("perez.marcos.torturapp.extras.FINISH");
        getActivity().registerReceiver(receiver, filter);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("mp", "RECIBIMOS BROADCAST");
                Toast.makeText(getActivity(), "Cancion finalizada", Toast.LENGTH_LONG).show();
                play = false;
                makePlay();
            }
        };

        //intent = new Intent("BOUNDSERVICE");
        intent = new Intent(getActivity(), MyBoundService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        play = sp.getBoolean("play", false);
        getActivity().startService(new Intent(getActivity(), MyBoundService.class));
        if (play) {
            makePause();
        } else {
            makePlay();
        }
        makeStop();
        return rootView;
    }

    private void makeStop() {
        stop.setImageResource(R.drawable.stop);
    }

    public void makePause() {
        played.setImageResource(R.drawable.pause);
    }

    public void makePlay() {
        played.setImageResource(R.drawable.play);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.apply();
        getActivity().unbindService(connection);
    }
}
