package extras;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import perez.marcos.torturapp.R;

/**
 * Created by Marcos on 03/02/2015.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MyArrayAdapter(Context context, String[] values) {
        super(context, R.layout.fragment_navigation_drawer, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_mobile, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);
        if (s.equals(context.getString(R.string.title_section1))) {
            imageView.setImageResource(R.drawable.calculator);
        } else if (s.equals(context.getString(R.string.title_section2))) {
            imageView.setImageResource(R.drawable.cards);
        } else if (s.equals(context.getString(R.string.title_section3))) {
            imageView.setImageResource(R.drawable.profile);
        } else if (s.equals(context.getString(R.string.title_section4))) {
            imageView.setImageResource(R.drawable.music);
        } else if (s.equals(context.getString(R.string.title_section5))) {
            imageView.setImageResource(R.drawable.ranking);
        } else if (s.equals(context.getString(R.string.title_section6))) {
            imageView.setImageResource(R.drawable.logout);
        }
        return rowView;
    }
}

