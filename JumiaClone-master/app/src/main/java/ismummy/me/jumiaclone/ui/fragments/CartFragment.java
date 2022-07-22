package ismummy.me.jumiaclone.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ismummy.me.jumiaclone.R;
import ismummy.me.jumiaclone.ui.activities.MainActivity;
import ismummy.me.jumiaclone.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {

    public static CartFragment newInstance(){
        return new CartFragment();
    }

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @OnClick(R.id.btn_continue_shopping)
    void continueShoppingClicked(){
        ((MainActivity) getActivity()).setViewPagerIndex(0);
    }

}
