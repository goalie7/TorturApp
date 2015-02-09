package extras;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import perez.marcos.torturapp.ReproductorFragment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Marcos on 03/02/2015.
 */
public class MyBoundService extends Service {

    public static final String INTENT_FINISH = "perez.marcos.torturapp.extras.FINISH";

    private final IBinder binder = new MyBinder();
    MediaPlayer mediaPlayer;
    File sdCard;
    File song;
    boolean stop = true;

    public class MyBinder extends Binder {
        public MyBoundService getService() {
            return MyBoundService.this;
        }
    }

    public void play() {
        if (!stop) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } else {
            try {
                init();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            stop = false;
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (!stop) {
            stop = true;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (sdCard == null) sdCard = Environment.getExternalStorageDirectory();
        if (song == null) song = new File(sdCard.getAbsolutePath() + "/Music/Song.mp3");
    }

    public void init() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent i = new Intent();
                i.putExtra("finish",true);
                i.setAction(INTENT_FINISH);
                stop();
                sendBroadcast(i);
            }
        });
        mediaPlayer.setDataSource(song.getAbsolutePath());
        mediaPlayer.prepare();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (stop) {
                init();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
