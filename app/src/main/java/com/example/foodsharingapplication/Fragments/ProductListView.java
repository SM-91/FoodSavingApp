package com.example.foodsharingapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.Adapters.UserUploadedFoodAdapter;
import com.example.foodsharingapplication.PostDetailActivity;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.UserUploadedFood;
import com.example.foodsharingapplication.UserUploadedFoodDetails;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListView extends Fragment implements UserUploadedFoodAdapter.ItemClickListener {

    FirebaseAuth.AuthStateListener firebaseAuthListener;
    BottomNavigationView nav_bar;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    LinearLayoutManager mLL;
    FirebaseAuth mFirebaseAuth;
    private DrawerLayout drawerLayout;
    private static ProductListView productListView;
    private UserUploadedFoodAdapter userUploadedFoodAdapter;

    SearchView searchbar;
    TextView text_search;

    private UserUploadedFoodAdapter adapter;
    private List<UserUploadFoodModel> userSearchList;
    UserUploadFoodModel userUploadFoodModel;

    public ProductListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_home, container, false);

        //Copied Code Start

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.getCurrentUser();

        userSearchList = new ArrayList<>();
        userUploadFoodModel = new UserUploadFoodModel();

        adapter = new UserUploadedFoodAdapter(getContext(), userSearchList);
        adapter.setClickListener(this);
        /*if (!mFirebaseAuth.getCurrentUser().equals(null)) {
        }*/
        // ////////Action Bar////////////
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today's Menu");*/
        searchbar = view.findViewById(R.id.search_bar);
        text_search = view.findViewById(R.id.textview_search_text);

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLL = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLL);

        text_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.onActionViewExpanded();
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });


        return view;
    }

    public static ProductListView getInstance() {
        return productListView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ///////////Query to get Data from Firebase and Populate HomePage///////////

        Query query = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter;
        FirebaseRecyclerOptions<UserUploadFoodModel> options =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(query, UserUploadFoodModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserUploadFoodModel model) {

                if (model.getmImageUri() != null) {
                    holder.setDetails(getContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
                } else {
                    holder.setDetails(getContext(), model.getFoodTitle(), model.getmArrayString().get(0), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
                }
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);

                // //////Handle click on the recycler view/////////////
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        String available = getItem(position).getAvailabilityDays();
                        String mImageUri = getItem(position).getmImageUri();
                        ArrayList<String> imageArray = getItem(position).getmArrayString();

                        String lat = Double.toString(getItem(position).getLatitude());
                        String long1 = Double.toString(getItem(position).getLongitude());

                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);

                        intent.putExtra("ad_id", ad_id);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("availability", available);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lang", long1);

                        if (mImageUri != null) {
                            intent.putExtra("imageUri", mImageUri);
                        } else {
                            intent.putStringArrayListExtra("imageArray", imageArray);
                        }

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                });

                return viewHolder;
            }
        };
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public void search(final String query) {
        text_search.setVisibility(View.GONE);
        searchbar.setMaxWidth(1000);
        DatabaseReference searchRef;
        searchRef = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        searchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userSearchList.clear();
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    UserUploadFoodModel searchedusers = usersSnapshot.getValue(UserUploadFoodModel.class);
                    if (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(searchedusers.getFoodTitle()).find()) {
                        userSearchList.add(searchedusers);
                    }
                }
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        userUploadFoodModel = adapter.getItem(position);
        String FoodAdID = userUploadFoodModel.getAdId();
        String myCurrentDateTime = userUploadFoodModel.getFoodUploadDateAndTime();
        String FoodTitle = userUploadFoodModel.getFoodTitle();
        String FoodType = userUploadFoodModel.getFoodType();
        String FoodCuisineType = userUploadFoodModel.getFoodTypeCuisine();
        String FoodDescription = userUploadFoodModel.getFoodDescription();
        String FoodPrice = userUploadFoodModel.getFoodPrice();
        String FoodPickUpDetail = userUploadFoodModel.getFoodPickUpDetail();
        String FoodAvailability = userUploadFoodModel.getAvailabilityDays();
        String FoodPayment = userUploadFoodModel.getPayment();
        String FoodSingleImage = userUploadFoodModel.getmImageUri();
        User postedBy = userUploadFoodModel.getFoodPostedBy();
        ArrayList<String> FoodMultipleImages = userUploadFoodModel.getmArrayString();


        String lat = Double.toString(userUploadFoodModel.getLatitude());
        String long1 = Double.toString(userUploadFoodModel.getLongitude());



        intent.putExtra("myCurrentDateTime", myCurrentDateTime);
        intent.putExtra("ad_id", FoodAdID);
        intent.putExtra("title", FoodTitle);
        intent.putExtra("type", FoodType);
        intent.putExtra("cuisineType", FoodCuisineType);
        intent.putExtra("description", FoodDescription);
        intent.putExtra("price", FoodPrice);
        intent.putExtra("time", FoodPickUpDetail);
        intent.putExtra("availability", FoodAvailability);
        intent.putExtra("pay", FoodPayment);
        intent.putExtra("foodPostedBy", postedBy);
        intent.putExtra("imageUri", FoodSingleImage);
        intent.putExtra("imageArray", FoodMultipleImages);
        intent.putExtra("lat", lat);
        intent.putExtra("lang", long1);

        startActivity(intent);

    }
}
