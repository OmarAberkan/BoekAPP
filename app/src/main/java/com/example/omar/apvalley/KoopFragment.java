package com.example.omar.apvalley;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class KoopFragment extends Fragment {
    public RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    private DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private DatabaseReference mPostReference;
    private ListView lijst;
    private static final String TAG = "PostDetailActivity";
    SearchView mSearchView;
    DatabaseReference dref;
    ListView listview;
    ArrayList<Boeken> list = new ArrayList<>(0);
    ArrayAdapter<Boeken> adapter;
    Boeken boekje;
    private  MyAdapter mAdapter;
    public KoopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_koop, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);



        Toast.makeText(getActivity(),   FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

        dref = FirebaseDatabase.getInstance().getReference().child("Boeken");

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {


                Boeken message = new Boeken();
//                message.ISBN=dataSnapshot.child("ISBN").getValue().toString();
                //              message.datum=dataSnapshot.child("datum").getValue().toString();

                message.foto=dataSnapshot.child("foto").getValue().toString();
                message.titel=dataSnapshot.child("titel").getValue().toString();
                //   message.uitgeverij=dataSnapshot.child("uitgeverij").getValue().toString();
                //   message.auteur=dataSnapshot.child("auteur").getValue().toString();
                //       message.auteur="auteur";
//     message.prijs= Double.parseDouble( dataSnapshot.child("prijs").getValue().toString());

                list.add(message);
                mRecyclerView.getAdapter().notifyDataSetChanged();



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
                        Toast.makeText(getActivity(),
                                it.toString(),
                                Toast.LENGTH_SHORT).show();
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


       RecyclerView.LayoutManager  mLayoutManager = new  GridLayoutManager(getActivity(),2 );
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new MyAdapter(list,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
           // public TextView mPrijs;
            public TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);

                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                mTextView = (TextView) itemView.findViewById(R.id.titel);
              //  mPrijs= (TextView) itemView.findViewById(R.id.price);
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
                    .inflate(R.layout.list_item_main, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mFilteredList.get(position).titel);

         //  if(mFilteredList.get(position).prijs !=null)
           //    holder.mPrijs.setText(mFilteredList.get(position).prijs.toString());

            Picasso.with(getActivity()).load(mFilteredList.get(position).foto).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment fragment = new AfzonderlijkFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("titel",mFilteredList.get(position).titel);
                    bundle.putString("auteur",mFilteredList.get(position).auteur);
                    bundle.putString("uitgeverij",mFilteredList.get(position).datum);
                    bundle.putString("isbn",mFilteredList.get(position).ISBN);
                    bundle.putString("foto",mFilteredList.get(position).foto);
                    bundle.putString("richting",mFilteredList.get(position).richting);
                    // intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                    bundle.putString("datum",mFilteredList.get(position).uitgeverij);
                    bundle.putString("klas",mFilteredList.get(position).klas);
                    bundle.putString("departement",mFilteredList.get(position).departement);
                    fragment.setArguments(bundle);
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
