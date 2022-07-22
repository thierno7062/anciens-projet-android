package sn.fallou_syll.covid_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import sn.fallou_syll.covid_project.menu;


import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends menu {
    private ListView listView;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;

    private static final int MY_REQUEST_CODE = 1000;

    private final List<Note> noteList = new ArrayList<Note>();
    private ArrayAdapter<Note> listViewAdapter;

    private Button presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = (Button) findViewById(R.id.btn_se_presenter);

        presenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), presentation.class);
                startActivity(intent);
            }
        });

    }
}