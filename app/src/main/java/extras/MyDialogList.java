package extras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import perez.marcos.torturapp.R;

import java.util.ArrayList;

public class MyDialogList extends DialogFragment {
    public static MyDialogList newInstance(ArrayList<String> x) {
        return new MyDialogList();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.from_camera));
        arrayAdapter.add(getString(R.string.from_gallery));

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getActivity(), "DESDE CAMARA", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "DESDE GALERÍA", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return b.create();
    }
}