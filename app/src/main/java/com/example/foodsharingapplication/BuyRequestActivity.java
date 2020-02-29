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
    User sender, reciever;
    private ArrayList<BidModel> requestsList = new ArrayList<>();

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
                requestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                        BidModel requestModel = requestSnapshot.getValue(BidModel.class);
                        if (requestModel != null && myId.equals(requestModel.getReciever().getUserId())) {
                            requestsList.add(requestModel);
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
        CustomModelAdapter customModelAdapter = new CustomModelAdapter(requestsList);
        customModelAdapter.setOnClickListener(this);
        rvRequestList.setAdapter(customModelAdapter);
    }

    @Override
    public void onClick(View v) {
        BidModel bidRequest = (BidModel) v.getTag();
        if(bidRequest != null) {
            bidRequest.setAccepted(true);

            DatabaseReference updateRequestReference = FirebaseDatabase.getInstance().getReference("Requests").child(bidRequest.getAdId()).child(bidRequest.getRequestId());
            updateRequestReference.setValue(bidRequest);

            Toast.makeText(this, "Bid Accepted", Toast.LENGTH_LONG).show();
        }
    }
}

// changes