package sn.fallou_syll.covid_project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class presentation extends menu {

    private Button btn_test_covid;
    private Button btn_last_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        btn_test_covid =(Button) findViewById(R.id.btn_test_covid);
        btn_test_covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), personal_info.class);
                startActivity(i);
            }
        });
        btn_last_result = (Button) findViewById(R.id.btn_last_result);
        btn_last_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Dernier_resultat.class);
                startActivity(i);
            }
        });
    }
}