package extras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


import perez.marcos.torturapp.R;

public class MyDialogWin extends DialogFragment {
    private win callback;

    public static MyDialogWin newInstance() {
        MyDialogWin frag = new MyDialogWin();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (win) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    public interface win {
        public void win();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Has ganado!")
                .setPositiveButton("Volver a jugar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                callback.win();
                            }
                        }
                ).create();
    }
}