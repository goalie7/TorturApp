package extras;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import perez.marcos.torturapp.R;

/**
 * Created by Marcos on 01/02/2015.
 */
public class Notification {
    int icon = R.drawable.ic_launcher;
    CharSequence title = "title";
    CharSequence resume = "resume";
    CharSequence text = "text";
    long when = System.currentTimeMillis();
    ;
    Context context;

    public Notification() {
    }

    public void setContext(Context c) {
        context = c;
    }

    public void setIcon(int i) {
        icon = i;
    }

    public void setTitle(CharSequence s) {
        title = s;
    }

    public void setText(CharSequence s) {
        text = s;
    }


    public void setResume(CharSequence s) {
        resume = s;
    }

    public void build() {
        NotificationManager myNManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        android.app.Notification notification = new android.app.Notification(icon, resume, when);
        Intent notificationIntent = new Intent(context, context.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, text, contentIntent);
        //notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= android.app.Notification.FLAG_INSISTENT;
        myNManager.notify(1, notification);
    }
}
