package ismummy.me.jumiaclone.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ismummy.me.jumiaclone.R;
import ismummy.me.jumiaclone.ui.base.BaseFragment;


public class SavedFragment extends BaseFragment {


    public SavedFragment() {
        // Required empty public constructor
    }


    public static SavedFragment newInstance() {
       return new SavedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }
}
