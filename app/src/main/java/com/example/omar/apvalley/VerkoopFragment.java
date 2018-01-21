package com.example.omar.apvalley;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class VerkoopFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    final ArrayList<String> boekenpakket = new ArrayList<String>();
    final ArrayList<Boeken> boekenlijst = new ArrayList<Boeken>();
    ArrayList<Image> images = new ArrayList<>();
    DatabaseReference dref;
     public View viewtje;
    public RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    private static final int REQUEST_CODE_PICKER=2000;
     Button b;
    String json = "{\n" +
            " \"kind\": \"books#volumes\",\n" +
            " \"totalItems\": 0\n" +
            "}\n";
    public VerkoopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Verkoop").removeValue();
       final   View v = inflater.inflate(R.layout.fragment_verkoop, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Toast.makeText(getApplicationContext(),mAuth.getUid(), Toast.LENGTH_LONG).show();

viewtje=v;
        b=(Button) v.findViewById(R.id.verkopen);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView1);








        ((Spinner) v.findViewById(R.id.scholen)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Kies uw departement")){
                    ((Button) v.findViewById(R.id.button5)).setEnabled(true);
                }

                else{
                    //     Toast.makeText(SellPacketActivity.this, "Selecteer eerst uw departement", Toast.LENGTH_SHORT).show();
                    ((Button) v.findViewById(R.id.button5)).setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ((Spinner) v.findViewById(R.id.richtingen)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Kies uw richting")){
                    ((Button) v.findViewById(R.id.button5)).setEnabled(true);
                }

                else{
                    //  Toast.makeText(SellPacketActivity.this, "Selecteer eerst uw richting", Toast.LENGTH_SHORT).show();
                    ((Button) v.findViewById(R.id.button5)).setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ((Spinner) v.findViewById(R.id.klas)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getItemAtPosition(i).toString().equals("Kies uw klas")){
                    ((Button) v.findViewById(R.id.button5)).setEnabled(true);
                }
                else
                    ((Button) v.findViewById(R.id.button5)).setEnabled(false);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dref = FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getUid()).child("Verkoop");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                Toast.makeText(getActivity(), "Boek toegevoegd", Toast.LENGTH_SHORT).show();
                Boeken message = new Boeken();
                message.ISBN=dataSnapshot.child("ISBN").getValue().toString();
  //            message.datum=dataSnapshot.child("datum").getValue().toString();
                message.foto=dataSnapshot.child("foto").getValue().toString();
                message.titel=dataSnapshot.child("titel").getValue().toString();
//                message.uitgeverij=dataSnapshot.child("uitgeverij").getValue().toString();
                message.departement=dataSnapshot.child("departement").getValue().toString();
                message.klas=dataSnapshot.child("klas").getValue().toString();
                message.richting=dataSnapshot.child("richting").getValue().toString();
                //   message.auteur=dataSnapshot.child("auteur").getValue().toString();
                message.auteur=dataSnapshot.child("auteur").getValue().toString();
                //  message.prijs= Double.parseDouble( dataSnapshot.child("prijs").getValue().toString());

                boekenlijst.add(message);

                mRecyclerView.getAdapter().notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

              Iterator<Boeken> it = boekenlijst.iterator();
                while (it.hasNext()) {
                    Boeken user = it.next();
                    if (user.titel.equals(dataSnapshot.getKey())) {
                    //    Toast.makeText(getActivity(),
                      //          it.toString(),
                        //        Toast.LENGTH_SHORT).show();
                        it.remove();
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }




            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());





        ((Button) v.findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           //     FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
             //   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              //  KoopFragment koopFragment = new KoopFragment();
               // fragmentTransaction.replace(R.id.frame,koopFragment);
               // fragmentTransaction.commit();

               IntentIntegrator.forSupportFragment(VerkoopFragment.this).initiateScan();


            }
        });

        mAdapter = new MyAdapter(boekenlijst,getActivity());
        mRecyclerView.setAdapter(mAdapter);





        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
     //   Toast.makeText(getActivity(), Integer.toString(resultCode), Toast.LENGTH_LONG).show();
        if(result !=null ){

            if(result.getContents()==null) {
                Toast.makeText(getActivity(), "Je annuleerde de scan", Toast.LENGTH_LONG).show();
                //  Intent gaTerug = new Intent(SellPacketActivity.this,RegisterActivity.class);
                //startActivity(gaTerug);
            }
            else{
                String waarde = null;

                try {
                    waarde = new GetLevDataTask(result.getContents()).execute().get();
              //      boekenpakket.add(result.getContents());
            //        Toast.makeText(getActivity(), Integer.toString( boekenpakket.size()), Toast.LENGTH_SHORT).show();
                    if(waarde.equals(json)){
                        Toast.makeText(getActivity(), "Boek niet gevonden", Toast.LENGTH_LONG).show();
                        //     Intent gaTerug = new Intent(SellPacketActivity.this,RegisterActivity.class);
                        //     startActivity(gaTerug);

                    }
                    else{
                        final Book book=  new Gson().fromJson(waarde,Book.class);
                        //  boekenlijst.add(book);

                       final Boeken test = new Boeken(book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(),book.getItems().get(0).getVolumeInfo().getAuthors().get(0),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),((Spinner) viewtje.findViewById(R.id.scholen)).getSelectedItem().toString(),((Spinner) viewtje.findViewById(R.id.richtingen)).getSelectedItem().toString(),((Spinner) viewtje.findViewById(R.id.klas)).getSelectedItem().toString());


                        Toast.makeText(getActivity(), Integer.toString(boekenlijst.size()), Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Verkoop").child(test.titel.replace('.','_')).setValue(test);
                     //   final   Boeken boekje = new Boeken(book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(), ((Spinner) viewtje.findViewById(R.id.richtingen)).getSelectedItem().toString(), ((Spinner) viewtje.findViewById(R.id.klas)).getSelectedItem().toString(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),((Spinner) viewtje.findViewById(R.id.scholen)).getSelectedItem().toString(), FirebaseAuth.getInstance().getUid(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName());;
                        //    mDatabase.child(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(book.getItems().get(0).getVolumeInfo().getTitle().replace('.','_')).setValue(boekje);
                        //     mDatabase.child(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(book.getItems().get(0).getVolumeInfo().getTitle().replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boekje);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                                String email = ((EditText) viewtje.findViewById(R.id.prijs)).getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    ((EditText) viewtje.findViewById(R.id.prijs)).setError("Vereist.");

                                } else {
                                    ((EditText) viewtje.findViewById(R.id.prijs)).setError(null);


                                    if(boekenlijst.size()==0){

                                        FirebaseDatabase.getInstance().getReference().child("Boeken").child("Afzonderlijk").child("Boeken").child(test.titel.replace('.','_')).setValue(test);
                                        test.prijs=Double.parseDouble(((EditText) viewtje.findViewById(R.id.prijs)).getText().toString().replace(",","."));
                                        test.username=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                        test.userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        FirebaseDatabase.getInstance().getReference().child("Boeken").child("Afzonderlijk").child("Boeken").child(test.titel.replace('.','_')).child("userID").child(FirebaseAuth.getInstance().getUid()).setValue(test);
                                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Verkoop").removeValue();
                                    }
                                    if(boekenlijst.size()>0){
                                        Integer aantal= boekenlijst.size();
                                        Boeken boekje =new Boeken("https://pbs.twimg.com/profile_images/378800000542961862/5c7765d0b82c5ab3831554e4dcaee38b_400x400.png",book.getItems().get(0).getVolumeInfo().getTitle(),book.getItems().get(0).getVolumeInfo().getAuthors().get(0),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),((Spinner) viewtje.findViewById(R.id.scholen)).getSelectedItem().toString(),((Spinner) viewtje.findViewById(R.id.richtingen)).getSelectedItem().toString(),((Spinner) viewtje.findViewById(R.id.klas)).getSelectedItem().toString(),aantal);
                                        boekje.prijs=Double.parseDouble(((EditText) viewtje.findViewById(R.id.prijs)).getText().toString().replace(",","."));
                                        boekje.ISBN=null;
                                        boekje.titel=null;
                                        boekje.auteur=null;
                                        boekje.userId=FirebaseAuth.getInstance().getUid();
                                        FirebaseDatabase.getInstance().getReference().child("Boeken").child("Pakket").child("Boeken").child(FirebaseAuth.getInstance().getUid()).setValue(boekje);
                                        for(int i=0;i<boekenlijst.size();i++)
                                        FirebaseDatabase.getInstance().getReference().child("Boeken").child("Pakket").child("Boeken").child(FirebaseAuth.getInstance().getUid()).child("Boeken").child(boekenlijst.get(i).titel).setValue(boekenlijst.get(i));
                                      //  test.prijs=Double.parseDouble(((EditText) viewtje.findViewById(R.id.prijs)).getText().toString().replace(",","."));
                                        //test.username=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                       // test.userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                     //   FirebaseDatabase.getInstance().getReference().child("Boeken").child("Afzonderlijk").child("Boeken").child(test.titel.replace('.','_')).child("userID").child(FirebaseAuth.getInstance().getUid()).setValue(test);
                                     //   FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Verkoop").removeValue();
                                    }


                                    Fragment fragment = new MijnBoekenFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    //  writeNewUser(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(),((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(),mAuth.getUid(),book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),book.getItems().get(0).getVolumeInfo().getAuthors().get(0),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")));
                                    // Intent gaNaarBoekenLijst = new Intent(SellActivity.this,RegisterActivity.class);
                                    //  Boeken boek = new Boeken( book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail(),book.getItems().get(0).getVolumeInfo().getTitle(), Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",",".")), ((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString(), ((Spinner) findViewById(R.id.klas)).getSelectedItem().toString(),book.getItems().get(0).getVolumeInfo().getPublisher(),book.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier(),book.getItems().get(0).getVolumeInfo().getPublishedDate(),((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString(), mAuth.getUid());

                                    //        if(boekenlijst.size()==1){
                                    //          mDatabase.child(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(book.getItems().get(0).getVolumeInfo().getTitle().replace('.','_')).setValue(boekje);
                                    //        boekje.prijs=Double.parseDouble(((EditText) findViewById(R.id.prijs)).getText().toString().replace(",","."));
                                    //      mDatabase.child(((Spinner) findViewById(R.id.scholen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.richtingen)).getSelectedItem().toString()).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(book.getItems().get(0).getVolumeInfo().getTitle().replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boekje);
                                    // }
                                    // if (boekenlijst.size()>1){

                                }



                                // }

                            }

                        });
//                        Picasso.with(SellPacketActivity.this).load(book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail()).into(((ImageView) findViewById(R.id.Cover)));


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


  /*  private void writeNewUser(String campus,String richting,String userId, String foto,String titel, String auteur,String uitgeverij,String datum, String ISBN) {
        //   Boeken boek = new Boeken(foto,titel,auteur,uitgeverij,datum,ISBN,prijs,userId);

        Boeken boek = new Boeken( foto,titel, richting, ((Spinner) findViewById(R.id.klas)).getSelectedItem().toString(),uitgeverij,ISBN,datum,campus, userId);



        mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).setValue(boek);
        mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boek);
    }
    */
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
        private ArrayList<Boeken> mDataset;
        private ArrayList<Boeken> mFilteredList;
        private Context mCtx;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            public ImageView mImageView;
            //  public ImageView overflow;
            public  ImageView kruisje;
            public ViewHolder(View itemView) {
                super(itemView);

                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                kruisje= (ImageView) itemView.findViewById(R.id.kruis);
                itemView.setTag(itemView);

            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<Boeken> myDataset, Context mCtx) {
            mDataset = myDataset;
            mFilteredList = myDataset;
            this.mCtx = mCtx;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_verkoop, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            holder.kruisje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                            .child("Verkoop").child(mFilteredList.get(position).titel.replace('.','_')).removeValue();
                    Toast.makeText(mCtx, "Boek verwijderd", Toast.LENGTH_SHORT).show();
                }
            });


            Picasso.with(getActivity()).load(mFilteredList.get(position).foto).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Toast.makeText(mCtx ,mFilteredList.get(position).richting, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mCtx ,mFilteredList.get(position).departement, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mCtx ,mFilteredList.get(position).klas, Toast.LENGTH_SHORT).show();

                    Intent intenje = new Intent(mCtx,BestelActivity.class);
                    intenje.putExtra("titel",mFilteredList.get(position).titel);
                    intenje.putExtra("auteur",mFilteredList.get(position).auteur);
                    intenje.putExtra("uitgeverij",mFilteredList.get(position).uitgeverij);
                    intenje.putExtra("isbn",mFilteredList.get(position).ISBN);
                    intenje.putExtra("foto",mFilteredList.get(position).foto);
                    intenje.putExtra("richting",mFilteredList.get(position).richting);
                    //   intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                    intenje.putExtra("datum",mFilteredList.get(position).datum);
                    intenje.putExtra("klas",mFilteredList.get(position).klas);
                    intenje.putExtra("departement",mFilteredList.get(position).departement);


                    startActivity(intenje);



                }
            });

        }




        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }
        public void filter(String charText) {charText = charText.toLowerCase(Locale.getDefault());


            //   mFilteredList.clear();
            //   mFilteredList.addAll(mDataset);

            if (charText.length() == 0) {

                mFilteredList.addAll(mDataset);

            } else {
                for (Boeken boekenlijst : mDataset) {
                    if ( boekenlijst.richting.toLowerCase().contains(charText)) {
                        mFilteredList.add(boekenlijst);
                    }
                }
            }
            notifyDataSetChanged();
        }


        /* @Override
         public void onAttachedToRecyclerView(RecyclerView recyclerView) {
             super.onAttachedToRecyclerView(recyclerView);
         }
         *
         *
         * */
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString();

                    if (charString.isEmpty()) {

                        mFilteredList = mDataset;
                    } else {

                        ArrayList<Boeken> filteredList = new ArrayList<>();

                        for (Boeken boekenlijst : mDataset) {

                            if (boekenlijst.titel.toLowerCase().contains(charString) || boekenlijst.ISBN.toLowerCase().contains(charString)   ) {

                                filteredList.add(boekenlijst);
                            }
                        }

                        mFilteredList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<Boeken>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public void Winkelmandje(Boeken boek,String id) {



        mDatabase.child("User").child(id).child("Winkelmandje").setValue(boek);
    }



}
