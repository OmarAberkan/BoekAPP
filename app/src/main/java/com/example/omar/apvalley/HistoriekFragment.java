package com.example.omar.apvalley;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class HistoriekFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference dref;
    ArrayList<String> arraytje;
    ArrayList<Boeken> list = new ArrayList<>(0);
    ArrayAdapter<Boeken> adapter;
    private FirebaseAuth mAuth;
    private MyAdapter mAdapter;
    public HistoriekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_historiek, container, false);

        arraytje= new ArrayList<String>();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .child("Bestelhistoriek");

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                Boeken message = dataSnapshot.getValue(Boeken.class);

                list.add(message);
                // The SharedPreferences editor - must use commit() to submit changes





                // ArrayList<Boeken> beerDrinkers = list.stream()
                //       .filter(p -> p.titel() == "klakele").collect(Collectors.toList());



                mRecyclerView.getAdapter().notifyDataSetChanged();


                //  }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Iterator<Boeken> it = list.iterator();
                while (it.hasNext()) {
                    Boeken user = it.next();
                    if (user.titel.equals(dataSnapshot.getKey())) {

                        it.remove();
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }


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
            public TextView auteur;
            public TextView titel;
            public TextView ISBN;
            public ImageView mImageView;
            public ImageView overflow;
            public TextView prijs;



            public ViewHolder(View itemView) {
                super(itemView);
                titel = (TextView) itemView.findViewById(R.id.textView7);
                auteur = (TextView) itemView.findViewById(R.id.textView5);
                ISBN =(TextView) itemView.findViewById(R.id.textView6);
                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                overflow = (ImageView) itemView.findViewById(R.id.overflow);
                prijs=(TextView) itemView.findViewById(R.id.textView9);

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
                    .inflate(R.layout.historiek_list, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.titel.setText(mFilteredList.get(position).titel);
            holder.auteur.setText("door "+mFilteredList.get(position).auteur);
            holder.ISBN.setText("ISBN: "+mFilteredList.get(0).ISBN);
            holder.prijs.setText(mFilteredList.get(position).prijs.toString()+"€");
            Picasso.with(mCtx).load(mFilteredList.get(position).foto).into(holder.mImageView);

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

}
