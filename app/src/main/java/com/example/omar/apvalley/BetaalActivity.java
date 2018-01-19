package com.example.omar.apvalley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class BetaalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betaal);
        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(BetaalActivity.this);

        ((Button) findViewById(R.id.button4 )).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(BetaalActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                progressDialog.setIndeterminate(true);

                progressDialog.setMessage("Betaling afronden...");
                progressDialog.show();


                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                        .child("Winkelmandje").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        Boeken message = dataSnapshot.getValue(Boeken.class);
                        Toast.makeText(BetaalActivity.this, message.departement, Toast.LENGTH_SHORT).show();
                        Toast.makeText(BetaalActivity.this, message.richting, Toast.LENGTH_SHORT).show();
                        Toast.makeText(BetaalActivity.this, message.klas, Toast.LENGTH_SHORT).show();
                        Toast.makeText(BetaalActivity.this, message.titel, Toast.LENGTH_SHORT).show();
                        Toast.makeText(BetaalActivity.this, message.userId, Toast.LENGTH_SHORT).show();


                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Bestelhistoriek").child(message.titel).setValue(message);
                        FirebaseDatabase.getInstance().getReference().child(message.departement).child(message.richting).child(message.klas).child(message.titel).child("userId").child(message.userId).removeValue();


                        //  }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {



                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Winkelmandje").removeValue();
                                Intent intentje = new Intent(getApplicationContext(),HistoriekActivity.class);
                                startActivity(intentje);
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }
        });

}
}
