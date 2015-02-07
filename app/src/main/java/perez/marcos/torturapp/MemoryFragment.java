package perez.marcos.torturapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Carles Guivernau
 */
public class MemoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_memory, container, false);
        getActivity().setTitle(R.string.title_section2);
        return rootView;
    }
}
