package com.example.omar.apvalley;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.omar.apvalley.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SellActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
   final ArrayList<String> boekenpakket = new ArrayList<String>();
   final ArrayList<Boeken> boekenlijst = new ArrayList<Boeken>();
    private static final int REQUEST_CODE_PICKER=2000;
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
        ((ImageButton) findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(SellActivity.this) // Activity or Fragment
                        .start(REQUEST_CODE_PICKER);

            }
        });

        ((Button) findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(SellActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            Toast.makeText(this, images.get(0).getPath(), Toast.LENGTH_SHORT).show();



        }



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
                    boekenpakket.add(result.getContents());
                    Toast.makeText(this, Integer.toString( boekenpakket.size()), Toast.LENGTH_SHORT).show();
                    if(waarde.equals(json)){
                        Toast.makeText(this, "Boek niet gevonden", Toast.LENGTH_LONG).show();
                        Intent gaTerug = new Intent(SellActivity.this,RegisterActivity.class);
                        startActivity(gaTerug);

                    }
                    else{
                        final Book book=  new Gson().fromJson(waarde,Book.class);
                      //  boekenlijst.add(book);

                        Picasso.with(SellActivity.this).load(book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail()).into(((ImageView) findViewById(R.id.Cover)));
                        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String email = ((EditText) findViewById(R.id.prijs)).getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    ((EditText) findViewById(R.id.prijs)).setError("Vereist.");

                                } else {
                                    ((EditText) findViewById(R.id.prijs)).setError(null);

                                  //  writeNewUser(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(),((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(),mAuth.getUid(),book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),book.getItems().get(0).getVolumeInfo().getAuthors().get(0),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")));
                                   // Intent gaNaarBoekenLijst = new Intent(SellActivity.this,RegisterActivity.class);
                                  //  Boeken boek = new Boeken( book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(), Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")), ((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(), ((Spinner) findViewById(R.id.klas)).getSelectedItem().toString(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(), mAuth.getUid());
                                                                                                   Boeken boekje = new Boeken(  book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(), Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")), ((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(), ((Spinner) findViewById(R.id.klas)).getSelectedItem().toString(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(), mAuth.getUid());
                                    boekenlijst.add(boekje);
                                    Toast.makeText(SellActivity.this,Integer.toString( boekenlijst.size()), Toast.LENGTH_SHORT).show();
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
     //   Boeken boek = new Boeken(foto,titel,auteur,uitgeverij,datum,ISBN,prijs,userId);

        Boeken boek = new Boeken( foto,titel, prijs, richting, ((Spinner) findViewById(R.id.klas)).getSelectedItem().toString(),uitgeverij,ISBN,datum,campus, userId);



        mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).setValue(boek);
        mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boek);
    }
}
