package com.example.omar.apvalley;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
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


public class RegisterActivity extends AppCompatActivity implements ItemClickListener,

         NavigationView.OnNavigationItemSelectedListener {

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
    ArrayList<Boeken> list = new ArrayList<>(0);
    ArrayAdapter<Boeken> adapter;

    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //mSearchView=(SearchView) findViewById(R.id.searchView1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // listview = (ListView) findViewById(R.id.listView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference().child(getIntent().getExtras().getString("campus"))
                .child(getIntent().getExtras().getString("richting"));

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Boeken message = dataSnapshot.getValue(Boeken.class);
                    list.add(message);
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
                        Toast.makeText(RegisterActivity.this,
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

        /*
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Boeken message = messageSnapshot.getValue(Boeken.class);

                    list.add(message);
                    mRecyclerView.getAdapter().notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

   */


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // specify an adapter (see also next example)

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.email);
        nav_user.setText(getIntent().getStringExtra("email"));
        navigationView.setNavigationItemSelectedListener(this);




        mAdapter = new MyAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

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
    @Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView view =(SearchView) MenuItemCompat.getActionView(item);
        search(view);

        return true;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent goToSell = new Intent(RegisterActivity.this, SellActivity.class);
            startActivity(goToSell);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mAuth.signOut();
        Intent intentje = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intentje);
    }


    @Override
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
        private ItemClickListener clickListener;
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
            // each data item is just a string in this case
            public TextView mTextView;
            public ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.titel);
                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
                itemView.setTag(itemView);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            }
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<Boeken> myDataset) {
            mDataset = myDataset;
            mFilteredList=myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mFilteredList.get(position).titel);

            Picasso.with(RegisterActivity.this).load(mFilteredList.get(position).foto).into(holder.mImageView);
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
}
