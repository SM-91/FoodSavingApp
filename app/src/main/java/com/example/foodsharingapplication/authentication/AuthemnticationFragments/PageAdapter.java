package com.example.foodsharingapplication.authentication.AuthemnticationFragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    Context context;
    private int countItems;

    public PageAdapter(FragmentManager fragmentManager, int countAllPages){
        super(fragmentManager);

        countItems = countAllPages;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
               Profile profile = new Profile();
                return  profile;
            case 1:
                UpdateProfile updateProfile = new UpdateProfile();
                return updateProfile;

            default:
                 return null;
        }

    }

    @Override
    public int getCount() {
        return countItems;
    }

  /*  @Override
    public int getItemPosition(@NonNull Object object) {
        //RETURN position null because we are assigning own position above at 0, 1st and 2nd respectively
        return POSITION_NONE;
    }*/
}
