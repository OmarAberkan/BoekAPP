package com.example.omar.apvalley;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
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
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //mSearchView=(SearchView) findViewById(R.id.searchView1);
        mDatabase =FirebaseDatabase.getInstance().getReference();
        // Toast.makeText(getApplicationContext(),mAuth.getUid(), Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.nav_boek:
                                setTitle("Koop");
                                KoopFragment fragment1= new KoopFragment();
                                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction1.replace(R.id.frame,fragment1,"FragmentName");
                                fragmentTransaction1.commit();
                                return true;
                            case R.id.history:
                                setTitle("Bestelhistoriek");
                                HistoriekFragment fragment5= new HistoriekFragment();
                                FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction5.replace(R.id.frame,fragment5,"FragmentName");
                                fragmentTransaction5.commit();
                                return true;
                            case R.id.nav_camera:
                                setTitle("Mijn boeken");
                                MijnBoekenFragment fragment2= new MijnBoekenFragment();
                                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.frame,fragment2,"FragmentName");
                                fragmentTransaction2.commit();
                                return true;
                            case R.id.nav_slideshnw:
                                setTitle("Winkelmandje");
                                WinkelmandjeFragment fragment3= new WinkelmandjeFragment();
                                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction3.replace(R.id.frame,fragment3,"FragmentName");
                                fragmentTransaction3.commit();
                                return  true;
                            case R.id.nav_send:

                                return true;
                        }
                        return false;
                    }
                });



        setTitle("Koop");
        KoopFragment fragment1= new KoopFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame,fragment1,"FragmentName");
        fragmentTransaction1.commit();




/*
     mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        dref = FirebaseDatabase.getInstance().getReference().child(getIntent().getExtras().getString("campus"))
                .child(getIntent().getExtras().getString("richting")).child(getIntent().getExtras().getString("klas"));

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
                        Toast.makeText(RegisterActivity.this,
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


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

*/
        setSupportActionBar(toolbar);








     //   mAdapter = new MyAdapter(list,getApplicationContext());
       // mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.setClickListener(this);

   /*     Spinner spin = (Spinner) findViewById(R.id.richtingen);
       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               Toast.makeText(RegisterActivity.this,
                       adapterView.getItemAtPosition(i).toString(),
                       Toast.LENGTH_SHORT).show();
               mAdapter
                       .filter((adapterView.getItemAtPosition(i).toString()));

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });


    /*    listview.setAdapter(new PlayerAdapter(this,list));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boeken boek =(Boeken)parent.getItemAtPosition(position);
                Intent intent = new Intent(RegisterActivity.this,BestelActivity.class);
                intent.putExtra("Titel",boek.titel);
                intent.putExtra("Auteur",boek.auteur);
                intent.putExtra("Datum",boek.datum);
                intent.putExtra("Foto",boek.foto);
                intent.putExtra("ISBN",boek.ISBN);
                intent.putExtra("Uitgeverij",boek.uitgeverij);
                intent.putExtra("userId",boek.userId);
                intent.putExtra("Prijs",boek.prijs);
                startActivity(intent);




            }
        });
        listview.setTextFilterEnabled(true);
        setupSearchView();
        /*listview.setAdapter(new PlayerAdapter(this,list));
        final FirebaseListAdapter<Boeken> adapter = new FirebaseListAdapter<Boeken>(options)  {
            @Override
            protected void populateView(View v, Boeken model, int position) {
                // Bind the Chat to the view

                ArrayList<Boeken> adaptertje = new ArrayList<>(Arrays.asList(model));
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                Picasso.with(RegisterActivity.this).load(model.foto.toString()).into(imageView);
                ((TextView) v.findViewById(R.id.titel)).setText(model.titel.toString());



            }
        };



        lijst.setAdapter(adapter);




        });


      /*  lijst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boeken boek =(Boeken)parent.getItemAtPosition(position);
                Intent intent = new Intent(RegisterActivity.this,BestelActivity.class);
                intent.putExtra("Titel",boek.titel);
                intent.putExtra("Auteir",boek.auteur);
                intent.putExtra("Datum",boek.datum);
                intent.putExtra("Foto",boek.foto);
                intent.putExtra("ISBN",boek.ISBN);
                intent.putExtra("Uitgeverij",boek.uitgeverij);
                intent.putExtra("userId",boek.userId);
                intent.putExtra("Prijs",boek.prijs);
                startActivity(intent);




            }
        });

*/

    }
  /*  @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }


 /*   @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/












  /*   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


   /* class PlayerAdapter extends BaseAdapter implements Filterable {
        public ArrayList<Boeken> orig;
        public  Context context;
        ArrayList<Boeken> boek;
        public PlayerAdapter(Context context, ArrayList<Boeken> boek) {
            super();
            this.context = context;
            this.boek = boek;
        }
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<Boeken> results = new ArrayList<Boeken>();
                    if (orig == null)
                        orig = boek;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final Boeken g : orig) {
                                if (g.titel.toLowerCase()
                                        .contains(constraint.toString()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    boek = (ArrayList<Boeken>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return boek.size();
        }

        @Override
        public Object getItem(int position) {
            return boek.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView != null) {
                Log.i("Adapter", "View already exists. Reuse!");
                view = convertView;

            } else {
                Log.i("Adapter", "New View!");
                view = getLayoutInflater().inflate(R.layout.list_item, parent, false);

            }

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(RegisterActivity.this).load(boek.get(position).foto).into(imageView);
            ((TextView) view.findViewById(R.id.titel)).setText(boek.get(position).titel);
            return view;


        }

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
            public TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);

                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                mTextView = (TextView) itemView.findViewById(R.id.titel);
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

            Picasso.with(RegisterActivity.this).load(mFilteredList.get(position).foto).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intenje = new Intent(mCtx,ListActivity.class);
                    intenje.putExtra("titel",mFilteredList.get(position).titel);
                    intenje.putExtra("auteur",mFilteredList.get(position).auteur);
                    intenje.putExtra("uitgeverij",mFilteredList.get(position).datum);
                    intenje.putExtra("isbn",mFilteredList.get(position).ISBN);
                    intenje.putExtra("foto",mFilteredList.get(position).foto);
                    intenje.putExtra("richting",mFilteredList.get(position).richting);
                   // intenje.putExtra("prijs",mFilteredList.get(position).prijs.toString());
                    intenje.putExtra("datum",mFilteredList.get(position).uitgeverij);
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
