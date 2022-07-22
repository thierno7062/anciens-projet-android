package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.adapter.MessageAdapter;
import com.projets.heven.taxijaune.adapter.RequeteAdapter;
import com.projets.heven.taxijaune.model.MessagePojo;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.onclick.ClickListener;
import com.projets.heven.taxijaune.onclick.RecyclerTouchListener;
import com.projets.heven.taxijaune.settings.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

public class FragmentMessage extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_message;
    public static List<MessagePojo> albumList_message;
    public static MessageAdapter adapter_message;
    public static SwipeRefreshLayout swipe_refresh;

    private LinearLayout layout_not_found,layout_failed;
    private RelativeLayout layout_liste;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            currpos = getArguments().getInt("tab_pos",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_message, container, false);

        context=getActivity();
        connectionDetector=new ConnectionDetector(context);

        albumList_message = new ArrayList<>();
        adapter_message = new MessageAdapter(context, albumList_message);

        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_message = (RecyclerView) view.findViewById(R.id.recycler_view_message);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_message.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_message.setItemAnimator(new DefaultItemAnimator());
        recycler_view_message.setAdapter(adapter_message);

        recycler_view_message.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_message, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MessagePojo messagePojo = albumList_message.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                MessagePojo messagePojo = albumList_message.get(position);
            }
        }));
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getMessage();
            }
        });

        swipe_refresh.setRefreshing(true);
//        getMessage();

        return view;
    }

    /*private void getMessage(){
        swipe_refresh.setRefreshing(false);
        MessagePojo messagePojo;
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019","oui",true);
        albumList_message.add(messagePojo);
        adapter_message.notifyDataSetChanged();
    }*/

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_home,menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.mi_search){
//            Intent it=new Intent(context,SearchActivity.class);
//            startActivity(it);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
