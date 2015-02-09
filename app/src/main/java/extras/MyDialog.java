package extras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


import perez.marcos.torturapp.R;

public class MyDialog extends DialogFragment {

    public static MyDialog newInstance() {
        MyDialog frag = new MyDialog();
        return frag;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("TÍTULO")
                .setPositiveButton(R.string.yesButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Intent intent = new Intent(getActivity(), Bandera.class);
                                //startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.noButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .setNeutralButton(R.string.neutralButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getActivity().finish();
                    }
                })
                .create();
    }
}