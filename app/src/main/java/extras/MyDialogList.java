package extras;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import perez.marcos.torturapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class MyDialogList extends DialogFragment{

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
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                        getActivity().startActivityForResult(takePictureIntent,0);
                        break;
                    case 1:
                        Intent i = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(i,1);
                        break;
                }
            }
        });
        return b.create();
    }
}