package com.example.omar.apvalley;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AfzonderlijkFragment extends Fragment implements AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private DatabaseReference mPostReference;
    private ListView lijst;
    private static final String TAG = "PostDetailActivity";
    SearchView mSearchView;
    DatabaseReference dref;
    ListView listview;
    Double waarde;
    ArrayList<String> arraytje;
    ArrayList<Boeken> list = new ArrayList<>(0);
    ArrayList<Boeken> lijstje = new ArrayList<>(0);
    ArrayAdapter<Boeken> adapter;
    Boeken boekje;
    CounterFab fab;
    View viewtje;
    private Spinner citySpinner;
    private Spinner citySpinner1;
    private Spinner citySpinner2;
    private MyAdapter mAdapter;

    public AfzonderlijkFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_afzonderlijk, container, false);
        arraytje = new ArrayList<String>();

            list.clear();
        citySpinner = (Spinner) v.findViewById(R.id.kies);
        citySpinner1 = (Spinner) v.findViewById(R.id.kies1);
        citySpinner2 = (Spinner) v.findViewById(R.id.kies2);
        citySpinner.setOnItemSelectedListener(this);
        citySpinner1.setOnItemSelectedListener(this);
        citySpinner2.setOnItemSelectedListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String titel = bundle.getString("titel", "");
            dref = FirebaseDatabase.getInstance().getReference().child("Boeken").child(titel).child("Boeken");
        }
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();                                                                                                                     //getIntent().getExtras().getString("titel")



        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                Boeken message = dataSnapshot.getValue(Boeken.class);

                list.add(message);

                // Toast.makeText(ListActivity.this, "lijstje"+lijstje.get(0).titel, Toast.LENGTH_SHORT).show();
                // ArrayList<Boeken> beerDrinkers = list.stream()
                //       .filter(p -> p.titel() == "klakele").collect(Collectors.toList());


                mRecyclerView.getAdapter().notifyDataSetChanged();


                //  }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Toast.makeText(getActivity(), "Veranderd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


                Iterator<Boeken> it = list.iterator();
                while (it.hasNext()) {
                    Boeken user = it.next();
                    if (user.titel.equals(dataSnapshot.getKey())) {
                        Toast.makeText(getActivity(),
                                it.toString(),
                                Toast.LENGTH_SHORT).show();
                        it.remove();
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                //      Boeken message = dataSnapshot.getValue(Boeken.class);

                //    list.add(message);

                // ArrayList<Boeken> beerDrinkers = list.stream()
                //       .filter(p -> p.titel() == "klakele").collect(Collectors.toList());


                //   mRecyclerView.getAdapter().notifyDataSetChanged();


                //   list.re(dataSnapshot.getValue(Boeken.class));

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(list, getActivity());

        mRecyclerView.setAdapter(mAdapter);


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }



    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
        private ArrayList<Boeken> mDataset;
        private ArrayList<Boeken> mFilteredList;
        private Context mCtx;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ImageView mImageView;
            public ImageView overflow;
            public  TextView ISBN;
            public  TextView auteur;
            public TextView titel;
            public Button toonVerkopers;
            public TextView aantal;
            public  TextView klas;


            public ViewHolder(View itemView) {
                super(itemView);
                klas=(TextView) itemView.findViewById(R.id.textView10);
                aantal=(TextView) itemView.findViewById(R.id.textView8);
                ISBN=(TextView) itemView.findViewById(R.id.textView6);
                auteur= (TextView) itemView.findViewById(R.id.textView5) ;
                mTextView = (TextView) itemView.findViewById(R.id.titel);
                titel = (TextView) itemView.findViewById(R.id.textView7);
                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                overflow = (ImageView) itemView.findViewById(R.id.overflow);
                toonVerkopers=(Button) itemView.findViewById(R.id.button6);
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
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_list, parent, false);
            // set the view's size, margins, paddings and layout parameters

            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            return vh;
        }

        //  mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boek);
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //       holder.mTextView.setText(mFilteredList.get(position).prijs.toString()+"€");

            if(mFilteredList.get(position).aantal!=null)
                holder.aantal.setText("Aantal boeken: "+mFilteredList.get(position).aantal);

            if(mFilteredList.get(position).ISBN==null)
                holder.ISBN.setText(mFilteredList.get(position).klas);
            else
                holder.ISBN.setText("ISBN: "+mFilteredList.get(position).ISBN);


            if(mFilteredList.get(position).auteur==null)
                holder.auteur.setText(mFilteredList.get(position).richting);
            else
                holder.auteur.setText("door "+mFilteredList.get(position).auteur);


            if(mFilteredList.get(position).titel==null)
                holder.titel.setText(mFilteredList.get(position).departement);
            else
                holder.titel.setText(mFilteredList.get(position).titel);



            holder.toonVerkopers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mFilteredList.get(position).aantal==null){
                        Fragment fragment = new LijstFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("titel", mFilteredList.get(position).titel);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    else{
                        Toast.makeText(mCtx, "gelukt", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new PakketInhoudFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", mFilteredList.get(position).userId);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                }
            });
            Picasso.with(mCtx).load(mFilteredList.get(position).foto).into(holder.mImageView);
        /*    holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intenje = new Intent(mCtx, BestelActivity.class);
                    intenje.putExtra("titel", mFilteredList.get(position).titel);
                    intenje.putExtra("auteur", mFilteredList.get(position).auteur);
                    intenje.putExtra("uitgeverij", mFilteredList.get(position).datum);
                    intenje.putExtra("isbn", mFilteredList.get(position).ISBN);
                    intenje.putExtra("foto", mFilteredList.get(position).foto);
                    intenje.putExtra("richting", mFilteredList.get(position).richting);
                    //    intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                    intenje.putExtra("datum", mFilteredList.get(position).uitgeverij);
                    intenje.putExtra("klas", mFilteredList.get(position).klas);
                    intenje.putExtra("departement", mFilteredList.get(position).departement);


                    startActivity(intenje);
                }
            });


            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popup = new PopupMenu(mCtx, holder.overflow);
                    popup.getMenuInflater().inflate(R.menu.menu_list, popup.getMenu());

                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_add_favourite:
                                    Toast.makeText(mCtx, "Toegevoegd aan winkelmandje", Toast.LENGTH_LONG).show();
                                    boekje = mFilteredList.get(position);
                                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Winkelmandje").child(mFilteredList.get(position).titel).setValue(boekje);
                                    // fab.increase();
                                    //   popup.getMenu().findItem(R.id.action_add_favourite).setVisible(false);
                                    return true;
                                case R.id.action_play_next:
                                    Intent intenje = new Intent(mCtx, BestelActivity.class);
                                    intenje.putExtra("titel", mFilteredList.get(position).titel);
                                    intenje.putExtra("auteur", mFilteredList.get(position).auteur);
                                    intenje.putExtra("uitgeverij", mFilteredList.get(position).datum);
                                    intenje.putExtra("isbn", mFilteredList.get(position).ISBN);
                                    intenje.putExtra("foto", mFilteredList.get(position).foto);
                                    intenje.putExtra("richting", mFilteredList.get(position).richting);
                                    intenje.putExtra("prijs", mFilteredList.get(position).prijs.toString());
                                    intenje.putExtra("datum", mFilteredList.get(position).uitgeverij);
                                    intenje.putExtra("klas", mFilteredList.get(position).klas);
                                    intenje.putExtra("departement", mFilteredList.get(position).departement);


                                    startActivity(intenje);

                                    return true;
                                default:
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
            */
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }

        public Boeken getItem(int position) {
            return mFilteredList.get(position);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());


            //   mFilteredList.clear();
            //   mFilteredList.addAll(mDataset);

            if (charText.length() == 0) {

                mFilteredList.addAll(mDataset);

            } else {
                for (Boeken boekenlijst : mDataset) {
                    if (boekenlijst.richting.toLowerCase().contains(charText)) {
                        mFilteredList.add(boekenlijst);
                    }
                }
            }
            notifyDataSetChanged();
        }


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

                            if (boekenlijst.titel.toLowerCase().contains(charString)) {

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (parent.getId() == R.id.kies && parent.getItemAtPosition(position).toString().equals("Campus")) {
            citySpinner1.setEnabled(false);
            citySpinner2.setEnabled(false);
        }

        if (parent.getId() == R.id.kies && !parent.getItemAtPosition(position).toString().equals("Campus")) {
            citySpinner1.setEnabled(true);

        }
        if (parent.getId() == R.id.kies1 && parent.getItemAtPosition(position).toString().equals("Richting")) {

            citySpinner2.setEnabled(false);
        }
        if (parent.getId() == R.id.kies1 && !parent.getItemAtPosition(position).toString().equals("Richting")) {
            citySpinner2.setEnabled(true);

        }


     Iterator<Boeken> it = list.iterator();
        while (it.hasNext()) {

            Boeken user = it.next();

            if (!user.departement.equals(parent.getItemAtPosition(position).toString()) && !parent.getItemAtPosition(position).toString().equals("Campus") && parent.getId() == R.id.kies) {
                Toast.makeText(getActivity(),
                        user.departement,
                        Toast.LENGTH_SHORT).show();

                //  lijstje.add(user);
                it.remove();

                mRecyclerView.getAdapter().notifyDataSetChanged();

            }
            if (!user.richting.equals(parent.getItemAtPosition(position).toString()) && !parent.getItemAtPosition(position).toString().equals("Richting") && parent.getId() == R.id.kies1) {
                Toast.makeText(getActivity(),
                        user.departement,
                        Toast.LENGTH_SHORT).show();

                //  lijstje.add(user);
                it.remove();

                mRecyclerView.getAdapter().notifyDataSetChanged();

            }
            if (!user.klas.equals(parent.getItemAtPosition(position).toString()) && !parent.getItemAtPosition(position).toString().equals("Klas") && parent.getId() == R.id.kies2) {
                Toast.makeText(getActivity(),
                        user.departement,
                        Toast.LENGTH_SHORT).show();

                //  lijstje.add(user);
                it.remove();

                mRecyclerView.getAdapter().notifyDataSetChanged();

            }

            // list.add(lijstje.get(0));

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

