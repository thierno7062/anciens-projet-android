package com.titans.codenation.register;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;
    DatabaseHandler databaseHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateOrEdit.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);


            }
        });
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        final Cursor cursor = databaseHandler.getAllPersons();
        String [] columns = new String []{
                DatabaseHandler.PERSON_COLUMN_ID,
                DatabaseHandler.PERSON_COLUMN_NAME
        };
        int [] widgets = new int[] {
                R.id.personID,
                R.id.personName
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.person_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor itemCursor = (Cursor) MainActivity.this.listView.getItemAtPosition(position);
                int personID = itemCursor.getInt(itemCursor.getColumnIndex(DatabaseHandler.PERSON_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), CreateOrEdit.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, personID);
                startActivity(intent);
            }
        });



    }
}
