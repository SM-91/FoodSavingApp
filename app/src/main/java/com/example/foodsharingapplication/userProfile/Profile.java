package com.example.foodsharingapplication.userProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Profile extends RecyclerView.Adapter<Profile.UserProfileViewHolder>{
    private Context mContext;
    private List<User> mUsersList;
    private String userNam;
    private String userEmail;
    private String userPassword;
    private String userCountry;
    private String userDoB;
    private String userGender;
    private String userProfileUrl;
    private String userPhoneNumber;
    User users;
    public Profile(Context context,
                   String userNam,
                   String userEmail,
                   String userPassword,
                   String userCountry,
                   String userDoB,
                   String userGender,
                   String userProfileUrl,
                   String userPhoneNumber){
    //public Profile(Context context,User user){

        mContext = context;
        //users =user;
        //mUsersList = users;
        this.userNam=userNam;
        this.userEmail=userEmail;
        this.userPassword= userPassword;
        this.userCountry=userCountry;
        this.userDoB= userDoB;
        this.userGender=userGender;
        this.userProfileUrl=userProfileUrl;
        this.userPhoneNumber=userPhoneNumber;
       /* users.setUserName(userNam);
        users.setUserEmail(userEmail);
        users.setUserPassword(userPassword);
        users.setUserCountry(userCountry);
        users.setUserDateOfBirth(userDoB);
        users.setSex(userGender);
        users.setUserProfilePicUrl(userProfileUrl);
        users.setUserPhoneNumber(userPhoneNumber);*/


    }
    @NonNull
    @Override
    public UserProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.profile_items, parent, false);
        return new UserProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileViewHolder holder, int position) {
        User currentUser= new User();
        holder.name.setText(userNam);
        //holder.name.setText(currentUser.getUserName());
        holder.email.setText(userEmail);
        holder.gender.setText(userGender);
        holder.country.setText(userCountry);
        holder.phoneNumber.setText(userPhoneNumber);
        Picasso.get().load(userProfileUrl).fit().centerCrop().into(holder.profileImgView);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class UserProfileViewHolder extends RecyclerView.ViewHolder{
        private ImageView profileImgView;
        private TextView phoneNumber;
        private TextView email;
        private TextView name;
        private TextView country;
        private TextView gender;

        public UserProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImgView = itemView.findViewById(R.id.userProfileImage);
            phoneNumber = itemView.findViewById(R.id.userPhoneNumber);
            email = itemView.findViewById(R.id.userEmail);
            name = itemView.findViewById(R.id.userName);
            country = itemView.findViewById(R.id.userCountry);
            gender = itemView.findViewById(R.id.userGender);
        }
    }


}
