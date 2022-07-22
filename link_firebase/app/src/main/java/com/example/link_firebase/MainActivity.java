package com.example.link_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText etName , etRollno;
    Spinner spinnerCourses;
    Button btnInsertData;

    DatabaseReference studentDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName=findViewById(R.id.etname);
        etRollno=findViewById(R.id.etRollno);
        spinnerCourses=findViewById(R.id.spinnercourse);
        btnInsertData=findViewById(R.id.btninsertdata);

        studentDbRef = FirebaseDatabase.getInstance().getReference().child("Students");

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStudentData();
            }
        });
    }

    private void insertStudentData() {

        String name = etName.getText().toString();
        String rollno = etRollno.getText().toString();
        String course = spinnerCourses.getSelectedItem().toString();

        Students students = new Students(name,rollno,course);

        studentDbRef.push().setValue(students);
        Toast.makeText(MainActivity.this,"Data inserted !!",Toast.LENGTH_LONG).show();
    }
}