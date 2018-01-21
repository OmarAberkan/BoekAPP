package com.example.omar.apvalley;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class LijstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    ArrayList<Boeken> list = new ArrayList<>(0);
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference dref;
    private MyAdapter mAdapter;
    public LijstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_lijst, container, false);



        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String titel = bundle.getString("titel", "");
            dref = FirebaseDatabase.getInstance().getReference().child("Boeken").child("Afzonderlijk").child("Boeken").child(titel).child("userID");
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);


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
                    if (user.richting.equals(dataSnapshot.getKey())) {
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
        mAdapter = new MyAdapter(list,getActivity());

        mRecyclerView.setAdapter(mAdapter);


        return v;
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
        private ArrayList<Boeken> mDataset;
        private ArrayList<Boeken> mFilteredList;
        private Context mCtx;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView username;
            public TextView prijs;
            public Button koop;


            public ViewHolder(View itemView) {
                super(itemView);
                username = (TextView) itemView.findViewById(R.id.username);
                prijs = (TextView) itemView.findViewById(R.id.prijs);
                koop = (Button) itemView.findViewById(R.id.koop);
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
                    .inflate(R.layout.list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        //  mDatabase.child(campus).child(richting).child(((Spinner) findViewById(R.id.klas)).getSelectedItem().toString()).child(titel.replace('.','_')).child("userId").child(FirebaseAuth.getInstance().getUid()).setValue(boek);
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //       holder.mTextView.setText(mFilteredList.get(position).prijs.toString()+"€");
            holder.username.setText(mFilteredList.get(position).username);
            holder.prijs.setText(mFilteredList.get(position).prijs.toString()+"€");

            holder.koop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("Winkelmandje").child(mFilteredList.get(position).titel).setValue(mFilteredList.get(position));
                    Toast.makeText(mCtx, "Toegevoegd aan winkelmandje", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new AfzonderlijkFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }
        public Boeken getItem(int position) {
            return mFilteredList.get(position);
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

                            if (boekenlijst.richting.toLowerCase().contains(charString)   ) {

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

}
