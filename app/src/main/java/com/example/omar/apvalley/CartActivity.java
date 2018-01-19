package com.example.omar.apvalley;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.util.stream.IntStream;

public class CartActivity extends FragmentActivity {
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
    ArrayAdapter<Boeken> adapter;

    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
            arraytje= new ArrayList<String>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .child("Winkelmandje");
        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenje = new Intent(getApplicationContext(),BetaalActivity.class);
                startActivity(intenje);
            }
        });

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                Boeken message = dataSnapshot.getValue(Boeken.class);

                list.add(message);
                Toast.makeText(CartActivity.this, Integer.toString( list.size()), Toast.LENGTH_SHORT).show();



                double sum = 0;
                for(int i=0;i<list.size();i++){



                        sum +=  list.get(i).prijs;






                }

                ((TextView) findViewById(R.id.textView3)).setText("Totale bedrag:"+ Double.toString(sum));

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
                        Toast.makeText(getApplicationContext(),
                                it.toString(),
                                Toast.LENGTH_SHORT).show();
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
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(list,getApplicationContext());

        mRecyclerView.setAdapter(mAdapter);



    }
    public class MyAdapter extends RecyclerView.Adapter<CartActivity.MyAdapter.ViewHolder> implements Filterable {
        private ArrayList<Boeken> mDataset;
        private ArrayList<Boeken> mFilteredList;
        private Context mCtx;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public TextView titel;
            public ImageView mImageView;
            public ImageView overflow;


            public ViewHolder(View itemView) {
                super(itemView);
                titel = (TextView) itemView.findViewById(R.id.textView7);
                mTextView = (TextView) itemView.findViewById(R.id.titel);
                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                overflow = (ImageView) itemView.findViewById(R.id.overflow);
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

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mFilteredList.get(position).prijs.toString()+"€");
            holder.titel.setText(mFilteredList.get(position).titel);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         Intent intenje = new Intent(mCtx,BestelActivity.class);
                                                         intenje.putExtra("titel",mFilteredList.get(position).titel);
                                                         intenje.putExtra("auteur",mFilteredList.get(position).auteur);
                                                         intenje.putExtra("uitgeverij",mFilteredList.get(position).datum);
                                                         intenje.putExtra("isbn",mFilteredList.get(position).ISBN);
                                                         intenje.putExtra("foto",mFilteredList.get(position).foto);
                                                         intenje.putExtra("richting",mFilteredList.get(position).richting);
                                                         intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                                                         intenje.putExtra("datum",mFilteredList.get(position).uitgeverij);
                                                         intenje.putExtra("klas",mFilteredList.get(position).klas);
                                                         intenje.putExtra("departement",mFilteredList.get(position).departement);


                                                         startActivity(intenje);
                                                     }
            });

            Picasso.with(getApplicationContext()).load(mFilteredList.get(position).foto).into(holder.mImageView);
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popup = new PopupMenu(mCtx, holder.overflow);
                    popup.getMenuInflater().inflate(R.menu.menu_cart, popup.getMenu());

                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_add_favourite:
                                    Toast.makeText(mCtx, "Verwijderd uit winkelmandje", Toast.LENGTH_LONG).show();
                                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                                        .child("Winkelmandje").child(mFilteredList.get(position).titel).removeValue();

                                    return true;
                                case R.id.action_play_next:
                                    Intent intenje = new Intent(mCtx,BestelActivity.class);
                                    intenje.putExtra("titel",mFilteredList.get(position).titel);
                                    intenje.putExtra("auteur",mFilteredList.get(position).auteur);
                                    intenje.putExtra("uitgeverij",mFilteredList.get(position).datum);
                                    intenje.putExtra("isbn",mFilteredList.get(position).ISBN);
                                    intenje.putExtra("foto",mFilteredList.get(position).foto);
                                    intenje.putExtra("richting",mFilteredList.get(position).richting);
                                    intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                                    intenje.putExtra("datum",mFilteredList.get(position).uitgeverij);
                                    intenje.putExtra("klas",mFilteredList.get(position).klas);
                                    intenje.putExtra("departement",mFilteredList.get(position).departement);


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
