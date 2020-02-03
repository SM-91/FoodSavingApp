package com.example.foodsharingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.foodsharingapplication.Adapters.CustomModelAdapter;
import com.example.foodsharingapplication.model.BidModel;
import com.example.foodsharingapplication.model.CustomModel;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvRequestList;
    private List<User> otherUserList = new ArrayList<>();
    private ArrayList<CustomModel> customModelArrayList = new ArrayList<>();
    private Map<String, Boolean> otherUserMap = new HashMap<>();
    private String myId;
    private String requestId;
    private String ad_id = " ";
    private String name;
    private boolean accepted;
    User sender,reciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_request);

        myId = FirebaseAuth.getInstance().getUid();
        rvRequestList = findViewById(R.id.rvRequestList);
        rvRequestList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference getProductAdIdReference = FirebaseDatabase.getInstance().getReference("Requests");
        //Show Progress Dialog
        getProductAdIdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //otherUserMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productId = snapshot.getKey();
                    if (!TextUtils.isEmpty(productId)) {
                        ad_id = productId;
                        getProduct();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getProduct() {
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers")
                .child(ad_id);
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserUploadFoodModel productModel = dataSnapshot.getValue(UserUploadFoodModel.class);
                if(productModel != null) {
                    getUser(productModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUser(final UserUploadFoodModel productModel) {
        DatabaseReference allRequestsReference = FirebaseDatabase.getInstance().getReference("Requests").child(ad_id);
        //Show Progress Dialog
        allRequestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //otherUserList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BidModel bidModel = snapshot.getValue(BidModel.class);
                    User otherUser = null;

                    sender = bidModel.getSender();
                    reciever = bidModel.getReciever();
                    accepted = bidModel.isAccepted();
                    name = bidModel.getName();
                    requestId = bidModel.getRequestId();

                    if (myId.equals(bidModel.getReciever().getUserId())) {
                        otherUser = bidModel.getSender();
                    }

                    if (otherUser != null) {
                        if (!otherUserMap.containsKey(otherUser.getUserId())) {
                            otherUserMap.put(otherUser.getUserId(), true);
                            otherUserList.add(otherUser);

                            CustomModel customModel = new CustomModel();
                            customModel.setUser(otherUser);
                            customModel.setBidModel(bidModel);
                            customModel.setUserUploadFoodModel(productModel);
                            customModelArrayList.add(customModel);
                        }
                    }
                }
                setRVAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setRVAdapter() {
        CustomModelAdapter customModelAdapter = new CustomModelAdapter(customModelArrayList);
        customModelAdapter.setOnClickListener(this);
        rvRequestList.setAdapter(customModelAdapter);
        customModelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        DatabaseReference setAcceptedReference;
        setAcceptedReference = FirebaseDatabase.getInstance().getReference("Requests")
                .child(ad_id);

        BidModel bidModel = new BidModel();
        bidModel.setAdId(ad_id);
        bidModel.setAccepted(true);
        bidModel.setSender(sender);
        bidModel.setReciever(reciever);
        bidModel.setName(name);
        bidModel.setRequestId(requestId);
        setAcceptedReference.child(requestId).setValue(bidModel);

        Toast.makeText(this,"Bid Accepted",Toast.LENGTH_LONG).show();
    }
}
