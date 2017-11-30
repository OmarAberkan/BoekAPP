package com.example.omar.apvalley;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.apvalley.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class SellActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String json = "{\n" +
            " \"kind\": \"books#volumes\",\n" +
            " \"totalItems\": 0\n" +
            "}\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();



                IntentIntegrator integrator = new IntentIntegrator(SellActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result !=null ){
            if(result.getContents()==null) {
                Toast.makeText(this, "Je annuleerde de scan", Toast.LENGTH_LONG).show();
                Intent gaTerug = new Intent(SellActivity.this,RegisterActivity.class);
                startActivity(gaTerug);
            }
            else{
                String waarde = null;

                try {
                    waarde = new GetLevDataTask(result.getContents()).execute().get();

                    if(waarde.equals(json)){
                        Toast.makeText(this, "Boek niet gevonden", Toast.LENGTH_LONG).show();
                        Intent gaTerug = new Intent(SellActivity.this,RegisterActivity.class);
                        startActivity(gaTerug);

                    }
                    else{
                        final Book book=  new Gson().fromJson(waarde,Book.class);

                       // ((TextView) findViewById(R.id.Title)).setText("Titel:  "+book.getItems().get(0).getVolumeInfo().getTitle());
                       // ((TextView) findViewById(R.id.Uitgeverij)).setText("Uitgeverij:  "+book.getItems().get(0).getVolumeInfo().getPublisher());
                       // ((TextView) findViewById(R.id.Datum)).setText("Datum:  "+book.getItems().get(0).getVolumeInfo().getPublishedDate());
                       // ((TextView) findViewById(R.id.Auteur)).setText("Auteur:  "+book.getItems().get(0).getVolumeInfo().getAuthors().get(0));
                       // ((TextView) findViewById(R.id.txtContent)).setText("ISBN:  "+book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier());
                        Picasso.with(SellActivity.this).load(book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail()).into(((ImageView) findViewById(R.id.Cover)));
                        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String email = ((EditText) findViewById(R.id.prijs)).getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    ((EditText) findViewById(R.id.prijs)).setError("Vereist.");

                                } else {
                                    ((EditText) findViewById(R.id.prijs)).setError(null);
                                    writeNewUser(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(),((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(),mAuth.getUid(),book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),book.getItems().get(0).getVolumeInfo().getAuthors().get(0),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")));
                                   // Intent gaNaarBoekenLijst = new Intent(SellActivity.this,RegisterActivity.class);
                                    finish();
                                }

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }

        }
        else{

            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void writeNewUser(String campus,String richting,String userId, String foto,String titel, String auteur,String uitgeverij,String datum, String ISBN,Double prijs) {
        Boeken boek = new Boeken(foto,titel,auteur,uitgeverij,datum,ISBN,prijs,userId);


        mDatabase.child(campus).child(richting).child(titel.replace('.','_')).setValue(boek);
    }
}
