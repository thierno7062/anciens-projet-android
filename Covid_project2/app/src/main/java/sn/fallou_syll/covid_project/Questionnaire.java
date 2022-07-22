package sn.fallou_syll.covid_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import static sn.fallou_syll.covid_project.R.layout.activity_questionnaire;


public class Questionnaire extends menu {

    private Button btnSubmit;

    private RadioButton q1_yes,q1_no,q2_yes, q3_yes, q4_yes, q5_yes, q6_yes, q7_yes, q2_no, q3_no, q4_no, q5_no, q6_no, q7_no;
    protected int score = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_questionnaire);

        btnSubmit = (Button) findViewById(R.id.btn_resultat);

        q1_yes = (RadioButton) findViewById(R.id.rb_q1_yes);
        q1_no = (RadioButton) findViewById(R.id.rb_q1_no);
        q2_yes = (RadioButton) findViewById(R.id.rb_q2_yes);
        q2_no = (RadioButton) findViewById(R.id.rb_q2_no);
        q3_yes = (RadioButton) findViewById(R.id.rb_q3_yes);
        q3_no = (RadioButton) findViewById(R.id.rb_q3_no);
        q4_yes = (RadioButton) findViewById(R.id.rb_q4_yes);
        q4_no = (RadioButton) findViewById(R.id.rb_q4_no);
        q5_yes = (RadioButton) findViewById(R.id.rb_q5_yes);
        q5_no = (RadioButton) findViewById(R.id.rb_q5_no);
        q6_yes = (RadioButton) findViewById(R.id.rb_q6_yes);
        q6_no = (RadioButton) findViewById(R.id.rb_q6_no);
        q7_yes = (RadioButton) findViewById(R.id.rb_q7_yes);
        q7_no = (RadioButton) findViewById(R.id.rb_q7_no);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (q1_yes.isChecked()){
                    score = score + 1;
                }else if (q1_no.isChecked()){
                    score = score + 0;
                }

                if (q2_yes.isChecked()) {
                    score = score + 1;
                } else if(q2_no.isChecked()){
                    score = score + 0;
                }

                if (q3_yes.isChecked()) {
                    score = score + 1;
                } else if(q3_no.isChecked()){
                    score = score + 0;
                }

                if (q4_yes.isChecked()) {
                    score = score + 1;
                } else if(q4_no.isChecked()){
                    score = score + 0;
                }

                if (q5_yes.isChecked()) {
                    score = score + 1;
                } else if(q5_no.isChecked()){
                    score = score + 0;
                }

                if (q6_yes.isChecked()) {
                    score = score + 1;
                } else if(q6_no.isChecked()){
                    score = score + 0;
                }

                if (q7_yes.isChecked()) {
                    score = score + 1;
                } else if(q7_no.isChecked()){
                    score = score + 0;
                }
                Intent i = new Intent(getApplicationContext(), Resultat.class);
                i.putExtra("scor",score);

                startActivity(i);


            }
        });

    }
}
