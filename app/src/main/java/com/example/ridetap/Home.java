package com.example.ridetap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridetap.adapter.AddcarAdapter;
import com.example.ridetap.model.Cars;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity implements AddcarAdapter.CarsAdapterListener {
    private static final String TAG = Home.class.getSimpleName();
private TextView tvsignin,tvsignup;
private FloatingActionButton fabaddcar;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ProgressDialog mProgressDialog;
    RecyclerView recycler_cars;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    private AddcarAdapter addcarAdapter;
    private List<Cars> carsList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolBar;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      toolBar = findViewById(R.id.toolbar);
     setSupportActionBar(toolBar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

//        tvsignin=findViewById(R.id.tvsignin);
//        tvsignup=findViewById(R.id.tvsignup);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        fabaddcar=findViewById(R.id.fabaddcar);
        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser().getEmail().equals("josephmbera124@gmail.com")){
//            fabaddcar.setVisibility(View.VISIBLE);
//        }else {
//            fabaddcar.setVisibility(View.INVISIBLE);
//        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code here

                Toast.makeText(getApplicationContext(), "Refreshing!", Toast.LENGTH_SHORT).show();

                // To keep animation for 4 seconds

                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {

                        // Stop animation (This will be after 3 seconds)

                        swipeRefreshLayout.setRefreshing(false);

                    }

                }, 4000); // Delay in millis

            }

        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);





        if(carsList.size() >0){
            carsList.clear();
        }
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Addcar");
        firebaseAuth = FirebaseAuth.getInstance();


//        if(firebaseAuth.getCurrentUser().getEmail().equals("josephmbera124@gmail.com")){
//             fabaddcar.setVisibility(View.GONE);
//        }else {
//            fabaddcar.setVisibility(View.VISIBLE);
//        }
        mAuthListener = firebaseAuth -> {
            FirebaseAuth user = FirebaseAuth.getInstance();
            if(!user.getCurrentUser().getEmail().equals("josephmbera124@gmail.com")){
                fabaddcar.setVisibility(View.VISIBLE);
            }else {
                fabaddcar.setVisibility(View.INVISIBLE);
            }
            if(carsList.size() >0){
            carsList.clear();
        }
        };

//        tvsignin.setOnClickListener(v -> {
//            startActivity(new Intent(Home.this, SignIn.class));
//
//        });
//        tvsignup.setOnClickListener(v -> {
//            startActivity(new Intent(Home.this, SignUp.class));
//                   });
        fabaddcar.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddCar.class));
            finish();
        });



        addcarAdapter= new AddcarAdapter(this, carsList);

        //load menu
        recycler_cars= findViewById(R.id.recycler_cars);
        addcarAdapter = new AddcarAdapter(this, carsList);
        recycler_cars.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
        recycler_cars.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_cars.setLayoutManager(staggeredGridLayoutManager);
        recycler_cars.setAdapter(addcarAdapter);
        loadCars();


    }

    private void loadCars() {
        showProgressDialog();
        if(carsList.size() >0){
            carsList.clear();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot mdataSnapshot : dataSnapshot.getChildren()){
                        Cars cars = mdataSnapshot.getValue(Cars.class);
                        carsList.add(cars);
                    }


                }
                //Toast.makeText(Home.this, "" + categoryList.size(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                addcarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }


    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
             addcarAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                addcarAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_refresh) {

            startActivity(new Intent(Home.this, SignIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onCarsSelected(Cars cars) {
        Toast.makeText(getApplicationContext(), "Selected: " + cars.getLocation() + ", " + cars.getContact(), Toast.LENGTH_LONG).show();
    }
}
