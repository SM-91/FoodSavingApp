package com.example.foodsharingapplication.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.Settings;
import com.example.foodsharingapplication.authentication.ShowData;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.example.foodsharingapplication.products.ProductsFragment.ProductListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    RecyclerView myRecyclerView,sRecyclerView;
//    RecyclerView myRecyclerView2;
//    LinearLayout slider_linear_layout;
//    FirebaseDatabase myFirebaseDatabase;
//    DatabaseReference myRef;
//    UploadModel uploadModel

    BottomNavigationView nav_bar;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {

                    Toast.makeText(HomeActivity.this, "Successfully Signed Out: ", Toast.LENGTH_SHORT).show();

                }
            }
        };


        // Navigation Bar Grid View
        nav_bar = findViewById(R.id.bottom_nav_bar);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.grid:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductGridView()).commit();
                        return true;
                    case R.id.add:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductListView()).commit();
                        return true;
                    // ///// ADD more cases for different navigation bar options////////
                    default:
                        return false;
                }
            }
        });

        //Default for all fragments

        if (savedInstanceState==null){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductGridView()).commit();
            navigationView.setCheckedItem(R.id.homeFragment);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductGridView()).commit();
               break;

            case R.id.signOut:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
