package com.example.omar.apvalley;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.omar.apvalley.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BestelActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICKER=2000;
    ArrayList<Bitmap> bitmaplijst = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestel);



        Picasso.with(getApplicationContext()).load(getIntent().getExtras().getString("foto")).into(  ((ImageView) findViewById(R.id.foto)));

        ((ImageView) findViewById(R.id.foto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(BestelActivity.this)
                        .start(REQUEST_CODE_PICKER);
            }
        });



        ((TextView) findViewById(R.id.titel)).setText(getIntent().getExtras().getString("titel"));
        ((TextView) findViewById(R.id.klas)).setText("Klas: "+getIntent().getExtras().getString("klas"));
        ((TextView) findViewById(R.id.richting)).setText("Richting: "+getIntent().getExtras().getString("richting"));
        ((TextView) findViewById(R.id.isbn)).setText("ISBN: "+getIntent().getExtras().getString("isbn"));
        ((TextView) findViewById(R.id.datum)).setText("Datum: "+getIntent().getExtras().getString("datum"));
        ((TextView) findViewById(R.id.departement)).setText("Departement: "+getIntent().getExtras().getString("departement"));
        ((TextView) findViewById(R.id.auteur)).setText("Auteur: "+getIntent().getExtras().getString("uitgeverij"));
       // ((TextView) findViewById(R.id.richting)).setText("Uitgeverij: "+getIntent().getExtras().getString("uitgeverij"));


        /*

        intenje.putExtra("titel",mFilteredList.get(position).titel);
                                    intenje.putExtra("auteur",mFilteredList.get(position).auteur);
                                    intenje.putExtra("datum",mFilteredList.get(position).datum);
                                    intenje.putExtra("isbn",mFilteredList.get(position).ISBN);
                                    intenje.putExtra("foto",mFilteredList.get(position).foto);
                                    intenje.putExtra("richting",mFilteredList.get(position).richting);
                                    intenje.putExtra("prijs",mFilteredList.get(position).prijs);
         */

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            Toast.makeText(this, images.get(0).getPath(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getIntent().getExtras().getString("departement"), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getIntent().getExtras().getString("richting"), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getIntent().getExtras().getString("klas"), Toast.LENGTH_SHORT).show();
            for(int i=0;i<images.size();i++) {


                bitmaplijst.add(BitmapFactory.decodeFile(images.get(i).getPath()));
                for(int j=0;j<bitmaplijst.size();j++){


                    FirebaseDatabase.getInstance().getReference().child("User")
                            .child(FirebaseAuth.getInstance().getUid()).child("Verkoop").child(getIntent().getExtras().getString("titel")).child("picture"+i).setValue(bitmapToBase64(bitmaplijst.get(j)));
                    ((ImageView) findViewById(R.id.foto)).setImageBitmap(base64ToBitmap(bitmapToBase64(bitmaplijst.get(0))));
                }

            }



        }

}

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
