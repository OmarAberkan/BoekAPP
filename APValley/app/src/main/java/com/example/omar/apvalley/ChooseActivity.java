package com.example.omar.apvalley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ((Button) findViewById(R.id.zoekButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent gaNaarLijst = new Intent(getApplicationContext(),RegisterActivity.class);
              gaNaarLijst.putExtra("campus",((Spinner) findViewById(R.id.campus)).getSelectedItem().toString());
                gaNaarLijst.putExtra("richting",((Spinner) findViewById(R.id.richting)).getSelectedItem().toString());
                gaNaarLijst.putExtra("klas",((Spinner) findViewById(R.id.klas)).getSelectedItem().toString());
                startActivity(gaNaarLijst);
            }
        });
    }

}
