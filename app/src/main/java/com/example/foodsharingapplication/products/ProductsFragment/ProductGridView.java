package com.example.foodsharingapplication.products.ProductsFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.PostDetailActivity;
import com.example.foodsharingapplication.products.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductGridView extends Fragment {

    RecyclerView myRecyclerView, sRecyclerView;
    BottomNavigationView nav_bar;
    RecyclerView myRecyclerView2;
    LinearLayout slider_linear_layout;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference myRef;
    UserUploadFoodModel userUploadFoodModel;

    public ProductGridView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_slider, container, false);


        // ///////ACTION BAR////////////
      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today's Menu");*/

        // ///////Recycler View Find by ID//////////////////
        slider_linear_layout = view.findViewById(R.id.sliders_linear_layout);

        myRecyclerView = view.findViewById(R.id.slider_recycler_view);
        myRecyclerView.setHasFixedSize(true);

        myRecyclerView2 = view.findViewById(R.id.slider2_recycler_view);
        myRecyclerView2.setHasFixedSize(true);


        sRecyclerView = view.findViewById(R.id.search_recycler_view);
        sRecyclerView.setHasFixedSize(true);
        LinearLayoutManager LL = new LinearLayoutManager(getContext());
        sRecyclerView.setLayoutManager(LL);


        // ////////Make it Horizontal/////////////

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerView.setLayoutManager(layoutManager);
        myRef = FirebaseDatabase.getInstance().getReference();
        //Database Reference

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerView2.setLayoutManager(layoutManager2);


        return view;
    }

    // /////////Search View Query and Populating View//////////
    private void firebaseSearch(String searchText) {
        String query = searchText;
        Query searchQuery = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers").orderByChild("foodTitle").startAt(query).endAt(query + "\uf0ff");

        FirebaseRecyclerOptions<UserUploadFoodModel> searchOptions =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(searchQuery, UserUploadFoodModel.class).build();

        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(searchOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserUploadFoodModel model) {
                holder.setDetails(getContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewSearch = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                ViewHolder viewHolderS = new ViewHolder(viewSearch);

                // ///////On click handled for Search View to Detail Page/////////////////
                viewHolderS.setOnClickListener(new ViewHolder.ClickListener() {
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

                        // Image setting
                        //String myImage2 = getItem(position).getImage2();
                        String myImage = getItem(position).getmImageUri();
                        HashMap<String, String> hashImage = getItem(position).getHashMap();

                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);

                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("availability", available);
                        intent.putExtra("foodPostedBy",foodPostedBy);


                        // Image Setting
                        //intent.putExtra("image2", myImage2);
                        if (myImage != null) {
                            intent.putExtra("image", myImage);
                        } else if (hashImage != null) {
                            intent.getStringArrayExtra("hashImage");
                        }

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });


                return viewHolderS;
            }
        };
        firebaseRecyclerAdapter.startListening();
        sRecyclerView.setAdapter(firebaseRecyclerAdapter);
        // ///////Search View ends here/////////
    } // firebase Search ends here

    // ///////// Inflating View for Search Bar /////////
    // //////Selecting Setting Options//////
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
/*        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //  //////////// SEARCHING ENDS here//////////

        // //////////////Query and Populating Recycler View 1 ///////////

        Query query = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        FirebaseRecyclerOptions<UserUploadFoodModel> options =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(query, UserUploadFoodModel.class).build();

        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserUploadFoodModel model) {
                holder.setDetails(getContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);

                // ////////// Handling Click on Slider View 1 /////////////////

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myImage = getItem(position).getmImageUri();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        String available = getItem(position).getAvailabilityDays();
                        HashMap<String, String> hashImage = getItem(position).getHashMap();
                        //String myImage2 = getItem(position).getImage2();


                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("image", myImage);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("foodPostedBy",foodPostedBy);
                        intent.putExtra("availability", available);

                        //intent.putExtra("image2", myImage2);
                        if (myImage != null) {
                            intent.putExtra("image", myImage);
                        } else if (hashImage != null) {
                            intent.putExtra("hashImage", hashImage);
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


        // //////////////Query and Populating Slider Recycler View 2 ///////////

        Query query2 = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter2;
        FirebaseRecyclerOptions<UserUploadFoodModel> options2 =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(query2, UserUploadFoodModel.class).build();

        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(options2) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull UserUploadFoodModel model) {
                holder.setDetails(getContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
                holder.itemView.findViewById(R.id.fav).setOnClickListener(new View.OnClickListener() {

                    String title = getItem(position).getFoodTitle();
                    Query q = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers").orderByChild("foodTitle");
                    Query favorites = q.equalTo(title);

                    @Override
                    public void onClick(View v) {
                        //UserUploadFoodModel userUploadFoodModel;
                        if (favorites == null) {

                        }
                        Toast toast = Toast.makeText(getContext(),
                                "This is a message displayed in a Toast",
                                Toast.LENGTH_SHORT);
                        toast.show();

                        ImageButton favImage = v.findViewById(R.id.fav);
                        favImage.getId();
                        favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid, parent, false);

                // ////////// Handling Click on Slider View 2 /////////////////

                ViewHolder viewHolder2 = new ViewHolder(view);
                viewHolder2.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myImage = getItem(position).getmImageUri();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        String available = getItem(position).getAvailabilityDays();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        HashMap<String, String> hashImage = getItem(position).getHashMap();
                        //String myImage2 = getItem(position).getImage2();


                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("image", myImage);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("availability", available);
                        intent.putExtra("foodPostedBy",foodPostedBy);


                        //intent.putExtra("image2", myImage2);
                        if (myImage != null) {
                            intent.putExtra("image", myImage);
                        } else if (hashImage != null) {
                            intent.putExtra("hashImage", hashImage);
                        }

                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast toast = Toast.makeText(getContext(),
                                "This is a message displayed in a Toast",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                return viewHolder2;
            }
        };
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter2.startListening();
        myRecyclerView.setAdapter(firebaseRecyclerAdapter);
        myRecyclerView2.setAdapter(firebaseRecyclerAdapter2);
    }


}
