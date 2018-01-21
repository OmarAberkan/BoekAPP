package com.example.omar.apvalley;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AfrekenenFragment extends Fragment {


    public AfrekenenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_afrekenen, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String titel = bundle.getString("prijs", "");
            ((TextView) v.findViewById(R.id.textView4)).setText(titel);
        }
        CardForm cardForm = (CardForm) v.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Afrekenen")
                .setup(getActivity());

        ((Button) v.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
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



                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Bestelhistoriek").child(message.titel).setValue(message);
                        FirebaseDatabase.getInstance().getReference().child("Boeken").child("Afzonderlijk").child("Boeken").child(message.titel).child("userID").child(message.userId).removeValue();


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
                                Intent intentje = new Intent(getActivity(),HistoriekActivity.class);
                                startActivity(intentje);
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }
        });

        return v;
    }

}
