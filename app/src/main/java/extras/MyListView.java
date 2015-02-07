package extras;

/**
 * Created by Marcos on 03/02/2015.
 */

import android.content.Context;
import android.widget.ListView;

import perez.marcos.torturapp.R;

public class MyListView extends ListView {

    final String[] menus = getResources().getStringArray(R.array.menu);

    public MyListView(Context c) {
        super(c);
        setAdapter(new MyArrayAdapter(c, menus));
    }
}