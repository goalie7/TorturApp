package perez.marcos.torturapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import extras.UserHelper;


public class ProfileFragment extends Fragment {

    UserHelper uh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(R.string.title_section4);
        return rootView;
    }
}

